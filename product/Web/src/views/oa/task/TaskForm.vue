<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.task.title')" prop="title">
        <el-input v-model="formData.title" :placeholder="t('oa.task.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.task.priority')" prop="priority">
        <el-input-number v-model="formData.priority" :min="0" :max="10" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.task.assigneeUserId')" prop="assigneeUserId">
        <el-input-number v-model="formData.assigneeUserId" :min="0" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.task.description')" prop="description">
        <el-input v-model="formData.description" :placeholder="t('oa.task.descriptionPlaceholder')" type="textarea" />
      </el-form-item>
      <el-form-item :label="t('oa.task.startTime')" prop="startTime">
        <el-date-picker v-model="formData.startTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.task.deadline')" prop="deadline">
        <el-date-picker v-model="formData.deadline" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.task.tags')" prop="tags">
        <el-input v-model="formData.tags" :placeholder="t('oa.task.tagsPlaceholder')" />
      </el-form-item>
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

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, title: '', description: '', priority: undefined,
  assigneeUserId: undefined, startTime: undefined, deadline: undefined,
  tags: '', projectId: undefined, attachment: ''
})
const formRules = reactive({
  title: [{ required: true, message: t('oa.task.title') + t('common.notEmpty'), trigger: 'blur' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await TaskApi.getTask(id) }
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
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, title: '', description: '', priority: undefined,
    assigneeUserId: undefined, startTime: undefined, deadline: undefined,
    tags: '', projectId: undefined, attachment: '' }
  formRef.value?.resetFields()
}
</script>
