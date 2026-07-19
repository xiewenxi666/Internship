package com.meession.etm.module.oa.service.businessTrip.listener;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripService;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class OaBusinessTripStatusListenerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaBusinessTripStatusListener listener;

    @Mock
    private OaBusinessTripService businessTripService;

    @Test
    void testGetProcessDefinitionKey() {
        assertEquals(OaBusinessTripServiceImpl.PROCESS_KEY, listener.getProcessDefinitionKey());
    }

    @Test
    void testOnApplicationEvent_matchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey(OaBusinessTripServiceImpl.PROCESS_KEY);
        event.setBusinessKey("456");
        event.setStatus(3);

        listener.onApplicationEvent(event);

        verify(businessTripService).updateBusinessTripStatus(456L, 3);
    }

    @Test
    void testOnApplicationEvent_nonMatchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey("other_key");
        event.setBusinessKey("456");
        event.setStatus(3);

        listener.onApplicationEvent(event);

        verify(businessTripService, never()).updateBusinessTripStatus(456L, 3);
    }

}
