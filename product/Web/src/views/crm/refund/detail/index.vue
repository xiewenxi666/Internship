<template>
  <RefundDetailsHeader v-loading="loading" :refund="refund">
    <el-button
      v-if="refund.auditStatus === 0 || refund.auditStatus === 30 || refund.auditStatus === 40 || refund.auditStatus === 50"
      v-hasPermi="['crm:refund:update']"
      type="primary"
      @click="openForm('update', refund.id)"
    >
      编辑
    </el-button>
  </RefundDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane label="基本信息">
        <RefundDetailsInfo :refund="refund" />
      </el-tab-pane>
      <el-tab-pane label="操作日志">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane label="团队成员">
        <PermissionList
          ref="permissionListRef"
          :biz-id="refund.id!"
          :biz-type="BizTypeEnum.CRM_REFUND"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>

  <RefundForm ref="formRef" @success="onFormSuccess" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as RefundApi from '@/api/crm/refund'
import RefundDetailsHeader from './RefundDetailsHeader.vue'
import RefundDetailsInfo from './RefundDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import RefundForm from '@/views/crm/refund/RefundForm.vue'

defineOptions({ name: 'CrmRefundDetail' })
const props = defineProps<{ id?: number }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const refundId = ref(0)
const loading = ref(true)
const refund = ref<RefundApi.RefundVO>({} as RefundApi.RefundVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

const getRefund = async (id: number) => {
  loading.value = true
  try {
    refund.value = await RefundApi.getRefund(id)
    if (refund.value.no) {
      updateTagTitle(refund.value.no)
    }
    await getOperateLog(id)
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
  getRefund(refundId.value)
}

const handleSubmit = async () => {
  try {
    await message.confirm('确定提交该退款审核吗？')
    await RefundApi.submitRefund(refundId.value)
    message.success('提交审核成功')
    await goProcessingDetail()
  } catch {}
}

const handleResubmit = async () => {
  try {
    await message.confirm('确定再次提交该退款审核吗？')
    await RefundApi.submitRefund(refundId.value)
    message.success('再次提交审核成功')
    await goProcessingDetail()
  } catch {}
}

const { push } = useRouter()
const goProcessingDetail = async () => {
  if (currentRoute.value.name === 'CrmRefundDetail') {
    await getRefund(refundId.value)
    return
  }
  push({ name: 'CrmRefundDetail', params: { id: refundId.value } })
}

const handleCancel = async () => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入撤销原因（可不填）：', '撤销审批', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入原因',
      inputType: 'textarea'
    })
    await RefundApi.cancelRefund(refundId.value, reason || undefined)
    message.success('撤销审批成功')
    await getRefund(refundId.value)
  } catch {}
}

const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (refundId: number) => {
  if (!refundId) {
    return
  }
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_REFUND,
    bizId: refundId
  })
  logList.value = data.list
}

const { delView } = useTagsViewStore()
const close = () => {
  delView(unref(currentRoute))
}

const { params } = useRoute()
onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning('参数错误，缺少退款编号')
    close()
    return
  }
  refundId.value = id
  await getRefund(refundId.value)
})
</script>
