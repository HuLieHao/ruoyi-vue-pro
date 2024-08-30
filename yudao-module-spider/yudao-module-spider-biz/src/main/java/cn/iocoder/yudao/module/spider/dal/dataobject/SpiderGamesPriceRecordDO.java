package cn.iocoder.yudao.module.spider.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author by huliehao
 * Date: 2024/8/8
 * Description: No Description
 */
@TableName("spider_games_price_record")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpiderGamesPriceRecordDO extends BaseDO {

    private Long id;

    private Long gamesId;

    private String gamesName;

    private Long sellerId;

    private String sellerName;

    private Long sellerGamesId;

    private Double price;

    private Integer insideDiff;

    private Integer outsideDiff;

    /**
     * 抓取日期
     */
    private String captureDate;
}
