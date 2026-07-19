<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
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
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              查询
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              重置
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
            <el-button
              v-hasPermi="['crm:invoice:query']"
              plain
              type="info"
              @click="goToReport"
            >
              <Icon class="mr-5px" icon="ep:document" />
              开票记录报表
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
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
          {{ scope.row.contract?.no ?? scope.row.orderNo }}
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
      <el-table-column align="center" label="发票经手人员" prop="handlerUserName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" label="操作" min-width="100">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:invoice:query']"
            link
            type="primary"
            @click="openDetail(scope.row.id)"
          >
            详情
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
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import { erpPriceTableColumnFormatter } from '@/utils'
import download from '@/utils/download'
import * as InvoiceApi from '@/api/crm/invoice'

defineOptions({ name: 'CrmInvoiceRecord' })

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const exportLoading = ref(false)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  orderNo: undefined,
  type: undefined,
  invoiceDate: undefined
})
const queryFormRef = ref()

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

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await InvoiceApi.exportInvoice(queryParams)
    download.excel(data, '发票记录.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmInvoiceDetail', params: { id } })
}

const goToReport = () => {
  push({ name: 'CrmInvoiceReport' })
}

onMounted(async () => {
  await getList()
})
</script>
