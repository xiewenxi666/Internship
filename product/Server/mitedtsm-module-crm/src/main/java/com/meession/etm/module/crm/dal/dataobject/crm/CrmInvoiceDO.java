package com.meession.etm.module.crm.dal.dataobject.crm;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 发票 DO
 *
 * @author 赤焰
 */
@TableName("crm_invoice")
@KeySequence("crm_invoice_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmInvoiceDO extends BaseDO {

    /**
     * ID
     */
    @TableId
    private Long id;
    /**
     * 发票编号
     */
    private String no;
    /**
     * 关联订单ID
     */
    private Long orderId;
    /**
     * 关联订单编号
     */
    private String orderNo;
    /**
     * 关联订单名称
     */
    private String orderName;
    /**
     * 开票日期
     */
    private LocalDateTime invoiceDate;
    /**
     * 票据类型
     */
    private Integer type;
    /**
     * 开票金额，单位：元
     */
    private BigDecimal price;
    /**
     * 税务发票号码
     */
    private String invoiceNo;
    /**
     * 票据内容
     */
    private String content;
    /**
     * 订单所属人员
     */
    private Long ownerUserId;
    /**
     * 发票经手人员
     */
    private Long handlerUserId;
    /**
     * 备注
     */
    private String remark;

}
