import request from '@/config/axios'

export type ReportVO = {
  id: number
  type: number
  reportDate: string
  content: string
  plan: string
  createTime: string
}

export const createReport = async (data: ReportVO) => {
  return await request.post({ url: '/oa/report/create', data })
}

export const updateReport = async (data: ReportVO) => {
  return await request.put({ url: '/oa/report/update', data })
}

export const getReport = async (id: number) => {
  return await request.get({ url: '/oa/report/get?id=' + id })
}

export const getReportPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/report/page', params })
}

export const deleteReport = async (id: number) => {
  return await request.delete({ url: '/oa/report/delete?id=' + id })
}
