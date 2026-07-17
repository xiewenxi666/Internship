import request from '@/config/axios'

export type DocumentVO = {
  id: number
  parentId: number
  name: string
  isFolder: boolean
  fileId: number
  fileUrl: string
  fileSize: number
  description: string
  createTime: string
}

export const createDocument = async (data: DocumentVO) => {
  return await request.post({ url: '/oa/document/create', data })
}

export const updateDocument = async (data: DocumentVO) => {
  return await request.put({ url: '/oa/document/update', data })
}

export const getDocument = async (id: number) => {
  return await request.get({ url: '/oa/document/get?id=' + id })
}

export const getDocumentPage = async (params: PageParam) => {
  return await request.get({ url: '/oa/document/page', params })
}

export const deleteDocument = async (id: number) => {
  return await request.delete({ url: '/oa/document/delete?id=' + id })
}
