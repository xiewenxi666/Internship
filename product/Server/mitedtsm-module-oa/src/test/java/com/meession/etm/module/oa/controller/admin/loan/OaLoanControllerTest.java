package com.meession.etm.module.oa.controller.admin.loan;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanCreateReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanPageReqVO;
import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.module.oa.service.loan.OaLoanService;
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

public class OaLoanControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaLoanController loanController;

    @Mock
    private OaLoanService loanService;

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
    void testCreateLoan() {
        OaLoanCreateReqVO reqVO = randomPojo(OaLoanCreateReqVO.class);
        when(loanService.createLoan(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = loanController.createLoan(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(loanService).createLoan(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateLoan() {
        Long id = 1L;
        OaLoanCreateReqVO reqVO = randomPojo(OaLoanCreateReqVO.class);

        CommonResult<Boolean> result = loanController.updateLoan(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(loanService).updateLoan(id, reqVO);
    }

    @Test
    void testSubmitLoan() {
        Long id = 1L;

        CommonResult<Boolean> result = loanController.submitLoan(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(loanService).submitLoan(id, LOGIN_USER_ID);
    }

    @Test
    void testUpdateLoanStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = loanController.updateLoanStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(loanService).updateLoanStatus(id, status);
    }

    @Test
    void testDeleteLoan() {
        Long id = 1L;

        CommonResult<Boolean> result = loanController.deleteLoan(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(loanService).deleteLoan(id);
    }

    @Test
    void testGetLoan() {
        Long id = 1L;
        OaLoanDO loanDO = randomPojo(OaLoanDO.class);
        when(loanService.getLoan(id)).thenReturn(loanDO);

        CommonResult<OaLoanRespVO> result = loanController.getLoan(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(loanService).getLoan(id);
    }

    @Test
    void testGetLoanPage() {
        OaLoanPageReqVO pageVO = randomPojo(OaLoanPageReqVO.class);
        PageResult<OaLoanDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(loanService.getLoanPage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaLoanRespVO>> result = loanController.getLoanPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(loanService).getLoanPage(LOGIN_USER_ID, pageVO);
    }

}
