package cn.iocoder.yudao.module.spider.service;

import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringEscapeUtils;
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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by huliehao
 * Date: 2024/7/17
 * Description: No Description
 */
public class SpiderService {

    public static void main(String[] args) {
        test();
    }

    public static void test() {

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
        filter.put("keyword", "我的世界 基岩版");
        filter.put("listorder", "");
        filter.put("favorite", "");
        filter.put("genres", "");
        filter.put("preset", "");

        params.put("filter", filter);

        String jsonParams = new GsonBuilder().create().toJson(params);
        String result = doPost(url, jsonParams, headers);
        System.out.println(result);
    }

    private static String doPost(String url, String jsonParams, Map<String, String> headers) {
        String result = null;
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        try {

            HttpPost httpPost = new HttpPost(url);

            RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setSocketTimeout(1000).build();
            httpPost.setConfig(requestConfig);

//            List<BasicNameValuePair> pairList = new ArrayList<>();
//            for (String key : params.keySet()) {
//                pairList.add(new BasicNameValuePair(key, params.get(key)));
//            }
//            UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(pairList, HTTP.UTF_8);
//            httpPost.setEntity(urlEncodedFormEntity);

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
