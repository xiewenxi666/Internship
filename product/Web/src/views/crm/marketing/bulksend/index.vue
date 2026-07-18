<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="任务标题" prop="title">
            <el-input v-model="queryParams.title" placeholder="请输入标题" clearable class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="类型" prop="type">
            <el-select v-model="queryParams.type" class="!w-240px" clearable>
              <el-option label="短信群发" :value="1" />
              <el-option label="邮件群发" :value="2" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" class="!w-240px" clearable>
              <el-option label="未提交" :value="1" />
              <el-option label="待审核" :value="2" />
              <el-option label="待发送" :value="3" />
              <el-option label="已发送" :value="4" />
              <el-option label="已驳回" :value="5" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> 搜索</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> 重置</el-button>
            <el-button type="primary" @click="openForm('create')">
              <Icon icon="ep:plus" class="mr-5px" /> 新建群发
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true">
      <el-table-column label="任务标题" align="center" prop="title" min-width="160" />
      <el-table-column label="类型" align="center" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.type === 1 ? '' : 'warning'">{{ scope.row.type === 1 ? '短信' : '邮件' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="目标数量" align="center" prop="targetCount" min-width="90" />
      <el-table-column label="成功" align="center" prop="successCount" min-width="90" />
      <el-table-column label="失败" align="center" prop="failCount" min-width="90" />
      <el-table-column label="状态" align="center" min-width="100">
        <template #default="scope">
          <el-tag :type="statusTagType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="负责人员" align="center" prop="ownerUserName" min-width="100" />
      <el-table-column label="创建时间" align="center" prop="createTime" :formatter="dateFormatter" min-width="160" />
      <el-table-column label="操作" align="center" min-width="200" fixed="right">
        <template #default="scope">
          <el-button v-if="scope.row.status === 1" link type="primary" @click="openForm('update', scope.row.id)">编辑</el-button>
          <el-button v-if="scope.row.status === 1" link type="success" @click="handleSubmit(scope.row.id)">提交审核</el-button>
          <el-button v-if="scope.row.status === 2" link type="success" @click="handleApprove(scope.row.id)">通过</el-button>
          <el-button v-if="scope.row.status === 2" link type="warning" @click="handleReject(scope.row.id)">驳回</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize" @pagination="getList" />
  </ContentWrap>

  <BulkSendForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import * as BulkSendApi from '@/api/crm/bulksend'
import BulkSendForm from './BulkSendForm.vue'
import { dateFormatter } from '@/utils/formatTime'

defineOptions({ name: 'CrmBulkSend' })

const message = useMessage()
const loading = ref(false)
const list = ref<BulkSendApi.BulkSendVO[]>([])
const total = ref(0)
const queryParams = reactive({ pageNo: 1, pageSize: 10, title: null, type: null, status: null })

const getList = async () => {
  loading.value = true
  try {
    const data = await BulkSendApi.getBulkSendPage(queryParams)
    list.value = data.list; total.value = data.total
  } finally { loading.value = false }
}

const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { Object.assign(queryParams, { pageNo:1, pageSize:10, title:null, type:null, status:null }); getList() }

const formRef = ref()
const openForm = (type: string, id?: number) => { formRef.value?.open(type, id) }

const handleSubmit = async (id: number) => {
  try {
    await message.confirm('确认提交审核？')
    await BulkSendApi.submitForApproval(id)
    message.success('提交成功')
    await getList()
  } catch {}
}

const handleApprove = async (id: number) => {
  try { await message.confirm('确认审批通过？'); await BulkSendApi.approve(id); message.success('已通过'); await getList() } catch {}
}

const handleReject = async (id: number) => {
  try { await message.confirm('确认驳回？'); await BulkSendApi.reject(id); message.success('已驳回'); await getList() } catch {}
}

const handleDelete = async (id: number) => {
  try {
    await message.confirm('确认删除？')
    await BulkSendApi.deleteBulkSend(id)
    message.success('删除成功')
    await getList()
  } catch {}
}

const statusTagType = (s: number) => s === 1 ? 'info' : s === 2 ? 'warning' : s === 3 ? '' : s === 4 ? 'success' : 'danger'
const statusLabel = (s: number) => ['', '未提交', '待审核', '待发送', '已发送', '已驳回'][s] || ''

onMounted(() => { getList() })
</script>
