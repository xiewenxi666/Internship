import request from '@/config/axios'

export const getMarketingStats = async () => {
  return await request.get({ url: '/crm/marketing-analysis/stats' })
}
