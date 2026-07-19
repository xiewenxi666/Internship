package com.meession.etm.module.oa.controller.admin.request.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 请示申请 Response VO")
@Data
public class OaRequestRespVO {

    @Schema(description = "请示表单主键", example = "1024")
    private Long id;

    @Schema(description = "请示标题", example = "关于采购新设备的请示")
    private String title;

    @Schema(description = "请示内容", example = "建议采购...")
    private String content;

    @Schema(description = "请示类型", example = "1")
    private Integer type;

    @Schema(description = "紧急程度", example = "0")
    private Integer urgency;

    @Schema(description = "附件地址", example = "/file/abc.pdf")
    private String attachment;

    @Schema(description = "涉及金额（元）", example = "50000.00")
    private BigDecimal expectedAmount;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}