<template>
  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="任务标题" prop="title">
            <el-input
              v-model="queryParams.title"
              class="!w-240px"
              clearable
              placeholder="请输入任务标题"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('common.status')" prop="status">
            <el-select
              v-model="queryParams.status"
              class="!w-240px"
              clearable
              placeholder="请选择任务状态"
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
            <el-button
              plain
              type="primary"
              @click="openForm('create')"
              v-hasPermi="['oa:task:create']"
            >
              <Icon class="mr-5px" icon="ep:plus" />
              {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column align="center" label="编号" prop="id" width="70" />
      <el-table-column align="center" label="标题" prop="title" min-width="160" />
      <el-table-column align="center" label="优先级" prop="priority" width="80">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.SYSTEM_NOTICE_TYPE" :value="scope.row.priority" />
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('common.status')" prop="status" width="90">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.BPM_PROCESS_INSTANCE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="负责人" prop="assigneeId" width="100" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="截止时间"
        prop="deadline"
        min-width="160"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="完成时间"
        prop="completedTime"
        min-width="160"
      />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('common.operation')" min-width="200">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['oa:task:update']"
          >
            {{ t('action.edit') }}
          </el-button>
          <el-button
            link
            type="success"
            @click="handleComplete(scope.row.id)"
            v-if="scope.row.status !== 2"
            v-hasPermi="['oa:task:update']"
          >
            完成
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['oa:task:delete']"
          >
            {{ t('action.del') }}
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

  <OaTaskForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as TaskApi from '@/api/oa/task'
import OaTaskForm from './OaTaskForm.vue'

defineOptions({ name: 'OaTask' })

const message = useMessage()
const { t } = useI18n('bpm')

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: '',
  status: undefined
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await TaskApi.getTaskPage(queryParams)
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
    await TaskApi.deleteTask(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

const handleComplete = async (id: number) => {
  try {
    await message.confirm('确认完成该任务？')
    await TaskApi.completeTask(id)
    message.success('操作成功')
    await getList()
  } catch {}
}

onMounted(() => {
  getList()
})
</script>
