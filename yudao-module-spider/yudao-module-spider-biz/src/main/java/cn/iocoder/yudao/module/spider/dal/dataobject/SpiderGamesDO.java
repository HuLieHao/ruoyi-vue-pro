package cn.iocoder.yudao.module.spider.dal.dataobject;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author by huliehao
 * Date: 2024/7/17
 * Description: No Description
 */
@TableName("spider_games")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpiderGamesDO extends BaseDO {

    private Long id;

    private String name;

}
