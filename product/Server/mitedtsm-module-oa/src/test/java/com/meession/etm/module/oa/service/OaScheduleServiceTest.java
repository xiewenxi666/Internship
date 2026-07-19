package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.dal.mysql.OaScheduleMapper;
import com.meession.etm.module.oa.service.schedule.OaScheduleServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_SCHEDULE_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaScheduleServiceImpl.class)
public class OaScheduleServiceTest extends BaseDbUnitTest {

    @Resource
    private OaScheduleServiceImpl scheduleService;

    @Resource
    private OaScheduleMapper scheduleMapper;

    @Test
    public void testCreateSchedule_success() {
        OaScheduleCreateReqVO reqVO = randomPojo(OaScheduleCreateReqVO.class, o -> {
            o.setStartTime(LocalDateTime.now());
            o.setEndTime(LocalDateTime.now().plusHours(2));
            o.setPriority(1);
        });
        Long userId = randomLongId();

        Long scheduleId = scheduleService.createSchedule(userId, reqVO);

        assertNotNull(scheduleId);
        OaScheduleDO schedule = scheduleMapper.selectById(scheduleId);
        assertEquals(userId, schedule.getUserId());
        assertEquals(0, schedule.getStatus());
    }

    @Test
    public void testUpdateSchedule_success() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class, o -> {
            o.setStatus(0);
            o.setPriority(1);
            o.setStartTime(LocalDateTime.now());
            o.setEndTime(LocalDateTime.now().plusHours(1));
        });
        scheduleMapper.insert(dbSchedule);

        OaScheduleCreateReqVO updateReqVO = randomPojo(OaScheduleCreateReqVO.class, o -> {
            o.setStartTime(LocalDateTime.now().plusDays(1));
            o.setEndTime(LocalDateTime.now().plusDays(1).plusHours(2));
            o.setPriority(1);
        });

        scheduleService.updateSchedule(dbSchedule.getId(), updateReqVO);

        OaScheduleDO updated = scheduleMapper.selectById(dbSchedule.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId",
                "startTime", "endTime");
    }

    @Test
    public void testGetScheduleCalendarList() {
        Long userId = randomLongId();
        LocalDateTime base = LocalDateTime.of(2026, 7, 15, 0, 0);
        scheduleMapper.insert(randomPojo(OaScheduleDO.class, o -> {
            o.setUserId(userId);
            o.setPriority(1);
            o.setStartTime(base.plusDays(1));
            o.setEndTime(base.plusDays(1).plusHours(1));
        }));
        scheduleMapper.insert(randomPojo(OaScheduleDO.class, o -> {
            o.setUserId(userId);
            o.setPriority(1);
            o.setStartTime(base.plusDays(3));
            o.setEndTime(base.plusDays(3).plusHours(1));
        }));

        List<OaScheduleDO> list = scheduleService.getScheduleCalendarList(
                userId, base, base.plusDays(7), null);

        assertEquals(2, list.size());
    }

    @Test
    public void testGetScheduleCalendarList_withTypeFilter() {
        Long userId = randomLongId();
        LocalDateTime base = LocalDateTime.of(2026, 7, 15, 0, 0);
        scheduleMapper.insert(randomPojo(OaScheduleDO.class, o -> {
            o.setUserId(userId);
            o.setType(1);
            o.setPriority(1);
            o.setStartTime(base);
            o.setEndTime(base.plusHours(1));
        }));
        scheduleMapper.insert(randomPojo(OaScheduleDO.class, o -> {
            o.setUserId(userId);
            o.setType(2);
            o.setPriority(1);
            o.setStartTime(base);
            o.setEndTime(base.plusHours(1));
        }));

        List<OaScheduleDO> list = scheduleService.getScheduleCalendarList(
                userId, base, base.plusDays(1), 1);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0).getType());
    }

    @Test
    public void testDeleteSchedule_success() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class, o -> {
            o.setPriority(1);
        });
        scheduleMapper.insert(dbSchedule);

        scheduleService.deleteSchedule(dbSchedule.getId());

        assertNull(scheduleMapper.selectById(dbSchedule.getId()));
    }

    @Test
    public void testUpdateScheduleStatus() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class, o -> {
            o.setStatus(0);
            o.setPriority(1);
        });
        scheduleMapper.insert(dbSchedule);

        scheduleService.updateScheduleStatus(dbSchedule.getId(), 2);

        OaScheduleDO updated = scheduleMapper.selectById(dbSchedule.getId());
        assertEquals(2, (int) updated.getStatus());
    }
}
