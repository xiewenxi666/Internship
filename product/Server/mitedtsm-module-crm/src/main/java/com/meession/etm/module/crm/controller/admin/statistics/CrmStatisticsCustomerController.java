package com.meession.etm.module.crm.controller.admin.statistics;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.customer.*;
import com.meession.etm.module.crm.service.statistics.CrmStatisticsCustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 客户统计")
@RestController
@RequestMapping("/crm/statistics-customer")
@Validated
/**
 * CRM 客户统计 Controller (Admin)
 */
public class CrmStatisticsCustomerController {

    @Resource
    private CrmStatisticsCustomerService customerService;

    // ==================== 客户总量分析 ====================

    /**
     * 获取客户总量分析(按日期)
     */
    @GetMapping("/get-customer-summary-by-date")
    @Operation(summary = "获取客户总量分析(按日期)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerSummaryByDateRespVO>> getCustomerSummaryByDate(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getCustomerSummaryByDate(reqVO));
    }

    /**
     * 获取客户总量分析(按用户)
     */
    @GetMapping("/get-customer-summary-by-user")
    @Operation(summary = "获取客户总量分析(按用户)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerSummaryByUserRespVO>> getCustomerSummaryByUser(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getCustomerSummaryByUser(reqVO));
    }

    // ==================== 客户跟进次数分析 ====================

    /**
     * 获取客户跟进次数分析(按日期)
     */
    @GetMapping("/get-follow-up-summary-by-date")
    @Operation(summary = "获取客户跟进次数分析(按日期)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsFollowUpSummaryByDateRespVO>> getFollowupSummaryByDate(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getFollowUpSummaryByDate(reqVO));
    }

    /**
     * 获取客户跟进次数分析(按用户)
     */
    @GetMapping("/get-follow-up-summary-by-user")
    @Operation(summary = "获取客户跟进次数分析(按用户)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsFollowUpSummaryByUserRespVO>> getFollowUpSummaryByUser(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getFollowUpSummaryByUser(reqVO));
    }

    /**
     * 获取客户跟进次数分析(按类型)
     */
    @GetMapping("/get-follow-up-summary-by-type")
    @Operation(summary = "获取客户跟进次数分析(按类型)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsFollowUpSummaryByTypeRespVO>> getFollowUpSummaryByType(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getFollowUpSummaryByType(reqVO));
    }

    // ==================== 客户转化率分析 ====================

    /**
     * 获取客户的首次合同、回款信息列表，用于客户转化率页面
     */
    @GetMapping("/get-contract-summary")
    @Operation(summary = "获取客户的首次合同、回款信息列表", description = "用于【客户转化率】页面")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerContractSummaryRespVO>> getContractSummary(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getContractSummary(reqVO));
    }

    // ==================== 公海客户分析 ====================

    /**
     * 获取公海客户分析(按日期)
     */
    @GetMapping("/get-pool-summary-by-date")
    @Operation(summary = "获取公海客户分析(按日期)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsPoolSummaryByDateRespVO>> getPoolSummaryByDate(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getPoolSummaryByDate(reqVO));
    }

    /**
     * 获取公海客户分析(按用户)
     */
    @GetMapping("/get-pool-summary-by-user")
    @Operation(summary = "获取公海客户分析(按用户)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsPoolSummaryByUserRespVO>> getPoolSummaryByUser(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getPoolSummaryByUser(reqVO));
    }

    // ==================== 客户成交周期分析 ====================

    /**
     * 获取客户成交周期(按日期)
     */
    @GetMapping("/get-customer-deal-cycle-by-date")
    @Operation(summary = "获取客户成交周期(按日期)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerDealCycleByDateRespVO>> getCustomerDealCycleByDate(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getCustomerDealCycleByDate(reqVO));
    }

    /**
     * 获取客户成交周期(按用户)
     */
    @GetMapping("/get-customer-deal-cycle-by-user")
    @Operation(summary = "获取客户成交周期(按用户)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerDealCycleByUserRespVO>> getCustomerDealCycleByUser(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getCustomerDealCycleByUser(reqVO));
    }

    /**
     * 获取客户成交周期(按地区)
     */
    @GetMapping("/get-customer-deal-cycle-by-area")
    @Operation(summary = "获取客户成交周期(按地区)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerDealCycleByAreaRespVO>> getCustomerDealCycleByArea(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getCustomerDealCycleByArea(reqVO));
    }

    /**
     * 获取客户成交周期(按产品)
     */
    @GetMapping("/get-customer-deal-cycle-by-product")
    @Operation(summary = "获取客户成交周期(按产品)")
    @PreAuthorize("@ss.hasPermission('crm:statistics-customer:query')")
    public CommonResult<List<CrmStatisticsCustomerDealCycleByProductRespVO>> getCustomerDealCycleByProduct(@Valid CrmStatisticsCustomerReqVO reqVO) {
        return success(customerService.getCustomerDealCycleByProduct(reqVO));
    }

}
