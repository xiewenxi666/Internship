import request from '@/config/axios'

export type LoanVO = {
  id: number
  status: number
  purpose: number
  amount: number
  expectedRepayTime: string
  reason: string
  processInstanceId: string
  createTime: string
}

export const createLoan = async (data: LoanVO) => {
  return await request.post({ url: '/oa/loan/create', data })
}

export const getLoan = async (id: number) => {
  return await request.get({ url: '/oa/loan/get?id=' + id })
}

export const getLoanPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/loan/page', params })
}
