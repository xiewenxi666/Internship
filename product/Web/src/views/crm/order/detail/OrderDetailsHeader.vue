<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ order.name }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <slot></slot>
      </div>
    </div>
  </div>
  <ContentWrap class="mt-10px">
    <el-descriptions :column="5" direction="vertical">
      <el-descriptions-item :label="t('crm.order.customerName')">
        {{ order.customerName }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('crm.order.totalPrice') + '（元）'">
        {{ erpPriceInputFormatter(order.totalPrice) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('crm.order.orderDate')">
        {{ formatDate(order.orderDate) }}
      </el-descriptions-item>
      <el-descriptions-item :label="t('crm.order.status')">
        <dict-tag :type="DICT_TYPE.CRM_ORDER_STATUS" :value="order.status" />
      </el-descriptions-item>
      <el-descriptions-item :label="t('crm.order.ownerUserName')">
        {{ order.ownerUserName }}
      </el-descriptions-item>
    </el-descriptions>
  </ContentWrap>
</template>
<script lang="ts" setup>
import * as OrderApi from '@/api/crm/order'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
import { DICT_TYPE } from '@/utils/dict'

const { t } = useI18n()

defineOptions({ name: 'OrderDetailsHeader' })
defineProps<{ order: OrderApi.OrderVO }>()
</script>
