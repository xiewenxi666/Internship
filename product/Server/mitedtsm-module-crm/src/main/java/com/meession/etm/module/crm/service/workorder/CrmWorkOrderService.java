// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.service.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.crm.controller.admin.workorder.vo.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.controller.admin.workorder.vo.CrmWorkOrderSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;

import jakarta.validation.Valid;

public interface CrmWorkOrderService {

    /**
     * 创建工单
     */
    Long createWorkOrder(@Valid CrmWorkOrderSaveReqVO createReqVO);

    /**
     * 更新工单
     */
    void updateWorkOrder(@Valid CrmWorkOrderSaveReqVO updateReqVO);

    /**
     * 删除工单
     */
    void deleteWorkOrder(Long id);

    /**
     * 处理工单（更新状态为"处理中"）
     */
    void processWorkOrder(Long id);

    /**
     * 工单完结
     */
    void completeWorkOrder(Long id, String solution);

    /**
     * 工单退回
     */
    void returnWorkOrder(Long id);

    /**
     * 获得工单
     */
    CrmWorkOrderDO getWorkOrder(Long id);

    /**
     * 获得工单分页
     */
    PageResult<CrmWorkOrderDO> getWorkOrderPage(CrmWorkOrderPageReqVO pageReqVO);

    /**
     * 获得工单分页（基于客户）
     */
    PageResult<CrmWorkOrderDO> getWorkOrderPageByCustomer(CrmWorkOrderPageReqVO pageReqVO);

}
