<template>
  <ContentWrap>
    <el-form class="-mb-15px" :model="queryParams" ref="queryFormRef" label-width="auto">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item :label="t('oa.documentDir.name')" prop="name">
            <el-input v-model="queryParams.name" :placeholder="t('oa.documentDir.namePlaceholder')"
              clearable @keyup.enter="handleQuery" class="!w-240px" />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item :label="t('oa.documentDir.type')" prop="type">
            <el-select v-model="queryParams.type" :placeholder="t('oa.documentDir.typePlaceholder')"
              clearable class="!w-240px">
              <el-option v-for="dict in getIntDictOptions(DICT_TYPE.OA_DOCUMENT_DIR_TYPE)" :key="dict.value"
                :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item>
            <el-button @click="handleQuery"><Icon icon="ep:search" class="mr-5px" /> {{ t('common.query') }}</el-button>
            <el-button @click="resetQuery"><Icon icon="ep:refresh" class="mr-5px" /> {{ t('common.reset') }}</el-button>
            <el-button type="primary" plain @click="openForm('create')" v-hasPermi="['oa:document-dir:create']">
              <Icon icon="ep:plus" class="mr-5px" /> {{ t('action.add') }}
            </el-button>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
  </ContentWrap>

  <ContentWrap>
    <el-table v-loading="loading" :data="list" :table-layout="'auto'">
      <el-table-column :label="t('oa.documentDir.id')" align="center" prop="id" />
      <el-table-column :label="t('oa.documentDir.name')" align="center" prop="name" />
      <el-table-column :label="t('oa.documentDir.type')" align="center" prop="type">
        <template #default="scope">
          <dict-tag :type="DICT_TYPE.OA_DOCUMENT_DIR_TYPE" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column :label="t('oa.documentDir.description')" align="center" prop="description" />
      <el-table-column :label="t('oa.documentDir.sort')" align="center" prop="sort" />
      <el-table-column :label="t('common.operation')" align="center">
        <template #default="scope">
          <el-button link type="primary" @click="openForm('update', scope.row.id)"
            v-hasPermi="['oa:document-dir:update']">{{ t('action.edit') }}</el-button>
          <el-button link type="danger" @click="handleDelete(scope.row.id)"
            v-hasPermi="['oa:document-dir:delete']">{{ t('action.del') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
    <Pagination :total="total" v-model:page="queryParams.pageNo" v-model:limit="queryParams.pageSize"
      @pagination="getList" />
  </ContentWrap>

  <DocumentDirForm ref="formRef" @success="getList" />
</template>

<script lang="ts" setup>
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import { dateFormatter } from '@/utils/formatTime'
import * as DocumentDirApi from '@/api/oa/documentDir'
import DocumentDirForm from './DocumentDirForm.vue'

defineOptions({ name: 'OaDocumentDir' })

const message = useMessage()
const { t } = useI18n()

const loading = ref(true)
const total = ref(0)
const list = ref([])
const queryParams = reactive({
  pageNo: 1, pageSize: 10,
  name: '', type: undefined
})
const queryFormRef = ref()

const getList = async () => {
  loading.value = true
  try {
    const data = await DocumentDirApi.getDocumentDirPage(queryParams)
    list.value = data.list
    total.value = data.total
  } finally { loading.value = false }
}
const handleQuery = () => { queryParams.pageNo = 1; getList() }
const resetQuery = () => { queryFormRef.value.resetFields(); handleQuery() }

const formRef = ref()
const openForm = (type: string, id?: number) => { formRef.value.open(type, id) }

const handleDelete = async (id: number) => {
  try {
    await message.delConfirm()
    await DocumentDirApi.deleteDocumentDir(id)
    message.success(t('common.delSuccess'))
    await getList()
  } catch {}
}

onMounted(() => { getList() })
</script>
