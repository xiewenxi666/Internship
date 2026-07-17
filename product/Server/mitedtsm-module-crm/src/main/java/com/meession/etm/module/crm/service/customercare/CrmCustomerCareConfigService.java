package com.meession.etm.module.crm.service.customercare;

import com.meession.etm.module.crm.controller.admin.customercare.vo.CrmCustomerCareConfigSaveReqVO;
import com.meession.etm.module.crm.dal.dataobject.customercare.CrmCustomerCareConfigDO;
import jakarta.validation.Valid;

/**
 * 客户关怀配置 Service 接口
 *
 * @author 密讯
 */
public interface CrmCustomerCareConfigService {

    /**
     * 获得客户关怀配置
     *
     * @return 客户关怀配置
     */
    CrmCustomerCareConfigDO getCustomerCareConfig();

    /**
     * 保存客户关怀配置
     *
     * @param saveReqVO 保存信息
     */
    void saveCustomerCareConfig(@Valid CrmCustomerCareConfigSaveReqVO saveReqVO);

}
