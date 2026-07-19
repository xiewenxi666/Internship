<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.loan.amount')" prop="amount">
        <el-input-number v-model="formData.amount" :min="0" :precision="2" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.loan.purpose')" prop="purpose">
        <el-input v-model="formData.purpose" :placeholder="t('oa.loan.purposePlaceholder')" type="textarea" />
      </el-form-item>
      <el-form-item :label="t('oa.loan.repaymentPlan')" prop="repaymentPlan">
        <el-input v-model="formData.repaymentPlan" :placeholder="t('oa.loan.repaymentPlanPlaceholder')" type="textarea" />
      </el-form-item>
      <el-form-item :label="t('oa.loan.expectedRepaymentTime')" prop="expectedRepaymentTime">
        <el-date-picker v-model="formData.expectedRepaymentTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" @click="submitForm('save')">{{ t('common.ok') }}</el-button>
      <el-button :disabled="formLoading" type="primary" @click="submitForm('submit')"
        v-if="formType === 'create'" v-hasPermi="['oa:loan:submit']">{{ t('common.submit') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as LoanApi from '@/api/oa/loan'

defineOptions({ name: 'OaLoanForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, amount: undefined, purpose: '', repaymentPlan: '', expectedRepaymentTime: undefined
})
const formRules = reactive({
  amount: [{ required: true, message: t('oa.loan.amount') + t('common.notEmpty'), trigger: 'blur' }],
  purpose: [{ required: true, message: t('oa.loan.purpose') + t('common.notEmpty'), trigger: 'blur' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await LoanApi.getLoan(id) }
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
    const data = formData.value as unknown as LoanApi.LoanVO
    if (formType.value === 'create') {
      const id = await LoanApi.createLoan(data)
      if (action === 'submit') {
        await LoanApi.submitLoan(id)
        message.success(t('common.submitSuccess'))
      } else {
        message.success(t('common.createSuccess'))
      }
    } else {
      await LoanApi.updateLoan(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, amount: undefined, purpose: '', repaymentPlan: '', expectedRepaymentTime: undefined }
  formRef.value?.resetFields()
}
</script>
