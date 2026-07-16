package com.meession.etm.module.crm.controller.admin.receivable.vo.plan;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 回款计划批量创建 Request VO")
@Data
public class CrmReceivablePlanBatchCreateReqVO {

    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "合同编号不能为空")
    private Long contractId;

    @Schema(description = "客户编号", hidden = true, example = "2")
    private Long customerId; // 该字段不通过前端传递，而是 contractId 查询出来设置进去

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "期数", requiredMode = Schema.RequiredMode.REQUIRED, example = "3")
    @NotNull(message = "期数不能为空")
    private Integer periodCount;

    @Schema(description = "订单总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "30000")
    @NotNull(message = "订单总金额不能为空")
    private BigDecimal totalPrice;

    @Schema(description = "回款方式", example = "1")
    private Integer returnType;

    @Schema(description = "提前几天提醒", example = "3")
    private Integer remindDays;

    @Schema(description = "是否启用提前收款提醒", example = "true")
    private Boolean remindEnabled;

    @Schema(description = "计划列表")
    private List<PlanItem> plans;

    @Schema(description = "备注", example = "备注")
    private String remark;

    @Data
    public static class PlanItem {
        @Schema(description = "计划回款日期", example = "2024-02-02")
        private LocalDateTime returnTime;

        @Schema(description = "计划回款占比", example = "33.33")
        private BigDecimal percent;

        @Schema(description = "计划回款金额", example = "10000")
        private BigDecimal price;

        @Schema(description = "备注", example = "")
        private String remark;
    }

}
