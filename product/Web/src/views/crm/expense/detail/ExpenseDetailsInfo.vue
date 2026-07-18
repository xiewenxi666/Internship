<template>
  <ContentWrap>
    <el-descriptions :column="5" direction="vertical">
      <el-descriptions-item label="费用编号">
        {{ expense.no }}
      </el-descriptions-item>
      <el-descriptions-item label="客户名称">
        {{ expense.customerName }}
      </el-descriptions-item>
      <el-descriptions-item label="负责人员">
        {{ expense.ownerUserName }}
      </el-descriptions-item>
      <el-descriptions-item label="费用类型">
        <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="expense.type" />
      </el-descriptions-item>
      <el-descriptions-item label="报销状态">
        <dict-tag :type="DICT_TYPE.CRM_EXPENSE_REIMBURSE_STATUS" :value="expense.reimburseStatus" />
      </el-descriptions-item>
      <el-descriptions-item label="费用金额">
        {{ erpPriceInputFormatter(expense.price) }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>

  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="basicInfo">
        <template #title>
          <span class="text-base font-bold">基本信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="费用编号">{{ expense.no }}</el-descriptions-item>
          <el-descriptions-item label="客户名称">{{ expense.customerName }}</el-descriptions-item>
          <el-descriptions-item label="负责人员">{{ expense.ownerUserName }}</el-descriptions-item>
          <el-descriptions-item label="费用类型">
            <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="expense.type" />
          </el-descriptions-item>
          <el-descriptions-item label="费用金额">{{ erpPriceInputFormatter(expense.price) }}</el-descriptions-item>
          <el-descriptions-item label="发生时间">{{ formatDate(expense.applyDate, 'YYYY-MM-DD') }}</el-descriptions-item>
          <el-descriptions-item label="费用内容">{{ expense.content }}</el-descriptions-item>
          <el-descriptions-item label="备注">{{ expense.remark }}</el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">系统信息</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item label="负责人">
            {{ expense.ownerUserName }}
          </el-descriptions-item>
          <el-descriptions-item label="创建人">
            {{ expense.creatorName }}
          </el-descriptions-item>
          <el-descriptions-item label="创建时间">
            {{ formatDate(expense.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="更新时间">
            {{ formatDate(expense.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as ExpenseApi from '@/api/crm/expense'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

defineOptions({ name: 'ExpenseDetailsInfo' })

const props = defineProps<{
  expense: ExpenseApi.ExpenseVO
}>()

const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
