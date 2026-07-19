package com.meession.etm.module.oa.controller.admin.task.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 任务更新 Request VO")
@Data
public class OaTaskUpdateReqVO {

    @Schema(description = "任务标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "完成OA模块开发")
    @NotEmpty(message = "任务标题不能为空")
    private String title;

    @Schema(description = "任务描述", example = "完成请假、出差等模块开发")
    private String description;

    @Schema(description = "优先级", example = "1")
    private Integer priority;

    @Schema(description = "关联项目编号", example = "10")
    private Long projectId;

    @Schema(description = "执行人用户编号", example = "2")
    private Long assigneeUserId;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime deadline;

    @Schema(description = "标签", example = "开发,紧急")
    private String tags;

    @Schema(description = "附件地址", example = "/file/doc.pdf")
    private String attachment;

}
