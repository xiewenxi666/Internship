import request from '@/config/axios'

export interface ScheduleVO {
  id: number | undefined
  title: string
  description: string
  startTime: string
  endTime: string
  isAllDay: boolean
  location: string
  type: number
  priority: number
  reminderTime: number
  color: string
  status: number
  createTime: Date
}

export const getSchedulePage = (params: PageParam) => {
  return request.get({ url: '/oa/schedule/page', params })
}

export const getSchedule = (id: number) => {
  return request.get({ url: '/oa/schedule/get?id=' + id })
}

export const createSchedule = (data: ScheduleVO) => {
  return request.post({ url: '/oa/schedule/create', data })
}

export const updateSchedule = (data: ScheduleVO) => {
  return request.put({ url: '/oa/schedule/update?id=' + data.id, data })
}

export const updateScheduleStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/schedule/update-status?id=' + id + '&status=' + status })
}

export const deleteSchedule = (id: number) => {
  return request.delete({ url: '/oa/schedule/delete?id=' + id })
}

export const getScheduleCalendarList = (params: { startTime: string; endTime: string; type?: number }) => {
  return request.get({ url: '/oa/schedule/calendar', params })
}
