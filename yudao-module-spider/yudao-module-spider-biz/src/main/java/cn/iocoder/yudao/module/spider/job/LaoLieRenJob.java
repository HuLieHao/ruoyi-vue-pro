package cn.iocoder.yudao.module.spider.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.spider.service.loalieren.LaoLieRenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author by huliehao
 * Date: 2024/8/12
 * Description: No Description
 */
@Slf4j
@Component
public class LaoLieRenJob implements JobHandler {

    @Resource
    private LaoLieRenService laoLieRenService;

    @Override
    public String execute(String param) throws Exception {
        try {
            laoLieRenService.spiderRun();
            return "抓取成功";
        } catch (Exception e) {
            log.error("抓取失败", e);
            return "抓取失败";
        }
    }
}
