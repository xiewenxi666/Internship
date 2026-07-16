<!-- -23计算机科学与技术2班-龚小波 -->
<template>
  <Dialog v-model="dialogVisible" :title="title" width="500">
    <!-- 步骤一：下载模板 -->
    <div v-if="currentStep === 0">
      <el-result icon="info" :title="t('crm.import.step1Title')" :sub-title="t('crm.import.step1Desc')">
        <template #extra>
          <el-link type="primary" :underline="false" @click="downloadTemplate">
            <Icon icon="ep:download" class="mr-5px" />
            {{ t('crm.import.downloadTemplate') }}
          </el-link>
        </template>
      </el-result>
    </div>

    <!-- 步骤二：上传文件 -->
    <div v-if="currentStep === 1">
      <el-upload
        ref="uploadRef"
        v-model:file-list="fileList"
        :action="uploadUrl"
        :auto-upload="false"
        :disabled="formLoading"
        :headers="uploadHeaders"
        :limit="1"
        :on-error="submitFormError"
        :on-exceed="handleExceed"
        :on-success="submitFormSuccess"
        accept=".xlsx, .xls"
        drag
      >
        <Icon icon="ep:upload" />
        <div class="el-upload__text">{{ t('crm.import.dragTip') }}</div>
        <template #tip>
          <div class="el-upload__tip">
            {{ t('crm.import.fileTypeTip') }}
          </div>
        </template>
      </el-upload>
    </div>

    <!-- 步骤三：导入完成 -->
    <div v-if="currentStep === 2">
      <el-result icon="success" :title="t('crm.import.successTitle')" />
    </div>

    <template #footer>
      <el-button v-if="currentStep === 0" type="primary" @click="currentStep = 1">
        {{ t('crm.import.nextStep') }}
      </el-button>
      <template v-else-if="currentStep === 1">
        <el-button :disabled="formLoading" type="primary" @click="submitForm">
          {{ t('crm.import.confirmImport') }}
        </el-button>
        <el-button @click="currentStep = 0">{{ t('common.previous') }}</el-button>
      </template>
      <el-button v-else-if="currentStep === 2" type="primary" @click="dialogVisible = false">
        {{ t('common.ok') }}
      </el-button>
      <el-button @click="dialogVisible = false">{{ t('common.cancel') }}</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import { getAccessToken, getTenantId } from '@/utils/auth'
import download from '@/utils/download'

defineOptions({ name: 'CrmImportDialog' })

const props = defineProps<{
  /** 弹窗标题 */
  title: string
  /** 下载模板的 API 方法 */
  templateApi: () => Promise<any>
  /** 上传文件的 API 地址（完整 URL） */
  uploadUrl: string
}>()

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const formLoading = ref(false)
const uploadRef = ref()
const fileList = ref([])
const currentStep = ref(0)
const uploadHeaders = ref()

/** 打开弹窗 */
const open = () => {
  dialogVisible.value = true
  currentStep.value = 0
  fileList.value = []
  formLoading.value = false
}
defineExpose({ open })

/** 下载模板 */
const downloadTemplate = async () => {
  try {
    const res = await props.templateApi()
    download.excel(res, props.title + t('crm.import.templateSuffix'))
  } catch {
    message.error(t('crm.import.downloadError'))
  }
}

/** 提交上传 */
const submitForm = async () => {
  if (fileList.value.length === 0) {
    message.error(t('crm.import.selectFile'))
    return
  }
  uploadHeaders.value = {
    Authorization: 'Bearer ' + getAccessToken(),
    'tenant-id': getTenantId()
  }
  formLoading.value = true
  uploadRef.value!.submit()
}

/** 上传成功 */
const emits = defineEmits(['success'])
const submitFormSuccess = (response: any) => {
  formLoading.value = false
  if (response.code !== 0) {
    message.error(response.msg)
    return
  }
  currentStep.value = 2
  emits('success')
}

/** 上传失败 */
const submitFormError = (): void => {
  message.error(t('crm.import.uploadError'))
  formLoading.value = false
}

/** 超出文件限制 */
const handleExceed = (): void => {
  message.error(t('crm.import.exceedLimit'))
}
</script>
