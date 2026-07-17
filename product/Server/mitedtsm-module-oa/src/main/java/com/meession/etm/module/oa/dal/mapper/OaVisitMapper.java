package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.visit.OaVisitPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaVisitDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaVisitMapper extends BaseMapperX<OaVisitDO> {

    default PageResult<OaVisitDO> selectPage(Long userId, OaVisitPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaVisitDO>()
                .eqIfPresent(OaVisitDO::getUserId, userId)
                .eqIfPresent(OaVisitDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaVisitDO::getCustomerId, reqVO.getCustomerId())
                .betweenIfPresent(OaVisitDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaVisitDO::getId));
    }

}
