import request from '@/config/axios'

export interface RequestVO {
  id: number | undefined
  status: number
  title: string
  content: string
  type: number
  urgency: number
  attachment: string
  expectedAmount: number
  processInstanceId: string
  startUserSelectAssignees?: Record<string, number[]>
  createTime: Date
}

export const getRequestPage = (params: PageParam) => {
  return request.get({ url: '/oa/request/page', params })
}

export const getRequest = (id: number) => {
  return request.get({ url: '/oa/request/get?id=' + id })
}

export const createRequest = (data: RequestVO) => {
  return request.post({ url: '/oa/request/create', data })
}

export const updateRequest = (data: RequestVO) => {
  return request.put({ url: '/oa/request/update?id=' + data.id, data })
}

export const submitRequest = (id: number) => {
  return request.put({ url: '/oa/request/submit?id=' + id })
}

export const cancelRequest = (id: number) => {
  return request.put({ url: '/oa/request/cancel?id=' + id })
}

export const reconsiderRequest = (id: number) => {
  return request.put({ url: '/oa/request/reconsider?id=' + id })
}

export const updateRequestStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/request/update-status?id=' + id + '&status=' + status })
}

export const deleteRequest = (id: number) => {
  return request.delete({ url: '/oa/request/delete?id=' + id })
}
