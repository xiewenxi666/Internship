package com.meession.etm.module.crm.controller.admin.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - CRM 财务数据汇总 Response VO")
@Data
public class CrmFinanceSummaryRespVO {

    @Schema(description = "回款总额", example = "150000.00")
    private BigDecimal totalReceivablePrice;

    @Schema(description = "回款数量", example = "10")
    private Long receivableCount;

    @Schema(description = "开票总额", example = "200000.00")
    private BigDecimal totalInvoicePrice;

    @Schema(description = "发票数量", example = "20")
    private Long invoiceCount;

    @Schema(description = "报销总额", example = "50000.00")
    private BigDecimal totalReimbursementPrice;

    @Schema(description = "报销数量", example = "15")
    private Long reimbursementCount;

    @Schema(description = "退款总额", example = "30000.00")
    private BigDecimal totalRefundPrice;

    @Schema(description = "退款数量", example = "5")
    private Long refundCount;

    @Schema(description = "月度统计")
    private List<CrmFinanceMonthlyVO> monthlyStats;

    @Schema(description = "按类型统计")
    private List<CrmFinanceTypeVO> typeStats;

}
