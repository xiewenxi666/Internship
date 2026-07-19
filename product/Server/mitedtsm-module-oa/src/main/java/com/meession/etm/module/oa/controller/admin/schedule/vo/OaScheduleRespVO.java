package com.meession.etm.module.oa.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 日程 Response VO")
@Data
public class OaScheduleRespVO {

    @Schema(description = "日程主键", example = "1024")
    private Long id;

    @Schema(description = "标题", example = "项目周会")
    private String title;

    @Schema(description = "描述", example = "讨论本周进展")
    private String description;

    @Schema(description = "开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间")
    private LocalDateTime endTime;

    @Schema(description = "是否全天", example = "false")
    private Boolean isAllDay;

    @Schema(description = "地点", example = "会议室A")
    private String location;

    @Schema(description = "日程类型", example = "2")
    private Integer type;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "提前提醒分钟数", example = "15")
    private Integer reminderTime;

    @Schema(description = "颜色标识", example = "#1890ff")
    private String color;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}