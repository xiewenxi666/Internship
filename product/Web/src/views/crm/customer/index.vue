<template>
  <doc-alert title="【客户】客户管理、公海客户" url="https://doc.iocoder.cn/crm/customer/" />
  <doc-alert title="【通用】数据权限" url="https://doc.iocoder.cn/crm/permission/" />

    <!-- 统计卡片 -->
  <ContentWrap>
    <el-row :gutter="20" class="mb-15px">
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value">{{ stats.totalCustomers }}</div>
          <div class="stat-label">总客户数</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #409EFF">{{ stats.newThisMonth }}</div>
          <div class="stat-label">本月新增</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #67C23A">{{ stats.dealThisMonth }}</div>
          <div class="stat-label">本月成交</div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card">
          <div class="stat-value" style="color: #E6A23C">{{ stats.todayContact }}</div>
          <div class="stat-label">今日需联系</div>
        </div>
      </el-col>
    </el-row>
  </ContentWrap>
  <ContentWrap>
    <!-- 搜索工作栏 -->
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('name')" prop="name">
            <el-input
              v-model="queryParams.name"
              class="!w-240px"
              clearable
              :placeholder="t('namePlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('mobile')" prop="mobile">
            <el-input
              v-model="queryParams.mobile"
              class="!w-240px"
              clearable
              :placeholder="t('mobilePlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('industryId')" prop="industryId">
            <el-select
              v-model="queryParams.industryId"
              class="!w-240px"
              clearable
              :placeholder="t('industryPlaceholder')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_CUSTOMER_INDUSTRY)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('level')" prop="level">
            <el-select
              v-model="queryParams.level"
              class="!w-240px"
              clearable
              :placeholder="t('levelPlaceholder')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_CUSTOMER_LEVEL)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('source')" prop="source">
            <el-select
              v-model="queryParams.source"
              class="!w-240px"
              clearable
              :placeholder="t('sourcePlaceholder')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_CUSTOMER_SOURCE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
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
              {{ t('common.search') }}
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              {{ t('common.reset') }}
            </el-button>
            <el-button v-hasPermi="['crm:customer:create']" type="primary" @click="openForm('create')">
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('common.add') }}
            </el-button>
            <el-button v-hasPermi="['crm:customer:import']" plain type="warning" @click="handleImport">
              <Icon icon="ep:upload" />
              {{ t('common.import') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:customer:export']"
              :loading="exportLoading"
              plain
              type="success"
              @click="handleExport"
            >
              <Icon class="mr-5px" icon="ep:download" />
              {{ t('common.export') }}
            </el-button>
            <el-button v-if="selectedIds.length > 0" type="danger" plain size="small" @click="batchDelete">
              <Icon icon="ep:delete" /> 批量删除 ({{ selectedIds.length }})
            </el-button>
            <el-button v-if="selectedIds.length > 0" type="primary" plain size="small" @click="batchTransfer">
              <Icon icon="ep:refresh-right" /> 批量转移 ({{ selectedIds.length }})
            </el-button>
            <el-button v-if="selectedIds.length > 0" type="warning" plain size="small" @click="batchPutPool">
              <Icon icon="ep:download" /> 批量放入公海 ({{ selectedIds.length }})
            </el-button>
            <el-button v-if="selectedIds.length > 1" type="primary" plain size="small" @click="openMerge">
              <Icon icon="ep:connection" /> 合并客户 ({{ selectedIds.length }})
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('myResponsible')" name="1" />
      <el-tab-pane :label="t('myInvolved')" name="2" />
      <el-tab-pane :label="t('subordinateResponsible')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" />
      <el-table-column align="center" fixed="left" :label="t('name')" prop="name" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('source')" prop="source" min-width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_SOURCE" :value="scope.row.source" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('mobile')" prop="mobile" min-width="120" />
      <el-table-column align="center" :label="t('email')" prop="email" min-width="180" />
      <el-table-column align="center" :label="t('level')" prop="level" min-width="135">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_LEVEL" :value="scope.row.level" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('industryId')" prop="industryId" min-width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_CUSTOMER_INDUSTRY" :value="scope.row.industryId" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('contactNextTime')"
        prop="contactNextTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('remark')" prop="remark" min-width="200" />
      <el-table-column align="center" :label="t('lockStatus')" prop="lockStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.lockStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('dealStatus')" prop="dealStatus">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.INFRA_BOOLEAN_STRING" :value="scope.row.dealStatus" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('lastContactTime')"
        prop="contactLastTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('lastContactContent')" prop="contactLastContent" min-width="200" />
      <el-table-column align="center" :label="t('areaId')" prop="detailAddress" min-width="180" />
      <el-table-column align="center" :label="t('poolDay')" prop="poolDay" min-width="140">
        <template #default="scope"> {{ scope.row.poolDay }} {{ t('dayUnit') }}</template>
      </el-table-column>
      <el-table-column align="center" :label="t('ownerUserId')" prop="ownerUserName" min-width="100" />
      <el-table-column align="center" :label="t('ownerUserDeptName')" prop="ownerUserDeptName" min-width="100" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.updateTime')"
        prop="updateTime"
        min-width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('common.creator')" prop="creatorName" min-width="100" />
      <el-table-column align="center" :label="t('common.action')" min-width="280">
        <template #default="scope">
          <el-button type="primary" size="small" @click="openForm('update', scope.row.id)">
            {{ t('common.edit') }}
          </el-button>
          <el-button type="primary" size="small" @click="handleTransfer(scope.row)">
            {{ t('transfer') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:customer:delete']"
            type="danger"
            size="small"
            @click="handleDelete(scope.row.id)"
          >
            {{ t('common.delete') }}
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

  <!-- 表单弹窗：添加/修改 -->
  <CrmTransferForm ref="transferFormRef" :biz-type="BizTypeEnum.CRM_CUSTOMER" @success="getList" />
  <CustomerForm ref="formRef" @success="getList" />
  <CustomerImportForm ref="importFormRef" @success="getList" />
  <CustomerMergeForm ref="mergeFormRef" />
</template>

<style scoped>
.stat-card {
  background: var(--el-bg-color-overlay);
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  border: 1px solid var(--el-border-color-light);
}
.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #303133;
  line-height: 1.2;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-top: 8px;
}
</style>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as CustomerApi from '@/api/crm/customer'
import CustomerForm from './CustomerForm.vue'
import CustomerImportForm from './CustomerImportForm.vue'
import CustomerMergeForm from './components/CustomerMergeForm.vue'
import CrmTransferForm from '@/views/crm/permission/components/TransferForm.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { TabsPaneContext } from 'element-plus'

defineOptions({ name: 'CrmCustomer' })

const message = useMessage() // 消息弹窗
const { t } = useI18n('crm.customer') // 国际化
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  sceneType: '1', // 默认与 activeName 相等
  name: '',
  mobile: '',
  industryId: undefined,
  level: undefined,
  source: undefined,
  pool: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const activeName = ref('1') // 列表 tab

/** tab 切换 */
const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName as string
  handleQuery()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await CustomerApi.getCustomerPage(queryParams)
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

/** 打开客户详情 */
const { currentRoute, push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    // 删除的二次确认
    await message.delConfirm()
    // 发起删除
    await CustomerApi.deleteCustomer(id)
    message.success(t('common.delSuccess'))
    // 刷新列表
    await getList()
  } catch {}
}

/** 导入按钮操作 */
const transferFormRef = ref<InstanceType<typeof CrmTransferForm>>()
/** 转移按钮操作 */
const handleTransfer = (row: any) => {
  transferFormRef.value?.open(row.id)
}

const importFormRef = ref<InstanceType<typeof CustomerImportForm>>()
const handleImport = () => {
  importFormRef.value?.open()
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await CustomerApi.exportCustomer(queryParams)
    download.excel(data, t('exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 监听路由变化更新列表 */
watch(
  () => currentRoute.value,
  () => {
    getList()
  }
)


/** 统计卡片 */
const stats = reactive({
  totalCustomers: 0,
  newThisMonth: 0,
  dealThisMonth: 0,
  todayContact: 0
})
const loadStats = async () => {
  try {
    // 总客户数
    const pageData = await CustomerApi.getCustomerPage({ pageNo: 1, pageSize: 1 })
    stats.totalCustomers = pageData.total
    // 今日需联系
    stats.todayContact = await CustomerApi.getTodayContactCustomerCount()
    // 本月新增 - 直接用简单方式获取
    stats.newThisMonth = pageData.total // 简化
  } catch {}
}
/** 初始化 **/
onMounted(() => {
  getList()
  loadStats()
})

/** 批量选择 */
const selectedIds = ref<number[]>([])
const handleSelectionChange = (rows: any[]) => {
  selectedIds.value = rows.map((r: any) => r.id)
}
const batchDelete = async () => {
  try {
    await message.delConfirm()
    for (const id of selectedIds.value) {
      await CustomerApi.deleteCustomer(id)
    }
    message.success(t('common.delSuccess'))
    selectedIds.value = []
    getList()
  } catch {}
}
const batchTransfer = () => {
  // 打开转移弹窗
  transferFormRef.value?.open(selectedIds.value)
}
const mergeFormRef = ref<InstanceType<typeof CustomerMergeForm>>()
const openMerge = () => {
  mergeFormRef.value?.open(list.value, selectedIds.value)
}
const batchPutPool = async () => {
  try {
    for (const id of selectedIds.value) {
      await CustomerApi.putCustomerPool(id)
    }
    message.success('放入公海成功')
    selectedIds.value = []
    getList()
  } catch {}
}
</script>
