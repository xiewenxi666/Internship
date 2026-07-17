package com.meession.etm.module.oa.service.schedule;

import com.meession.etm.framework.common.exception.util.ServiceExceptionUtil;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.common.util.object.BeanUtils;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.module.oa.dal.mapper.OaScheduleMapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;

import static com.meession.etm.module.oa.enums.ErrorCodeConstants.OA_SCHEDULE_NOT_EXISTS;

@Slf4j
@Service
@Validated
public class OaScheduleServiceImpl implements OaScheduleService {

    @Resource
    private OaScheduleMapper scheduleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createSchedule(Long userId, OaScheduleCreateReqVO createReqVO) {
        OaScheduleDO schedule = BeanUtils.toBean(createReqVO, OaScheduleDO.class)
                .setUserId(userId);
        scheduleMapper.insert(schedule);
        return schedule.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateSchedule(OaScheduleDO schedule) {
        validateScheduleExists(schedule.getId());
        scheduleMapper.updateById(schedule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchedule(Long id) {
        validateScheduleExists(id);
        scheduleMapper.deleteById(id);
    }

    private void validateScheduleExists(Long id) {
        if (scheduleMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(OA_SCHEDULE_NOT_EXISTS);
        }
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
    public List<OaScheduleDO> getScheduleListByDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime) {
        return scheduleMapper.selectListByDateRange(userId, startTime, endTime);
    }

}
