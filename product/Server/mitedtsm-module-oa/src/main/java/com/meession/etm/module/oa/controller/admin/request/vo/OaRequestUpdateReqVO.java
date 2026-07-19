package com.meession.etm.module.oa.controller.admin.request.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 请示申请更新 Request VO")
@Data
public class OaRequestUpdateReqVO {

    @Schema(description = "请示标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "关于采购新设备的请示")
    @NotEmpty(message = "请示标题不能为空")
    private String title;

    @Schema(description = "请示内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "建议采购...")
    @NotEmpty(message = "请示内容不能为空")
    private String content;

    @Schema(description = "请示类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "请示类型不能为空")
    private Integer type;

    @Schema(description = "紧急程度", example = "0")
    private Integer urgency;

    @Schema(description = "附件地址", example = "/file/abc.pdf")
    private String attachment;

    @Schema(description = "涉及金额（元）", example = "50000.00")
    private BigDecimal expectedAmount;

    @Schema(description = "发起人自选审批人 Map")
    private Map<String, List<Long>> startUserSelectAssignees;

}
