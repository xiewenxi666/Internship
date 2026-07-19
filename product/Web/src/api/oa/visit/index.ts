import request from '@/config/axios'

export interface VisitVO {
  id: number | undefined
  status: number
  customerName: string
  crmCustomerId: number
  contactPerson: string
  contactPhone: string
  visitAddress: string
  visitTime: string
  purpose: string
  result: string
  nextVisitTime: string
  processInstanceId: string
  startUserSelectAssignees: string
  createTime: Date
}

export const getVisitPage = (params: PageParam) => {
  return request.get({ url: '/oa/visit/page', params })
}

export const getVisit = (id: number) => {
  return request.get({ url: '/oa/visit/get?id=' + id })
}

export const createVisit = (data: VisitVO) => {
  return request.post({ url: '/oa/visit/create', data })
}

export const updateVisit = (data: VisitVO) => {
  return request.put({ url: '/oa/visit/update?id=' + data.id, data })
}

export const submitVisit = (id: number) => {
  return request.put({ url: '/oa/visit/submit?id=' + id })
}

export const updateVisitStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/visit/update-status?id=' + id + '&status=' + status })
}

export const deleteVisit = (id: number) => {
  return request.delete({ url: '/oa/visit/delete?id=' + id })
}
