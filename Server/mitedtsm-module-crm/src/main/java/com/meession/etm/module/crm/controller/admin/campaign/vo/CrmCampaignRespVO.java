package com.meession.etm.module.crm.controller.admin.campaign.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 营销活动 Response VO")
@Data
@ToString(callSuper = true)
@ExcelIgnoreUnannotated
public class CrmCampaignRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "活动标题", requiredMode = Schema.RequiredMode.REQUIRED, example = "春季促销")
    @ExcelProperty("活动标题")
    private String title;

    @Schema(description = "活动类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("活动类型")
    private Integer type;

    @Schema(description = "开始时间", example = "2023-10-01 00:00:00")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", example = "2023-10-31 23:59:59")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "预计成本", example = "10000.00")
    @ExcelProperty("预计成本")
    private BigDecimal estimatedCost;

    @Schema(description = "预计收入", example = "50000.00")
    @ExcelProperty("预计收入")
    private BigDecimal estimatedRevenue;

    @Schema(description = "负责人编号")
    private Long ownerUserId;
    @Schema(description = "负责人名字", example = "张三")
    @ExcelProperty("负责人名字")
    private String ownerUserName;

    @Schema(description = "参与人员", example = "张三,李四")
    @ExcelProperty("参与人员")
    private String participants;

    @Schema(description = "活动地址", example = "北京市朝阳区")
    @ExcelProperty("活动地址")
    private String address;

    @Schema(description = "活动描述", example = "春季大型促销活动")
    @ExcelProperty("活动描述")
    private String description;

    @Schema(description = "活动状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("活动状态")
    private Integer status;

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
