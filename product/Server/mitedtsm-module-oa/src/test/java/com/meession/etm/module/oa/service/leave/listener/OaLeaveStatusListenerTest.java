package com.meession.etm.module.oa.service.leave.listener;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.oa.service.leave.OaLeaveService;
import com.meession.etm.module.oa.service.leave.OaLeaveServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class OaLeaveStatusListenerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaLeaveStatusListener listener;

    @Mock
    private OaLeaveService leaveService;

    @Test
    void testGetProcessDefinitionKey() {
        assertEquals(OaLeaveServiceImpl.PROCESS_KEY, listener.getProcessDefinitionKey());
    }

    @Test
    void testOnApplicationEvent_matchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey(OaLeaveServiceImpl.PROCESS_KEY);
        event.setBusinessKey("123");
        event.setStatus(2);

        listener.onApplicationEvent(event);

        verify(leaveService).updateLeaveStatus(123L, 2);
    }

    @Test
    void testOnApplicationEvent_nonMatchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey("other_key");
        event.setBusinessKey("123");
        event.setStatus(2);

        listener.onApplicationEvent(event);

        verify(leaveService, never()).updateLeaveStatus(123L, 2);
    }

}
