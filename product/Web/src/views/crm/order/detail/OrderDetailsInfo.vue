<template>
  <ContentWrap>
    <el-collapse v-model="activeNames">
      <el-collapse-item name="orderInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('crm.customer.basicInfoTab') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('crm.order.no')">{{ order.no }}</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.name')">{{ order.name }}</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.customerName')">{{ order.customerName }}</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.businessName')">{{ order.businessName }}</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.totalPrice') + '（元）'">
            {{ erpPriceInputFormatter(order.totalPrice) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.orderDate')">
            {{ formatDate(order.orderDate) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.status')">
            <dict-tag :type="DICT_TYPE.CRM_ORDER_STATUS" :value="order.status" />
          </el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.remark')">
            {{ order.remark }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
      <el-collapse-item name="systemInfo">
        <template #title>
          <span class="text-base font-bold">{{ t('common.systemInfo') }}</span>
        </template>
        <el-descriptions :column="4">
          <el-descriptions-item :label="t('crm.order.ownerUserName')">{{ order.ownerUserName }}</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.contactLastTime')">
            {{ formatDate(order.contactLastTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="''">&nbsp;</el-descriptions-item>
          <el-descriptions-item :label="''">&nbsp;</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.creatorName')">{{ order.creatorName }}</el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.createTime')">
            {{ formatDate(order.createTime) }}
          </el-descriptions-item>
          <el-descriptions-item :label="t('crm.order.updateTime')">
            {{ formatDate(order.updateTime) }}
          </el-descriptions-item>
        </el-descriptions>
      </el-collapse-item>
    </el-collapse>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as OrderApi from '@/api/crm/order'
import { formatDate } from '@/utils/formatTime'
import { DICT_TYPE } from '@/utils/dict'
import { erpPriceInputFormatter } from '@/utils'

const { t } = useI18n()

defineOptions({ name: 'OrderDetailsInfo' })
defineProps<{
  order: OrderApi.OrderVO
}>()

const activeNames = ref(['orderInfo', 'systemInfo'])
</script>
