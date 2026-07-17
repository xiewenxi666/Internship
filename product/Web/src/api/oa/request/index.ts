import request from '@/config/axios'

export type RequestVO = {
  id: number
  status: number
  title: string
  content: string
  urgency: number
  processInstanceId: string
  createTime: string
}

export const createRequest = async (data: RequestVO) => {
  return await request.post({ url: '/oa/request/create', data })
}

export const getRequest = async (id: number) => {
  return await request.get({ url: '/oa/request/get?id=' + id })
}

export const getRequestPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/request/page', params })
}
