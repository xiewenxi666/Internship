package com.meession.etm.module.crm.controller.admin.bulksend.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 群发管理 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmBulkSendRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "群发标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "春节祝福")
    @ExcelProperty("群发标题")
    private String title;

    @Schema(description = "关联活动ID", example = "1")
    @ExcelProperty("关联活动ID")
    private Long campaignId;

    @Schema(description = "关联活动名称", example = "春节营销活动")
    private String campaignName;

    @Schema(description = "群发类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("群发类型")
    private Integer type;

    @Schema(description = "模板编号", example = "1")
    @ExcelProperty("模板编号")
    private Long templateId;

    @Schema(description = "群发内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "尊敬的客户，祝您新春快乐")
    @ExcelProperty("群发内容")
    private String content;

    @Schema(description = "目标类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("目标类型")
    private Integer targetType;

    @Schema(description = "目标ID列表", example = "1,2,3")
    @ExcelProperty("目标ID列表")
    private String targetIds;

    @Schema(description = "目标数量", example = "100")
    @ExcelProperty("目标数量")
    private Integer targetCount;

    @Schema(description = "成功数量", example = "95")
    @ExcelProperty("成功数量")
    private Integer successCount;

    @Schema(description = "失败数量", example = "5")
    @ExcelProperty("失败数量")
    private Integer failCount;

    @Schema(description = "群发状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("群发状态")
    private Integer status;

    @Schema(description = "负责人编号")
    private Long ownerUserId;
    @Schema(description = "负责人名字", example = "张三")
    @ExcelProperty("负责人名字")
    private String ownerUserName;

    @Schema(description = "发送时间", example = "2023-10-01 10:00:00")
    @ExcelProperty("发送时间")
    private LocalDateTime sendTime;

    @Schema(description = "创建人", example = "1024")
    @ExcelProperty("创建人")
    private String creator;
    @Schema(description = "创建人名字", example = "管理员")
    @ExcelProperty("创建人名字")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

}
