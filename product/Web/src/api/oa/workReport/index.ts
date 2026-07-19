import request from '@/config/axios'

export interface WorkReportVO {
  id: number | undefined
  status: number
  type: number
  title: string
  content: string
  plan: string
  summary: string
  reportDate: string
  createTime: Date
}

export const getWorkReportPage = (params: PageParam) => {
  return request.get({ url: '/oa/work-report/page', params })
}

export const getWorkReport = (id: number) => {
  return request.get({ url: '/oa/work-report/get?id=' + id })
}

export const createWorkReport = (data: WorkReportVO) => {
  return request.post({ url: '/oa/work-report/create', data })
}

export const updateWorkReport = (data: WorkReportVO) => {
  return request.put({ url: '/oa/work-report/update?id=' + data.id, data })
}

export const submitWorkReport = (id: number) => {
  return request.put({ url: '/oa/work-report/submit?id=' + id })
}

export const reviewWorkReport = (id: number, reviewContent: string) => {
  return request.put({ url: '/oa/work-report/review?id=' + id + '&reviewContent=' + reviewContent })
}

export const deleteWorkReport = (id: number) => {
  return request.delete({ url: '/oa/work-report/delete?id=' + id })
}
