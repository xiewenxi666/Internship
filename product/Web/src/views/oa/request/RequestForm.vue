<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.request.title')" prop="title">
        <el-input v-model="formData.title" :placeholder="t('oa.request.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.request.type')" prop="type">
        <el-select v-model="formData.type" clearable :placeholder="t('oa.request.typePlaceholder')">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_REQUEST_TYPE)"
            :key="parseInt(dict.value as any)" :label="dict.label" :value="parseInt(dict.value as any)" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('oa.request.urgency')" prop="urgency">
        <el-input-number v-model="formData.urgency" :min="0" :max="10" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.request.content')" prop="content">
        <el-input v-model="formData.content" :placeholder="t('oa.request.contentPlaceholder')" type="textarea" :rows="4" />
      </el-form-item>
      <el-form-item :label="t('oa.request.expectedAmount')" prop="expectedAmount">
        <el-input-number v-model="formData.expectedAmount" :min="0" :precision="2" class="!w-full" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" @click="submitForm('save')">{{ t('common.ok') }}</el-button>
      <el-button :disabled="formLoading" type="primary" @click="submitForm('submit')"
        v-if="formType === 'create'" v-hasPermi="['oa:request:submit']">{{ t('common.submit') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as RequestApi from '@/api/oa/request'

defineOptions({ name: 'OaRequestForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, title: '', type: undefined, urgency: 0, content: '', expectedAmount: undefined
})
const formRules = reactive({
  title: [{ required: true, message: t('oa.request.title') + t('common.notEmpty'), trigger: 'blur' }],
  type: [{ required: true, message: t('oa.request.type') + t('common.notEmpty'), trigger: 'change' }],
  content: [{ required: true, message: t('oa.request.content') + t('common.notEmpty'), trigger: 'blur' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await RequestApi.getRequest(id) }
    finally { formLoading.value = false }
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async (action: 'save' | 'submit') => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  try {
    const data = formData.value as unknown as RequestApi.RequestVO
    if (formType.value === 'create') {
      const id = await RequestApi.createRequest(data)
      if (action === 'submit') {
        await RequestApi.submitRequest(id)
        message.success(t('common.submitSuccess'))
      } else {
        message.success(t('common.createSuccess'))
      }
    } else {
      await RequestApi.updateRequest(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, title: '', type: undefined, urgency: 0, content: '', expectedAmount: undefined }
  formRef.value?.resetFields()
}
</script>
