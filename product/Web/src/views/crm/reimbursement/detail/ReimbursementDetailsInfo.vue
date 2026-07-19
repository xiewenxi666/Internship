<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('customer.basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('reimbursement.no')">{{ reimbursement.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.price')">
            {{ erpPriceInputFormatter(reimbursement.price) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.applyDate')">
            {{ formatDate(reimbursement.applyDate, 'YYYY-MM-DD') }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('reimbursement.remark')">{{ reimbursement.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="expenseInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('reimbursement.expenseInfo') }}</span>
        </template>
        <el-table
          v-if="reimbursement.expenses && reimbursement.expenses.length"
          :data="reimbursement.expenses"
          :show-overflow-tooltip="true"
          :stripe="true"
          :table-layout="'auto'"
          border
        >
          <el-table-column align="center" :label="t('reimbursement.expenseNo')" prop="no" min-width="150" />
          <el-table-column align="center" :label="t('reimbursement.expenseContent')" prop="content" min-width="180" />
          <el-table-column align="center" :label="t('reimbursement.expenseType')" prop="type" min-width="130">
            <template #default="scope">
              <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="scope.row.type" />
            </template>
          </el-table-column>
          <el-table-column align="center" :label="t('reimbursement.expenseStatus')" min-width="100">
            <template #default="{ row }">
              <dict-tag :type="DICT_TYPE.CRM_EXPENSE_REIMBURSE_STATUS" :value="row.reimburseStatus" />
            </template>
          </el-table-column>
          <el-table-column align="center" :label="t('reimbursement.expensePrice') + '（元）'" min-width="130">
            <template #default="scope">
              {{ erpPriceInputFormatter(scope.row.price) }}
            </template>
          </el-table-column>
          <el-table-column align="center" :label="t('reimbursement.expenseApplyDate')" min-width="140">
            <template #default="scope">
              {{ formatDate(scope.row.applyDate, 'YYYY-MM-DD') }}
            </template>
          </el-table-column>
        </el-table>
        <el-empty v-else :description="t('reimbursement.noExpense')" />
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

const activeNames = ref(['basicInfo', 'expenseInfo', 'systemInfo'])
</script>
