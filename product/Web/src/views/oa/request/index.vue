<template>
  <doc-alert title="审批接入（业务表单）" url="https://doc.iocoder.cn/bpm/use-business-form/" />

  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.request.title')" prop="title">
            <el-input
              v-model="queryParams.title"
              class="!w-240px"
              clearable
              :placeholder="t('oa.request.titlePlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('oa.request.applyTime')" prop="createTime">
            <el-date-picker
              v-model="queryParams.createTime"
              :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
              class="!w-240px"
              :end-placeholder="t('process.instance.endDate')"
              :start-placeholder="t('process.instance.startDate')"
              type="daterange"
              value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('oa.request.approvalResult')" prop="status">
            <el-select
              v-model="queryParams.status"
              class="!w-240px"
              clearable
              :placeholder="t('oa.request.approvalResultPlaceholder')"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS)"
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
            <el-button plain type="primary" @click="handleCreate()">
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('oa.request.createLeave') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column align="center" :label="t('oa.request.applyId')" prop="id" />
      <el-table-column align="center" :label="t('common.status')" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('oa.request.title')" prop="title" />
      <el-table-column align="center" :label="t('oa.request.content')" prop="content">
        <template #default="scope">
          <el-text truncated>{{ scope.row.content }}</el-text>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('oa.request.urgency')" prop="urgency">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_REQUEST_URGENCY" :value="scope.row.urgency" />
        </template>
      </el-table-column>
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('oa.request.applyTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('common.operation')" min-width="200">
        <template #default="scope">
          <el-button
            v-hasPermi="['oa:request:query']"
            link
            type="primary"
            @click="handleDetail(scope.row)"
          >
            {{ t('common.detail') }}
          </el-button>
          <el-button
            v-hasPermi="['oa:request:query']"
            link
            type="primary"
            @click="handleProcessDetail(scope.row)"
          >
            {{ t('oa.request.progress') }}
          </el-button>
          <el-button
            v-if="scope.row.result === 1"
            v-hasPermi="['oa:request:create']"
            link
            type="danger"
            @click="cancelRequest(scope.row)"
          >
            {{ t('approval.cancel') }}
          </el-button>
          <el-button
            v-if="scope.row.status !== 1"
            v-hasPermi="['oa:request:create']"
            link
            type="primary"
            @click="handleReCreate(scope.row)"
          >
            {{ t('process.instance.restart') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      v-model:limit="queryParams.pageSize"
      v-model:page="queryParams.pageNo"
      :total="total"
      @pagination="getList"
    />
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as RequestApi from '@/api/oa/request'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'

defineOptions({ name: 'OaRequest' })

const message = useMessage()
const router = useRouter()
const { t } = useI18n('bpm')

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: undefined,
  status: undefined,
  createTime: []
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await RequestApi.getRequestPage(queryParams)
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

const handleCreate = () => {
  router.push({ name: 'OARequestCreate' })
}

const handleReCreate = (row: RequestApi.RequestVO) => {
  router.push({
    name: 'OARequestCreate',
    query: {
      id: row.id
    }
  })
}

const handleDetail = (row: RequestApi.RequestVO) => {
  router.push({
    name: 'OARequestDetail',
    query: {
      id: row.id
    }
  })
}

const cancelRequest = async (row) => {
  const { value } = await ElMessageBox.prompt(t('process.instance.cancelReason'), t('process.instance.cancelTitle'), {
    confirmButtonText: t('common.ok'),
    cancelButtonText: t('common.cancel'),
    inputPattern: /^[\s\S]*.*\S[\s\S]*$/,
    inputErrorMessage: t('process.instance.cancelReasonRequired')
  })
  await ProcessInstanceApi.cancelProcessInstanceByStartUser(row.id, value)
  message.success(t('process.instance.cancelSuccess'))
  await getList()
}

const handleProcessDetail = (row) => {
  router.push({
    name: 'BpmProcessInstanceDetail',
    query: {
      id: row.processInstanceId
    }
  })
}

watch(
  () => router.currentRoute.value,
  () => {
    getList()
  }
)

onMounted(() => {
  getList()
})
</script>
