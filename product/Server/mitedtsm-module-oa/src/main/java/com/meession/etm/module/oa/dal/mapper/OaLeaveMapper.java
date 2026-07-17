package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.leave.OaLeavePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLeaveDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaLeaveMapper extends BaseMapperX<OaLeaveDO> {

    default PageResult<OaLeaveDO> selectPage(Long userId, OaLeavePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaLeaveDO>()
                .eqIfPresent(OaLeaveDO::getUserId, userId)
                .eqIfPresent(OaLeaveDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaLeaveDO::getType, reqVO.getType())
                .likeIfPresent(OaLeaveDO::getReason, reqVO.getReason())
                .betweenIfPresent(OaLeaveDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaLeaveDO::getId));
    }

}
