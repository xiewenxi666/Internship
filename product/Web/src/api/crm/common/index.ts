// -23计算机科学与技术2班-龚小波
import request from '@/config/axios'

// 获取客户关联数据（商机、合同、回款）
export const getCustomerRelation = async (customerId: number) => {
  return await request.get({ url: '/crm/relation/customer/' + customerId })
}
