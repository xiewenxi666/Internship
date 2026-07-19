package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.bpm.api.task.BpmProcessInstanceApi;
import com.meession.etm.module.oa.controller.admin.visit.vo.OaVisitCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import com.meession.etm.module.oa.dal.mysql.OaVisitMapper;
import com.meession.etm.module.oa.service.visit.OaVisitServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import jakarta.annotation.Resource;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_VISIT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@Import(OaVisitServiceImpl.class)
public class OaVisitServiceTest extends BaseDbUnitTest {

    @Resource
    private OaVisitServiceImpl visitService;

    @Resource
    private OaVisitMapper visitMapper;

    @MockitoBean
    private BpmProcessInstanceApi processInstanceApi;

    @Test
    public void testCreateVisit_success() {
        OaVisitCreateReqVO reqVO = randomPojo(OaVisitCreateReqVO.class);
        Long userId = randomLongId();

        Long visitId = visitService.createVisit(userId, reqVO);

        assertNotNull(visitId);
        OaVisitDO visit = visitMapper.selectById(visitId);
        assertEquals(userId, visit.getUserId());
        assertEquals(-1, visit.getStatus());
    }

    @Test
    public void testUpdateVisit_success() {
        OaVisitDO dbVisit = randomPojo(OaVisitDO.class, o -> o.setStatus(-1));
        visitMapper.insert(dbVisit);

        OaVisitCreateReqVO updateReqVO = randomPojo(OaVisitCreateReqVO.class);

        visitService.updateVisit(dbVisit.getId(), updateReqVO);

        OaVisitDO updated = visitMapper.selectById(dbVisit.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status",
                "processInstanceId", "startUserSelectAssignees",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId");
    }

    @Test
    public void testUpdateVisit_notExists() {
        OaVisitCreateReqVO reqVO = randomPojo(OaVisitCreateReqVO.class);
        assertServiceException(() -> visitService.updateVisit(randomLongId(), reqVO), OA_VISIT_NOT_EXISTS);
    }

    @Test
    public void testSubmitVisit_success() {
        OaVisitDO dbVisit = randomPojo(OaVisitDO.class, o -> o.setStatus(-1));
        visitMapper.insert(dbVisit);
        when(processInstanceApi.createProcessInstance(any(), any()))
                .thenReturn("mock-process-instance-id");

        visitService.submitVisit(dbVisit.getId(), randomLongId());

        OaVisitDO updated = visitMapper.selectById(dbVisit.getId());
        assertEquals(1, (int) updated.getStatus());
        assertNotNull(updated.getProcessInstanceId());
    }

    @Test
    public void testSubmitVisit_fail_notDraft() {
        OaVisitDO dbVisit = randomPojo(OaVisitDO.class, o -> o.setStatus(1));
        visitMapper.insert(dbVisit);

        assertServiceException(() -> visitService.submitVisit(dbVisit.getId(), randomLongId()),
                com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_VISIT_SUBMIT_FAIL_NOT_DRAFT);
    }

    @Test
    public void testDeleteVisit_success() {
        OaVisitDO dbVisit = randomPojo(OaVisitDO.class, o -> o.setStatus(-1));
        visitMapper.insert(dbVisit);

        visitService.deleteVisit(dbVisit.getId());

        assertNull(visitMapper.selectById(dbVisit.getId()));
    }

    @Test
    public void testUpdateVisitStatus() {
        OaVisitDO dbVisit = randomPojo(OaVisitDO.class, o -> o.setStatus(-1));
        visitMapper.insert(dbVisit);

        visitService.updateVisitStatus(dbVisit.getId(), 1);

        OaVisitDO updated = visitMapper.selectById(dbVisit.getId());
        assertEquals(1, (int) updated.getStatus());
    }
}
