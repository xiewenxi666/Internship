package com.meession.etm.module.oa.controller.admin.oa.vo.schedule;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 日程创建 Request VO")
@Data
public class OaScheduleCreateReqVO {

    @NotBlank(message = "日程标题不能为空")
    @Schema(description = "日程标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "项目评审会议")
    private String title;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "是否全天", example = "false")
    private Boolean allDay;

    @Schema(description = "地点", example = "3楼会议室")
    private String location;

    @Schema(description = "备注", example = "请提前准备材料")
    private String description;

}
