/**
 * 订单 Response VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.order.vo.order;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmOrderRespVO {

    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @ExcelProperty("订单编号")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "DD20260101000001")
    @ExcelProperty("订单号")
    private String no;

    @Schema(description = "订单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "XX项目订单")
    @ExcelProperty("订单名称")
    private String name;

    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18336")
    @ExcelProperty("客户编号")
    private Long customerId;
    @Schema(description = "客户名称", example = "张三")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "商机编号", example = "10864")
    @ExcelProperty("商机编号")
    private Long businessId;
    @Schema(description = "商机名称", example = "XX项目")
    @ExcelProperty("商机名称")
    private String businessName;

    @Schema(description = "负责人编号", example = "25682")
    @ExcelProperty("负责人编号")
    private Long ownerUserId;
    @Schema(description = "负责人名字", example = "李四")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    @Schema(description = "工作流编号", example = "1043")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("订单状态")
    private Integer status;

    @Schema(description = "下单日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("下单日期")
    private LocalDateTime orderDate;

    @Schema(description = "产品总金额", example = "19510")
    @ExcelProperty("产品总金额")
    private BigDecimal totalProductPrice;

    @Schema(description = "整单折扣")
    @ExcelProperty("整单折扣")
    private BigDecimal discountPercent;

    @Schema(description = "订单金额", example = "5617")
    @ExcelProperty("订单金额")
    private BigDecimal totalPrice;

    @Schema(description = "备注", example = "加急处理")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人", example = "25682")
    @ExcelProperty("创建人")
    private String creator;

    @Schema(description = "创建人名字", example = "test")
    @ExcelProperty("创建人名字")
    private String creatorName;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "产品列表")
    private List<Product> products;

    @Schema(description = "产品列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        @Schema(description = "编号", example = "888")
        private Long id;

        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        private Long productId;
        @Schema(description = "产品名称", example = "笔记本电脑")
        private String productName;
        @Schema(description = "产品条码", example = "P001")
        private String productNo;
        @Schema(description = "产品单位", example = "台")
        private Integer productUnit;

        @Schema(description = "产品单价", example = "5000.00")
        private BigDecimal productPrice;

        @Schema(description = "订单价格", example = "4800.00")
        private BigDecimal orderPrice;

        @Schema(description = "产品数量", example = "10")
        private Integer count;

        @Schema(description = "总计价格", example = "48000.00")
        private BigDecimal totalPrice;

    }

}
