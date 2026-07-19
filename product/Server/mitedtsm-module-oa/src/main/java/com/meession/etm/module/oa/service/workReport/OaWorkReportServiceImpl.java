package com.meession.etm.module.oa.service.workReport;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportCreateReqVO;
import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import com.meession.etm.module.oa.dal.mysql.OaWorkReportMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_WORK_REPORT_NOT_EXISTS;

@Service
@Validated
public class OaWorkReportServiceImpl implements OaWorkReportService {

    @Resource
    private OaWorkReportMapper workReportMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createWorkReport(Long userId, OaWorkReportCreateReqVO createReqVO) {
        OaWorkReportDO report = BeanUtils.toBean(createReqVO, OaWorkReportDO.class)
                .setUserId(userId).setStatus(0);
        workReportMapper.insert(report);
        return report.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWorkReport(Long id, OaWorkReportCreateReqVO updateReqVO) {
        validateExists(id);
        OaWorkReportDO report = BeanUtils.toBean(updateReqVO, OaWorkReportDO.class).setId(id);
        workReportMapper.updateById(report);
    }

    @Override
    public void deleteWorkReport(Long id) {
        validateExists(id);
        workReportMapper.deleteById(id);
    }

    @Override
    public OaWorkReportDO getWorkReport(Long id) {
        return workReportMapper.selectById(id);
    }

    @Override
    public PageResult<OaWorkReportDO> getWorkReportPage(Long userId, OaWorkReportPageReqVO pageReqVO) {
        return workReportMapper.selectPage(userId, pageReqVO);
    }

    @Override
    public void submitWorkReport(Long id) {
        validateExists(id);
        workReportMapper.updateById(new OaWorkReportDO().setId(id).setStatus(1));
    }

    @Override
    public void reviewWorkReport(Long id, Long reviewerUserId, String reviewContent) {
        validateExists(id);
        workReportMapper.updateById(new OaWorkReportDO().setId(id).setStatus(2)
                .setReviewerUserId(reviewerUserId).setReviewTime(LocalDateTime.now())
                .setReviewContent(reviewContent));
    }

    private void validateExists(Long id) {
        if (workReportMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_WORK_REPORT_NOT_EXISTS);
        }
    }

}