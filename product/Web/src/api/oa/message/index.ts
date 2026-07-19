import request from '@/config/axios'

export interface MessageVO {
  id: number | undefined
  receiverUserId: number
  title: string
  content: string
  status: number
  readTime: string
  createTime: Date
}

export const getMessagePage = (params: PageParam) => {
  return request.get({ url: '/oa/message/page', params })
}

export const getMessage = (id: number) => {
  return request.get({ url: '/oa/message/get?id=' + id })
}

export const sendMessage = (data: MessageVO) => {
  return request.post({ url: '/oa/message/send', data })
}

export const markRead = (id: number) => {
  return request.put({ url: '/oa/message/read?id=' + id })
}

export const deleteMessage = (id: number) => {
  return request.delete({ url: '/oa/message/delete?id=' + id })
}

export const getUnreadCount = () => {
  return request.get({ url: '/oa/message/unread-count' })
}
