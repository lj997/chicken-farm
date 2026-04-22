<template>
  <div class="ai-config-page">
    <el-row :gutter="20">
      <el-col :span="12" :offset="6">
        <el-card class="config-card">
          <template #header>
            <div class="card-header">
              <span>大模型配置</span>
              <el-tag type="info" size="small">DeepSeek</el-tag>
            </div>
          </template>
          <el-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            label-width="120px"
          >
            <el-form-item label="API Key" prop="apiKey">
              <el-input
                v-model="formData.apiKey"
                type="password"
                placeholder="请输入 API Key"
                show-password
              />
            </el-form-item>
            <el-form-item label="Base URL" prop="baseUrl">
              <el-input
                v-model="formData.baseUrl"
                placeholder="请输入 Base URL，例如：https://api.deepseek.com"
              />
              <div class="form-tip">DeepSeek 官方地址：https://api.deepseek.com</div>
            </el-form-item>
            <el-form-item label="模型版本" prop="modelVersion">
              <el-select v-model="formData.modelVersion" placeholder="请选择模型版本" style="width: 100%">
                <el-option label="deepseek-chat" value="deepseek-chat" />
                <el-option label="deepseek-coder" value="deepseek-coder" />
              </el-select>
              <div class="form-tip">推荐使用 deepseek-chat 模型</div>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSave" :loading="loading">
                保存配置
              </el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="info-card" style="margin-top: 20px">
          <template #header>
            <div class="card-header">
              <span>配置说明</span>
            </div>
          </template>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="API Key">
              从 DeepSeek 官方网站获取的 API 密钥，用于身份验证
            </el-descriptions-item>
            <el-descriptions-item label="Base URL">
              DeepSeek API 的基础地址，默认使用官方地址
            </el-descriptions-item>
            <el-descriptions-item label="模型版本">
              选择要使用的模型，推荐使用 deepseek-chat
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getAiConfig, saveAiConfig } from '../api/aiConfig'

const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  apiKey: '',
  baseUrl: 'https://api.deepseek.com',
  modelVersion: 'deepseek-chat'
})

const rules = {
  apiKey: [
    { required: true, message: '请输入 API Key', trigger: 'blur' }
  ],
  baseUrl: [
    { required: true, message: '请输入 Base URL', trigger: 'blur' }
  ],
  modelVersion: [
    { required: true, message: '请选择模型版本', trigger: 'change' }
  ]
}

const loadConfig = async () => {
  try {
    const res = await getAiConfig()
    if (res.data) {
      formData.apiKey = res.data.apiKey || ''
      formData.baseUrl = res.data.baseUrl || 'https://api.deepseek.com'
      formData.modelVersion = res.data.modelVersion || 'deepseek-chat'
    }
  } catch (error) {
    console.error('加载配置失败:', error)
  }
}

const handleSave = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await saveAiConfig(formData)
    ElMessage.success('配置保存成功')
  } catch (error) {
    console.error(error)
    ElMessage.error('保存失败，请重试')
  } finally {
    loading.value = false
  }
}

const handleReset = () => {
  formData.apiKey = ''
  formData.baseUrl = 'https://api.deepseek.com'
  formData.modelVersion = 'deepseek-chat'
}

onMounted(() => {
  loadConfig()
})
</script>

<style scoped>
.ai-config-page {
  padding: 20px;
}

.config-card, .info-card {
  min-height: 300px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
}

.form-tip {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
</style>
