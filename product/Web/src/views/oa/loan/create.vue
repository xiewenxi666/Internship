<template>
  <el-row :gutter="20">
    <el-col :span="16">
      <ContentWrap :title="t('oa.loan.title') + t('common.info')">
        <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="80px">
          <el-form-item :label="t('oa.loan.purpose')" prop="purpose">
            <el-select v-model="formData.purpose" clearable :placeholder="t('oa.loan.purposePlaceholder')">
              <el-option v-for="dict in purposeOptions" :key="dict.value" :label="dict.label" :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('oa.loan.amount')" prop="amount">
            <el-input-number v-model="formData.amount" :min="0" :step="1" controls-position="right" :placeholder="t('oa.loan.amountPlaceholder')" style="width: 100%" />
          </el-form-item>
          <el-form-item :label="t('oa.loan.expectedRepayTime')" prop="expectedRepayTime">
            <el-date-picker v-model="formData.expectedRepayTime" clearable :placeholder="t('oa.loan.expectedRepayTimePlaceholder')" type="date" value-format="x" style="width: 100%" />
          </el-form-item>
          <el-form-item :label="t('oa.loan.reason')" prop="reason">
            <el-input v-model="formData.reason" :placeholder="t('oa.loan.reasonPlaceholder')" type="textarea" />
          </el-form-item>
          <el-form-item>
            <el-button :disabled="formLoading" type="primary" @click="submitForm">
              {{ t('common.ok') }}
            </el-button>
          </el-form-item>
        </el-form>
      </ContentWrap>
    </el-col>

    <el-col :span="8">
      <ContentWrap :title="t('process.instance.flowDiagram')" :bodyStyle="{ padding: '0 20px 0' }">
        <ProcessInstanceTimeline
          ref="timelineRef"
          :activity-nodes="activityNodes"
          :show-status-icon="false"
          @select-user-confirm="selectUserConfirm"
        />
      </ContentWrap>
    </el-col>
  </el-row>
</template>
<script lang="ts" setup>
import * as LoanApi from '@/api/oa/loan'
import { useTagsViewStore } from '@/store/modules/tagsView'

import * as DefinitionApi from '@/api/bpm/definition'
import ProcessInstanceTimeline from '@/views/bpm/processInstance/detail/ProcessInstanceTimeline.vue'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import { CandidateStrategy, NodeId } from '@/components/SimpleProcessDesignerV2/src/consts'
import { ApprovalNodeInfo } from '@/api/bpm/processInstance'

defineOptions({ name: 'OaLoanCreate' })

const message = useMessage()
const { t } = useI18n('bpm')
const { delView } = useTagsViewStore()
const { push, currentRoute } = useRouter()
const { query } = useRoute()

const formLoading = ref(false)
const formData = ref({
  purpose: undefined,
  amount: undefined,
  expectedRepayTime: undefined,
  reason: undefined
})
const formRules = reactive({
  purpose: [{ required: true, message: t('oa.loan.purpose') + t('common.notEmpty'), trigger: 'blur' }],
  amount: [{ required: true, message: t('oa.loan.amount') + t('common.notEmpty'), trigger: 'blur' }],
  expectedRepayTime: [{ required: true, message: t('oa.loan.expectedRepayTime') + t('common.notEmpty'), trigger: 'change' }],
  reason: [{ required: true, message: t('oa.loan.reason') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const processDefineKey = 'oa_loan'
const startUserSelectTasks = ref([])
const startUserSelectAssignees = ref({})
const tempStartUserSelectAssignees = ref({})
const activityNodes = ref<ProcessInstanceApi.ApprovalNodeInfo[]>([])
const processDefinitionId = ref('')

const purposeOptions = [
  { value: 1, label: '日常开支' },
  { value: 2, label: '差旅费用' },
  { value: 3, label: '采购垫付' },
  { value: 4, label: '其他' }
]

const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  if (startUserSelectTasks.value?.length > 0) {
    for (const userTask of startUserSelectTasks.value) {
      if (Array.isArray(startUserSelectAssignees.value[userTask.id]) && startUserSelectAssignees.value[userTask.id].length === 0) {
        return message.warning(t('process.instance.selectCandidate', { name: userTask.name }))
      }
    }
  }

  formLoading.value = true
  try {
    const data = { ...formData.value } as unknown as LoanApi.LoanVO
    if (startUserSelectTasks.value?.length > 0) {
      data.startUserSelectAssignees = startUserSelectAssignees.value
    }
    await LoanApi.createLoan(data)
    message.success(t('process.instance.startSuccess'))
    delView(unref(currentRoute))
    await push('/bpm/oa/loan')
  } finally {
    formLoading.value = false
  }
}

const getApprovalDetail = async () => {
  try {
    const data = await ProcessInstanceApi.getApprovalDetail({
      processDefinitionId: processDefinitionId.value,
      activityId: NodeId.START_USER_NODE_ID,
      processVariablesStr: JSON.stringify({})
    })
    if (!data) {
      message.error(t('process.instance.queryError'))
      return
    }
    activityNodes.value = data.activityNodes
    startUserSelectTasks.value = data.activityNodes?.filter(
      (node: ApprovalNodeInfo) => CandidateStrategy.START_USER_SELECT === node.candidateStrategy
    )
    if (startUserSelectTasks.value?.length > 0) {
      for (const node of startUserSelectTasks.value) {
        if (tempStartUserSelectAssignees.value[node.id] && tempStartUserSelectAssignees.value[node.id].length > 0) {
          startUserSelectAssignees.value[node.id] = tempStartUserSelectAssignees.value[node.id]
        } else {
          startUserSelectAssignees.value[node.id] = []
        }
      }
    }
  } finally {
  }
}

const selectUserConfirm = (id: string, userList: any[]) => {
  startUserSelectAssignees.value[id] = userList?.map((item: any) => item.id)
}

const getDetail = async (id: number) => {
  try {
    formLoading.value = true
    const data = await LoanApi.getLoan(id)
    if (!data) {
      message.error(t('oa.loan.restartFailed') + '：' + t('oa.loan.dataNotFound'))
      return
    }
    formData.value = {
      purpose: data.purpose,
      amount: data.amount,
      expectedRepayTime: data.expectedRepayTime,
      reason: data.reason
    }
  } finally {
    formLoading.value = false
  }
}

onMounted(async () => {
  const processDefinitionDetail = await DefinitionApi.getProcessDefinition(undefined, processDefineKey)
  if (!processDefinitionDetail) {
    message.error(t('oa.loan.processModelNotFound'))
    return
  }
  processDefinitionId.value = processDefinitionDetail.id
  startUserSelectTasks.value = processDefinitionDetail.startUserSelectTasks

  if (query.id) {
    await getDetail(Number(query.id))
  }

  await getApprovalDetail()
})

watch(
  formData.value,
  (newValue, oldValue) => {
    if (!oldValue) return
    if (newValue && Object.keys(newValue).length > 0) {
      tempStartUserSelectAssignees.value = startUserSelectAssignees.value
      startUserSelectAssignees.value = {}
      getApprovalDetail()
    }
  },
  { immediate: true }
)
</script>
