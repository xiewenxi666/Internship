package com.meession.etm.module.oa.service.loan;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.loan.OaLoanCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.module.oa.dal.mapper.OaLoanMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LOAN_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OaLoanServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaLoanServiceImpl loanService;

    @Mock
    private OaLoanMapper loanMapper;

    @Mock
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    void testCreateLoan_success() {
        Long userId = randomLongId();
        OaLoanCreateReqVO reqVO = randomPojo(OaLoanCreateReqVO.class);
        doAnswer(invocation -> {
            OaLoanDO arg = invocation.getArgument(0);
            arg.setId(randomLongId());
            return 1;
        }).when(loanMapper).insert(any(OaLoanDO.class));
        when(processInstanceApi.createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class)))
                .thenReturn("process-instance-id");

        Long id = loanService.createLoan(userId, reqVO);

        assertNotNull(id);
        verify(loanMapper).insert(any(OaLoanDO.class));
        verify(loanMapper).updateById(any(OaLoanDO.class));
        verify(processInstanceApi).createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class));
    }

    @Test
    void testUpdateLoanStatus_success() {
        Long id = randomLongId();
        Integer status = BpmTaskStatusEnum.APPROVE.getStatus();
        when(loanMapper.selectById(id)).thenReturn(new OaLoanDO().setId(id));

        loanService.updateLoanStatus(id, status);

        verify(loanMapper).updateById((OaLoanDO) argThat(d -> ((OaLoanDO) d).getStatus().equals(status)));
    }

    @Test
    void testUpdateLoanStatus_notExists() {
        Long id = randomLongId();
        when(loanMapper.selectById(id)).thenReturn(null);

        assertServiceException(() -> loanService.updateLoanStatus(id, BpmTaskStatusEnum.APPROVE.getStatus()),
                OA_LOAN_NOT_EXISTS);
    }

    @Test
    void testGetLoan_success() {
        Long id = randomLongId();
        OaLoanDO expected = new OaLoanDO().setId(id);
        when(loanMapper.selectById(id)).thenReturn(expected);

        OaLoanDO result = loanService.getLoan(id);

        assertSame(expected, result);
    }
}
