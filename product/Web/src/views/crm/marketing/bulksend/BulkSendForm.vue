<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="auto">
      <el-row>
        <el-col :span="12">
          <el-form-item label="群发类型" prop="type">
            <el-select v-model="formData.type" class="w-1/1" disabled>
              <el-option label="邮件群发" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="任务标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入任务标题" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="关联活动">
            <el-select v-model="formData.campaignId" class="w-1/1" clearable placeholder="选择关联的营销活动（可选）">
              <el-option v-for="c in campaignOptions" :key="c.id" :label="c.title" :value="c.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="发送内容" prop="content">
            <el-input v-model="formData.content" type="textarea" :rows="4" placeholder="请输入邮件正文" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="推广产品">
            <el-select v-model="selectedProductIds" multiple filterable placeholder="搜索并选择推广产品" class="w-1/1" @change="onProductChange">
              <el-option v-for="p in productOptions" :key="p.id" :label="p.name + ' (¥' + (p.price || 0) + ')'" :value="p.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="目标类型" prop="targetType">
            <el-select v-model="formData.targetType" class="w-1/1">
              <el-option label="全部客户" :value="1" />
              <el-option label="指定客户" :value="2" />
              <el-option label="指定线索" :value="3" />
              <el-option label="指定联系人" :value="4" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row v-if="formData.targetType === 2">
        <el-col :span="24">
          <el-form-item label="选择客户">
            <el-select v-model="selectedCustomerIds" multiple filterable placeholder="搜索并选择客户" class="w-1/1" @change="onCustomerChange">
              <el-option v-for="c in customerOptions" :key="c.id" :label="c.name + ' (' + (c.mobile || '无手机') + ')'" :value="c.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="负责人员" prop="ownerUserId">
            <el-select v-model="formData.ownerUserId" class="w-1/1">
              <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确认</el-button>
      <el-button @click="dialogVisible = false">取消</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as BulkSendApi from '@/api/crm/bulksend'
import * as CampaignApi from '@/api/crm/campaign'
import * as CustomerApi from '@/api/crm/customer'
import * as ProductApi from '@/api/crm/product'
import * as UserApi from '@/api/system/user'

defineOptions({ name: 'CrmBulkSendForm' })

const message = useMessage()
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const userOptions = ref<UserApi.UserVO[]>([])
const campaignOptions = ref<CampaignApi.CampaignVO[]>([])
const customerOptions = ref<any[]>([])
const productOptions = ref<any[]>([])
const selectedCustomerIds = ref<number[]>([])
const selectedProductIds = ref<number[]>([])
const formRef = ref()
const formData = ref({ id: undefined, title: undefined, campaignId: undefined, type: 2, templateId: undefined, content: undefined, targetType: undefined, targetIds: undefined, targetCount: 0, ownerUserId: undefined, status: 1 })

const onCustomerChange = (ids: number[]) => {
  formData.value.targetIds = ids.join(',')
  formData.value.targetCount = ids.length
}

const onProductChange = (ids: number[]) => {
  formData.value.productIds = ids.join(',')
}
const formRules = reactive({
  title: [{ required: true, message: '不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '请选择', trigger: 'change' }],
  content: [{ required: true, message: '不能为空', trigger: 'blur' }],
  targetType: [{ required: true, message: '请选择', trigger: 'change' }],
  ownerUserId: [{ required: true, message: '请选择', trigger: 'change' }]
})

const open = async (type: string, id?: number) => {
  dialogVisible.value = true; formType.value = type
  dialogTitle.value = type === 'create' ? '新建群发' : '编辑群发'
  userOptions.value = await UserApi.getSimpleUserList()
  const campaignData = await CampaignApi.getCampaignPage({ pageNo: 1, pageSize: 100 })
  campaignOptions.value = campaignData?.list || []
  const cdata = await CustomerApi.getCustomerPage({ pageNo: 1, pageSize: 100 })
  customerOptions.value = cdata?.list || []
  const pdata = await ProductApi.getProductPage({ pageNo: 1, pageSize: 100 })
  productOptions.value = pdata?.list || []
  if (id) { formLoading.value = true; try { const d = await BulkSendApi.getBulkSend(id); formData.value = { ...d } } finally { formLoading.value = false } }
  else resetForm()
}
defineExpose({ open })

const submitForm = async () => {
  const valid = await formRef.value?.validate(); if (!valid) return
  formLoading.value = true
  try {
    if (formType.value === 'create') { await BulkSendApi.createBulkSend(formData.value); message.success('创建成功') }
    else { await BulkSendApi.updateBulkSend(formData.value); message.success('修改成功') }
    dialogVisible.value = false; emits('success')
  } finally { formLoading.value = false }
}

const emits = defineEmits(['success'])
const resetForm = () => { formData.value = { id: undefined, title: undefined, campaignId: undefined, productIds: undefined, type: 2, templateId: undefined, content: undefined, targetType: undefined, targetIds: undefined, targetCount: 0, ownerUserId: undefined, status: 1 }; selectedCustomerIds.value = []; selectedProductIds.value = [] }
</script>
