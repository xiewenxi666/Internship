<template>
  <Dialog
    :model-value="visible"
    :title="title || '导入数据'"
    width="700px"
    @close="$emit('close')"
    @update:model-value="$emit('update:visible', $event)"
  >
    <div class="import-dialog">
      <!-- 步骤1: 下载模板 -->
      <el-steps :active="currentStep" align-center class="mb-20">
        <el-step title="下载模板" />
        <el-step title="上传文件" />
        <el-step title="预览确认" />
      </el-steps>

      <!-- 步骤1内容 -->
      <div v-if="currentStep === 0" class="step-content tac">
        <p class="mb-20">请先下载导入模板，按照模板格式填写数据后上传</p>
        <el-button type="primary" @click="handleDownloadTemplate">
          下载导入模板
        </el-button>
        <div class="mt-10">
          <el-button type="success" @click="currentStep = 1">
            已下载，下一步
          </el-button>
        </div>
      </div>

      <!-- 步骤2内容 -->
      <div v-if="currentStep === 1" class="step-content">
        <el-upload
          ref="uploadRef"
          drag
          :auto-upload="false"
          :limit="1"
          accept=".xls,.xlsx"
          :on-change="handleFileChange"
          :on-remove="handleFileRemove"
        >
          <el-icon class="el-icon--upload"><i-ep-upload-filled /></el-icon>
          <div class="el-upload__text">
            将填写好的 Excel 文件拖到此处，或<em>点击上传</em>
          </div>
        </el-upload>
        <div class="tac mt-10">
          <el-button @click="currentStep = 0">上一步</el-button>
          <el-button type="primary" :disabled="!uploadFile" @click="handleUpload">
            上传并预览
          </el-button>
        </div>
      </div>

      <!-- 步骤3内容 -->
      <div v-if="currentStep === 2" class="step-content">
        <el-alert
          :type="importResult?.successCount > 0 ? 'success' : 'error'"
          :closable="false"
          show-icon
          class="mb-10"
        >
          <template #title>
            共 {{ importResult?.totalCount ?? 0 }} 条数据，
            成功 {{ importResult?.successCount ?? 0 }} 条，
            失败 {{ importResult?.failureCount ?? 0 }} 条
          </template>
        </el-alert>

        <!-- 失败明细 -->
        <div v-if="importResult?.failureDetails?.length" class="mt-10">
          <h4>失败明细：</h4>
          <el-table :data="importResult.failureDetails" max-height="300">
            <el-table-column prop="row" label="行号" width="60" />
            <el-table-column prop="reason" label="失败原因" />
          </el-table>
        </div>

        <div class="tac mt-10">
          <el-button @click="currentStep = 1">重新上传</el-button>
          <el-button
            type="primary"
            :loading="importing"
            :disabled="!importResult || importResult.failureCount === importResult.totalCount"
            @click="handleConfirmImport"
          >
            确认导入{{ importResult?.successCount ? importResult.successCount + ' 条' : '' }}
          </el-button>
        </div>
      </div>
    </div>
  </Dialog>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { ElMessage } from 'element-plus'
import Dialog from '@/components/Dialog/index.vue'

interface ImportResult {
  totalCount: number
  successCount: number
  failureCount: number
  failureDetails: Array<{ row: number; reason: string }>
}

const props = defineProps<{
  visible: boolean
  title?: string
  /** 下载模板的API函数 */
  templateApi?: () => Promise<any>
  /** 预览导入的API函数 */
  previewApi?: (file: File) => Promise<ImportResult>
  /** 确认导入的API函数 */
  confirmApi?: () => Promise<any>
}>()

const emit = defineEmits<{
  (e: 'close'): void
  (e: 'update:visible', val: boolean): void
  (e: 'success'): void
}>()

const currentStep = ref(0)
const uploadFile = ref<any>(null)
const uploading = ref(false)
const importing = ref(false)
const importResult = ref<ImportResult | null>(null)

// 弹窗关闭时重置
watch(
  () => props.visible,
  (val) => {
    if (!val) {
      currentStep.value = 0
      uploadFile.value = null
      importResult.value = null
    }
  }
)

/** 下载模板 */
const handleDownloadTemplate = async () => {
  if (props.templateApi) {
    try {
      await props.templateApi()
      ElMessage.success('模板下载成功')
    } catch {
      ElMessage.error('模板下载失败')
    }
  }
}

/** 文件选择 */
const handleFileChange = (file: any) => {
  uploadFile.value = file
}

/** 文件移除 */
const handleFileRemove = () => {
  uploadFile.value = null
}

/** 上传预览 */
const handleUpload = async () => {
  if (!uploadFile.value?.raw) {
    ElMessage.warning('请选择文件')
    return
  }
  if (props.previewApi) {
    uploading.value = true
    try {
      importResult.value = await props.previewApi(uploadFile.value.raw)
      currentStep.value = 2
    } catch {
      ElMessage.error('文件解析失败')
    } finally {
      uploading.value = false
    }
  }
}

/** 确认导入 */
const handleConfirmImport = async () => {
  if (props.confirmApi) {
    importing.value = true
    try {
      await props.confirmApi()
      ElMessage.success('导入成功')
      emit('success')
      emit('close')
    } catch {
      ElMessage.error('导入失败')
    } finally {
      importing.value = false
    }
  }
}
</script>

<style scoped>
.import-dialog {
  min-height: 300px;
}
.step-content {
  padding: 20px 0;
}
.tac {
  text-align: center;
}
.mb-10 {
  margin-bottom: 10px;
}
.mb-20 {
  margin-bottom: 20px;
}
.mt-10 {
  margin-top: 10px;
}
</style>
