package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import com.meession.etm.module.oa.dal.mysql.OaBusinessTripMapper;
import com.meession.etm.module.oa.service.businessTrip.OaBusinessTripServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_BUSINESS_TRIP_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaBusinessTripServiceImpl.class)
public class OaBusinessTripServiceTest extends BaseDbUnitTest {

    @Resource
    private OaBusinessTripServiceImpl businessTripService;

    @Resource
    private OaBusinessTripMapper businessTripMapper;

    @MockitoBean
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    public void testCreateBusinessTrip_success() {
        OaBusinessTripCreateReqVO reqVO = randomPojo(OaBusinessTripCreateReqVO.class, o -> {
            o.setStartTime(LocalDateTime.now());
            o.setEndTime(LocalDateTime.now().plusDays(2));
            o.setEstimatedAmount(BigDecimal.valueOf(1000));
        });
        Long userId = randomLongId();

        Long tripId = businessTripService.createBusinessTrip(userId, reqVO);

        assertNotNull(tripId);
        OaBusinessTripDO trip = businessTripMapper.selectById(tripId);
        assertEquals(userId, trip.getUserId());
        assertEquals(-1, trip.getStatus());
    }

    @Test
    public void testUpdateBusinessTrip_success() {
        OaBusinessTripDO dbTrip = randomPojo(OaBusinessTripDO.class, o -> {
            o.setStatus(-1);
            o.setDay(BigDecimal.valueOf(3));
            o.setEstimatedAmount(BigDecimal.valueOf(1000));
            o.setStartTime(LocalDateTime.now());
            o.setEndTime(LocalDateTime.now().plusDays(1));
        });
        businessTripMapper.insert(dbTrip);

        OaBusinessTripCreateReqVO updateReqVO = randomPojo(OaBusinessTripCreateReqVO.class, o -> {
            o.setStartTime(LocalDateTime.now().plusDays(3));
            o.setEndTime(LocalDateTime.now().plusDays(5));
            o.setEstimatedAmount(BigDecimal.valueOf(2000));
        });

        businessTripService.updateBusinessTrip(dbTrip.getId(), updateReqVO);

        OaBusinessTripDO updated = businessTripMapper.selectById(dbTrip.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status", "day",
                "processInstanceId", "startUserSelectAssignees",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId",
                "startTime", "endTime", "estimatedAmount");
    }

    @Test
    public void testDeleteBusinessTrip_success() {
        OaBusinessTripDO dbTrip = randomPojo(OaBusinessTripDO.class, o -> {
            o.setDay(BigDecimal.valueOf(3));
            o.setEstimatedAmount(BigDecimal.valueOf(1000));
        });
        businessTripMapper.insert(dbTrip);

        businessTripService.deleteBusinessTrip(dbTrip.getId());

        assertNull(businessTripMapper.selectById(dbTrip.getId()));
    }

    @Test
    public void testDeleteBusinessTrip_notExists() {
        assertServiceException(() -> businessTripService.deleteBusinessTrip(randomLongId()),
                OA_BUSINESS_TRIP_NOT_EXISTS);
    }

    @Test
    public void testUpdateBusinessTripStatus() {
        OaBusinessTripDO dbTrip = randomPojo(OaBusinessTripDO.class, o -> {
            o.setStatus(-1);
            o.setDay(BigDecimal.valueOf(3));
            o.setEstimatedAmount(BigDecimal.valueOf(1000));
        });
        businessTripMapper.insert(dbTrip);

        businessTripService.updateBusinessTripStatus(dbTrip.getId(), 2);

        OaBusinessTripDO updated = businessTripMapper.selectById(dbTrip.getId());
        assertEquals(2, (int) updated.getStatus());
    }
}
