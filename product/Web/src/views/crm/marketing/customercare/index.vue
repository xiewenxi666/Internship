<template>
  <ContentWrap>
    <div class="care-header">
      <h2><Icon icon="ep:present" :size="24" color="#a855f7" style="vertical-align:middle;margin-right:8px" />客户关怀设置</h2>
      <el-tag type="success" v-if="configSaved">配置已保存</el-tag>
    </div>

    <el-row :gutter="20" class="care-body">
      <el-col :span="12">
        <el-card shadow="hover" class="care-card">
          <template #header>
            <div class="card-title"><Icon icon="ep:message" color="#409eff" /> 邮件祝福模板</div>
          </template>
          <el-form ref="formRef" v-loading="loading" :model="formData" label-width="110px">
            <el-form-item label="发件人邮箱">
              <el-input v-model="formData.senderEmail" style="width:280px" />
            </el-form-item>
            <el-form-item label="收件人邮箱">
              <el-input v-model="formData.testEmail" placeholder="邮件将发送至此地址" style="width:280px" />
            </el-form-item>
            <el-form-item label="定时发送">
              <el-time-picker v-model="formData.sendTime" format="HH:mm" value-format="HH:mm" style="width:200px" />
            </el-form-item>
            <el-form-item label="邮件标题">
              <el-input v-model="formData.emailTitle" placeholder="支持 {称呼}{年龄}{客户名}" style="width:280px" />
            </el-form-item>
            <el-form-item label="邮件正文">
              <el-input v-model="formData.emailBody" type="textarea" :rows="5" placeholder="支持 {称呼}{年龄}{客户名}" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave" :loading="saving">保存设置</el-button>
              <el-button type="success" @click="handleTestMail" :loading="testing" style="margin-left:12px">发送邮件</el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <el-col :span="12">
        <el-card shadow="hover" class="care-card">
          <template #header>
            <div class="card-title"><Icon icon="ep:view" color="#409eff" /> 邮件预览 & 发送记录</div>
          </template>
          <div class="preview-box">
            <div class="preview-row"><span>发件人</span><b>{{ formData.senderEmail }}</b></div>
            <div class="preview-row"><span>收件人</span><b>{{ formData.testEmail || '未填写' }}</b></div>
            <div class="preview-row"><span>标题</span><b>{{ formData.emailTitle }}</b></div>
            <div class="preview-row"><span>定时</span><b>{{ formData.sendTime }}</b></div>
            <el-divider />
            <div class="preview-content">{{ formData.emailBody || '正文内容预览...' }}</div>
          </div>
          <el-divider />
          <el-row :gutter="12">
            <el-col :span="12" class="stat-block">
              <div class="stat-num blue">{{ testCount }}</div>
              <div class="stat-name">已发送数</div>
            </el-col>
            <el-col :span="12" class="stat-block">
              <div class="stat-num purple">{{ formData.sendTime }}</div>
              <div class="stat-name">定时发送</div>
            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </ContentWrap>
</template>

<script lang="ts" setup>
import * as CustomerCareApi from '@/api/crm/customercare'

defineOptions({ name: 'CrmCustomerCare' })

const message = useMessage()
const loading = ref(false)
const saving = ref(false)
const testing = ref(false)
const configSaved = ref(false)
const testCount = ref(0)
const formRef = ref()
const formData = ref<CustomerCareApi.CustomerCareConfigVO>({
  id: undefined, smsContent: '', emailTitle: '生日祝福', emailBody: '亲爱的{称呼}您好，今天是您{年龄}岁的生日，祝福您生日快乐！', senderEmail: 'mitedtsm_09@email.com', testEmail: '',
  sendTime: '09:00', smsEnabled: false, emailEnabled: true, holidayList: '[]',
  createTime: undefined as any, updateTime: undefined as any
})

const loadConfig = async () => { loading.value = true; try { const d = await CustomerCareApi.getConfig(); if (d?.emailBody) formData.value = { ...formData.value, ...d } } catch {} finally { loading.value = false } }

const handleSave = async () => {
  saving.value = true
  try { await CustomerCareApi.saveConfig(formData.value); configSaved.value = true; message.success('保存成功'); setTimeout(() => configSaved.value = false, 3000) } finally { saving.value = false }
}

const handleTestMail = async () => {
  if (!formData.value.testEmail?.trim()) { message.warning('请填写收件人邮箱'); return }
  testing.value = true
  try {
    await CustomerCareApi.sendTestEmail(formData.value)
    testCount.value++
    message.success('测试邮件已发送至 ' + formData.value.testEmail)
  } catch (e: any) { message.error(e?.message || '发送失败') }
  finally { testing.value = false }
}

onMounted(() => { loadConfig() })
</script>

<style scoped>
.care-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.care-header h2 { margin: 0; font-size: 18px; color: #303133; }
.care-body { margin: 0; }
.care-card { height: 100%; }
.card-title { display: flex; align-items: center; gap: 6px; font-weight: 600; }
.hint { margin-left: 8px; font-size: 12px; color: #909399; }
.preview-box { font-size: 13px; }
.preview-row { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px dashed #ebeef5; }
.preview-row span { color: #909399; }
.preview-row b { color: #303133; }
.preview-content { background: #f5f7fa; padding: 12px; border-radius: 6px; color: #606266; line-height: 1.8; min-height: 80px; font-size: 13px; }
.stat-block { text-align: center; padding: 8px 0; }
.stat-num { font-size: 26px; font-weight: 700; }
.stat-num.blue { color: #409eff; }
.stat-num.green { color: #67c23a; }
.stat-num.purple { color: #a855f7; }
.stat-name { font-size: 12px; color: #909399; margin-top: 2px; }
</style>
