package com.meession.etm.module.crm.dal.mysql.refund;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.refund.vo.refund.CrmRefundPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.refund.CrmRefundDO;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import com.meession.etm.module.crm.util.CrmPermissionUtils;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 退款 Mapper
 *
 * @author 赤焰
 */
@Mapper
public interface CrmRefundMapper extends BaseMapperX<CrmRefundDO> {

    default CrmRefundDO selectByNo(String no) {
        return selectOne(CrmRefundDO::getNo, no);
    }

    default PageResult<CrmRefundDO> selectPageByCustomerId(CrmRefundPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmRefundDO>()
                .eq(CrmRefundDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CrmRefundDO::getNo, reqVO.getNo())
                .eqIfPresent(CrmRefundDO::getContractId, reqVO.getContractId())
                .orderByDesc(CrmRefundDO::getId));
    }

    default PageResult<CrmRefundDO> selectPage(CrmRefundPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmRefundDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_REFUND.getType(),
                CrmRefundDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmRefundDO.class)
                .eqIfPresent(CrmRefundDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmRefundDO::getContractId, pageReqVO.getContractId())
                .eqIfPresent(CrmRefundDO::getAuditStatus, pageReqVO.getAuditStatus())
                .orderByDesc(CrmRefundDO::getId);
        return selectJoinPage(pageReqVO, CrmRefundDO.class, query);
    }

    default Long selectCountByAudit(Long userId) {
        MPJLambdaWrapperX<CrmRefundDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_REFUND.getType(),
                CrmRefundDO::getId, userId, CrmSceneTypeEnum.OWNER.getType());
        query.eq(CrmRefundDO::getAuditStatus, CrmAuditStatusEnum.PROCESS.getStatus());
        return selectCount(query);
    }

    default List<CrmRefundDO> selectListForReport(Integer year, Long ownerUserId) {
        LambdaQueryWrapperX<CrmRefundDO> query = new LambdaQueryWrapperX<CrmRefundDO>()
                .orderByDesc(CrmRefundDO::getId);
        if (ownerUserId != null) {
            query.eq(CrmRefundDO::getOwnerUserId, ownerUserId);
        }
        if (year != null) {
            query.apply("YEAR(refund_date) = {0}", year);
        }
        return selectList(query);
    }

    default PageResult<CrmRefundDO> selectPageForApproval(CrmRefundApprovalPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmRefundDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_REFUND.getType(),
                CrmRefundDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmRefundDO.class)
                .eqIfPresent(CrmRefundDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmRefundDO::getAuditStatus, pageReqVO.getAuditStatus())
                .orderByDesc(CrmRefundDO::getId);
        return selectJoinPage(pageReqVO, CrmRefundDO.class, query);
    }

}
