package com.meession.etm.module.oa.service;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeaveCreateReqVO;
import com.meession.etm.module.oa.controller.admin.leave.vo.OaLeavePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import com.meession.etm.module.oa.dal.mysql.OaLeaveMapper;
import com.meession.etm.module.oa.service.leave.OaLeaveServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_LEAVE_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaLeaveServiceImpl.class)
public class OaLeaveServiceTest extends BaseDbUnitTest {

    @Resource
    private OaLeaveServiceImpl leaveService;

    @Resource
    private OaLeaveMapper leaveMapper;

    @MockitoBean
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    public void testCreateLeave_success() {
        OaLeaveCreateReqVO reqVO = randomPojo(OaLeaveCreateReqVO.class, o -> {
            o.setStartTime(LocalDateTime.now());
            o.setEndTime(LocalDateTime.now().plusDays(3));
        });
        Long userId = randomLongId();

        Long leaveId = leaveService.createLeave(userId, reqVO);

        assertNotNull(leaveId);
        OaLeaveDO leave = leaveMapper.selectById(leaveId);
        assertEquals(userId, leave.getUserId());
        assertEquals(-1, leave.getStatus());
        assertNotNull(leave.getDay());
    }

    @Test
    public void testUpdateLeave_success() {
        OaLeaveDO dbLeave = randomPojo(OaLeaveDO.class, o -> {
            o.setStatus(-1);
            o.setDay(BigDecimal.valueOf(3));
            o.setStartTime(LocalDateTime.now());
            o.setEndTime(LocalDateTime.now().plusDays(1));
        });
        leaveMapper.insert(dbLeave);

        OaLeaveCreateReqVO updateReqVO = randomPojo(OaLeaveCreateReqVO.class, o -> {
            o.setStartTime(LocalDateTime.now().plusDays(2));
            o.setEndTime(LocalDateTime.now().plusDays(5));
        });

        leaveService.updateLeave(dbLeave.getId(), updateReqVO);

        OaLeaveDO updated = leaveMapper.selectById(dbLeave.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status", "day",
                "processInstanceId", "startUserSelectAssignees",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId",
                "startTime", "endTime");
    }

    @Test
    public void testUpdateLeave_notExists() {
        OaLeaveCreateReqVO reqVO = randomPojo(OaLeaveCreateReqVO.class);
        assertServiceException(() -> leaveService.updateLeave(randomLongId(), reqVO), OA_LEAVE_NOT_EXISTS);
    }

    @Test
    public void testDeleteLeave_success() {
        OaLeaveDO dbLeave = randomPojo(OaLeaveDO.class, o -> o.setDay(BigDecimal.valueOf(3)));
        leaveMapper.insert(dbLeave);

        leaveService.deleteLeave(dbLeave.getId());

        assertNull(leaveMapper.selectById(dbLeave.getId()));
    }

    @Test
    public void testDeleteLeave_notExists() {
        assertServiceException(() -> leaveService.deleteLeave(randomLongId()), OA_LEAVE_NOT_EXISTS);
    }

    @Test
    public void testGetLeave_success() {
        OaLeaveDO dbLeave = randomPojo(OaLeaveDO.class, o -> o.setDay(BigDecimal.valueOf(3)));
        leaveMapper.insert(dbLeave);

        OaLeaveDO result = leaveService.getLeave(dbLeave.getId());

        assertPojoEquals(dbLeave, result, "startTime", "endTime", "day");
    }

    @Test
    public void testGetLeavePage() {
        Long userId = randomLongId();
        for (int i = 0; i < 5; i++) {
            leaveMapper.insert(randomPojo(OaLeaveDO.class, o -> {
                o.setUserId(userId);
                o.setDay(BigDecimal.valueOf(3));
            }));
        }

        OaLeavePageReqVO pageReqVO = new OaLeavePageReqVO();
        PageResult<OaLeaveDO> result = leaveService.getLeavePage(userId, pageReqVO);

        assertEquals(5, result.getTotal());
    }

    @Test
    public void testUpdateLeaveStatus_success() {
        OaLeaveDO dbLeave = randomPojo(OaLeaveDO.class, o -> {
            o.setStatus(-1);
            o.setDay(BigDecimal.valueOf(3));
        });
        leaveMapper.insert(dbLeave);

        leaveService.updateLeaveStatus(dbLeave.getId(), 2);

        OaLeaveDO updated = leaveMapper.selectById(dbLeave.getId());
        assertEquals(2, (int) updated.getStatus());
    }
}
