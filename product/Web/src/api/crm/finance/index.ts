import request from '@/config/axios'

export const getFinanceSummary = async (params: { year?: number; ownerUserId?: number }) => {
  return await request.get({ url: '/crm/finance/summary', params })
}
