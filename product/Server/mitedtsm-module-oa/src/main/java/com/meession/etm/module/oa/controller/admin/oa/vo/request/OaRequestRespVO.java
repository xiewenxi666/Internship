package com.meession.etm.module.oa.controller.admin.oa.vo.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 请示申请 Response VO")
@Data
public class OaRequestRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "标题", example = "关于采购办公用品的请示")
    private String title;

    @Schema(description = "内容", example = "因公司业务发展需要...")
    private String content;

    @Schema(description = "紧急程度 1-普通 2-紧急 3-特急", example = "1")
    private Integer urgency;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}
