package com.meession.etm.module.oa.service.schedule;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaScheduleCreateReqVO;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;

import java.time.LocalDateTime;
import java.util.List;

public interface OaScheduleService {

    Long createSchedule(Long userId, OaScheduleCreateReqVO createReqVO);

    void updateSchedule(OaScheduleDO schedule);

    void deleteSchedule(Long id);

    OaScheduleDO getSchedule(Long id);

    PageResult<OaScheduleDO> getSchedulePage(Long userId, OaSchedulePageReqVO pageReqVO);

    List<OaScheduleDO> getScheduleListByDateRange(Long userId, LocalDateTime startTime, LocalDateTime endTime);

}
