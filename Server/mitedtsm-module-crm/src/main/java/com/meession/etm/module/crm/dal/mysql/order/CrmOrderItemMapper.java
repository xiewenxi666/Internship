package com.meession.etm.module.crm.dal.mysql.order;

import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单产品 Mapper
 *
 * @author 23计三倪雨晗
 */
@Mapper
public interface CrmOrderItemMapper extends BaseMapperX<CrmOrderItemDO> {

    default List<CrmOrderItemDO> selectListByOrderId(Long orderId) {
        return selectList(CrmOrderItemDO::getOrderId, orderId);
    }

}
