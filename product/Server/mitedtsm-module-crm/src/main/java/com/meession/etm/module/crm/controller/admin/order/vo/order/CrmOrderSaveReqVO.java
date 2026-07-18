/**
 * 订单创建/更新 Request VO
 *
 * @author 23计三倪雨晗
 * @since 2026-03
 */
package com.meession.etm.module.crm.controller.admin.order.vo.order;

import com.meession.etm.module.crm.framework.operatelog.core.CrmBusinessParseFunction;
import com.meession.etm.module.crm.framework.operatelog.core.CrmCustomerParseFunction;
import com.meession.etm.module.crm.framework.operatelog.core.SysAdminUserParseFunction;
import com.mzt.logapi.starter.annotation.DiffLogField;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - CRM 订单创建/更新 Request VO")
@Data
public class CrmOrderSaveReqVO {

    /** 订单编号 */
    @Schema(description = "订单编号", example = "10430")
    private Long id;

    /** 订单名称 */
    @Schema(description = "订单名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "XX项目订单")
    @DiffLogField(name = "订单名称")
    @NotNull(message = "订单名称不能为空")
    private String name;

    /** 客户编号 */
    @Schema(description = "客户编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "18336")
    @DiffLogField(name = "客户", function = CrmCustomerParseFunction.NAME)
    @NotNull(message = "客户编号不能为空")
    private Long customerId;

    /** 商机编号 */
    @Schema(description = "商机编号", example = "10864")
    @DiffLogField(name = "商机", function = CrmBusinessParseFunction.NAME)
    private Long businessId;

    /** 负责人编号 */
    @Schema(description = "负责人编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17144")
    @DiffLogField(name = "负责人", function = SysAdminUserParseFunction.NAME)
    @NotNull(message = "负责人不能为空")
    private Long ownerUserId;

    /** 下单日期 */
    @Schema(description = "下单日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @DiffLogField(name = "下单日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @NotNull(message = "下单日期不能为空")
    private LocalDateTime orderDate;

    /** 整单折扣 */
    @Schema(description = "整单折扣", requiredMode = Schema.RequiredMode.REQUIRED, example = "10.00")
    @DiffLogField(name = "整单折扣")
    @NotNull(message = "整单折扣不能为空")
    private BigDecimal discountPercent;

    /** 备注 */
    @Schema(description = "备注", example = "加急处理")
    @DiffLogField(name = "备注")
    private String remark;

    /** 产品列表 */
    @Schema(description = "产品列表")
    private List<Product> products;

    /** 产品列表 */
    @Schema(description = "产品列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Product {

        /** 产品编号 */
        @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20529")
        @NotNull(message = "产品编号不能为空")
        private Long productId;

        /** 产品单价 */
        @Schema(description = "产品单价", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "产品单价不能为空")
        private BigDecimal productPrice;

        /** 订单价格 */
        @Schema(description = "订单价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "123.00")
        @NotNull(message = "订单价格不能为空")
        private BigDecimal orderPrice;

        /** 产品数量 */
        @Schema(description = "产品数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "10")
        @NotNull(message = "产品数量不能为空")
        private Integer count;

    }

}
