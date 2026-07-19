package com.meession.etm.module.oa.service.businessTrip.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripService;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class OaBusinessTripStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaBusinessTripService businessTripService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaBusinessTripServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        businessTripService.updateBusinessTripStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}