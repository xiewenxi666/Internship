// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.dal.mysql.workorder;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.workorder.vo.CrmWorkOrderPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.workorder.CrmWorkOrderDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmWorkOrderMapper extends BaseMapperX<CrmWorkOrderDO> {

    default PageResult<CrmWorkOrderDO> selectPage(CrmWorkOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmWorkOrderDO>()
                .eqIfPresent(CrmWorkOrderDO::getType, reqVO.getType())
                .eqIfPresent(CrmWorkOrderDO::getPriority, reqVO.getPriority())
                .eqIfPresent(CrmWorkOrderDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CrmWorkOrderDO::getContractId, reqVO.getContractId())
                .eqIfPresent(CrmWorkOrderDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CrmWorkOrderDO::getOwnerUserId, reqVO.getOwnerUserId())
                .likeIfPresent(CrmWorkOrderDO::getTitle, reqVO.getTitle())
                .likeIfPresent(CrmWorkOrderDO::getNo, reqVO.getNo())
                .betweenIfPresent(CrmWorkOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CrmWorkOrderDO::getId));
    }

    default PageResult<CrmWorkOrderDO> selectPageByCustomer(CrmWorkOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmWorkOrderDO>()
                .eq(CrmWorkOrderDO::getCustomerId, reqVO.getCustomerId())
                .orderByDesc(CrmWorkOrderDO::getId));
    }
}
