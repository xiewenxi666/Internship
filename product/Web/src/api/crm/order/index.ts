import request from '@/config/axios'
import { TransferReqVO } from '@/api/crm/permission'

/**
 * 订单 API
 */
export interface OrderVO {
  id: number
  no: string
  name: string
  customerId: number
  customerName?: string
  businessId: number
  businessName?: string
  ownerUserId: number
  ownerUserName?: string
  ownerUserDeptName?: string
  processInstanceId: string
  status: number
  orderDate: string
  totalProductPrice: number
  discountPercent: number
  totalPrice: number
  remark: string
  contactLastTime: string
  contactNextTime?: string
  createTime?: string
  creator: string
  creatorName?: string
  updateTime?: string
  products?: [
    {
      id: number
      productId: number
      productName: string
      productNo: string
      productUnit: number
      productPrice: number
      orderPrice: number
      count: number
      totalPrice: number
    }
  ]
}

export const getOrderPage = async (params) => {
  return await request.get({ url: `/crm/order/page`, params })
}

export const getOrderPageByCustomer = async (params: any) => {
  return await request.get({ url: `/crm/order/page-by-customer`, params })
}

export const getOrderPageByBusiness = async (params: any) => {
  return await request.get({ url: `/crm/order/page-by-business`, params })
}

export const getOrder = async (id: number) => {
  return await request.get({ url: `/crm/order/get?id=` + id })
}

export const getOrderSimpleList = async (customerId: number) => {
  return await request.get({
    url: `/crm/order/simple-list?customerId=${customerId}`
  })
}

export const createOrder = async (data: OrderVO) => {
  return await request.post({ url: `/crm/order/create`, data })
}

export const updateOrder = async (data: OrderVO) => {
  return await request.put({ url: `/crm/order/update`, data })
}

export const deleteOrder = async (id: number) => {
  return await request.delete({ url: `/crm/order/delete?id=` + id })
}

export const exportOrder = async (params) => {
  return await request.download({ url: `/crm/order/export-excel`, params })
}

export const submitOrder = async (id: number) => {
  return await request.put({ url: `/crm/order/submit?id=${id}` })
}

export const transferOrder = async (data: TransferReqVO) => {
  return await request.put({ url: '/crm/order/transfer', data })
}

export const updateOrderStatus = async (data: any) => {
  return await request.put({ url: '/crm/order/update-status', data })
}

export const getAuditOrderCount = async () => {
  return await request.get({ url: '/crm/order/audit-count' })
}
