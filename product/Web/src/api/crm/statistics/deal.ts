import request from '@/config/axios'

export interface DealReqVO {
  deptId: number
  userId?: number
  year?: number
  pageNo?: number
  pageSize?: number
}

export interface DealBusinessSummaryVO {
  businessCount: number
}

export interface FunnelReportSummaryVO {
  businessCount: number
  totalPrice: number
  probabilityPrice: number
}

// 成交商机报表 - 汇总
export const getDealBusinessSummary = async (params: DealReqVO) => {
  return await request.get({ url: '/crm/statistics-deal/get-deal-business-summary', params })
}

// 成交商机报表 - 分页
export const getDealBusinessPage = async (params: DealReqVO) => {
  return await request.get({ url: '/crm/statistics-deal/get-deal-business-page', params })
}

// 销售漏斗报表 - 汇总
export const getFunnelReportSummary = async (params: DealReqVO) => {
  return await request.get({ url: '/crm/statistics-deal/get-funnel-report-summary', params })
}

// 销售漏斗报表 - 分页（活跃商机）
export const getActiveBusinessPage = async (params: DealReqVO) => {
  return await request.get({ url: '/crm/statistics-deal/get-active-business-page', params })
}
