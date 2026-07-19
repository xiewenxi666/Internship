package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.schedule.vo.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OaScheduleMapper extends BaseMapperX<OaScheduleDO> {

    default PageResult<OaScheduleDO> selectPage(Long userId, OaSchedulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaScheduleDO>()
                .eqIfPresent(OaScheduleDO::getUserId, userId)
                .eqIfPresent(OaScheduleDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaScheduleDO::getType, reqVO.getType())
                .orderByDesc(OaScheduleDO::getStartTime));
    }

    default List<OaScheduleDO> selectCalendarList(Long userId, LocalDateTime startTime, LocalDateTime endTime,
                                                   Integer type) {
        return selectList(new LambdaQueryWrapperX<OaScheduleDO>()
                .eq(OaScheduleDO::getUserId, userId)
                .eqIfPresent(OaScheduleDO::getType, type)
                .and(w -> w.le(OaScheduleDO::getStartTime, endTime)
                        .ge(OaScheduleDO::getEndTime, startTime))
                .orderByAsc(OaScheduleDO::getStartTime));
    }

}