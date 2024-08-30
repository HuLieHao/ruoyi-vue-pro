package cn.iocoder.yudao.module.spider.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author by huliehao
 * Date: 2024/8/8
 * Description: No Description
 */
@TableName("spider_seller_games")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpiderSellerGamesDO extends BaseDO {

    private Long id;

    private Long gamesId;

    private Long sellerId;

    private String spiderId;

    private String spiderKeywords;
}
