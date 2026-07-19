package com.meession.etm.module.oa.service.visit.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.oa.service.visit.OaVisitService;
import com.meession.etm.module.oa.service.visit.OaVisitServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class OaVisitStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaVisitService visitService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaVisitServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        visitService.updateVisitStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
