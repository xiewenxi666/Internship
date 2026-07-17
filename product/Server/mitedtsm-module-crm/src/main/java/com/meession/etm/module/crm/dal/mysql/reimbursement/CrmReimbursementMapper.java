package com.meession.etm.module.crm.dal.mysql.reimbursement;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementApprovalPageReqVO;
import com.meession.etm.module.crm.controller.admin.reimbursement.vo.reimbursement.CrmReimbursementPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.reimbursement.CrmReimbursementDO;
import com.meession.etm.module.crm.enums.common.CrmAuditStatusEnum;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.enums.common.CrmSceneTypeEnum;
import com.meession.etm.module.crm.util.CrmPermissionUtils;
import org.apache.ibatis.annotations.Mapper;

/**
 * 报销 Mapper
 *
 * @author 赤焰
 */
@Mapper
public interface CrmReimbursementMapper extends BaseMapperX<CrmReimbursementDO> {

    default CrmReimbursementDO selectByNo(String no) {
        return selectOne(CrmReimbursementDO::getNo, no);
    }

    default PageResult<CrmReimbursementDO> selectPageByCustomerId(CrmReimbursementPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CrmReimbursementDO>()
                .eq(CrmReimbursementDO::getCustomerId, reqVO.getCustomerId())
                .eqIfPresent(CrmReimbursementDO::getNo, reqVO.getNo())
                .eqIfPresent(CrmReimbursementDO::getContractId, reqVO.getContractId())
                .orderByDesc(CrmReimbursementDO::getId));
    }

    default PageResult<CrmReimbursementDO> selectPage(CrmReimbursementPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmReimbursementDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_REIMBURSEMENT.getType(),
                CrmReimbursementDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmReimbursementDO.class)
                .eqIfPresent(CrmReimbursementDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmReimbursementDO::getContractId, pageReqVO.getContractId())
                .eqIfPresent(CrmReimbursementDO::getAuditStatus, pageReqVO.getAuditStatus())
                .eqIfPresent(CrmReimbursementDO::getType, pageReqVO.getType())
                .orderByDesc(CrmReimbursementDO::getId);
        return selectJoinPage(pageReqVO, CrmReimbursementDO.class, query);
    }

    default Long selectCountByAudit(Long userId) {
        MPJLambdaWrapperX<CrmReimbursementDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_REIMBURSEMENT.getType(),
                CrmReimbursementDO::getId, userId, CrmSceneTypeEnum.OWNER.getType());
        query.eq(CrmReimbursementDO::getAuditStatus, CrmAuditStatusEnum.PROCESS.getStatus());
        return selectCount(query);
    }

    default PageResult<CrmReimbursementDO> selectPageForApproval(CrmReimbursementApprovalPageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmReimbursementDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_REIMBURSEMENT.getType(),
                CrmReimbursementDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmReimbursementDO.class)
                .eqIfPresent(CrmReimbursementDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmReimbursementDO::getAuditStatus, pageReqVO.getAuditStatus())
                .orderByDesc(CrmReimbursementDO::getId);
        return selectJoinPage(pageReqVO, CrmReimbursementDO.class, query);
    }

}
