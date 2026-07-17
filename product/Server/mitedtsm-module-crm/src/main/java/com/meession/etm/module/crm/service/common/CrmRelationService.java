package com.meession.etm.module.crm.service.common;

import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO;

/**
 * CRM 跨域关联查询 Service 接口
 *
 * @author 密讯
 */
public interface CrmRelationService {

    /**
     * 根据客户 ID 查询关联的所有 CRM 数据
     *
     * @param customerId 客户 ID
     * @return 关联数据
     */
    CustomerRelationRespVO getCustomerRelation(Long customerId);
}
