<template>
  <ContentWrap>
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="部门" prop="deptId">
            <el-tree-select
              v-model="queryParams.deptId"
              :data="deptList"
              :props="{ label: 'name', value: 'id' }"
              check-strictly
              :render-after-expand="false"
              placeholder="选择部门"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="人员" prop="userId">
            <el-select
              v-model="queryParams.userId"
              clearable
              placeholder="选择人员"
              class="!w-240px"
            >
              <el-option
                v-for="item in userListByDeptId"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              查询
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              重置
            </el-button>
            <el-button :loading="exportLoading" plain type="success" @click="handleExport">
              <Icon class="mr-5px" icon="ep:download" />
              导出
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 汇总 -->
  <el-row :gutter="20" class="mb-20px">
    <el-col :span="8">
      <ContentWrap>
        <el-statistic title="商机数量" :value="summaryData.businessCount" />
      </ContentWrap>
    </el-col>
    <el-col :span="8">
      <ContentWrap>
        <el-statistic title="预计销售金额" :value="summaryData.totalPrice" :precision="2" />
      </ContentWrap>
    </el-col>
    <el-col :span="8">
      <ContentWrap>
        <el-statistic title="概率金额" :value="summaryData.probabilityPrice" :precision="2" />
      </ContentWrap>
    </el-col>
  </el-row>

  <!-- 数据表格 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" label="商机标题" prop="name" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="对应客户" prop="customerName" min-width="120">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openCustomerDetail(scope.row.customerId)">
            {{ scope.row.customerName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="预计签单日期" prop="dealTime" min-width="140" />
      <el-table-column align="center" label="预计销售金额" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.totalPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="销售阶段" prop="statusName" min-width="100" />
      <el-table-column align="center" label="成交机率" min-width="100">
        <template #default="scope">
          {{ scope.row.probability != null ? scope.row.probability + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="概率金额" min-width="120">
        <template #default="scope">
          {{ formatPrice(scope.row.probabilityPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="负责人员" prop="ownerUserName" min-width="100" />
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
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
import { handleTree } from '@/utils/tree'
import {
  FunnelReportSummaryVO,
  getActiveBusinessPage,
  getFunnelReportSummary
} from '@/api/crm/statistics/deal'
import download from '@/utils/download'

defineOptions({ name: 'CrmFunnelReport' })

const message = useMessage()
const { push } = useRouter()

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  deptId: useUserStore().getUser.deptId,
  userId: undefined as number | undefined
})
const queryFormRef = ref()
const loading = ref(true)
const list = ref([])
const total = ref(0)
const exportLoading = ref(false)
const summaryData = ref<FunnelReportSummaryVO>({ businessCount: 0, totalPrice: 0, probabilityPrice: 0 })
const deptList = ref([])
const userList = ref([])

const userListByDeptId = computed(() =>
  queryParams.deptId
    ? userList.value.filter((u: any) => u.deptId === queryParams.deptId)
    : []
)

const formatPrice = (price: number) => {
  if (price == null) return '-'
  return '¥ ' + Number(price).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getList = async () => {
  loading.value = true
  try {
    const data = await getActiveBusinessPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const getSummary = async () => {
  summaryData.value = await getFunnelReportSummary(queryParams)
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
  getSummary()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const openDetail = (id: number) => {
  push({ name: 'CrmBusinessDetail', params: { id } })
}

const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

const handleExport = async () => {
  try {
    exportLoading.value = true
    const data = await getActiveBusinessPage({ ...queryParams, pageNo: 1, pageSize: 9999 })
    download.excel(data.list, '销售漏斗报表.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = await UserApi.getSimpleUserList()
  getList()
  getSummary()
})
</script>
