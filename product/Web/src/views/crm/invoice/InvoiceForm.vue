<template>
  <Dialog v-model="dialogVisible" title="发票信息">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="发票编号" prop="no">
            <el-input v-model="formData.no" disabled placeholder="系统自动生成" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="经手人员" prop="handlerUserId">
            <el-select v-model="formData.handlerUserId" class="w-1/1">
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
      <el-row>
        <el-col :span="12">
          <el-form-item label="关联订单" prop="contractId">
            <el-select
              v-model="formData.contractId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              placeholder="请选择订单"
            >
              <el-option
                v-for="data in contractList"
                :key="data.id"
                :label="data.name"
                :value="data.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="票据类型" prop="type">
            <el-select v-model="formData.type" class="w-1/1" placeholder="请选择票据类型">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_INVOICE_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="开票日期" prop="invoiceDate">
            <el-date-picker
              v-model="formData.invoiceDate"
              placeholder="选择开票日期"
              type="date"
              value-format="x"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="开票金额" prop="price">
            <el-input-number
              v-model="formData.price"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              placeholder="请输入开票金额"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="发票号码" prop="invoiceNo">
            <el-input v-model="formData.invoiceNo" placeholder="请输入发票号码" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="订单所属人员" prop="ownerUserId">
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
      <el-row>
        <el-col :span="24">
          <el-form-item label="票据内容" prop="content">
            <el-input v-model="formData.content" placeholder="请输入票据内容" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as InvoiceApi from '@/api/crm/invoice'
import { InvoiceVO } from '@/api/crm/invoice'
import * as UserApi from '@/api/system/user'
import * as ContractApi from '@/api/crm/contract'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

defineOptions({ name: 'InvoiceForm' })

const message = useMessage()
const userOptions = ref<UserApi.UserVO[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref<InvoiceApi.InvoiceVO>({} as InvoiceApi.InvoiceVO)
const formRules = reactive({
  contractId: [{ required: true, message: '关联订单不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '票据类型不能为空', trigger: 'blur' }],
  invoiceDate: [{ required: true, message: '开票日期不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '开票金额不能为空', trigger: 'blur' }],
  handlerUserId: [{ required: true, message: '经手人员不能为空', trigger: 'blur' }],
  ownerUserId: [{ required: true, message: '订单所属人员不能为空', trigger: 'blur' }]
})
const formRef = ref()
const contractList = ref<ContractApi.ContractVO[]>([])

const open = async (
  type: string,
  id?: number
) => {
  dialogVisible.value = true
  dialogTitle.value = type === 'create' ? '新增发票' : '编辑发票'
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = (await InvoiceApi.getInvoice(id)) as InvoiceVO
      formData.value = data
    } finally {
      formLoading.value = false
    }
  }
  userOptions.value = await UserApi.getSimpleUserList()
  contractList.value = await ContractApi.getContractSimpleList()
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
    formData.value.handlerUserId = useUserStore().getUser.id
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
    const data = formData.value as unknown as InvoiceApi.InvoiceVO
    if (formType.value === 'create') {
      await InvoiceApi.createInvoice(data)
      message.success('新增成功')
    } else {
      await InvoiceApi.updateInvoice(data)
      message.success('修改成功')
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {} as InvoiceApi.InvoiceVO
  formRef.value?.resetFields()
}
</script>
