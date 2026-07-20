import request from '@/config/axios'

export interface InvoiceVO {
  id: number
  no: string
  orderId?: number
  orderNo?: string
  orderName?: string
  customerId?: number
  customerName?: string
  invoiceDate: Date
  type: number
  price: number
  invoiceNo: string
  content: string
  handlerUserId: number
  handlerUserName?: string
  ownerUserId: number
  ownerUserName?: string
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
}

export const getInvoicePage = async (params) => {
  return await request.get({ url: `/crm/invoice/page`, params })
}

export const getInvoice = async (id: number) => {
  return await request.get({ url: `/crm/invoice/get?id=` + id })
}

export const createInvoice = async (data: InvoiceVO) => {
  return await request.post({ url: `/crm/invoice/create`, data })
}

export const updateInvoice = async (data: InvoiceVO) => {
  return await request.put({ url: `/crm/invoice/update`, data })
}

export const deleteInvoice = async (id: number) => {
  return await request.delete({ url: `/crm/invoice/delete?id=` + id })
}

export const exportInvoice = async (params) => {
  return await request.download({ url: `/crm/invoice/export`, params })
}

export const getInvoiceReport = async (params) => {
  return await request.get({ url: `/crm/invoice/report`, params })
}
