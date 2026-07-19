package com.meession.etm.module.crm.controller.admin.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - CRM 财务月度统计 Response VO")
@Data
public class CrmFinanceMonthlyVO {

    @Schema(description = "月份", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer month;

    @Schema(description = "回款金额", example = "10000.00")
    private BigDecimal receivablePrice;

    @Schema(description = "开票金额", example = "20000.00")
    private BigDecimal invoicePrice;

    @Schema(description = "报销金额", example = "5000.00")
    private BigDecimal reimbursementPrice;

    @Schema(description = "退款金额", example = "3000.00")
    private BigDecimal refundPrice;

}
