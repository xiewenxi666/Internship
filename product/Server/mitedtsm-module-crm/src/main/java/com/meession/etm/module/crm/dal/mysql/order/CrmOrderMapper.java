package com.meession.etm.module.crm.dal.mysql.order;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.order.vo.order.CrmOrderPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.order.CrmOrderDO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.util.CrmPermissionUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CRM 订单 Mapper
 *
 * @author 23计三倪雨晗
 */
@Mapper
public interface CrmOrderMapper extends BaseMapperX<CrmOrderDO> {

    default CrmOrderDO selectByNo(String no) {
        return selectOne(CrmOrderDO::getNo, no);
    }

    default PageResult<CrmOrderDO> selectPage(CrmOrderPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmOrderDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_ORDER.getType(),
                CrmOrderDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmOrderDO.class)
                .likeIfPresent(CrmOrderDO::getNo, pageReqVO.getNo())
                .likeIfPresent(CrmOrderDO::getName, pageReqVO.getName())
                .eqIfPresent(CrmOrderDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmOrderDO::getBusinessId, pageReqVO.getBusinessId())
                .eqIfPresent(CrmOrderDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(CrmOrderDO::getId);
        return selectJoinPage(pageReqVO, CrmOrderDO.class, query);
    }

    default PageResult<CrmOrderDO> selectPageByCustomerId(CrmOrderPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmOrderDO>()
                .eq(CrmOrderDO::getCustomerId, pageReqVO.getCustomerId())
                .likeIfPresent(CrmOrderDO::getNo, pageReqVO.getNo())
                .likeIfPresent(CrmOrderDO::getName, pageReqVO.getName())
                .orderByDesc(CrmOrderDO::getId));
    }

    default PageResult<CrmOrderDO> selectPageByBusinessId(CrmOrderPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmOrderDO>()
                .eq(CrmOrderDO::getBusinessId, pageReqVO.getBusinessId())
                .likeIfPresent(CrmOrderDO::getNo, pageReqVO.getNo())
                .likeIfPresent(CrmOrderDO::getName, pageReqVO.getName())
                .orderByDesc(CrmOrderDO::getId));
    }

    default Long selectCountByAudit(Long userId) {
        MPJLambdaWrapperX<CrmOrderDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_ORDER.getType(),
                CrmOrderDO::getId, userId, 1);
        query.eq(CrmOrderDO::getStatus, 15); // APPROVING
        return selectCount(query);
    }

    default List<CrmOrderDO> selectListByCustomerIdOwnerUserId(Long customerId, Long ownerUserId) {
        return selectList(new LambdaQueryWrapperX<CrmOrderDO>()
                .eq(CrmOrderDO::getCustomerId, customerId)
                .eq(CrmOrderDO::getOwnerUserId, ownerUserId));
    }

}
