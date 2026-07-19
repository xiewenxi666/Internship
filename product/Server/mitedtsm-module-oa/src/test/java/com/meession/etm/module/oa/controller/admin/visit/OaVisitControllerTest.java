package com.meession.etm.module.oa.controller.admin.visit;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitCreateReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitPageReqVO;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.module.oa.service.visit.OaVisitService;
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

public class OaVisitControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaVisitController visitController;

    @Mock
    private OaVisitService visitService;

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
    void testCreateVisit() {
        OaVisitCreateReqVO reqVO = randomPojo(OaVisitCreateReqVO.class);
        when(visitService.createVisit(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = visitController.createVisit(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(visitService).createVisit(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateVisit() {
        Long id = 1L;
        OaVisitCreateReqVO reqVO = randomPojo(OaVisitCreateReqVO.class);

        CommonResult<Boolean> result = visitController.updateVisit(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(visitService).updateVisit(id, reqVO);
    }

    @Test
    void testSubmitVisit() {
        Long id = 1L;

        CommonResult<Boolean> result = visitController.submitVisit(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(visitService).submitVisit(id, LOGIN_USER_ID);
    }

    @Test
    void testUpdateVisitStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = visitController.updateVisitStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(visitService).updateVisitStatus(id, status);
    }

    @Test
    void testDeleteVisit() {
        Long id = 1L;

        CommonResult<Boolean> result = visitController.deleteVisit(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(visitService).deleteVisit(id);
    }

    @Test
    void testGetVisit() {
        Long id = 1L;
        OaVisitDO visitDO = randomPojo(OaVisitDO.class);
        when(visitService.getVisit(id)).thenReturn(visitDO);

        CommonResult<OaVisitRespVO> result = visitController.getVisit(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(visitService).getVisit(id);
    }

    @Test
    void testGetVisitPage() {
        OaVisitPageReqVO pageVO = randomPojo(OaVisitPageReqVO.class);
        PageResult<OaVisitDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(visitService.getVisitPage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaVisitRespVO>> result = visitController.getVisitPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(visitService).getVisitPage(LOGIN_USER_ID, pageVO);
    }

}
