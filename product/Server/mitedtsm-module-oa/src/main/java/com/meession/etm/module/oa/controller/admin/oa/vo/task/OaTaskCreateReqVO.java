package com.meession.etm.module.oa.controller.admin.oa.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 任务创建 Request VO")
@Data
public class OaTaskCreateReqVO {

    @NotBlank(message = "任务标题不能为空")
    @Schema(description = "任务标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "完成OA模块开发")
    private String title;

    @Schema(description = "任务描述", example = "完成OA模块的后端开发工作")
    private String description;

    @Schema(description = "负责人ID")
    private Long assigneeId;

    @Schema(description = "优先级: 1-低 2-中 3-高", example = "2")
    private Integer priority;

    @Schema(description = "截止时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime deadline;

}
