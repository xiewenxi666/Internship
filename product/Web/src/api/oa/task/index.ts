import request from '@/config/axios'

export type TaskVO = {
  id: number
  title: string
  description: string
  assigneeId: number
  priority: number
  status: number
  deadline: string
  completedTime: string
  createTime: string
}

export const createTask = async (data: TaskVO) => {
  return await request.post({ url: '/oa/task/create', data })
}

export const updateTask = async (data: TaskVO) => {
  return await request.put({ url: '/oa/task/update', data })
}

export const getTask = async (id: number) => {
  return await request.get({ url: '/oa/task/get?id=' + id })
}

export const getTaskPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/task/page', params })
}

export const completeTask = async (id: number) => {
  return await request.put({ url: '/oa/task/complete?id=' + id })
}

export const deleteTask = async (id: number) => {
  return await request.delete({ url: '/oa/task/delete?id=' + id })
}
