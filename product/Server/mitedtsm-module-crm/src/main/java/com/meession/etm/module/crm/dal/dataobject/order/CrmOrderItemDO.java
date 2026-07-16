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
    /** 订单编号 */
    private Long orderId;
    /** 产品编号 */
    private Long productId;
    /** 产品标准单价 */
    private BigDecimal productPrice;
    /** 实际成交单价 */
    private BigDecimal orderPrice;
    /** 数量 */
    private BigDecimal count;
    /** 小计（orderPrice × count） */
    private BigDecimal totalPrice;

}
