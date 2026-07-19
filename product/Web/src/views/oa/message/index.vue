<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.message.title')" prop="title">
            <el-input v-model="queryParams.title" :placeholder="t('oa.message.titlePlaceholder')"
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
            <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['oa:message:send']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column :label="t('oa.message.id')" align="center" prop="id" />
      <el-table-column :label="t('oa.message.title')" align="center" prop="title" />
      <el-table-column :label="t('oa.message.receiverUserId')" align="center" prop="receiverUserId" />
      <el-table-column :label="t('oa.message.status')" align="center" prop="status">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_MESSAGE_STATUS" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column :label="t('common.createTime')" align="center" prop="createTime" min-width="160" :formatter="dateFormatter" />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-hasPermi="['oa:message:delete']">{{ t('action.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <MessageForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as MessageApi from '@/api/oa/message'
import MessageForm from './MessageForm.vue'

defineOptions({ name: 'OaMessage' })

const message = useMessage()
const { t } = useI18n()

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
    const data = await MessageApi.getMessagePage(queryParams)
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
    await MessageApi.deleteMessage(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

onMounted(() => { getList() })
</script>
