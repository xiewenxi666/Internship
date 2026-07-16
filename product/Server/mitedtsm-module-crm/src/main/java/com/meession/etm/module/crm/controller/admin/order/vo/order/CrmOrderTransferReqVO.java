/**
 * 订单转移 Request VO
 *
 * @author 23计三倪雨晗
 */
package com.meession.etm.module.crm.controller.admin.order.vo.order;

import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "管理后台 - CRM 订单转移 Request VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CrmOrderTransferReqVO {

    /** 订单编号 */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @NotNull(message = "订单编号不能为空")
    private Long id;

    /** 新负责人的用户编号 */
    @Schema(description = "新负责人的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @NotNull(message = "新负责人的用户编号不能为空")
    private Long newOwnerUserId;

    /** 老负责人加入团队后的权限级别 */
    @Schema(description = "老负责人加入团队后的权限级别", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @InEnum(value = CrmPermissionLevelEnum.class)
    private Integer oldOwnerPermissionLevel;

}
