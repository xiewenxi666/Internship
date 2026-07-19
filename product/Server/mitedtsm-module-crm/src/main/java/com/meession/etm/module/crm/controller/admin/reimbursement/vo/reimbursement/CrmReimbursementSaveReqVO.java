package com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement;

import com.meession.etm.module.crm.framework.operatelog.core.SysAdminUserParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 报销新增/修改 Request VO")
@Data
public class CrmReimbursementSaveReqVO {

    @Schema(description = "编号", example = "25787")
    private Long id;

    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @DiffLogField(name = "负责人", function = SysAdminUserParseFunction.NAME)
    @NotNull(message = "负责人不能为空")
    private Long ownerUserId;

    @Schema(description = "报销金额（由所选费用单金额自动计算）", example = "9000")
    private BigDecimal price;

    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @NotNull(message = "申请日期不能为空")
    @DiffLogField(name = "申请日期")
    private LocalDateTime applyDate;

    @Schema(description = "关联费用单ID列表", example = "[1,2,3]")
    @NotNull(message = "请选择费用单")
    private List<Long> expenseIds;

    @Schema(description = "备注", example = "备注")
    @DiffLogField(name = "备注")
    private String remark;

}
