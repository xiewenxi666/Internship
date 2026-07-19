package com.meession.etm.module.oa.service.workReport;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import jakarta.validation.Valid;

public interface OaWorkReportService {

    Long createWorkReport(Long userId, @Valid OaWorkReportCreateReqVO createReqVO);

    void updateWorkReport(Long id, @Valid OaWorkReportCreateReqVO updateReqVO);

    void deleteWorkReport(Long id);

    OaWorkReportDO getWorkReport(Long id);

    PageResult<OaWorkReportDO> getWorkReportPage(Long userId, OaWorkReportPageReqVO pageReqVO);

    void submitWorkReport(Long id);

    void reviewWorkReport(Long id, Long reviewerUserId, String reviewContent);

}