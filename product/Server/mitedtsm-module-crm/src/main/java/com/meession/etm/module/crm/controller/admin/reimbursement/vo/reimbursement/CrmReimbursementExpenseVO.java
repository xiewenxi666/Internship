package com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "报销关联费用单 VO")
@Data
public class CrmReimbursementExpenseVO {

    @Schema(description = "费用ID", example = "1")
    private Long id;

    @Schema(description = "费用编号", example = "FY20260717000001")
    private String no;

    @Schema(description = "费用内容", example = "差旅费")
    private String content;

    @Schema(description = "费用金额", example = "500.00")
    private BigDecimal price;

    @Schema(description = "费用类型", example = "1")
    private Integer type;

    @Schema(description = "发生时间")
    private LocalDateTime applyDate;

    @Schema(description = "报销状态", example = "0")
    private Integer reimburseStatus;

}
