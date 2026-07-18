package com.meession.etm.module.crm.controller.admin.bulksend.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 群发管理创建/更新 Request VO")
@Data
public class CrmBulkSendSaveReqVO {

    @Schema(description = "编号", example = "1")
    private Long id;

    @Schema(description = "群发标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "春节祝福")
    @NotEmpty(message = "群发标题不能为空")
    private String title;

    @Schema(description = "推广产品ID列表", example = "1,2,3")
    private String productIds;

    @Schema(description = "关联活动ID", example = "1")
    private Long campaignId;

    @Schema(description = "群发类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "群发类型不能为空")
    private Integer type;

    @Schema(description = "模板编号", example = "1")
    private Long templateId;

    @Schema(description = "群发内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "尊敬的客户，祝您新春快乐")
    @NotNull(message = "群发内容不能为空")
    private String content;

    @Schema(description = "目标类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "目标类型不能为空")
    private Integer targetType;

    @Schema(description = "目标ID列表", example = "1,2,3")
    private String targetIds;

    @Schema(description = "目标数量", example = "100")
    private Integer targetCount;

    @Schema(description = "成功数量", example = "95")
    private Integer successCount;

    @Schema(description = "失败数量", example = "5")
    private Integer failCount;

    @Schema(description = "群发状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "群发状态不能为空")
    private Integer status;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2048")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "发送时间", example = "2023-10-01 10:00:00")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime sendTime;

}
