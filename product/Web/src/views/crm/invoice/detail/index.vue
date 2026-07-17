<template>
  <div>
    <div class="flex items-start justify-between">
      <div>
        <el-col>
          <el-row>
            <span class="text-xl font-bold">{{ invoice.no }}</span>
          </el-row>
        </el-col>
      </div>
      <div>
        <el-button
          v-hasPermi="['crm:invoice:update']"
          type="primary"
          @click="openForm('update', invoice.id)"
        >
          编辑
        </el-button>
      </div>
    </div>
    <ContentWrap class="mt-10px">
      <el-descriptions :column="5" direction="vertical">
        <el-descriptions-item label="关联订单">
          {{ invoice.contract?.no }}
        </el-descriptions-item>
        <el-descriptions-item label="开票日期">
          {{ formatDate(invoice.invoiceDate, 'YYYY-MM-DD') }}
        </el-descriptions-item>
        <el-descriptions-item label="票据类型">
          <dict-tag :type="DICT_TYPE.CRM_INVOICE_TYPE" :value="invoice.type" />
        </el-descriptions-item>
        <el-descriptions-item label="开票金额">
          {{ erpPriceInputFormatter(invoice.price) }}
        </el-descriptions-item>
        <el-descriptions-item label="发票号码">
          {{ invoice.invoiceNo }}
        </el-descriptions-item>
      </el-descriptions>
    </ContentWrap>

    <ContentWrap>
      <el-collapse v-model="activeNames">
        <el-collapse-item name="basicInfo">
          <template #title>
            <span class="text-base font-bold">基本信息</span>
          </template>
          <el-descriptions :column="4">
            <el-descriptions-item label="发票编号">{{ invoice.no }}</el-descriptions-item>
            <el-descriptions-item label="关联订单">{{ invoice.contract?.no }}</el-descriptions-item>
            <el-descriptions-item label="开票日期">{{ formatDate(invoice.invoiceDate, 'YYYY-MM-DD') }}</el-descriptions-item>
            <el-descriptions-item label="票据类型">
              <dict-tag :type="DICT_TYPE.CRM_INVOICE_TYPE" :value="invoice.type" />
            </el-descriptions-item>
            <el-descriptions-item label="开票金额">{{ erpPriceInputFormatter(invoice.price) }}</el-descriptions-item>
            <el-descriptions-item label="发票号码">{{ invoice.invoiceNo }}</el-descriptions-item>
            <el-descriptions-item label="票据内容">{{ invoice.content }}</el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
        <el-collapse-item name="systemInfo">
          <template #title>
            <span class="text-base font-bold">系统信息</span>
          </template>
          <el-descriptions :column="4">
            <el-descriptions-item label="经手人员">
              {{ invoice.handlerUserName }}
            </el-descriptions-item>
            <el-descriptions-item label="订单所属人员">
              {{ invoice.ownerUserName }}
            </el-descriptions-item>
            <el-descriptions-item label="创建人">
              {{ invoice.creatorName }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ formatDate(invoice.createTime) }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间">
              {{ formatDate(invoice.updateTime) }}
            </el-descriptions-item>
          </el-descriptions>
        </el-collapse-item>
      </el-collapse>
    </ContentWrap>

    <InvoiceForm ref="formRef" @success="onFormSuccess" />
  </div>
</template>
<script lang="ts" setup>
import { useTagsViewStore } from '@/store/modules/tagsView'
import * as InvoiceApi from '@/api/crm/invoice'
import { DICT_TYPE } from '@/utils/dict'
import { formatDate } from '@/utils/formatTime'
import { erpPriceInputFormatter } from '@/utils'
import InvoiceForm from '@/views/crm/invoice/InvoiceForm.vue'

defineOptions({ name: 'CrmInvoiceDetail' })

const props = defineProps<{ id?: number }>()

const route = useRoute()
const message = useMessage()
const invoiceId = ref(0)
const loading = ref(true)
const invoice = ref<InvoiceApi.InvoiceVO>({} as InvoiceApi.InvoiceVO)
const activeNames = ref(['basicInfo', 'systemInfo'])

const getInvoice = async (id: number) => {
  loading.value = true
  try {
    invoice.value = await InvoiceApi.getInvoice(id)
    if (invoice.value.no) {
      updateTagTitle(invoice.value.no)
    }
  } finally {
    loading.value = false
  }
}

const { currentRoute } = useRouter()
const tagsViewStore = useTagsViewStore()
const updateTagTitle = (title: string) => {
  tagsViewStore.updateVisitedView({ ...unref(currentRoute), title })
}

const formRef = ref()
const openForm = (type: string, id?: number) => {
  formRef.value.open(type, id)
}

const onFormSuccess = () => {
  getInvoice(invoiceId.value)
}

const { delView } = useTagsViewStore()
const close = () => {
  delView(unref(currentRoute))
}

const { params } = useRoute()
onMounted(async () => {
  const id = props.id || route.params.id
  if (!id) {
    message.warning('参数错误，发票不能为空！')
    close()
    return
  }
  invoiceId.value = id
  await getInvoice(invoiceId.value)
})
</script>
