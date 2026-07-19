<template>
  <doc-alert title="【报销】报销审批" url="https://doc.iocoder.cn/crm/reimbursement/" />

  <ContentWrap>
    <el-form
      ref="queryFormRef"
      :model="queryParams"
      class="-mb-15px"
      label-width="auto"
    >
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="报销编号" prop="no">
            <el-input
              v-model="queryParams.no"
              class="!w-240px"
              clearable
              placeholder="请输入报销编号"
              @keyup.enter="handleQuery"
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
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-tabs v-model="activeName" @tab-click="handleTabClick">
      <el-tab-pane label="等待审批" name="10" />
      <el-tab-pane label="审批通过" name="20" />
      <el-tab-pane label="驳回审批" name="30" />
      <el-tab-pane label="审批否决" name="50" />
    </el-tabs>
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
      <el-table-column align="center" fixed="left" label="报销编号" prop="no" min-width="180">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.no }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column
        align="center"
        label="报销金额（元）"
        prop="price"
        min-width="140"
        :formatter="erpPriceTableColumnFormatter"
      />
      <el-table-column
        :formatter="dateFormatter2"
        align="center"
        label="申请日期"
        prop="applyDate"
        min-width="150"
      />
      <el-table-column align="center" label="审批状态" prop="auditStatus" min-width="120">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="scope.row.auditStatus" />
        </template>
      </el-table-column>
      <el-table-column align="center" label="负责人" prop="ownerUserName" min-width="120" />
      <el-table-column
        :formatter="dateFormatter"
        align="center"
        label="创建时间"
        prop="createTime"
        min-width="180"
      />
      <el-table-column align="center" fixed="right" label="操作" min-width="100">
        <template #default="scope">
          <el-button
            v-hasPermi="['crm:reimbursement:query']"
            link
            type="primary"
            @click="openDetail(scope.row.id)"
          >
            详情
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
import { DICT_TYPE } from '@/utils/dict'
import { dateFormatter, dateFormatter2 } from '@/utils/formatTime'
import { erpPriceTableColumnFormatter } from '@/utils'
import * as ReimbursementApi from '@/api/crm/reimbursement'
import { TabsPaneContext } from 'element-plus'

defineOptions({ name: 'CrmReimbursementApproval' })

const { t } = useI18n('crm')
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  auditStatus: 10,
  no: undefined
})
const queryFormRef = ref()
const activeName = ref('10')

const handleTabClick = (tab: TabsPaneContext) => {
  queryParams.auditStatus = Number(tab.paneName)
  handleQuery()
}

const getList = async () => {
  loading.value = true
  try {
    const data = await ReimbursementApi.getReimbursementApprovalPage(queryParams)
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

const { push } = useRouter()
const openDetail = (id: number) => {
  push({ name: 'CrmReimbursementApprovalDetail', params: { id } })
}

onMounted(async () => {
  await getList()
})
</script>
