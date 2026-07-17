package com.meession.etm.module.oa.controller.admin.oa.vo.loan;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 借款申请 Response VO")
@Data
public class OaLoanRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "借款用途", example = "1")
    private Integer purpose;

    @Schema(description = "借款金额（分）", example = "50000")
    private Long amount;

    @Schema(description = "预计归还时间")
    private LocalDateTime expectedRepayTime;

    @Schema(description = "借款原因", example = "出差预支")
    private String reason;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "审批结果", example = "1")
    private Integer status;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}
