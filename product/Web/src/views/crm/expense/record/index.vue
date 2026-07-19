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
          <el-form-item label="费用编号" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              placeholder="请输入费用编号"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="费用类型" prop="type">
            <el-select
              v-model="queryParams.type"
              class="!w-240px"
              clearable
              placeholder="请选择费用类型"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_EXPENSE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="发生时间" prop="applyDate">
            <el-date-picker
              v-model="queryParams.applyDate"
              class="!w-240px"
              placeholder="请选择发生时间"
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
              v-hasPermi="['crm:expense:export']"
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
      <el-table-column align="center" fixed="left" label="费用编号" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="客户名称" prop="customerName" min-width="180" />
      <el-table-column align="center" label="费用类型" prop="type" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="报销状态" prop="reimburseStatus" min-width="110">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_EXPENSE_REIMBURSE_STATUS" :value="scope.row.reimburseStatus" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="费用金额（元）"
        prop="price"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        label="发生时间"
        prop="applyDate"
        min-width="150"
      />
      <el-table-column align="center" label="负责人" prop="ownerUserName" min-width="120" />
      <el-table-column align="center" fixed="right" label="操作" min-width="100">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:expense:query']"
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
import { dateFormatter2 } from '@/utils/formatTime'
import { erpPriceTableColumnFormatter } from '@/utils'
import download from '@/utils/download'
import * as ExpenseApi from '@/api/crm/expense'

defineOptions({ name: 'CrmExpenseRecord' })

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const exportLoading = ref(false)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  no: undefined,
  type: undefined,
  applyDate: undefined
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await ExpenseApi.getExpensePage(queryParams)
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
    const data = await ExpenseApi.exportExpense(queryParams)
    download.excel(data, '费用记录.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmExpenseDetail', params: { id } })
}

onMounted(async () => {
  await getList()
})
</script>
