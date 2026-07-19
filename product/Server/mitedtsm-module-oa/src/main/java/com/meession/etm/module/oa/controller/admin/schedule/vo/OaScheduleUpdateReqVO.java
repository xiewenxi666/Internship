package com.meession.etm.module.oa.controller.admin.schedule.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程更新 Request VO")
@Data
public class OaScheduleUpdateReqVO {

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "项目周会")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "描述", example = "讨论本周进展")
    private String description;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "是否全天", example = "false")
    private Boolean isAllDay;

    @Schema(description = "地点", example = "会议室A")
    private String location;

    @Schema(description = "日程类型", example = "2")
    private Integer type;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "提前提醒分钟数", example = "15")
    private Integer reminderTime;

    @Schema(description = "颜色标识", example = "#1890ff")
    private String color;

}
