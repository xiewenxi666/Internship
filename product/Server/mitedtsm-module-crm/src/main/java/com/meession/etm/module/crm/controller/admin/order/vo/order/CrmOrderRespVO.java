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

    /** 订单编号 */
    @Schema(description = "订单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @ExcelProperty("订单编号")
    private Long id;

    /** 订单号 */
    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "DD20260101000001")
    @ExcelProperty("订单号")
    private String no;

    /** 订单名称 */
    @Schema(description = "订单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "XX项目订单")
    @ExcelProperty("订单名称")
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18336")
    @ExcelProperty("客户编号")
    private Long customerId;
    /** 客户名称 */
    @Schema(description = "客户名称", example = "张三")
    @ExcelProperty("客户名称")
    private String customerName;

    /** 商机编号 */
    @Schema(description = "商机编号", example = "10864")
    @ExcelProperty("商机编号")
    private Long businessId;
    /** 商机名称 */
    @Schema(description = "商机名称", example = "XX项目")
    @ExcelProperty("商机名称")
    private String businessName;

    /** 负责人编号 */
    @Schema(description = "负责人编号", example = "25682")
    @ExcelProperty("负责人编号")
    private Long ownerUserId;
    /** 负责人名字 */
    @Schema(description = "负责人名字", example = "李四")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    /** 负责人部门 */
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    /** 工作流编号 */
    @Schema(description = "工作流编号", example = "1043")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    /** 订单状态 */
    @Schema(description = "订单状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("订单状态")
    private Integer status;

    /** 下单日期 */
    @Schema(description = "下单日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("下单日期")
    private LocalDateTime orderDate;

    /** 产品总金额 */
    @Schema(description = "产品总金额", example = "19510")
    @ExcelProperty("产品总金额")
    private BigDecimal totalProductPrice;

    /** 整单折扣 */
    @Schema(description = "整单折扣")
    @ExcelProperty("整单折扣")
    private BigDecimal discountPercent;

    /** 订单金额 */
    @Schema(description = "订单金额", example = "5617")
    @ExcelProperty("订单金额")
    private BigDecimal totalPrice;

    /** 备注 */
    @Schema(description = "备注", example = "加急处理")
    @ExcelProperty("备注")
    private String remark;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    /** 创建人 */
    @Schema(description = "创建人", example = "25682")
    @ExcelProperty("创建人")
    private String creator;

    /** 创建人名字 */
    @Schema(description = "创建人名字", example = "test")
    @ExcelProperty("创建人名字")
    private String creatorName;

    /** 更新时间 */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;

    /** 产品列表 */
    @Schema(description = "产品列表")
    private List<Product> products;

    /** 产品列表 */
    @Schema(description = "产品列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        /** 编号 */
        @Schema(description = "编号", example = "888")
        private Long id;

        /** 产品编号 */
        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        private Long productId;
        /** 产品名称 */
        @Schema(description = "产品名称", example = "笔记本电脑")
        private String productName;
        /** 产品条码 */
        @Schema(description = "产品条码", example = "P001")
        private String productNo;
        /** 产品单位 */
        @Schema(description = "产品单位", example = "台")
        private Integer productUnit;

        /** 产品单价 */
        @Schema(description = "产品单价", example = "5000.00")
        private BigDecimal productPrice;

        /** 订单价格 */
        @Schema(description = "订单价格", example = "4800.00")
        private BigDecimal orderPrice;

        /** 产品数量 */
        @Schema(description = "产品数量", example = "10")
        private Integer count;

        /** 总计价格 */
        @Schema(description = "总计价格", example = "48000.00")
        private BigDecimal totalPrice;

    }

}
