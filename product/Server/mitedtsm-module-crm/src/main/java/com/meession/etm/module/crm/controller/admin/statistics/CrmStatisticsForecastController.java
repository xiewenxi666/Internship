package com.meession.etm.module.crm.controller.admin.statistics;

import com.meession.etm.framework.common.pojo.CommonResult;
import com.meession.etm.module.crm.controller.admin.statistics.vo.forecast.CrmForecastSummaryByMonthRespVO;
import com.meession.etm.module.crm.controller.admin.statistics.vo.forecast.CrmForecastSummaryRespVO;
import com.meession.etm.module.crm.service.statistics.CrmStatisticsDealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.meession.etm.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - CRM 销售预测")
@RestController
@RequestMapping("/crm/statistics-forecast")
@Validated
public class CrmStatisticsForecastController {

    @Resource
    private CrmStatisticsDealService dealService;

    @GetMapping("/get-forecast-summary")
    @Operation(summary = "获取销售预测汇总")
    @PreAuthorize("@ss.hasPermission('crm:statistics-forecast:query')")
    public CommonResult<CrmForecastSummaryRespVO> getForecastSummary(
            @RequestParam Long deptId,
            @RequestParam(required = false) Long userId,
            @RequestParam Integer year) {
        return success(dealService.getForecastSummary(deptId, userId, year));
    }

    @GetMapping("/get-forecast-by-month")
    @Operation(summary = "获取销售预测按月数据")
    @PreAuthorize("@ss.hasPermission('crm:statistics-forecast:query')")
    public CommonResult<List<CrmForecastSummaryByMonthRespVO>> getForecastByMonth(
            @RequestParam Long deptId,
            @RequestParam(required = false) Long userId,
            @RequestParam Integer year) {
        return success(dealService.getForecastByMonth(deptId, userId, year));
    }

}
