package com.meession.etm.module.crm.controller.admin.receivable;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.*;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.customer.CrmCustomerDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.customer.CrmCustomerService;
import com.meession.etm.module.crm.service.receivable.CrmReceivablePlanService;
import com.meession.etm.module.crm.service.receivable.CrmReceivableService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import com.meession.etm.module.system.api.user.dto.AdminUserRespDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CrmReceivablePlanControllerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private CrmReceivablePlanController controller;

    @Mock
    private CrmReceivablePlanService receivablePlanService;
    @Mock
    private CrmReceivableService receivableService;
    @Mock
    private CrmContractService contractService;
    @Mock
    private CrmCustomerService customerService;
    @Mock
    private AdminUserApi adminUserApi;

    // ==================== 批量创建 ====================

    @Test
    public void testBatchCreateReceivablePlan_success() {
        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(100L);
        reqVO.setOwnerUserId(1L);
        reqVO.setPeriodCount(3);
        reqVO.setTotalPrice(new BigDecimal("30000.00"));

        when(receivablePlanService.batchCreateReceivablePlan(any())).thenReturn(Arrays.asList(101L, 102L, 103L));

        CommonResult<List<Long>> result = controller.batchCreateReceivablePlan(reqVO);
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals(3, result.getData().size());
        verify(receivablePlanService, times(1)).batchCreateReceivablePlan(any());
    }

    @Test
    public void testBatchCreateReceivablePlan_returnsCorrectIds() {
        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(200L);
        reqVO.setOwnerUserId(2L);
        reqVO.setPeriodCount(1);
        reqVO.setTotalPrice(new BigDecimal("5000.00"));

        when(receivablePlanService.batchCreateReceivablePlan(any())).thenReturn(Collections.singletonList(999L));

        CommonResult<List<Long>> result = controller.batchCreateReceivablePlan(reqVO);
        assertEquals(Long.valueOf(999L), result.getData().get(0));
    }

    // ==================== 汇总统计 ====================

    @Test
    public void testGetReceivablePlanSummary_success() {
        CrmReceivablePlanSummaryReqVO reqVO = new CrmReceivablePlanSummaryReqVO();
        reqVO.setYear(2026);

        CrmReceivablePlanSummaryRespVO vo = new CrmReceivablePlanSummaryRespVO();
        vo.setMonth("2026-01");
        vo.setTargetPrice(new BigDecimal("100000.00"));
        vo.setCompletedPrice(new BigDecimal("80000.00"));
        vo.setUncompletedPrice(new BigDecimal("20000.00"));
        vo.setCompletionRate(new BigDecimal("80.00"));

        when(receivablePlanService.getReceivablePlanSummary(any())).thenReturn(Collections.singletonList(vo));

        CommonResult<List<CrmReceivablePlanSummaryRespVO>> result = controller.getReceivablePlanSummary(reqVO);
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertFalse(result.getData().isEmpty());
        assertEquals("2026-01", result.getData().get(0).getMonth());
        assertEquals(0, result.getData().get(0).getTargetPrice().compareTo(new BigDecimal("100000.00")));
    }

    @Test
    public void testGetReceivablePlanSummaryWithDeptFilter() {
        CrmReceivablePlanSummaryReqVO reqVO = new CrmReceivablePlanSummaryReqVO();
        reqVO.setYear(2026);
        reqVO.setDeptId(10L);
        reqVO.setOwnerUserId(100L);

        when(receivablePlanService.getReceivablePlanSummary(any())).thenReturn(Collections.emptyList());

        CommonResult<List<CrmReceivablePlanSummaryRespVO>> result = controller.getReceivablePlanSummary(reqVO);
        assertNotNull(result);
        assertTrue(result.getData().isEmpty());
    }

    // ==================== 报表查询 ====================

    @Test
    public void testGetReceivablePlanReport_success() {
        CrmReceivablePlanReportReqVO reqVO = new CrmReceivablePlanReportReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(10);
        reqVO.setYear(2026);

        CrmReceivablePlanDO plan = new CrmReceivablePlanDO();
        plan.setId(1L);
        plan.setPeriod(1);
        plan.setContractId(100L);
        plan.setCustomerId(200L);
        plan.setPrice(new BigDecimal("10000.00"));
        plan.setStatus(2);
        plan.setCreator("1");
        plan.setOwnerUserId(10L);

        PageResult<CrmReceivablePlanDO> pageResult = new PageResult<>(Collections.singletonList(plan), 1L);
        when(receivablePlanService.getReceivablePlanReport(any())).thenReturn(pageResult);
        when(customerService.getCustomerMap(any())).thenReturn(Collections.emptyMap());
        when(adminUserApi.getUserMap(any())).thenReturn(Collections.emptyMap());
        when(contractService.getContractMap(any())).thenReturn(Collections.emptyMap());
        when(receivableService.getReceivableMap(any())).thenReturn(Collections.emptyMap());

        CommonResult<PageResult<CrmReceivablePlanRespVO>> result = controller.getReceivablePlanReport(reqVO);
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals(1, result.getData().getTotal());
        assertEquals(1, result.getData().getList().size());
        CrmReceivablePlanRespVO vo = result.getData().getList().get(0);
        assertNotNull(vo.getStatus());
        assertNotNull(vo.getStatusName());
    }

    @Test
    public void testGetReceivablePlanReport_emptyResult() {
        CrmReceivablePlanReportReqVO reqVO = new CrmReceivablePlanReportReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(10);

        PageResult<CrmReceivablePlanDO> emptyPage = new PageResult<>(Collections.emptyList(), 0L);
        when(receivablePlanService.getReceivablePlanReport(any())).thenReturn(emptyPage);

        CommonResult<PageResult<CrmReceivablePlanRespVO>> result = controller.getReceivablePlanReport(reqVO);
        assertNotNull(result);
        assertEquals(0, result.getCode());
        assertEquals(0, result.getData().getTotal());
        assertTrue(result.getData().getList().isEmpty());
    }
}
