<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="活动标题" prop="title">
            <el-input v-model="queryParams.title" placeholder="请输入活动标题" clearable class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="活动类型" prop="type">
            <el-select v-model="queryParams.type" class="!w-240px" clearable>
              <el-option label="促销活动" :value="1" />
              <el-option label="品牌活动" :value="2" />
              <el-option label="会议营销" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="活动状态" prop="status">
            <el-select v-model="queryParams.status" class="!w-240px" clearable>
              <el-option label="筹备" :value="1" />
              <el-option label="进行中" :value="2" />
              <el-option label="已结束" :value="3" />
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
              <Icon icon="ep:plus" class="mr-5px" /> 新增
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :stripe="true" :show-overflow-tooltip="true">
      <el-table-column label="活动标题" align="center" prop="title" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="活动类型" align="center" prop="type" min-width="100">
        <template #default="scope">
          <span>{{ scope.row.type === 1 ? '促销活动' : scope.row.type === 2 ? '品牌活动' : '会议营销' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" :formatter="dateFormatter" min-width="160" />
      <el-table-column label="结束时间" align="center" prop="endTime" :formatter="dateFormatter" min-width="160" />
      <el-table-column label="预计成本" align="center" prop="estimatedCost" min-width="120">
        <template #default="scope">¥ {{ scope.row.estimatedCost }}</template>
      </el-table-column>
      <el-table-column label="预计收入" align="center" prop="estimatedRevenue" min-width="120">
        <template #default="scope">¥ {{ scope.row.estimatedRevenue }}</template>
      </el-table-column>
      <el-table-column label="活动状态" align="center" prop="status" min-width="100">
        <template #default="scope">
          <el-tag :type="scope.row.status === 1 ? 'info' : scope.row.status === 2 ? 'success' : 'default'">
            {{ scope.row.status === 1 ? '筹备' : scope.row.status === 2 ? '进行中' : '已结束' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="负责人员" align="center" prop="ownerUserName" min-width="100" />
      <el-table-column :label="'创建时间'" align="center" prop="createTime" :formatter="dateFormatter" min-width="160" />
      <el-table-column :label="'操作'" align="center" min-width="150" fixed="right">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)">编辑</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <CampaignForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import * as CampaignApi from '@/api/crm/campaign'
import CampaignForm from './CampaignForm.vue'
import { dateFormatter } from '@/utils/formatTime'

defineOptions({ name: 'CrmCampaign' })

const message = useMessage()

const loading = ref(false)
const list = ref<CampaignApi.CampaignVO[]>([])
const total = ref(0)
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  title: null,
  type: null,
  status: null
})

const getList = async () => {
  loading.value = true
  try {
    const data = await CampaignApi.getCampaignPage(queryParams)
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
  Object.assign(queryParams, { pageNo: 1, pageSize: 10, title: null, type: null, status: null })
  getList()
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value?.open(type, id)
}

const handleDelete = async (id: number) => {
  try {
    await message.confirm('确认删除该活动？')
    await CampaignApi.deleteCampaign(id)
    message.success('删除成功')
    await getList()
  } catch {}
}

const openDetail = (id: number) => {
  message.alert('详情页开发中，活动ID: ' + id)
}

onMounted(() => {
  getList()
})
</script>
