package com.meession.etm.module.oa.controller.admin.workReport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 工作报告 Response VO")
@Data
public class OaWorkReportRespVO {

    @Schema(description = "工作报告主键", example = "1024")
    private Long id;

    @Schema(description = "报告类型", example = "1")
    private Integer type;

    @Schema(description = "标题", example = "今日工作总结")
    private String title;

    @Schema(description = "报告内容", example = "完成了OA模块开发")
    private String content;

    @Schema(description = "计划", example = "明天继续")
    private String plan;

    @Schema(description = "总结", example = "进展顺利")
    private String summary;

    @Schema(description = "报告日期")
    private LocalDate reportDate;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "审阅人用户编号")
    private Long reviewerUserId;

    @Schema(description = "审阅时间")
    private LocalDateTime reviewTime;

    @Schema(description = "审阅意见")
    private String reviewContent;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

}