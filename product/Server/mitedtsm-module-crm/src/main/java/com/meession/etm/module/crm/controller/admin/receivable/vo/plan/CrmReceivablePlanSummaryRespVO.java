package com.meession.etm.module.crm.controller.admin.receivable.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - CRM 回款计划汇总统计 Response VO")
@Data
public class CrmReceivablePlanSummaryRespVO {

    @Schema(description = "月份", example = "2026-01")
    private String month;

    @Schema(description = "目标金额", example = "100000.00")
    private BigDecimal targetPrice;

    @Schema(description = "已完成金额", example = "80000.00")
    private BigDecimal completedPrice;

    @Schema(description = "完成率", example = "80.00")
    private BigDecimal completionRate;

    @Schema(description = "未完成金额", example = "20000.00")
    private BigDecimal uncompletedPrice;

    @Schema(description = "已开票金额", example = "75000.00")
    private BigDecimal invoicedPrice;

}
