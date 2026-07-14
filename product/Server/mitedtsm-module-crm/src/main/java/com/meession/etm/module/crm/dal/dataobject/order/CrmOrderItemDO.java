package com.meession.etm.module.crm.dal.dataobject.order;

import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.meession.etm.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * CRM 订单产品关联表 DO
 *
 * CrmOrderDO : CrmOrderItemDO = 1 : N
 *
 * @author 23计三倪雨晗
 */
@TableName("crm_order_item")
@KeySequence("crm_order_item_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrmOrderItemDO extends BaseDO {

    @TableId
    private Long id;
    private Long orderId;
    private Long productId;
    private BigDecimal productPrice;
    private BigDecimal orderPrice;
    private BigDecimal count;
    private BigDecimal totalPrice;

}
