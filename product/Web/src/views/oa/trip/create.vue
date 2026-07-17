<template>
  <el-row :gutter="20">
    <el-col :span="16">
      <ContentWrap :title="t('oa.trip.title') + t('common.info')">
        <el-form
          ref="formRef"
          v-loading="formLoading"
          :model="formData"
          :rules="formRules"
          label-width="80px"
        >
          <el-form-item :label="t('oa.trip.type')" prop="type">
            <el-select v-model="formData.type" clearable :placeholder="t('oa.trip.typePlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.OA_TRIP_TYPE)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('oa.trip.destination')" prop="destination">
            <el-input v-model="formData.destination" :placeholder="t('oa.trip.destinationPlaceholder')" />
          </el-form-item>
          <el-form-item :label="t('oa.trip.startTime')" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              clearable
              :placeholder="t('common.selectTime')"
              type="datetime"
              value-format="x"
            />
          </el-form-item>
          <el-form-item :label="t('oa.trip.endTime')" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              clearable
              :placeholder="t('common.selectTime')"
              type="datetime"
              value-format="x"
            />
          </el-form-item>
          <el-form-item :label="t('oa.trip.reason')" prop="reason">
            <el-input v-model="formData.reason" :placeholder="t('oa.trip.reasonPlaceholder')" type="textarea" />
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
import { DICT_TYPE, getIntDictOptions } from '@/utils/dict'
import * as TripApi from '@/api/oa/trip'
import { useTagsViewStore } from '@/store/modules/tagsView'

import * as DefinitionApi from '@/api/bpm/definition'
import ProcessInstanceTimeline from '@/views/bpm/processInstance/detail/ProcessInstanceTimeline.vue'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import { CandidateStrategy, NodeId } from '@/components/SimpleProcessDesignerV2/src/consts'
import { ApprovalNodeInfo } from '@/api/bpm/processInstance'

defineOptions({ name: 'OaTripCreate' })

const message = useMessage()
const { t } = useI18n('bpm')
const { delView } = useTagsViewStore()
const { push, currentRoute } = useRouter()
const { query } = useRoute()

const formLoading = ref(false)
const formData = ref({
  type: undefined,
  destination: undefined,
  reason: undefined,
  startTime: undefined,
  endTime: undefined
})
const formRules = reactive({
  type: [{ required: true, message: t('oa.trip.type') + t('common.notEmpty'), trigger: 'blur' }],
  destination: [{ required: true, message: t('oa.trip.destination') + t('common.notEmpty'), trigger: 'blur' }],
  reason: [{ required: true, message: t('oa.trip.reason') + t('common.notEmpty'), trigger: 'change' }],
  startTime: [{ required: true, message: t('oa.trip.startTime') + t('common.notEmpty'), trigger: 'change' }],
  endTime: [{ required: true, message: t('oa.trip.endTime') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const processDefineKey = 'oa_trip'
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
      if (
        Array.isArray(startUserSelectAssignees.value[userTask.id]) &&
        startUserSelectAssignees.value[userTask.id].length === 0
      ) {
        return message.warning(t('process.instance.selectCandidate', { name: userTask.name }))
      }
    }
  }

  formLoading.value = true
  try {
    const data = { ...formData.value } as unknown as TripApi.TripVO
    if (startUserSelectTasks.value?.length > 0) {
      data.startUserSelectAssignees = startUserSelectAssignees.value
    }
    await TripApi.createTrip(data)
    message.success(t('process.instance.startSuccess'))
    delView(unref(currentRoute))
    await push('/bpm/oa/trip')
  } finally {
    formLoading.value = false
  }
}

const getApprovalDetail = async () => {
  try {
    const data = await ProcessInstanceApi.getApprovalDetail({
      processDefinitionId: processDefinitionId.value,
      activityId: NodeId.START_USER_NODE_ID,
      processVariablesStr: JSON.stringify({ day: daysDifference() })
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
        if (
          tempStartUserSelectAssignees.value[node.id] &&
          tempStartUserSelectAssignees.value[node.id].length > 0
        ) {
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

const daysDifference = () => {
  const oneDay = 24 * 60 * 60 * 1000
  const diffTime = Math.abs(Number(formData.value.endTime) - Number(formData.value.startTime))
  return Math.floor(diffTime / oneDay)
}

const getDetail = async (id: number) => {
  try {
    formLoading.value = true
    const data = await TripApi.getTrip(id)
    if (!data) {
      message.error(t('oa.trip.restartFailed') + '：' + t('oa.trip.dataNotFound'))
      return
    }
    formData.value = {
      type: data.type,
      destination: data.destination,
      reason: data.reason,
      startTime: data.startTime,
      endTime: data.endTime
    }
  } finally {
    formLoading.value = false
  }
}

onMounted(async () => {
  const processDefinitionDetail = await DefinitionApi.getProcessDefinition(
    undefined,
    processDefineKey
  )

  if (!processDefinitionDetail) {
    message.error(t('oa.trip.processModelNotFound'))
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
    if (!oldValue) {
      return
    }
    if (newValue && Object.keys(newValue).length > 0) {
      tempStartUserSelectAssignees.value = startUserSelectAssignees.value
      startUserSelectAssignees.value = {}
      getApprovalDetail()
    }
  },
  {
    immediate: true
  }
)
</script>
