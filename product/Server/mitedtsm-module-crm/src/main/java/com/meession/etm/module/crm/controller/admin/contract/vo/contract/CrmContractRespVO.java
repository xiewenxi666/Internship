/**
 * 合同 Response VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.contract.vo.contract;

import cn.idev.excel.annotation.ExcelIgnoreUnannotated;
import cn.idev.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 合同 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CrmContractRespVO {

    /** 合同编号 */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10430")
    @ExcelProperty("合同编号")
    private Long id;

    /** 合同名称 */
    @Schema(description = "合同名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("合同名称")
    private String name;

    /** 合同编号 */
    @Schema(description = "合同编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20230101")
    @ExcelProperty("合同编号")
    private String no;

    /** 客户编号 */
    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18336")
    @ExcelProperty("客户编号")
    private Long customerId;
    /** 客户名称 */
    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "18336")
    @ExcelProperty("客户名称")
    private String customerName;

    /** 商机编号 */
    @Schema(description = "商机编号", example = "10864")
    @ExcelProperty("商机编号")
    private Long businessId;
    /** 商机名称 */
    @Schema(description = "商机名称", example = "10864")
    @ExcelProperty("商机名称")
    private String businessName;

    /** 最后跟进时间 */
    @Schema(description = "最后跟进时间")
    @ExcelProperty("最后跟进时间")
    private LocalDateTime contactLastTime;

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

    /** 工作流编号 */
    @Schema(description = "工作流编号", example = "1043")
    @ExcelProperty("工作流编号")
    private String processInstanceId;

    /** 审批状态 */
    @Schema(description = "审批状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    @ExcelProperty("审批状态")
    private Integer auditStatus;

    /** 下单日期 */
    @Schema(description = "下单日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("下单日期")
    private LocalDateTime orderDate;

    /** 开始时间 */
    @Schema(description = "开始时间")
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    /** 结束时间 */
    @Schema(description = "结束时间")
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    /** 产品总金额 */
    @Schema(description = "产品总金额", example = "19510")
    @ExcelProperty("产品总金额")
    private BigDecimal totalProductPrice;

    /** 整单折扣 */
    @Schema(description = "整单折扣")
    @ExcelProperty("整单折扣")
    private BigDecimal discountPercent;

    /** 合同金额 */
    @Schema(description = "合同金额", example = "5617")
    @ExcelProperty("合同金额")
    private BigDecimal totalPrice;

    /** 已回款金额 */
    @Schema(description = "已回款金额", example = "5617")
    @ExcelProperty("已回款金额")
    private BigDecimal totalReceivablePrice;

    /** 客户签约人编号 */
    @Schema(description = "客户签约人编号", example = "18546")
    private Long signContactId;
    /** 客户签约人 */
    @Schema(description = "客户签约人", example = "小豆")
    @ExcelProperty("客户签约人")
    private String signContactName;

    /** 公司签约人 */
    @Schema(description = "公司签约人", example = "14036")
    private Long signUserId;
    /** 公司签约人 */
    @Schema(description = "公司签约人", example = "小明")
    @ExcelProperty("公司签约人")
    private String signUserName;

    /** 备注 */
    @Schema(description = "备注", example = "你猜")
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

        /** 合同价格 */
        @Schema(description = "合同价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal contractPrice;

        /** 产品数量 */
        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "8911")
        private BigDecimal count;

        /** 总计价格 */
        @Schema(description = "总计价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        private BigDecimal totalPrice;

    }

}
