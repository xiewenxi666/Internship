package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.trip.OaTripPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaTripDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaTripMapper extends BaseMapperX<OaTripDO> {

    default PageResult<OaTripDO> selectPage(Long userId, OaTripPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaTripDO>()
                .eqIfPresent(OaTripDO::getUserId, userId)
                .eqIfPresent(OaTripDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaTripDO::getType, reqVO.getType())
                .likeIfPresent(OaTripDO::getDestination, reqVO.getDestination())
                .betweenIfPresent(OaTripDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaTripDO::getId));
    }

}
