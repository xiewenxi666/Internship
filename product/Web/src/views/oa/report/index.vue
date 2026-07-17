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
          <el-form-item label="报告类型" prop="type">
            <el-select
              v-model="queryParams.type"
              class="!w-240px"
              clearable
              placeholder="请选择报告类型"
            >
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.OA_REPORT_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="报告日期" prop="reportDate">
            <el-date-picker
              v-model="queryParams.reportDate"
              :default-time="[new Date('1 00:00:00'), new Date('1 23:59:59')]"
              class="!w-240px"
              end-placeholder="结束日期"
              start-placeholder="开始日期"
              type="daterange"
              value-format="YYYY-MM-DD"
            />
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
              v-hasPermi="['oa:report:create']"
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
      <el-table-column align="center" label="编号" prop="id" />
      <el-table-column align="center" label="报告类型" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_REPORT_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="报告日期"
        prop="reportDate"
        min-width="120"
      />
      <el-table-column align="center" label="工作内容" prop="content" min-width="200" show-overflow-tooltip />
      <el-table-column align="center" label="工作计划" prop="plan" min-width="200" show-overflow-tooltip />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        :label="t('common.createTime')"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" :label="t('common.operation')" min-width="160">
        <template #default="scope">
          <el-button
            link
            type="primary"
            @click="openForm('update', scope.row.id)"
            v-hasPermi="['oa:report:update']"
          >
            {{ t('action.edit') }}
          </el-button>
          <el-button
            link
            type="danger"
            @click="handleDelete(scope.row.id)"
            v-hasPermi="['oa:report:delete']"
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

  <OaReportForm ref="formRef" @success="getList" />
</template>
<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as ReportApi from '@/api/oa/report'
import OaReportForm from './OaReportForm.vue'

defineOptions({ name: 'OaReport' })

const message = useMessage()
const { t } = useI18n('bpm')

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  type: undefined,
  reportDate: []
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await ReportApi.getReportPage(queryParams)
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
    await ReportApi.deleteReport(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

onMounted(() => {
  getList()
})
</script>
