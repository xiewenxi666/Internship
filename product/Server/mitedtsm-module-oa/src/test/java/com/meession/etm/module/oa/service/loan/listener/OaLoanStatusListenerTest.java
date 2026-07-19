package com.meession.etm.module.oa.service.loan.listener;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.event.BpmProcessInstanceStatusEvent;
import com.meession.etm.module.oa.service.loan.OaLoanService;
import com.meession.etm.module.oa.service.loan.OaLoanServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class OaLoanStatusListenerTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaLoanStatusListener listener;

    @Mock
    private OaLoanService loanService;

    @Test
    void testGetProcessDefinitionKey() {
        assertEquals(OaLoanServiceImpl.PROCESS_KEY, listener.getProcessDefinitionKey());
    }

    @Test
    void testOnApplicationEvent_matchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey(OaLoanServiceImpl.PROCESS_KEY);
        event.setBusinessKey("789");
        event.setStatus(4);

        listener.onApplicationEvent(event);

        verify(loanService).updateLoanStatus(789L, 4);
    }

    @Test
    void testOnApplicationEvent_nonMatchingKey() {
        BpmProcessInstanceStatusEvent event = new BpmProcessInstanceStatusEvent("source");
        event.setProcessDefinitionKey("other_key");
        event.setBusinessKey("789");
        event.setStatus(4);

        listener.onApplicationEvent(event);

        verify(loanService, never()).updateLoanStatus(789L, 4);
    }

}
