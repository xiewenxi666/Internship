<template>
  <el-row :gutter="20">
    <el-col :span="16">
      <ContentWrap :title="t('oa.request.title') + t('common.info')">
        <el-form
          ref="formRef"
          v-loading="formLoading"
          :model="formData"
          :rules="formRules"
          label-width="80px"
        >
          <el-form-item :label="t('oa.request.title')" prop="title">
            <el-input v-model="formData.title" :placeholder="t('oa.request.titlePlaceholder')" />
          </el-form-item>
          <el-form-item :label="t('oa.request.urgency')" prop="urgency">
            <el-select v-model="formData.urgency" clearable :placeholder="t('oa.request.urgencyPlaceholder')">
              <el-option
                v-for="dict in getIntDictOptions(DICT_TYPE.OA_REQUEST_URGENCY)"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item :label="t('oa.request.content')" prop="content">
            <el-input v-model="formData.content" :placeholder="t('oa.request.contentPlaceholder')" type="textarea" :rows="6" />
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
import * as RequestApi from '@/api/oa/request'
import { useTagsViewStore } from '@/store/modules/tagsView'

import * as DefinitionApi from '@/api/bpm/definition'
import ProcessInstanceTimeline from '@/views/bpm/processInstance/detail/ProcessInstanceTimeline.vue'
import * as ProcessInstanceApi from '@/api/bpm/processInstance'
import { CandidateStrategy, NodeId } from '@/components/SimpleProcessDesignerV2/src/consts'
import { ApprovalNodeInfo } from '@/api/bpm/processInstance'

defineOptions({ name: 'OaRequestCreate' })

const message = useMessage()
const { t } = useI18n('bpm')
const { delView } = useTagsViewStore()
const { push, currentRoute } = useRouter()
const { query } = useRoute()

const formLoading = ref(false)
const formData = ref({
  title: undefined,
  urgency: undefined,
  content: undefined
})
const formRules = reactive({
  title: [{ required: true, message: t('oa.request.title') + t('common.notEmpty'), trigger: 'blur' }],
  urgency: [{ required: true, message: t('oa.request.urgency') + t('common.notEmpty'), trigger: 'blur' }],
  content: [{ required: true, message: t('oa.request.content') + t('common.notEmpty'), trigger: 'change' }]
})
const formRef = ref()

const processDefineKey = 'oa_request'
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
    const data = { ...formData.value } as unknown as RequestApi.RequestVO
    if (startUserSelectTasks.value?.length > 0) {
      data.startUserSelectAssignees = startUserSelectAssignees.value
    }
    await RequestApi.createRequest(data)
    message.success(t('process.instance.startSuccess'))
    delView(unref(currentRoute))
    await push({ name: 'OaRequest' })
  } finally {
    formLoading.value = false
  }
}

const getApprovalDetail = async () => {
  try {
    const data = await ProcessInstanceApi.getApprovalDetail({
      processDefinitionId: processDefinitionId.value,
      activityId: NodeId.START_USER_NODE_ID
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

const getDetail = async (id: number) => {
  try {
    formLoading.value = true
    const data = await RequestApi.getRequest(id)
    if (!data) {
      message.error(t('oa.request.restartFailed') + '：' + t('oa.request.dataNotFound'))
      return
    }
    formData.value = {
      title: data.title,
      urgency: data.urgency,
      content: data.content
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
    message.error(t('oa.request.processModelNotFound'))
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
