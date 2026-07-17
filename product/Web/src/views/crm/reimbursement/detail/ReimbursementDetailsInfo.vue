<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('customer.basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('reimbursement.no')">{{ reimbursement.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.customerName')">
            {{ reimbursement.customerName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.contractNo')">
            {{ reimbursement.contract?.no }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.type')">
            <dict-tag :type="DICT_TYPE.CRM_REIMBURSEMENT_TYPE" :value="reimbursement.type" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.price')">
            {{ erpPriceInputFormatter(reimbursement.price) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.applyDate')">
            {{ formatDate(reimbursement.applyDate, 'YYYY-MM-DD') }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.content')">{{ reimbursement.content }}</el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.remark')">{{ reimbursement.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('reimbursement.systemInfo') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('reimbursement.auditStatus')">
            <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="reimbursement.auditStatus" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.ownerUserName')">
            {{ reimbursement.ownerUserName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.creatorName')">
            {{ reimbursement.creatorName }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.createTime')">
            {{ formatDate(reimbursement.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.updateTime')">
            {{ formatDate(reimbursement.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as ReimbursementApi from '@/api/crm/reimbursement'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')

const { reimbursement } = defineProps<{
  reimbursement: ReimbursementApi.ReimbursementVO
}>()

const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
