package com.meession.etm.module.crm.controller.admin.receivable.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 管理后台 - CRM 回款计划新增/修改 Request VO
 */
@Schema(description = "管理后台 - CRM 回款计划新增/修改 Request VO")
@Data
public class CrmReceivablePlanSaveReqVO {

    /** 编号 */
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private Long id;

    /** 客户编号 */
    @Schema(description = "客户编号", hidden = true, example = "2")
    private Long customerId; // 该字段不通过前端传递，而是 contractId 查询出来设置进去

    /** 合同编号 */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "合同编号不能为空")
    private Long contractId;

    /** 负责人编号 */
    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    /** 计划回款日期 */
    @Schema(description = "计划回款日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @NotNull(message = "计划回款日期不能为空")
    private LocalDateTime returnTime;

    /** 回款方式 */
    @Schema(description = "回款方式", example = "1")
    private Integer returnType;

    /** 计划回款金额 */
    @Schema(description = "计划回款金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @NotNull(message = "计划回款金额不能为空")
    private BigDecimal price;

    /** 提前几天提醒 */
    @Schema(description = "提前几天提醒", example = "1")
    private Integer remindDays;

    /** 备注 */
    @Schema(description = "备注", example = "备注")
    private String remark;

}
