package com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement;

import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.crm.enums.DictTypeConstants;
import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 报销 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmReimbursementRespVO {

    @Schema(description = "编号", example = "25787")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "报销编号", example = "BX20240001")
    @ExcelProperty("报销编号")
    private String no;

    @Schema(description = "报销金额（费用单合计）", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @ExcelProperty("报销金额")
    private BigDecimal price;

    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @ExcelProperty("申请日期")
    private LocalDateTime applyDate;

    @Schema(description = "负责人的用户编号", example = "25682")
    private Long ownerUserId;
    @Schema(description = "负责人名字", example = "25682")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    @Schema(description = "工作流编号", example = "1043")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty(value = "审批状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CRM_AUDIT_STATUS)
    private Integer auditStatus;

    @Schema(description = "备注", example = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "关联费用单列表")
    private List<CrmReimbursementExpenseVO> expenses;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建人", example = "25682")
    private String creator;
    @Schema(description = "创建人名字", example = "test")
    @ExcelProperty("创建人名字")
    private String creatorName;

}
