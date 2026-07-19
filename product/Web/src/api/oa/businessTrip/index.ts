import request from '@/config/axios'

export interface BusinessTripVO {
  id: number | undefined
  status: number
  destination: string
  reason: string
  companion: string
  vehicle: string
  estimatedAmount: number
  processInstanceId: string
  startTime: string
  endTime: string
  startUserSelectAssignees?: Record<string, number[]>
  createTime: Date
}

export const getBusinessTripPage = (params: PageParam) => {
  return request.get({ url: '/oa/business-trip/page', params })
}

export const getBusinessTrip = (id: number) => {
  return request.get({ url: '/oa/business-trip/get?id=' + id })
}

export const createBusinessTrip = (data: BusinessTripVO) => {
  return request.post({ url: '/oa/business-trip/create', data })
}

export const updateBusinessTrip = (data: BusinessTripVO) => {
  return request.put({ url: '/oa/business-trip/update?id=' + data.id, data })
}

export const submitBusinessTrip = (id: number) => {
  return request.put({ url: '/oa/business-trip/submit?id=' + id })
}

export const updateBusinessTripStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/business-trip/update-status?id=' + id + '&status=' + status })
}

export const deleteBusinessTrip = (id: number) => {
  return request.delete({ url: '/oa/business-trip/delete?id=' + id })
}
