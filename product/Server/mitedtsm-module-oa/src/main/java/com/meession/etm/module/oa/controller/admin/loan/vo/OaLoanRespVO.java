package com.meession.etm.module.oa.controller.admin.loan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 借款申请 Response VO")
@Data
public class OaLoanRespVO {

    @Schema(description = "借款表单主键", example = "1024")
    private Long id;

    @Schema(description = "借款金额", example = "10000.00")
    private BigDecimal amount;

    @Schema(description = "借款事由", example = "出差预支费用")
    private String purpose;

    @Schema(description = "还款计划", example = "下月工资扣除")
    private String repaymentPlan;

    @Schema(description = "预计还款时间")
    private LocalDateTime expectedRepaymentTime;

    @Schema(description = "实际还款时间")
    private LocalDateTime actualRepaymentTime;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "流程编号")
    private String processInstanceId;

    @Schema(description = "申请时间")
    private LocalDateTime createTime;

}