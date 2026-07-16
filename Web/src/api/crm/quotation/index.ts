import request from '@/config/axios'

export interface QuotationVO {
  id?: number
  quotationNo: string
  businessId: number
  businessName?: string
  customerId: number
  customerName?: string
  contactId?: number
  contactName?: string
  ownerUserId: number
  ownerUserName?: string
  status: number
  statusName?: string
  totalAmount: number
  discountPercent: number
  finalAmount: number
  validDays?: number
  expireTime?: Date
  auditUserId?: number
  auditUserName?: string
  auditTime?: Date
  auditRemark?: string
  remark?: string
  creator?: string
  creatorName?: string
  createTime?: Date
  updateTime?: Date
  products?: QuotationProductVO[]
}

export interface QuotationProductVO {
  id?: number
  quotationId?: number
  productId: number
  productName?: string
  productCode?: string
  productUnit?: string
  productPrice: number
  quotationPrice: number
  count: number
  totalPrice: number
  remark?: string
}

// 查询报价单分页
export const getQuotationPage = async (params) => {
  return await request.get({ url: `/crm/quotation/page`, params })
}

// 获取报价单详情
export const getQuotation = async (id: number) => {
  return await request.get({ url: `/crm/quotation/get?id=` + id })
}

// 创建报价单
export const createQuotation = async (data: QuotationVO) => {
  return await request.post({ url: `/crm/quotation/create`, data })
}

// 更新报价单
export const updateQuotation = async (data: QuotationVO) => {
  return await request.put({ url: `/crm/quotation/update`, data })
}

// 删除报价单
export const deleteQuotation = async (id: number) => {
  return await request.delete({ url: `/crm/quotation/delete?id=` + id })
}

// 确认报价单
export const confirmQuotation = async (id: number, auditRemark?: string) => {
  return await request.put({ url: `/crm/quotation/confirm?id=${id}&auditRemark=${auditRemark || ''}` })
}

// 拒绝报价单
export const rejectQuotation = async (id: number, auditRemark?: string) => {
  return await request.put({ url: `/crm/quotation/reject?id=${id}&auditRemark=${auditRemark || ''}` })
}

// 作废报价单
export const voidQuotation = async (id: number) => {
  return await request.put({ url: `/crm/quotation/void?id=` + id })
}

// 提交报价单审批
export const submitQuotation = async (id: number) => {
  return await request.put({ url: `/crm/quotation/submit?id=` + id })
}

// 导出报价单 Excel
export const exportQuotation = async (params) => {
  return await request.download({ url: `/crm/quotation/export-excel`, params })
}
