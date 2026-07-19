package com.meession.etm.module.oa.job;

import com.meession.etm.framework.quartz.core.handler.JobHandler;
import com.meession.etm.framework.tenant.core.job.TenantJob;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.dal.mysql.OaScheduleMapper;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class OaScheduleRemindJob implements JobHandler {

    @Resource
    private OaScheduleMapper scheduleMapper;

    @Override
    @TenantJob
    public String execute(String param) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime maxEnd = now.plusDays(1);
        List<OaScheduleDO> schedules = scheduleMapper.selectList(new LambdaQueryWrapperX<OaScheduleDO>()
                .le(OaScheduleDO::getStartTime, maxEnd)
                .ge(OaScheduleDO::getStartTime, now));

        int remindCount = 0;
        for (OaScheduleDO schedule : schedules) {
            if (schedule.getReminderTime() == null) {
                continue;
            }
            LocalDateTime remindAt = schedule.getStartTime().minusMinutes(schedule.getReminderTime());
            if (!remindAt.isAfter(now)) {
                remindCount++;
            }
        }
        return "日程提醒任务执行完成，共处理 " + schedules.size() + " 个日程，其中 " + remindCount + " 个需要提醒";
    }

}
