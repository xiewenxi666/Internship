package com.meession.etm.module.crm.controller.admin.statistics;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.deal.CrmDealPageReqVO;
import com.meession.etm.module.crm.controller.admin.statistics.vo.deal.CrmDealSummaryRespVO;
import com.meession.etm.module.crm.service.statistics.CrmStatisticsDealService;
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
import java.util.Map;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 成交商机统计")
@RestController
@RequestMapping("/crm/statistics-deal")
@Validated
public class CrmStatisticsDealController {

    @Resource
    private CrmStatisticsDealService dealService;

    @GetMapping("/get-deal-business-summary")
    @Operation(summary = "成交商机报表汇总")
    @PreAuthorize("@ss.hasPermission('crm:statistics-deal:query')")
    public CommonResult<CrmDealSummaryRespVO> getDealBusinessSummary(CrmDealPageReqVO reqVO) {
        return success(dealService.getDealSummary(reqVO.getDeptId(), reqVO.getUserId(), reqVO.getYear()));
    }

    @GetMapping("/get-deal-business-page")
    @Operation(summary = "成交商机报表分页")
    @PreAuthorize("@ss.hasPermission('crm:statistics-deal:query')")
    public CommonResult<PageResult<Map<String, Object>>> getDealBusinessPage(CrmDealPageReqVO reqVO) {
        return success(dealService.getDealBusinessPage(reqVO.getDeptId(), reqVO.getUserId(), reqVO.getYear(),
                reqVO.getPageNo(), reqVO.getPageSize()));
    }

    @GetMapping("/get-funnel-report-summary")
    @Operation(summary = "销售漏斗报表汇总")
    @PreAuthorize("@ss.hasPermission('crm:statistics-deal:query')")
    public CommonResult<CrmDealSummaryRespVO> getFunnelReportSummary(CrmDealPageReqVO reqVO) {
        return success(dealService.getFunnelReportSummary(reqVO.getDeptId(), reqVO.getUserId(), reqVO.getYear()));
    }

    @GetMapping("/get-active-business-page")
    @Operation(summary = "活跃商机分页")
    @PreAuthorize("@ss.hasPermission('crm:statistics-deal:query')")
    public CommonResult<PageResult<Map<String, Object>>> getActiveBusinessPage(CrmDealPageReqVO reqVO) {
        return success(dealService.getActiveBusinessPage(reqVO.getDeptId(), reqVO.getUserId(), reqVO.getYear(),
                reqVO.getPageNo(), reqVO.getPageSize()));
    }

}
