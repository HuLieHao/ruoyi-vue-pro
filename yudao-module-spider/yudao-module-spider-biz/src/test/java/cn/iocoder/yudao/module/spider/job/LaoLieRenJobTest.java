package cn.iocoder.yudao.module.spider.job;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;
import cn.iocoder.yudao.module.spider.service.loalieren.LaoLieRenService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import javax.annotation.Resource;

/**
 * @author by huliehao
 * Date: 2024/8/12
 * Description: No Description
 */
@Import(LaoLieRenJob.class)
public class LaoLieRenJobTest extends BaseDbUnitTest {

    @Resource
    private LaoLieRenJob lieRenJob;

    @MockBean
    private LaoLieRenService laoLieRenService;

    @Test
    public void testJob() throws Exception {
        lieRenJob.execute("");
    }
}
