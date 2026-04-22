<template>
  <el-container style="height: 100vh">
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
        <el-menu-item index="/config">
          <el-icon><Setting /></el-icon>
          <span>大模型配置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background-color: #fff; border-bottom: 1px solid #e4e7ed; padding: 0 20px">
        <h4 style="margin: 0; line-height: 60px; color: #2c3e50">{{ currentTitle }}</h4>
      </el-header>
      <el-main style="background-color: #f5f7fa">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { Document, TrendCharts, Wallet, Money, Setting } from '@element-plus/icons-vue'

const route = useRoute()

const activeMenu = computed(() => route.path)

const currentTitle = computed(() => {
  const titles = {
    '/entry': '入栏详情',
    '/breeding': '养殖现状',
    '/cost': '养殖成本开销',
    '/profit': '预期盈利',
    '/config': '大模型配置'
  }
  return titles[route.path] || '养鸡场管理平台'
})
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
