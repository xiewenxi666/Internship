package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.task.vo.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OaTaskMapper extends BaseMapperX<OaTaskDO> {

    default PageResult<OaTaskDO> selectPage(Long userId, OaTaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaTaskDO>()
                .eq(OaTaskDO::getUserId, userId)
                .eqIfPresent(OaTaskDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaTaskDO::getPriority, reqVO.getPriority())
                .eqIfPresent(OaTaskDO::getAssigneeUserId, reqVO.getAssigneeUserId())
                .likeIfPresent(OaTaskDO::getTitle, reqVO.getTitle())
                .orderByDesc(OaTaskDO::getPriority).orderByAsc(OaTaskDO::getDeadline));
    }

    default List<OaTaskDO> selectListByAssignee(Long assigneeUserId, Integer status) {
        return selectList(new LambdaQueryWrapperX<OaTaskDO>()
                .eq(OaTaskDO::getAssigneeUserId, assigneeUserId)
                .eqIfPresent(OaTaskDO::getStatus, status)
                .orderByDesc(OaTaskDO::getPriority).orderByAsc(OaTaskDO::getDeadline));
    }

}