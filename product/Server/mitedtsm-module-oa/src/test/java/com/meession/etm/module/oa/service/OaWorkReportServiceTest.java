package com.meession.etm.module.oa.service;

import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportCreateReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import com.meession.etm.module.oa.dal.mysql.OaWorkReportMapper;
import com.meession.etm.module.oa.service.workReport.OaWorkReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;
import java.time.LocalDate;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.*;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_WORK_REPORT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaWorkReportServiceImpl.class)
public class OaWorkReportServiceTest extends BaseDbUnitTest {

    @Resource
    private OaWorkReportServiceImpl workReportService;

    @Resource
    private OaWorkReportMapper workReportMapper;

    @Test
    public void testCreateWorkReport_success() {
        OaWorkReportCreateReqVO reqVO = randomPojo(OaWorkReportCreateReqVO.class);
        Long userId = randomLongId();

        Long reportId = workReportService.createWorkReport(userId, reqVO);

        assertNotNull(reportId);
        OaWorkReportDO report = workReportMapper.selectById(reportId);
        assertEquals(userId, report.getUserId());
        assertEquals(0, report.getStatus());
    }

    @Test
    public void testUpdateWorkReport_success() {
        OaWorkReportDO dbReport = randomPojo(OaWorkReportDO.class, o -> o.setStatus(0));
        workReportMapper.insert(dbReport);

        OaWorkReportCreateReqVO updateReqVO = randomPojo(OaWorkReportCreateReqVO.class);

        workReportService.updateWorkReport(dbReport.getId(), updateReqVO);

        OaWorkReportDO updated = workReportMapper.selectById(dbReport.getId());
        assertPojoEquals(updateReqVO, updated, "id", "userId", "status",
                "creator", "createTime", "updater", "updateTime", "deleted", "tenantId");
    }

    @Test
    public void testDeleteWorkReport_success() {
        OaWorkReportDO dbReport = randomPojo(OaWorkReportDO.class);
        workReportMapper.insert(dbReport);

        workReportService.deleteWorkReport(dbReport.getId());

        assertNull(workReportMapper.selectById(dbReport.getId()));
    }

    @Test
    public void testDeleteWorkReport_notExists() {
        assertServiceException(() -> workReportService.deleteWorkReport(randomLongId()),
                OA_WORK_REPORT_NOT_EXISTS);
    }
}
