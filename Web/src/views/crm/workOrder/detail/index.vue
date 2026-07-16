<!-- -23计算机科学与技术2班-龚小波 -->
<template>
  <ContentWrap v-loading="loading">
    <div class="flex gap-20px">
      <div>
        <el-tag :type="statusType" size="large">{{ t('workOrder.status') }}：{{ statusName }}</el-tag>
      </div>
      <WorkOrderDetailsHeader :data="formData" />
      <template v-if="canUpdate">
        <div>
          <el-button type="primary" @click="openForm('update', formData.id)">
            <Icon class="mr-5px" icon="ep:edit" />
            {{ t('common.edit') }}
          </el-button>
        </div>
      </template>
    </div>
  </ContentWrap>

  <ContentWrap>
    <el-tabs v-model="activeTab">
      <el-tab-pane :label="t('workOrder.basicInfoTab')" name="info">
        <WorkOrderDetailsInfo :data="formData" />
      </el-tab-pane>
      <el-tab-pane :label="t('workOrder.operateLogTab')" name="log">
        <OperateLogV2 :biz-info="operateLogBizInfo" />
      </el-tab-pane>
      <el-tab-pane :label="t('workOrder.teamMemberTab')" name="permission">
        <PermissionList ref="permissionListRef" :biz-type="bizType" :biz-id="formData.id" :user-id="formData.ownerUserId" />
      </el-tab-pane>
    </el-tabs>
  </ContentWrap>

  <WorkOrderForm ref="formRef" @success="getDetail" />
</template>

<script lang="ts" setup>
import * as WorkOrderApi from '@/api/crm/workOrder'
import WorkOrderForm from '../WorkOrderForm.vue'
import WorkOrderDetailsHeader from './WorkOrderDetailsHeader.vue'
import WorkOrderDetailsInfo from './WorkOrderDetailsInfo.vue'
import OperateLogV2 from '@/views/crm/components/OperateLogV2.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { computed } from 'vue'

defineOptions({ name: 'CrmWorkOrderDetail' })

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const loading = ref(false)
const formData = ref<WorkOrderApi.WorkOrderVO>({} as WorkOrderApi.WorkOrderVO)
const activeTab = ref('info')
const bizType = 'CRM_WORK_ORDER'
const permissionListRef = ref()

const operateLogBizInfo = computed(() => {
  return { type: 'crm_work_order', id: formData.value.id }
})

const canUpdate = computed(() => {
  return permissionListRef.value?.validateWrite?.() ?? true
})

const statusType = computed(() => {
  const map: Record<number, string> = { 1: 'warning', 2: 'primary', 3: 'success', 4: 'danger' }
  return map[formData.value.status] || 'info'
})

const statusName = computed(() => {
  const map: Record<number, string> = { 1: '发起', 2: '处理中', 3: '完结', 4: '退回' }
  return map[formData.value.status] || ''
})

const getDetail = async () => {
  loading.value = true
  try {
    if (!route.params.id) {
      message.warning(t('workOrder.paramError'))
      return
    }
    formData.value = await WorkOrderApi.getWorkOrder(Number(route.params.id))
  } finally {
    loading.value = false
  }
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

onMounted(async () => {
  await getDetail()
})
</script>
