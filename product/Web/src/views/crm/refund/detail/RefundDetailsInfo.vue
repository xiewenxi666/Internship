<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">基本信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="退款编号">{{ refund.no }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">
            {{ refund.customerName }}
          </el-descriptions-item>
          <el-descriptions-item label="合同编号">
            {{ refund.contract?.no }}
          </el-descriptions-item>
          <el-descriptions-item label="退款类型">
            <dict-tag :type="DICT_TYPE.CRM_REFUND_TYPE" :value="refund.type" />
          </el-descriptions-item>
          <el-descriptions-item label="退款金额">
            {{ erpPriceInputFormatter(refund.price) }}
          </el-descriptions-item>
          <el-descriptions-item label="退款日期">
            {{ formatDate(refund.refundDate, 'YYYY-MM-DD') }}
          </el-descriptions-item>
          <el-descriptions-item label="退款内容">{{ refund.content }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ refund.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">系统信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="审批状态">
            <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="refund.auditStatus" />
          </el-descriptions-item>
          <el-descriptions-item label="负责人">
            {{ refund.ownerUserName }}
          </el-descriptions-item>
          <el-descriptions-item label="创建人">
            {{ refund.creatorName }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(refund.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新人">
            {{ refund.updater }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(refund.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as RefundApi from '@/api/crm/refund'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

defineOptions({ name: 'RefundDetailsInfo' })

const { t } = useI18n('crm')

const { refund } = defineProps<{
  refund: RefundApi.RefundVO
}>()

const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
