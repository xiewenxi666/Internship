<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.workReport.title')" prop="title">
            <el-input v-model="queryParams.title" :placeholder="t('oa.workReport.titlePlaceholder')"
              clearable @keyup.enter="handleQuery" class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('oa.workReport.type')" prop="type">
            <el-select v-model="queryParams.type" :placeholder="t('oa.workReport.typePlaceholder')"
              clearable class="!w-240px">
              <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_WORK_REPORT_TYPE)" :key="dict.value"
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
            <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['oa:work-report:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column :label="t('oa.workReport.id')" align="center" prop="id" />
      <el-table-column :label="t('common.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_WORK_REPORT_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.workReport.title')" align="center" prop="title" />
      <el-table-column :label="t('oa.workReport.type')" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_WORK_REPORT_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.workReport.reportDate')" align="center" prop="reportDate" min-width="120" />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)"
            v-hasPermi="['oa:work-report:update']">{{ t('action.edit') }}</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-hasPermi="['oa:work-report:delete']">{{ t('action.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <WorkReportForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as WorkReportApi from '@/api/oa/workReport'
import WorkReportForm from './WorkReportForm.vue'

defineOptions({ name: 'OaWorkReport' })

const message = useMessage()
const { t } = useI18n()

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  title: '', type: undefined
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await WorkReportApi.getWorkReportPage(queryParams)
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
    await WorkReportApi.deleteWorkReport(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

onMounted(() => { getList() })
</script>
