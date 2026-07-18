<template>
  <ContentWrap title="发送分析">
    <div class="mb-10px">
      <el-button @click="loadStats" :loading="loading"><Icon icon="ep:refresh" class="mr-5px" />刷新数据</el-button>
    </div>
    <el-row :gutter="20" class="mb-20px">
      <el-col :span="6">
        <el-statistic title="发送总数" :value="stats.totalSent">
          <template #prefix><Icon icon="ep:message" /></template>
        </el-statistic>
      </el-col>
      <el-col :span="6">
        <el-statistic title="到达数" :value="stats.totalDelivered">
          <template #prefix><Icon icon="ep:success-filled" color="green" /></template>
        </el-statistic>
      </el-col>
      <el-col :span="6">
        <el-statistic title="到达率" :value="stats.deliveryRate + '%'">
          <template #prefix><Icon icon="ep:data-line" color="blue" /></template>
        </el-statistic>
      </el-col>
      <el-col :span="6">
        <el-statistic title="转化线索数" :value="stats.totalConverted">
          <template #prefix><Icon icon="ep:trend-charts" color="orange" /></template>
        </el-statistic>
      </el-col>
    </el-row>

    <ContentWrap title="月度统计">
      <el-table v-loading="loading" :data="stats.monthlyData" :stripe="true">
        <el-table-column label="月份" prop="month" align="center" />
        <el-table-column label="发送数" prop="sent" align="center" />
        <el-table-column label="到达数" prop="delivered" align="center" />
        <el-table-column label="到达率" prop="deliveryRate" align="center">
          <template #default="scope">{{ scope.row.deliveryRate }}%</template>
        </el-table-column>
        <el-table-column label="转化数" prop="converted" align="center" />
        <el-table-column label="转化率" prop="conversionRate" align="center">
          <template #default="scope">{{ scope.row.conversionRate }}%</template>
        </el-table-column>
      </el-table>
    </ContentWrap>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as AnalysisApi from '@/api/crm/analysis'

defineOptions({ name: 'CrmMarketingAnalysis' })

const loading = ref(false)
const stats = ref({ totalSent: 0, totalDelivered: 0, deliveryRate: 0, totalConverted: 0, conversionRate: 0, monthlyData: [] })

const loadStats = async () => {
  loading.value = true
  try { const data = await AnalysisApi.getMarketingStats(); if (data) stats.value = data } catch {} finally { loading.value = false }
}

onMounted(() => loadStats())
const route = useRoute()
watch(() => route.path, () => { if (route.path.includes('analysis')) loadStats() })
</script>
