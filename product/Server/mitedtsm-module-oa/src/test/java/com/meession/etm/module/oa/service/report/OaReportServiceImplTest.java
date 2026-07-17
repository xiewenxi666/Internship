package com.meession.etm.module.oa.service.report;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.test.core.ut.BaseDbUnitTest;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaReportDO;
import com.meession.etm.module.oa.dal.mapper.OaReportMapper;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;

import jakarta.annotation.Resource;

import static com.meession.etm.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.meession.etm.framework.test.core.util.AssertUtils.assertServiceException;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomLongId;
import static com.meession.etm.framework.test.core.util.RandomUtils.randomPojo;
import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REPORT_NOT_EXISTS;
import static org.junit.jupiter.api.Assertions.*;

@Import(OaReportServiceImpl.class)
class OaReportServiceImplTest extends BaseDbUnitTest {

    @Resource
    private OaReportServiceImpl reportService;

    @Resource
    private OaReportMapper reportMapper;

    @Test
    void testCreateReport_success() {
        OaReportCreateReqVO reqVO = randomPojo(OaReportCreateReqVO.class);
        Long userId = randomLongId();

        Long id = reportService.createReport(userId, reqVO);

        assertNotNull(id);
        OaReportDO report = reportMapper.selectById(id);
        assertNotNull(report);
        assertEquals(userId, report.getUserId());
    }

    @Test
    void testGetReport_success() {
        OaReportDO dbReport = randomPojo(OaReportDO.class);
        reportMapper.insert(dbReport);

        OaReportDO report = reportService.getReport(dbReport.getId());

        assertNotNull(report);
        assertPojoEquals(dbReport, report);
    }

    @Test
    void testGetReport_notExists() {
        assertNull(reportService.getReport(randomLongId()));
    }

    @Test
    void testGetReportPage_success() {
        OaReportDO dbReport = randomPojo(OaReportDO.class, o -> o.setType("1"));
        reportMapper.insert(dbReport);
        OaReportDO otherType = new OaReportDO();
        otherType.setUserId(dbReport.getUserId());
        otherType.setType("2");
        otherType.setReportDate(dbReport.getReportDate());
        otherType.setContent(dbReport.getContent());
        reportMapper.insert(otherType);
        OaReportDO otherUser = randomPojo(OaReportDO.class);
        reportMapper.insert(otherUser);

        OaReportPageReqVO reqVO = new OaReportPageReqVO();
        reqVO.setType(1);

        PageResult<OaReportDO> result = reportService.getReportPage(dbReport.getUserId(), reqVO);

        assertEquals(1, result.getTotal());
        assertPojoEquals(dbReport, result.getList().get(0));
    }

    @Test
    void testGetReportPage_noFilter() {
        OaReportDO dbReport = randomPojo(OaReportDO.class);
        reportMapper.insert(dbReport);
        OaReportDO copy = new OaReportDO();
        copy.setUserId(dbReport.getUserId());
        copy.setType(dbReport.getType());
        copy.setReportDate(dbReport.getReportDate());
        copy.setContent(dbReport.getContent());
        reportMapper.insert(copy);

        OaReportPageReqVO reqVO = new OaReportPageReqVO();

        PageResult<OaReportDO> result = reportService.getReportPage(dbReport.getUserId(), reqVO);

        assertEquals(2, result.getTotal());
    }

    @Test
    void testUpdateReport_success() {
        OaReportDO dbReport = randomPojo(OaReportDO.class);
        reportMapper.insert(dbReport);

        OaReportDO update = new OaReportDO();
        update.setId(dbReport.getId());
        update.setContent("Updated content");
        reportService.updateReport(update);

        OaReportDO report = reportMapper.selectById(dbReport.getId());
        assertEquals("Updated content", report.getContent());
    }

    @Test
    void testUpdateReport_notExists() {
        assertServiceException(() ->
                        reportService.updateReport(new OaReportDO().setId(randomLongId())),
                OA_REPORT_NOT_EXISTS);
    }

    @Test
    void testDeleteReport_success() {
        OaReportDO dbReport = randomPojo(OaReportDO.class);
        reportMapper.insert(dbReport);

        reportService.deleteReport(dbReport.getId());

        assertNull(reportMapper.selectById(dbReport.getId()));
    }

    @Test
    void testDeleteReport_notExists() {
        assertServiceException(() ->
                        reportService.deleteReport(randomLongId()),
                OA_REPORT_NOT_EXISTS);
    }
}
