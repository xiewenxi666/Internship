<template>
  <el-form
    ref="formRef"
    :model="formData"
    :rules="formRules"
    v-loading="formLoading"
    label-width="0px"
    :inline-message="true"
    :disabled="disabled"
  >
    <el-table :data="formData" class="-mt-10px" :table-layout="'auto'">
      <el-table-column :label="t('crm.business.index')" type="index" align="center" width="60" />
      <el-table-column :label="t('crm.business.product')" min-width="180">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.productId`" :rules="formRules.productId" class="mb-0px!">
            <el-select
              v-model="row.productId"
              clearable
              filterable
              @change="onChangeProduct($event, row)"
              :placeholder="t('common.select')"
            >
              <el-option v-for="item in productList" :key="item.id" :label="item.name" :value="item.id" />
            </el-select>
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.business.productNo')" min-width="150">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.productNo" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.business.productUnit')" min-width="80">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.CRM_PRODUCT_UNIT" :value="row.productUnit" />
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.business.productPrice')" min-width="120">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.productPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.order.orderPrice')" fixed="right" min-width="140">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.orderPrice`" :rules="formRules.orderPrice" class="mb-0px!">
            <el-input-number
              v-model="row.orderPrice"
              controls-position="right"
              :min="0.001"
              :precision="2"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.business.count')" prop="count" fixed="right" min-width="120">
        <template #default="{ row, $index }">
          <el-form-item :prop="`${$index}.count`" :rules="formRules.count" class="mb-0px!">
            <el-input-number
              v-model="row.count"
              controls-position="right"
              :min="0.001"
              :precision="3"
              class="!w-100%"
            />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.business.total')" prop="totalPrice" fixed="right" min-width="140">
        <template #default="{ row }">
          <el-form-item class="mb-0px!">
            <el-input disabled v-model="row.totalPrice" :formatter="erpPriceInputFormatter" />
          </el-form-item>
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="150">
        <template #default="{ $index }">
          <el-button link type="danger" @click="handleDelete($index)"><Icon icon="ep:delete" /> {{ t('common.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
  </el-form>
  <el-row justify="center" class="mt-3" v-if="!disabled">
    <el-button @click="handleAdd" round>+ {{ t('crm.business.addProduct') }}</el-button>
  </el-row>
</template>
<script setup lang="ts">
import * as ProductApi from '@/api/crm/product'
import { erpPriceInputFormatter, erpPriceMultiply } from '@/utils'
import { DICT_TYPE } from '@/utils/dict'

const { t } = useI18n()

const props = defineProps<{
  products: undefined
  disabled: false
}>()

const formLoading = ref(false)
const formData = ref([])
const formRules = reactive({
  productId: [{ required: true, message: t('crm.business.productRequired'), trigger: 'blur' }],
  orderPrice: [{ required: true, message: t('crm.order.orderPriceRequired'), trigger: 'blur' }],
  count: [{ required: true, message: t('crm.business.countRequired'), trigger: 'blur' }]
})
const formRef = ref([])
const productList = ref<ProductApi.ProductVO[]>([])

watch(
  () => props.products,
  async (val) => {
    formData.value = val
  },
  { immediate: true }
)

watch(
  () => formData.value,
  (val) => {
    if (!val || val.length === 0) return
    val.forEach((item) => {
      if (item.orderPrice != null && item.count != null) {
        item.totalPrice = erpPriceMultiply(item.orderPrice, item.count)
      } else {
        item.totalPrice = undefined
      }
    })
  },
  { deep: true }
)

const handleAdd = () => {
  const row = {
    id: undefined,
    productId: undefined,
    productUnit: undefined,
    productNo: undefined,
    productPrice: undefined,
    orderPrice: undefined,
    count: 1
  }
  formData.value.push(row)
}

const handleDelete = (index: number) => {
  formData.value.splice(index, 1)
}

const onChangeProduct = (productId, row) => {
  const product = productList.value.find((item) => item.id === productId)
  if (product) {
    row.productUnit = product.unit
    row.productNo = product.no
    row.productPrice = product.price
    row.orderPrice = product.price
  }
}

const validate = () => {
  return formRef.value?.validate()
}
defineExpose({ validate })

onMounted(async () => {
  productList.value = await ProductApi.getProductSimpleList()
})
</script>
