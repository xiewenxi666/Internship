package com.meession.etm.module.crm.controller.admin.campaign.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 营销活动创建/更新 Request VO")
@Data
public class CrmCampaignSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "活动标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "春季促销")
    @NotEmpty(message = "活动标题不能为空")
    private String title;

    @Schema(description = "活动类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "活动类型不能为空")
    private Integer type;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-10-01 00:00:00")
    @NotNull(message = "开始时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2023-10-31 23:59:59")
    @NotNull(message = "结束时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endTime;

    @Schema(description = "预计成本", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    @NotNull(message = "预计成本不能为空")
    private BigDecimal estimatedCost;

    @Schema(description = "预计收入", requiredMode = Schema.RequiredMode.REQUIRED, example = "50000.00")
    @NotNull(message = "预计收入不能为空")
    private BigDecimal estimatedRevenue;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "参与人员", example = "张三,李四")
    private String participants;

    @Schema(description = "活动地址", example = "北京市朝阳区")
    private String address;

    @Schema(description = "活动描述", example = "春季大型促销活动")
    private String description;

    @Schema(description = "活动状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "活动状态不能为空")
    private Integer status;

}
