package com.meession.etm.module.oa.controller.admin.businessTrip.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出差申请创建 Request VO")
@Data
public class OaBusinessTripCreateReqVO {

    @Schema(description = "出差目的地", requiredMode = Schema.RequiredMode.REQUIRED, example = "上海")
    @NotEmpty(message = "出差目的地不能为空")
    private String destination;

    @Schema(description = "出差事由", requiredMode = Schema.RequiredMode.REQUIRED, example = "客户拜访")
    @NotEmpty(message = "出差事由不能为空")
    private String reason;

    @Schema(description = "同行人员", example = "张三,李四")
    private String companion;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "交通工具", example = "高铁")
    private String vehicle;

    @Schema(description = "预估费用", example = "5000.00")
    private BigDecimal estimatedAmount;

    @Schema(description = "发起人自选审批人 Map")
    private Map<String, List<Long>> startUserSelectAssignees;

    @AssertTrue(message = "结束时间，需要在开始时间之后")
    public boolean isEndTimeValid() {
        return !getEndTime().isBefore(getStartTime());
    }

}