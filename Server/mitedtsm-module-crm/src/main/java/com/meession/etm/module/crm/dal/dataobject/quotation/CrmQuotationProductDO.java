package com.meession.etm.module.crm.dal.dataobject.quotation;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * CRM 报价单明细 DO
 *
 * @author engineer
 */
@TableName("crm_quotation_product")
@KeySequence("crm_quotation_product_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmQuotationProductDO extends BaseDO {

    /**
     * 报价明细ID
     */
    @TableId
    private Long id;

    /**
     * 报价单ID
     */
    private Long quotationId;

    /**
     * 产品ID
     */
    private Long productId;

    /**
     * 产品名称（冗余）
     */
    private String productName;

    /**
     * 产品编码（冗余）
     */
    private String productCode;

    /**
     * 产品单位（冗余）
     */
    private String productUnit;

    /**
     * 产品标准价，单位：元
     */
    private BigDecimal productPrice;

    /**
     * 报价单价，单位：元
     */
    private BigDecimal quotationPrice;

    /**
     * 数量
     */
    private BigDecimal count;

    /**
     * 小计金额 = quotation_price × count
     */
    private BigDecimal totalPrice;

    /**
     * 备注
     */
    private String remark;

}
