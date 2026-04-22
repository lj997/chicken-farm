<template>
  <div>
    <router-view v-if="isLoginPage"></router-view>
    <el-container style="height: 100vh" v-else>
      <el-aside width="200px" style="background-color: #2c3e50">
        <div class="logo">
          <h3>养鸡场管理平台</h3>
        </div>
        <el-menu
          :default-active="activeMenu"
          router
          background-color="#2c3e50"
          text-color="#ecf0f1"
          active-text-color="#3498db"
        >
          <el-menu-item index="/entry">
            <el-icon><Document /></el-icon>
            <span>入栏详情</span>
          </el-menu-item>
          <el-menu-item index="/breeding">
            <el-icon><TrendCharts /></el-icon>
            <span>养殖现状</span>
          </el-menu-item>
          <el-menu-item index="/cost">
            <el-icon><Wallet /></el-icon>
            <span>养殖成本开销</span>
          </el-menu-item>
          <el-menu-item index="/profit">
            <el-icon><Money /></el-icon>
            <span>预期盈利</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header style="background-color: #fff; border-bottom: 1px solid #e4e7ed; padding: 0 20px; display: flex; justify-content: space-between; align-items: center">
          <h4 style="margin: 0; line-height: 60px; color: #2c3e50">{{ currentTitle }}</h4>
          <div class="header-right">
            <span style="margin-right: 16px; color: #606266">欢迎，{{ username }}</span>
            <el-button type="text" @click="handleLogout">
              <el-icon><SwitchButton /></el-icon>
              退出登录
            </el-button>
          </div>
        </el-header>
        <el-main style="background-color: #f5f7fa">
          <router-view></router-view>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Document, TrendCharts, Wallet, Money, SwitchButton } from '@element-plus/icons-vue'
import { logout } from './api/auth'

const route = useRoute()
const router = useRouter()

const username = computed(() => sessionStorage.getItem('username') || '')

const isLoginPage = computed(() => route.path === '/login')

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const titles = {
    '/entry': '入栏详情',
    '/breeding': '养殖现状',
    '/cost': '养殖成本开销',
    '/profit': '预期盈利'
  }
  return titles[route.path] || '养鸡场管理平台'
})

const handleLogout = () => {
  ElMessageBox.confirm('确定要退出登录吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    logout().then(res => {
      ElMessage.success('已退出登录')
    }).catch(err => {
      console.error('Logout error:', err)
    }).finally(() => {
      sessionStorage.removeItem('token')
      sessionStorage.removeItem('username')
      router.push('/login')
    })
  }).catch(() => {
  })
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

html, body {
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', Arial, sans-serif;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo h3 {
  color: #fff;
  font-size: 16px;
  font-weight: 500;
}

.el-menu-item {
  height: 50px;
  line-height: 50px;
}
</style>
