// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.controller.admin.workorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 工单创建/更新 Request VO")
@Data
public class CrmWorkOrderSaveReqVO {

    @Schema(description = "工单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "工单标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "设备维修")
    @NotEmpty(message = "工单标题不能为空")
    private String title;

    @Schema(description = "工单类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "工单类型不能为空")
    private Integer type;

    @Schema(description = "优先级", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "优先级不能为空")
    private Integer priority;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "负责人不能为空")
    private Long ownerUserId;

    @Schema(description = "描述", example = "设备异响需要检修")
    private String description;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

}
