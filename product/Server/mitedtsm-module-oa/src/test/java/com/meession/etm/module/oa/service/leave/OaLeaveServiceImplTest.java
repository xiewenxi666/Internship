package com.meession.etm.module.oa.service.leave;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import com.meession.etm.module.oa.dal.mapper.OaLeaveMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LEAVE_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OaLeaveServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaLeaveServiceImpl leaveService;

    @Mock
    private OaLeaveMapper leaveMapper;

    @Mock
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    void testCreateLeave_success() {
        Long userId = randomLongId();
        LocalDateTime now = LocalDateTime.now();
        OaLeaveCreateReqVO reqVO = randomPojo(OaLeaveCreateReqVO.class, vo -> {
            vo.setStartTime(now);
            vo.setEndTime(now.plusDays(3));
        });
        doAnswer(invocation -> {
            OaLeaveDO arg = invocation.getArgument(0);
            arg.setId(randomLongId());
            return 1;
        }).when(leaveMapper).insert(any(OaLeaveDO.class));
        when(processInstanceApi.createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class)))
                .thenReturn("process-instance-id");

        Long id = leaveService.createLeave(userId, reqVO);

        assertNotNull(id);
        verify(leaveMapper).insert(any(OaLeaveDO.class));
        verify(leaveMapper).updateById(any(OaLeaveDO.class));
        verify(processInstanceApi).createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class));
    }

    @Test
    void testUpdateLeaveStatus_success() {
        Long id = randomLongId();
        Integer status = BpmTaskStatusEnum.APPROVE.getStatus();
        when(leaveMapper.selectById(id)).thenReturn(new OaLeaveDO().setId(id));

        leaveService.updateLeaveStatus(id, status);

        verify(leaveMapper).updateById((OaLeaveDO) argThat(d -> ((OaLeaveDO) d).getStatus().equals(status)));
    }

    @Test
    void testUpdateLeaveStatus_notExists() {
        Long id = randomLongId();
        when(leaveMapper.selectById(id)).thenReturn(null);

        assertServiceException(() -> leaveService.updateLeaveStatus(id, BpmTaskStatusEnum.APPROVE.getStatus()),
                OA_LEAVE_NOT_EXISTS);
    }

    @Test
    void testGetLeave_success() {
        Long id = randomLongId();
        OaLeaveDO expected = new OaLeaveDO().setId(id);
        when(leaveMapper.selectById(id)).thenReturn(expected);

        OaLeaveDO result = leaveService.getLeave(id);

        assertSame(expected, result);
    }
}
