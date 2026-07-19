package com.meession.etm.module.oa.service.leave.listener;

import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEventListener;
import com.meession.etm.module.oa.service.leave.OaLeaveService;
import com.meession.etm.module.oa.service.leave.OaLeaveServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class OaLeaveStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private OaLeaveService leaveService;

    @Override
    protected String getProcessDefinitionKey() {
        return OaLeaveServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        leaveService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}