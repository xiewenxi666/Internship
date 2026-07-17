<template>
  <el-row :gutter="20">
    <el-col :span="16">
      <ContentWrap :title="t('oa.visit.title') + t('common.info')">
        <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="80px">
          <el-form-item :label="t('oa.visit.customerId')" prop="customerId">
            <el-input-number v-model="formData.customerId" :min="0" :step="1" controls-position="right" :placeholder="t('oa.visit.customerId')" style="width: 100%" />
          </el-form-item>
          <el-form-item :label="t('oa.visit.contactPerson')" prop="contactPerson">
            <el-input v-model="formData.contactPerson" :placeholder="t('oa.visit.contactPersonPlaceholder')" />
          </el-form-item>
          <el-form-item :label="t('oa.visit.contactPhone')" prop="contactPhone">
            <el-input v-model="formData.contactPhone" :placeholder="t('oa.visit.contactPhonePlaceholder')" />
          </el-form-item>
          <el-form-item :label="t('oa.visit.visitTime')" prop="visitTime">
            <el-date-picker v-model="formData.visitTime" clearable :placeholder="t('oa.visit.visitTimePlaceholder')" type="datetime" value-format="x" style="width: 100%" />
          </el-form-item>
          <el-form-item :label="t('oa.visit.location')" prop="location">
            <el-input v-model="formData.location" :placeholder="t('oa.visit.locationPlaceholder')" />
          </el-form-item>
          <el-form-item :label="t('oa.visit.purpose')" prop="purpose">
            <el-input v-model="formData.purpose" :placeholder="t('oa.visit.purposePlaceholder')" />
          </el-form-item>
          <el-form-item :label="t('oa.visit.notes')" prop="notes">
            <el-input v-model="formData.notes" :placeholder="t('oa.visit.notesPlaceholder')" type="textarea" />
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
import * as VisitApi from '@/api/oa/visit'
import { useTagsViewStore } from '@/store/modules/tagsView'

import * as DefinitionApi from '@/api/bpm/definition'
import ProcessInstanceTimeline from '@/views/bpm/processInstance/detail/ProcessInstanceTimeline.vue'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import { CandidateStrategy, NodeId } from '@/components/SimpleProcessDesignerV2/src/consts'
import { ApprovalNodeInfo } from '@/api/bpm/processInstance'

defineOptions({ name: 'OaVisitCreate' })

const message = useMessage()
const { t } = useI18n('bpm')
const { delView } = useTagsViewStore()
const { push, currentRoute } = useRouter()
const { query } = useRoute()

const formLoading = ref(false)
const formData = ref({
  customerId: undefined,
  contactPerson: undefined,
  contactPhone: undefined,
  visitTime: undefined,
  location: undefined,
  purpose: undefined,
  notes: undefined
})
const formRules = reactive({
  contactPerson: [{ required: true, message: t('oa.visit.contactPerson') + t('common.notEmpty'), trigger: 'blur' }],
  visitTime: [{ required: true, message: t('oa.visit.visitTime') + t('common.notEmpty'), trigger: 'change' }],
  location: [{ required: true, message: t('oa.visit.location') + t('common.notEmpty'), trigger: 'blur' }]
})
const formRef = ref()

const processDefineKey = 'oa_visit'
const startUserSelectTasks = ref([])
const startUserSelectAssignees = ref({})
const tempStartUserSelectAssignees = ref({})
const activityNodes = ref<ProcessInstanceApi.ApprovalNodeInfo[]>([])
const processDefinitionId = ref('')

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
    const data = { ...formData.value } as unknown as VisitApi.VisitVO
    if (startUserSelectTasks.value?.length > 0) {
      data.startUserSelectAssignees = startUserSelectAssignees.value
    }
    await VisitApi.createVisit(data)
    message.success(t('process.instance.startSuccess'))
    delView(unref(currentRoute))
    await push({ name: 'OaVisit' })
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
    const data = await VisitApi.getVisit(id)
    if (!data) {
      message.error(t('oa.visit.restartFailed') + '：' + t('oa.visit.dataNotFound'))
      return
    }
    formData.value = {
      customerId: data.customerId,
      contactPerson: data.contactPerson,
      contactPhone: data.contactPhone,
      visitTime: data.visitTime,
      location: data.location,
      purpose: data.purpose,
      notes: data.notes
    }
  } finally {
    formLoading.value = false
  }
}

onMounted(async () => {
  const processDefinitionDetail = await DefinitionApi.getProcessDefinition(undefined, processDefineKey)
  if (!processDefinitionDetail) {
    message.error(t('oa.visit.processModelNotFound'))
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
