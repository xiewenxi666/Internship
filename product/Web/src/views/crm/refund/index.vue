<template>
  <doc-alert title="【退款】退款管理" url="https://doc.iocoder.cn/crm/refund/" />

  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="退款编号" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              placeholder="请输入退款编号"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="客户名称" prop="customerId">
            <el-select
              v-model="queryParams.customerId"
              class="!w-240px"
              placeholder="请选择客户"
              @keyup.enter="handleQuery"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              {{ t('common.search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('common.reset') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:refund:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:refund:export']"
              :loading="exportLoading"
              plain
              type="success"
              @click="handleExport"
            >
              <Icon class="mr-5px" icon="ep:download" />
              {{ t('common.export') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('customer.myResponsible')" name="1" />
      <el-tab-pane :label="t('customer.myInvolved')" name="2" />
      <el-tab-pane :label="t('customer.subordinateResponsible')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
      <el-table-column align="center" fixed="left" label="退款编号" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="客户名称" prop="customerName" min-width="120">
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openCustomerDetail(scope.row.customerId)"
          >
            {{ scope.row.customerName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="关联订单" prop="orderNo" min-width="180">
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openOrderDetail(scope.row.orderId)"
          >
            {{ scope.row.order?.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="退款类型" prop="type" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_REFUND_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="退款金额（元）"
        prop="price"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        label="退款日期"
        prop="refundDate"
        min-width="150"
      />
      <el-table-column align="center" label="审批状态" prop="auditStatus" min-width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="负责人" prop="ownerUserName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" label="操作" min-width="220">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:refund:query']"
            link
            type="primary"
            @click="openDetail(scope.row.id)"
          >
            详情
          </el-button>
          <el-button
            v-if="scope.row.auditStatus === 0 || scope.row.auditStatus === 30 || scope.row.auditStatus === 40 || scope.row.auditStatus === 50"
            v-hasPermi="['crm:refund:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            编辑
          </el-button>
          <el-button
            v-if="scope.row.auditStatus === 0 || scope.row.auditStatus === 30 || scope.row.auditStatus === 40 || scope.row.auditStatus === 50"
            v-hasPermi="['crm:refund:update']"
            link
            type="success"
            @click="handleSubmit(scope.row.id)"
          >
            提交审核
          </el-button>
          <el-button
            v-if="scope.row.auditStatus === 10"
            v-hasPermi="['crm:refund:update']"
            link
            type="danger"
            @click="handleCancel(scope.row.id)"
          >
            撤销审批
          </el-button>
          <el-button
            v-if="scope.row.auditStatus !== 10 && scope.row.auditStatus !== 20"
            v-hasPermi="['crm:refund:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <RefundForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as RefundApi from '@/api/crm/refund'
import RefundForm from './RefundForm.vue'
import * as CustomerApi from '@/api/crm/customer'
import { TabsPaneContext } from 'element-plus'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmRefund' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  sceneType: '1',
  no: undefined,
  customerId: undefined
})
const queryFormRef = ref()
const exportLoading = ref(false)
const activeName = ref('1')
const customerList = ref<CustomerApi.CustomerVO[]>([])

const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName
  handleQuery()
}

const getList = async () => {
  loading.value = true
  try {
    const data = await RefundApi.getRefundPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await RefundApi.deleteRefund(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/** 提交审核 */
const handleSubmit = async (id: number) => {
  try {
    await message.confirm('确定提交该退款审核吗？')
    await RefundApi.submitRefund(id)
    message.success('提交审核成功')
    await getList()
  } catch {}
}

/** 撤销审批 */
const handleCancel = async (id: number) => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入撤销原因（可不填）：', '撤销审批', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入原因',
      inputType: 'textarea'
    })
    await RefundApi.cancelRefund(id, reason || undefined)
    message.success('撤销审批成功')
    await getList()
  } catch {}
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmRefundDetail', params: { id } })
}

const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

const openOrderDetail = (id: number) => {
  push({ name: 'CrmOrderDetail', params: { id } })
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await RefundApi.exportRefund(queryParams)
    download.excel(data, '退款管理.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  await getList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
