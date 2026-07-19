package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.request.vo.OaRequestPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaRequestMapper extends BaseMapperX<OaRequestDO> {

    default PageResult<OaRequestDO> selectPage(Long userId, OaRequestPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaRequestDO>()
                .eqIfPresent(OaRequestDO::getUserId, userId)
                .eqIfPresent(OaRequestDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaRequestDO::getType, reqVO.getType())
                .eqIfPresent(OaRequestDO::getUrgency, reqVO.getUrgency())
                .orderByDesc(OaRequestDO::getId));
    }

}