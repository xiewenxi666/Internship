package com.meession.etm.module.oa.controller.admin.workReport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - 工作报告创建 Request VO")
@Data
public class OaWorkReportCreateReqVO {

    @Schema(description = "报告类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "报告类型不能为空")
    private Integer type;

    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "今日工作总结")
    @NotEmpty(message = "标题不能为空")
    private String title;

    @Schema(description = "报告内容", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "报告内容不能为空")
    private String content;

    @Schema(description = "计划", example = "明天继续推进项目")
    private String plan;

    @Schema(description = "总结", example = "完成核心功能开发")
    private String summary;

    @Schema(description = "报告日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报告日期不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate reportDate;

}