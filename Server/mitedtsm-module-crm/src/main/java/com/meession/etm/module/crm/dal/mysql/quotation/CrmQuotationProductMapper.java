package com.meession.etm.module.crm.dal.mysql.quotation;

import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.module.crm.dal.dataobject.quotation.CrmQuotationProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * CRM 报价单明细 Mapper
 *
 * @author engineer
 */
@Mapper
public interface CrmQuotationProductMapper extends BaseMapperX<CrmQuotationProductDO> {

    default List<CrmQuotationProductDO> selectListByQuotationId(Long quotationId) {
        return selectList(CrmQuotationProductDO::getQuotationId, quotationId);
    }

    default int deleteByQuotationId(Long quotationId) {
        return delete(CrmQuotationProductDO::getQuotationId, quotationId);
    }

}
