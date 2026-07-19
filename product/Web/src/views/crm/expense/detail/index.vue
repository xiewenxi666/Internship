<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ expense.no }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <el-button
          v-hasPermi="['crm:expense:update']"
          type="primary"
          @click="openForm('update', expense.id)"
        >
          编辑
        </el-button>
        <el-button
          v-hasPermi="['crm:expense:delete']"
          type="danger"
          @click="handleDelete(expense.id)"
        >
          删除
        </el-button>
      </div>
    </div>

    <ExpenseDetailsInfo :expense="expense" />

    <ExpenseForm ref="formRef" @success="onFormSuccess" />
  </div>
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as ExpenseApi from '@/api/crm/expense'
import ExpenseDetailsInfo from './ExpenseDetailsInfo.vue'
import ExpenseForm from '@/views/crm/expense/ExpenseForm.vue'

defineOptions({ name: 'CrmExpenseDetail' })

const props = defineProps<{ id?: number }>()

const route = useRoute()
const message = useMessage()
const expenseId = ref(0)
const loading = ref(true)
const expense = ref<ExpenseApi.ExpenseVO>({} as ExpenseApi.ExpenseVO)

const getExpense = async (id: number) => {
  loading.value = true
  try {
    expense.value = await ExpenseApi.getExpense(id)
    if (expense.value.no) {
      updateTagTitle(expense.value.no)
    }
  } finally {
    loading.value = false
  }
}

const { currentRoute } = useRouter()
const tagsViewStore = useTagsViewStore()
const updateTagTitle = (title: string) => {
  tagsViewStore.updateVisitedView({ ...unref(currentRoute), title })
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const onFormSuccess = () => {
  getExpense(expenseId.value)
}

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await ExpenseApi.deleteExpense(id)
    message.success('删除成功')
    const { push } = useRouter()
    push({ name: 'CrmExpense' })
  } catch {}
}

const { delView } = useTagsViewStore()
const close = () => {
  delView(unref(currentRoute))
}

const { params } = useRoute()
onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning('参数错误，费用不能为空！')
    close()
    return
  }
  expenseId.value = id
  await getExpense(expenseId.value)
})
</script>
