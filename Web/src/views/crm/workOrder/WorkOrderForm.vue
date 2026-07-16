<!-- -23计算机科学与技术2班-龚小波 -->
<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.title')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('workOrder.titlePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.ownerUserName')" prop="ownerUserId">
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
            >
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.type')" prop="type">
            <el-select v-model="formData.type" class="w-1/1" :placeholder="t('common.selectPlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_WORK_ORDER_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.priority')" prop="priority">
            <el-select v-model="formData.priority" class="w-1/1" :placeholder="t('common.selectPlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_WORK_ORDER_PRIORITY)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.startTime')" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              :placeholder="t('workOrder.startTime')"
              type="date"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('workOrder.endTime')" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              :placeholder="t('workOrder.endTime')"
              type="date"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="24">
          <el-form-item :label="t('workOrder.description')" prop="description">
            <el-input v-model="formData.description" :placeholder="t('workOrder.descPlaceholder')" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('dialog.confirm') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('dialog.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as WorkOrderApi from '@/api/crm/workOrder'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { cloneDeep } from 'lodash-es'

const { t } = useI18n('crm')
const message = useMessage()
const userOptions = ref<UserApi.UserVO[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref<WorkOrderApi.WorkOrderVO>({} as WorkOrderApi.WorkOrderVO)
const formRules = reactive({
  title: [{ required: true, message: t('workOrder.titlePlaceholder'), trigger: 'blur' }],
  type: [{ required: true, message: t('workOrder.type'), trigger: 'change' }],
  priority: [{ required: true, message: t('workOrder.priority'), trigger: 'change' }],
  ownerUserId: [{ required: true, message: t('customer.ownerUserRequired'), trigger: 'change' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type, { scope: 'common' })
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = await WorkOrderApi.getWorkOrder(id)
      formData.value = cloneDeep(data)
    } finally {
      formLoading.value = false
    }
  }
  userOptions.value = await UserApi.getSimpleUserList()
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as unknown as WorkOrderApi.WorkOrderVO
    if (formType.value === 'create') {
      await WorkOrderApi.createWorkOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkOrderApi.updateWorkOrder(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {} as WorkOrderApi.WorkOrderVO
  formRef.value?.resetFields()
}
</script>
