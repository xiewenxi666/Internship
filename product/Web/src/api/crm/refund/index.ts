import request from '@/config/axios'

export interface RefundVO {
  id?: number
  no: string
  customerId?: number
  customerName?: string
  contractId?: number
  contract?: {
    id?: number
    name?: string
    no: string
    totalPrice: number
  }
  auditStatus: number
  processInstanceId?: number
  content: string
  price: number
  type: number
  refundDate: Date
  remark: string
  ownerUserId: number
  ownerUserName?: string
  creator: string
  creatorName?: string
  createTime: Date
  updater?: string
  updateTime?: Date
}

export const getRefundPage = async (params) => {
  return await request.get({ url: `/crm/refund/page`, params })
}

export const getRefundPageByCustomer = async (params) => {
  return await request.get({ url: `/crm/refund/page-by-customer`, params })
}

export const getRefund = async (id: number) => {
  return await request.get({ url: `/crm/refund/get?id=` + id })
}

export const createRefund = async (data: RefundVO) => {
  return await request.post({ url: `/crm/refund/create`, data })
}

export const updateRefund = async (data: RefundVO) => {
  return await request.put({ url: `/crm/refund/update`, data })
}

export const deleteRefund = async (id: number) => {
  return await request.delete({ url: `/crm/refund/delete?id=` + id })
}

export const exportRefund = async (params) => {
  return await request.download({ url: `/crm/refund/export-excel`, params })
}

export const submitRefund = async (id: number) => {
  return await request.put({ url: `/crm/refund/submit?id=${id}` })
}

export const getAuditRefundCount = async () => {
  return await request.get({ url: '/crm/refund/audit-count' })
}

export const cancelRefund = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/refund/cancel?${params.toString()}` })
}

export const getRefundApprovalPage = async (params) => {
  return await request.get({ url: `/crm/refund/approval-page`, params })
}

export const approveRefund = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/refund/approve?${params.toString()}` })
}

export const rejectRefund = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/refund/reject?${params.toString()}` })
}

export const vetoRefund = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/refund/veto?${params.toString()}` })
}
