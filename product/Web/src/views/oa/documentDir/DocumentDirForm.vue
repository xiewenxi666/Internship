<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.documentDir.name')" prop="name">
        <el-input v-model="formData.name" :placeholder="t('oa.documentDir.namePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.documentDir.type')" prop="type">
        <el-select v-model="formData.type" clearable :placeholder="t('oa.documentDir.typePlaceholder')">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_DOCUMENT_DIR_TYPE)"
            :key="parseInt(dict.value as any)" :label="dict.label" :value="parseInt(dict.value as any)" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('oa.documentDir.parentId')" prop="parentId">
        <el-input-number v-model="formData.parentId" :min="0" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.documentDir.sort')" prop="sort">
        <el-input-number v-model="formData.sort" :min="0" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.documentDir.description')" prop="description">
        <el-input v-model="formData.description" :placeholder="t('oa.documentDir.descriptionPlaceholder')" type="textarea" />
      </el-form-item>
      <el-form-item :label="t('oa.documentDir.keywords')" prop="keywords">
        <el-input v-model="formData.keywords" :placeholder="t('oa.documentDir.keywordsPlaceholder')" />
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
import * as DocumentDirApi from '@/api/oa/documentDir'

defineOptions({ name: 'OaDocumentDirForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, name: '', type: undefined, parentId: 0,
  sort: 0, description: '', keywords: '', permission: undefined,
  fileUrl: '', fileName: '', fileType: '', fileSize: undefined
})
const formRules = reactive({
  name: [{ required: true, message: t('oa.documentDir.name') + t('common.notEmpty'), trigger: 'blur' }],
  type: [{ required: true, message: t('oa.documentDir.type') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await DocumentDirApi.getDocumentDir(id) }
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
    const data = formData.value as unknown as DocumentDirApi.DocumentDirVO
    if (formType.value === 'create') {
      await DocumentDirApi.createDocumentDir(data)
      message.success(t('common.createSuccess'))
    } else {
      await DocumentDirApi.updateDocumentDir(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, name: '', type: undefined, parentId: 0,
    sort: 0, description: '', keywords: '', permission: undefined,
    fileUrl: '', fileName: '', fileType: '', fileSize: undefined }
  formRef.value?.resetFields()
}
</script>
