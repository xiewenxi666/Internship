<template>
  <ContentWrap>
    <el-card shadow="never">
      <template #header>
        <span class="text-base font-bold">{{ t('crm.business.addTitle') }}</span>
      </template>
      <el-empty v-if="!formVisible" :description="t('crm.business.addHint')">
        <el-button type="primary" @click="openCreateForm">
          <Icon class="mr-5px" icon="ep:plus" />
          {{ t('crm.business.addNew') }}
        </el-button>
      </el-empty>
    </el-card>
  </ContentWrap>
  <BusinessForm ref="formRef" @success="onSuccess" />
</template>

<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import BusinessForm from '../BusinessForm.vue'

defineOptions({ name: 'CrmBusinessAdd' })

const { t } = useI18n()
const { push } = useRouter()
const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()

const formVisible = ref(false)
const formRef = ref()

const openCreateForm = () => {
  formVisible.value = true
  nextTick(() => {
    formRef.value.open('create')
  })
}

const onSuccess = () => {
  delView(unref(currentRoute))
  push({ name: 'CrmBusinessList' })
}

onMounted(() => {
  openCreateForm()
})
</script>
