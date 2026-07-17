package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.schedule.OaSchedulePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaScheduleDO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OaScheduleMapper extends BaseMapperX<OaScheduleDO> {

    default PageResult<OaScheduleDO> selectPage(Long userId, OaSchedulePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaScheduleDO>()
                .eqIfPresent(OaScheduleDO::getUserId, userId)
                .likeIfPresent(OaScheduleDO::getTitle, reqVO.getTitle())
                .geIfPresent(OaScheduleDO::getStartTime, reqVO.getStartTime())
                .leIfPresent(OaScheduleDO::getEndTime, reqVO.getEndTime())
                .orderByAsc(OaScheduleDO::getStartTime));
    }

    default List<OaScheduleDO> selectListByDateRange(Long userId, LocalDateTime start, LocalDateTime end) {
        return selectList(new LambdaQueryWrapperX<OaScheduleDO>()
                .eq(OaScheduleDO::getUserId, userId)
                .le(OaScheduleDO::getStartTime, end)
                .ge(OaScheduleDO::getEndTime, start)
                .orderByAsc(OaScheduleDO::getStartTime));
    }

}
