package com.meession.etm.module.crm.service.refund.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.crm.service.refund.CrmRefundService;
import com.meession.etm.module.crm.service.refund.CrmRefundServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 退款审批的结果的监听器实现类
 *
 * @author HUIHUI
 */
@Component
public class CrmRefundStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CrmRefundService refundService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmRefundServiceImpl.BPM_PROCESS_DEFINITION_KEY;
    }

    @Override
    public void onEvent(BpmProcessInstanceStatusEvent event) {
        refundService.updateRefundAuditStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
