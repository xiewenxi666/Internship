package com.meession.etm.module.oa.controller.admin.oa.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 任务 Response VO")
@Data
public class OaTaskRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "任务标题", example = "完成OA模块开发")
    private String title;

    @Schema(description = "任务描述")
    private String description;

    @Schema(description = "负责人ID")
    private Long assigneeId;

    @Schema(description = "优先级: 1-低 2-中 3-高", example = "2")
    private Integer priority;

    @Schema(description = "状态: 1-待办 2-进行中 3-已完成", example = "1")
    private Integer status;

    @Schema(description = "截止时间")
    private LocalDateTime deadline;

    @Schema(description = "完成时间")
    private LocalDateTime completedTime;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}
