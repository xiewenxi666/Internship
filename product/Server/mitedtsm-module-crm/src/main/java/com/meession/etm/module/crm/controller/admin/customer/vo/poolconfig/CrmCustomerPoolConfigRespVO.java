package com.meession.etm.module.crm.controller.admin.customer.vo.poolconfig;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理后台 - CRM 客户公海规则 Response VO
 */
@Schema(description = "管理后台 - CRM 客户公海规则 Response VO")
@Data
public class CrmCustomerPoolConfigRespVO {

    /** 是否启用客户公海 */
    @Schema(description = "是否启用客户公海", requiredMode = Schema.RequiredMode.REQUIRED, example = "true")
    @NotNull(message = "是否启用客户公海不能为空")
    private Boolean enabled;

    /** 未跟进放入公海天数 */
    @Schema(description = "未跟进放入公海天数", example = "2")
    private Integer contactExpireDays;

    /** 未成交放入公海天数 */
    @Schema(description = "未成交放入公海天数", example = "2")
    private Integer dealExpireDays;

    /** 是否开启提前提醒 */
    @Schema(description = "是否开启提前提醒", example = "true")
    private Boolean notifyEnabled;

    /** 提前提醒天数 */
    @Schema(description = "提前提醒天数", example = "2")
    private Integer notifyDays;

}
