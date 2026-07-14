package com.meession.etm.module.crm.dal.mysql.campaign;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.campaign.vo.CrmCampaignPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.campaign.CrmCampaignDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmCampaignMapper extends BaseMapperX<CrmCampaignDO> {

    default PageResult<CrmCampaignDO> selectPage(CrmCampaignPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmCampaignDO>()
                .likeIfPresent(CrmCampaignDO::getTitle, reqVO.getTitle())
                .eqIfPresent(CrmCampaignDO::getType, reqVO.getType())
                .eqIfPresent(CrmCampaignDO::getStatus, reqVO.getStatus())
                .orderByDesc(CrmCampaignDO::getId));
    }
}
