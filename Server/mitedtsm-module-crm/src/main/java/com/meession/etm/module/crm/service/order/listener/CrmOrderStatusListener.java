/**
 * 订单审批结果监听器
 *
 * @author 23计三倪雨晗
 */
package com.meession.etm.module.crm.service.order.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.crm.service.order.CrmOrderService;
import com.meession.etm.module.crm.service.order.CrmOrderServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class CrmOrderStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CrmOrderService orderService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmOrderServiceImpl.BPM_PROCESS_DEFINITION_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        orderService.updateOrderAuditStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
