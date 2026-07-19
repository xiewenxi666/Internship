<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.leave.type')" prop="type">
            <el-select v-model="queryParams.type" :placeholder="t('oa.leave.typePlaceholder')" clearable class="!w-240px">
              <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_LEAVE_TYPE)" :key="dict.value"
                :label="dict.label" :value="dict.value" />
            </el-select>
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
            <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['oa:leave:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column :label="t('oa.leave.applyId')" align="center" prop="id" />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_LEAVE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.leave.type')" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_LEAVE_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.leave.reason')" align="center" prop="reason" />
      <el-table-column :label="t('oa.leave.startTime')" align="center" prop="startTime" min-width="160" :formatter="dateFormatter" />
      <el-table-column :label="t('oa.leave.endTime')" align="center" prop="endTime" min-width="160" :formatter="dateFormatter" />
      <el-table-column :label="t('common.operation')" align="center" width="320">
        <template #default="scope">
          <el-button link type="primary" @click="handleSubmit(scope.row.id)"
            v-if="scope.row.status === -1" v-hasPermi="['oa:leave:submit']">{{ t('common.submit') }}</el-button>
          <el-button link type="primary" @click="openForm('update', scope.row.id)"
            v-if="scope.row.status === -1" v-hasPermi="['oa:leave:update']">{{ t('action.edit') }}</el-button>
          <el-button link type="primary" @click="handleApprovalProgress(scope.row.processInstanceId)"
            v-if="scope.row.status >= 1 && scope.row.processInstanceId" v-hasPermi="['oa:leave:query']">{{ t('common.approvalProgress') }}</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-if="scope.row.status === -1" v-hasPermi="['oa:leave:delete']">{{ t('action.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <LeaveForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as LeaveApi from '@/api/oa/leave'
import LeaveForm from './LeaveForm.vue'
import { useRouter } from 'vue-router'

defineOptions({ name: 'OaLeave' })

const message = useMessage()
const { t } = useI18n()
const router = useRouter()

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  type: undefined,
  createTime: []
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await LeaveApi.getLeavePage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally {
    loading.value = false
  }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

const formRef = ref()
const openForm = (type: string, id?: number) => { formRef.value.open(type, id) }

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await LeaveApi.deleteLeave(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleSubmit = async (id: number) => {
  try {
    await message.confirm(t('common.submitConfirm'))
    await LeaveApi.submitLeave(id)
    message.success(t('common.submitSuccess'))
    await getList()
  } catch {}
}

const handleApprovalProgress = (processInstanceId: string) => {
  router.push({ name: 'BpmProcessInstanceDetail', query: { id: processInstanceId } })
}

onMounted(() => { getList() })
</script>
