<template>
  <doc-alert title="【回款】回款管理、回款计划" url="https://doc.iocoder.cn/crm/receivable/" />
  <doc-alert title="【通用】数据权限" url="https://doc.iocoder.cn/crm/permission/" />

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
          <el-form-item :label="t('receivable.no')" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              :placeholder="t('receivable.noPlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('receivable.customerName')" prop="customerId">
            <el-select
              v-model="queryParams.customerId"
              class="!w-240px"
              :placeholder="t('customer.ownerUserPlaceholder')"
              @keyup.enter="handleQuery"
            >
              <el-option
                v-for="item in customerList"
                :key="item.id"
                :label="item.name"
                :value="item.id"
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
            <el-button
              v-hasPermi="['crm:receivable:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:receivable:export']"
              :loading="exportLoading"
              plain
              type="success"
              @click="handleExport"
            >
              <Icon class="mr-5px" icon="ep:download" />
              {{ t('common.export') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:receivable:query']"
              plain
              type="info"
              @click="goToReport"
            >
              <Icon class="mr-5px" icon="ep:document" />
              回款记录报表
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane :label="t('customer.myResponsible')" name="1" />
      <el-tab-pane :label="t('customer.myInvolved')" name="2" />
      <el-tab-pane :label="t('customer.subordinateResponsible')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" fixed="left" :label="t('receivable.no')" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('receivable.customerName')" prop="customerName" min-width="120">
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openCustomerDetail(scope.row.customerId)"
          >
            {{ scope.row.customerName }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('receivable.contractNo')" prop="contractNo" min-width="180">
        <template #default="scope">
          <el-link
            :underline="false"
            type="primary"
            @click="openContractDetail(scope.row.contractId)"
          >
            {{ scope.row.contract.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        :label="t('receivable.returnTime')"
        prop="returnTime"
        min-width="150"
      />
      <el-table-column
        align="center"
        :label="t('receivable.price') + '（元）'"
        prop="price"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" :label="t('receivable.returnType')" prop="returnType" min-width="130">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_RECEIVABLE_RETURN_TYPE" :value="scope.row.returnType" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('receivable.remark')" prop="remark" min-width="200" />
      <el-table-column
        align="center"
        :label="t('receivable.contractPrice') + '（元）'"
        prop="contract.totalPrice"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column align="center" :label="t('receivable.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column align="center" :label="t('receivable.ownerUserDeptName')" prop="ownerUserDeptName" min-width="100" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('receivable.updateTime')"
        prop="updateTime"
        min-width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('receivable.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('receivable.creatorName')" prop="creatorName" min-width="120" />
      <el-table-column align="center" fixed="right" :label="t('receivable.auditStatus')" prop="auditStatus" min-width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="320">
        <template #default="scope">
          <div style="display: flex; flex-wrap: nowrap; gap: 6px; align-items: center; justify-content: center;">
            <el-button
              v-hasPermi="['crm:receivable:query']"
              link
              type="primary"
              size="small"
              @click="openDetail(scope.row.id)"
            >
              {{ t('common.detail') }}
            </el-button>
            <el-button
              v-if="scope.row.auditStatus === 0 || scope.row.auditStatus === 30 || scope.row.auditStatus === 40 || scope.row.auditStatus === 50"
              v-hasPermi="['crm:receivable:update']"
              link
              type="primary"
              size="small"
              @click="openForm('update', scope.row.id)"
            >
              编辑
            </el-button>
            <el-button
              v-if="scope.row.auditStatus === 0 || scope.row.auditStatus === 30 || scope.row.auditStatus === 40 || scope.row.auditStatus === 50"
              v-hasPermi="['crm:receivable:update']"
              link
              type="success"
              size="small"
              @click="handleSubmit(scope.row.id)"
            >
              提交审核
            </el-button>
            <el-button
              v-if="scope.row.auditStatus === 10"
              v-hasPermi="['crm:receivable:update']"
              link
              type="danger"
              size="small"
              @click="handleCancel(scope.row.id)"
            >
              撤销审批
            </el-button>
            <el-button
              v-if="scope.row.auditStatus !== 10 && scope.row.auditStatus !== 20"
              v-hasPermi="['crm:receivable:delete']"
              link
              type="danger"
              size="small"
              @click="handleDelete(scope.row.id)"
            >
              {{ t('common.delete') }}
            </el-button>
          </div>
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
  <ReceivableForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import download from '@/utils/download'
import * as ReceivableApi from '@/api/crm/receivable'
import ReceivableForm from './ReceivableForm.vue'
import * as CustomerApi from '@/api/crm/customer'
import { TabsPaneContext } from 'element-plus'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'Receivable' })

const message = useMessage() // 消息弹窗
const { t } = useI18n('crm') // 国际化
const loading = ref(true) // 列表的加载中
const total = ref(0) // 列表的总页数
const list = ref([]) // 列表的数据
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  sceneType: '1', // 默认与 activeName 相等
  no: undefined,
  customerId: undefined
})
const queryFormRef = ref() // 搜索的表单
const exportLoading = ref(false) // 导出的加载中
const activeName = ref('1') // 列表 tab
const customerList = ref<CustomerApi.CustomerVO[]>([]) // 客户列表

/** tab 切换 */
const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName
  handleQuery()
}

/** 查询列表 */
const getList = async () => {
  loading.value = true
  try {
    const data = await ReceivableApi.getReceivablePage(queryParams)
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

/** 添加/修改操作 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 删除按钮操作 */
const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await ReceivableApi.deleteReceivable(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

/** 提交审核 */
const handleSubmit = async (id: number) => {
  try {
    await message.confirm('确定提交该回款审核吗？')
    await ReceivableApi.submitReceivable(id)
    message.success('提交审核成功')
    await getList()
  } catch {}
}

/** 撤销审批 */
const handleCancel = async (id: number) => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入撤销原因（可不填）：', '撤销审批', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入原因',
      inputType: 'textarea'
    })
    await ReceivableApi.cancelReceivable(id, reason || undefined)
    message.success('撤销审批成功')
    await getList()
  } catch {}
}

/** 打开回款详情 */
const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmReceivableDetail', params: { id } })
}

/** 打开客户详情 */
const openCustomerDetail = (id: number) => {
  push({ name: 'CrmCustomerDetail', params: { id } })
}

/** 打开合同详情 */
const openContractDetail = (id: number) => {
  push({ name: 'CrmContractDetail', params: { id } })
}

/** 导出按钮操作 */
const handleExport = async () => {
  try {
    // 导出的二次确认
    await message.exportConfirm()
    // 发起导出
    exportLoading.value = true
    const data = await ReceivableApi.exportReceivable(queryParams)
    download.excel(data, t('receivable.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

/** 查看回款记录报表 */
const goToReport = () => {
  push({ name: 'CrmReceivableReport' })
}

/** 初始化 **/
onMounted(async () => {
  await getList()
  // 获得客户列表
  customerList.value = await CustomerApi.getCustomerSimpleList()
})
</script>
