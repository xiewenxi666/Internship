import request from '@/config/axios'

export interface LoanVO {
  id: number | undefined
  status: number
  amount: number
  purpose: string
  repaymentPlan: string
  expectedRepaymentTime: string
  processInstanceId: string
  startUserSelectAssignees?: Record<string, number[]>
  createTime: Date
}

export const getLoanPage = (params: PageParam) => {
  return request.get({ url: '/oa/loan/page', params })
}

export const getLoan = (id: number) => {
  return request.get({ url: '/oa/loan/get?id=' + id })
}

export const createLoan = (data: LoanVO) => {
  return request.post({ url: '/oa/loan/create', data })
}

export const updateLoan = (data: LoanVO) => {
  return request.put({ url: '/oa/loan/update?id=' + data.id, data })
}

export const submitLoan = (id: number) => {
  return request.put({ url: '/oa/loan/submit?id=' + id })
}

export const updateLoanStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/loan/update-status?id=' + id + '&status=' + status })
}

export const deleteLoan = (id: number) => {
  return request.delete({ url: '/oa/loan/delete?id=' + id })
}
