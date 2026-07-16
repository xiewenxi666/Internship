// -23计算机科学与技术2班-龚小波
package com.meession.etm.module.crm.service.common;

import com.meession.etm.module.crm.controller.admin.common.vo.CustomerRelationRespVO;

/**
 * CRM 关联查询服务接口
 *
 * @author 密讯
 */
public interface CrmRelationService {

    /**
     * 根据客户 ID 获取关联的商机、合同、回款信息
     *
     * @param customerId 客户 ID
     * @return 关联数据
     */
    CustomerRelationRespVO getCustomerRelation(Long customerId);

}
