package com.meession.etm.module.crm.dal.mysql.bulksend;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.bulksend.vo.CrmBulkSendPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.bulksend.CrmBulkSendDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmBulkSendMapper extends BaseMapperX<CrmBulkSendDO> {

    default PageResult<CrmBulkSendDO> selectPage(CrmBulkSendPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmBulkSendDO>()
                .likeIfPresent(CrmBulkSendDO::getTitle, reqVO.getTitle())
                .eqIfPresent(CrmBulkSendDO::getType, reqVO.getType())
                .eqIfPresent(CrmBulkSendDO::getStatus, reqVO.getStatus())
                .orderByDesc(CrmBulkSendDO::getId));
    }
}
