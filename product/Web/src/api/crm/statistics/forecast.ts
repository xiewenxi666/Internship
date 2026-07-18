import request from '@/config/axios'

export interface StatisticsForecastReqVO {
  deptId: number
  userId?: number
  year: number
}

export interface StatisticsForecastSummaryVO {
  businessCount: number
  totalPrice: number
  probabilityPrice: number
}

export interface StatisticsForecastByMonthVO {
  month: string
  businessCount: number
  totalPrice: number
  probabilityPrice: number
}

// 获取销售预测汇总数据
export const getForecastSummary = async (params: StatisticsForecastReqVO) => {
  return await request.get({ url: `/crm/statistics-forecast/get-forecast-summary`, params })
}

// 获取销售预测按月数据
export const getForecastByMonth = async (params: StatisticsForecastReqVO) => {
  return await request.get({ url: `/crm/statistics-forecast/get-forecast-by-month`, params })
}
