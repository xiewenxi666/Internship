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
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 汇总卡片 -->
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

  <!-- 柱状图 -->
  <ContentWrap>
    <el-skeleton :loading="loading" animated>
      <Echart :height="400" :options="echartsOption" />
    </el-skeleton>
  </ContentWrap>

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
import { EChartsOption } from 'echarts'

defineOptions({ name: 'CrmForecastSummary' })

const queryParams = reactive({
  deptId: useUserStore().getUser.deptId,
  userId: undefined,
  year: new Date().getFullYear().toString()
})
const queryFormRef = ref()
const loading = ref(true)
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

const echartsOption = reactive<EChartsOption>({
  grid: {
    left: 30,
    right: 30,
    bottom: 20,
    containLabel: true
  },
  legend: {},
  series: [
    {
      name: '商机数',
      type: 'bar',
      yAxisIndex: 0,
      data: []
    },
    {
      name: '概率加权金额',
      type: 'bar',
      yAxisIndex: 1,
      data: []
    }
  ],
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' }
  },
  yAxis: [
    {
      type: 'value',
      name: '商机数',
      min: 0,
      minInterval: 1
    },
    {
      type: 'value',
      name: '概率加权金额',
      min: 0,
      splitLine: { lineStyle: { type: 'dotted', opacity: 0.7 } }
    }
  ],
  xAxis: {
    type: 'category',
    name: '月份',
    data: []
  }
}) as EChartsOption

const formatPrice = (price: number) => {
  if (price == null) return '-'
  return '¥ ' + Number(price).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getList = async () => {
  loading.value = true
  try {
    summaryData.value = await getForecastSummary(queryParams)
    monthData.value = await getForecastByMonth(queryParams)
    if (echartsOption.xAxis && echartsOption.xAxis['data']) {
      echartsOption.xAxis['data'] = monthData.value.map((s) => s.month)
    }
    if (echartsOption.series && echartsOption.series[0] && echartsOption.series[0]['data']) {
      echartsOption.series[0]['data'] = monthData.value.map((s) => s.businessCount)
    }
    if (echartsOption.series && echartsOption.series[1] && echartsOption.series[1]['data']) {
      echartsOption.series[1]['data'] = monthData.value.map((s) => s.probabilityPrice)
    }
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

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = await UserApi.getSimpleUserList()
  getList()
})
</script>
