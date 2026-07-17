package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.task.OaTaskPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTaskDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaTaskMapper extends BaseMapperX<OaTaskDO> {

    default PageResult<OaTaskDO> selectPage(Long userId, OaTaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaTaskDO>()
                .eqIfPresent(OaTaskDO::getUserId, userId)
                .eqIfPresent(OaTaskDO::getAssigneeId, reqVO.getAssigneeId())
                .eqIfPresent(OaTaskDO::getStatus, reqVO.getStatus())
                .likeIfPresent(OaTaskDO::getTitle, reqVO.getTitle())
                .orderByDesc(OaTaskDO::getId));
    }

}
