<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="600">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="100px">
      <el-form-item :label="t('oa.businessTrip.destination')" prop="destination">
        <el-input v-model="formData.destination" :placeholder="t('oa.businessTrip.destinationPlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.businessTrip.reason')" prop="reason">
        <el-input v-model="formData.reason" :placeholder="t('oa.businessTrip.reasonPlaceholder')" type="textarea" />
      </el-form-item>
      <el-form-item :label="t('oa.businessTrip.companion')" prop="companion">
        <el-input v-model="formData.companion" :placeholder="t('oa.businessTrip.companionPlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.businessTrip.vehicle')" prop="vehicle">
        <el-input v-model="formData.vehicle" :placeholder="t('oa.businessTrip.vehiclePlaceholder')" />
      </el-form-item>
      <el-form-item :label="t('oa.businessTrip.estimatedAmount')" prop="estimatedAmount">
        <el-input-number v-model="formData.estimatedAmount" :min="0" :precision="2" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.businessTrip.startTime')" prop="startTime">
        <el-date-picker v-model="formData.startTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
      <el-form-item :label="t('oa.businessTrip.endTime')" prop="endTime">
        <el-date-picker v-model="formData.endTime" clearable :placeholder="t('common.selectTime')"
          type="datetime" value-format="x" class="!w-full" />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" @click="submitForm('save')">{{ t('common.ok') }}</el-button>
      <el-button :disabled="formLoading" type="primary" @click="submitForm('submit')"
        v-if="formType === 'create'" v-hasPermi="['oa:business-trip:submit']">{{ t('common.submit') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as BusinessTripApi from '@/api/oa/businessTrip'

defineOptions({ name: 'OaBusinessTripForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined, destination: '', reason: '', companion: '', vehicle: '',
  estimatedAmount: undefined, startTime: undefined, endTime: undefined
})
const formRules = reactive({
  destination: [{ required: true, message: t('oa.businessTrip.destination') + t('common.notEmpty'), trigger: 'blur' }],
  reason: [{ required: true, message: t('oa.businessTrip.reason') + t('common.notEmpty'), trigger: 'blur' }],
  startTime: [{ required: true, message: t('oa.businessTrip.startTime') + t('common.notEmpty'), trigger: 'change' }],
  endTime: [{ required: true, message: t('oa.businessTrip.endTime') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try { formData.value = await BusinessTripApi.getBusinessTrip(id) }
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
    const data = formData.value as unknown as BusinessTripApi.BusinessTripVO
    if (formType.value === 'create') {
      const id = await BusinessTripApi.createBusinessTrip(data)
      if (action === 'submit') {
        await BusinessTripApi.submitBusinessTrip(id)
        message.success(t('common.submitSuccess'))
      } else {
        message.success(t('common.createSuccess'))
      }
    } else {
      await BusinessTripApi.updateBusinessTrip(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally { formLoading.value = false }
}

const resetForm = () => {
  formData.value = { id: undefined, destination: '', reason: '', companion: '', vehicle: '',
    estimatedAmount: undefined, startTime: undefined, endTime: undefined }
  formRef.value?.resetFields()
}
</script>
