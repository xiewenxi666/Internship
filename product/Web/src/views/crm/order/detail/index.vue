<template>
  <OrderDetailsHeader v-loading="loading" :order="order">
    <el-button v-if="permissionListRef?.validateWrite" @click="openForm('update', order.id)">
      {{ t('common.edit') }}
    </el-button>
    <el-button
      v-if="order.status === 15"
      v-hasPermi="['crm:order:update']"
      type="warning"
      @click="handleWithdraw"
    >
      {{ t('crm.order.withdrawAudit') }}
    </el-button>
    <el-button v-if="permissionListRef?.validateOwnerUser" type="primary" @click="transferOrderObj">
      {{ t('crm.customer.transfer') }}
    </el-button>
  </OrderDetailsHeader>
  <el-col>
    <el-tabs>
      <el-tab-pane :label="t('crm.order.followUpTab')">
        <FollowUpList :biz-id="order.id" :biz-type="BizTypeEnum.CRM_ORDER" />
      </el-tab-pane>
      <el-tab-pane :label="t('crm.order.basicInfoTab')">
        <OrderDetailsInfo :order="order" />
      </el-tab-pane>
      <el-tab-pane :label="t('crm.order.productTab')">
        <OrderProductList :order="order" />
      </el-tab-pane>
      <el-tab-pane :label="t('crm.order.teamMemberTab')">
        <PermissionList
          ref="permissionListRef"
          :biz-id="order.id!"
          :biz-type="BizTypeEnum.CRM_ORDER"
          :show-action="true"
          @quit-team="close"
        />
      </el-tab-pane>
      <el-tab-pane :label="t('crm.order.operateLogTab')">
        <OperateLogV2 :log-list="logList" />
      </el-tab-pane>
    </el-tabs>
  </el-col>
  <OrderForm ref="formRef" @success="getOrderData" />
  <CrmTransferForm ref="transferFormRef" :biz-type="BizTypeEnum.CRM_ORDER" @success="close" />
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import { OperateLogVO } from '@/api/system/operatelog'
import * as OrderApi from '@/api/crm/order'
import OrderDetailsInfo from './OrderDetailsInfo.vue'
import OrderDetailsHeader from './OrderDetailsHeader.vue'
import OrderProductList from './OrderProductList.vue'
import { BizTypeEnum } from '@/api/crm/permission'
import { getOperateLogPage } from '@/api/crm/operateLog'
import OrderForm from '@/views/crm/order/OrderForm.vue'
import CrmTransferForm from '@/views/crm/permission/components/TransferForm.vue'
import PermissionList from '@/views/crm/permission/components/PermissionList.vue'
import FollowUpList from '@/views/crm/followup/index.vue'

defineOptions({ name: 'CrmOrderDetail' })

const { t } = useI18n()
const props = defineProps<{ id?: number }>()

const route = useRoute()
const message = useMessage()
const orderId = ref(0)
const loading = ref(true)
const order = ref<OrderApi.OrderVO>({} as OrderApi.OrderVO)
const permissionListRef = ref<InstanceType<typeof PermissionList>>()

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const getOrderData = async () => {
  loading.value = true
  try {
    order.value = await OrderApi.getOrder(orderId.value)
    await getOperateLog(orderId.value)
  } finally {
    loading.value = false
  }
}

const logList = ref<OperateLogVO[]>([])
const getOperateLog = async (id: number) => {
  if (!id) return
  const data = await getOperateLogPage({
    bizType: BizTypeEnum.CRM_ORDER,
    bizId: id
  })
  logList.value = data.list
}

const handleWithdraw = async () => {
  await message.confirm(t('crm.order.withdrawAuditConfirm', { name: order.value.name }))
  await OrderApi.withdrawOrder(order.value.id)
  message.success(t('crm.order.withdrawAuditSuccess'))
  await getOrderData()
}

const transferFormRef = ref<InstanceType<typeof CrmTransferForm>>()
const transferOrderObj = () => {
  transferFormRef.value?.open(order.value.id)
}

const { delView } = useTagsViewStore()
const { currentRoute } = useRouter()
const close = () => {
  delView(unref(currentRoute))
}

onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning(t('crm.order.paramError'))
    close()
    return
  }
  orderId.value = id as unknown as number
  await getOrderData()
})
</script>
