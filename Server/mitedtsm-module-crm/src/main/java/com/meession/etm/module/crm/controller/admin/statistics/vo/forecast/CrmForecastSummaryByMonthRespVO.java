package com.meession.etm.module.crm.controller.admin.statistics.vo.forecast;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class CrmForecastSummaryByMonthRespVO {

    @Schema(description = "月份，示例：2025-01", requiredMode = Schema.RequiredMode.REQUIRED, example = "2025-01")
    private String month;

    @Schema(description = "商机数", requiredMode = Schema.RequiredMode.REQUIRED, example = "5")
    private Long businessCount;

    @Schema(description = "总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    private Double totalPrice;

    @Schema(description = "加权金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "5000.00")
    private Double probabilityPrice;

}
