package com.meession.etm.module.oa.controller.admin.oa.vo.visit;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 拜访申请创建 Request VO")
@Data
public class OaVisitCreateReqVO {

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long customerId;

    @NotBlank(message = "联系人不能为空")
    @Schema(description = "联系人", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13800138000")
    private String contactPhone;

    @Schema(description = "拜访时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "拜访时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime visitTime;

    @Schema(description = "拜访地点", requiredMode = Schema.RequiredMode.REQUIRED, example = "北京市朝阳区")
    private String location;

    @Schema(description = "拜访目的", example = "商务洽谈")
    private String purpose;

    @Schema(description = "备注")
    private String notes;

    @Schema(description = "发起人自选审批人 Map", example = "{taskKey1: [1, 2]}")
    private Map<String, List<Long>> startUserSelectAssignees;

}
