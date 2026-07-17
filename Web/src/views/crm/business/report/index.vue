<template>
  <ContentWrap>
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-form-item label="部门" prop="deptId">
            <el-tree-select
              v-model="queryParams.deptId"
              :data="deptList"
              :props="{ label: 'name', value: 'id' }"
              check-strictly
              :render-after-expand="false"
              placeholder="选择部门"
              class="!w-240px"
            />
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item label="人员" prop="userId">
            <el-select
              v-model="queryParams.userId"
              clearable
              placeholder="选择人员"
              class="!w-240px"
            >
              <el-option
                v-for="item in userListByDeptId"
                :key="item.id"
                :label="item.nickname"
                :value="item.id"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="6">
          <el-form-item>
            <el-button @click="handleQuery">
              <Icon class="mr-5px" icon="ep:search" />
              查询
            </el-button>
            <el-button @click="resetQuery">
              <Icon class="mr-5px" icon="ep:refresh" />
              重置
            </el-button>
            <el-button :loading="exportLoading" plain type="success" @click="handleExport">
              <Icon class="mr-5px" icon="ep:download" />
              导出
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <!-- 列表 -->
  <ContentWrap>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true">
      <el-table-column align="center" label="商机名称" prop="name" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" label="客户名称" prop="customerName" min-width="120" />
      <el-table-column align="center" label="负责人" prop="ownerUserName" min-width="100" />
      <el-table-column align="center" label="商机金额（元）" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.totalPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="成交概率" min-width="100">
        <template #default="scope">
          {{ scope.row.probability != null ? scope.row.probability + '%' : '-' }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="概率金额（元）" min-width="140">
        <template #default="scope">
          {{ formatPrice(scope.row.probabilityPrice) }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" prop="statusName" min-width="100" />
      <el-table-column align="center" label="创建时间" prop="createTime" min-width="180" />
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
import * as DeptApi from '@/api/system/dept'
import * as UserApi from '@/api/system/user'
import * as BusinessApi from '@/api/crm/business'
import { useUserStore } from '@/store/modules/user'
import { handleTree } from '@/utils/tree'
import download from '@/utils/download'

defineOptions({ name: 'CrmBusinessReport' })

const { push } = useRouter()

const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  deptId: useUserStore().getUser.deptId,
  userId: undefined as number | undefined
})
const queryFormRef = ref()
const loading = ref(true)
const exportLoading = ref(false)
const list = ref([])
const total = ref(0)
const deptList = ref([])
const userList = ref([])

const userListByDeptId = computed(() =>
  queryParams.deptId
    ? userList.value.filter((u: any) => u.deptId === queryParams.deptId)
    : []
)

const formatPrice = (price: number) => {
  if (price == null) return '-'
  return '¥ ' + Number(price).toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
}

const getList = async () => {
  loading.value = true
  try {
    const data = await BusinessApi.getBusinessPage(queryParams)
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

const openDetail = (id: number) => {
  push({ name: 'CrmBusinessDetail', params: { id } })
}

const handleExport = async () => {
  try {
    exportLoading.value = true
    const data = await BusinessApi.getBusinessPage({ ...queryParams, pageNo: 1, pageSize: 9999 })
    download.excel(data.list, '商机记录报表.xls')
  } catch {
  } finally {
    exportLoading.value = false
  }
}

onMounted(async () => {
  deptList.value = handleTree(await DeptApi.getSimpleDeptList())
  userList.value = await UserApi.getSimpleUserList()
  getList()
})
</script>
