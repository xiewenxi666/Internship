package com.meession.etm.module.crm.controller.admin.clue.vo;

import com.meession.etm.framework.common.validation.InEnum;
import com.meession.etm.module.crm.enums.permission.CrmPermissionLevelEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 管理后台 - 线索转移 Request VO
 */
@Schema(description = "管理后台 - 线索转移 Request VO")
@Data
public class CrmClueTransferReqVO {

    /** 线索编号 */
    @Schema(description = "线索编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @NotNull(message = "线索编号不能为空")
    private Long id;

    /** 新负责人的用户编号 */
    @Schema(description = "新负责人的用户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @NotNull(message = "新负责人的用户编号不能为空")
    private Long newOwnerUserId;

    /** 老负责人加入团队后的权限级别 */
    @Schema(description = "老负责人加入团队后的权限级别", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @InEnum(value = CrmPermissionLevelEnum.class)
    private Integer oldOwnerPermissionLevel; // 老负责人加入团队后的权限级别。如果 null 说明移除

}
