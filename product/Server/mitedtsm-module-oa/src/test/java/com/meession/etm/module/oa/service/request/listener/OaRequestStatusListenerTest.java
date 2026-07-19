package com.meession.etm.module.oa.service.request.listener;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.oa.service.request.OaRequestService;
import com.meession.etm.module.oa.service.request.OaRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class OaRequestStatusListenerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaRequestStatusListener listener;

    @Mock
    private OaRequestService requestService;

    @Test
    void testGetProcessDefinitionKey() {
        assertEquals(OaRequestServiceImpl.PROCESS_KEY, listener.getProcessDefinitionKey());
    }

    @Test
    void testOnApplicationEvent_matchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey(OaRequestServiceImpl.PROCESS_KEY);
        event.setBusinessKey("222");
        event.setStatus(3);

        listener.onApplicationEvent(event);

        verify(requestService).updateRequestStatus(222L, 3);
    }

    @Test
    void testOnApplicationEvent_nonMatchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey("other_key");
        event.setBusinessKey("222");
        event.setStatus(3);

        listener.onApplicationEvent(event);

        verify(requestService, never()).updateRequestStatus(222L, 3);
    }

}
