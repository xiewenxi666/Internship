<template>
  <ContentWrap>
    <el-descriptions :column="1" border>
      <el-descriptions-item :label="t('oa.loan.amount')">
        {{ detailData.amount }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.loan.purpose')">
        {{ detailData.purpose }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.loan.repaymentPlan')">
        {{ detailData.repaymentPlan }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.loan.expectedRepaymentTime')">
        {{ formatDate(detailData.expectedRepaymentTime, 'YYYY-MM-DD') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.loan.actualRepaymentTime')">
        {{ formatDate(detailData.actualRepaymentTime, 'YYYY-MM-DD') }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('oa.loan.status')">
        <dict-tag :type="DICT_TYPE.OA_LOAN_STATUS" :value="detailData.status" />
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { propTypes } from '@/utils/propTypes'
import * as LoanApi from '@/api/oa/loan'

defineOptions({ name: 'BpmOALoanDetail' })
const { t } = useI18n('bpm')
const { query } = useRoute()
const props = defineProps({ id: propTypes.number.def(undefined) })
const detailLoading = ref(false)
const detailData = ref<any>({})
const queryId = query.id as unknown as number

const getInfo = async () => {
  detailLoading.value = true
  try {
    detailData.value = await LoanApi.getLoan(props.id || queryId)
  } finally {
    detailLoading.value = false
  }
}
defineExpose({ open: getInfo })
onMounted(() => { getInfo() })
</script>
