<template>
  <RefundDetailsHeader v-loading="loading" :refund="refund">
    <el-button
      v-if="refund.auditStatus === 10"
      v-hasPermi="['crm:refund:update']"
      type="success"
      @click="handleApprove"
    >
      审批通过
    </el-button>
    <el-button
      v-if="refund.auditStatus === 10"
      v-hasPermi="['crm:refund:update']"
      type="warning"
      @click="handleReject"
    >
      驳回审批
    </el-button>
    <el-button
      v-if="refund.auditStatus === 10"
      v-hasPermi="['crm:refund:update']"
      type="danger"
      @click="handleVeto"
    >
      审批否决
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
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as RefundApi from '@/api/crm/refund'
import RefundDetailsHeader from '@/views/crm/refund/detail/RefundDetailsHeader.vue'
import RefundDetailsInfo from '@/views/crm/refund/detail/RefundDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'

defineOptions({ name: 'CrmRefundApprovalDetail' })
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

const handleApprove = async () => {
  try {
    const { value: remark } = await ElMessageBox.prompt('请输入备注信息（可不填）：', '审批通过', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入备注',
      inputType: 'textarea'
    })
    await RefundApi.approveRefund(refundId.value, remark || undefined)
    message.success('审批通过成功')
    await getRefund(refundId.value)
  } catch {}
}

const handleReject = async () => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入驳回原因（可不填）：', '驳回审批', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入驳回原因',
      inputType: 'textarea'
    })
    await RefundApi.rejectRefund(refundId.value, reason || undefined)
    message.success('驳回审批成功')
    await getRefund(refundId.value)
  } catch {}
}

const handleVeto = async () => {
  try {
    const { value: reason } = await ElMessageBox.prompt('请输入否决原因（可不填）：', '审批否决', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      inputPlaceholder: '请输入否决原因',
      inputType: 'textarea'
    })
    await RefundApi.vetoRefund(refundId.value, reason || undefined)
    message.success('审批否决成功')
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
