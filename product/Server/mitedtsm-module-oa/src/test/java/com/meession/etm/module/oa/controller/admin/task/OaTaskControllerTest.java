package com.meession.etm.module.oa.controller.admin.task;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskCreateReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskPageReqVO;
import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.module.oa.service.task.OaTaskService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OaTaskControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaTaskController taskController;

    @Mock
    private OaTaskService taskService;

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
    void testCreateTask() {
        OaTaskCreateReqVO reqVO = randomPojo(OaTaskCreateReqVO.class);
        when(taskService.createTask(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = taskController.createTask(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(taskService).createTask(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateTask() {
        Long id = 1L;
        OaTaskCreateReqVO reqVO = randomPojo(OaTaskCreateReqVO.class);

        CommonResult<Boolean> result = taskController.updateTask(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(taskService).updateTask(id, reqVO);
    }

    @Test
    void testDeleteTask() {
        Long id = 1L;

        CommonResult<Boolean> result = taskController.deleteTask(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(taskService).deleteTask(id);
    }

    @Test
    void testGetTask() {
        Long id = 1L;
        OaTaskDO taskDO = randomPojo(OaTaskDO.class);
        when(taskService.getTask(id)).thenReturn(taskDO);

        CommonResult<OaTaskRespVO> result = taskController.getTask(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(taskService).getTask(id);
    }

    @Test
    void testGetTaskPage() {
        OaTaskPageReqVO pageVO = randomPojo(OaTaskPageReqVO.class);
        PageResult<OaTaskDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(taskService.getTaskPage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaTaskRespVO>> result = taskController.getTaskPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(taskService).getTaskPage(LOGIN_USER_ID, pageVO);
    }

    @Test
    void testUpdateTaskStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = taskController.updateTaskStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(taskService).updateTaskStatus(id, status);
    }

    @Test
    void testUpdateTaskProgress() {
        Long id = 1L;
        Integer progress = 50;

        CommonResult<Boolean> result = taskController.updateTaskProgress(id, progress);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(taskService).updateTaskProgress(id, progress);
    }

    @Test
    void testCompleteTask() {
        Long id = 1L;

        CommonResult<Boolean> result = taskController.completeTask(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(taskService).completeTask(id);
    }

    @Test
    void testGetTaskListByAssignee() {
        Long assigneeUserId = 2L;
        Integer status = 1;
        List<OaTaskDO> list = Collections.emptyList();
        when(taskService.getTaskListByAssignee(assigneeUserId, status)).thenReturn(list);

        CommonResult<List<OaTaskRespVO>> result = taskController.getTaskListByAssignee(assigneeUserId, status);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(taskService).getTaskListByAssignee(assigneeUserId, status);
    }

    @Test
    void testGetTaskStatistics() {
        Map<String, Long> statistics = Collections.singletonMap("total", 10L);
        when(taskService.getTaskStatistics(LOGIN_USER_ID)).thenReturn(statistics);

        CommonResult<Map<String, Long>> result = taskController.getTaskStatistics();

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        assertEquals(10L, result.getData().get("total"));
        verify(taskService).getTaskStatistics(LOGIN_USER_ID);
    }

}
