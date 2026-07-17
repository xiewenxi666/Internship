<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="700">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="全天" prop="allDay">
            <el-switch v-model="formData.allDay" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              placeholder="选择开始时间"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="!w-full"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              placeholder="选择结束时间"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="!w-full"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="地点" prop="location">
            <el-input v-model="formData.location" placeholder="请输入地点" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="formData.description" placeholder="请输入描述" type="textarea" :rows="3" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.ok') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as ScheduleApi from '@/api/oa/schedule'

defineOptions({ name: 'OaScheduleForm' })

const { t } = useI18n('bpm')
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  title: '',
  startTime: '',
  endTime: '',
  allDay: false,
  location: '',
  description: ''
})
const formRules = reactive({
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await ScheduleApi.getSchedule(id)
    } finally {
      formLoading.value = false
    }
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
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {
    id: undefined,
    title: '',
    startTime: '',
    endTime: '',
    allDay: false,
    location: '',
    description: ''
  }
  formRef.value?.resetFields()
}
</script>
