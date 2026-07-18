<template>
  <ContentWrap>
    <el-form ref="queryFormRef" :model="queryParams" class="-mb-15px" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('crm.business.name')" prop="name">
            <el-input
              v-model="queryParams.name"
              class="!w-240px"
              clearable
              :placeholder="t('crm.business.namePlaceholder')"
              @keyup.enter="handleQuery"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
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
    <el-table v-loading="loading" :data="list" :show-overflow-tooltip="true" :stripe="true" :table-layout="'auto'">
      <el-table-column align="center" :label="t('crm.business.name')" prop="name" min-width="160">
        <template #default="scope">
          <el-link :underline="false" type="primary" @click="openDetail(scope.row.id)">
            {{ scope.row.name }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column align="center" :label="t('crm.business.customerName')" prop="customerName" min-width="120" />
      <el-table-column align="center" :label="t('crm.business.statusName')" prop="statusName" min-width="100" />
      <el-table-column
        :formatter="erpPriceTableColumnFormatter"
        align="center"
        :label="t('crm.business.price') + '（元）'"
        prop="totalPrice"
        min-width="120"
      />
      <el-table-column align="center" :label="t('crm.business.ownerUserName')" prop="ownerUserName" min-width="100" />
      <el-table-column align="center" fixed="right" :label="t('common.action')" min-width="120">
        <template #default="scope">
          <el-button link type="primary" @click="openDetail(scope.row.id)">
            {{ t('crm.business.viewDetail') }}
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
import * as BusinessApi from '@/api/crm/business'
import { erpPriceTableColumnFormatter } from '@/utils'

defineOptions({ name: 'CrmBusinessOverview' })

const { t } = useI18n()
const { push } = useRouter()
const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  name: null
})
const queryFormRef = ref()

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

onMounted(() => {
  getList()
})
</script>
