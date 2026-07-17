<template>
  <ContentWrap v-hasPermi="['crm:finance:query']">
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="年份" prop="year">
            <el-date-picker
              v-model="queryParams.year"
              class="!w-240px"
              placeholder="选择年份"
              type="year"
              value-format="YYYY"
              @change="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="人员" prop="ownerUserId">
            <el-select
              v-model="queryParams.ownerUserId"
              class="!w-240px"
              clearable
              placeholder="请选择人员"
              @change="handleQuery"
            >
              <el-option
                v-for="user in userList"
                :key="user.id"
                :label="user.nickname"
                :value="user.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              查询
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              重置
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap v-hasPermi="['crm:finance:query']" v-loading="loading">
    <el-row :gutter="20" class="mb-20px">
      <el-col :span="6">
        <el-card shadow="hover" class="summary-card summary-card--green">
          <div class="flex items-center justify-between">
            <div>
              <div class="text-14px text-gray-500">回款总额（元）</div>
              <div class="text-24px font-700 mt-8px">{{ erpPriceInputFormatter(summaryData.receivablePrice) }}</div>
              <div class="text-12px text-gray-400 mt-4px">共 {{ summaryData.receivableCount }} 笔</div>
            </div>
            <div class="summary-card__icon">
              <Icon :size="48" color="#67c23a" icon="ep:money" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="summary-card summary-card--blue">
          <div class="flex items-center justify-between">
            <div>
              <div class="text-14px text-gray-500">开票总额（元）</div>
              <div class="text-24px font-700 mt-8px">{{ erpPriceInputFormatter(summaryData.invoicePrice) }}</div>
              <div class="text-12px text-gray-400 mt-4px">共 {{ summaryData.invoiceCount }} 笔</div>
            </div>
            <div class="summary-card__icon">
              <Icon :size="48" color="#409eff" icon="ep:document-checked" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="summary-card summary-card--orange">
          <div class="flex items-center justify-between">
            <div>
              <div class="text-14px text-gray-500">报销总额（元）</div>
              <div class="text-24px font-700 mt-8px">{{ erpPriceInputFormatter(summaryData.reimbursementPrice) }}</div>
              <div class="text-12px text-gray-400 mt-4px">共 {{ summaryData.reimbursementCount }} 笔</div>
            </div>
            <div class="summary-card__icon">
              <Icon :size="48" color="#e6a23c" icon="ep:credit-card" />
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="summary-card summary-card--red">
          <div class="flex items-center justify-between">
            <div>
              <div class="text-14px text-gray-500">退款总额（元）</div>
              <div class="text-24px font-700 mt-8px">{{ erpPriceInputFormatter(summaryData.refundPrice) }}</div>
              <div class="text-12px text-gray-400 mt-4px">共 {{ summaryData.refundCount }} 笔</div>
            </div>
            <div class="summary-card__icon">
              <Icon :size="48" color="#f56c6c" icon="ep:delete" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-table v-loading="loading" :data="monthlyData" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" label="月份" prop="month" min-width="100" />
      <el-table-column
        align="center"
        label="回款金额（元）"
        prop="receivablePrice"
        min-width="150"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        label="开票金额（元）"
        prop="invoicePrice"
        min-width="150"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        label="报销金额（元）"
        prop="reimbursementPrice"
        min-width="150"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        align="center"
        label="退款金额（元）"
        prop="refundPrice"
        min-width="150"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" label="合计（元）" prop="totalPrice" min-width="150">
        <template #default="scope">
          {{ erpPriceInputFormatter((scope.row.receivablePrice || 0) + (scope.row.invoicePrice || 0) + (scope.row.reimbursementPrice || 0) + (scope.row.refundPrice || 0)) }}
        </template>
      </el-table-column>
    </el-table>
  </ContentWrap>

  <ContentWrap v-hasPermi="['crm:finance:query']" v-loading="loading" class="mt-15px" title="模块类型统计">
    <el-row :gutter="20">
      <el-col v-for="item in typeStatistics" :key="item.label" :span="6">
        <el-card shadow="hover" class="mb-10px">
          <div class="text-14px text-gray-500 mb-8px">{{ item.label }}</div>
          <template v-if="item.details && item.details.length">
            <div v-for="detail in item.details" :key="detail.type" class="flex justify-between text-13px py-3px">
              <span class="text-gray-400">{{ detail.type }}</span>
              <span class="font-600">{{ erpPriceInputFormatter(detail.price) }}</span>
            </div>
          </template>
          <template v-else>
            <div class="text-13px text-gray-400">暂无数据</div>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </ContentWrap>
</template>

<script lang="ts" setup>
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import * as FinanceApi from '@/api/crm/finance'
import * as UserApi from '@/api/system/user'
import { handleTree } from '@/utils/tree'

defineOptions({ name: 'CrmFinance' })

const loading = ref(false)
const userList = ref<UserApi.UserVO[]>([])

const queryParams = reactive({
  year: new Date().getFullYear(),
  ownerUserId: undefined
})
const queryFormRef = ref()

const summaryData = reactive({
  receivablePrice: 0,
  receivableCount: 0,
  invoicePrice: 0,
  invoiceCount: 0,
  reimbursementPrice: 0,
  reimbursementCount: 0,
  refundPrice: 0,
  refundCount: 0
})

const monthlyData = ref<any[]>([])
const typeStatistics = ref<any[]>([])

const loadData = async () => {
  loading.value = true
  try {
    const data = await FinanceApi.getFinanceSummary(queryParams)
    if (data) {
      Object.assign(summaryData, {
        receivablePrice: data.totalReceivablePrice || 0,
        receivableCount: data.receivableCount || 0,
        invoicePrice: data.totalInvoicePrice || 0,
        invoiceCount: data.invoiceCount || 0,
        reimbursementPrice: data.totalReimbursementPrice || 0,
        reimbursementCount: data.reimbursementCount || 0,
        refundPrice: data.totalRefundPrice || 0,
        refundCount: data.refundCount || 0
      })
      monthlyData.value = data.monthlyStats || []
      typeStatistics.value = data.typeStats || []
    }
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  loadData()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  queryParams.year = new Date().getFullYear()
  handleQuery()
}

onMounted(async () => {
  userList.value = handleTree(await UserApi.getSimpleUserList())
  await loadData()
})
</script>

<style lang="scss" scoped>
.summary-card {
  border-left: 4px solid transparent;

  &--green {
    border-left-color: #67c23a;
  }

  &--blue {
    border-left-color: #409eff;
  }

  &--orange {
    border-left-color: #e6a23c;
  }

  &--red {
    border-left-color: #f56c6c;
  }

  &__icon {
    opacity: 0.2;
  }
}
</style>
