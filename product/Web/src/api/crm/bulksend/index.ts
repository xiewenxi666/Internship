import request from '@/config/axios'

export interface BulkSendVO {
  id: number
  title: string
  campaignId: number
  campaignName: string
  type: number
  templateId: number
  content: string
  targetType: number
  targetIds: string
  targetCount: number
  successCount: number
  failCount: number
  status: number
  ownerUserId: number
  ownerUserName?: string
  sendTime: Date
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

export const getBulkSendPage = async (params: any) => {
  return await request.get({ url: '/crm/bulk-send/page', params })
}

export const getBulkSend = async (id: number) => {
  return await request.get({ url: '/crm/bulk-send/get?id=' + id })
}

export const createBulkSend = async (data: BulkSendVO) => {
  return await request.post({ url: '/crm/bulk-send/create', data })
}

export const updateBulkSend = async (data: BulkSendVO) => {
  return await request.put({ url: '/crm/bulk-send/update', data })
}

export const deleteBulkSend = async (id: number) => {
  return await request.delete({ url: '/crm/bulk-send/delete?id=' + id })
}

export const submitForApproval = async (id: number) => {
  return await request.post({ url: '/crm/bulk-send/submit/' + id })
}

export const approve = async (id: number) => {
  return await request.post({ url: '/crm/bulk-send/approve/' + id })
}

export const reject = async (id: number) => {
  return await request.post({ url: '/crm/bulk-send/reject/' + id })
}
