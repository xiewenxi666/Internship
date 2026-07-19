package com.meession.etm.module.crm.service.reimbursement.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.crm.service.reimbursement.CrmReimbursementService;
import com.meession.etm.module.crm.service.reimbursement.CrmReimbursementServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * 报销审批的结果的监听器实现类
 *
 * @author HUIHUI
 */
@Component
public class CrmReimbursementStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private CrmReimbursementService reimbursementService;

    @Override
    public String getProcessDefinitionKey() {
        return CrmReimbursementServiceImpl.BPM_PROCESS_DEFINITION_KEY;
    }

    @Override
    public void onEvent(BpmProcessInstanceStatusEvent event) {
        reimbursementService.updateReimbursementAuditStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
