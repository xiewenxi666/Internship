import request from '@/config/axios'

export type TripVO = {
  id: number
  status: number
  type: number
  destination: string
  reason: string
  processInstanceId: string
  startTime: string
  endTime: string
  createTime: string
}

export const createTrip = async (data: TripVO) => {
  return await request.post({ url: '/oa/trip/create', data })
}

export const getTrip = async (id: number) => {
  return await request.get({ url: '/oa/trip/get?id=' + id })
}

export const getTripPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/trip/page', params })
}
