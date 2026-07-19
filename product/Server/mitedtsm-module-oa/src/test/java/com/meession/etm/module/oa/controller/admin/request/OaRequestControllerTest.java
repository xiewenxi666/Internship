package com.meession.etm.module.oa.controller.admin.request;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestCreateReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestPageReqVO;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.module.oa.service.request.OaRequestService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;

import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OaRequestControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaRequestController requestController;

    @Mock
    private OaRequestService requestService;

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
    void testCreateRequest() {
        OaRequestCreateReqVO reqVO = randomPojo(OaRequestCreateReqVO.class);
        when(requestService.createRequest(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = requestController.createRequest(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(requestService).createRequest(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateRequest() {
        Long id = 1L;
        OaRequestCreateReqVO reqVO = randomPojo(OaRequestCreateReqVO.class);

        CommonResult<Boolean> result = requestController.updateRequest(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(requestService).updateRequest(id, reqVO);
    }

    @Test
    void testSubmitRequest() {
        Long id = 1L;

        CommonResult<Boolean> result = requestController.submitRequest(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(requestService).submitRequest(id, LOGIN_USER_ID);
    }

    @Test
    void testCancelRequest() {
        Long id = 1L;

        CommonResult<Boolean> result = requestController.cancelRequest(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(requestService).cancelRequest(id, LOGIN_USER_ID);
    }

    @Test
    void testReconsiderRequest() {
        Long id = 1L;

        CommonResult<Boolean> result = requestController.reconsiderRequest(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(requestService).reconsiderRequest(id, LOGIN_USER_ID);
    }

    @Test
    void testUpdateRequestStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = requestController.updateRequestStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(requestService).updateRequestStatus(id, status);
    }

    @Test
    void testDeleteRequest() {
        Long id = 1L;

        CommonResult<Boolean> result = requestController.deleteRequest(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(requestService).deleteRequest(id);
    }

    @Test
    void testGetRequest() {
        Long id = 1L;
        OaRequestDO requestDO = randomPojo(OaRequestDO.class);
        when(requestService.getRequest(id)).thenReturn(requestDO);

        CommonResult<OaRequestRespVO> result = requestController.getRequest(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(requestService).getRequest(id);
    }

    @Test
    void testGetRequestPage() {
        OaRequestPageReqVO pageVO = randomPojo(OaRequestPageReqVO.class);
        PageResult<OaRequestDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(requestService.getRequestPage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaRequestRespVO>> result = requestController.getRequestPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(requestService).getRequestPage(LOGIN_USER_ID, pageVO);
    }

}
