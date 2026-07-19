<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.schedule.title')" prop="title">
        <el-input v-model="formData.title" :placeholder="t('oa.schedule.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.schedule.type')" prop="type">
        <el-select v-model="formData.type" clearable :placeholder="t('oa.schedule.typePlaceholder')">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_SCHEDULE_TYPE)"
            :key="parseInt(dict.value as any)" :label="dict.label" :value="parseInt(dict.value as any)" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('oa.schedule.location')" prop="location">
        <el-input v-model="formData.location" :placeholder="t('oa.schedule.locationPlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.schedule.startTime')" prop="startTime">
        <el-date-picker v-model="formData.startTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.schedule.endTime')" prop="endTime">
        <el-date-picker v-model="formData.endTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.schedule.description')" prop="description">
        <el-input v-model="formData.description" :placeholder="t('oa.schedule.descriptionPlaceholder')" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.ok') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as ScheduleApi from '@/api/oa/schedule'

defineOptions({ name: 'OaScheduleForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, title: '', type: undefined, location: '',
  startTime: undefined, endTime: undefined, description: '',
  isAllDay: false, priority: undefined, reminderTime: undefined, color: ''
})
const formRules = reactive({
  title: [{ required: true, message: t('oa.schedule.title') + t('common.notEmpty'), trigger: 'blur' }],
  startTime: [{ required: true, message: t('oa.schedule.startTime') + t('common.notEmpty'), trigger: 'change' }],
  endTime: [{ required: true, message: t('oa.schedule.endTime') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await ScheduleApi.getSchedule(id) }
    finally { formLoading.value = false }
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
    const data = formData.value as unknown as ScheduleApi.ScheduleVO
    if (formType.value === 'create') {
      await ScheduleApi.createSchedule(data)
      message.success(t('common.createSuccess'))
    } else {
      await ScheduleApi.updateSchedule(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, title: '', type: undefined, location: '',
    startTime: undefined, endTime: undefined, description: '',
    isAllDay: false, priority: undefined, reminderTime: undefined, color: '' }
  formRef.value?.resetFields()
}
</script>
