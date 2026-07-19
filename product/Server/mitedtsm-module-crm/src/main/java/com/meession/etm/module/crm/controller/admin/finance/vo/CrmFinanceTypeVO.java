package com.meession.etm.module.crm.controller.admin.finance.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - CRM 财务类型统计 Response VO")
@Data
public class CrmFinanceTypeVO {

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "回款方式-1")
    private String name;

    @Schema(description = "模块", requiredMode = Schema.RequiredMode.REQUIRED, example = "receivable")
    private String module;

    @Schema(description = "金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    private BigDecimal price;

}
