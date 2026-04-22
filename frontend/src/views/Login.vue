<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="login-title">养鸡场管理平台</h2>
      
      <div class="tab-container">
        <div 
          class="tab-item" 
          :class="{ active: isLoginMode }" 
          @click="switchToLogin"
        >
          登录
        </div>
        <div 
          class="tab-item" 
          :class="{ active: !isLoginMode }" 
          @click="switchToRegister"
        >
          注册
        </div>
      </div>
      
      <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" class="login-form" v-if="isLoginMode">
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            placeholder="请输入用户名"
            prefix-icon="User"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            prefix-icon="Lock"
            @keyup.enter="handleLogin"
          ></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha-row">
            <el-input
              v-model="loginForm.captcha"
              placeholder="请输入验证码"
              prefix-icon="Key"
              style="width: 200px"
              @keyup.enter="handleLogin"
            ></el-input>
            <img 
              :src="captchaImage" 
              class="captcha-img" 
              @click="refreshCaptcha"
              title="点击刷新验证码"
            />
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-btn" @click="handleLogin">
            登 录
          </el-button>
        </el-form-item>
      </el-form>
      
      <el-form :model="registerForm" :rules="registerRules" ref="registerFormRef" class="login-form" v-else>
        <el-form-item prop="username">
          <el-input
            v-model="registerForm.username"
            placeholder="请输入用户名（至少3个字符）"
            prefix-icon="User"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="registerForm.password"
            type="password"
            placeholder="请输入密码（至少6个字符）"
            prefix-icon="Lock"
          ></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            placeholder="请再次输入密码"
            prefix-icon="Lock"
            @keyup.enter="handleRegister"
          ></el-input>
        </el-form-item>
        <el-form-item prop="captcha">
          <div class="captcha-row">
            <el-input
              v-model="registerForm.captcha"
              placeholder="请输入验证码"
              prefix-icon="Key"
              style="width: 200px"
              @keyup.enter="handleRegister"
            ></el-input>
            <img 
              :src="captchaImage" 
              class="captcha-img" 
              @click="refreshCaptcha"
              title="点击刷新验证码"
            />
          </div>
        </el-form-item>
        <el-form-item>
          <el-button type="success" :loading="loading" class="login-btn" @click="handleRegister">
            注 册
          </el-button>
        </el-form-item>
      </el-form>
      
      <div class="login-tip">
        <p v-if="isLoginMode">提示：默认用户名 admin，密码 123456</p>
        <p v-else>提示：注册成功后将自动登录</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login, register } from '../api/auth'

const router = useRouter()
const route = useRoute()
const loginFormRef = ref(null)
const registerFormRef = ref(null)
const loading = ref(false)
const captchaImage = ref('')
const isLoginMode = ref(true)

const loginForm = reactive({
  username: '',
  password: '',
  captcha: '',
  captchaKey: ''
})

const registerForm = reactive({
  username: '',
  password: '',
  confirmPassword: '',
  captcha: '',
  captchaKey: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== registerForm.password) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const loginRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

const registerRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, message: '用户名至少3个字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码至少6个字符', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入密码', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' }
  ]
}

const refreshCaptcha = () => {
  const timestamp = Date.now()
  captchaImage.value = `/api/auth/captcha?t=${timestamp}`
  loginForm.captchaKey = timestamp.toString()
  registerForm.captchaKey = timestamp.toString()
}

const switchToLogin = () => {
  isLoginMode.value = true
  refreshCaptcha()
}

const switchToRegister = () => {
  isLoginMode.value = false
  refreshCaptcha()
}

const handleLoginSuccess = (res) => {
  ElMessage.success(isLoginMode.value ? '登录成功' : '注册成功')
  if (res.data && res.data.token) {
    sessionStorage.setItem('token', res.data.token)
  }
  const redirect = route.query.redirect || '/entry'
  router.push(redirect)
}

const handleLogin = () => {
  loginFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      login({
        username: loginForm.username,
        password: loginForm.password,
        captcha: loginForm.captcha,
        captchaKey: loginForm.captchaKey
      }).then(res => {
        handleLoginSuccess(res)
      }).catch(err => {
        refreshCaptcha()
        loading.value = false
      })
    }
  })
}

const handleRegister = () => {
  registerFormRef.value.validate((valid) => {
    if (valid) {
      loading.value = true
      register({
        username: registerForm.username,
        password: registerForm.password,
        confirmPassword: registerForm.confirmPassword,
        captcha: registerForm.captcha,
        captchaKey: registerForm.captchaKey
      }).then(res => {
        handleLoginSuccess(res)
      }).catch(err => {
        refreshCaptcha()
        loading.value = false
      })
    }
  })
}

onMounted(() => {
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-title {
  text-align: center;
  margin-bottom: 20px;
  color: #2c3e50;
  font-size: 24px;
  font-weight: 500;
}

.tab-container {
  display: flex;
  margin-bottom: 30px;
  border-bottom: 1px solid #e4e7ed;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  cursor: pointer;
  color: #909399;
  font-size: 16px;
  position: relative;
  transition: color 0.3s;
}

.tab-item:hover {
  color: #409eff;
}

.tab-item.active {
  color: #409eff;
  font-weight: 500;
}

.tab-item.active::after {
  content: '';
  position: absolute;
  bottom: -1px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 2px;
  background: #409eff;
}

.login-form {
  width: 100%;
}

.captcha-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.captcha-img {
  height: 40px;
  cursor: pointer;
  border-radius: 4px;
  border: 1px solid #dcdfe6;
}

.login-btn {
  width: 100%;
}

.login-tip {
  text-align: center;
  margin-top: 20px;
  color: #909399;
  font-size: 12px;
}
</style>
