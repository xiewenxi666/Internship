<template>
  <ContentWrap>
    <el-descriptions :column="1" border>
      <el-descriptions-item :label="t('oa.request.title')">
        {{ detailData.title }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.request.type')">
        <dict-tag :type="DICT_TYPE.OA_REQUEST_TYPE" :value="detailData.type" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.request.urgency')">
        {{ detailData.urgency }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.request.content')">
        {{ detailData.content }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.request.expectedAmount')">
        {{ detailData.expectedAmount }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.request.status')">
        <dict-tag :type="DICT_TYPE.OA_REQUEST_STATUS" :value="detailData.status" />
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { propTypes } from '@/utils/propTypes'
import * as RequestApi from '@/api/oa/request'

defineOptions({ name: 'BpmOARequestDetail' })
const { t } = useI18n('bpm')
const { query } = useRoute()
const props = defineProps({ id: propTypes.number.def(undefined) })
const detailLoading = ref(false)
const detailData = ref<any>({})
const queryId = query.id as unknown as number

const getInfo = async () => {
  detailLoading.value = true
  try {
    detailData.value = await RequestApi.getRequest(props.id || queryId)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open: getInfo })
onMounted(() => { getInfo() })
</script>
