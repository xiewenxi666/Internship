package com.meession.etm.module.oa.controller.admin.loan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 借款申请创建 Request VO")
@Data
public class OaLoanCreateReqVO {

    @Schema(description = "借款金额（元）", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    @NotNull(message = "借款金额不能为空")
    private BigDecimal amount;

    @Schema(description = "借款事由", requiredMode = Schema.RequiredMode.REQUIRED, example = "出差预支费用")
    @NotEmpty(message = "借款事由不能为空")
    private String purpose;

    @Schema(description = "还款计划", example = "下月工资扣除")
    private String repaymentPlan;

    @Schema(description = "预计还款时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime expectedRepaymentTime;

    @Schema(description = "发起人自选审批人 Map")
    private Map<String, List<Long>> startUserSelectAssignees;

}