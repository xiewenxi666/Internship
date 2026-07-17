import request from '@/config/axios'

export type VisitVO = {
  id: number
  status: number
  customerId: number
  contactPerson: string
  contactPhone: string
  visitTime: string
  location: string
  purpose: string
  notes: string
  processInstanceId: string
  createTime: string
}

export const createVisit = async (data: VisitVO) => {
  return await request.post({ url: '/oa/visit/create', data })
}

export const getVisit = async (id: number) => {
  return await request.get({ url: '/oa/visit/get?id=' + id })
}

export const getVisitPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/visit/page', params })
}
