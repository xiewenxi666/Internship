package com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement;

import com.meession.etm.module.crm.framework.operatelog.core.CrmCustomerParseFunction;
import com.meession.etm.module.crm.framework.operatelog.core.SysAdminUserParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 报销新增/修改 Request VO")
@Data
public class CrmReimbursementSaveReqVO {

    @Schema(description = "编号", example = "25787")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @DiffLogField(name = "负责人", function = SysAdminUserParseFunction.NAME)
    @NotNull(message = "负责人编号不能为空")
    private Long ownerUserId;

    @Schema(description = "客户编号", example = "2")
    @DiffLogField(name = "客户", function = CrmCustomerParseFunction.NAME)
    private Long customerId;

    @Schema(description = "合同编号", example = "2")
    private Long contractId;

    @Schema(description = "报销内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "差旅报销")
    @DiffLogField(name = "报销内容")
    @NotNull(message = "报销内容不能为空")
    private String content;

    @Schema(description = "报销金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @DiffLogField(name = "报销金额")
    @NotNull(message = "报销金额不能为空")
    private BigDecimal price;

    @Schema(description = "报销类型", example = "1")
    @DiffLogField(name = "报销类型")
    private Integer type;

    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @NotNull(message = "申请日期不能为空")
    @DiffLogField(name = "申请日期")
    private LocalDateTime applyDate;

    @Schema(description = "备注", example = "备注")
    @DiffLogField(name = "备注")
    private String remark;

}
