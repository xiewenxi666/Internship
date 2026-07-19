package com.meession.etm.module.oa.controller.admin.leave;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeavePageReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import com.meession.etm.module.oa.service.leave.OaLeaveService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static com.meession.etm.framework.common.util.object.BeanUtils.toBean;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OaLeaveControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaLeaveController leaveController;

    @Mock
    private OaLeaveService leaveService;

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
    void testCreateLeave() {
        OaLeaveCreateReqVO reqVO = randomPojo(OaLeaveCreateReqVO.class);
        when(leaveService.createLeave(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = leaveController.createLeave(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(leaveService).createLeave(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateLeave() {
        Long id = 1L;
        OaLeaveCreateReqVO reqVO = randomPojo(OaLeaveCreateReqVO.class);

        CommonResult<Boolean> result = leaveController.updateLeave(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(leaveService).updateLeave(id, reqVO);
    }

    @Test
    void testSubmitLeave() {
        Long id = 1L;

        CommonResult<Boolean> result = leaveController.submitLeave(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(leaveService).submitLeave(id, LOGIN_USER_ID);
    }

    @Test
    void testUpdateLeaveStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = leaveController.updateLeaveStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(leaveService).updateLeaveStatus(id, status);
    }

    @Test
    void testDeleteLeave() {
        Long id = 1L;

        CommonResult<Boolean> result = leaveController.deleteLeave(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(leaveService).deleteLeave(id);
    }

    @Test
    void testGetLeave() {
        Long id = 1L;
        OaLeaveDO leaveDO = randomPojo(OaLeaveDO.class);
        when(leaveService.getLeave(id)).thenReturn(leaveDO);

        CommonResult<OaLeaveRespVO> result = leaveController.getLeave(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(leaveService).getLeave(id);
    }

    @Test
    void testGetLeavePage() {
        OaLeavePageReqVO pageVO = randomPojo(OaLeavePageReqVO.class);
        PageResult<OaLeaveDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(leaveService.getLeavePage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaLeaveRespVO>> result = leaveController.getLeavePage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(leaveService).getLeavePage(LOGIN_USER_ID, pageVO);
    }

}
