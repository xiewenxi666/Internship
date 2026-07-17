package com.meession.etm.module.oa.service.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.oa.service.request.OaRequestService;
import com.meession.etm.module.oa.service.request.OaRequestServiceImpl;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class OaRequestStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaRequestService requestService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaRequestServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        requestService.updateRequestStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
