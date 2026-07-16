<template>
  <ReceivableDetailsHeader v-loading="loading" :receivable="receivable">
    <el-button
      v-if="receivable.auditStatus === 0"
      v-hasPermi="['crm:receivable:update']"
      type="primary"
      @click="openForm('update', receivable.id)"
    >
      {{ t('common.edit') }}
    </el-button>
    <el-button
      v-if="receivable.auditStatus === 0"
      v-hasPermi="['crm:receivable:update']"
      type="primary"
      @click="handleSubmit"
    >
      {{ t('contract.submitAudit') }}
    </el-button>
    <el-button
      v-if="receivable.auditStatus === 30 || receivable.auditStatus === 40 || receivable.auditStatus === 50"
      v-hasPermi="['crm:receivable:update']"
      type="primary"
      @click="openForm('update', receivable.id)"
    >
      重新编辑
    </el-button>
    <el-button
      v-if="receivable.auditStatus === 30 || receivable.auditStatus === 40 || receivable.auditStatus === 50"
      v-hasPermi="['crm:receivable:update']"
      type="primary"
      @click="handleResubmit"
    >
      再次提交
    </el-button>
    <el-button
      v-if="receivable.auditStatus === 10"
      v-hasPermi="['crm:receivable:update']"
      type="danger"
      plain
      @click="handleCancel"
    >
      撤销审批
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
const props = defineProps<{ id?: number }>()

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

/** 提交审核，提交后跳转到“审批中”页面（即当前回款管理详情页，仅提供撤销审批功能） */
const handleSubmit = async () => {
  try {
    await message.confirm('确定提交该回款审核吗？')
    await ReceivableApi.submitReceivable(receivableId.value)
    message.success('提交审核成功')
    await goProcessingDetail()
  } catch {}
}

/** 重新提交被驳回/被否决/已撤销的回款，提交后跳转到“审批中”页面 */
const handleResubmit = async () => {
  try {
    await message.confirm('确定再次提交该回款审核吗？')
    await ReceivableApi.submitReceivable(receivableId.value)
    message.success('再次提交审核成功')
    await goProcessingDetail()
  } catch {}
}

/** 跳转到“审批中”详情页面 */
const { push } = useRouter()
const goProcessingDetail = async () => {
  if (currentRoute.value.name === 'CrmReceivableDetail') {
    await getReceivable(receivableId.value)
    return
  }
  push({ name: 'CrmReceivableDetail', params: { id: receivableId.value } })
}

/** 撤销审批 */
const handleCancel = async () => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入撤销原因（可不填）：', '撤销审批', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入原因',
      inputType: 'textarea'
    })
    await ReceivableApi.cancelReceivable(receivableId.value, reason || undefined)
    message.success('撤销审批成功')
    await getReceivable(receivableId.value)
  } catch {}
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
  const id = props.id || route.params.id
  if (!id) {
    message.warning(t('receivable.paramError'))
    close()
    return
  }
  receivableId.value = id
  await getReceivable(receivableId.value)
})
</script>
