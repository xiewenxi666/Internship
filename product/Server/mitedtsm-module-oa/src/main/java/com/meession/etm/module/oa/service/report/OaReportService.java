package com.meession.etm.module.oa.service.report;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaReportDO;

public interface OaReportService {

    Long createReport(Long userId, OaReportCreateReqVO createReqVO);

    void updateReport(OaReportDO report);

    void deleteReport(Long id);

    OaReportDO getReport(Long id);

    PageResult<OaReportDO> getReportPage(Long userId, OaReportPageReqVO pageReqVO);

}
