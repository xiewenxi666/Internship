package com.meession.etm.module.oa.service.schedule;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.schedule.vo.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

public interface OaScheduleService {

    Long createSchedule(Long userId, @Valid OaScheduleCreateReqVO createReqVO);

    void updateSchedule(Long id, @Valid OaScheduleCreateReqVO updateReqVO);

    void deleteSchedule(Long id);

    OaScheduleDO getSchedule(Long id);

    PageResult<OaScheduleDO> getSchedulePage(Long userId, OaSchedulePageReqVO pageReqVO);

    void updateScheduleStatus(Long id, Integer status);

    List<OaScheduleDO> getScheduleCalendarList(Long userId, LocalDateTime startTime, LocalDateTime endTime,
                                                Integer type);

}