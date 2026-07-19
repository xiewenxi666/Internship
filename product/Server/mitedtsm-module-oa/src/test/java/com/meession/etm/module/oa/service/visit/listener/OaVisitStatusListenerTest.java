package com.meession.etm.module.oa.service.visit.listener;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.oa.service.visit.OaVisitService;
import com.meession.etm.module.oa.service.visit.OaVisitServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class OaVisitStatusListenerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaVisitStatusListener listener;

    @Mock
    private OaVisitService visitService;

    @Test
    void testGetProcessDefinitionKey() {
        assertEquals(OaVisitServiceImpl.PROCESS_KEY, listener.getProcessDefinitionKey());
    }

    @Test
    void testOnApplicationEvent_matchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey(OaVisitServiceImpl.PROCESS_KEY);
        event.setBusinessKey("111");
        event.setStatus(2);

        listener.onApplicationEvent(event);

        verify(visitService).updateVisitStatus(111L, 2);
    }

    @Test
    void testOnApplicationEvent_nonMatchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey("other_key");
        event.setBusinessKey("111");
        event.setStatus(2);

        listener.onApplicationEvent(event);

        verify(visitService, never()).updateVisitStatus(111L, 2);
    }

}
