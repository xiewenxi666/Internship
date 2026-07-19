<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.message.receiverUserId')" prop="receiverUserId">
        <el-input-number v-model="formData.receiverUserId" :min="0" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.message.title')" prop="title">
        <el-input v-model="formData.title" :placeholder="t('oa.message.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.message.content')" prop="content">
        <el-input v-model="formData.content" :placeholder="t('oa.message.contentPlaceholder')" type="textarea" :rows="4" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.ok') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as MessageApi from '@/api/oa/message'

defineOptions({ name: 'OaMessageForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, receiverUserId: undefined, title: '', content: ''
})
const formRules = reactive({
  receiverUserId: [{ required: true, message: t('oa.message.receiverUserId') + t('common.notEmpty'), trigger: 'blur' }],
  title: [{ required: true, message: t('oa.message.title') + t('common.notEmpty'), trigger: 'blur' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as unknown as MessageApi.MessageVO
    await MessageApi.sendMessage(data)
    message.success(t('common.createSuccess'))
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, receiverUserId: undefined, title: '', content: '' }
  formRef.value?.resetFields()
}
</script>
