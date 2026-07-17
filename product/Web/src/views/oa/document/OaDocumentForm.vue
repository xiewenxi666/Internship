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
          <el-form-item label="名称" prop="name">
            <el-input v-model="formData.name" placeholder="请输入名称" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="文件夹" prop="isFolder">
            <el-switch v-model="formData.isFolder" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="上级目录" prop="parentId">
            <el-input v-model="formData.parentId" placeholder="上级目录ID（可选）" />
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
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.ok') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as DocumentApi from '@/api/oa/document'

defineOptions({ name: 'OaDocumentForm' })

const { t } = useI18n('bpm')
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  parentId: undefined,
  name: '',
  isFolder: false,
  fileId: undefined,
  fileUrl: '',
  fileSize: undefined,
  description: ''
})
const formRules = reactive({
  name: [{ required: true, message: '请输入名称', trigger: 'blur' }]
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
      formData.value = await DocumentApi.getDocument(id)
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
    const data = formData.value as unknown as DocumentApi.DocumentVO
    if (formType.value === 'create') {
      await DocumentApi.createDocument(data)
      message.success(t('common.createSuccess'))
    } else {
      await DocumentApi.updateDocument(data)
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
    parentId: undefined,
    name: '',
    isFolder: false,
    fileId: undefined,
    fileUrl: '',
    fileSize: undefined,
    description: ''
  }
  formRef.value?.resetFields()
}
</script>
