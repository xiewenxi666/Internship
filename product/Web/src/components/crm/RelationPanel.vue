<!-- -23计算机科学与技术2班-龚小波 -->
<template>
  <el-tabs v-model="activeTab" type="card">
    <!-- 商机 -->
    <el-tab-pane :label="t('crm.relation.business')" name="business">
      <el-table v-loading="loading" :data="relationData?.businessList || []" size="small">
        <el-table-column :label="t('crm.relation.name')" prop="name" min-width="150" />
        <el-table-column :label="t('crm.relation.status')" prop="statusName" width="100" />
        <el-table-column :label="t('crm.relation.totalPrice')" prop="totalPrice" width="120">
          <template #default="{ row }">
            {{ row.totalPrice ? (row.totalPrice / 100).toFixed(2) : '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="t('crm.relation.createTime')" prop="createTime" width="160" />
      </el-table>
    </el-tab-pane>

    <!-- 合同 -->
    <el-tab-pane :label="t('crm.relation.contract')" name="contract">
      <el-table v-loading="loading" :data="relationData?.contractList || []" size="small">
        <el-table-column :label="t('crm.relation.contractNo')" prop="no" min-width="150" />
        <el-table-column :label="t('crm.relation.contractName')" prop="name" min-width="150" />
        <el-table-column :label="t('crm.relation.totalPrice')" prop="totalPrice" width="120">
          <template #default="{ row }">
            {{ row.totalPrice ? (row.totalPrice / 100).toFixed(2) : '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="t('crm.relation.status')" prop="auditStatusName" width="100" />
        <el-table-column :label="t('crm.relation.createTime')" prop="createTime" width="160" />
      </el-table>
    </el-tab-pane>

    <!-- 回款 -->
    <el-tab-pane :label="t('crm.relation.receivable')" name="receivable">
      <el-table v-loading="loading" :data="relationData?.receivableList || []" size="small">
        <el-table-column :label="t('crm.relation.receivableNo')" prop="no" min-width="150" />
        <el-table-column :label="t('crm.relation.period')" prop="period" width="80" />
        <el-table-column :label="t('crm.relation.totalPrice')" prop="price" width="120">
          <template #default="{ row }">
            {{ row.price ? (row.price / 100).toFixed(2) : '-' }}
          </template>
        </el-table-column>
        <el-table-column :label="t('crm.relation.returnTime')" prop="returnTime" width="160" />
      </el-table>
    </el-tab-pane>
  </el-tabs>
</template>

<script lang="ts" setup>
import * as CrmCommonApi from '@/api/crm/common'

defineOptions({ name: 'CrmRelationPanel' })

const props = defineProps<{
  customerId: number
}>()

const { t } = useI18n()

const loading = ref(false)
const activeTab = ref('business')
const relationData = ref<{
  businessList: any[]
  contractList: any[]
  receivableList: any[]
} | null>(null)

watch(
  () => props.customerId,
  (newId) => {
    if (newId) {
      loadData()
    }
  },
  { immediate: true }
)

const loadData = async () => {
  loading.value = true
  try {
    const res = await CrmCommonApi.getCustomerRelation(props.customerId)
    relationData.value = res
  } finally {
    loading.value = false
  }
}
</script>
