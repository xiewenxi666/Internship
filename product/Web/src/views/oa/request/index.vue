<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.request.title')" prop="title">
            <el-input v-model="queryParams.title" :placeholder="t('oa.request.titlePlaceholder')"
              clearable @keyup.enter="handleQuery" class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('common.createTime')" prop="createTime">
            <el-date-picker v-model="queryParams.createTime" :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
              class="!w-240px" :end-placeholder="t('instance.endDate')" :start-placeholder="t('instance.startDate')"
              type="daterange" value-format="YYYY-MM-DD HH:mm:ss" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.query') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['oa:request:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column :label="t('oa.request.id')" align="center" prop="id" />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_REQUEST_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.request.title')" align="center" prop="title" />
      <el-table-column :label="t('oa.request.type')" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_REQUEST_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.request.urgency')" align="center" prop="urgency" />
      <el-table-column :label="t('common.operation')" align="center" width="380">
        <template #default="scope">
          <el-button link type="primary" @click="handleSubmit(scope.row.id)"
            v-if="scope.row.status === -1" v-hasPermi="['oa:request:submit']">{{ t('common.submit') }}</el-button>
          <el-button link type="warning" @click="handleCancel(scope.row.id)"
            v-if="scope.row.status === 1" v-hasPermi="['oa:request:cancel']">{{ t('bpm.processInstance.cancel') }}</el-button>
          <el-button link type="primary" @click="handleReconsider(scope.row.id)"
            v-if="scope.row.status === 3" v-hasPermi="['oa:request:submit']">{{ t('bpm.processInstance.submitAgain') }}</el-button>
          <el-button link type="primary" @click="handleApprovalProgress(scope.row.processInstanceId)"
            v-if="scope.row.status >= 1 && scope.row.processInstanceId" v-hasPermi="['oa:request:query']">{{ t('common.approvalProgress') }}</el-button>
          <el-button link type="primary" @click="openForm('update', scope.row.id)"
            v-if="scope.row.status === -1" v-hasPermi="['oa:request:update']">{{ t('action.edit') }}</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-if="scope.row.status === -1" v-hasPermi="['oa:request:delete']">{{ t('action.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <RequestForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as RequestApi from '@/api/oa/request'
import RequestForm from './RequestForm.vue'
import { useRouter } from 'vue-router'

defineOptions({ name: 'OaRequest' })

const message = useMessage()
const { t } = useI18n()
const router = useRouter()

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  title: '', createTime: []
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await RequestApi.getRequestPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally { loading.value = false }
}
const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

const formRef = ref()
const openForm = (type: string, id?: number) => { formRef.value.open(type, id) }

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await RequestApi.deleteRequest(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleSubmit = async (id: number) => {
  try {
    await message.confirm(t('common.submitConfirm'))
    await RequestApi.submitRequest(id)
    message.success(t('common.submitSuccess'))
    await getList()
  } catch {}
}

const handleCancel = async (id: number) => {
  try {
    await message.confirm(t('common.submitConfirm'))
    await RequestApi.cancelRequest(id)
    message.success(t('common.submitSuccess'))
    await getList()
  } catch {}
}

const handleReconsider = async (id: number) => {
  try {
    await message.confirm(t('common.submitConfirm'))
    await RequestApi.reconsiderRequest(id)
    message.success(t('common.submitSuccess'))
    await getList()
  } catch {}
}

const handleApprovalProgress = (processInstanceId: string) => {
  router.push({ name: 'BpmProcessInstanceDetail', query: { id: processInstanceId } })
}

onMounted(() => { getList() })
</script>
