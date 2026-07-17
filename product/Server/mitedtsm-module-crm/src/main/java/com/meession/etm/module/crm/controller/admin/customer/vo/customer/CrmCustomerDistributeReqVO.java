package com.meession.etm.module.crm.controller.admin.customer.vo.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 管理后台 - CRM 客户分配公海给对应负责人 Request VO
 */
@Schema(description = "管理后台 - CRM 客户分配公海给对应负责人 Request VO")
@Data
public class CrmCustomerDistributeReqVO {

    /** 客户编号 */
    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "[1024]")
    @NotEmpty(message = "客户编号不能为空")
    private List<Long> ids;

    /** 负责人 */
    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "负责人不能为空")
    private Long ownerUserId;

}
