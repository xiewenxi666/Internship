package com.meession.etm.module.oa.service.request;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.module.oa.dal.mapper.OaRequestMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REQUEST_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OaRequestServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaRequestServiceImpl requestService;

    @Mock
    private OaRequestMapper requestMapper;

    @Mock
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    void testCreateRequest_success() {
        Long userId = randomLongId();
        OaRequestCreateReqVO reqVO = randomPojo(OaRequestCreateReqVO.class);
        doAnswer(invocation -> {
            OaRequestDO arg = invocation.getArgument(0);
            arg.setId(randomLongId());
            return 1;
        }).when(requestMapper).insert(any(OaRequestDO.class));
        when(processInstanceApi.createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class)))
                .thenReturn("process-instance-id");

        Long id = requestService.createRequest(userId, reqVO);

        assertNotNull(id);
        verify(requestMapper).insert(any(OaRequestDO.class));
        verify(requestMapper).updateById(any(OaRequestDO.class));
        verify(processInstanceApi).createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class));
    }

    @Test
    void testUpdateRequestStatus_success() {
        Long id = randomLongId();
        Integer status = BpmTaskStatusEnum.APPROVE.getStatus();
        when(requestMapper.selectById(id)).thenReturn(new OaRequestDO().setId(id));

        requestService.updateRequestStatus(id, status);

        verify(requestMapper).updateById((OaRequestDO) argThat(d -> ((OaRequestDO) d).getStatus().equals(status)));
    }

    @Test
    void testUpdateRequestStatus_notExists() {
        Long id = randomLongId();
        when(requestMapper.selectById(id)).thenReturn(null);

        assertServiceException(() -> requestService.updateRequestStatus(id, BpmTaskStatusEnum.APPROVE.getStatus()),
                OA_REQUEST_NOT_EXISTS);
    }

    @Test
    void testGetRequest_success() {
        Long id = randomLongId();
        OaRequestDO expected = new OaRequestDO().setId(id);
        when(requestMapper.selectById(id)).thenReturn(expected);

        OaRequestDO result = requestService.getRequest(id);

        assertSame(expected, result);
    }
}
