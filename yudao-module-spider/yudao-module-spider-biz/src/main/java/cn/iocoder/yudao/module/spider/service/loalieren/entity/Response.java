package cn.iocoder.yudao.module.spider.service.loalieren.entity;

import lombok.Data;

import java.util.List;

/**
 * @author by huliehao
 * Date: 2024/8/12
 * Description: No Description
 */
@Data
public class Response {

    private Integer status;

    private List<Game> rows;

    private Integer total;
}
