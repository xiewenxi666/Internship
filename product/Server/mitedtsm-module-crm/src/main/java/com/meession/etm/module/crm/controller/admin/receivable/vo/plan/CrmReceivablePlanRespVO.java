package com.meession.etm.module.crm.controller.admin.receivable.vo.plan;

import com.meession.etm.module.crm.controller.admin.receivable.vo.receivable.CrmReceivableRespVO;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - CRM 回款计划 Response VO
 */
@Schema(description = "管理后台 - CRM 回款计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmReceivablePlanRespVO {

    /** 编号 */
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("编号")
    private Long id;

    /** 期数 */
    @Schema(description = "期数", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("期数")
    private Integer period;

    /** 客户编号 */
    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("客户编号")
    private Long customerId;
    /** 客户名字 */
    @Schema(description = "客户名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "test")
    @ExcelProperty("客户名字")
    private String customerName;

    /** 合同编号 */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("合同编号")
    private Long contractId;
    /** 合同编号（合同号） */
    @Schema(description = "合同编号", example = "Q110")
    @ExcelProperty("合同编号")
    private String contractNo;

    /** 负责人编号 */
    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("负责人编号")
    private Long ownerUserId;
    /** 负责人 */
    @Schema(description = "负责人", example = "test")
    @ExcelProperty("负责人")
    private String ownerUserName;

    /** 计划回款日期 */
    @Schema(description = "计划回款日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @ExcelProperty("计划回款日期")
    private LocalDateTime returnTime;

    /** 计划回款方式 */
    @Schema(description = "计划回款方式", example = "1")
    @ExcelProperty("计划回款方式")
    private Integer returnType;

    /** 计划回款金额 */
    @Schema(description = "计划回款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @ExcelProperty("计划回款金额")
    private BigDecimal price;

    /** 回款编号 */
    @Schema(description = "回款编号", example = "19852")
    @ExcelProperty("回款编号")
    private Long receivableId;
    /** 回款信息 */
    @Schema(description = "回款信息")
    private CrmReceivableRespVO receivable;

    /** 提前几天提醒 */
    @Schema(description = "提前几天提醒", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("提前几天提醒")
    private Integer remindDays;

    /** 提醒日期 */
    @Schema(description = "提醒日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @ExcelProperty("提醒日期")
    private LocalDateTime remindTime;

    /** 备注 */
    @Schema(description = "备注", example = "备注")
    @ExcelProperty("备注")
    private String remark;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /** 更新时间 */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    /** 创建人 */
    @Schema(description = "创建人", example = "1024")
    @ExcelProperty("创建人")
    private String creator;
    /** 创建人名字 */
    @Schema(description = "创建人名字", example = "密讯")
    @ExcelProperty("创建人名字")
    private String creatorName;

}
