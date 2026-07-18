<template>
  <ContentWrap>
    <div class="analysis-header">
      <h2>营销分析仪表盘</h2>
      <el-button @click="loadStats" :loading="loading"><Icon icon="ep:refresh" class="mr-5px" />刷新数据</el-button>
    </div>

    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <div class="stat-card blue">
          <div class="stat-icon"><Icon icon="ep:message" :size="32" /></div>
          <div class="stat-value">{{ stats.totalSent }}</div>
          <div class="stat-label">发送总数</div>
          <div class="stat-desc">短信+邮件群发总量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card green">
          <div class="stat-icon"><Icon icon="ep:circle-check-filled" :size="32" /></div>
          <div class="stat-value">{{ stats.totalDelivered }}</div>
          <div class="stat-label">成功到达</div>
          <div class="stat-desc">已确认送达数量</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card orange">
          <div class="stat-icon"><Icon icon="ep:odometer" :size="32" /></div>
          <div class="stat-value">{{ stats.deliveryRate }}%</div>
          <div class="stat-label">到达率</div>
          <div class="stat-desc">成功到达/发送总数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card purple">
          <div class="stat-icon"><Icon icon="ep:trend-charts" :size="32" /></div>
          <div class="stat-value">{{ stats.totalConverted }}</div>
          <div class="stat-label">转化线索</div>
          <div class="stat-desc">群发带来的转化数</div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="16" class="mt-20px">
      <el-col :span="16">
        <ContentWrap title="月度发送趋势" class="no-padding">
          <div class="bar-chart">
            <div v-if="stats.monthlyData.length === 0" class="empty-hint">暂无数据，完成群发后将自动生成月度统计</div>
            <div v-for="(m, i) in stats.monthlyData" :key="i" class="bar-row">
              <span class="bar-label">{{ m.month }}</span>
              <div class="bar-track">
                <div class="bar sent" :style="{ width: barWidth(m.sent, 'sent') + '%' }">
                  <span class="bar-num">{{ m.sent }}</span>
                </div>
              </div>
              <div class="bar-track">
                <div class="bar delivered" :style="{ width: barWidth(m.delivered, 'sent') + '%' }">
                  <span class="bar-num">{{ m.delivered }}</span>
                </div>
              </div>
              <span class="bar-rate">{{ m.deliveryRate }}%</span>
            </div>
            <div class="bar-legend">
              <span class="legend-item"><span class="dot sent-dot"></span> 发送数</span>
              <span class="legend-item"><span class="dot delivered-dot"></span> 到达数</span>
            </div>
          </div>
        </ContentWrap>
      </el-col>
      <el-col :span="8">
        <ContentWrap title="本月概览" class="no-padding">
          <div class="overview-cards">
            <div class="overview-item">
              <span class="overview-label">短信群发</span>
              <span class="overview-value">{{ stats.smsCount || 0 }} 条</span>
            </div>
            <div class="overview-item">
              <span class="overview-label">邮件群发</span>
              <span class="overview-value">{{ stats.emailCount || 0 }} 条</span>
            </div>
            <div class="overview-item">
              <span class="overview-label">本月活动</span>
              <span class="overview-value">{{ stats.campaignCount || 0 }} 个</span>
            </div>
            <div class="overview-item">
              <span class="overview-label">推广产品</span>
              <span class="overview-value">{{ stats.productCount || 0 }} 个</span>
            </div>
            <div class="overview-item">
              <span class="overview-label">完成率</span>
              <span class="overview-value highlight">{{ stats.deliveryRate }}%</span>
            </div>
          </div>
        </ContentWrap>
      </el-col>
    </el-row>

    <ContentWrap title="详细记录" class="mt-20px">
      <el-table v-loading="loading" :data="stats.monthlyData" :stripe="true" :header-cell-style="{background:'#f5f7fa',color:'#333'}">
        <el-table-column label="月份" prop="month" align="center" width="120" />
        <el-table-column label="发送数" prop="sent" align="center">
          <template #default="scope">
            <el-tag type="primary" effect="plain">{{ scope.row.sent }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="到达数" prop="delivered" align="center">
          <template #default="scope">
            <el-tag type="success" effect="plain">{{ scope.row.delivered }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="到达率" prop="deliveryRate" align="center">
          <template #default="scope">
            <el-progress :percentage="scope.row.deliveryRate" :stroke-width="8" :color="scope.row.deliveryRate >= 80 ? '#67c23a' : '#e6a23c'" />
          </template>
        </el-table-column>
        <el-table-column label="转化数" prop="converted" align="center">
          <template #default="scope">
            <el-tag type="warning" effect="plain">{{ scope.row.converted }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="转化率" prop="conversionRate" align="center" width="160">
          <template #default="scope">
            <el-progress :percentage="scope.row.conversionRate" :stroke-width="8" color="#409eff" />
          </template>
        </el-table-column>
      </el-table>
    </ContentWrap>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as AnalysisApi from '@/api/crm/analysis'

defineOptions({ name: 'CrmMarketingAnalysis' })

const loading = ref(false)
const stats = ref({ totalSent: 0, totalDelivered: 0, deliveryRate: 0, totalConverted: 0, conversionRate: 0, monthlyData: [], smsCount: 0, emailCount: 0, campaignCount: 0, productCount: 0 })

const barWidth = (val: number, type: string): number => {
  const max = Math.max(...(stats.value.monthlyData as any[]).map((m: any) => m.sent || 0), 1)
  return Math.round((val || 0) / max * 100) || 2
}

const loadStats = async () => {
  loading.value = true
  try { const data = await AnalysisApi.getMarketingStats(); if (data) stats.value = { ...stats.value, ...data } } catch {} finally { loading.value = false }
}

onMounted(() => loadStats())
const route = useRoute()
watch(() => route.path, () => { if (route.path.includes('analysis')) loadStats() })
</script>

<style scoped>
.analysis-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.analysis-header h2 { margin: 0; font-size: 20px; color: #303133; }
.stats-row { margin-bottom: 0; }
.stat-card { border-radius: 12px; padding: 20px; color: #fff; position: relative; overflow: hidden; min-height: 120px; }
.stat-card.blue { background: linear-gradient(135deg, #409eff, #337ecc); }
.stat-card.green { background: linear-gradient(135deg, #67c23a, #529b2e); }
.stat-card.orange { background: linear-gradient(135deg, #e6a23c, #c78c2e); }
.stat-card.purple { background: linear-gradient(135deg, #a855f7, #7c3aed); }
.stat-icon { opacity: 0.3; position: absolute; right: 16px; top: 16px; }
.stat-value { font-size: 32px; font-weight: bold; margin: 8px 0 4px; }
.stat-label { font-size: 14px; opacity: 0.9; }
.stat-desc { font-size: 11px; opacity: 0.7; margin-top: 4px; }
.mt-20px { margin-top: 20px; }

.bar-chart { padding: 16px; }
.empty-hint { text-align: center; color: #909399; padding: 40px 0; font-size: 14px; }
.bar-row { display: flex; align-items: center; margin-bottom: 12px; gap: 10px; }
.bar-label { width: 60px; font-size: 12px; color: #606266; text-align: right; flex-shrink: 0; }
.bar-track { flex: 1; height: 22px; background: #f0f0f0; border-radius: 4px; overflow: hidden; position: relative; }
.bar { height: 100%; border-radius: 4px; display: flex; align-items: center; justify-content: flex-end; min-width: 20px; transition: width 0.6s ease; }
.bar.sent { background: linear-gradient(90deg, #409eff, #66b1ff); }
.bar.delivered { background: linear-gradient(90deg, #67c23a, #95d475); }
.bar-num { font-size: 11px; color: #fff; padding-right: 6px; font-weight: bold; }
.bar-rate { width: 50px; font-size: 12px; color: #67c23a; font-weight: bold; text-align: right; }
.bar-legend { display: flex; gap: 20px; margin-top: 12px; justify-content: center; }
.legend-item { font-size: 12px; color: #909399; display: flex; align-items: center; gap: 4px; }
.dot { width: 10px; height: 10px; border-radius: 50%; display: inline-block; }
.sent-dot { background: #409eff; }
.delivered-dot { background: #67c23a; }

.overview-cards { padding: 8px 0; }
.overview-item { display: flex; justify-content: space-between; padding: 12px 16px; border-bottom: 1px solid #f0f0f0; }
.overview-item:last-child { border-bottom: none; }
.overview-label { font-size: 13px; color: #909399; }
.overview-value { font-size: 14px; color: #303133; font-weight: 600; }
.overview-value.highlight { color: #409eff; font-size: 18px; }
</style>
