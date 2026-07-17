package com.meession.etm.module.crm.controller.admin.expense.vo.expense;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.crm.enums.DictTypeConstants;
import com.meession.etm.module.crm.controller.admin.contract.vo.contract.CrmContractRespVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 费用 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmExpenseRespVO {

    @Schema(description = "编号", example = "25787")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "费用编号", example = "BG202401010001")
    @ExcelProperty("费用编号")
    private String no;

    @Schema(description = "客户ID", example = "1024")
    private Long customerId;
    @Schema(description = "客户名称", example = "XX公司")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "关联合同ID", example = "2048")
    private Long contractId;
    @Schema(description = "合同信息")
    private CrmContractRespVO contract;

    @Schema(description = "负责人", example = "25682")
    private Long ownerUserId;
    @Schema(description = "负责人名字", example = "张三")
    @ExcelProperty("负责人")
    private String ownerUserName;
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    @Schema(description = "费用内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "差旅费")
    @ExcelProperty("费用内容")
    private String content;

    @Schema(description = "费用金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @ExcelProperty("费用金额")
    private BigDecimal price;

    @Schema(description = "费用类型", example = "1")
    @ExcelProperty(value = "费用类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CRM_EXPENSE_TYPE)
    private Integer type;

    @Schema(description = "申请日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @ExcelProperty("申请日期")
    private LocalDateTime applyDate;

    @Schema(description = "备注", example = "备注")
    @ExcelProperty("备注")
    private String remark;

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
