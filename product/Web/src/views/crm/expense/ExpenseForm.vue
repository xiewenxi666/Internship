<template>
  <Dialog v-model="dialogVisible" title="费用信息">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item label="费用编号" prop="no">
            <el-input v-model="formData.no" disabled placeholder="系统自动生成" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="客户" prop="customerId">
            <el-select
              v-model="formData.customerId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              placeholder="请选择客户"
              @change="handleCustomerChange"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="合同" prop="contractId">
            <el-select
              v-model="formData.contractId"
              :disabled="formType !== 'create'"
              class="w-1/1"
              filterable
              placeholder="请选择合同"
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
          <el-form-item label="费用类型" prop="type">
            <el-select v-model="formData.type" class="w-1/1" placeholder="请选择费用类型">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_EXPENSE_TYPE)"
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
          <el-form-item label="费用金额" prop="price">
            <el-input-number
              v-model="formData.price"
              :min="0.01"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              placeholder="请输入费用金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="申请日期" prop="applyDate">
            <el-date-picker
              v-model="formData.applyDate"
              placeholder="选择申请日期"
              type="date"
              value-format="x"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="费用内容" prop="content">
            <el-input v-model="formData.content" placeholder="请输入费用内容" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="formData.remark" placeholder="请输入备注" type="textarea" />
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
import * as ExpenseApi from '@/api/crm/expense'
import { ExpenseVO } from '@/api/crm/expense'
import * as CustomerApi from '@/api/crm/customer'
import * as ContractApi from '@/api/crm/contract'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'

defineOptions({ name: 'ExpenseForm' })

const message = useMessage()
const dialogVisible = ref(false)
const formLoading = ref(false)
const formType = ref('')
const formData = ref<ExpenseApi.ExpenseVO>({} as ExpenseApi.ExpenseVO)
const formRules = reactive({
  customerId: [{ required: true, message: '客户不能为空', trigger: 'blur' }],
  contractId: [{ required: true, message: '合同不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '费用类型不能为空', trigger: 'blur' }],
  price: [{ required: true, message: '费用金额不能为空', trigger: 'blur' }],
  applyDate: [{ required: true, message: '申请日期不能为空', trigger: 'blur' }]
})
const formRef = ref()
const customerList = ref<CustomerApi.CustomerVO[]>([])
const contractList = ref<ContractApi.ContractVO[]>([])

const handleCustomerChange = async (customerId: number) => {
  if (customerId) {
    contractList.value = await ContractApi.getContractSimpleList(customerId)
  } else {
    contractList.value = []
  }
  formData.value.contractId = undefined
}

const open = async (
  type: string,
  id?: number
) => {
  dialogVisible.value = true
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = (await ExpenseApi.getExpense(id)) as ExpenseVO
      formData.value = data
      customerList.value = await CustomerApi.getCustomerSimpleList()
      if (data.customerId) {
        contractList.value = await ContractApi.getContractSimpleList(data.customerId)
      }
    } finally {
      formLoading.value = false
    }
  } else {
    customerList.value = await CustomerApi.getCustomerSimpleList()
    contractList.value = []
  }
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
  formLoading.value = true
  try {
    const data = formData.value as unknown as ExpenseApi.ExpenseVO
    if (formType.value === 'create') {
      await ExpenseApi.createExpense(data)
      message.success('新增成功')
    } else {
      await ExpenseApi.updateExpense(data)
      message.success('修改成功')
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {} as ExpenseApi.ExpenseVO
  formRef.value?.resetFields()
}
</script>
