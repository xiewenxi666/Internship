package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.businessTrip.vo.OaBusinessTripPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaBusinessTripDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaBusinessTripMapper extends BaseMapperX<OaBusinessTripDO> {

    default PageResult<OaBusinessTripDO> selectPage(Long userId, OaBusinessTripPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaBusinessTripDO>()
                .eqIfPresent(OaBusinessTripDO::getUserId, userId)
                .eqIfPresent(OaBusinessTripDO::getStatus, reqVO.getStatus())
                .likeIfPresent(OaBusinessTripDO::getDestination, reqVO.getDestination())
                .betweenIfPresent(OaBusinessTripDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaBusinessTripDO::getId));
    }

}