<template>
  <ContentWrap>
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item :label="'报价单编号'" prop="quotationNo">
            <el-input
              v-model="queryParams.quotationNo"
              class="!w-240px"
              clearable
              :placeholder="'请输入报价单编号'"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="'负责人'" prop="ownerUserId">
            <el-select
              v-model="queryParams.ownerUserId"
              clearable
              filterable
              :placeholder="'选择负责人'"
              class="!w-240px"
            >
              <el-option
                v-for="item in userList"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="'状态'" prop="status">
            <el-select
              v-model="queryParams.status"
              clearable
              :placeholder="'选择状态'"
              class="!w-240px"
            >
              <el-option :label="'草稿'" :value="0" />
              <el-option :label="'待审批'" :value="1" />
              <el-option :label="'已通过'" :value="2" />
              <el-option :label="'已拒绝'" :value="3" />
              <el-option :label="'已作废'" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item :label="'创建时间'" prop="createTimeRange">
            <el-date-picker
              v-model="createTimeRange"
              type="daterange"
              range-separator="-"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="!w-240px"
              @change="handleCreateTimeRangeChange"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              {{ t('common.search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('common.reset') }}
            </el-button>
            <el-button v-hasPermi="['crm:quotation:create']" type="primary" @click="openForm('create')">
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('common.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:quotation:export']"
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
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" :label="'报价单编号'" prop="quotationNo" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openForm('detail', scope.row.id)">
            {{ scope.row.quotationNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="'关联商机'" prop="businessName" min-width="140" />
      <el-table-column align="center" :label="'关联客户'" prop="customerName" min-width="120" />
      <el-table-column align="center" :label="'负责人'" prop="ownerUserName" min-width="100" />
      <el-table-column align="center" :label="'产品合计（元）'" prop="totalAmount" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.totalAmount) }}
        </template>
      </el-table-column>
      <el-table-column align="center" :label="'折后金额（元）'" prop="finalAmount" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.finalAmount) }}
        </template>
      </el-table-column>
      <el-table-column align="center" :label="'状态'" prop="status" min-width="100">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusLabel(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="'创建时间'" prop="createTime" min-width="180" />
      <el-table-column
        align="center"
        fixed="right"
        :label="t('common.action')"
        min-width="320"
      >
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:quotation:query']"
            link
            type="primary"
            @click="openForm('detail', scope.row.id)"
          >
            详情
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['crm:quotation:update']"
            link
            type="warning"
            @click="handleSubmit(scope.row.id)"
          >
            提交审批
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['crm:quotation:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-if="scope.row.status === 0 || scope.row.status === 3"
            v-hasPermi="['crm:quotation:update']"
            link
            type="info"
            @click="handleVoid(scope.row.id)"
          >
            作废
          </el-button>
          <el-button
            v-if="scope.row.status === 0"
            v-hasPermi="['crm:quotation:delete']"
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
          >
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

  <QuotationForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import download from '@/utils/download'
import * as QuotationApi from '@/api/crm/quotation'
import * as UserApi from '@/api/system/user'
import QuotationForm from './QuotationForm.vue'

defineOptions({ name: 'CrmQuotation' })

const message = useMessage()
const { t } = useI18n()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  quotationNo: undefined,
  ownerUserId: undefined,
  status: undefined,
  createTimeStart: undefined,
  createTimeEnd: undefined
})
const queryFormRef = ref()
const exportLoading = ref(false)
const userList = ref([])
const createTimeRange = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await QuotationApi.getQuotationPage(queryParams)
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

const handleCreateTimeRangeChange = (val: [string, string] | null) => {
  if (val) {
    queryParams.createTimeStart = val[0]
    queryParams.createTimeEnd = val[1]
  } else {
    queryParams.createTimeStart = undefined
    queryParams.createTimeEnd = undefined
  }
  handleQuery()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  createTimeRange.value = undefined
  queryParams.createTimeStart = undefined
  queryParams.createTimeEnd = undefined
  handleQuery()
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await QuotationApi.deleteQuotation(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleSubmit = async (id: number) => {
  try {
    await message.confirm('确认提交该报价单进行审批？')
    await QuotationApi.submitQuotation(id)
    message.success('提交成功')
    await getList()
  } catch {}
}

const handleVoid = async (id: number) => {
  try {
    await message.confirm('确认作废该报价单？')
    await QuotationApi.voidQuotation(id)
    message.success('操作成功')
    await getList()
  } catch {}
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await QuotationApi.exportQuotation(queryParams)
    download.excel(data, '报价单.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const formatPrice = (price: number) => {
  if (price == null) return '-'
  return price.toFixed(2)
}

const getStatusType = (status: number) => {
  const map: Record<number, string> = {
    0: 'info',
    1: 'warning',
    2: 'success',
    3: 'danger',
    4: 'info'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status: number) => {
  const map: Record<number, string> = {
    0: '草稿',
    1: '待审批',
    2: '已通过',
    3: '已拒绝',
    4: '已作废'
  }
  return map[status] || '未知'
}

onMounted(async () => {
  userList.value = await UserApi.getSimpleUserList()
  getList()
})
</script>
