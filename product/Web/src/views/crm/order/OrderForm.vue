<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="1280">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('crm.order.no')" prop="no">
            <el-input disabled v-model="formData.no" :placeholder="t('crm.order.noAutoGenerate')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('crm.order.name')" prop="name">
            <el-input v-model="formData.name" :placeholder="t('crm.order.namePlaceholder')" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('crm.order.ownerUserId')" prop="ownerUserId">
            <el-select
              v-model="formData.ownerUserId"
              :disabled="formType !== 'create'"
              class="w-1/1"
            >
              <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('crm.order.customerId')" prop="customerId">
            <el-select
              v-model="formData.customerId"
              :placeholder="t('crm.order.customerIdPlaceholder')"
              class="w-1/1"
              @change="handleCustomerChange"
            >
              <el-option v-for="item in customerList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('crm.order.businessId')" prop="businessId">
            <el-select
              v-model="formData.businessId"
              :disabled="!formData.customerId"
              class="w-1/1"
              @change="handleBusinessChange"
              :placeholder="getBusinessOptions.length === 0 && formData.customerId ? t('crm.order.noBusinessHint') : t('crm.order.businessIdPlaceholder')"
            >
              <el-option
                v-for="item in getBusinessOptions"
                :key="item.id"
                :label="item.name"
                :value="item.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('crm.order.orderDate')" prop="orderDate">
            <el-date-picker
              v-model="formData.orderDate"
              :placeholder="t('crm.order.orderDatePlaceholder')"
              type="date"
              value-format="x"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-form-item :label="t('crm.order.remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('crm.order.remarkPlaceholder')" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
      <ContentWrap>
        <el-tabs v-model="subTabsName" class="-mt-15px -mb-10px">
          <el-tab-pane :label="t('crm.order.productList')" name="product">
            <OrderProductForm
              ref="productFormRef"
              :products="formData.products"
              :disabled="disabled"
            />
          </el-tab-pane>
        </el-tabs>
      </ContentWrap>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('crm.order.totalProductPrice')" prop="totalProductPrice">
            <el-input disabled v-model="formData.totalProductPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('crm.order.discountPercent')" prop="discountPercent">
            <el-input-number
              v-model="formData.discountPercent"
              :placeholder="t('crm.order.discountPercent')"
              controls-position="right"
              :min="0"
              :precision="2"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="t('crm.order.totalPrice')" prop="totalPrice">
            <el-input disabled v-model="formData.totalPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('common.save') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as CustomerApi from '@/api/crm/customer'
import * as OrderApi from '@/api/crm/order'
import * as UserApi from '@/api/system/user'
import * as BusinessApi from '@/api/crm/business'
import * as ProductApi from '@/api/crm/product'
import { erpPriceMultiply, erpPriceInputFormatter } from '@/utils'
import { useUserStore } from '@/store/modules/user'
import OrderProductForm from '@/views/crm/order/components/OrderProductForm.vue'

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref({
  id: undefined,
  no: undefined,
  name: undefined,
  customerId: undefined,
  businessId: undefined,
  orderDate: undefined,
  ownerUserId: undefined,
  discountPercent: 0,
  totalProductPrice: undefined,
  totalPrice: undefined,
  remark: undefined,
  products: []
})
const formRules = reactive({
  name: [{ required: true, message: t('crm.order.nameRequired'), trigger: 'blur' }],
  customerId: [{ required: true, message: t('crm.order.customerIdRequired'), trigger: 'blur' }],
  orderDate: [{ required: true, message: t('crm.order.orderDateRequired'), trigger: 'blur' }],
  ownerUserId: [{ required: true, message: t('crm.order.ownerUserRequired'), trigger: 'blur' }]
})
const formRef = ref()
const userOptions = ref<UserApi.UserVO[]>([])
const customerList = ref([])
const businessList = ref<BusinessApi.BusinessVO[]>([])

const subTabsName = ref('product')
const productFormRef = ref()

watch(
  () => formData.value,
  (val) => {
    if (!val) return
    const totalProductPrice = val.products.reduce((prev, curr) => prev + (curr.totalPrice || 0), 0)
    const discountPrice = val.discountPercent != null
      ? erpPriceMultiply(totalProductPrice, val.discountPercent / 100.0)
      : 0
    const totalPrice = totalProductPrice - discountPrice
    formData.value.totalProductPrice = totalProductPrice
    formData.value.totalPrice = totalPrice
  },
  { deep: true }
)

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      formData.value = await OrderApi.getOrder(id)
    } finally {
      formLoading.value = false
    }
  }
  customerList.value = await CustomerApi.getCustomerSimpleList()
  userOptions.value = await UserApi.getSimpleUserList()
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
  businessList.value = await BusinessApi.getSimpleBusinessList()
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  formLoading.value = true
  productFormRef.value?.validate()
  try {
    const data = unref(formData.value) as unknown as OrderApi.OrderVO
    if (formType.value === 'create') {
      await OrderApi.createOrder(data)
      message.success(t('common.createSuccess'))
    } else {
      await OrderApi.updateOrder(data)
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
    no: undefined,
    name: undefined,
    customerId: undefined,
    businessId: undefined,
    orderDate: undefined,
    ownerUserId: undefined,
    discountPercent: 0,
    totalProductPrice: undefined,
    totalPrice: undefined,
    remark: undefined,
    products: []
  }
  formRef.value?.resetFields()
}

const handleCustomerChange = () => {
  formData.value.businessId = undefined
  formData.value.products = []
}

const handleBusinessChange = async (businessId: number) => {
  if (!businessId) return
  const business = await BusinessApi.getBusiness(businessId)
  const enabledProducts = await ProductApi.getProductSimpleList()
  const enabledIds = new Set(enabledProducts.map(p => p.id))
  const validProducts = (business.products || []).filter(p => enabledIds.has(p.productId))
  validProducts.forEach((item) => {
    item.orderPrice = item.businessPrice
  })
  formData.value.products = validProducts
  if (validProducts.length !== (business.products || []).length) {
    message.warning(t('crm.order.someProductsDisabled'))
  }
}

const getBusinessOptions = computed(() =>
  businessList.value.filter((item) => item.customerId == formData.value.customerId)
)
</script>
