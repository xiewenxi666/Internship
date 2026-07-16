package com.meession.etm.module.crm.dal.dataobject.order;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * CRM 订单 DO
 *
 * @author 23计三倪雨晗
 */
@TableName("crm_order")
@KeySequence("crm_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmOrderDO extends BaseDO {

    @TableId
    private Long id;
    /** 订单编号（自动生成，格式 DD+yyyyMMdd+seq） */
    private String no;
    /** 订单名称 */
    private String name;
    /** 客户编号 */
    private Long customerId;
    /** 商机编号 */
    private Long businessId;
    /** 下单时间 */
    private LocalDateTime orderDate;
    /** 负责人编号 */
    private Long ownerUserId;
    /** BPM 流程实例编号 */
    private String processInstanceId;
    /** 订单状态：{@link CrmOrderStatusEnum} */
    private Integer status;
    /** 产品总金额 */
    private BigDecimal totalProductPrice;
    /** 整单折扣（百分比） */
    private BigDecimal discountPercent;
    /** 订单总金额（产品总金额 × (1 - 折扣%)） */
    private BigDecimal totalPrice;
    /** 备注 */
    private String remark;
    /** 最后跟进时间 */
    private LocalDateTime contactLastTime;
    /** 下次跟进时间 */
    private LocalDateTime contactNextTime;

}
