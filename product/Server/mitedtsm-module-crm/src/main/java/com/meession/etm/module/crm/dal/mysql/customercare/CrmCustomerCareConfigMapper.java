package com.meession.etm.module.crm.dal.mysql.customercare;

import com.meession.etm.framework.mybatis.core.mapper.BaseMapperX;
import com.meession.etm.framework.mybatis.core.query.QueryWrapperX;
import com.meession.etm.module.crm.dal.dataobject.customercare.CrmCustomerCareConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 客户关怀配置 Mapper
 *
 * @author 密讯
 */
@Mapper
public interface CrmCustomerCareConfigMapper extends BaseMapperX<CrmCustomerCareConfigDO> {

    default CrmCustomerCareConfigDO selectOne() {
        return selectOne(new QueryWrapperX<CrmCustomerCareConfigDO>().limitN(1));
    }

}
