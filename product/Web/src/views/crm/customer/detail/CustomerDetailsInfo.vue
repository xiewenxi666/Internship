<template>
  <ContentWrap>
    <el-collapse v-model="activeNames" class="">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('name')">
            {{ customer.name }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('source')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_SOURCE" :value="customer.source" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('mobile')">{{ customer.mobile }}</el-descriptions-item>
          <el-descriptions-item :label="t('telephone')">{{ customer.telephone }}</el-descriptions-item>
          <el-descriptions-item :label="t('email')">{{ customer.email }}</el-descriptions-item>
          <el-descriptions-item :label="t('areaId')">
            {{ customer.areaName }} {{ customer.detailAddress }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('qq')">{{ customer.qq }}</el-descriptions-item>
          <el-descriptions-item :label="t('wechat')">{{ customer.wechat }}</el-descriptions-item>
          <el-descriptions-item :label="t('industryId')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_INDUSTRY" :value="customer.industryId" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('level')">
            <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="customer.level" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('contactNextTime')">
            {{ formatDate(customer.contactNextTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('remark')">{{ customer.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('common.systemInfo') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('ownerUserId')">{{ customer.ownerUserName }}</el-descriptions-item>
          <el-descriptions-item :label="t('lastContactContent')">
            {{ customer.contactLastContent }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('lastContactTime')">
            {{ formatDate(customer.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="''">&nbsp;</el-descriptions-item>
          <el-descriptions-item :label="t('common.creator')">{{ customer.creatorName }}</el-descriptions-item>
          <el-descriptions-item :label="t('common.createTime')">
            {{ formatDate(customer.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('common.updateTime')">
            {{ formatDate(customer.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="tradeOverview">
        <template #title><span class="text-base font-bold">交易概览</span></template>
        <el-row :gutter="20">
          <el-col :span="6"><div class="trade-stat-card"><div class="trade-stat-value" style="color:#409EFF">{{ tradeStats.totalAmount }}</div><div class="trade-stat-label">累计消费(元)</div></div></el-col>
          <el-col :span="6"><div class="trade-stat-card"><div class="trade-stat-value" style="color:#67C23A">{{ tradeStats.firstDealDate || '-' }}</div><div class="trade-stat-label">首次成交</div></div></el-col>
          <el-col :span="6"><div class="trade-stat-card"><div class="trade-stat-value" style="color:#E6A23C">{{ tradeStats.lastDealDate || '-' }}</div><div class="trade-stat-label">最近交易</div></div></el-col>
          <el-col :span="6"><div class="trade-stat-card"><div class="trade-stat-value" style="color:#F56C6C">{{ tradeStats.unsettledCount }}</div><div class="trade-stat-label">未结合同</div></div></el-col>
        </el-row>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as CustomerApi from '@/api/crm/customer'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { reactive, watch } from 'vue'

defineOptions({ name: 'CrmCustomerDetailsInfo' })
const { t } = useI18n('crm.customer') // 国际化
const { customer } = defineProps<{
  customer: CustomerApi.CustomerVO // 客户明细
}>()

const activeNames = ref(['basicInfo', 'systemInfo', 'tradeOverview']) // 展示的折叠面板

const tradeStats = reactive({
  totalAmount: 0, firstDealDate: '', lastDealDate: '', unsettledCount: 0
})
const loadTradeStats = async () => {
  if (!customer?.id) return
  try {
    const { getReceivablePageByCustomer } = await import('@/api/crm/receivable')
    const rData = await getReceivablePageByCustomer({ customerId: customer.id, pageSize: 100 })
    if (rData.list?.length) {
      tradeStats.totalAmount = rData.list.reduce((s, r) => s + (r.price || 0), 0)
      tradeStats.firstDealDate = rData.list[rData.list.length-1]?.returnTime ? new Date(rData.list[rData.list.length-1].returnTime).toISOString().slice(0,10) : '-'
      tradeStats.lastDealDate = rData.list[0]?.returnTime ? new Date(rData.list[0].returnTime).toISOString().slice(0,10) : '-'
    }
    const { getContractPageByCustomer } = await import('@/api/crm/contract')
    const cData = await getContractPageByCustomer({ customerId: customer.id, pageSize: 1 })
    tradeStats.unsettledCount = cData.total || 0
  } catch {}
}
watch(() => customer?.id, (val) => {
  if (val) loadTradeStats()
})

</script>
<style lang="scss" scoped>
.trade-stat-card { background: var(--el-bg-color-overlay); border-radius: 8px; padding: 16px; text-align: center; border: 1px solid var(--el-border-color-light); }
.trade-stat-value { font-size: 24px; font-weight: bold; }
.trade-stat-label { font-size: 13px; color: #909399; margin-top: 6px; }
</style>