package com.meession.etm.module.oa.controller.admin.businessTrip;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.security.core.LoginUser;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripCreateReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripPageReqVO;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripRespVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripService;
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

public class OaBusinessTripControllerTest extends BaseMockitoUnitTest {

    private static final Long LOGIN_USER_ID = 1L;

    @InjectMocks
    private OaBusinessTripController businessTripController;

    @Mock
    private OaBusinessTripService businessTripService;

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
    void testCreateBusinessTrip() {
        OaBusinessTripCreateReqVO reqVO = randomPojo(OaBusinessTripCreateReqVO.class);
        when(businessTripService.createBusinessTrip(LOGIN_USER_ID, reqVO)).thenReturn(1L);

        CommonResult<Long> result = businessTripController.createBusinessTrip(reqVO);

        assertEquals(0, result.getCode());
        assertEquals(1L, result.getData());
        verify(businessTripService).createBusinessTrip(LOGIN_USER_ID, reqVO);
    }

    @Test
    void testUpdateBusinessTrip() {
        Long id = 1L;
        OaBusinessTripCreateReqVO reqVO = randomPojo(OaBusinessTripCreateReqVO.class);

        CommonResult<Boolean> result = businessTripController.updateBusinessTrip(reqVO, id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(businessTripService).updateBusinessTrip(id, reqVO);
    }

    @Test
    void testSubmitBusinessTrip() {
        Long id = 1L;

        CommonResult<Boolean> result = businessTripController.submitBusinessTrip(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(businessTripService).submitBusinessTrip(id, LOGIN_USER_ID);
    }

    @Test
    void testUpdateBusinessTripStatus() {
        Long id = 1L;
        Integer status = 2;

        CommonResult<Boolean> result = businessTripController.updateBusinessTripStatus(id, status);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(businessTripService).updateBusinessTripStatus(id, status);
    }

    @Test
    void testDeleteBusinessTrip() {
        Long id = 1L;

        CommonResult<Boolean> result = businessTripController.deleteBusinessTrip(id);

        assertEquals(0, result.getCode());
        assertEquals(true, result.getData());
        verify(businessTripService).deleteBusinessTrip(id);
    }

    @Test
    void testGetBusinessTrip() {
        Long id = 1L;
        OaBusinessTripDO tripDO = randomPojo(OaBusinessTripDO.class);
        when(businessTripService.getBusinessTrip(id)).thenReturn(tripDO);

        CommonResult<OaBusinessTripRespVO> result = businessTripController.getBusinessTrip(id);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(businessTripService).getBusinessTrip(id);
    }

    @Test
    void testGetBusinessTripPage() {
        OaBusinessTripPageReqVO pageVO = randomPojo(OaBusinessTripPageReqVO.class);
        PageResult<OaBusinessTripDO> pageResult = new PageResult<>(Collections.emptyList(), 0L);
        when(businessTripService.getBusinessTripPage(LOGIN_USER_ID, pageVO)).thenReturn(pageResult);

        CommonResult<PageResult<OaBusinessTripRespVO>> result = businessTripController.getBusinessTripPage(pageVO);

        assertEquals(0, result.getCode());
        assertNotNull(result.getData());
        verify(businessTripService).getBusinessTripPage(LOGIN_USER_ID, pageVO);
    }

}
