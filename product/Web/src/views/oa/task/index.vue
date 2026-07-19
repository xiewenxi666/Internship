<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.task.title')" prop="title">
            <el-input v-model="queryParams.title" :placeholder="t('oa.task.titlePlaceholder')"
              clearable @keyup.enter="handleQuery" class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('oa.task.status')" prop="status">
            <el-select v-model="queryParams.status" :placeholder="t('oa.task.statusPlaceholder')"
              clearable class="!w-240px">
              <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_TASK_STATUS)" :key="dict.value"
                :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.query') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['oa:task:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column :label="t('oa.task.id')" align="center" prop="id" />
      <el-table-column :label="t('oa.task.title')" align="center" prop="title" />
      <el-table-column :label="t('oa.task.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_TASK_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.task.progress')" align="center" prop="progress" />
      <el-table-column :label="t('oa.task.priority')" align="center" prop="priority" />
      <el-table-column :label="t('oa.task.deadline')" align="center" prop="deadline" min-width="160" :formatter="dateFormatter" />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)"
            v-hasPermi="['oa:task:update']">{{ t('action.edit') }}</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-hasPermi="['oa:task:delete']">{{ t('action.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <TaskForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as TaskApi from '@/api/oa/task'
import TaskForm from './TaskForm.vue'

defineOptions({ name: 'OaTask' })

const message = useMessage()
const { t } = useI18n()

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  title: '', status: undefined
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await TaskApi.getTaskPage(queryParams)
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
    await TaskApi.deleteTask(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

onMounted(() => { getList() })
</script>
