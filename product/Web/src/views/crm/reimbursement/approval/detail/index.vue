<template>
  <div v-loading="loading">
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ reimbursement.no }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <el-button
          v-if="reimbursement.auditStatus === 10"
          v-hasPermi="['crm:reimbursement:update']"
          type="success"
          @click="handleApprove"
        >
          审批通过
        </el-button>
        <el-button
          v-if="reimbursement.auditStatus === 10"
          v-hasPermi="['crm:reimbursement:update']"
          type="warning"
          @click="handleReject"
        >
          驳回审批
        </el-button>
        <el-button
          v-if="reimbursement.auditStatus === 10"
          v-hasPermi="['crm:reimbursement:update']"
          type="danger"
          @click="handleVeto"
        >
          审批否决
        </el-button>
      </div>
    </div>
    <ContentWrap class="mt-10px">
      <el-descriptions :column="5" direction="vertical">
        <el-descriptions-item :label="t('reimbursement.applyDate')">
          {{ formatDate(reimbursement.applyDate) }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('reimbursement.price')">
          {{ erpPriceInputFormatter(reimbursement.price) }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('reimbursement.ownerUserName')">
          {{ reimbursement.ownerUserName }}
        </el-descriptions-item>
        <el-descriptions-item :label="t('reimbursement.auditStatus')">
          <dict-tag :type="DICT_TYPE.CRM_AUDIT_STATUS" :value="reimbursement.auditStatus" />
        </el-descriptions-item>
      </el-descriptions>
    </ContentWrap>
  </div>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('reimbursement.basicInfoTab')">
        <ReimbursementDetailsInfo :reimbursement="reimbursement" />
      </el-tab-pane>
      <el-tab-pane :label="t('reimbursement.operateLogTab')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
      <el-tab-pane :label="t('reimbursement.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="reimbursement.id!"
          :biz-type="BizTypeEnum.CRM_REIMBURSEMENT"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
    </el-tabs>
  </el-col>
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as ReimbursementApi from '@/api/crm/reimbursement'
import ReimbursementDetailsInfo from '@/views/crm/reimbursement/detail/ReimbursementDetailsInfo.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { OperateLogVO } from '@/api/system/operatelog'
import { getOperateLogPage } from '@/api/crm/operateLog'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'

defineOptions({ name: 'CrmReimbursementApprovalDetail' })
const props = defineProps<{ id?: number }>()

const { t } = useI18n('crm')
const route = useRoute()
const message = useMessage()
const reimbursementId = ref(0)
const loading = ref(true)
const reimbursement = ref<ReimbursementApi.ReimbursementVO>({} as ReimbursementApi.ReimbursementVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

const getReimbursement = async (id: number) => {
  loading.value = true
  try {
    reimbursement.value = await ReimbursementApi.getReimbursement(id)
    if (reimbursement.value.no) {
      updateTagTitle(reimbursement.value.no)
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
    await ReimbursementApi.approveReimbursement(reimbursementId.value, remark || undefined)
    message.success('审批通过成功')
    await getReimbursement(reimbursementId.value)
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
    await ReimbursementApi.rejectReimbursement(reimbursementId.value, reason || undefined)
    message.success('驳回审批成功')
    await getReimbursement(reimbursementId.value)
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
    await ReimbursementApi.vetoReimbursement(reimbursementId.value, reason || undefined)
    message.success('审批否决成功')
    await getReimbursement(reimbursementId.value)
  } catch {}
}

const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (reimbursementId: number) => {
  if (!reimbursementId) {
    return
  }
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_REIMBURSEMENT,
    bizId: reimbursementId
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
    message.warning(t('reimbursement.paramError'))
    close()
    return
  }
  reimbursementId.value = id
  await getReimbursement(reimbursementId.value)
})
</script>
