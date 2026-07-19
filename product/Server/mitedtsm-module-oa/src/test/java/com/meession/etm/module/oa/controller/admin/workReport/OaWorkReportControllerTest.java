package com.meession.etm.module.oa.controller.admin.workReport;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportPageReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import com.meession.etm.module.oa.service.workReport.OaWorkReportService;
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

public class OaWorkReportControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaWorkReportController workReportController;

    @Mock
    private OaWorkReportService workReportService;

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
    void testCreateWorkReport() {
        OaWorkReportCreateReqVO reqVO = randomPojo(OaWorkReportCreateReqVO.class);
        when(workReportService.createWorkReport(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = workReportController.createWorkReport(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(workReportService).createWorkReport(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateWorkReport() {
        Long id = 1L;
        OaWorkReportCreateReqVO reqVO = randomPojo(OaWorkReportCreateReqVO.class);

        CommonResult<Boolean> result = workReportController.updateWorkReport(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(workReportService).updateWorkReport(id, reqVO);
    }

    @Test
    void testDeleteWorkReport() {
        Long id = 1L;

        CommonResult<Boolean> result = workReportController.deleteWorkReport(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(workReportService).deleteWorkReport(id);
    }

    @Test
    void testGetWorkReport() {
        Long id = 1L;
        OaWorkReportDO reportDO = randomPojo(OaWorkReportDO.class);
        when(workReportService.getWorkReport(id)).thenReturn(reportDO);

        CommonResult<OaWorkReportRespVO> result = workReportController.getWorkReport(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(workReportService).getWorkReport(id);
    }

    @Test
    void testGetWorkReportPage() {
        OaWorkReportPageReqVO pageVO = randomPojo(OaWorkReportPageReqVO.class);
        PageResult<OaWorkReportDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(workReportService.getWorkReportPage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaWorkReportRespVO>> result = workReportController.getWorkReportPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(workReportService).getWorkReportPage(LOGIN_USER_ID, pageVO);
    }

    @Test
    void testSubmitWorkReport() {
        Long id = 1L;

        CommonResult<Boolean> result = workReportController.submitWorkReport(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(workReportService).submitWorkReport(id);
    }

    @Test
    void testReviewWorkReport() {
        Long id = 1L;
        String reviewContent = "内容详实";

        CommonResult<Boolean> result = workReportController.reviewWorkReport(id, reviewContent);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(workReportService).reviewWorkReport(id, LOGIN_USER_ID, reviewContent);
    }

}
