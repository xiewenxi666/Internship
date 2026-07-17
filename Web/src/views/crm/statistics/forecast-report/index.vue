<template>
  <ContentWrap>
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="年份" prop="year">
            <el-date-picker
              v-model="queryParams.year"
              type="year"
              value-format="YYYY"
              placeholder="选择年份"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
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
        <el-statistic title="活跃商机数" :value="summaryData.businessCount" />
      </ContentWrap>
    </el-col>
    <el-col :span="8">
      <ContentWrap>
        <el-statistic title="商机总报价额" :value="summaryData.totalPrice" :precision="2" />
      </ContentWrap>
    </el-col>
    <el-col :span="8">
      <ContentWrap>
        <el-statistic title="概率加权金额" :value="summaryData.probabilityPrice" :precision="2" />
      </ContentWrap>
    </el-col>
  </el-row>

  <!-- 月度数据表格 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="monthData" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" label="月份" prop="month" min-width="100" />
      <el-table-column align="center" label="活跃商机数" prop="businessCount" min-width="100" />
      <el-table-column align="center" label="商机总报价额" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.totalPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="概率加权金额" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.probabilityPrice) }}
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
import { handleTree } from '@/utils/tree'
import {
  StatisticsForecastSummaryVO,
  StatisticsForecastByMonthVO,
  getForecastSummary,
  getForecastByMonth
} from '@/api/crm/statistics/forecast'
import download from '@/utils/download'

defineOptions({ name: 'CrmForecastReport' })

const queryParams = reactive({
  deptId: useUserStore().getUser.deptId,
  userId: undefined,
  year: new Date().getFullYear().toString()
})
const queryFormRef = ref()
const loading = ref(true)
const exportLoading = ref(false)
const summaryData = ref<StatisticsForecastSummaryVO>({
  businessCount: 0,
  totalPrice: 0,
  probabilityPrice: 0
})
const monthData = ref<StatisticsForecastByMonthVO[]>([])
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
    summaryData.value = await getForecastSummary(queryParams)
    monthData.value = await getForecastByMonth(queryParams)
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  getList()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const handleExport = async () => {
  try {
    exportLoading.value = true
    download.excel(monthData.value, '销售预测报表.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = await UserApi.getSimpleUserList()
  getList()
})
</script>
