<template>
  <el-card v-loading="loading" class="relation-panel">
    <template #header>
      <span>关联数据</span>
    </template>

    <!-- 关联商机 -->
    <div v-if="data?.businessList?.length" class="relation-section">
      <h4 class="section-title">关联商机 ({{ data.businessList.length }})</h4>
      <el-table :data="data.businessList" size="small" max-height="200">
        <el-table-column prop="name" label="商机名称" min-width="150" />
        <el-table-column prop="totalPrice" label="商机金额" width="120">
          <template #default="{ row }">
            {{ row.totalPrice?.toLocaleString() ?? '-' }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 关联合同 -->
    <div v-if="data?.contractList?.length" class="relation-section">
      <h4 class="section-title">关联合同 ({{ data.contractList.length }})</h4>
      <el-table :data="data.contractList" size="small" max-height="200">
        <el-table-column prop="no" label="合同编号" width="180" />
        <el-table-column prop="name" label="合同名称" min-width="150" />
        <el-table-column prop="totalPrice" label="合同金额" width="120">
          <template #default="{ row }">
            {{ row.totalPrice?.toLocaleString() ?? '-' }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 关联回款 -->
    <div v-if="data?.receivableList?.length" class="relation-section">
      <h4 class="section-title">关联回款 ({{ data.receivableList.length }})</h4>
      <el-table :data="data.receivableList" size="small" max-height="200">
        <el-table-column prop="no" label="回款编号" width="180" />
        <el-table-column prop="price" label="回款金额" width="120">
          <template #default="{ row }">
            {{ row.price?.toLocaleString() ?? '-' }}
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 无关联数据 -->
    <el-empty
      v-if="!data?.businessList?.length && !data?.contractList?.length && !data?.receivableList?.length"
      description="暂无关联数据"
      :image-size="80"
    />
  </el-card>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { getCustomerRelation } from '@/api/crm/common'

interface RelationData {
  businessList: Array<{ name: string; totalPrice: number }>
  contractList: Array<{ no: string; name: string; totalPrice: number }>
  receivableList: Array<{ no: string; price: number }>
}

const props = defineProps<{
  customerId: number | null
}>()

const loading = ref(false)
const data = ref<RelationData | null>(null)

watch(
  () => props.customerId,
  (id) => {
    if (id) {
      fetchRelation(id)
    }
  },
  { immediate: true }
)

async function fetchRelation(id: number) {
  loading.value = true
  try {
    data.value = await getCustomerRelation(id)
  } catch {
    data.value = null
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.relation-panel {
  margin-top: 16px;
}
.relation-section {
  margin-bottom: 16px;
}
.section-title {
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}
</style>
