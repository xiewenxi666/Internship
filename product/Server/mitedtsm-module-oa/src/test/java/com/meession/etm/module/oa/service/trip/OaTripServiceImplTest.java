package com.meession.etm.module.oa.service.trip;

import com.meession.etm.framework.test.core.ut.BaseMockitoUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.meession.etm.module.bpm.enums.task.BpmTaskStatusEnum;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTripDO;
import com.meession.etm.module.oa.dal.mapper.OaTripMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDateTime;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_TRIP_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OaTripServiceImplTest extends BaseMockitoUnitTest {

    @InjectMocks
    private OaTripServiceImpl tripService;

    @Mock
    private OaTripMapper tripMapper;

    @Mock
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    void testCreateTrip_success() {
        Long userId = randomLongId();
        LocalDateTime now = LocalDateTime.now();
        OaTripCreateReqVO reqVO = randomPojo(OaTripCreateReqVO.class, vo -> {
            vo.setStartTime(now);
            vo.setEndTime(now.plusDays(2));
        });
        doAnswer(invocation -> {
            OaTripDO arg = invocation.getArgument(0);
            arg.setId(randomLongId());
            return 1;
        }).when(tripMapper).insert(any(OaTripDO.class));
        when(processInstanceApi.createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class)))
                .thenReturn("process-instance-id");

        Long id = tripService.createTrip(userId, reqVO);

        assertNotNull(id);
        verify(tripMapper).insert(any(OaTripDO.class));
        verify(tripMapper).updateById(any(OaTripDO.class));
        verify(processInstanceApi).createProcessInstance(eq(userId), any(BpmProcessInstanceCreateReqDTO.class));
    }

    @Test
    void testUpdateTripStatus_success() {
        Long id = randomLongId();
        Integer status = BpmTaskStatusEnum.APPROVE.getStatus();
        when(tripMapper.selectById(id)).thenReturn(new OaTripDO().setId(id));

        tripService.updateTripStatus(id, status);

        verify(tripMapper).updateById((OaTripDO) argThat(d -> ((OaTripDO) d).getStatus().equals(status)));
    }

    @Test
    void testUpdateTripStatus_notExists() {
        Long id = randomLongId();
        when(tripMapper.selectById(id)).thenReturn(null);

        assertServiceException(() -> tripService.updateTripStatus(id, BpmTaskStatusEnum.APPROVE.getStatus()),
                OA_TRIP_NOT_EXISTS);
    }

    @Test
    void testGetTrip_success() {
        Long id = randomLongId();
        OaTripDO expected = new OaTripDO().setId(id);
        when(tripMapper.selectById(id)).thenReturn(expected);

        OaTripDO result = tripService.getTrip(id);

        assertSame(expected, result);
    }
}
