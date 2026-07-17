package com.meession.etm.module.oa.controller.admin.oa.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 请示申请创建 Request VO")
@Data
public class OaRequestCreateReqVO {

    @NotBlank(message = "标题不能为空")
    @Schema(description = "标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "关于采购办公用品的请示")
    private String title;

    @NotBlank(message = "内容不能为空")
    @Schema(description = "内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "因公司业务发展需要，拟采购一批办公用品...")
    private String content;

    @Schema(description = "紧急程度 1-普通 2-紧急 3-特急", example = "1")
    private Integer urgency;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

}
