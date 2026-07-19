<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.workReport.type')" prop="type">
        <el-select v-model="formData.type" clearable :placeholder="t('oa.workReport.typePlaceholder')">
          <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_WORK_REPORT_TYPE)"
            :key="parseInt(dict.value as any)" :label="dict.label" :value="parseInt(dict.value as any)" />
        </el-select>
      </el-form-item>
      <el-form-item :label="t('oa.workReport.title')" prop="title">
        <el-input v-model="formData.title" :placeholder="t('oa.workReport.titlePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.workReport.reportDate')" prop="reportDate">
        <el-date-picker v-model="formData.reportDate" clearable :placeholder="t('common.selectDate')"
          type="date" value-format="YYYY-MM-DD" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.workReport.content')" prop="content">
        <el-input v-model="formData.content" :placeholder="t('oa.workReport.contentPlaceholder')" type="textarea" :rows="4" />
      </el-form-item>
      <el-form-item :label="t('oa.workReport.plan')" prop="plan">
        <el-input v-model="formData.plan" :placeholder="t('oa.workReport.planPlaceholder')" type="textarea" :rows="3" />
      </el-form-item>
      <el-form-item :label="t('oa.workReport.summary')" prop="summary">
        <el-input v-model="formData.summary" :placeholder="t('oa.workReport.summaryPlaceholder')" type="textarea" :rows="3" />
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
import * as WorkReportApi from '@/api/oa/workReport'

defineOptions({ name: 'OaWorkReportForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, type: undefined, title: '', reportDate: '',
  content: '', plan: '', summary: ''
})
const formRules = reactive({
  type: [{ required: true, message: t('oa.workReport.type') + t('common.notEmpty'), trigger: 'change' }],
  title: [{ required: true, message: t('oa.workReport.title') + t('common.notEmpty'), trigger: 'blur' }],
  reportDate: [{ required: true, message: t('oa.workReport.reportDate') + t('common.notEmpty'), trigger: 'change' }],
  content: [{ required: true, message: t('oa.workReport.content') + t('common.notEmpty'), trigger: 'blur' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await WorkReportApi.getWorkReport(id) }
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
    const data = formData.value as unknown as WorkReportApi.WorkReportVO
    if (formType.value === 'create') {
      await WorkReportApi.createWorkReport(data)
      message.success(t('common.createSuccess'))
    } else {
      await WorkReportApi.updateWorkReport(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, type: undefined, title: '', reportDate: '',
    content: '', plan: '', summary: '' }
  formRef.value?.resetFields()
}
</script>
