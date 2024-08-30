package cn.iocoder.yudao.module.spider.service.loalieren;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.spider.dal.dataobject.SpiderGamesDO;
import cn.iocoder.yudao.module.spider.dal.dataobject.SpiderGamesPriceRecordDO;
import cn.iocoder.yudao.module.spider.dal.dataobject.SpiderSellerDO;
import cn.iocoder.yudao.module.spider.dal.dataobject.SpiderSellerGamesDO;
import cn.iocoder.yudao.module.spider.dal.mysql.SpiderGamesMapper;
import cn.iocoder.yudao.module.spider.dal.mysql.SpiderGamesPriceRecordMapper;
import cn.iocoder.yudao.module.spider.dal.mysql.SpiderSellerGamesMapper;
import cn.iocoder.yudao.module.spider.dal.mysql.SpiderSellerMapper;
import cn.iocoder.yudao.module.spider.service.loalieren.entity.Game;
import cn.iocoder.yudao.module.spider.service.loalieren.entity.Response;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by huliehao
 * Date: 2024/8/7
 * Description: No Description
 */
@Slf4j
@Service
public class LaoLieRenService {

    private final Gson gson = new GsonBuilder().create();

    @Resource
    private SpiderGamesMapper spiderGamesMapper;

    @Resource
    private SpiderSellerMapper spiderSellerMapper;

    @Resource
    private SpiderSellerGamesMapper sellerGamesMapper;

    @Resource
    private SpiderGamesPriceRecordMapper gamesPriceRecordMapper;

    public void spiderRun() throws Exception {
        List<SpiderSellerGamesDO> sellerGames = sellerGamesMapper.selectList();
        for (SpiderSellerGamesDO sellerGame : sellerGames) {

            String spiderId = sellerGame.getSpiderId();
            String keywords = sellerGame.getSpiderKeywords();

            Game spiderGame = httpSpider(spiderId, keywords);
            if (spiderGame == null) {
                log.error("抓取失败 keywords: {}", keywords);
                continue;
            }

            SpiderGamesDO game = spiderGamesMapper.selectById(sellerGame.getGamesId());
            SpiderSellerDO seller = spiderSellerMapper.selectById(sellerGame.getSellerId());

            String captureDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LambdaQueryWrapper<SpiderGamesPriceRecordDO> queryWrapper = new LambdaQueryWrapperX<SpiderGamesPriceRecordDO>()
                    .eq(SpiderGamesPriceRecordDO::getSellerGamesId, sellerGame.getId())
                    .eq(SpiderGamesPriceRecordDO::getCaptureDate, captureDate);

            SpiderGamesPriceRecordDO priceRecord = gamesPriceRecordMapper.selectOne(queryWrapper);
            if (priceRecord == null) {
                //当天已抓取价格，则直接更新
                priceRecord = new SpiderGamesPriceRecordDO();
                priceRecord.setGamesId(game.getId());
                priceRecord.setGamesName(game.getName());
                priceRecord.setSellerId(seller.getId());
                priceRecord.setSellerName(seller.getName());
                priceRecord.setSellerGamesId(sellerGame.getId());
            }

            priceRecord.setCaptureDate(captureDate);
            BigDecimal price = BigDecimal.valueOf(NumberUtils.toDouble(spiderGame.getPrice()));
            priceRecord.setPrice(price.doubleValue());
            priceRecord.setInsideDiff(spiderGame.getInside_diff());
            priceRecord.setOutsideDiff(spiderGame.getOutside_diff());

            if (priceRecord.getId() != null) {
                gamesPriceRecordMapper.updateById(priceRecord);
            } else {
                gamesPriceRecordMapper.insert(priceRecord);
            }

            Thread.sleep(5000);
        }
    }

    private Game httpSpider(String spiderId, String keywords) {
        String url = "https://api.laolieren.com/v2/game/home";

        Map<String, String> headers = new HashMap<>();
        headers.put("Host", "api.laolieren.com");
        headers.put("content-type", "application/json");
        headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 16_0_2 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) Mobile/15E148 MicroMessenger/8.0.50(0x18003231) NetType/WIFI Language/zh_CN");
        headers.put("Referer", "https://servicewechat.com/wxa50e5fd9fc4f0b66/159/page-frame.html");

        Map<String, Object> params = new HashMap<>();
        params.put("auth", "");
        params.put("page", 1);
        params.put("app", "weixin");

        Map<String, String> filter = new HashMap<>();
        filter.put("platform", "switch");
        filter.put("keyword", keywords);
        filter.put("listorder", "");
        filter.put("favorite", "");
        filter.put("genres", "");
        filter.put("preset", "");

        params.put("filter", filter);

        String jsonParams = new GsonBuilder().create().toJson(params);
        String result = doPost(url, jsonParams, headers);
        log.info("游戏抓取结果 params: {} result: {}", jsonParams, result);
        Response games = gson.fromJson(result, Response.class);
        if (games.getTotal() == 0) {
            log.error("未抓取到商品 keyword: {}", keywords);
            return null;
        }

        Game spiderGame = games.getRows().stream()
                .filter(item -> item.getId() == NumberUtils.toLong(spiderId)).findFirst().orElse(null);
        if (spiderGame == null) {
            log.error("未匹配到商品 keyword: {}", keywords);
            return null;
        }
        return spiderGame;
    }

    private static String doPost(String url, String jsonParams, Map<String, String> headers) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {

            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setSocketTimeout(1000).build();
            httpPost.setConfig(requestConfig);

            StringEntity stringEntity = new StringEntity(jsonParams, ContentType.APPLICATION_JSON);
            stringEntity.setContentEncoding("UTF-8");
            httpPost.setEntity(stringEntity);

            for (String head : headers.keySet()) {
                httpPost.addHeader(head, headers.get(head));
            }

            response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();
            if (HttpStatus.SC_OK != statusCode) {
                httpPost.abort();
                throw new RuntimeException("HttpClient error status code :" + statusCode);
            }

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                result = StringEscapeUtils.unescapeJava(result);
            }

            EntityUtils.consume(entity);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

}
