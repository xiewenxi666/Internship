<template>
  <Dialog v-model="dialogVisible" title="批量创建回款计划" width="55%">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="合同编号" prop="contractId">
            <el-select
              v-model="formData.contractId"
              class="w-1/1"
              filterable
              placeholder="请选择合同"
              @change="handleContractChange"
            >
              <el-option
                v-for="item in contractList"
                :key="item.id"
                :label="item.name || item.no"
                :value="item.id!"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="负责人" prop="ownerUserId">
            <el-select
              v-model="formData.ownerUserId"
              class="w-1/1"
              filterable
              placeholder="请选择负责人"
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
        <el-col :span="8">
          <el-form-item label="回款期数" prop="periodCount">
            <el-input-number
              v-model="formData.periodCount"
              :min="1"
              :max="12"
              class="!w-100%"
              controls-position="right"
              :placeholder="t('receivablePlan.periodPlaceholder') || '请输入回款期数'"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="订单总金额" prop="totalPrice">
            <el-input-number
              v-model="formData.totalPrice"
              :min="0"
              :precision="2"
              class="!w-100%"
              controls-position="right"
              placeholder="请输入订单总金额"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="回款方式" prop="returnType">
            <el-select v-model="formData.returnType" class="w-1/1" placeholder="请选择回款方式">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_RECEIVABLE_RETURN_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="提前收款提醒" prop="remindEnabled">
            <el-switch v-model="formData.remindEnabled" />
          </el-form-item>
        </el-col>
        <el-col v-if="formData.remindEnabled" :span="12">
          <el-form-item label="提前提醒天数" prop="remindDays">
            <el-select v-model="formData.remindDays" class="w-1/1" placeholder="请选择提醒天数">
              <el-option :key="3" label="3天" :value="3" />
              <el-option :key="7" label="7天" :value="7" />
              <el-option :key="15" label="15天" :value="15" />
              <el-option :key="30" label="30天" :value="30" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="备注" prop="remark">
            <el-input v-model="formData.remark" type="textarea" :rows="2" placeholder="请输入备注" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <div v-if="plans.length > 0" style="margin-top: 20px">
      <el-table :data="plans" border size="small">
        <el-table-column label="期次" prop="period" width="80" align="center" />
        <el-table-column label="计划回款占比%" prop="percent" align="center">
          <template #default="{ row }">
            <el-input-number
              v-model="row.percent"
              :min="0"
              :max="100"
              :precision="2"
              size="small"
              controls-position="right"
              style="width: 120px"
              @change="handlePercentChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="计划回款金额(元)" prop="price" align="center">
          <template #default="{ row }">
            <el-input-number
              v-model="row.price"
              :min="0"
              :precision="2"
              size="small"
              controls-position="right"
              style="width: 160px"
              @change="handlePriceChange(row)"
            />
          </template>
        </el-table-column>
      </el-table>
      <div class="summary-row">
        <span>总计划期次：<strong>{{ plans.length }}期</strong></span>
        <span>总回款占比：<strong>{{ summaryPercent }}%</strong></span>
        <span>总回款金额：<strong>¥{{ summaryPrice }}</strong></span>
      </div>
    </div>

    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">
        {{ t('dialog.confirm') }}
      </el-button>
      <el-button @click="dialogVisible = false">
        {{ t('dialog.cancel') }}
      </el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import * as UserApi from '@/api/system/user'
import * as ContractApi from '@/api/crm/contract'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const message = useMessage()

interface PlanItem {
  period: number
  percent: number
  price: number
}

const dialogVisible = ref(false)
const formLoading = ref(false)
const formRef = ref()
const userOptions = ref<UserApi.UserVO[]>([])
const contractList = ref<ContractApi.ContractVO[]>([])

const formData = ref({
  contractId: undefined as number | undefined,
  ownerUserId: undefined as number | undefined,
  periodCount: undefined as number | undefined,
  totalPrice: undefined as number | undefined,
  returnType: undefined as number | undefined,
  remindEnabled: false,
  remindDays: undefined as number | undefined,
  remark: ''
})

const formRules = reactive({
  contractId: [{ required: true, message: '请选择合同', trigger: 'change' }],
  ownerUserId: [{ required: true, message: '请选择负责人', trigger: 'change' }],
  periodCount: [{ required: true, message: '请输入回款期数', trigger: 'blur' }],
  totalPrice: [{ required: true, message: '请输入订单总金额', trigger: 'blur' }],
  returnType: [{ required: true, message: '请选择回款方式', trigger: 'change' }]
})

const plans = ref<PlanItem[]>([])

const totalPercent = computed(() => {
  return plans.value.reduce((sum, item) => sum + (item.percent || 0), 0)
})

const totalPriceSum = computed(() => {
  return plans.value.reduce((sum, item) => sum + (item.price || 0), 0)
})

const summaryPercent = computed(() => {
  return totalPercent.value.toFixed(2)
})

const summaryPrice = computed(() => {
  return erpPriceInputFormatter(totalPriceSum.value)
})

const generatePlans = () => {
  const count = formData.value.periodCount
  const total = formData.value.totalPrice
  if (!count || !total || count <= 0 || total <= 0) {
    plans.value = []
    return
  }
  const avgPercent = parseFloat((100 / count).toFixed(2))
  const avgPrice = parseFloat((total / count).toFixed(2))

  const newPlans: PlanItem[] = []
  let cumulativePercent = 0
  let cumulativePrice = 0

  for (let i = 0; i < count; i++) {
    if (i === count - 1) {
      newPlans.push({
        period: i + 1,
        percent: parseFloat((100 - cumulativePercent).toFixed(2)),
        price: parseFloat((total - cumulativePrice).toFixed(2))
      })
    } else {
      newPlans.push({
        period: i + 1,
        percent: avgPercent,
        price: avgPrice
      })
      cumulativePercent += avgPercent
      cumulativePrice += avgPrice
    }
  }
  plans.value = newPlans
}

watch(
  () => [formData.value.periodCount, formData.value.totalPrice],
  () => {
    generatePlans()
  }
)

const handlePercentChange = (row: PlanItem) => {
  if (formData.value.totalPrice) {
    row.price = parseFloat(((formData.value.totalPrice * row.percent) / 100).toFixed(2))
  }
}

const handlePriceChange = (row: PlanItem) => {
  if (formData.value.totalPrice && formData.value.totalPrice > 0) {
    row.percent = parseFloat(((row.price / formData.value.totalPrice) * 100).toFixed(2))
  }
}

const handleContractChange = (contractId: number) => {
  if (contractId) {
    const contract = contractList.value.find((c) => c.id === contractId)
    if (contract && contract.totalPrice) {
      formData.value.totalPrice = contract.totalPrice
    }
  }
}

const resetForm = () => {
  formData.value = {
    contractId: undefined,
    ownerUserId: undefined,
    periodCount: undefined,
    totalPrice: undefined,
    returnType: undefined,
    remindEnabled: false,
    remindDays: undefined,
    remark: ''
  }
  plans.value = []
  formRef.value?.resetFields()
}

const open = async () => {
  dialogVisible.value = true
  resetForm()
  userOptions.value = await UserApi.getSimpleUserList()
  contractList.value = await ContractApi.getContractSimpleList(0 as any)
  formData.value.ownerUserId = useUserStore().getUser.id
}
defineExpose({ open })

const emit = defineEmits(['success'])
const submitForm = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  if (plans.value.length === 0) {
    message.warning('请设置回款期数和订单总金额以生成回款计划')
    return
  }

  formLoading.value = true
  try {
    const data = {
      contractId: formData.value.contractId,
      ownerUserId: formData.value.ownerUserId,
      periodCount: formData.value.periodCount,
      totalPrice: formData.value.totalPrice,
      returnType: formData.value.returnType,
      remindEnabled: formData.value.remindEnabled,
      remindDays: formData.value.remindEnabled ? formData.value.remindDays : undefined,
      remark: formData.value.remark,
      plans: plans.value.map((item) => ({
        period: item.period,
        percent: item.percent,
        price: item.price
      }))
    }
    await ReceivablePlanApi.batchCreateReceivablePlan(data)
    message.success(t('common.createSuccess'))
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}
</script>

<style lang="scss" scoped>
.summary-row {
  display: flex;
  justify-content: flex-end;
  gap: 30px;
  padding: 12px 16px;
  background-color: var(--el-fill-color-light);
  border: 1px solid var(--el-border-color);
  border-top: none;
  border-radius: 0 0 4px 4px;
  font-size: 14px;
  color: var(--el-text-color-regular);

  strong {
    color: var(--el-color-primary);
  }
}
</style>
