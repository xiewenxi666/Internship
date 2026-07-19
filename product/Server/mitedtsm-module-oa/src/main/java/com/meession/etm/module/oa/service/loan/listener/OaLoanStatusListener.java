package com.meession.etm.module.oa.service.loan.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.oa.service.loan.OaLoanService;
import com.meession.etm.module.oa.service.loan.OaLoanServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class OaLoanStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaLoanService loanService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaLoanServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        loanService.updateLoanStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}