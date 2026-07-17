package com.meession.etm.module.oa.service.visit;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.module.oa.dal.mapper.OaVisitMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_VISIT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OaVisitServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaVisitServiceImpl visitService;

    @Mock
    private OaVisitMapper visitMapper;

    @Mock
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    void testCreateVisit_success() {
        Long userId = randomLongId();
        OaVisitCreateReqVO reqVO = randomPojo(OaVisitCreateReqVO.class);
        doAnswer(invocation -> {
            OaVisitDO arg = invocation.getArgument(0);
            arg.setId(randomLongId());
            return 1;
        }).when(visitMapper).insert(any(OaVisitDO.class));
        when(processInstanceApi.createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class)))
                .thenReturn("process-instance-id");

        Long id = visitService.createVisit(userId, reqVO);

        assertNotNull(id);
        verify(visitMapper).insert(any(OaVisitDO.class));
        verify(visitMapper).updateById(any(OaVisitDO.class));
        verify(processInstanceApi).createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class));
    }

    @Test
    void testUpdateVisitStatus_success() {
        Long id = randomLongId();
        Integer status = BpmTaskStatusEnum.APPROVE.getStatus();
        when(visitMapper.selectById(id)).thenReturn(new OaVisitDO().setId(id));

        visitService.updateVisitStatus(id, status);

        verify(visitMapper).updateById((OaVisitDO) argThat(d -> ((OaVisitDO) d).getStatus().equals(status)));
    }

    @Test
    void testUpdateVisitStatus_notExists() {
        Long id = randomLongId();
        when(visitMapper.selectById(id)).thenReturn(null);

        assertServiceException(() -> visitService.updateVisitStatus(id, BpmTaskStatusEnum.APPROVE.getStatus()),
                OA_VISIT_NOT_EXISTS);
    }

    @Test
    void testGetVisit_success() {
        Long id = randomLongId();
        OaVisitDO expected = new OaVisitDO().setId(id);
        when(visitMapper.selectById(id)).thenReturn(expected);

        OaVisitDO result = visitService.getVisit(id);

        assertSame(expected, result);
    }
}
