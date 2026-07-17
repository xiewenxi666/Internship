import request from '@/config/axios'

/**
 * 获取客户关联的所有 CRM 数据
 * @param customerId 客户ID
 */
export function getCustomerRelation(customerId: number) {
  return request.get({ url: `/crm/relation/customer/${customerId}` })
}
