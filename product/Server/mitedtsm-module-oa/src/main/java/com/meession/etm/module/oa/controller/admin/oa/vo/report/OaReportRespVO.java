package com.meession.etm.module.oa.controller.admin.oa.vo.report;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作报告 Response VO")
@Data
public class OaReportRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "报告类型: 1-日报 2-周报 3-月报", example = "1")
    private Integer type;

    @Schema(description = "报告日期")
    private LocalDate reportDate;

    @Schema(description = "工作内容", example = "完成了OA模块的开发")
    private String content;

    @Schema(description = "工作计划", example = "明天继续开发测试")
    private String plan;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}
