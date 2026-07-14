import request from '@/config/axios'

export interface CampaignVO {
  id: number
  title: string
  type: number
  startTime: Date
  endTime: Date
  estimatedCost: number
  estimatedRevenue: number
  ownerUserId: number
  ownerUserName?: string
  participants: string
  address: string
  description: string
  status: number
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

export const getCampaignPage = async (params: any) => {
  return await request.get({ url: '/crm/campaign/page', params })
}

export const getCampaign = async (id: number) => {
  return await request.get({ url: '/crm/campaign/get?id=' + id })
}

export const createCampaign = async (data: CampaignVO) => {
  return await request.post({ url: '/crm/campaign/create', data })
}

export const updateCampaign = async (data: CampaignVO) => {
  return await request.put({ url: '/crm/campaign/update', data })
}

export const deleteCampaign = async (id: number) => {
  return await request.delete({ url: '/crm/campaign/delete?id=' + id })
}
