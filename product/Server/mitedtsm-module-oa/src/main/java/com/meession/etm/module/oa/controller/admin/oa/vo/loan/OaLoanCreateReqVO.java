package com.meession.etm.module.oa.controller.admin.oa.vo.loan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 借款申请创建 Request VO")
@Data
public class OaLoanCreateReqVO {

    @Schema(description = "借款用途", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer purpose;

    @NotNull(message = "借款金额不能为空")
    @Schema(description = "借款金额（分）", requiredMode = Schema.RequiredMode.REQUIRED, example = "50000")
    private Long amount;

    @Schema(description = "预计归还时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime expectedRepayTime;

    @Schema(description = "借款原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "出差预支")
    private String reason;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

}
