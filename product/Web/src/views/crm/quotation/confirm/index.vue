<template>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="报价单编号" prop="quotationNo">
            <el-input
              v-model="queryParams.quotationNo"
              class="!w-240px"
              clearable
              placeholder="请输入报价单编号"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="关联客户" prop="customerName">
            <el-input
              v-model="queryParams.customerName"
              class="!w-240px"
              clearable
              placeholder="请输入客户名称"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="负责人" prop="ownerUserId">
            <el-select v-model="queryParams.ownerUserId" clearable filterable placeholder="选择负责人" class="!w-240px">
              <el-option v-for="item in userList" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              {{ t('common.search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('common.reset') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" label="报价单编号" prop="quotationNo" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="handleDetail(scope.row)">
            {{ scope.row.quotationNo }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="关联商机" prop="businessName" min-width="140" />
      <el-table-column align="center" label="关联客户" prop="customerName" min-width="120" />
      <el-table-column align="center" label="负责人" prop="ownerUserName" min-width="100" />
      <el-table-column align="center" label="产品合计（元）" prop="totalAmount" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.totalAmount) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="折后金额（元）" prop="finalAmount" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.finalAmount) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" prop="createTime" min-width="180" />
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="260">
        <template #default="scope">
          <el-button v-hasPermi="['crm:quotation:update']" link type="primary" @click="handleDetail(scope.row)">
            查看详情
          </el-button>
          <el-button v-hasPermi="['crm:quotation:update']" link type="success" @click="handleConfirm(scope.row)">
            确认通过
          </el-button>
          <el-button v-hasPermi="['crm:quotation:update']" link type="danger" @click="handleReject(scope.row)">
            驳回
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>

  <!-- 详情弹窗 -->
  <QuotationForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import download from '@/utils/download'
import * as QuotationApi from '@/api/crm/quotation'
import * as UserApi from '@/api/system/user'
import QuotationForm from '../QuotationForm.vue'

defineOptions({ name: 'CrmQuotationConfirm' })

const { t } = useI18n()
const message = useMessage()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  status: 1, // 只显示待审批的
  quotationNo: undefined as string | undefined,
  customerName: undefined as string | undefined,
  ownerUserId: undefined as number | undefined
})
const queryFormRef = ref()
const userList = ref([])
const formRef = ref()

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await QuotationApi.getQuotationPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

/** 查看详情 */
const handleDetail = (row: any) => {
  formRef.value.open('detail', row.id)
}

/** 确认通过 */
const handleConfirm = async (row: any) => {
  try {
    await message.confirm(`确认通过报价单 ${row.quotationNo}？`)
    await QuotationApi.confirmQuotation(row.id)
    message.success('确认通过成功')
    getList()
  } catch {
    // 用户取消
  }
}

/** 驳回 */
const handleReject = async (row: any) => {
  try {
    await message.confirm(`确定要驳回报价单 ${row.quotationNo}？`)
    await QuotationApi.rejectQuotation(row.id)
    message.success('驳回成功')
    getList()
  } catch {
    // 用户取消
  }
}

/** 格式化价格 */
const formatPrice = (price: number) => {
  if (price === null || price === undefined) return '0.00'
  return price.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

/** 初始化 **/
onMounted(async () => {
  userList.value = await UserApi.getSimpleUserList()
  getList()
})
</script>
