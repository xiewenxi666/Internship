// -23计算机科学与技术2班-龚小波
import request from '@/config/axios'

export interface WorkOrderVO {
  id: number
  no: string
  title: string
  type: number
  priority: number
  status: number
  contractId: number
  contractName?: string
  customerId: number
  customerName?: string
  ownerUserId: number
  ownerUserName?: string
  description: string
  solution: string
  startTime: Date
  endTime: Date
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

// 获取工单分页
export const getWorkOrderPage = async (params) => {
  return await request.get({ url: '/crm/work-order/page', params })
}

// 获取工单分页（基于客户）
export const getWorkOrderPageByCustomer = async (params) => {
  return await request.get({ url: '/crm/work-order/page-by-customer', params })
}

// 获取工单详情
export const getWorkOrder = async (id: number) => {
  return await request.get({ url: '/crm/work-order/get?id=' + id })
}

// 创建工单
export const createWorkOrder = async (data: WorkOrderVO) => {
  return await request.post({ url: '/crm/work-order/create', data })
}

// 更新工单
export const updateWorkOrder = async (data: WorkOrderVO) => {
  return await request.put({ url: '/crm/work-order/update', data })
}

// 删除工单
export const deleteWorkOrder = async (id: number) => {
  return await request.delete({ url: '/crm/work-order/delete?id=' + id })
}

// 处理工单
export const processWorkOrder = async (id: number) => {
  return await request.put({ url: '/crm/work-order/process?id=' + id })
}

// 工单完结
export const completeWorkOrder = async (id: number, solution: string) => {
  return await request.put({ url: '/crm/work-order/complete', params: { id, solution } })
}

// 工单退回
export const returnWorkOrder = async (id: number) => {
  return await request.put({ url: '/crm/work-order/return?id=' + id })
}

// 导出工单 Excel
export const exportWorkOrderExcel = async (params) => {
  return await request.download({ url: '/crm/work-order/export-excel', params })
}
