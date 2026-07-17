<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ refund.no }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <slot></slot>
      </div>
    </div>
  </div>
  <ContentWrap class="mt-10px">
    <el-descriptions :column="5" direction="vertical">
      <el-descriptions-item label="客户名称">
        {{ refund.customerName }}
      </el-descriptions-item>
      <el-descriptions-item label="合同金额">
        {{ erpPriceInputFormatter(refund.contract?.totalPrice) }}
      </el-descriptions-item>
      <el-descriptions-item label="退款日期">
        {{ formatDate(refund.refundDate) }}
      </el-descriptions-item>
      <el-descriptions-item label="退款金额">
        {{ erpPriceInputFormatter(refund.price) }}
      </el-descriptions-item>
      <el-descriptions-item label="负责人">
        {{ refund.ownerUserName }}
      </el-descriptions-item>
      <el-descriptions-item label="审批状态">
        <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="refund.auditStatus" />
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as RefundApi from '@/api/crm/refund'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const { refund } = defineProps<{ refund: RefundApi.RefundVO }>()
</script>
