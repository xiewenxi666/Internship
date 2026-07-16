<template>
  <Dialog v-model="dialogVisible" title="编辑回款计划" width="65%">
    <div v-loading="loading" class="mb-4">
      <el-descriptions :column="4" border size="small">
        <el-descriptions-item label="客户名称">{{ contractInfo?.customerName }}</el-descriptions-item>
        <el-descriptions-item label="合同编号">{{ contractInfo?.contractNo }}</el-descriptions-item>
        <el-descriptions-item label="合同总金额">
          {{ erpPriceInputFormatter(contractInfo?.totalPrice) }}
        </el-descriptions-item>
        <el-descriptions-item label="签单日期">
          {{ formatDate(contractInfo?.signTime, 'YYYY-MM-DD') }}
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <el-table :data="planList" border size="small">
      <el-table-column label="期次" prop="period" width="80" align="center" />
      <el-table-column label="计划回款日期" prop="returnTime" min-width="180" align="center">
        <template #default="{ row }">
          <el-date-picker
            v-model="row.returnTime"
            type="date"
            value-format="YYYY-MM-DD HH:mm:ss"
            placeholder="选择日期"
            size="small"
            style="width: 100%"
          />
        </template>
      </el-table-column>
      <el-table-column label="计划回款占比(%)" prop="percent" min-width="160" align="center">
        <template #default="{ row }">
          <el-input-number
            v-model="row.percent"
            :min="0"
            :max="100"
            :precision="2"
            size="small"
            controls-position="right"
            style="width: 120px"
          />
        </template>
      </el-table-column>
      <el-table-column label="计划回款金额(元)" prop="price" min-width="180" align="center">
        <template #default="{ row }">
          <el-input-number
            v-model="row.price"
            :min="0"
            :precision="2"
            size="small"
            controls-position="right"
            style="width: 160px"
          />
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="remark" min-width="150" align="center">
        <template #default="{ row }">
          <el-input v-model="row.remark" size="small" placeholder="备注" />
        </template>
      </el-table-column>
      <el-table-column label="操作" width="80" align="center">
        <template #default="{ $index }">
          <el-button link type="danger" size="small" @click="removeRow($index)">
            <Icon icon="ep:delete" />
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="mt-2">
      <el-button link type="primary" size="small" @click="addRow">
        <Icon icon="ep:plus" class="mr-1" />新增一行
      </el-button>
    </div>

    <div class="summary-row mt-3">
      <span>总计划期次：<strong>{{ planList.length }}期</strong></span>
      <span>总回款占比：<strong>{{ summaryPercent }}%</strong></span>
      <span>总回款金额：<strong>¥{{ erpPriceInputFormatter(summaryPrice) }}</strong></span>
    </div>

    <template #footer>
      <el-button type="primary" @click="submitForm" :loading="submitting">保存并关闭</el-button>
      <el-button @click="dialogVisible = false">取消</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as ReceivablePlanApi from '@/api/crm/receivable/plan'
import { erpPriceInputFormatter } from '@/utils'
import { formatDate } from '@/utils/formatTime'

const { t } = useI18n('crm')
const message = useMessage()

const dialogVisible = ref(false)
const loading = ref(false)
const submitting = ref(false)
const contractInfo = ref<any>({})
const planList = ref<any[]>([])
const contractId = ref<number>()

const summaryPercent = computed(() => {
  return planList.value.reduce((sum, item) => sum + (item.percent || 0), 0).toFixed(2)
})

const summaryPrice = computed(() => {
  return planList.value.reduce((sum, item) => sum + (item.price || 0), 0)
})

const addRow = () => {
  const maxPeriod = planList.value.reduce((max, item) => Math.max(max, item.period || 0), 0)
  planList.value.push({
    period: maxPeriod + 1,
    returnTime: '',
    percent: 0,
    price: 0,
    remark: ''
  })
}

const removeRow = (index: number) => {
  planList.value.splice(index, 1)
  planList.value.forEach((item, i) => {
    item.period = i + 1
  })
}

const open = async (cId: number) => {
  contractId.value = cId
  dialogVisible.value = true
  loading.value = true
  try {
    const data = await ReceivablePlanApi.getReceivablePlanPageByCustomer({
      contractId: cId,
      pageNo: 1,
      pageSize: 100
    })
    const plans = data.list || []
    planList.value = plans.map((p: any) => ({
      id: p.id,
      period: p.period,
      returnTime: p.returnTime,
      percent: p.percent || 0,
      price: p.price || 0,
      remark: p.remark || ''
    }))
    if (plans.length > 0) {
      const first = plans[0]
      contractInfo.value = {
        customerName: first.customerName,
        contractNo: first.contractNo,
        contractId: first.contractId,
        totalPrice: (data.list || []).reduce((sum: number, item: any) => sum + (item.price || 0), 0),
        signTime: first.createTime
      }
    }
  } finally {
    loading.value = false
  }
}

defineExpose({ open })

const emit = defineEmits(['success'])

const submitForm = async () => {
  submitting.value = true
  try {
    for (const plan of planList.value) {
      if (plan.id) {
        await ReceivablePlanApi.updateReceivablePlan({
          id: plan.id,
          returnTime: plan.returnTime,
          percent: plan.percent,
          price: plan.price,
          remark: plan.remark
        })
      } else {
        await ReceivablePlanApi.createReceivablePlan({
          contractId: contractId.value,
          returnTime: plan.returnTime,
          percent: plan.percent,
          price: plan.price,
          remark: plan.remark,
          returnType: undefined,
          remindDays: undefined,
          ownerUserId: undefined
        })
      }
    }
    message.success(t('common.updateSuccess'))
    dialogVisible.value = false
    emit('success')
  } finally {
    submitting.value = false
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
  border-radius: 4px;
  font-size: 14px;
  color: var(--el-text-color-regular);

  strong {
    color: var(--el-color-primary);
  }
}
</style>
