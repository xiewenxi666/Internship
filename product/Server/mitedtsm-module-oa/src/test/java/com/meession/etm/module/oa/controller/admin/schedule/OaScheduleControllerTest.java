package com.meession.etm.module.oa.controller.admin.schedule;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaSchedulePageReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.service.schedule.OaScheduleService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OaScheduleControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaScheduleController scheduleController;

    @Mock
    private OaScheduleService scheduleService;

    @BeforeEach
    void setUp() {
        LoginUser loginUser = new LoginUser();
        loginUser.setId(LOGIN_USER_ID);
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginUser, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testCreateSchedule() {
        OaScheduleCreateReqVO reqVO = randomPojo(OaScheduleCreateReqVO.class);
        when(scheduleService.createSchedule(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = scheduleController.createSchedule(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(scheduleService).createSchedule(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateSchedule() {
        Long id = 1L;
        OaScheduleCreateReqVO reqVO = randomPojo(OaScheduleCreateReqVO.class);

        CommonResult<Boolean> result = scheduleController.updateSchedule(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(scheduleService).updateSchedule(id, reqVO);
    }

    @Test
    void testDeleteSchedule() {
        Long id = 1L;

        CommonResult<Boolean> result = scheduleController.deleteSchedule(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(scheduleService).deleteSchedule(id);
    }

    @Test
    void testGetSchedule() {
        Long id = 1L;
        OaScheduleDO scheduleDO = randomPojo(OaScheduleDO.class);
        when(scheduleService.getSchedule(id)).thenReturn(scheduleDO);

        CommonResult<OaScheduleRespVO> result = scheduleController.getSchedule(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(scheduleService).getSchedule(id);
    }

    @Test
    void testGetSchedulePage() {
        OaSchedulePageReqVO pageVO = randomPojo(OaSchedulePageReqVO.class);
        PageResult<OaScheduleDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(scheduleService.getSchedulePage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaScheduleRespVO>> result = scheduleController.getSchedulePage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(scheduleService).getSchedulePage(LOGIN_USER_ID, pageVO);
    }

    @Test
    void testUpdateScheduleStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = scheduleController.updateScheduleStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(scheduleService).updateScheduleStatus(id, status);
    }

    @Test
    void testGetScheduleCalendarList() {
        LocalDateTime startTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2025, 1, 31, 23, 59);
        Integer type = 1;
        List<OaScheduleDO> list = Collections.emptyList();
        when(scheduleService.getScheduleCalendarList(LOGIN_USER_ID, startTime, endTime, type)).thenReturn(list);

        CommonResult<List<OaScheduleRespVO>> result = scheduleController.getScheduleCalendarList(startTime, endTime, type);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(scheduleService).getScheduleCalendarList(LOGIN_USER_ID, startTime, endTime, type);
    }

}
