<template>
  <ContentWrap>
    <el-descriptions :column="1" border>
      <el-descriptions-item :label="t('oa.visit.customerId')">
        {{ detailData.customerId }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.contactPerson')">
        {{ detailData.contactPerson }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.contactPhone')">
        {{ detailData.contactPhone }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.visitTime')">
        {{ formatDate(detailData.visitTime, 'YYYY-MM-DD HH:mm:ss') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.location')">
        {{ detailData.location }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.purpose')">
        {{ detailData.purpose }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.visit.notes')">
        {{ detailData.notes }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { formatDate } from '@/utils/formatTime'
import { propTypes } from '@/utils/propTypes'
import * as VisitApi from '@/api/oa/visit'

defineOptions({ name: 'OaVisitDetail' })

const { t } = useI18n('bpm')
const { query } = useRoute()

const props = defineProps({
  id: propTypes.number.def(undefined)
})
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

onMounted(() => {
  getInfo()
})
</script>
