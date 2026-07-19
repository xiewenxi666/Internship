<template>
  <ReceivableDetailsHeader v-loading="loading" :receivable="receivable">
    <el-button
      v-if="receivable.auditStatus === 0 || receivable.auditStatus === 30 || receivable.auditStatus === 40 || receivable.auditStatus === 50"
      v-hasPermi="['crm:receivable:update']"
      type="primary"
      @click="openForm('update', receivable.id)"
    >
      编辑
    </el-button>
  </ReceivableDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('receivable.basicInfoTab')">
        <ReceivableDetailsInfo :receivable="receivable" />
      </el-tab-pane>
      <el-tab-pane :label="t('receivable.operateLogTab')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('receivable.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="receivable.id!"
          :biz-type="BizTypeEnum.CRM_RECEIVABLE"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>

  <ReceivableForm ref="formRef" @success="onFormSuccess" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as ReceivableApi from '@/api/crm/receivable'
import ReceivableDetailsHeader from './ReceivableDetailsHeader.vue'
import ReceivableDetailsInfo from './ReceivableDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import ReceivableForm from '@/views/crm/receivable/ReceivableForm.vue'

defineOptions({ name: 'CrmReceivableDetail' })
const props = defineProps<{ id?: number | string }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const receivableId = ref(0)
const loading = ref(true)
const receivable = ref<ReceivableApi.ReceivableVO>({} as ReceivableApi.ReceivableVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

/** 获取详情 */
const getReceivable = async (id: number) => {
  loading.value = true
  try {
    receivable.value = await ReceivableApi.getReceivable(id)
    if (receivable.value.no) {
      updateTagTitle(receivable.value.no)
    }
    await getOperateLog(id)
  } finally {
    loading.value = false
  }
}

/** 更新标签页标题为回款编号 */
const { currentRoute } = useRouter()
const tagsViewStore = useTagsViewStore()
const updateTagTitle = (title: string) => {
  tagsViewStore.updateVisitedView({ ...unref(currentRoute), title })
}

/** 编辑 */
const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

/** 表单保存成功后回调 */
const onFormSuccess = () => {
  getReceivable(receivableId.value)
}

/** 获取操作日志 */
const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (receivableId: number) => {
  if (!receivableId) {
    return
  }
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_RECEIVABLE,
    bizId: receivableId
  })
  logList.value = data.list
}

/** 关闭窗口 */
const { delView } = useTagsViewStore()
const close = () => {
  delView(unref(currentRoute))
}

/** 初始化 */
const { params } = useRoute()
onMounted(async () => {
  const id = Number(props.id || route.params.id)
  if (!id) {
    message.warning(t('receivable.paramError'))
    close()
    return
  }
  receivableId.value = id
  await getReceivable(receivableId.value)
})
</script>
