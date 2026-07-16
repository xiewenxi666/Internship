package com.meession.etm.module.crm.service.receivable;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.*;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivablePlanMapper;
import com.meession.etm.module.crm.enums.receivable.CrmReceivablePlanStatusEnum;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.system.api.user.AdminUserApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 覆盖率补充测试 — 覆盖 Service 和 Mapper 的更多分支
 */
@Import(CrmReceivablePlanServiceImpl.class)
public class CrmReceivablePlanServiceCoverageTest extends BaseDbUnitTest {

    @Resource
    private CrmReceivablePlanServiceImpl receivablePlanService;
    @Resource
    private CrmReceivablePlanMapper receivablePlanMapper;

    @MockitoBean
    private CrmContractService contractService;
    @MockitoBean
    private CrmPermissionService permissionService;
    @MockitoBean
    private AdminUserApi adminUserApi;

    private static final Long UID = 1L;
    private static final Long CID = 100L;
    private static final Long CUST = 200L;

    // ==================== 批量创建边界测试 ====================

    @Test
    public void testBatchCreate_onePeriod_exactPrice() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(CID)).thenReturn(buildContract(CID, CUST));

        CrmReceivablePlanBatchCreateReqVO req = new CrmReceivablePlanBatchCreateReqVO();
        req.setContractId(CID);
        req.setOwnerUserId(UID);
        req.setPeriodCount(1);
        req.setTotalPrice(new BigDecimal("100.00"));
        req.setReturnType(1);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(req);
        assertEquals(1, ids.size());
        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(ids.get(0));
        assertEquals(0, plan.getPrice().compareTo(new BigDecimal("100.00")));
        assertEquals(CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus(), plan.getStatus());
    }

    @Test
    public void testBatchCreate_threePeriods_evenDiv() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(CID)).thenReturn(buildContract(CID, CUST));

        CrmReceivablePlanBatchCreateReqVO req = new CrmReceivablePlanBatchCreateReqVO();
        req.setContractId(CID);
        req.setOwnerUserId(UID);
        req.setPeriodCount(3);
        req.setTotalPrice(new BigDecimal("9999.00"));
        req.setReturnType(2);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(req);
        assertEquals(3, ids.size());
        BigDecimal sum = BigDecimal.ZERO;
        for (Long id : ids) {
            CrmReceivablePlanDO p = receivablePlanMapper.selectById(id);
            sum = sum.add(p.getPrice());
            assertNotNull(p.getPercent());
        }
        assertEquals(0, sum.compareTo(new BigDecimal("9999.00")));
    }

    @Test
    public void testBatchCreate_remindEnabledWithoutDays_shouldNotSetRemind() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(CID)).thenReturn(buildContract(CID, CUST));

        CrmReceivablePlanBatchCreateReqVO req = new CrmReceivablePlanBatchCreateReqVO();
        req.setContractId(CID);
        req.setOwnerUserId(UID);
        req.setPeriodCount(2);
        req.setTotalPrice(new BigDecimal("5000.00"));
        req.setRemindEnabled(true);
        req.setRemindDays(null);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(req);
        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(ids.get(0));
        assertNull(plan.getRemindDays());
    }

    @Test
    public void testBatchCreate_remindWithCustomPlans_setsRemindTime() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(CID)).thenReturn(buildContract(CID, CUST));

        CrmReceivablePlanBatchCreateReqVO req = new CrmReceivablePlanBatchCreateReqVO();
        req.setContractId(CID);
        req.setOwnerUserId(UID);
        req.setPeriodCount(2);
        req.setTotalPrice(new BigDecimal("20000.00"));
        req.setReturnType(1);
        req.setRemindEnabled(true);
        req.setRemindDays(15);

        CrmReceivablePlanBatchCreateReqVO.PlanItem p1 = new CrmReceivablePlanBatchCreateReqVO.PlanItem();
        p1.setPrice(new BigDecimal("10000.00"));
        p1.setPercent(new BigDecimal("50.00"));
        p1.setReturnTime(LocalDateTime.now().plusDays(30));
        CrmReceivablePlanBatchCreateReqVO.PlanItem p2 = new CrmReceivablePlanBatchCreateReqVO.PlanItem();
        p2.setPrice(new BigDecimal("10000.00"));
        p2.setPercent(new BigDecimal("50.00"));
        p2.setReturnTime(LocalDateTime.now().plusDays(60));
        req.setPlans(List.of(p1, p2));

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(req);
        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(ids.get(0));
        assertEquals(Integer.valueOf(15), plan.getRemindDays());
        assertNotNull(plan.getRemindTime());
    }

    @Test
    public void testBatchCreate_remindDisabled_shouldNotSetRemindFields() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(CID)).thenReturn(buildContract(CID, CUST));

        CrmReceivablePlanBatchCreateReqVO req = new CrmReceivablePlanBatchCreateReqVO();
        req.setContractId(CID);
        req.setOwnerUserId(UID);
        req.setPeriodCount(2);
        req.setTotalPrice(new BigDecimal("10000.00"));
        req.setRemindEnabled(false);
        req.setRemindDays(7);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(req);
        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(ids.get(0));
        assertNull(plan.getRemindDays());
        assertNull(plan.getRemindTime());
    }

    // ==================== 汇总统计更多场景 ====================

    @Test
    public void testGetSummary_byOwner() {
        insertPlan("2026-06", "10000", "10000", UID);
        insertPlan("2026-06", "20000", "0", UID + 99);

        CrmReceivablePlanSummaryReqVO req = new CrmReceivablePlanSummaryReqVO();
        req.setYear(2026);
        req.setOwnerUserId(UID);

        List<CrmReceivablePlanSummaryRespVO> res = receivablePlanService.getReceivablePlanSummary(req);
        assertFalse(res.isEmpty());
        CrmReceivablePlanSummaryRespVO m06 = res.stream()
                .filter(r -> "2026-06".equals(r.getMonth())).findFirst().orElse(null);
        assertNotNull(m06);
        assertEquals(0, m06.getTargetPrice().compareTo(new BigDecimal("10000.00")));
    }

    @Test
    public void testGetSummary_multipleMonths() {
        insertPlan("2026-07", "5000", "0", UID);
        insertPlan("2026-08", "15000", "15000", UID);
        insertPlan("2026-09", "8000", "3000", UID);

        CrmReceivablePlanSummaryReqVO req = new CrmReceivablePlanSummaryReqVO();
        req.setYear(2026);

        List<CrmReceivablePlanSummaryRespVO> res = receivablePlanService.getReceivablePlanSummary(req);
        assertTrue(res.size() >= 3);
    }

    @Test
    public void testGetSummary_completionRate_fullCompleted() {
        insertPlan("2026-10", "10000", "10000", UID + 5);

        CrmReceivablePlanSummaryReqVO req = new CrmReceivablePlanSummaryReqVO();
        req.setYear(2026);

        List<CrmReceivablePlanSummaryRespVO> res = receivablePlanService.getReceivablePlanSummary(req);
        CrmReceivablePlanSummaryRespVO m10 = res.stream()
                .filter(r -> "2026-10".equals(r.getMonth())).findFirst().orElse(null);
        assertNotNull(m10);
        assertTrue(m10.getCompletionRate().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    public void testGetSummary_noDataForYear() {
        CrmReceivablePlanSummaryReqVO req = new CrmReceivablePlanSummaryReqVO();
        req.setYear(1999);

        List<CrmReceivablePlanSummaryRespVO> res = receivablePlanService.getReceivablePlanSummary(req);
        assertNotNull(res);
        assertTrue(res.isEmpty());
    }

    // ==================== 报表更多场景 ====================

    @Test
    public void testGetReport_secondPage() {
        for (int i = 0; i < 5; i++) {
            insertPlan("2026-11", "1000", "0", UID);
        }

        CrmReceivablePlanReportReqVO req = new CrmReceivablePlanReportReqVO();
        req.setPageNo(2);
        req.setPageSize(2);
        req.setYear(2026);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(req);
        assertNotNull(result);
        assertTrue(result.getTotal() >= 5);
        assertTrue(result.getList().size() <= 2);
    }

    @Test
    public void testGetReport_pageBeyondRange() {
        CrmReceivablePlanReportReqVO req = new CrmReceivablePlanReportReqVO();
        req.setPageNo(99);
        req.setPageSize(10);
        req.setYear(2026);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(req);
        assertNotNull(result);
        assertTrue(result.getList().isEmpty());
    }

    @Test
    public void testGetReport_noYearFilter() {
        insertPlan("2026-12", "5000", "0", UID);

        CrmReceivablePlanReportReqVO req = new CrmReceivablePlanReportReqVO();
        req.setPageNo(1);
        req.setPageSize(10);
        req.setOwnerUserId(UID);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(req);
        assertNotNull(result);
    }

    // ==================== Mapper 覆盖：selectMaxPeriod ====================

    @Test
    public void testMapper_selectMaxPeriod_empty() {
        CrmReceivablePlanDO result = receivablePlanMapper.selectMaxPeriodByContractId(9999L);
        assertNull(result);
    }

    @Test
    public void testMapper_selectMaxPeriod_withPlans() {
        CrmReceivablePlanDO p1 = new CrmReceivablePlanDO();
        p1.setPeriod(1);
        p1.setContractId(CID);
        p1.setCustomerId(CUST);
        p1.setPrice(new BigDecimal("1000"));
        p1.setCreator("1");
        receivablePlanMapper.insert(p1);

        CrmReceivablePlanDO p2 = new CrmReceivablePlanDO();
        p2.setPeriod(2);
        p2.setContractId(CID);
        p2.setCustomerId(CUST);
        p2.setPrice(new BigDecimal("2000"));
        p2.setCreator("1");
        receivablePlanMapper.insert(p2);

        CrmReceivablePlanDO max = receivablePlanMapper.selectMaxPeriodByContractId(CID);
        assertNotNull(max);
        assertEquals(Integer.valueOf(2), max.getPeriod());
    }

    // ==================== Mapper 覆盖：selectListForSummary/Report ====================

    @Test
    public void testMapper_selectListForSummary_noFilter() {
        insertPlan("2026-01", "5000", "0", UID);
        List<CrmReceivablePlanDO> plans = receivablePlanMapper.selectListForSummary(null, null);
        assertNotNull(plans);
    }

    @Test
    public void testMapper_selectListForReport_noFilter() {
        List<CrmReceivablePlanDO> plans = receivablePlanMapper.selectListForReport(null, null);
        assertNotNull(plans);
    }

    @Test
    public void testMapper_selectListForSummary_withYearFilter() {
        insertPlan("2026-02", "3000", "0", UID);
        List<CrmReceivablePlanDO> plans = receivablePlanMapper.selectListForSummary(2026, null);
        assertFalse(plans.isEmpty());
        for (CrmReceivablePlanDO p : plans) {
            assertEquals(2026, p.getReturnTime().getYear());
        }
    }

    private CrmContractDO buildContract(Long id, Long customerId) {
        CrmContractDO c = new CrmContractDO();
        c.setId(id);
        c.setCustomerId(customerId);
        c.setTotalPrice(new BigDecimal("100000.00"));
        c.setName("test");
        c.setNo("TC-" + id);
        return c;
    }

    private void insertPlan(String ym, String price, String received, Long ownerUserId) {
        CrmReceivablePlanDO p = new CrmReceivablePlanDO();
        p.setPeriod(1);
        p.setContractId(CID);
        p.setCustomerId(CUST);
        p.setOwnerUserId(ownerUserId != null ? ownerUserId : UID);
        p.setPrice(new BigDecimal(price));
        String[] parts = ym.split("-");
        p.setReturnTime(LocalDateTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 15, 0, 0));
        p.setStatus(CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus());
        p.setCreator("1");
        if (received != null && !"0".equals(received)) {
            p.setReceivableId(randomLongId());
        }
        receivablePlanMapper.insert(p);
    }
}
