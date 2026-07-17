package com.meession.etm.module.oa.service.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.oa.service.trip.OaTripService;
import com.meession.etm.module.oa.service.trip.OaTripServiceImpl;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Component
public class OaTripStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaTripService tripService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaTripServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        tripService.updateTripStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
