package com.meession.etm.module.oa.controller.admin.oa.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;

@Schema(description = "管理后台 - 工作报告创建 Request VO")
@Data
public class OaReportCreateReqVO {

    @Schema(description = "报告类型: 1-日报 2-周报 3-月报", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "报告类型不能为空")
    private Integer type;

    @Schema(description = "报告日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "报告日期不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY)
    private LocalDate reportDate;

    @NotBlank(message = "内容不能为空")
    @Schema(description = "工作内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "完成了OA模块的开发")
    private String content;

    @Schema(description = "工作计划", example = "明天继续开发测试")
    private String plan;

}
