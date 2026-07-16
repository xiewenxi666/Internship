package com.meession.etm.module.crm.service.receivable;

import cn.hutool.core.collection.ListUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.crm.controller.admin.receivable.vo.plan.*;
import com.meession.etm.module.crm.dal.dataobject.contract.CrmContractDO;
import com.meession.etm.module.crm.dal.dataobject.receivable.CrmReceivablePlanDO;
import com.meession.etm.module.crm.dal.mysql.receivable.CrmReceivablePlanMapper;
import com.meession.etm.module.crm.enums.receivable.CrmReceivablePlanStatusEnum;
import com.meession.etm.module.crm.service.contract.CrmContractService;
import com.meession.etm.module.crm.service.permission.CrmPermissionService;
import com.meession.etm.module.crm.service.permission.bo.CrmPermissionCreateReqBO;
import com.meession.etm.module.system.api.user.AdminUserApi;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.meession.etm.framework.test.core.util.RandomUtils;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Import(CrmReceivablePlanServiceImpl.class)
public class CrmReceivablePlanServiceImplTest extends BaseDbUnitTest {

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

    private static final Long TEST_USER_ID = 1L;
    private static final Long TEST_CONTRACT_ID = 100L;
    private static final Long TEST_CUSTOMER_ID = 200L;

    // ==================== 状态枚举测试 ====================

    @Test
    public void testStatusEnum_completed() {
        assertEquals(Integer.valueOf(1), CrmReceivablePlanStatusEnum.COMPLETED.getStatus());
        assertEquals("已完成", CrmReceivablePlanStatusEnum.COMPLETED.getName());
    }

    @Test
    public void testStatusEnum_uncompleted() {
        assertEquals(Integer.valueOf(2), CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus());
        assertEquals("未完成", CrmReceivablePlanStatusEnum.UNCOMPLETED.getName());
    }

    @Test
    public void testStatusEnum_overdue() {
        assertEquals(Integer.valueOf(3), CrmReceivablePlanStatusEnum.OVERDUE.getStatus());
        assertEquals("已逾期", CrmReceivablePlanStatusEnum.OVERDUE.getName());
    }

    @Test
    public void testStatusEnum_values() {
        assertEquals(3, CrmReceivablePlanStatusEnum.values().length);
    }

    // ==================== 批量创建测试 ====================

    @Test
    public void testBatchCreateReceivablePlan_autoGenerate() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setPeriodCount(3);
        reqVO.setTotalPrice(new BigDecimal("30000.00"));
        reqVO.setReturnType(1);
        reqVO.setRemindEnabled(false);
        reqVO.setRemark("批量测试");

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(reqVO);
        assertNotNull(ids);
        assertEquals(3, ids.size());

        List<CrmReceivablePlanDO> plans = receivablePlanMapper.selectByIds(ids);
        assertEquals(3, plans.size());

        for (CrmReceivablePlanDO plan : plans) {
            assertEquals(TEST_CONTRACT_ID, plan.getContractId());
            assertEquals(TEST_USER_ID, plan.getOwnerUserId());
            assertEquals(Integer.valueOf(1), plan.getReturnType());
            assertNotNull(plan.getStatus());
        }

        BigDecimal totalPrice = plans.stream()
                .map(CrmReceivablePlanDO::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        assertEquals(0, totalPrice.compareTo(new BigDecimal("30000.00")));
    }

    @Test
    public void testBatchCreateReceivablePlan_withRemind() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setPeriodCount(2);
        reqVO.setTotalPrice(new BigDecimal("10000.00"));
        reqVO.setReturnType(2);
        reqVO.setRemindEnabled(true);
        reqVO.setRemindDays(7);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(reqVO);
        assertEquals(2, ids.size());

        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(ids.get(0));
        assertEquals(Integer.valueOf(7), plan.getRemindDays());
    }

    @Test
    public void testBatchCreateReceivablePlan_withCustomPlans() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setPeriodCount(3);
        reqVO.setTotalPrice(new BigDecimal("9000.00"));
        reqVO.setReturnType(5);

        CrmReceivablePlanBatchCreateReqVO.PlanItem item1 = new CrmReceivablePlanBatchCreateReqVO.PlanItem();
        item1.setPrice(new BigDecimal("3000.00"));
        item1.setPercent(new BigDecimal("33.33"));
        item1.setReturnTime(LocalDateTime.now().plusDays(30));
        CrmReceivablePlanBatchCreateReqVO.PlanItem item2 = new CrmReceivablePlanBatchCreateReqVO.PlanItem();
        item2.setPrice(new BigDecimal("3000.00"));
        item2.setPercent(new BigDecimal("33.33"));
        item2.setReturnTime(LocalDateTime.now().plusDays(60));
        CrmReceivablePlanBatchCreateReqVO.PlanItem item3 = new CrmReceivablePlanBatchCreateReqVO.PlanItem();
        item3.setPrice(new BigDecimal("3000.00"));
        item3.setPercent(new BigDecimal("33.34"));
        item3.setReturnTime(LocalDateTime.now().plusDays(90));
        reqVO.setPlans(ListUtil.of(item1, item2, item3));

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(reqVO);
        assertEquals(3, ids.size());

        CrmReceivablePlanDO plan1 = receivablePlanMapper.selectById(ids.get(0));
        assertEquals(0, plan1.getPercent().compareTo(new BigDecimal("33.33")));
        assertNotNull(plan1.getReturnTime());
    }

    @Test
    public void testBatchCreateReceivablePlan_periodSequence() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setPeriodCount(4);
        reqVO.setTotalPrice(new BigDecimal("40000.00"));
        reqVO.setReturnType(1);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(reqVO);
        assertEquals(4, ids.size());

        CrmReceivablePlanDO p1 = receivablePlanMapper.selectById(ids.get(0));
        CrmReceivablePlanDO p2 = receivablePlanMapper.selectById(ids.get(1));
        CrmReceivablePlanDO p3 = receivablePlanMapper.selectById(ids.get(2));
        CrmReceivablePlanDO p4 = receivablePlanMapper.selectById(ids.get(3));

        assertTrue(p1.getPeriod() < p2.getPeriod());
        assertTrue(p2.getPeriod() < p3.getPeriod());
        assertTrue(p3.getPeriod() < p4.getPeriod());
    }

    @Test
    public void testBatchCreateReceivablePlan_statusIsUncompleted() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setPeriodCount(1);
        reqVO.setTotalPrice(new BigDecimal("5000.00"));
        reqVO.setReturnType(1);

        List<Long> ids = receivablePlanService.batchCreateReceivablePlan(reqVO);
        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(ids.get(0));
        assertEquals(CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus(), plan.getStatus());
    }

    // ==================== 汇总统计测试 ====================

    @Test
    public void testGetReceivablePlanSummary_withData() {
        insertPlan("2026-01", "10000", "5000", null);
        insertPlan("2026-01", "20000", "20000", TEST_USER_ID + 1);
        insertPlan("2026-02", "15000", "0", null);

        CrmReceivablePlanSummaryReqVO reqVO = new CrmReceivablePlanSummaryReqVO();
        reqVO.setYear(2026);

        List<CrmReceivablePlanSummaryRespVO> result = receivablePlanService.getReceivablePlanSummary(reqVO);
        assertNotNull(result);
        assertTrue(result.size() >= 2);
    }

    @Test
    public void testGetReceivablePlanSummary_emptyResult() {
        CrmReceivablePlanSummaryReqVO reqVO = new CrmReceivablePlanSummaryReqVO();
        reqVO.setYear(2099);
        List<CrmReceivablePlanSummaryRespVO> result = receivablePlanService.getReceivablePlanSummary(reqVO);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testGetReceivablePlanSummary_targetAndCompletedPrice() {
        insertPlan("2026-03", "50000", "50000", TEST_USER_ID + 2);
        insertPlan("2026-03", "10000", "0", null);

        CrmReceivablePlanSummaryReqVO reqVO = new CrmReceivablePlanSummaryReqVO();
        reqVO.setYear(2026);

        List<CrmReceivablePlanSummaryRespVO> result = receivablePlanService.getReceivablePlanSummary(reqVO);
        assertNotNull(result);

        CrmReceivablePlanSummaryRespVO m03 = result.stream()
                .filter(r -> "2026-03".equals(r.getMonth())).findFirst().orElse(null);
        assertNotNull(m03);
        assertEquals(0, m03.getTargetPrice().compareTo(new BigDecimal("60000.00")));
        assertEquals(0, m03.getCompletedPrice().compareTo(new BigDecimal("50000.00")));
        assertEquals(0, m03.getUncompletedPrice().compareTo(new BigDecimal("10000.00")));
        assertTrue(m03.getCompletionRate().compareTo(BigDecimal.ZERO) > 0);
    }

    // ==================== 报表查询测试 ====================

    @Test
    public void testGetReceivablePlanReport_withData() {
        insertPlan("2026-01", "10000", "10000", TEST_USER_ID);
        insertPlan("2026-02", "20000", "0", TEST_USER_ID);

        CrmReceivablePlanReportReqVO reqVO = new CrmReceivablePlanReportReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(10);
        reqVO.setYear(2026);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(reqVO);
        assertNotNull(result);
        assertTrue(result.getTotal() >= 2);
        assertTrue(result.getList().size() >= 2);
    }

    @Test
    public void testGetReceivablePlanReport_withOwnerFilter() {
        insertPlan("2026-04", "10000", "0", TEST_USER_ID);
        insertPlan("2026-04", "20000", "0", TEST_USER_ID + 99);

        CrmReceivablePlanReportReqVO reqVO = new CrmReceivablePlanReportReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(10);
        reqVO.setYear(2026);
        reqVO.setOwnerUserId(TEST_USER_ID);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(reqVO);
        assertNotNull(result);
        for (CrmReceivablePlanDO plan : result.getList()) {
            assertEquals(TEST_USER_ID, plan.getOwnerUserId());
        }
    }

    @Test
    public void testGetReceivablePlanReport_pagination() {
        for (int i = 1; i <= 5; i++) {
            insertPlan("2026-05", "1000", "0", TEST_USER_ID);
        }

        CrmReceivablePlanReportReqVO reqVO = new CrmReceivablePlanReportReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(2);
        reqVO.setYear(2026);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(reqVO);
        assertNotNull(result);
        assertEquals(2, result.getList().size());
        assertTrue(result.getTotal() >= 5);
    }

    @Test
    public void testGetReceivablePlanReport_emptyResult() {
        CrmReceivablePlanReportReqVO reqVO = new CrmReceivablePlanReportReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(10);
        reqVO.setYear(2099);

        PageResult<CrmReceivablePlanDO> result = receivablePlanService.getReceivablePlanReport(reqVO);
        assertNotNull(result);
        assertEquals(0, result.getTotal());
        assertTrue(result.getList().isEmpty());
    }

    // ==================== 单条创建（含status） ====================

    @Test
    public void testCreateReceivablePlan_setsPercentField() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanSaveReqVO reqVO = new CrmReceivablePlanSaveReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setReturnTime(LocalDateTime.now().plusDays(30));
        reqVO.setPrice(new BigDecimal("5000.00"));
        reqVO.setReturnType(1);
        reqVO.setPercent(new BigDecimal("25.00"));

        Long id = receivablePlanService.createReceivablePlan(reqVO);
        CrmReceivablePlanDO plan = receivablePlanMapper.selectById(id);
        assertNotNull(plan);
        assertEquals(0, plan.getPercent().compareTo(new BigDecimal("25.00")));
    }

    // ==================== 数据权限创建验证 ====================

    @Test
    public void testBatchCreate_createsPermissionForEachPlan() {
        doNothing().when(adminUserApi).validateUser(any());
        when(contractService.getContract(TEST_CONTRACT_ID))
                .thenReturn(buildContract(TEST_CONTRACT_ID, TEST_CUSTOMER_ID));

        CrmReceivablePlanBatchCreateReqVO reqVO = new CrmReceivablePlanBatchCreateReqVO();
        reqVO.setContractId(TEST_CONTRACT_ID);
        reqVO.setOwnerUserId(TEST_USER_ID);
        reqVO.setPeriodCount(2);
        reqVO.setTotalPrice(new BigDecimal("20000.00"));
        reqVO.setReturnType(1);

        receivablePlanService.batchCreateReceivablePlan(reqVO);

        verify(permissionService, atLeast(2)).createPermission(any(CrmPermissionCreateReqBO.class));
    }

    // ==================== 辅助方法 ====================

    private CrmContractDO buildContract(Long id, Long customerId) {
        CrmContractDO contract = new CrmContractDO();
        contract.setId(id);
        contract.setCustomerId(customerId);
        contract.setTotalPrice(new BigDecimal("100000.00"));
        contract.setName("测试合同");
        contract.setNo("TC-" + id);
        return contract;
    }

    private void insertPlan(String yearMonth, String priceStr, String receivablePrice, Long ownerUserId) {
        CrmReceivablePlanDO plan = new CrmReceivablePlanDO();
        plan.setPeriod(1);
        plan.setContractId(TEST_CONTRACT_ID);
        plan.setCustomerId(TEST_CUSTOMER_ID);
        plan.setOwnerUserId(ownerUserId != null ? ownerUserId : TEST_USER_ID);
        plan.setPrice(new BigDecimal(priceStr));
        String[] parts = yearMonth.split("-");
        plan.setReturnTime(LocalDateTime.of(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), 15, 0, 0));
        plan.setStatus(CrmReceivablePlanStatusEnum.UNCOMPLETED.getStatus());
        plan.setCreator("1");
        if (receivablePrice != null && !"0".equals(receivablePrice)) {
            plan.setReceivableId(RandomUtils.randomLongId());
        }
        receivablePlanMapper.insert(plan);
    }
}
