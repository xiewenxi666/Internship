package com.meession.etm.module.crm.controller.admin.expense.vo.expense;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 费用新增/修改 Request VO")
@Data
public class CrmExpenseSaveReqVO {

    @Schema(description = "编号", example = "25787")
    private Long id;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "关联合同ID", example = "2048")
    private Long contractId;

    @Schema(description = "负责人", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "负责人不能为空")
    private Long ownerUserId;

    @Schema(description = "费用内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "差旅费")
    @NotNull(message = "费用内容不能为空")
    private String content;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @NotNull(message = "费用金额不能为空")
    private BigDecimal price;

    @Schema(description = "费用类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "费用类型不能为空")
    private Integer type;

    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @NotNull(message = "申请日期不能为空")
    private LocalDateTime applyDate;

    @Schema(description = "备注", example = "备注")
    private String remark;

}
