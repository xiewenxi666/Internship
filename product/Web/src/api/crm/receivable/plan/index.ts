import request from '@/config/axios'

export interface ReceivablePlanVO {
  id: number
  period: number
  receivableId: number
  price: number
  returnTime: Date
  remindDays: number
  returnType: number
  remindTime: Date
  customerId: number
  customerName?: string
  contractId?: number
  contractNo?: string
  ownerUserId: number
  ownerUserName?: string
  remark: string
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
  status: number
  statusName?: string
  overdueDays?: number
  percent?: number
  receivable?: {
    price: number
    returnTime: Date
  }
}

export interface ReceivablePlanSummaryVO {
  month: string
  targetPrice: number
  completedPrice: number
  completionRate: number
  uncompletedPrice: number
  invoicedPrice: number
}

// 查询回款计划列表
export const getReceivablePlanPage = async (params) => {
  return await request.get({ url: `/crm/receivable-plan/page`, params })
}

// 查询回款计划列表
export const getReceivablePlanPageByCustomer = async (params) => {
  return await request.get({ url: `/crm/receivable-plan/page-by-customer`, params })
}

// 查询回款计划详情
export const getReceivablePlan = async (id: number) => {
  return await request.get({ url: `/crm/receivable-plan/get?id=` + id })
}

// 查询回款计划下拉数据
export const getReceivablePlanSimpleList = async (customerId: number, contractId: number) => {
  return await request.get({
    url: `/crm/receivable-plan/simple-list?customerId=${customerId}&contractId=${contractId}`
  })
}

// 新增回款计划
export const createReceivablePlan = async (data: ReceivablePlanVO) => {
  return await request.post({ url: `/crm/receivable-plan/create`, data })
}

// 修改回款计划
export const updateReceivablePlan = async (data: ReceivablePlanVO) => {
  return await request.put({ url: `/crm/receivable-plan/update`, data })
}

// 删除回款计划
export const deleteReceivablePlan = async (id: number) => {
  return await request.delete({ url: `/crm/receivable-plan/delete?id=` + id })
}

// 导出回款计划 Excel
export const exportReceivablePlan = async (params) => {
  return await request.download({ url: `/crm/receivable-plan/export-excel`, params })
}

// 获得待回款提醒数量
export const getReceivablePlanRemindCount = async () => {
  return await request.get({ url: '/crm/receivable-plan/remind-count' })
}

// 批量创建多期回款计划
export const batchCreateReceivablePlan = async (data) => {
  return await request.post({ url: '/crm/receivable-plan/batch-create', data })
}

// 获得回款计划汇总统计
export const getReceivablePlanSummary = async (params) => {
  return await request.get({ url: '/crm/receivable-plan/summary', params })
}

// 获得回款计划报表
export const getReceivablePlanReport = async (params) => {
  return await request.get({ url: '/crm/receivable-plan/report', params })
}
