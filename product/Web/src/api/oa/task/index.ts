import request from '@/config/axios'

export interface TaskVO {
  id: number | undefined
  title: string
  description: string
  priority: number
  projectId: number
  assigneeUserId: number
  startTime: string
  deadline: string
  tags: string
  attachment: string
  status: number
  progress: number
  createTime: Date
}

export const getTaskPage = (params: PageParam) => {
  return request.get({ url: '/oa/task/page', params })
}

export const getTask = (id: number) => {
  return request.get({ url: '/oa/task/get?id=' + id })
}

export const createTask = (data: TaskVO) => {
  return request.post({ url: '/oa/task/create', data })
}

export const updateTask = (data: TaskVO) => {
  return request.put({ url: '/oa/task/update?id=' + data.id, data })
}

export const updateTaskStatus = (id: number, status: number) => {
  return request.put({ url: '/oa/task/update-status?id=' + id + '&status=' + status })
}

export const updateTaskProgress = (id: number, progress: number) => {
  return request.put({ url: '/oa/task/update-progress?id=' + id + '&progress=' + progress })
}

export const completeTask = (id: number) => {
  return request.put({ url: '/oa/task/complete?id=' + id })
}

export const deleteTask = (id: number) => {
  return request.delete({ url: '/oa/task/delete?id=' + id })
}

export const getTaskListByAssignee = (params: { assigneeUserId: number; status?: number }) => {
  return request.get({ url: '/oa/task/list-by-assignee', params })
}

export const getTaskStatistics = () => {
  return request.get({ url: '/oa/task/statistics' })
}
