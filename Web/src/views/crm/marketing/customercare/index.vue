<template>
  <ContentWrap title="客户关怀设置">
    <el-form ref="formRef" v-loading="loading" :model="formData" label-width="120px">
      <el-divider content-position="left">短信祝福设置</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="启用短信祝福">
            <el-switch v-model="formData.smsEnabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发送时间">
            <el-time-picker v-model="formData.sendTime" format="HH:mm" value-format="HH:mm" placeholder="选择发送时间" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="短信内容">
            <el-input v-model="formData.smsContent" type="textarea" :rows="3" placeholder="支持变量：{称呼}、{年龄}、{客户名}" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-divider content-position="left">邮件祝福设置</el-divider>
      <el-row>
        <el-col :span="12">
          <el-form-item label="启用邮件祝福">
            <el-switch v-model="formData.emailEnabled" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="发件人邮箱">
            <el-input v-model="formData.senderEmail" placeholder="请输入公司邮箱地址" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="邮件标题">
            <el-input v-model="formData.emailTitle" placeholder="支持变量：{称呼}、{年龄}、{客户名}" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="邮件正文">
            <el-input v-model="formData.emailBody" type="textarea" :rows="5" placeholder="HTML格式邮件正文" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" @click="handleSave" :loading="saving">保存设置</el-button>
        <el-button type="success" @click="handleTestSms" :loading="testingSms" style="margin-left:10px">测试发送短信</el-button>
        <el-button type="warning" @click="handleTestMail" :loading="testingMail" style="margin-left:10px">测试发送邮件</el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as CustomerCareApi from '@/api/crm/customercare'
import * as BulksendApi from '@/api/crm/bulksend'

defineOptions({ name: 'CrmCustomerCare' })

const message = useMessage()
const loading = ref(false)
const saving = ref(false)
const testingSms = ref(false)
const testingMail = ref(false)

const doTestSend = async (type: number) => {
  await CustomerCareApi.saveConfig(formData.value)
  const content = type === 1 ? formData.value.smsContent : formData.value.emailBody
  const res = await BulksendApi.createBulkSend({
    title: '测试-' + (type === 1 ? '短信' : '邮件') + '-' + Date.now(),
    type, content, targetType: 1, targetCount: 1, ownerUserId: 1, status: 1
  } as any)
  const id = (res as any)?.id || (res as any)?.data || res
  await BulksendApi.submitForApproval(id as number)
  await BulksendApi.approve(id as number)
  message.success(type === 1 ? '测试短信已发送！' : '测试邮件已发送！')
}

const handleTestSms = async () => { testingSms.value = true; try { await doTestSend(1) } finally { testingSms.value = false } }
const handleTestMail = async () => { testingMail.value = true; try { await doTestSend(2) } finally { testingMail.value = false } }
const formRef = ref()
const formData = ref<CustomerCareApi.CustomerCareConfigVO>({
  id: undefined, smsContent: '', emailTitle: '', emailBody: '', senderEmail: '',
  sendTime: '09:00', smsEnabled: true, emailEnabled: true, holidayList: '[]',
  createTime: undefined as any, updateTime: undefined as any
})

const loadConfig = async () => {
  loading.value = true
  try {
    const data = await CustomerCareApi.getConfig()
    if (data && data.smsContent) formData.value = { ...formData.value, ...data }
  } catch {} finally { loading.value = false }
}

const handleSave = async () => {
  saving.value = true
  try {
    await CustomerCareApi.saveConfig(formData.value)
    message.success('保存成功')
  } finally { saving.value = false }
}

onMounted(() => loadConfig())
</script>
