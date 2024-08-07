<script setup>
import { User, Lock } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { userLoginService } from '@/api/user/user.js'
import { useUserStore } from '@/stores/modules/user'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

// 登录注册 整个的form数据
const formModel = ref({
  userName: '',
  password: ''
})
// 整个表单的rules
const rules = {
  userName: [
    {
      required: true,
      message: '请输入用户名',
      trigger: 'blur'
    }
  ],
  password: [
    {
      required: true,
      message: '请输入密码',
      trigger: 'blur'
    }
  ]
}
// 登录
const formRef = ref(null)
const userStore = useUserStore()
const router = useRouter()
const login = async () => {
  await formRef.value.validate() //登录前的预校验(获取到组件调用方法)
  const res = await userLoginService(formModel.value)
  userStore.setToken(res.data.data)
  ElMessage.success('登录成功')
  router.push('/')
}
</script>

<template>
  <el-row class="login-page">
    <el-col :span="6" :offset="3" class="form">
      <el-col :span="8">
        <img src="@/assets/logo.png" alt="Logo" class="logo-img" />
      </el-col>
      <el-col class="login">
        <h1>管理员登录</h1>
      </el-col>
      <el-form
        ref="formRef"
        size="large"
        autocomplete="off"
        :model="formModel"
        :rules="rules"
      >
        <el-form-item prop="userName">
          <el-input
            v-model="formModel.userName"
            :prefix-icon="User"
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="formModel.password"
            name="password"
            :prefix-icon="Lock"
            type="password"
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex"></div>
        </el-form-item>
        <el-form-item>
          <el-button class="button" auto-insert-space @click="login"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
    </el-col>
  </el-row>
</template>

<style lang="scss" scoped>
.login-page {
  height: 100vh;
  background-color: #181d2b;

  .form {
    top: 0%;
    left: 0%;
    right: 0%;
    bottom: 0%;
    margin: auto;
    position: relative;

    .logo-img {
      position: absolute;
      top: -110%;
      left: 0%;
      right: 0%;
      bottom: 0%;
      margin: auto;
      width: 160px;
      height: auto;
    }

    ::v-deep(.el-input__wrapper) {
      width: 480px;
      height: 70px;
      border-radius: 722px 722px 722px 722px;
      background-color: #181d2b;
    }

    ::v-deep(.el-form-item__error) {
      color: var(--el-color-danger);
      font-size: 12px;
      line-height: 1;
      padding-top: 2px;
      position: absolute;
      top: 100%;
      left: 30%;
    }
  }

  .login {
    font-family: FZLanTingHeiS-H-GB, FZLanTingHeiS-H-GB;
    font-weight: 400;
    font-size: 20px;
    color: #ffffff;
    text-align: center;
    font-style: normal;
    text-transform: none;
  }

  .button {
    width: 480px;
    height: 70px;
    border-radius: 722px 722px 722px 722px;
    border: 1px solid #697397;
    background-image: linear-gradient(
      to right,
      #13bbc9,
      #6468ed,
      #d16cc8,
      #ff9269
    );

    ::v-deep(.el-button__text--expand) {
      width: 60px;
      height: 43px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 500;
      font-size: 30px;
      color: #ffffff;
      line-height: 43px;
      text-align: center;
      font-style: normal;
      text-transform: none;
    }
  }
}
</style>
