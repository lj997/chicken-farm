<template>
  <div class="cost-page">
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="form-card">
          <template #header>
            <div class="card-header">
              <span>记录成本开销</span>
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
            <el-form-item label="类别" prop="category">
              <el-select v-model="formData.category" placeholder="请选择类别" style="width: 100%">
                <el-option label="药品" value="medicine" />
                <el-option label="饲料" value="feed" />
                <el-option label="其他" value="other" />
              </el-select>
            </el-form-item>
            <el-form-item label="金额" prop="amount">
              <el-input-number
                v-model="formData.amount"
                :min="0"
                :precision="2"
                :step="1"
                placeholder="请输入金额"
                style="width: 100%"
              />
              <span class="unit">元</span>
            </el-form-item>
            <el-form-item label="备注">
              <el-input
                v-model="formData.remark"
                type="textarea"
                :rows="2"
                placeholder="请输入备注（可选）"
              />
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
        <el-card class="chart-card">
          <template #header>
            <div class="card-header">
              <span>成本开销统计</span>
              <div class="chart-controls">
                <el-radio-group v-model="viewType" @change="loadChartData">
                  <el-radio-button label="daily">按天查看</el-radio-button>
                  <el-radio-button label="monthly">按月查看</el-radio-button>
                  <el-radio-button label="toDate">至今日</el-radio-button>
                </el-radio-group>
                <el-date-picker
                  v-if="viewType === 'daily'"
                  v-model="selectDate"
                  type="date"
                  placeholder="选择日期"
                  value-format="YYYY-MM-DD"
                  style="margin-left: 10px; width: 150px"
                  @change="loadChartData"
                />
                <el-date-picker
                  v-if="viewType === 'monthly'"
                  v-model="selectMonth"
                  type="month"
                  placeholder="选择月份"
                  value-format="YYYY-MM"
                  style="margin-left: 10px; width: 150px"
                  @change="loadChartData"
                />
              </div>
            </div>
          </template>
          <div class="chart-container">
            <div ref="chartRef" class="pie-chart"></div>
            <div class="chart-legend">
              <div class="legend-item" v-for="item in chartData" :key="item.category">
                <span class="legend-color" :style="{ backgroundColor: item.color }"></span>
                <span class="legend-name">{{ item.categoryName }}</span>
                <span class="legend-value">{{ item.total ? item.total.toFixed(2) : '0.00' }}元</span>
              </div>
              <div class="legend-total">
                <span class="legend-name">合计</span>
                <span class="legend-value total-value">{{ totalAmount.toFixed(2) }}元</span>
              </div>
            </div>
          </div>
        </el-card>

        <el-card class="table-card" style="margin-top: 20px">
          <template #header>
            <div class="card-header">
              <span>历史记录列表</span>
            </div>
          </template>
          <el-table :data="tableData" stripe style="width: 100%">
            <el-table-column prop="recordDate" label="记录日期" width="150" />
            <el-table-column prop="category" label="类别" width="120">
              <template #default="scope">
                <el-tag :type="getTagType(scope.row.category)">{{ getCategoryName(scope.row.category) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="金额(元)" width="150">
              <template #default="scope">
                {{ scope.row.amount ? scope.row.amount.toFixed(2) : '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="remark" label="备注" />
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, nextTick, watch } from 'vue'
import { ElMessage } from 'element-plus'
import * as echarts from 'echarts'
import { getCostList, saveCost, getDailySummary, getMonthlySummary, getSummaryToDate } from '../api/cost'

const formRef = ref(null)
const chartRef = ref(null)
const loading = ref(false)
const tableData = ref([])
const viewType = ref('toDate')
const selectDate = ref('')
const selectMonth = ref('')
const chartData = ref([])
let chartInstance = null

const formData = reactive({
  recordDate: '',
  category: '',
  amount: null,
  remark: ''
})

const rules = {
  recordDate: [{ required: true, message: '请选择记录日期', trigger: 'change' }],
  category: [{ required: true, message: '请选择类别', trigger: 'change' }],
  amount: [{ required: true, message: '请输入金额', trigger: 'blur' }]
}

const totalAmount = computed(() => {
  return chartData.value.reduce((sum, item) => sum + (item.total || 0), 0)
})

const getCategoryName = (category) => {
  const names = {
    medicine: '药品',
    feed: '饲料',
    other: '其他'
  }
  return names[category] || category
}

const getTagType = (category) => {
  const types = {
    medicine: 'danger',
    feed: 'success',
    other: 'info'
  }
  return types[category] || ''
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await saveCost({
      recordDate: formData.recordDate,
      category: formData.category,
      amount: formData.amount,
      remark: formData.remark
    })
    ElMessage.success('保存成功')
    loadData()
    loadChartData()
    formData.recordDate = ''
    formData.category = ''
    formData.amount = null
    formData.remark = ''
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const loadData = async () => {
  try {
    const res = await getCostList()
    tableData.value = res.data || []
  } catch (error) {
    console.error(error)
  }
}

const loadChartData = async () => {
  try {
    let res
    const today = new Date().toISOString().split('T')[0]
    
    if (viewType.value === 'daily') {
      const date = selectDate.value || today
      res = await getDailySummary(date)
    } else if (viewType.value === 'monthly') {
      if (selectMonth.value) {
        const [year, month] = selectMonth.value.split('-').map(Number)
        res = await getMonthlySummary(year, month)
      } else {
        const now = new Date()
        res = await getMonthlySummary(now.getFullYear(), now.getMonth() + 1)
      }
    } else {
      res = await getSummaryToDate(today)
    }
    
    const colors = ['#e74c3c', '#27ae60', '#3498db']
    chartData.value = (res.data || []).map((item, index) => ({
      ...item,
      color: colors[index]
    }))
    
    nextTick(() => {
      initChart()
    })
  } catch (error) {
    console.error(error)
  }
}

const initChart = () => {
  if (!chartRef.value) return
  
  if (!chartInstance) {
    chartInstance = echarts.init(chartRef.value)
  }
  
  const pieData = chartData.value
    .filter(item => item.total && item.total > 0)
    .map(item => ({
      name: item.categoryName,
      value: item.total,
      itemStyle: { color: item.color }
    }))
  
  const option = {
    tooltip: {
      trigger: 'item',
      formatter: '{b}: {c}元 ({d}%)'
    },
    series: [
      {
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 10,
          borderColor: '#fff',
          borderWidth: 2
        },
        label: {
          show: false
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 16,
            fontWeight: 'bold'
          }
        },
        labelLine: {
          show: false
        },
        data: pieData.length > 0 ? pieData : [
          { name: '暂无数据', value: 1, itemStyle: { color: '#e0e0e0' } }
        ]
      }
    ]
  }
  
  chartInstance.setOption(option, true)
}

const handleResize = () => {
  chartInstance?.resize()
}

onMounted(() => {
  const today = new Date().toISOString().split('T')[0]
  formData.recordDate = today
  selectDate.value = today
  
  loadData()
  loadChartData()
  
  window.addEventListener('resize', handleResize)
})

watch(
  () => viewType.value,
  () => {
    loadChartData()
  }
)
</script>

<style scoped>
.cost-page {
  padding: 20px;
}

.form-card, .chart-card, .table-card {
  height: auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: 500;
  color: #2c3e50;
}

.chart-controls {
  display: flex;
  align-items: center;
}

.unit {
  margin-left: 8px;
  color: #909399;
}

.chart-container {
  display: flex;
  align-items: center;
  min-height: 350px;
}

.pie-chart {
  width: 50%;
  height: 300px;
}

.chart-legend {
  width: 50%;
  padding-left: 30px;
}

.legend-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.legend-color {
  width: 16px;
  height: 16px;
  border-radius: 4px;
  margin-right: 10px;
}

.legend-name {
  flex: 1;
  color: #303133;
  font-size: 14px;
}

.legend-value {
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.legend-total {
  display: flex;
  align-items: center;
  margin-top: 20px;
  padding: 15px 10px;
  background-color: #ecf5ff;
  border-radius: 8px;
  border: 1px solid #d9ecff;
}

.total-value {
  color: #409eff;
  font-size: 18px;
  font-weight: bold;
}
</style>
