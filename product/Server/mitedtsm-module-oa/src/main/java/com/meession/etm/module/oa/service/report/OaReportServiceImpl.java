package com.meession.etm.module.oa.service.report;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaReportDO;
import com.meession.etm.module.oa.dal.mapper.OaReportMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_REPORT_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaReportServiceImpl implements OaReportService {

    @Resource
    private OaReportMapper reportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createReport(Long userId, OaReportCreateReqVO createReqVO) {
        OaReportDO report = BeanUtils.toBean(createReqVO, OaReportDO.class)
                .setUserId(userId);
        reportMapper.insert(report);
        return report.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateReport(OaReportDO report) {
        validateReportExists(report.getId());
        reportMapper.updateById(report);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteReport(Long id) {
        validateReportExists(id);
        reportMapper.deleteById(id);
    }

    private void validateReportExists(Long id) {
        if (reportMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_REPORT_NOT_EXISTS);
        }
    }

    @Override
    public OaReportDO getReport(Long id) {
        return reportMapper.selectById(id);
    }

    @Override
    public PageResult<OaReportDO> getReportPage(Long userId, OaReportPageReqVO pageReqVO) {
        return reportMapper.selectPage(userId, pageReqVO);
    }

}
