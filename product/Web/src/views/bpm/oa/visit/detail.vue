<template>
  <ContentWrap>
    <el-descriptions :column="1" border>
      <el-descriptions-item :label="t('oa.visit.customerName')">
        {{ detailData.customerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.contactPerson')">
        {{ detailData.contactPerson }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.contactPhone')">
        {{ detailData.contactPhone }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.visitAddress')">
        {{ detailData.visitAddress }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.visitTime')">
        {{ formatDate(detailData.visitTime, 'YYYY-MM-DD HH:mm') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.purpose')">
        {{ detailData.purpose }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.result')">
        {{ detailData.result }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.nextVisitTime')">
        {{ formatDate(detailData.nextVisitTime, 'YYYY-MM-DD') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.status')">
        <dict-tag :type="DICT_TYPE.OA_VISIT_STATUS" :value="detailData.status" />
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { propTypes } from '@/utils/propTypes'
import * as VisitApi from '@/api/oa/visit'

defineOptions({ name: 'BpmOAVisitDetail' })
const { t } = useI18n('bpm')
const { query } = useRoute()
const props = defineProps({ id: propTypes.number.def(undefined) })
const detailLoading = ref(false)
const detailData = ref<any>({})
const queryId = query.id as unknown as number

const getInfo = async () => {
  detailLoading.value = true
  try {
    detailData.value = await VisitApi.getVisit(props.id || queryId)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open: getInfo })
onMounted(() => { getInfo() })
</script>
