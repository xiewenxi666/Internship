package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.module.oa.dal.mysql.OaRequestMapper;
import com.meession.etm.module.oa.service.request.OaRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.annotation.Resource;
import java.math.BigDecimal;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.*;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaRequestServiceImpl.class)
public class OaRequestServiceTest extends BaseDbUnitTest {

    @Resource
    private OaRequestServiceImpl requestService;

    @Resource
    private OaRequestMapper requestMapper;

    @MockitoBean
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    public void testCreateRequest_success() {
        OaRequestCreateReqVO reqVO = randomPojo(OaRequestCreateReqVO.class, o -> {
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        Long userId = randomLongId();

        Long requestId = requestService.createRequest(userId, reqVO);

        assertNotNull(requestId);
        OaRequestDO req = requestMapper.selectById(requestId);
        assertEquals(userId, req.getUserId());
        assertEquals(-1, req.getStatus());
    }

    @Test
    public void testUpdateRequest_success() {
        OaRequestDO dbReq = randomPojo(OaRequestDO.class, o -> {
            o.setStatus(-1);
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        requestMapper.insert(dbReq);

        OaRequestCreateReqVO updateReqVO = randomPojo(OaRequestCreateReqVO.class, o -> {
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(2000));
        });

        requestService.updateRequest(dbReq.getId(), updateReqVO);

        OaRequestDO updated = requestMapper.selectById(dbReq.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status",
                "processInstanceId", "startUserSelectAssignees",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId",
                "expectedAmount");
    }

    @Test
    public void testCancelRequest_success() {
        OaRequestDO dbReq = randomPojo(OaRequestDO.class, o -> {
            o.setStatus(1);
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        requestMapper.insert(dbReq);

        requestService.cancelRequest(dbReq.getId(), randomLongId());

        OaRequestDO updated = requestMapper.selectById(dbReq.getId());
        assertEquals(4, (int) updated.getStatus());
    }

    @Test
    public void testCancelRequest_fail_notRunning() {
        OaRequestDO dbReq = randomPojo(OaRequestDO.class, o -> {
            o.setStatus(-1);
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        requestMapper.insert(dbReq);

        assertServiceException(() -> requestService.cancelRequest(dbReq.getId(), randomLongId()),
                OA_REQUEST_CANCEL_FAIL_NOT_RUNNING);
    }

    @Test
    public void testReconsiderRequest_fail_notRejected() {
        OaRequestDO dbReq = randomPojo(OaRequestDO.class, o -> {
            o.setStatus(-1);
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        requestMapper.insert(dbReq);

        assertServiceException(() -> requestService.reconsiderRequest(dbReq.getId(), randomLongId()),
                OA_REQUEST_RECONSIDER_FAIL_NOT_REJECTED);
    }

    @Test
    public void testDeleteRequest_success() {
        OaRequestDO dbReq = randomPojo(OaRequestDO.class, o -> {
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        requestMapper.insert(dbReq);

        requestService.deleteRequest(dbReq.getId());

        assertNull(requestMapper.selectById(dbReq.getId()));
    }

    @Test
    public void testUpdateRequestStatus() {
        OaRequestDO dbReq = randomPojo(OaRequestDO.class, o -> {
            o.setStatus(1);
            o.setUrgency(1);
            o.setExpectedAmount(BigDecimal.valueOf(1000));
        });
        requestMapper.insert(dbReq);

        requestService.updateRequestStatus(dbReq.getId(), 2);

        OaRequestDO updated = requestMapper.selectById(dbReq.getId());
        assertEquals(2, (int) updated.getStatus());
    }
}
