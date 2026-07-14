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
    private String no;
    private String name;
    private Long customerId;
    private Long businessId;
    private LocalDateTime orderDate;
    private Long ownerUserId;
    private String processInstanceId;
    private Integer status;
    private BigDecimal totalProductPrice;
    private BigDecimal discountPercent;
    private BigDecimal totalPrice;
    private String remark;
    private LocalDateTime contactLastTime;
    private LocalDateTime contactNextTime;

}
