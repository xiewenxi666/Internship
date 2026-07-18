<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="900px">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="auto"
    >
      <el-row>
        <el-col :span="12">
          <el-form-item :label="t('reimbursement.no')" prop="no">
            <el-input v-model="formData.no" disabled :placeholder="t('contract.noAutoGenerate')" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="t('reimbursement.ownerUserName')" prop="ownerUserId">
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
        <el-col :span="12">
          <el-form-item :label="t('reimbursement.applyDate')" prop="applyDate">
            <el-date-picker
              v-model="formData.applyDate"
              :placeholder="t('reimbursement.applyDate')"
              type="date"
              value-format="x"
              class="!w-100%"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item :label="t('reimbursement.remark')" prop="remark">
            <el-input v-model="formData.remark" :placeholder="t('customer.remarkPlaceholder')" type="textarea" />
          </el-form-item>
        </el-col>
      </el-row>

      <el-divider content-position="left">{{ t('reimbursement.expenseInfo') }}</el-divider>

      <el-row>
        <el-col :span="24">
          <el-button type="primary" @click="getExpenseList">
            <Icon class="mr-5px" icon="ep:search" />
            {{ t('common.search') }}
          </el-button>
        </el-col>
      </el-row>

      <el-row class="mt-10px">
        <el-col :span="24">
          <span class="text-sm font-medium">{{ t('reimbursement.availableExpense') }}</span>
          <el-table
            ref="expenseTableRef"
            v-loading="expenseLoading"
            :data="expenseList"
            :show-overflow-tooltip="true"
            :stripe="true"
            :table-layout="'auto'"
            max-height="240"
            class="mt-5px"
            @selection-change="handleExpenseSelectionChange"
          >
            <el-table-column type="selection" width="45" />
            <el-table-column align="center" :label="t('reimbursement.expenseNo')" prop="no" min-width="150" />
            <el-table-column align="center" :label="t('reimbursement.expenseContent')" prop="content" min-width="150" />
            <el-table-column align="center" :label="t('reimbursement.expenseType')" prop="type" min-width="130">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="scope.row.type" />
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="t('reimbursement.expensePrice') + '（元）'"
              prop="price"
              min-width="120"
              :formatter="erpPriceTableColumnFormatter"
            />
            <el-table-column
              :formatter="dateFormatter2"
              align="center"
              :label="t('reimbursement.expenseApplyDate')"
              prop="applyDate"
              min-width="130"
            />
          </el-table>
        </el-col>
      </el-row>

      <el-row class="mt-10px">
        <el-col :span="24">
          <span class="text-sm font-medium">{{ t('reimbursement.selectedExpense') }}</span>
          <el-table
            :data="selectedExpenses"
            :show-overflow-tooltip="true"
            :stripe="true"
            :table-layout="'auto'"
            max-height="200"
            class="mt-5px"
          >
            <el-table-column align="center" :label="t('reimbursement.expenseNo')" prop="no" min-width="150" />
            <el-table-column align="center" :label="t('reimbursement.expenseContent')" prop="content" min-width="150" />
            <el-table-column align="center" :label="t('reimbursement.expenseType')" prop="type" min-width="130">
              <template #default="scope">
                <dict-tag :type="DICT_TYPE.CRM_EXPENSE_TYPE" :value="scope.row.type" />
              </template>
            </el-table-column>
            <el-table-column
              align="center"
              :label="t('reimbursement.expensePrice') + '（元）'"
              prop="price"
              min-width="120"
              :formatter="erpPriceTableColumnFormatter"
            />
            <el-table-column
              :formatter="dateFormatter2"
              align="center"
              :label="t('reimbursement.expenseApplyDate')"
              prop="applyDate"
              min-width="130"
            />
            <el-table-column align="center" :label="t('common.action')" width="80">
              <template #default="scope">
                <el-button link type="danger" @click="removeExpense(scope.$index)">
                  {{ t('common.delete') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-col>
      </el-row>

      <el-row class="mt-10px">
        <el-col :span="24" class="text-right">
          <span class="text-base font-bold">{{ t('reimbursement.price') }}（{{ t('reimbursement.expenseTotal') }}）：¥ {{ erpPriceInputFormatter(totalPrice) }}</span>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">{{ t('dialog.confirm') }}</el-button>
      <el-button @click="dialogVisible = false">{{ t('dialog.cancel') }}</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import * as ReimbursementApi from '@/api/crm/reimbursement'
import { ReimbursementVO } from '@/api/crm/reimbursement'
import * as ExpenseApi from '@/api/crm/expense'
import { ExpenseVO } from '@/api/crm/expense'
import * as UserApi from '@/api/system/user'
import { useUserStore } from '@/store/modules/user'
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter2 } from '@/utils/formatTime'
import { erpPriceTableColumnFormatter, erpPriceInputFormatter } from '@/utils'

const { t } = useI18n('crm')
const message = useMessage()
const userOptions = ref<UserApi.UserVO[]>([])
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const formData = ref<ReimbursementApi.ReimbursementVO>({} as ReimbursementApi.ReimbursementVO)
const formRules = reactive({
  ownerUserId: [{ required: true, message: t('reimbursement.ownerUserIdRequired'), trigger: 'blur' }],
  applyDate: [{ required: true, message: t('reimbursement.applyDateRequired'), trigger: 'blur' }]
})
const formRef = ref()

const expenseTableRef = ref()
const expenseLoading = ref(false)
const expenseList = ref<ExpenseVO[]>([])
const selectedExpenses = ref<ExpenseVO[]>([])
const expenseQueryParams = reactive({
  pageNo: 1,
  pageSize: 100
})

const totalPrice = computed(() => {
  return selectedExpenses.value.reduce((sum, item) => sum + (item.price || 0), 0)
})

const getExpenseList = async () => {
  expenseLoading.value = true
  try {
    const data = await ExpenseApi.getExpensePage(expenseQueryParams)
    expenseList.value = data.list
  } finally {
    expenseLoading.value = false
  }
}

const handleExpenseSelectionChange = (rows: ExpenseVO[]) => {
  const existingIds = new Set(selectedExpenses.value.map(e => e.id))
  const selectedIds = new Set(rows.map(e => e.id))
  
  selectedExpenses.value = selectedExpenses.value.filter(e => selectedIds.has(e.id))
  rows.forEach(row => {
    if (!existingIds.has(row.id)) {
      selectedExpenses.value.push(row)
    }
  })
}

const removeExpense = (index: number) => {
  const removed = selectedExpenses.value[index]
  selectedExpenses.value.splice(index, 1)
  if (expenseTableRef.value) {
    expenseTableRef.value.toggleRowSelection(removed, false)
  }
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type, { scope: 'common' })
  formType.value = type
  resetForm()
  if (id) {
    formLoading.value = true
    try {
      const data = (await ReimbursementApi.getReimbursement(id)) as ReimbursementVO
      formData.value = data
      if (data.expenses) {
        selectedExpenses.value = data.expenses
      }
    } finally {
      formLoading.value = false
    }
  }
  userOptions.value = await UserApi.getSimpleUserList()
  if (formType.value === 'create') {
    formData.value.ownerUserId = useUserStore().getUser.id
  }
  await getExpenseList()
  await nextTick()
  if (selectedExpenses.value.length && expenseTableRef.value) {
    const selectedIds = new Set(selectedExpenses.value.map(e => e.id))
    expenseList.value.forEach(row => {
      if (selectedIds.has(row.id)) {
        expenseTableRef.value.toggleRowSelection(row, true)
      }
    })
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
    const data = formData.value as unknown as ReimbursementApi.ReimbursementVO
    data.expenseIds = selectedExpenses.value.map(e => e.id)
    data.price = totalPrice.value
    if (formType.value === 'create') {
      await ReimbursementApi.createReimbursement(data)
      message.success(t('common.createSuccess'))
    } else {
      await ReimbursementApi.updateReimbursement(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const resetForm = () => {
  formData.value = {} as ReimbursementApi.ReimbursementVO
  selectedExpenses.value = []
  expenseList.value = []
  formRef.value?.resetFields()
}
</script>
