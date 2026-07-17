package com.meession.etm.module.crm.controller.admin.quotation.vo.quotation;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Schema(description = "管理后台 - CRM 报价单创建/修改 Request VO")
@Data
public class CrmQuotationSaveReqVO {

    @Schema(description = "报价单ID", example = "1024")
    private Long id;

    @Schema(description = "关联商机ID", example = "100")
    private Long businessId;

    @Schema(description = "客户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "200")
    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @Schema(description = "联系人ID", example = "300")
    private Long contactId;

    @Schema(description = "负责人用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1000")
    @NotNull(message = "负责人不能为空")
    private Long ownerUserId;

    @Schema(description = "折扣百分比（0-100）", example = "95.50")
    private BigDecimal discountPercent;

    @Schema(description = "报价有效天数", example = "30")
    private Integer validDays;

    @Schema(description = "备注", example = "这是备注")
    private String remark;

    @Schema(description = "报价产品明细列表")
    private List<ProductItem> products;

    @Schema(description = "报价产品明细")
    @Data
    public static class ProductItem {

        @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "500")
        @NotNull(message = "产品ID不能为空")
        private Long productId;

        @Schema(description = "报价单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "99.99")
        @NotNull(message = "报价单价不能为空")
        private BigDecimal quotationPrice;

        @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
        @NotNull(message = "数量不能为空")
        private BigDecimal count;

        @Schema(description = "备注", example = "产品备注")
        private String remark;
    }

}
