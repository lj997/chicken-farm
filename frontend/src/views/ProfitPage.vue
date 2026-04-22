<template>
  <div class="profit-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="form-card">
          <template #header>
            <div class="card-header">
              <span>市场价格设置</span>
            </div>
          </template>
          <el-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            label-width="140px"
          >
            <el-form-item label="当前市场价格" prop="marketPrice">
              <el-input-number
                v-model="formData.marketPrice"
                :min="0"
                :precision="2"
                :step="0.1"
                placeholder="请输入市场价格"
                style="width: 100%"
              />
              <span class="unit">元/斤</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="handleCalculate" :loading="loading">
                计算预估盈利
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-card class="result-card">
          <template #header>
            <div class="card-header">
              <span>预估盈利计算结果</span>
            </div>
          </template>
          <el-empty v-if="!resultData" description="请输入市场价格并点击计算" />
          <template v-else>
            <el-row :gutter="20">
              <el-col :span="8">
                <div class="info-card">
                  <div class="info-label">鸡平均重量</div>
                  <div class="info-value">
                    {{ resultData.avgWeight ? resultData.avgWeight.toFixed(2) : '0.00' }}
                    <span class="info-unit">斤</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-card">
                  <div class="info-label">当前存活数量</div>
                  <div class="info-value">
                    {{ resultData.survivalCount || 0 }}
                    <span class="info-unit">只</span>
                  </div>
                </div>
              </el-col>
              <el-col :span="8">
                <div class="info-card">
                  <div class="info-label">市场售价</div>
                  <div class="info-value">
                    {{ resultData.marketPrice ? resultData.marketPrice.toFixed(2) : '0.00' }}
                    <span class="info-unit">元/斤</span>
                  </div>
                </div>
              </el-col>
            </el-row>

            <el-divider content-position="left">收益计算</el-divider>

            <el-row :gutter="20">
              <el-col :span="12">
                <div class="result-card-item income">
                  <div class="result-label">预估售出总价</div>
                  <div class="result-value">
                    ¥ {{ resultData.estimatedTotalPrice ? resultData.estimatedTotalPrice.toFixed(2) : '0.00' }}
                  </div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="result-card-item cost">
                  <div class="result-label">养殖总成本</div>
                  <div class="result-value">
                    ¥ {{ resultData.totalCost ? resultData.totalCost.toFixed(2) : '0.00' }}
                  </div>
                </div>
              </el-col>
            </el-row>

            <el-divider content-position="left">预估利润</el-divider>

            <div class="profit-summary" :class="{ 'profit-positive': isProfit, 'profit-negative': !isProfit }">
              <div class="profit-label">预估利润</div>
              <div class="profit-value">
                <span class="profit-sign">{{ profitSign }}</span>
                ¥ {{ profitAmount }}
              </div>
            </div>
          </template>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { calculateProfit } from '../api/profit'

const formRef = ref(null)
const loading = ref(false)
const resultData = ref(null)

const formData = reactive({
  marketPrice: null
})

const rules = {
  marketPrice: [
    { required: true, message: '请输入市场价格', trigger: 'blur' }
  ]
}

const isProfit = computed(() => {
  if (!resultData.value) return true
  return resultData.value.estimatedProfit >= 0
})

const profitSign = computed(() => {
  return isProfit.value ? '' : '-'
})

const profitAmount = computed(() => {
  if (!resultData.value) return '0.00'
  return Math.abs(resultData.value.estimatedProfit).toFixed(2)
})

const handleCalculate = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    const res = await calculateProfit({
      marketPrice: formData.marketPrice
    })
    resultData.value = res.data
    ElMessage.success('计算完成')
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.profit-page {
  padding: 20px;
}

.form-card, .result-card {
  min-height: 500px;
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

.info-card {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
}

.info-label {
  color: #909399;
  font-size: 14px;
  margin-bottom: 10px;
}

.info-value {
  color: #303133;
  font-size: 28px;
  font-weight: bold;
}

.info-unit {
  font-size: 14px;
  font-weight: normal;
  color: #909399;
  margin-left: 5px;
}

.result-card-item {
  border-radius: 8px;
  padding: 25px;
  text-align: center;
}

.result-card-item.income {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.result-card-item.cost {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.result-label {
  color: rgba(255, 255, 255, 0.8);
  font-size: 14px;
  margin-bottom: 10px;
}

.result-value {
  color: #fff;
  font-size: 32px;
  font-weight: bold;
}

.profit-summary {
  border-radius: 12px;
  padding: 30px;
  text-align: center;
  transition: all 0.3s ease;
}

.profit-summary.profit-positive {
  background: linear-gradient(135deg, #11998e 0%, #38ef7d 100%);
}

.profit-summary.profit-negative {
  background: linear-gradient(135deg, #eb3349 0%, #f45c43 100%);
}

.profit-label {
  color: rgba(255, 255, 255, 0.9);
  font-size: 16px;
  margin-bottom: 15px;
}

.profit-value {
  color: #fff;
  font-size: 42px;
  font-weight: bold;
}

.profit-sign {
  font-size: 32px;
  margin-right: 5px;
}
</style>
