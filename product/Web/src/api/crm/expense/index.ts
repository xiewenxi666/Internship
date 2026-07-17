import request from '@/config/axios'

export interface ExpenseVO {
  id: number
  no: string
  customerId?: number
  customerName?: string
  contractId?: number
  contract?: {
    id?: number
    name?: string
    no: string
  }
  ownerUserId?: number
  ownerUserName?: string
  content?: string
  price?: number
  type?: number
  applyDate?: Date
  remark?: string
  creator?: string
  creatorName?: string
  createTime?: Date
  updateTime?: Date
}

export const getExpensePage = async (params) => {
  return await request.get({ url: `/crm/expense/page`, params })
}

export const getExpense = async (id: number) => {
  return await request.get({ url: `/crm/expense/get?id=` + id })
}

export const createExpense = async (data: ExpenseVO) => {
  return await request.post({ url: `/crm/expense/create`, data })
}

export const updateExpense = async (data: ExpenseVO) => {
  return await request.put({ url: `/crm/expense/update`, data })
}

export const deleteExpense = async (id: number) => {
  return await request.delete({ url: `/crm/expense/delete?id=` + id })
}

export const exportExpense = async (params) => {
  return await request.download({ url: `/crm/expense/export-excel`, params })
}
