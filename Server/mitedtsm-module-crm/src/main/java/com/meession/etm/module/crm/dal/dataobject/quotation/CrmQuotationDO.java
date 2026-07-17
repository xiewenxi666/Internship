package com.meession.etm.module.crm.dal.dataobject.quotation;

import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CRM 报价单 DO
 *
 * @author engineer
 */
@TableName("crm_quotation")
@KeySequence("crm_quotation_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmQuotationDO extends BaseDO {

    /**
     * 报价单ID
     */
    @TableId
    private Long id;

    /**
     * 报价单编号
     */
    private String quotationNo;

    /**
     * 关联商机ID
     */
    private Long businessId;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 联系人ID
     */
    private Long contactId;

    /**
     * 负责人用户ID
     */
    private Long ownerUserId;

    /**
     * 报价总金额，单位：元
     */
    private BigDecimal totalAmount;

    /**
     * 折扣百分比（0-100）
     */
    private BigDecimal discountPercent;

    /**
     * 折后金额，单位：元
     */
    private BigDecimal finalAmount;

    /**
     * 状态：0-草稿 1-待审批 2-已通过 3-已拒绝 4-已作废
     */
    private Integer status;

    /**
     * 审批人用户ID
     */
    private Long auditUserId;

    /**
     * 审批时间
     */
    private LocalDateTime auditTime;

    /**
     * 审批备注
     */
    private String auditRemark;

    /**
     * 报价有效天数
     */
    private Integer validDays;

    /**
     * 报价过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 备注
     */
    private String remark;

}
