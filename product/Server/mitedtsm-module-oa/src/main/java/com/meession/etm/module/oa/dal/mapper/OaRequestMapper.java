package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.request.OaRequestPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaRequestDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaRequestMapper extends BaseMapperX<OaRequestDO> {

    default PageResult<OaRequestDO> selectPage(Long userId, OaRequestPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaRequestDO>()
                .eqIfPresent(OaRequestDO::getUserId, userId)
                .eqIfPresent(OaRequestDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(OaRequestDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaRequestDO::getId));
    }

}
