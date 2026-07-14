<template>
  <ContentWrap>
    <el-table :data="order.products" :stripe="true" :show-overflow-tooltip="true" :table-layout="'auto'">
      <el-table-column align="center" :label="t('crm.product.name')" fixed="left" prop="productName" min-width="160">
        <template #default="scope">
          {{ scope.row.productName }}
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.product.no')" align="center" prop="productNo" min-width="120" />
      <el-table-column align="center" :label="t('crm.product.unit')" prop="productUnit" min-width="160">
        <template #default="{ row }">
          <dict-tag :type="DICT_TYPE.CRM_PRODUCT_UNIT" :value="row.productUnit" />
        </template>
      </el-table-column>
      <el-table-column :label="t('crm.product.price') + '（元）'" align="center" prop="productPrice" min-width="140"
        :formatter="erpPriceTableColumnFormatter" />
      <el-table-column :label="t('crm.order.orderPrice') + '（元）'" align="center" prop="orderPrice" min-width="140"
        :formatter="erpPriceTableColumnFormatter" />
      <el-table-column align="center" :label="t('crm.business.count')" prop="count" min-width="100px"
        :formatter="erpPriceTableColumnFormatter" />
      <el-table-column :label="t('crm.business.total') + '（元）'" align="center" prop="totalPrice" min-width="140"
        :formatter="erpPriceTableColumnFormatter" />
    </el-table>
    <el-row class="mt-10px" justify="end">
      <el-col :span="3">
        {{ t('crm.order.discountPercent') }}：{{ erpPriceInputFormatter(order.discountPercent) }}%
      </el-col>
      <el-col :span="4">
        {{ t('crm.order.totalProductPrice') }}：{{ erpPriceInputFormatter(order.totalProductPrice) }} 元
      </el-col>
    </el-row>
  </ContentWrap>
</template>
<script setup lang="ts">
import * as OrderApi from '@/api/crm/order'
import { erpPriceInputFormatter, erpPriceTableColumnFormatter } from '@/utils'
import { DICT_TYPE } from '@/utils/dict'

const { t } = useI18n()
const { order } = defineProps<{
  order: OrderApi.OrderVO
}>()
</script>
