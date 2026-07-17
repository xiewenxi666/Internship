package com.meession.etm.module.crm.controller.admin.quotation.vo.quotation;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "管理后台 - CRM 报价单 Response VO")
@Data
public class CrmQuotationRespVO {

    @Schema(description = "报价单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "报价单编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "QT-20260101-001")
    private String quotationNo;

    @Schema(description = "关联商机ID", example = "100")
    private Long businessId;

    @Schema(description = "商机名称", example = "XX项目商机")
    private String businessName;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "200")
    private Long customerId;

    @Schema(description = "客户名称", example = "XX科技有限公司")
    private String customerName;

    @Schema(description = "联系人ID", example = "300")
    private Long contactId;

    @Schema(description = "联系人名称", example = "张三")
    private String contactName;

    @Schema(description = "负责人用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    private Long ownerUserId;

    @Schema(description = "负责人名称", example = "李四")
    private String ownerUserName;

    @Schema(description = "报价总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "10000.00")
    private BigDecimal totalAmount;

    @Schema(description = "折扣百分比", example = "95.50")
    private BigDecimal discountPercent;

    @Schema(description = "折后金额", example = "9550.00")
    private BigDecimal finalAmount;

    @Schema(description = "状态：0-草稿 1-待审批 2-已通过 3-已拒绝 4-已作废", requiredMode = Schema.RequiredMode.REQUIRED, example = "0")
    private Integer status;

    @Schema(description = "状态名称", example = "草稿")
    private String statusName;

    @Schema(description = "审批人用户ID", example = "1001")
    private Long auditUserId;

    @Schema(description = "审批人名称", example = "王五")
    private String auditUserName;

    @Schema(description = "审批时间", example = "2026-01-01 10:00:00")
    private LocalDateTime auditTime;

    @Schema(description = "审批备注", example = "同意")
    private String auditRemark;

    @Schema(description = "报价有效天数", example = "30")
    private Integer validDays;

    @Schema(description = "报价过期时间", example = "2026-01-31 10:00:00")
    private LocalDateTime expireTime;

    @Schema(description = "备注", example = "这是备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

    @Schema(description = "报价产品明细列表")
    private List<ProductItemVO> products;

    @Schema(description = "报价产品明细")
    @Data
    public static class ProductItemVO {

        @Schema(description = "报价明细ID", example = "1001")
        private Long id;

        @Schema(description = "产品ID", example = "500")
        private Long productId;

        @Schema(description = "产品名称", example = "XX产品")
        private String productName;

        @Schema(description = "产品编码", example = "P001")
        private String productCode;

        @Schema(description = "产品单位", example = "个")
        private String productUnit;

        @Schema(description = "产品标准价", example = "100.00")
        private BigDecimal productPrice;

        @Schema(description = "报价单价", example = "95.00")
        private BigDecimal quotationPrice;

        @Schema(description = "数量", example = "10")
        private BigDecimal count;

        @Schema(description = "小计金额", example = "950.00")
        private BigDecimal totalPrice;

        @Schema(description = "备注", example = "产品备注")
        private String remark;
    }

}
