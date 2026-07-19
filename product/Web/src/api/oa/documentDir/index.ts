import request from '@/config/axios'

export interface DocumentDirVO {
  id: number | undefined
  parentId: number
  name: string
  type: number
  sort: number
  description: string
  keywords: string
  permission: number
  fileUrl: string
  fileName: string
  fileType: string
  fileSize: number
  createTime: Date
}

export const getDocumentDirPage = (params: PageParam) => {
  return request.get({ url: '/oa/document-dir/page', params })
}

export const getDocumentDir = (id: number) => {
  return request.get({ url: '/oa/document-dir/get?id=' + id })
}

export const createDocumentDir = (data: DocumentDirVO) => {
  return request.post({ url: '/oa/document-dir/create', data })
}

export const updateDocumentDir = (data: DocumentDirVO) => {
  return request.put({ url: '/oa/document-dir/update?id=' + data.id, data })
}

export const deleteDocumentDir = (id: number) => {
  return request.delete({ url: '/oa/document-dir/delete?id=' + id })
}

export const getChildList = (parentId: number) => {
  return request.get({ url: '/oa/document-dir/children?parentId=' + parentId })
}
