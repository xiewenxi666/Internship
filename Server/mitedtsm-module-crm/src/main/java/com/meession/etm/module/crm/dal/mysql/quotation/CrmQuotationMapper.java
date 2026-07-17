package com.meession.etm.module.crm.dal.mysql.quotation;

import com.meession.etm.framework.common.pojo.PageResult;
import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.meession.etm.module.crm.controller.admin.quotation.vo.quotation.CrmQuotationPageReqVO;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * CRM 报价单 Mapper
 *
 * @author engineer
 */
@Mapper
public interface CrmQuotationMapper extends BaseMapperX<CrmQuotationDO> {

    default PageResult<CrmQuotationDO> selectPage(CrmQuotationPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<CrmQuotationDO>()
                .eqIfPresent(CrmQuotationDO::getOwnerUserId, pageReqVO.getOwnerUserId())
                .eqIfPresent(CrmQuotationDO::getCustomerId, pageReqVO.getCustomerId())
                .eqIfPresent(CrmQuotationDO::getBusinessId, pageReqVO.getBusinessId())
                .eqIfPresent(CrmQuotationDO::getStatus, pageReqVO.getStatus())
                .likeIfPresent(CrmQuotationDO::getQuotationNo, pageReqVO.getQuotationNo())
                .geIfPresent(CrmQuotationDO::getCreateTime, pageReqVO.getCreateTimeStart())
                .leIfPresent(CrmQuotationDO::getCreateTime, pageReqVO.getCreateTimeEnd())
                .orderByDesc(CrmQuotationDO::getId));
    }

    default CrmQuotationDO selectByQuotationNo(String quotationNo) {
        return selectOne(CrmQuotationDO::getQuotationNo, quotationNo);
    }

    default Long selectCountByBusinessId(Long businessId) {
        return selectCount(CrmQuotationDO::getBusinessId, businessId);
    }

    default Long selectCountByCustomerId(Long customerId) {
        return selectCount(CrmQuotationDO::getCustomerId, customerId);
    }

}
