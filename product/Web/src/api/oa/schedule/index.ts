import request from '@/config/axios'

export type ScheduleVO = {
  id: number
  title: string
  startTime: string
  endTime: string
  allDay: boolean
  location: string
  description: string
  createTime: string
}

export const createSchedule = async (data: ScheduleVO) => {
  return await request.post({ url: '/oa/schedule/create', data })
}

export const updateSchedule = async (data: ScheduleVO) => {
  return await request.put({ url: '/oa/schedule/update', data })
}

export const getSchedule = async (id: number) => {
  return await request.get({ url: '/oa/schedule/get?id=' + id })
}

export const getSchedulePage = async (params: PageParam) => {
  return await request.get({ url: '/oa/schedule/page', params })
}

export const deleteSchedule = async (id: number) => {
  return await request.delete({ url: '/oa/schedule/delete?id=' + id })
}
