<template>
  <doc-alert title="【订单】订单管理" url="https://doc.iocoder.cn/crm/order/" />
  <doc-alert title="【通用】数据权限" url="https://doc.iocoder.cn/crm/permission/" />

  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('crm.order.no')" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              :placeholder="t('crm.order.noPlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('crm.order.name')" prop="name">
            <el-input
              v-model="queryParams.name"
              class="!w-240px"
              clearable
              :placeholder="t('crm.order.namePlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('crm.order.customerId')" prop="customerId">
            <el-select
              v-model="queryParams.customerId"
              class="!w-240px"
              clearable
              :placeholder="t('crm.order.customerIdPlaceholder')"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id!"
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
            <el-button v-hasPermi="['crm:order:create']" type="primary" @click="openForm('create')">
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('common.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:order:export']"
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
      <el-tab-pane :label="t('crm.customer.myResponsible')" name="1" />
      <el-tab-pane :label="t('crm.customer.myInvolved')" name="2" />
      <el-tab-pane :label="t('crm.customer.subordinateResponsible')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" fixed="left" :label="t('crm.order.no')" prop="no" min-width="180" />
      <el-table-column align="center" fixed="left" :label="t('crm.order.name')" prop="name" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('crm.order.customerName')" prop="customerName" min-width="120">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openCustomerDetail(scope.row.customerId)">
            {{ scope.row.customerName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('crm.order.businessName')" prop="businessName" min-width="130">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openBusinessDetail(scope.row.businessId)">
            {{ scope.row.businessName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('crm.order.totalPrice') + '（元）'" prop="totalPrice" min-width="140" :formatter="erpPriceTableColumnFormatter" />
      <el-table-column align="center" :label="t('crm.order.orderDate')" prop="orderDate" min-width="120" :formatter="dateFormatter2" />
      <el-table-column align="center" :label="t('crm.order.remark')" prop="remark" min-width="200" />
      <el-table-column align="center" :label="t('crm.order.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column align="center" :label="t('crm.order.ownerUserDeptName')" prop="ownerUserDeptName" min-width="100" />
      <el-table-column :formatter="dateFormatter" align="center" :label="t('crm.order.createTime')" prop="createTime" min-width="180" />
      <el-table-column align="center" :label="t('crm.order.creatorName')" prop="creatorName" min-width="120" />
      <el-table-column align="center" :label="t('crm.order.status')" prop="status" min-width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_ORDER_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column fixed="right" :label="t('common.action')" min-width="280">
        <template #default="scope">
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['crm:order:update']"
            link type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['crm:order:update']"
            link type="primary"
            @click="handleSubmit(scope.row)"
          >
            {{ t('crm.order.submitAudit') }}
          </el-button>
          <el-button
            v-else-if="scope.row.processInstanceId"
            link
            v-hasPermi="['crm:order:update']"
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('crm.order.viewApproval') }}
          </el-button>
          <el-button
            v-if="scope.row.status === 0 || scope.row.status === 10 || scope.row.status === 20"
            v-hasPermi="['crm:order:update']"
            link type="primary"
            @click="handleUpdateStatus(scope.row)"
          >
            {{ t('crm.order.updateStatus') }}
          </el-button>
          <el-button v-hasPermi="['crm:order:query']" link type="primary" @click="openDetail(scope.row.id)">
            {{ t('common.detail') }}
          </el-button>
          <el-button v-hasPermi="['crm:order:delete']" link type="danger" @click="handleDelete(scope.row.id)">
            {{ t('common.del') }}
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

  <OrderForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as OrderApi from '@/api/crm/order'
import OrderForm from './OrderForm.vue'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceTableColumnFormatter } from '@/utils'
import * as CustomerApi from '@/api/crm/customer'
import { TabsPaneContext } from 'element-plus'

defineOptions({ name: 'CrmOrder' })

const message = useMessage()
const { t } = useI18n()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  sceneType: '1',
  name: null,
  customerId: null,
  orderDate: [],
  no: null
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
    const data = await OrderApi.getOrderPage(queryParams)
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
    await OrderApi.deleteOrder(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await OrderApi.exportOrder(queryParams)
    download.excel(data, '订单.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const handleSubmit = async (row: OrderApi.OrderVO) => {
  await message.confirm(t('crm.order.submitAuditConfirm', { name: row.name }))
  await OrderApi.submitOrder(row.id)
  message.success(t('crm.order.submitAuditSuccess'))
  await getList()
}

const handleProcessDetail = (row: OrderApi.OrderVO) => {
  push({ name: 'BpmProcessInstanceDetail', query: { id: row.processInstanceId } })
}

const handleUpdateStatus = async (row: OrderApi.OrderVO) => {
  const status = await message.prompt({
    title: t('crm.order.updateStatus'),
    inputType: 'select',
    inputOptions: [
      { value: 40, label: t('crm.order.statusProcessing') },
      { value: 60, label: t('crm.order.statusCompleted') },
      { value: 70, label: t('crm.order.statusCancelled') }
    ],
    inputPlaceholder: t('crm.order.updateStatus')
  })
  if (!status) return
  await OrderApi.updateOrderStatus({ id: row.id, status })
  message.success(t('crm.order.updateStatusSuccess'))
  await getList()
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmOrderDetail', params: { id } })
}

const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

const openBusinessDetail = (id: number) => {
  push({ name: 'CrmBusinessDetail', params: { id } })
}

onMounted(async () => {
  await getList()
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
