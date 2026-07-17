package com.meession.etm.module.oa.dal.mapper;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.oa.controller.admin.oa.vo.report.OaReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaReportDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaReportMapper extends BaseMapperX<OaReportDO> {

    default PageResult<OaReportDO> selectPage(Long userId, OaReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaReportDO>()
                .eqIfPresent(OaReportDO::getUserId, userId)
                .eqIfPresent(OaReportDO::getType, reqVO.getType())
                .betweenIfPresent(OaReportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OaReportDO::getId));
    }

}
