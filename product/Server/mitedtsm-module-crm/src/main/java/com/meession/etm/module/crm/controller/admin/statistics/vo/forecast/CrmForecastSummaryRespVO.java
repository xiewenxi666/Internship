package com.meession.etm.module.crm.controller.admin.statistics.vo.forecast;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - CRM 预测汇总 Response VO")
@Data
public class CrmForecastSummaryRespVO {

    @Schema(description = "商机数", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
    private Long businessCount;

    @Schema(description = "总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    private Double totalPrice;

    @Schema(description = "加权金额（总价 × 成交概率）", requiredMode = Schema.RequiredMode.REQUIRED, example = "5000.00")
    private Double probabilityPrice;

}
