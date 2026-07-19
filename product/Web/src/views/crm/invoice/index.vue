<template>
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="发票编号" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              placeholder="请输入发票编号"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="关联订单" prop="orderNo">
            <el-input
              v-model="queryParams.orderNo"
              class="!w-240px"
              clearable
              placeholder="请输入关联订单号"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="票据类型" prop="type">
            <el-select
              v-model="queryParams.type"
              class="!w-240px"
              clearable
              placeholder="请选择票据类型"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_INVOICE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="8">
          <el-form-item label="开票日期" prop="invoiceDate">
            <el-date-picker
              v-model="queryParams.invoiceDate"
              class="!w-240px"
              placeholder="请选择开票日期"
              type="date"
              value-format="x"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              搜索
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              重置
            </el-button>
            <el-button
              v-hasPermi="['crm:invoice:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              新增
            </el-button>
            <el-button
              v-hasPermi="['crm:invoice:export']"
              :loading="exportLoading"
              plain
              type="success"
              @click="handleExport"
            >
              <Icon class="mr-5px" icon="ep:download" />
              导出
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
      <el-table-column align="center" fixed="left" label="发票编号" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="关联订单" prop="orderNo" min-width="180">
        <template #default="scope">
          {{ scope.row.contract?.no }}
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        label="开票日期"
        prop="invoiceDate"
        min-width="150"
      />
      <el-table-column align="center" label="票据类型" prop="type" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_INVOICE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="开票金额（元）"
        prop="price"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" label="发票号码" prop="invoiceNo" min-width="180" />
      <el-table-column align="center" label="订单所属人员" prop="ownerUserName" min-width="120" />
      <el-table-column align="center" fixed="right" label="操作" min-width="150">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:invoice:query']"
            link
            type="primary"
            @click="openDetail(scope.row.id)"
          >
            详情
          </el-button>
          <el-button
            v-hasPermi="['crm:invoice:delete']"
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

  <InvoiceForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as InvoiceApi from '@/api/crm/invoice'
import InvoiceForm from './InvoiceForm.vue'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmInvoice' })

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  orderNo: undefined,
  type: undefined,
  invoiceDate: undefined
})
const queryFormRef = ref()
const exportLoading = ref(false)

const getList = async () => {
  loading.value = true
  try {
    const data = await InvoiceApi.getInvoicePage(queryParams)
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
    await InvoiceApi.deleteInvoice(id)
    message.success('删除成功')
    await getList()
  } catch {}
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmInvoiceDetail', params: { id } })
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await InvoiceApi.exportInvoice(queryParams)
    download.excel(data, '发票管理.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  await getList()
})
</script>
