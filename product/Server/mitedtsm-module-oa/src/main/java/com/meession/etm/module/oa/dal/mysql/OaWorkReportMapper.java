package com.meession.etm.module.oa.dal.mysql;

import com.meession.etm.module.oa.controller.admin.workReport.vo.OaWorkReportPageReqVO;
import com.meession.etm.module.oa.dal.dataobject.OaWorkReportDO;
import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OaWorkReportMapper extends BaseMapperX<OaWorkReportDO> {

    default PageResult<OaWorkReportDO> selectPage(Long userId, OaWorkReportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OaWorkReportDO>()
                .eqIfPresent(OaWorkReportDO::getUserId, userId)
                .eqIfPresent(OaWorkReportDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OaWorkReportDO::getType, reqVO.getType())
                .orderByDesc(OaWorkReportDO::getId));
    }

}