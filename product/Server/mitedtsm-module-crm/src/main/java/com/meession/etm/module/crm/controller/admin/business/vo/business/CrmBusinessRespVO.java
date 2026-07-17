/**
 * 商机 Response VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.business.vo.business;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 商机 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmBusinessRespVO {

    /** 编号 */
    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "32129")
    @ExcelProperty("编号")
    private Long id;

    /** 商机名称 */
    @Schema(description = "商机名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("商机名称")
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10299")
    private Long customerId;
    /** 客户名称 */
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("客户名称")
    private String customerName;

    /** 跟进状态 */
    @Schema(description = "跟进状态", requiredMode = Schema.RequiredMode.REQUIRED, example ="true")
    @ExcelProperty("跟进状态")
    private Boolean followUpStatus;

    /** 最后跟进时间 */
    @Schema(description = "最后跟进时间")
    @ExcelProperty("最后跟进时间")
    private LocalDateTime contactLastTime;

    /** 下次联系时间 */
    @Schema(description = "下次联系时间")
    @ExcelProperty("下次联系时间")
    private LocalDateTime contactNextTime;

    /** 负责人的用户编号 */
    @Schema(description = "负责人的用户编号", example = "25682")
    @ExcelProperty("负责人的用户编号")
    private Long ownerUserId;
    /** 负责人名字 */
    @Schema(description = "负责人名字", example = "25682")
    @ExcelProperty("负责人名字")
    private String ownerUserName;
    /** 负责人部门 */
    @Schema(description = "负责人部门")
    @ExcelProperty("负责人部门")
    private String ownerUserDeptName;

    /** 商机状态组编号 */
    @Schema(description = "商机状态组编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25714")
    private Long statusTypeId;
    /** 商机状组名字 */
    @Schema(description = "商机状组名字", requiredMode = Schema.RequiredMode.REQUIRED, example = "进行中")
    @ExcelProperty("商机状态组")
    private String statusTypeName;

    /** 商机状态编号 */
    @Schema(description = "商机状态编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "30320")
    private Long statusId;
    /** 状态名称 */
    @Schema(description = "状态名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "跟进中")
    @ExcelProperty("商机状态")
    private String statusName;

    /** 结束状态 */
    @Schema
    @ExcelProperty("结束状态")
    private Integer endStatus;

    /** 结束时的备注 */
    @ExcelProperty("结束时的备注")
    private String endRemark;

    /** 预计成交日期 */
    @Schema(description = "预计成交日期")
    @ExcelProperty("预计成交日期")
    private LocalDateTime dealTime;

    /** 产品总金额 */
    @Schema(description = "产品总金额", example = "12025")
    @ExcelProperty("产品总金额")
    private BigDecimal totalProductPrice;

    /** 整单折扣 */
    @Schema(description = "整单折扣")
    @ExcelProperty("整单折扣")
    private BigDecimal discountPercent;

    /** 商机总金额 */
    @Schema(description = "商机总金额", example = "12371")
    @ExcelProperty("商机总金额")
    private BigDecimal totalPrice;

    /** 备注 */
    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    /** 创建人 */
    @Schema(description = "创建人", example = "1024")
    @ExcelProperty("创建人")
    private String creator;
    /** 创建人名字 */
    @Schema(description = "创建人名字", example = "密讯")
    @ExcelProperty("创建人名字")
    private String creatorName;

    /** 创建时间 */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

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
        @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "888")
        private Long id;

        /** 产品编号 */
        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        private Long productId;
        /** 产品名称 */
        @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
        private String productName;
        /** 产品条码 */
        @Schema(description = "产品条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        private String productNo;
        /** 产品单位 */
        @Schema(description = "产品单位", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
        private Integer productUnit;

        /** 产品单价 */
        @Schema(description = "产品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal productPrice;

        /** 商机价格 */
        @Schema(description = "商机价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal businessPrice;

        /** 产品数量 */
        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "8911")
        private BigDecimal count;

        /** 总计价格 */
        @Schema(description = "总计价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal totalPrice;

    }

}
