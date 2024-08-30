package cn.iocoder.yudao.module.spider.service.loalieren.entity;

import lombok.Data;

/**
 * @author by huliehao
 * Date: 2024/8/12
 * Description: No Description
 */
@Data
public class Game {

    private Long id;

    private String title;

    private String price;

    private Integer inside_diff;

    private Integer outside_diff;
}
