<template>
  <Dialog :title="dialogTitle" v-model="dialogVisible" width="1280">
    <el-form
      ref="formRef"
      :model="formData"
      :rules="formRules"
      label-width="auto"
      v-loading="formLoading"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="'报价单编号'" prop="quotationNo">
            <el-input v-model="formData.quotationNo" :placeholder="formType === 'create' ? '系统自动生成' : ''" :disabled="true" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="'负责人'" prop="ownerUserId">
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
            >
              <el-option
                v-for="item in userOptions"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="'关联商机'" prop="businessId">
            <el-select
              v-model="formData.businessId"
              filterable
              :placeholder="'请选择关联商机'"
              class="w-1/1"
              :disabled="formType !== 'create'"
              @change="onChangeBusiness"
            >
              <el-option
                v-for="item in businessList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="'关联客户'">
            <el-input v-model="formData.customerName" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="'关联联系人'">
            <el-input v-model="formData.contactName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="'折扣比例（%）'" prop="discountPercent">
            <el-input-number
              v-model="formData.discountPercent"
              controls-position="right"
              :min="0"
              :max="100"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="'报价有效天数'" prop="validDays">
            <el-input-number
              v-model="formData.validDays"
              controls-position="right"
              :min="1"
              :precision="0"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12" v-if="formType === 'detail'">
          <el-form-item :label="'过期时间'">
            <el-input v-model="formData.expireTime" disabled />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="'备注'" prop="remark">
            <el-input type="textarea" v-model="formData.remark" :placeholder="'请输入备注'" />
          </el-form-item>
        </el-col>
      </el-row>
      <!-- 产品子表 -->
      <ContentWrap>
        <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
          <el-tab-pane :label="'报价产品'" name="product">
            <QuotationProductForm
              ref="productFormRef"
              :products="formData.products"
              :disabled="disabled"
            />
          </el-tab-pane>
        </el-tabs>
      </ContentWrap>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="'产品合计（元）'">
            <el-input disabled v-model="formData.totalAmount" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="'折扣比例（%）'">
            <el-input disabled v-model="formData.discountPercent" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="'折后金额（元）'">
            <el-input disabled v-model="formData.finalAmount" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button v-if="formType !== 'detail'" @click="submitForm" type="primary" :disabled="formLoading">{{ t('common.confirm') }}</el-button>
      <el-button @click="dialogVisible = false">{{ formType === 'detail' ? t('common.close') : t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script setup lang="ts">
import * as QuotationApi from '@/api/crm/quotation'
import * as BusinessApi from '@/api/crm/business'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
import QuotationProductForm from './components/QuotationProductForm.vue'
import { erpPriceMultiply, erpPriceInputFormatter } from '@/utils'

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const disabled = ref(false)
const formData = ref({
  id: undefined,
  quotationNo: undefined,
  businessId: undefined,
  customerId: undefined,
  customerName: undefined,
  contactId: undefined,
  contactName: undefined,
  ownerUserId: undefined,
  status: 0,
  totalAmount: 0,
  discountPercent: 100,
  finalAmount: 0,
  validDays: 30,
  expireTime: undefined,
  remark: undefined,
  products: []
})
const formRules = reactive({
  businessId: [{ required: true, message: '请选择关联商机', trigger: 'blur' }],
  ownerUserId: [{ required: true, message: '请选择负责人', trigger: 'blur' }]
})
const formRef = ref()
const userOptions = ref<UserApi.UserVO[]>([])
const businessList = ref([])

const subTabsName = ref('product')
const productFormRef = ref()

watch(
  () => formData.value,
  (val) => {
    if (!val) return
    const totalAmount = (val.products || []).reduce(
      (prev, curr) => prev + (curr.totalPrice || 0),
      0
    )
    const discount = val.discountPercent != null ? val.discountPercent : 100
    const finalAmount = erpPriceMultiply(totalAmount, discount / 100.0)
    formData.value.totalAmount = totalAmount
    formData.value.finalAmount = finalAmount
  },
  { deep: true }
)

const onChangeBusiness = (businessId) => {
  const business = businessList.value.find((item) => item.id === businessId)
  if (business) {
    formData.value.customerId = business.customerId
    formData.value.customerName = business.customerName
  }
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = type === 'detail' ? '报价单详情' : t('action.' + type)
  formType.value = type
  disabled.value = type === 'detail'
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = await QuotationApi.getQuotation(id)
      formData.value = data
    } finally {
      formLoading.value = false
    }
  }
  businessList.value = await BusinessApi.getSimpleBusinessList()
  userOptions.value = await UserApi.getSimpleUserList()
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  if (productFormRef.value) {
    await productFormRef.value.validate()
  }
  formLoading.value = true
  try {
    const data = formData.value as unknown as QuotationApi.QuotationVO
    if (formType.value === 'create') {
      await QuotationApi.createQuotation(data)
      message.success(t('common.createSuccess'))
    } else {
      await QuotationApi.updateQuotation(data)
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
    quotationNo: undefined,
    businessId: undefined,
    customerId: undefined,
    customerName: undefined,
    contactId: undefined,
    contactName: undefined,
    ownerUserId: undefined,
    status: 0,
    totalAmount: 0,
    discountPercent: 100,
    finalAmount: 0,
    validDays: 30,
    expireTime: undefined,
    remark: undefined,
    products: []
  }
  formRef.value?.resetFields()
}
</script>
