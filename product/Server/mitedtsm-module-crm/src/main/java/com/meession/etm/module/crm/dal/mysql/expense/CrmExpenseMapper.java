package com.meession.etm.module.crm.dal.mysql.expense;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.meession.etm.module.crm.controller.admin.expense.vo.expense.CrmExpensePageReqVO;
import com.meession.etm.module.crm.dal.dataobject.expense.CrmExpenseDO;
import com.meession.etm.module.crm.enums.common.CrmBizTypeEnum;
import com.meession.etm.module.crm.util.CrmPermissionUtils;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CrmExpenseMapper extends BaseMapperX<CrmExpenseDO> {

    default CrmExpenseDO selectByNo(String no) {
        return selectOne(CrmExpenseDO::getNo, no);
    }

    default PageResult<CrmExpenseDO> selectPage(CrmExpensePageReqVO pageReqVO, Long userId) {
        MPJLambdaWrapperX<CrmExpenseDO> query = new MPJLambdaWrapperX<>();
        CrmPermissionUtils.appendPermissionCondition(query, CrmBizTypeEnum.CRM_EXPENSE.getType(),
                CrmExpenseDO::getId, userId, pageReqVO.getSceneType());
        query.selectAll(CrmExpenseDO.class)
                .eqIfPresent(CrmExpenseDO::getNo, pageReqVO.getNo())
                .eqIfPresent(CrmExpenseDO::getType, pageReqVO.getType())
                .eqIfPresent(CrmExpenseDO::getCustomerId, pageReqVO.getCustomerId())
                .betweenIfPresent(CrmExpenseDO::getApplyDate, pageReqVO.getApplyDateStart(), pageReqVO.getApplyDateEnd())
                .eqIfPresent(CrmExpenseDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .orderByDesc(CrmExpenseDO::getId);
        return selectJoinPage(pageReqVO, CrmExpenseDO.class, query);
    }

}
