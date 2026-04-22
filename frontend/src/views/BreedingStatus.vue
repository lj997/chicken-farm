<template>
  <div class="breeding-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="form-card">
          <template #header>
            <div class="card-header">
              <span>每日养殖记录</span>
            </div>
          </template>
          <el-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            label-width="100px"
          >
            <el-form-item label="记录日期" prop="recordDate">
              <el-date-picker
                v-model="formData.recordDate"
                type="date"
                placeholder="选择日期"
                value-format="YYYY-MM-DD"
                style="width: 100%"
              />
            </el-form-item>
            <el-form-item label="平均重量" prop="avgWeight">
              <el-input-number
                v-model="formData.avgWeight"
                :min="0"
                :precision="2"
                :step="0.1"
                placeholder="请输入平均重量"
                style="width: 100%"
              />
              <span class="unit">斤</span>
            </el-form-item>
            <el-form-item label="死亡个数" prop="deadCount">
              <el-input-number
                v-model="formData.deadCount"
                :min="0"
                :max="99999"
                placeholder="请输入死亡个数"
                style="width: 100%"
              />
              <span class="unit">只</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleSubmit" :loading="loading">
                保存记录
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="table-card">
          <template #header>
            <div class="card-header">
              <span>养殖记录列表</span>
            </div>
          </template>
          <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="recordDate" label="记录日期" width="150" />
            <el-table-column prop="avgWeight" label="平均重量(斤)" width="150">
              <template #default="scope">
                {{ scope.row.avgWeight ? scope.row.avgWeight.toFixed(2) : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="deadCount" label="当日死亡(只)" width="150" />
            <el-table-column prop="survivalCount" label="存活个数(只)" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getBreedingList, saveBreeding } from '../api/breeding'

const formRef = ref(null)
const loading = ref(false)
const tableData = ref([])

const formData = reactive({
  recordDate: '',
  avgWeight: null,
  deadCount: 0
})

const rules = {
  recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
  avgWeight: [
    { required: true, message: '请输入平均重量', trigger: 'blur' }
  ],
  deadCount: [{ required: true, message: '请输入死亡个数', trigger: 'blur' }]
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await saveBreeding({
      recordDate: formData.recordDate,
      avgWeight: formData.avgWeight,
      deadCount: formData.deadCount
    })
    ElMessage.success('保存成功')
    loadData()
    formData.recordDate = ''
    formData.avgWeight = null
    formData.deadCount = 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadData = async () => {
  try {
    const res = await getBreedingList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => {
  loadData()
})
</script>

<style scoped>
.breeding-page {
  padding: 20px;
}

.form-card, .table-card {
  height: 100%;
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
