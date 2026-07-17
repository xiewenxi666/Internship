import request from '@/config/axios'

export interface ReimbursementVO {
  id: number
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
  ownerUserId: number
  ownerUserName?: string
  content: string
  price: number
  type: number
  applyDate: Date
  remark: string
  auditStatus: number
  processInstanceId: number
  creator: string
  creatorName?: string
  createTime: Date
  updateTime: Date
  updater: string
}

export const getReimbursementPage = async (params) => {
  return await request.get({ url: `/crm/reimbursement/page`, params })
}

export const getReimbursementPageByCustomer = async (params) => {
  return await request.get({ url: `/crm/reimbursement/page-by-customer`, params })
}

export const getReimbursement = async (id: number) => {
  return await request.get({ url: `/crm/reimbursement/get?id=` + id })
}

export const createReimbursement = async (data: ReimbursementVO) => {
  return await request.post({ url: `/crm/reimbursement/create`, data })
}

export const updateReimbursement = async (data: ReimbursementVO) => {
  return await request.put({ url: `/crm/reimbursement/update`, data })
}

export const deleteReimbursement = async (id: number) => {
  return await request.delete({ url: `/crm/reimbursement/delete?id=` + id })
}

export const exportReimbursement = async (params) => {
  return await request.download({ url: `/crm/reimbursement/export-excel`, params })
}

export const submitReimbursement = async (id: number) => {
  return await request.put({ url: `/crm/reimbursement/submit?id=${id}` })
}

export const getAuditReimbursementCount = async () => {
  return await request.get({ url: '/crm/reimbursement/audit-count' })
}

export const cancelReimbursement = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/reimbursement/cancel?${params.toString()}` })
}

export const getReimbursementApprovalPage = async (params) => {
  return await request.get({ url: `/crm/reimbursement/approval-page`, params })
}

export const approveReimbursement = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/reimbursement/approve?${params.toString()}` })
}

export const rejectReimbursement = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/reimbursement/reject?${params.toString()}` })
}

export const vetoReimbursement = async (id: number, reason?: string) => {
  const params = new URLSearchParams()
  params.append('id', String(id))
  if (reason) {
    params.append('reason', reason)
  }
  return await request.put({ url: `/crm/reimbursement/veto?${params.toString()}` })
}
