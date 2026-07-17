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
          <el-form-item label="报告类型" prop="type">
            <el-select v-model="formData.type" clearable placeholder="请选择报告类型">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.OA_REPORT_TYPE)"
                :key="parseInt(dict.value as any)"
                :label="dict.label"
                :value="parseInt(dict.value as any)"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="报告日期" prop="reportDate">
            <el-date-picker
              v-model="formData.reportDate"
              placeholder="选择日期"
              type="date"
              value-format="YYYY-MM-DD"
              class="!w-full"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="工作内容" prop="content">
            <el-input v-model="formData.content" placeholder="请输入工作内容" type="textarea" :rows="4" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="工作计划" prop="plan">
            <el-input v-model="formData.plan" placeholder="请输入工作计划" type="textarea" :rows="4" />
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
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as ReportApi from '@/api/oa/report'

defineOptions({ name: 'OaReportForm' })

const { t } = useI18n('bpm')
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  type: undefined,
  reportDate: '',
  content: '',
  plan: ''
})
const formRules = reactive({
  type: [{ required: true, message: '请选择报告类型', trigger: 'change' }],
  reportDate: [{ required: true, message: '请选择报告日期', trigger: 'change' }],
  content: [{ required: true, message: '请输入工作内容', trigger: 'blur' }]
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
      formData.value = await ReportApi.getReport(id)
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
    const data = formData.value as unknown as ReportApi.ReportVO
    if (formType.value === 'create') {
      await ReportApi.createReport(data)
      message.success(t('common.createSuccess'))
    } else {
      await ReportApi.updateReport(data)
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
    type: undefined,
    reportDate: '',
    content: '',
    plan: ''
  }
  formRef.value?.resetFields()
}
</script>
