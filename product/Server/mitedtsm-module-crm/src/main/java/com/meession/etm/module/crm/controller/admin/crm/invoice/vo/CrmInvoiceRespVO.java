package com.meession.etm.module.crm.controller.admin.crm.invoice.vo;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import com.meession.etm.framework.excel.core.annotations.DictFormat;
import com.meession.etm.framework.excel.core.convert.DictConvert;
import com.meession.etm.module.crm.enums.DictTypeConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - CRM 发票 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmInvoiceRespVO {

    @Schema(description = "编号", example = "25787")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "发票编号", example = "FP202401010001")
    @ExcelProperty("发票编号")
    private String no;

    @Schema(description = "关联订单ID", example = "1024")
    private Long orderId;

    @Schema(description = "关联订单编号", example = "DD202401010001")
    @ExcelProperty("关联订单编号")
    private String orderNo;

    @Schema(description = "关联订单名称", example = "XX项目订单")
    @ExcelProperty("关联订单名称")
    private String orderName;

    @Schema(description = "开票日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "2024-02-02")
    @ExcelProperty("开票日期")
    private LocalDateTime invoiceDate;

    @Schema(description = "票据类型", example = "1")
    @ExcelProperty(value = "票据类型", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.CRM_INVOICE_TYPE)
    private Integer type;

    @Schema(description = "开票金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "9000")
    @ExcelProperty("开票金额")
    private BigDecimal price;

    @Schema(description = "税务发票号码", example = "1234567890")
    @ExcelProperty("税务发票号码")
    private String invoiceNo;

    @Schema(description = "票据内容", example = "软件开发服务费")
    @ExcelProperty("票据内容")
    private String content;

    @Schema(description = "订单所属人员", example = "25682")
    private Long ownerUserId;
    @Schema(description = "订单所属人员名字", example = "张三")
    @ExcelProperty("订单所属人员")
    private String ownerUserName;
    @Schema(description = "订单所属人员部门")
    @ExcelProperty("订单所属人员部门")
    private String ownerUserDeptName;

    @Schema(description = "发票经手人员", example = "25683")
    private Long handlerUserId;
    @Schema(description = "发票经手人员名字", example = "李四")
    @ExcelProperty("发票经手人员")
    private String handlerUserName;
    @Schema(description = "发票经手人员部门")
    @ExcelProperty("发票经手人员部门")
    private String handlerUserDeptName;

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
