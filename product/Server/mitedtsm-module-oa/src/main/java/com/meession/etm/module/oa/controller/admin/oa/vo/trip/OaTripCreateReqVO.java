package com.meession.etm.module.oa.controller.admin.oa.vo.trip;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 出差申请创建 Request VO")
@Data
public class OaTripCreateReqVO {

    @Schema(description = "出差类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer type;

    @NotBlank(message = "目的地不能为空")
    @Schema(description = "目的地", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京")
    private String destination;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "出差原因", requiredMode = Schema.RequiredMode.REQUIRED, example = "客户拜访")
    private String reason;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

    @AssertTrue(message = "结束时间，需要在开始时间之后")
    public boolean isEndTimeValid() {
        return getEndTime() != null && getStartTime() != null && !getEndTime().isBefore(getStartTime());
    }

}
