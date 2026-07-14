/**
 * 订单更新状态 Request VO
 *
 * @author 23计三倪雨晗
 */
package com.meession.etm.module.crm.controller.admin.order.vo.order;

import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.order.CrmOrderStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "管理后台 - CRM 订单更新状态 Request VO")
@Data
public class CrmOrderUpdateStatusReqVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @NotNull(message = "订单编号不能为空")
    private Long id;

    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "40")
    @NotNull(message = "订单状态不能为空")
    @InEnum(value = CrmOrderStatusEnum.class)
    private Integer status;

}
