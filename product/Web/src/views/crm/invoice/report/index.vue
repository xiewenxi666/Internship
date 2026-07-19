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
          <el-form-item label="部门" prop="deptId">
            <el-tree-select
              v-model="queryParams.deptId"
              class="!w-240px"
              :data="deptList"
              :props="defaultProps"
              check-strictly
              node-key="id"
              placeholder="部门"
              @change="queryParams.ownerUserId = undefined"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="人员" prop="ownerUserId">
            <el-select
              v-model="queryParams.ownerUserId"
              class="!w-240px"
              clearable
              placeholder="人员"
            >
              <el-option
                v-for="user in userListByDeptId"
                :key="user.id"
                :label="user.nickname"
                :value="user.id"
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
              搜索
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              重置
            </el-button>
            <el-button
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
    <el-row :gutter="20" class="mb-15px">
      <el-col :span="6">
        <el-statistic :value="erpPriceInputFormatter(totalPrice)">
          <template #title>
            <span class="text-14px">已开票金额（元）</span>
          </template>
        </el-statistic>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
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
        min-width="160"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" label="发票经手人员" prop="handlerUserName" min-width="130" />
      <el-table-column align="center" label="订单所属人员" prop="ownerUserName" min-width="120" />
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
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import * as InvoiceApi from '@/api/crm/invoice'
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { defaultProps, handleTree } from '@/utils/tree'
import download from '@/utils/download'

defineOptions({ name: 'CrmInvoiceReport' })

const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const totalPrice = ref(0)
const exportLoading = ref(false)

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  deptId: undefined,
  ownerUserId: undefined
})
const queryFormRef = ref()

const deptList = ref<Tree[]>([])
const userList = ref<UserApi.UserVO[]>([])

const userListByDeptId = computed(() =>
  queryParams.deptId
    ? userList.value.filter((u: UserApi.UserVO) => u.deptId === queryParams.deptId)
    : []
)

const getList = async () => {
  loading.value = true
  try {
    const data = await InvoiceApi.getInvoiceReport(queryParams)
    list.value = data.list
    total.value = data.total
    totalPrice.value = (data.list || []).reduce((sum: number, item: any) => sum + (item.price || 0), 0)
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
    download.excel(data, '开票记录报表.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = handleTree(await UserApi.getSimpleUserList())
  await getList()
})
</script>
