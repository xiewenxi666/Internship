<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="700">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('oa.visit.customerName')" prop="customerName">
            <el-input v-model="formData.customerName" :placeholder="t('oa.visit.customerNamePlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('oa.visit.crmCustomerId')" prop="crmCustomerId">
            <el-input-number v-model="formData.crmCustomerId" :min="0" class="!w-full" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('oa.visit.contactPerson')" prop="contactPerson">
            <el-input v-model="formData.contactPerson" :placeholder="t('oa.visit.contactPersonPlaceholder')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('oa.visit.contactPhone')" prop="contactPhone">
            <el-input v-model="formData.contactPhone" :placeholder="t('oa.visit.contactPhonePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item :label="t('oa.visit.visitAddress')" prop="visitAddress">
        <el-input v-model="formData.visitAddress" :placeholder="t('oa.visit.visitAddressPlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.visit.visitTime')" prop="visitTime">
        <el-date-picker v-model="formData.visitTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.visit.purpose')" prop="purpose">
        <el-input v-model="formData.purpose" :placeholder="t('oa.visit.purposePlaceholder')" type="textarea" />
      </el-form-item>
      <el-form-item :label="t('oa.visit.result')" prop="result">
        <el-input v-model="formData.result" :placeholder="t('oa.visit.resultPlaceholder')" type="textarea" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" @click="submitForm('save')">{{ t('common.ok') }}</el-button>
      <el-button :disabled="formLoading" type="primary" @click="submitForm('submit')"
        v-if="formType === 'create'" v-hasPermi="['oa:visit:submit']">{{ t('common.submit') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as VisitApi from '@/api/oa/visit'

defineOptions({ name: 'OaVisitForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, customerName: '', crmCustomerId: undefined, contactPerson: '', contactPhone: '',
  visitAddress: '', visitTime: undefined, purpose: '', result: ''
})
const formRules = reactive({
  customerName: [{ required: true, message: t('oa.visit.customerName') + t('common.notEmpty'), trigger: 'blur' }],
  purpose: [{ required: true, message: t('oa.visit.purpose') + t('common.notEmpty'), trigger: 'blur' }],
  visitTime: [{ required: true, message: t('oa.visit.visitTime') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await VisitApi.getVisit(id) }
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
    const data = formData.value as unknown as VisitApi.VisitVO
    if (formType.value === 'create') {
      const id = await VisitApi.createVisit(data)
      if (action === 'submit') {
        await VisitApi.submitVisit(id)
        message.success(t('common.submitSuccess'))
      } else {
        message.success(t('common.createSuccess'))
      }
    } else {
      await VisitApi.updateVisit(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, customerName: '', crmCustomerId: undefined, contactPerson: '', contactPhone: '',
    visitAddress: '', visitTime: undefined, purpose: '', result: '' }
  formRef.value?.resetFields()
}
</script>
