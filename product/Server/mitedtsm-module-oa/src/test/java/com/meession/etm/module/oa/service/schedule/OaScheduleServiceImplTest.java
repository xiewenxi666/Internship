package com.meession.etm.module.oa.service.schedule;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.dal.mapper.OaScheduleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_SCHEDULE_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaScheduleServiceImpl.class)
class OaScheduleServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OaScheduleServiceImpl scheduleService;

    @Resource
    private OaScheduleMapper scheduleMapper;

    @Test
    void testCreateSchedule_success() {
        OaScheduleCreateReqVO reqVO = randomPojo(OaScheduleCreateReqVO.class);
        Long userId = randomLongId();

        Long id = scheduleService.createSchedule(userId, reqVO);

        assertNotNull(id);
        OaScheduleDO schedule = scheduleMapper.selectById(id);
        assertNotNull(schedule);
        assertEquals(userId, schedule.getUserId());
    }

    @Test
    void testGetSchedule_success() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class);
        scheduleMapper.insert(dbSchedule);

        OaScheduleDO schedule = scheduleService.getSchedule(dbSchedule.getId());

        assertNotNull(schedule);
        assertPojoEquals(dbSchedule, schedule);
    }

    @Test
    void testGetSchedule_notExists() {
        assertNull(scheduleService.getSchedule(randomLongId()));
    }

    @Test
    void testGetSchedulePage_success() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class, o -> o.setTitle("Meeting"));
        scheduleMapper.insert(dbSchedule);
        OaScheduleDO other = new OaScheduleDO();
        other.setUserId(dbSchedule.getUserId());
        other.setTitle("Other");
        other.setStartTime(dbSchedule.getStartTime());
        other.setEndTime(dbSchedule.getEndTime());
        scheduleMapper.insert(other);
        OaScheduleDO otherUser = randomPojo(OaScheduleDO.class);
        scheduleMapper.insert(otherUser);

        OaSchedulePageReqVO reqVO = new OaSchedulePageReqVO();
        reqVO.setTitle("Meeting");

        PageResult<OaScheduleDO> result = scheduleService.getSchedulePage(dbSchedule.getUserId(), reqVO);

        assertEquals(1, result.getTotal());
        assertPojoEquals(dbSchedule, result.getList().get(0));
    }

    @Test
    void testGetSchedulePage_noFilter() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class);
        scheduleMapper.insert(dbSchedule);
        OaScheduleDO copy = new OaScheduleDO();
        copy.setUserId(dbSchedule.getUserId());
        copy.setTitle(dbSchedule.getTitle());
        copy.setStartTime(dbSchedule.getStartTime());
        copy.setEndTime(dbSchedule.getEndTime());
        scheduleMapper.insert(copy);

        OaSchedulePageReqVO reqVO = new OaSchedulePageReqVO();

        PageResult<OaScheduleDO> result = scheduleService.getSchedulePage(dbSchedule.getUserId(), reqVO);

        assertEquals(2, result.getTotal());
    }

    @Test
    void testUpdateSchedule_success() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class);
        scheduleMapper.insert(dbSchedule);

        OaScheduleDO update = new OaScheduleDO();
        update.setId(dbSchedule.getId());
        update.setTitle("Updated title");
        scheduleService.updateSchedule(update);

        OaScheduleDO schedule = scheduleMapper.selectById(dbSchedule.getId());
        assertEquals("Updated title", schedule.getTitle());
    }

    @Test
    void testUpdateSchedule_notExists() {
        assertServiceException(() ->
                        scheduleService.updateSchedule(new OaScheduleDO().setId(randomLongId())),
                OA_SCHEDULE_NOT_EXISTS);
    }

    @Test
    void testDeleteSchedule_success() {
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class);
        scheduleMapper.insert(dbSchedule);

        scheduleService.deleteSchedule(dbSchedule.getId());

        assertNull(scheduleMapper.selectById(dbSchedule.getId()));
    }

    @Test
    void testDeleteSchedule_notExists() {
        assertServiceException(() ->
                        scheduleService.deleteSchedule(randomLongId()),
                OA_SCHEDULE_NOT_EXISTS);
    }

    @Test
    void testGetScheduleListByDateRange_success() {
        LocalDateTime base = LocalDateTime.of(2026, 6, 15, 10, 0);
        OaScheduleDO dbSchedule = randomPojo(OaScheduleDO.class, o -> {
            o.setStartTime(base);
            o.setEndTime(base.plusHours(2));
        });
        scheduleMapper.insert(dbSchedule);
        OaScheduleDO outsideRange = randomPojo(OaScheduleDO.class, o -> {
            o.setStartTime(base.plusDays(10));
            o.setEndTime(base.plusDays(10).plusHours(2));
        });
        scheduleMapper.insert(outsideRange);

        List<OaScheduleDO> result = scheduleService.getScheduleListByDateRange(
                dbSchedule.getUserId(), base.minusDays(1), base.plusDays(1));

        assertEquals(1, result.size());
        assertPojoEquals(dbSchedule, result.get(0));
    }

    @Test
    void testGetScheduleListByDateRange_empty() {
        List<OaScheduleDO> result = scheduleService.getScheduleListByDateRange(
                randomLongId(), LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        assertTrue(result.isEmpty());
    }
}
