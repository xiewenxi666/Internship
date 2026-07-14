<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form ref="formRef" v-loading="formLoading" :model="formData" :rules="formRules" label-width="auto">
      <el-row>
        <el-col :span="12">
          <el-form-item label="活动标题" prop="title">
            <el-input v-model="formData.title" placeholder="请输入活动标题" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动类型" prop="type">
            <el-select v-model="formData.type" placeholder="请选择活动类型" class="w-1/1">
              <el-option label="促销活动" :value="1" />
              <el-option label="品牌活动" :value="2" />
              <el-option label="会议营销" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
              v-model="formData.startTime"
              type="datetime"
              value-format="x"
              placeholder="选择开始时间"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
              v-model="formData.endTime"
              type="datetime"
              value-format="x"
              placeholder="选择结束时间"
              class="!w-1/1"
            />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="预计成本" prop="estimatedCost">
            <el-input-number v-model="formData.estimatedCost" :min="0" :precision="2" class="!w-1/1" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="预计收入" prop="estimatedRevenue">
            <el-input-number v-model="formData.estimatedRevenue" :min="0" :precision="2" class="!w-1/1" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="12">
          <el-form-item label="负责人员" prop="ownerUserId">
            <el-select v-model="formData.ownerUserId" class="w-1/1">
              <el-option v-for="item in userOptions" :key="item.id" :label="item.nickname" :value="item.id" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="活动状态" prop="status">
            <el-select v-model="formData.status" class="w-1/1">
              <el-option label="筹备" :value="1" />
              <el-option label="进行中" :value="2" />
              <el-option label="已结束" :value="3" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="活动地址" prop="address">
            <el-input v-model="formData.address" placeholder="请输入活动地址" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="24">
          <el-form-item label="活动详情">
            <el-input v-model="formData.description" type="textarea" :rows="4" placeholder="请输入活动详情" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    <template #footer>
      <el-button :disabled="formLoading" type="primary" @click="submitForm">确认</el-button>
      <el-button @click="dialogVisible = false">取消</el-button>
    </template>
  </Dialog>
</template>

<script lang="ts" setup>
import * as CampaignApi from '@/api/crm/campaign'
import * as UserApi from '@/api/system/user'

defineOptions({ name: 'CrmCampaignForm' })

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')
const formLoading = ref(false)
const formType = ref('')
const userOptions = ref<UserApi.UserVO[]>([])
const formRef = ref()
const formData = ref({
  id: undefined,
  title: undefined,
  type: undefined,
  startTime: undefined,
  endTime: undefined,
  estimatedCost: 0,
  estimatedRevenue: 0,
  ownerUserId: undefined,
  participants: undefined,
  address: undefined,
  description: undefined,
  status: 1
})

const formRules = reactive({
  title: [{ required: true, message: '活动标题不能为空', trigger: 'blur' }],
  type: [{ required: true, message: '请选择活动类型', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  estimatedCost: [{ required: true, message: '请输入预计成本', trigger: 'blur' }],
  estimatedRevenue: [{ required: true, message: '请输入预计收入', trigger: 'blur' }],
  ownerUserId: [{ required: true, message: '请选择负责人员', trigger: 'change' }],
  status: [{ required: true, message: '请选择活动状态', trigger: 'change' }]
})

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  formType.value = type
  dialogTitle.value = type === 'create' ? '新增活动' : '编辑活动'
  userOptions.value = await UserApi.getSimpleUserList()
  if (id) {
    formLoading.value = true
    try {
      const data = await CampaignApi.getCampaign(id)
      formData.value = { ...data }
    } finally {
      formLoading.value = false
    }
  } else {
    resetForm()
  }
}
defineExpose({ open })

const submitForm = async () => {
  const valid = await formRef.value?.validate()
  if (!valid) return
  formLoading.value = true
  try {
    if (formType.value === 'create') {
      await CampaignApi.createCampaign(formData.value)
      message.success('创建成功')
    } else {
      await CampaignApi.updateCampaign(formData.value)
      message.success('修改成功')
    }
    dialogVisible.value = false
    emits('success')
  } finally {
    formLoading.value = false
  }
}

const emits = defineEmits(['success'])

const resetForm = () => {
  formData.value = {
    id: undefined, title: undefined, type: undefined,
    startTime: undefined, endTime: undefined,
    estimatedCost: 0, estimatedRevenue: 0,
    ownerUserId: undefined, participants: undefined,
    address: undefined, description: undefined, status: 1
  }
}
</script>
