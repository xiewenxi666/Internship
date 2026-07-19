package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.message.vo.OaMessagePageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaMessageDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaMessageMapper extends BaseMapperX<OaMessageDO> {

    default PageResult<OaMessageDO> selectPage(Long userId, OaMessagePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaMessageDO>()
                .eq(OaMessageDO::getReceiverUserId, userId)
                .eqIfPresent(OaMessageDO::getReadStatus, reqVO.getReadStatus())
                .orderByDesc(OaMessageDO::getId));
    }

}