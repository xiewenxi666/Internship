package com.meession.etm.module.oa.job;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.dal.mysql.OaScheduleMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class OaScheduleRemindJobTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaScheduleRemindJob job;

    @Mock
    private OaScheduleMapper scheduleMapper;

    @Test
    void testExecute_withNoSchedules() throws Exception {
        when(scheduleMapper.selectList(any())).thenReturn(List.of());
        String result = job.execute("testParam");
        assertTrue(result.contains("共处理 0 个日程"));
    }

    @Test
    void testExecute_withSchedules() throws Exception {
        OaScheduleDO schedule = OaScheduleDO.builder()
                .id(1L)
                .userId(100L)
                .title("测试日程")
                .startTime(LocalDateTime.now().plusHours(1))
                .reminderTime(30)
                .build();
        when(scheduleMapper.selectList(any())).thenReturn(List.of(schedule));
        String result = job.execute("testParam");
        assertTrue(result.contains("共处理 1 个日程"));
    }

}
