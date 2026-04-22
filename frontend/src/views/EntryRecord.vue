<template>
  <div class="entry-page">
    <el-card class="form-card">
      <template #header>
        <div class="card-header">
          <span>入栏详情信息</span>
        </div>
      </template>
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
        style="max-width: 500px"
      >
        <el-form-item label="入栏鸡苗总数">
          <el-input-number
            v-model="formData.totalChicks"
            :min="1"
            :max="99999"
            placeholder="请输入鸡苗总数"
            style="width: 100%"
            @change="calculateTotalPrice"
          />
          <span class="unit">只</span>
        </el-form-item>
        <el-form-item label="入栏日期" prop="entryDate">
          <el-date-picker
            v-model="formData.entryDate"
            type="date"
            placeholder="选择入栏日期"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="养殖天数">
          <el-input v-model="computedDays" disabled placeholder="系统自动计算" />
          <span class="unit">天</span>
        </el-form-item>
        <el-form-item label="品种" prop="breed">
          <el-input v-model="formData.breed" placeholder="请输入鸡苗品种" />
        </el-form-item>
        <el-form-item label="鸡苗价格">
          <el-input-number
            v-model="formData.pricePerChick"
            :min="0"
            :precision="2"
            :step="0.1"
            placeholder="请输入单价"
            style="width: 100%"
            @change="calculateTotalPrice"
          />
          <span class="unit">元/只</span>
        </el-form-item>
        <el-form-item label="鸡苗总价">
          <el-input v-model="computedTotalPrice" disabled placeholder="系统自动计算" />
          <span class="unit">元</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">
            保存信息
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { getEntryRecord, saveEntryRecord } from '../api/entry'

const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  totalChicks: null,
  entryDate: '',
  breed: '',
  pricePerChick: null
})

const existingData = ref(null)

const computedDays = computed(() => {
  if (!formData.entryDate) return ''
  const entryDate = new Date(formData.entryDate)
  const today = new Date()
  const diffTime = today - entryDate
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))
  return diffDays > 0 ? diffDays : 0
})

const computedTotalPrice = computed(() => {
  if (formData.totalChicks && formData.pricePerChick != null) {
    return (formData.totalChicks * formData.pricePerChick).toFixed(2)
  }
  return existingData.value?.totalPrice || ''
})

const rules = {
  entryDate: [{ required: true, message: '请选择入栏日期', trigger: 'change' }],
  breed: [{ required: true, message: '请输入品种', trigger: 'blur' }]
}

const calculateTotalPrice = () => {
  // computed property handles this
}

const handleSubmit = async () => {
  if (!formData.totalChicks) {
    ElMessage.warning('请输入入栏鸡苗总数')
    return
  }
  if (formData.pricePerChick == null) {
    ElMessage.warning('请输入鸡苗价格')
    return
  }
  
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await saveEntryRecord({
      totalChicks: formData.totalChicks,
      entryDate: formData.entryDate,
      breed: formData.breed,
      pricePerChick: formData.pricePerChick
    })
    ElMessage.success('保存成功')
    loadData()
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadData = async () => {
  try {
    const res = await getEntryRecord()
    if (res.data) {
      existingData.value = res.data
      formData.totalChicks = res.data.totalChicks
      formData.entryDate = res.data.entryDate
      formData.breed = res.data.breed
      formData.pricePerChick = res.data.pricePerChick
    }
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.entry-page {
  padding: 20px;
}

.form-card {
  max-width: 700px;
  margin: 0 auto;
}

.card-header {
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
}

.unit {
  margin-left: 8px;
  color: #909399;
}
</style>
