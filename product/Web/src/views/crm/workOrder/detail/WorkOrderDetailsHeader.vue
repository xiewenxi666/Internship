<!-- -23计算机科学与技术2班-龚小波 -->
<template>
  <div class="flex flex-1 flex-wrap gap-x-30px gap-y-10px">
    <el-descriptions :column="1" size="small">
      <el-descriptions-item :label="t('workOrder.no')">
        <span class="text-16px font-bold">{{ data.no }}</span>
      </el-descriptions-item>
      <el-descriptions-item :label="t('workOrder.title')">
        {{ data.title }}
      </el-descriptions-item>
    </el-descriptions>
    <el-descriptions :column="1" size="small">
      <el-descriptions-item :label="t('workOrder.priority')">
        <dict-tag :type="DICT_TYPE.CRM_WORK_ORDER_PRIORITY" :value="data.priority" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('workOrder.ownerUserName')">
        {{ data.ownerUserName }}
      </el-descriptions-item>
    </el-descriptions>
  </div>
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'

defineProps<{ data: any }>()
const { t } = useI18n('crm')
</script>
