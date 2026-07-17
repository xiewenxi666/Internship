import request from '@/config/axios'

export type LeaveVO = {
  id: number
  status: number
  type: number
  reason: string
  processInstanceId: string
  startTime: string
  endTime: string
  createTime: string
}

export const createLeave = async (data: LeaveVO) => {
  return await request.post({ url: '/oa/leave/create', data })
}

export const getLeave = async (id: number) => {
  return await request.get({ url: '/oa/leave/get?id=' + id })
}

export const getLeavePage = async (params: PageParam) => {
  return await request.get({ url: '/oa/leave/page', params })
}
