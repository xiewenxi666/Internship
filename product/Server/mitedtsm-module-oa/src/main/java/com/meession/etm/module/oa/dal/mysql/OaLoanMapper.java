package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.loan.vo.OaLoanPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaLoanDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaLoanMapper extends BaseMapperX<OaLoanDO> {

    default PageResult<OaLoanDO> selectPage(Long userId, OaLoanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaLoanDO>()
                .eqIfPresent(OaLoanDO::getUserId, userId)
                .eqIfPresent(OaLoanDO::getStatus, reqVO.getStatus())
                .likeIfPresent(OaLoanDO::getPurpose, reqVO.getPurpose())
                .betweenIfPresent(OaLoanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaLoanDO::getId));
    }

}