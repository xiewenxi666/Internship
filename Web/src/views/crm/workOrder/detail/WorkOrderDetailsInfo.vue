<!-- -23计算机科学与技术2班-龚小波 -->
<template>
  <el-collapse v-model="activeNames">
    <el-collapse-item :title="t('workOrder.basicInfoTab')" name="basicInfo">
      <el-descriptions :column="3" border>
        <el-descriptions-item :label="t('workOrder.no')" min-width="150">
          {{ data.no }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.title')" min-width="150">
          {{ data.title }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.type')" min-width="150">
          <dict-tag :type="DICT_TYPE.CRM_WORK_ORDER_TYPE" :value="data.type" />
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.priority')" min-width="150">
          <dict-tag :type="DICT_TYPE.CRM_WORK_ORDER_PRIORITY" :value="data.priority" />
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.contractName')" min-width="150">
          {{ data.contractName }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.customerName')" min-width="150">
          {{ data.customerName }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.description')" min-width="150" :span="3">
          {{ data.description || '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.solution')" min-width="150" :span="3">
          {{ data.solution || '-' }}
        </el-descriptions-item>
      </el-descriptions>
    </el-collapse-item>
    <el-collapse-item :title="t('common.systemInfo')" name="systemInfo">
      <el-descriptions :column="3" border>
        <el-descriptions-item :label="t('workOrder.ownerUserName')" min-width="150">
          {{ data.ownerUserName }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.startTime')" min-width="150">
          {{ data.startTime ? dayjs(data.startTime).format('YYYY-MM-DD') : '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('workOrder.endTime')" min-width="150">
          {{ data.endTime ? dayjs(data.endTime).format('YYYY-MM-DD') : '-' }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('common.creator')" min-width="150">
          {{ data.creatorName || data.creator }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('common.createTime')" min-width="150">
          {{ dayjs(data.createTime).format('YYYY-MM-DD HH:mm:ss') }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('common.updateTime')" min-width="150">
          {{ dayjs(data.updateTime).format('YYYY-MM-DD HH:mm:ss') }}
        </el-descriptions-item>
      </el-descriptions>
    </el-collapse-item>
  </el-collapse>
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import dayjs from 'dayjs'

defineProps<{ data: any }>()
const { t } = useI18n('crm')
const activeNames = ref(['basicInfo', 'systemInfo'])
</script>
