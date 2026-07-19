import request from '@/config/axios'

export interface CustomerCareConfigVO {
  id: number
  smsContent: string
  emailTitle: string
  emailBody: string
  senderEmail: string
  testEmail: string
  sendTime: string
  smsEnabled: boolean
  emailEnabled: boolean
  holidayList: string
  createTime: Date
  updateTime: Date
}

export const getConfig = async () => {
  return await request.get({ url: '/crm/customer-care/config' })
}

export const saveConfig = async (data: CustomerCareConfigVO) => {
  return await request.post({ url: '/crm/customer-care/save', data })
}

export const sendTestEmail = async (data: CustomerCareConfigVO) => {
  return await request.post({ url: '/crm/customer-care/send-test', data })
}
