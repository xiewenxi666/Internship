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
          <el-form-item label="任务标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入任务标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="优先级" prop="priority">
            <el-select v-model="formData.priority" clearable placeholder="请选择优先级">
              <el-option label="低" :value="1" />
              <el-option label="中" :value="2" />
              <el-option label="高" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('common.status')" prop="status">
            <el-select v-model="formData.status" clearable placeholder="请选择状态">
              <el-option label="待办" :value="1" />
              <el-option label="已完成" :value="2" />
              <el-option label="已取消" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="截止时间" prop="deadline">
            <el-date-picker
              v-model="formData.deadline"
              placeholder="选择截止时间"
              type="datetime"
              value-format="YYYY-MM-DD HH:mm:ss"
              class="!w-full"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="描述" prop="description">
            <el-input v-model="formData.description" placeholder="请输入描述" type="textarea" :rows="4" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="负责人" prop="assigneeId">
            <el-input v-model="formData.assigneeId" placeholder="负责人ID（自动分配）" disabled />
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
import * as TaskApi from '@/api/oa/task'

defineOptions({ name: 'OaTaskForm' })

const { t } = useI18n('bpm')
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  title: '',
  description: '',
  assigneeId: undefined,
  priority: 2,
  status: 1,
  deadline: ''
})
const formRules = reactive({
  title: [{ required: true, message: '请输入任务标题', trigger: 'blur' }],
  priority: [{ required: true, message: '请选择优先级', trigger: 'change' }]
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
      formData.value = await TaskApi.getTask(id)
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
    const data = formData.value as unknown as TaskApi.TaskVO
    if (formType.value === 'create') {
      await TaskApi.createTask(data)
      message.success(t('common.createSuccess'))
    } else {
      await TaskApi.updateTask(data)
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
    description: '',
    assigneeId: undefined,
    priority: 2,
    status: 1,
    deadline: ''
  }
  formRef.value?.resetFields()
}
</script>
