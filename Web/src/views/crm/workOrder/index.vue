<!-- -23计算机科学与技术2班-龚小波 -->
<template>
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
          <el-form-item :label="t('workOrder.no')" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              :placeholder="t('workOrder.noPlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workOrder.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              class="!w-240px"
              clearable
              :placeholder="t('workOrder.titlePlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('workOrder.type')" prop="type">
            <el-select
              v-model="queryParams.type"
              class="!w-240px"
              clearable
              :placeholder="t('common.selectPlaceholder')"
              @keyup.enter="handleQuery"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.CRM_WORK_ORDER_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item :label="t('workOrder.priority')" prop="priority">
              <el-select
                v-model="queryParams.priority"
                class="!w-240px"
                clearable
                :placeholder="t('common.selectPlaceholder')"
                @keyup.enter="handleQuery"
              >
                <el-option
                  v-for="dict in getIntDictOptions(DICT_TYPE.CRM_WORK_ORDER_PRIORITY)"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
        </el-col>
        <el-col :span="8">
            <el-form-item :label="t('workOrder.status')" prop="status">
              <el-select
                v-model="queryParams.status"
                class="!w-240px"
                clearable
                :placeholder="t('common.selectPlaceholder')"
                @keyup.enter="handleQuery"
              >
                <el-option
                  v-for="dict in getIntDictOptions(DICT_TYPE.CRM_WORK_ORDER_STATUS)"
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
            <el-button
              v-hasPermi="['crm:work-order:create']"
              plain
              type="primary"
              @click="openForm('create')"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
            <el-button
              v-hasPermi="['crm:work-order:export']"
              :loading="exportLoading"
              plain
              type="success"
              @click="handleExport"
            >
              <Icon class="mr-5px" icon="ep:download" />
              {{ t('common.export') }}
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
      <el-tab-pane :label="t('customer.subordinateResponsible')" name="3" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
      <el-table-column align="center" fixed="left" :label="t('workOrder.no')" prop="no" min-width="160" />
      <el-table-column align="center" :label="t('workOrder.title')" prop="title" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('workOrder.type')" prop="type" min-width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_WORK_ORDER_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('workOrder.priority')" prop="priority" min-width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_WORK_ORDER_PRIORITY" :value="scope.row.priority" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('workOrder.status')" prop="status" min-width="100">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_WORK_ORDER_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('workOrder.ownerUserName')" prop="ownerUserName" min-width="120" />
      <el-table-column align="center" :label="t('workOrder.solution')" prop="solution" min-width="150" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('workOrder.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('workOrder.updateTime')"
        prop="updateTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('workOrder.creatorName')" prop="creatorName" min-width="100" />
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="240">
        <template #default="scope">
          <el-button
            v-if="scope.row.status === 1"
            v-hasPermi="['crm:work-order:update']"
            link
            type="warning"
            @click="handleProcess(scope.row.id)"
          >
            {{ t('workOrder.processBtn') }}
          </el-button>
          <el-dropdown
            v-if="scope.row.status === 2"
            v-hasPermi="['crm:work-order:update']"
            @command="(action: string) => handleActionOpen(scope.row.id, action)"
          >
            <el-button link type="primary">
              {{ t('common.operation') }}
              <Icon icon="ep:arrow-down" />
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="complete">
                  <Icon class="mr-5px" icon="ep:circle-check" />
                  {{ t('workOrder.completeBtn') }}
                </el-dropdown-item>
                <el-dropdown-item command="return">
                  <Icon class="mr-5px" icon="ep:circle-close" />
                  {{ t('workOrder.returnBtn') }}
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
          <el-button
            v-hasPermi="['crm:work-order:update']"
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            :disabled="scope.row.status === 3"
          >
            {{ t('common.edit') }}
          </el-button>
          <el-button
            v-hasPermi="['crm:work-order:delete']"
            link
            type="danger"
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
  <WorkOrderForm ref="formRef" @success="getList" />

  <!-- 操作对话框 -->
  <Dialog v-model="actionDialogVisible" :title="actionDialogTitle">
    <el-form ref="actionFormRef" :model="actionFormData" :rules="actionFormRules" label-width="auto">
      <el-form-item :label="t('workOrder.solutionInput')" prop="solution">
        <el-input
          v-model="actionFormData.solution"
          type="textarea"
          :rows="3"
          :placeholder="t('workOrder.solutionRequired')"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button :disabled="actionFormLoading" type="primary" @click="handleActionSubmit">
        {{ t('dialog.confirm') }}
      </el-button>
      <el-button @click="actionDialogVisible = false">{{ t('dialog.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import download from '@/utils/download'
import * as WorkOrderApi from '@/api/crm/workOrder'
import WorkOrderForm from './WorkOrderForm.vue'
import { TabsPaneContext } from 'element-plus'

defineOptions({ name: 'WorkOrder' })

const message = useMessage()
const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  sceneType: '1',
  no: undefined,
  title: undefined,
  type: undefined,
  priority: undefined,
  status: undefined
})
const queryFormRef = ref()
const exportLoading = ref(false)
const activeName = ref('1')

const actionDialogVisible = ref(false)
const actionDialogTitle = ref('')
const actionFormLoading = ref(false)
const actionFormData = reactive({ id: 0, action: '' as string, solution: '' })
const actionFormRef = ref()
const actionFormRules = reactive({
  solution: [{ required: true, message: t('workOrder.solutionRequired'), trigger: 'blur' }]
})

const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.sceneType = tab.paneName as string
  handleQuery()
}

const getList = async () => {
  loading.value = true
  try {
    const data = await WorkOrderApi.getWorkOrderPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}

const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await WorkOrderApi.deleteWorkOrder(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleProcess = async (id: number) => {
  try {
    await message.confirm(t('workOrder.processConfirm'))
    await WorkOrderApi.processWorkOrder(id)
    message.success(t('workOrder.processSuccess'))
    await getList()
  } catch {}
}

const handleActionOpen = (id: number, action: string) => {
  actionFormData.id = id
  actionFormData.action = action
  actionFormData.solution = ''
  actionDialogTitle.value = action === 'complete' ? t('workOrder.completionTitle') : t('workOrder.returnBtn')
  actionDialogVisible.value = true
}

const handleActionSubmit = async () => {
  if (!actionFormRef.value) return
  const valid = await actionFormRef.value.validate()
  if (!valid) return
  actionFormLoading.value = true
  try {
    if (actionFormData.action === 'complete') {
      await WorkOrderApi.completeWorkOrder(actionFormData.id, actionFormData.solution)
      message.success(t('workOrder.completeSuccess'))
    } else {
      await WorkOrderApi.returnWorkOrder(actionFormData.id)
      message.success(t('workOrder.returnSuccess'))
    }
    actionDialogVisible.value = false
    await getList()
  } catch {} finally {
    actionFormLoading.value = false
  }
}

const handleExport = async () => {
  try {
    await message.exportConfirm()
    exportLoading.value = true
    const data = await WorkOrderApi.exportWorkOrderExcel(queryParams)
    download.excel(data, t('workOrder.exportFileName') + '.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmWorkOrderDetail', params: { id } })
}

onMounted(async () => {
  await getList()
})
</script>
