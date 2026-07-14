package com.meession.etm.module.crm.service.order;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderSaveReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderTransferReqVO;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderUpdateStatusReqVO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderItemDO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.meession.etm.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * CRM 订单 Service 接口
 *
 * @author 23计三倪雨晗
 */
public interface CrmOrderService {

    Long createOrder(@Valid CrmOrderSaveReqVO createReqVO, Long userId);

    void updateOrder(@Valid CrmOrderSaveReqVO updateReqVO);

    void deleteOrder(Long id);

    CrmOrderDO getOrder(Long id);

    CrmOrderDO validateOrder(Long id);

    List<CrmOrderDO> getOrderList(Collection<Long> ids);

    default Map<Long, CrmOrderDO> getOrderMap(Collection<Long> ids) {
        return convertMap(getOrderList(ids), CrmOrderDO::getId);
    }

    PageResult<CrmOrderDO> getOrderPage(CrmOrderPageReqVO pageReqVO, Long userId);

    PageResult<CrmOrderDO> getOrderPageByCustomerId(CrmOrderPageReqVO pageReqVO);

    PageResult<CrmOrderDO> getOrderPageByBusinessId(CrmOrderPageReqVO pageReqVO);

    Long getAuditOrderCount(Long userId);

    List<CrmOrderItemDO> getOrderProductListByOrderId(Long orderId);

    // ======================== 额外功能 ========================

    /**
     * 提交订单审批
     */
    void submitOrder(Long id, Long userId);

    /**
     * 更新订单审核状态（BPM 回调）
     */
    void updateOrderAuditStatus(Long id, Integer bpmResult);

    /**
     * 转移订单负责人
     */
    void transferOrder(@Valid CrmOrderTransferReqVO reqVO, Long userId);

    /**
     * 更新订单状态（手动）
     */
    void updateOrderStatus(@Valid CrmOrderUpdateStatusReqVO reqVO);

    /**
     * 统计客户下的订单数量
     */
    Long getOrderCountByCustomerId(Long customerId);

    /**
     * 统计商机下的订单数量
     */
    Long getOrderCountByBusinessId(Long businessId);

    /**
     * 更新订单跟进信息
     */
    void updateOrderFollowUp(Long id, LocalDateTime contactNextTime);

}
