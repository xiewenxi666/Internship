<template>
  <Dialog v-model="dialogVisible" title="合并客户">
    <el-form label-width="auto">
      <el-form-item label="主客户（保留）">
        <el-select v-model="mainId" placeholder="选择要保留的主客户" class="w-1/1">
          <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="被合并客户">
        <el-tag v-for="c in mergeCustomers" :key="c.id" closable @close="removeItem(c.id)" class="mr-5px">
          {{ c.name }}
        </el-tag>
      </el-form-item>
    </el-form>
    <el-alert type="warning" :closable="false" show-icon title="合并后，被合并客户的合同、回款、联系人、商机、跟进记录将全部转移到主客户名下，被合并客户将被删除。此操作不可撤销。" />
    <template #footer>
      <el-button :loading="loading" type="primary" @click="submit">确认合并</el-button>
      <el-button @click="dialogVisible = false">取消</el-button>
    </template>
  </Dialog>
</template>
<script lang="ts" setup>
import { mergeCustomer } from '@/api/crm/customer'
const message = useMessage()
const dialogVisible = ref(false)
const loading = ref(false)
const mainId = ref<number>()
const customers = ref<any[]>([])
const selectedIds = ref<number[]>([])
const mergeCustomers = computed(() => customers.value.filter(c => selectedIds.value.includes(c.id)))
const open = (list: any[], ids: number[]) => { customers.value = list; selectedIds.value = ids; mainId.value = ids[0]; dialogVisible.value = true }
defineExpose({ open })
const removeItem = (id: number) => { selectedIds.value = selectedIds.value.filter(i => i !== id); if (mainId.value === id) mainId.value = selectedIds.value[0] }
const submit = async () => {
  if (!mainId.value) { message.warning('请选择主客户'); return }
  const mergeIds = selectedIds.value.filter(i => i !== mainId.value)
  if (!mergeIds.length) { message.warning('没有要合并的客户'); return }
  loading.value = true
  try { await mergeCustomer(mainId.value, mergeIds); message.success('合并成功'); dialogVisible.value = false }
  catch { message.error('合并失败') }
  finally { loading.value = false }
}
</script>