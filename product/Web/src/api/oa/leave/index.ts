import request from '@/config/axios'

export interface LeaveVO {
  id: number | undefined
  status: number
  type: number
  reason: string
  startTime: string
  endTime: string
  processInstanceId: string
  startUserSelectAssignees?: Record<string, number[]>
  createTime: Date
}

export const getLeavePage = (params: PageParam) => {
  return request.get({ url: '/oa/leave/page', params })
}

export const getLeave = (id: number) => {
  return request.get({ url: '/oa/leave/get?id=' + id })
}

export const createLeave = (data: LeaveVO) => {
  return request.post({ url: '/oa/leave/create', data })
}

export const updateLeave = (data: LeaveVO) => {
  return request.put({ url: '/oa/leave/update?id=' + data.id, data })
}

export const submitLeave = (id: number) => {
  return request.put({ url: '/oa/leave/submit?id=' + id })
}

export const updateLeaveStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/leave/update-status?id=' + id + '&status=' + status })
}

export const deleteLeave = (id: number) => {
  return request.delete({ url: '/oa/leave/delete?id=' + id })
}
