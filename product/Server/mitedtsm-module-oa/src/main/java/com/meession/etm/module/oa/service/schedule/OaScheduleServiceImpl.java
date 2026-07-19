package com.meession.etm.module.oa.service.schedule;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.dal.mysql.OaScheduleMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_SCHEDULE_NOT_EXISTS;

@Service
@Validated
public class OaScheduleServiceImpl implements OaScheduleService {

    @Resource
    private OaScheduleMapper scheduleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSchedule(Long userId, OaScheduleCreateReqVO createReqVO) {
        OaScheduleDO schedule = BeanUtils.toBean(createReqVO, OaScheduleDO.class)
                .setUserId(userId).setStatus(0);
        scheduleMapper.insert(schedule);
        return schedule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSchedule(Long id, OaScheduleCreateReqVO updateReqVO) {
        validateExists(id);
        OaScheduleDO schedule = BeanUtils.toBean(updateReqVO, OaScheduleDO.class).setId(id);
        scheduleMapper.updateById(schedule);
    }

    @Override
    public void deleteSchedule(Long id) {
        validateExists(id);
        scheduleMapper.deleteById(id);
    }

    @Override
    public OaScheduleDO getSchedule(Long id) {
        return scheduleMapper.selectById(id);
    }

    @Override
    public PageResult<OaScheduleDO> getSchedulePage(Long userId, OaSchedulePageReqVO pageReqVO) {
        return scheduleMapper.selectPage(userId, pageReqVO);
    }

    @Override
    public void updateScheduleStatus(Long id, Integer status) {
        validateExists(id);
        scheduleMapper.updateById(new OaScheduleDO().setId(id).setStatus(status));
    }

    @Override
    public List<OaScheduleDO> getScheduleCalendarList(Long userId, LocalDateTime startTime, LocalDateTime endTime,
                                                       Integer type) {
        return scheduleMapper.selectCalendarList(userId, startTime, endTime, type);
    }

    private void validateExists(Long id) {
        if (scheduleMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_SCHEDULE_NOT_EXISTS);
        }
    }

}