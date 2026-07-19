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
          <el-form-item :label="t('statistics.selectYear')" prop="year">
            <el-date-picker
              v-model="queryParams.year"
              class="!w-240px"
              type="year"
              value-format="YYYY"
              placeholder="选择年份"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('statistics.dept')" prop="deptId">
            <el-tree-select
              v-model="queryParams.deptId"
              class="!w-240px"
              :data="deptList"
              :props="defaultProps"
              check-strictly
              node-key="id"
              :placeholder="t('statistics.dept')"
              clearable
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('statistics.user')" prop="ownerUserId">
            <el-select
              v-model="queryParams.ownerUserId"
              class="!w-240px"
              :placeholder="t('statistics.user')"
              clearable
            >
              <el-option
                v-for="user in userList"
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
              {{ t('statistics.search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('statistics.reset') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:receivable-plan:summary:export']"
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
    <el-row :gutter="20">
      <el-col :span="6">
        <SummaryCard
          title="计划回款金额目标"
          :value="summary.targetPrice"
          prefix="¥"
          :decimals="2"
          icon="ep:coin"
        />
      </el-col>
      <el-col :span="6">
        <SummaryCard
          title="已完成金额"
          :value="summary.completedPrice"
          prefix="¥"
          :decimals="2"
          icon="ep:circle-check"
        />
      </el-col>
      <el-col :span="6">
        <SummaryCard
          title="完成率（%）"
          :value="summary.completionRate"
          :decimals="2"
          icon="ep:data-line"
        />
      </el-col>
      <el-col :span="6">
        <SummaryCard
          title="未完成金额"
          :value="summary.uncompletedPrice"
          prefix="¥"
          :decimals="2"
          icon="ep:warning-filled"
        />
      </el-col>
    </el-row>
  </ContentWrap>

  <ContentWrap>
    <div ref="chartRef" style="width: 100%; height: 400px"></div>
  </ContentWrap>

  <ContentWrap>
    <el-table
      v-loading="loading"
      :data="tableData"
      :show-overflow-tooltip="true"
      :stripe="true"
      show-summary
      :summary-method="getSummaries"
    >
      <el-table-column align="center" label="时间" prop="month" />
      <el-table-column align="center" label="目标金额（元）" prop="targetPrice">
        <template #default="scope">
          {{ erpPriceInputFormatter(scope.row.targetPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="已完成金额（元）" prop="completedPrice">
        <template #default="scope">
          {{ erpPriceInputFormatter(scope.row.completedPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="完成率（%）" prop="completionRate">
        <template #default="scope">
          {{ erpPriceInputFormatter(scope.row.completionRate) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="未完成金额（元）" prop="uncompletedPrice">
        <template #default="scope">
          {{ erpPriceInputFormatter(scope.row.uncompletedPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="已开票金额（元）" prop="invoicedPrice">
        <template #default="scope">
          {{ erpPriceInputFormatter(scope.row.invoicedPrice) }}
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as echarts from 'echarts'
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import type { ReceivablePlanSummaryVO } from '@/api/crm/receivable/plan'
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import { defaultProps, handleTree } from '@/utils/tree'
import { erpPriceInputFormatter } from '@/utils'
import download from '@/utils/download'
import SummaryCard from '@/components/SummaryCard/index.vue'

defineOptions({ name: 'CrmReceivablePlanSummary' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(false)
const exportLoading = ref(false)
const tableData = ref<ReceivablePlanSummaryVO[]>([])

const queryParams = reactive({
  year: String(new Date().getFullYear()),
  deptId: undefined as number | undefined,
  ownerUserId: undefined as number | undefined
})
const queryFormRef = ref()

const deptList = ref<Tree[]>([])
const userList = ref<UserApi.UserVO[]>([])

const summary = computed(() => {
  let targetPrice = 0
  let completedPrice = 0
  let uncompletedPrice = 0
  for (const item of tableData.value) {
    targetPrice += item.targetPrice || 0
    completedPrice += item.completedPrice || 0
    uncompletedPrice += item.uncompletedPrice || 0
  }
  const completionRate = targetPrice > 0 ? (completedPrice / targetPrice) * 100 : 0
  return {
    targetPrice,
    completedPrice,
    completionRate: parseFloat(completionRate.toFixed(2)),
    uncompletedPrice
  }
})

const chartRef = ref<HTMLDivElement>()
let chartInstance: echarts.ECharts | null = null

const updateChart = () => {
  if (!chartRef.value) return
  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value)
    window.addEventListener('resize', () => {
      chartInstance?.resize()
    })
  }
  const months = tableData.value.map((item) => item.month)
  const targetData = tableData.value.map((item) => item.targetPrice)
  const completedData = tableData.value.map((item) => item.completedPrice)

  chartInstance.setOption({
    tooltip: {
      trigger: 'axis',
      axisPointer: { type: 'shadow' }
    },
    legend: {
      data: ['目标金额', '已完成金额']
    },
    grid: {
      left: 20,
      right: 20,
      bottom: 20,
      containLabel: true
    },
    xAxis: {
      type: 'category',
      data: months
    },
    yAxis: {
      type: 'value',
      name: '金额（元）'
    },
    series: [
      {
        name: '目标金额',
        type: 'bar',
        data: targetData,
        itemStyle: { color: '#409eff' }
      },
      {
        name: '已完成金额',
        type: 'bar',
        data: completedData,
        itemStyle: { color: '#67c23a' }
      }
    ]
  })
}

const loadData = async () => {
  loading.value = true
  try {
    const data = await ReceivablePlanApi.getReceivablePlanSummary(queryParams)
    tableData.value = (data as any) || []
    await nextTick()
    updateChart()
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  loadData()
}

const resetQuery = () => {
  queryFormRef.value?.resetFields()
  queryParams.year = String(new Date().getFullYear())
  loadData()
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await ReceivablePlanApi.exportReceivablePlan(queryParams)
    download.excel(data, '回款计划汇总.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const getSummaries = (param: any) => {
  const { columns, data } = param
  const sums: string[] = []
  columns.forEach((column: any, index: number) => {
    if (index === 0) {
      sums[index] = '合计'
      return
    }
    const values = data.map((item: any) => Number(item[column.property]))
    if (column.property === 'completionRate') {
      const totalTarget = data.reduce(
        (acc: number, item: any) => acc + Number(item.targetPrice || 0),
        0
      )
      const totalCompleted = data.reduce(
        (acc: number, item: any) => acc + Number(item.completedPrice || 0),
        0
      )
      sums[index] =
        totalTarget > 0
          ? erpPriceInputFormatter((totalCompleted / totalTarget) * 100)
          : erpPriceInputFormatter(0)
    } else {
      const sum = values.reduce(
        (prev: number, curr: number) => prev + (isNaN(curr) ? 0 : curr),
        0
      )
      sums[index] = erpPriceInputFormatter(sum)
    }
  })
  return sums
}

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = await UserApi.getSimpleUserList()
  await loadData()
})

onBeforeUnmount(() => {
  if (chartInstance) {
    chartInstance.dispose()
    chartInstance = null
  }
})
</script>
