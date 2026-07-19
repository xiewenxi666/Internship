package com.meession.etm.module.oa.controller.admin.visit.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 拜访记录创建 Request VO")
@Data
public class OaVisitCreateReqVO {

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "密讯科技")
    @NotEmpty(message = "客户名称不能为空")
    private String customerName;

    @Schema(description = "关联CRM客户编号", example = "1024")
    private Long crmCustomerId;

    @Schema(description = "联系人", example = "张三")
    private String contactPerson;

    @Schema(description = "联系电话", example = "13800138000")
    private String contactPhone;

    @Schema(description = "拜访地址", example = "上海市浦东新区")
    private String visitAddress;

    @Schema(description = "拜访时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "拜访时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime visitTime;

    @Schema(description = "拜访目的", requiredMode = Schema.RequiredMode.REQUIRED, example = "产品演示")
    @NotEmpty(message = "拜访目的不能为空")
    private String purpose;

    @Schema(description = "拜访结果", example = "客户表示感兴趣，需进一步跟进")
    private String result;

    @Schema(description = "下次拜访时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime nextVisitTime;

    @Schema(description = "审批发起人选", example = "{\"assignedUser\":[1,2]}")
    private String startUserSelectAssignees;

}