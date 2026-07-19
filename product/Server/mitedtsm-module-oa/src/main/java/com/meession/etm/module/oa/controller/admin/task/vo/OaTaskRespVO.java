package com.meession.etm.module.oa.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 任务 Response VO")
@Data
public class OaTaskRespVO {

    @Schema(description = "任务主键", example = "1024")
    private Long id;

    @Schema(description = "任务标题", example = "完成OA模块开发")
    private String title;

    @Schema(description = "任务描述", example = "完成请假、出差等模块")
    private String description;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "关联项目编号", example = "10")
    private Long projectId;

    @Schema(description = "执行人用户编号", example = "2")
    private Long assigneeUserId;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "截止时间")
    private LocalDateTime deadline;

    @Schema(description = "完成时间")
    private LocalDateTime completedTime;

    @Schema(description = "进度百分比", example = "80")
    private Integer progress;

    @Schema(description = "标签", example = "开发,紧急")
    private String tags;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}