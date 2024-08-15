<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'
import {
  loginByVerifyCodeApi /* 验证码登录 */,
  sendVerifyCodeApi /* 发送邮箱验证码 */,
  loginByPassword /* 密码登录 */,
  registerApi /* 注册 */,
  updatePasswordApi /* 重置密码 */
} from '@/api/login2/loginApi.js'
import {
  listLabelApi,
  listArticleByLabelApi,
  recommendLabelApi
} from '@/api/ClassificationManage/ClassificationManageApi.js'
import _ from 'lodash'
import { useCounterStore } from '@/stores/modules/homeList'
import Vcode from 'vue3-puzzle-vcode'
const counterStore = useCounterStore()

const props = defineProps({
  // onSearch: {
  //   type: Function
  // },
  title: {
    type: String,
    default: ''
  }
  // total: {
  //   type: Number,
  //   default: 0
  // },
  // pageNum: {
  //   type: Number
  // },
  // pageSize: {
  //   type: Number
  // }
})
const emit = defineEmits(['update:title']) // 用于自定义事件
const localTitle = ref(props.title)
const handleSearch = () => {
  // 调用从父组件传入的 onSearch 方法，并传递当前的输入值作为参数
  // props.onSearch(localTitle.value)
  if (route.name) router.push('/') //先去首页再搜索
  emit('update:title', localTitle.value)
}
// 监听 localTitle 的变化，并可选地通过自定义事件通知父组件
watch(localTitle, (newValue) => {
  emit('update:title', newValue)
})

const router = useRouter()
const route = useRoute()
const loginTitle = ref(0)
// const email = ref('2067346311@qq.com') //邮箱
const email = ref('') //邮箱
const emailCode = ref() //邮箱验证码
const password = ref() //密码
const nickName = ref() //昵称
const verifyCode = ref() //验证码
const verifyPassword = ref() //验证密码
const token = ref(localStorage.getItem('token')) ////验证密码
const listLabel = ref({}) //标签列表
const dialogVisible = ref(false) //模态框控制
const codeStatus = ref('获取验证码') //验证码状态
const disabledCode = ref(true) //验证码状态

//登录方式切换
const loginChange = (v) => {
  loginTitle.value = v
  dialogVisible.value = true
}
//获取验证码
const clickCode = async () => {
  if (!email.value) {
    ElMessage({
      message: '请先完善邮箱',
      type: 'warning'
    })
    return
  } else {
    isShow.value = true
    dialogVisible.value = false
    //验证成功为1
    if (vcodeType.value == 1) {
      console.log('验证成功')
      const params = { email: email.value }
      const res = await sendVerifyCodeApi(params)
      if (res.data.code == 200) {
        dialogVisible.value = true
        ElMessage({
          message: res.data.msg,
          type: 'success'
        })
        //计时器
        var time = ref(59)
        disabledCode.value = true // 禁用按钮
        // 开启定时器
        var timer = setInterval(() => {
          // 判断剩余秒数
          if (time.value == 0) {
            // 清除定时器和复原按钮
            clearInterval(timer)
            disabledCode.value = true
          } else {
            codeStatus.value = `${time.value}秒后重发`
            time.value--
            disabledCode.value = false
            //如果模态框关闭 计时器停止
            if (loginTitle.value == 0 || dialogVisible.value == false) {
              clearInterval(timer)
              disabledCode.value = true
            }
          }
        }, 1000)
      } else {
        ElMessage({
          message: res.data.msg,
          type: 'error'
        })
      }
    }
  }
}

//验证码登录
const loginClick = async () => {
  localStorage.removeItem('token')
  const params = {
    email: email.value,
    verifyCode: emailCode.value
  }
  clickStyle() //点击特效
  const res = await loginByVerifyCodeApi(params)
  if (res.data.code == 200) {
    ElMessage({
      message: res.data.msg,
      type: 'success'
    })
    loginTitle.value = 0
    localStorage.setItem('token', res.data.data)
    changeLoad()
    gopersonalparticulars()
  }
}
// }
//密码登录
const passwordLoginClick = async () => {
  if (!email.value || !password.value) {
    ElMessage({
      message: '请先完善邮箱和密码信息',
      type: 'warning'
    })
    return
  } else {
    clickStyle()
    const params = {
      email: email.value,
      password: password.value
    }
    const res = await loginByPassword(params)
    if (res.data.code == 200) {
      ElMessage({
        message: res.data.msg,
        type: 'success'
      })
      loginTitle.value = 0
      localStorage.setItem('token', res.data.data)
      changeLoad()
      gopersonalparticulars()
    }
  }
}
//注册
const registerClick = async () => {
  if (
    !nickName.value ||
    !email.value ||
    !password.value ||
    !verifyCode.value ||
    !verifyPassword.value
  ) {
    ElMessage({
      message: '请先完善登录信息',
      type: 'warning'
    })
    return
  } else {
    clickStyle()
    const params = {
      nickName: nickName.value,
      email: email.value,
      verifyCode: verifyCode.value,
      password: password.value,
      verifyPassword: verifyPassword.value
    }
    const res = await registerApi(params)
    if (res.data.code == 200) {
      ElMessage({
        message: res.data.msg,
        type: 'success'
      })
      loginTitle.value = 0
      changeLoad()
    }
  }
}
//重置密码
const updatePasswordClick = async () => {
  if (
    !email.value ||
    !password.value ||
    !verifyCode.value ||
    !verifyPassword.value
  ) {
    ElMessage({
      message: '请先完善登录信息',
      type: 'warning'
    })
    return
  } else {
    clickStyle()
    const params = {
      email: email.value,
      verifyCode: verifyCode.value,
      password: password.value,
      verifyPassword: verifyPassword.value
    }
    const res = await updatePasswordApi(params)
    if (res.data.code == 200) {
      ElMessage({
        message: res.data.msg,
        type: 'success'
      })
      loginTitle.value = 0
      changeLoad()
    }
  }
}

//清空输入框
const changeLoad = () => {
  if (loginTitle.value == 0) {
    email.value = null
    emailCode.value = null
    password.value = null
    verifyCode.value = null
    verifyPassword.value = null
  }
  dialogVisible.value = false
  disabledCode.value = true
}
//去我的页面
const gopersonalparticulars = () => {
  token.value = localStorage.getItem('token')
  router.push({ path: 'personalparticulars' })
}

//模态框关闭回调
const handleClose = () => {
  email.value = null
  emailCode.value = null
  password.value = null
  verifyCode.value = null
  verifyPassword.value = null
  dialogVisible.value = false
}

//退出登录
const loginOut = () => {
  localStorage.removeItem('token')
  router.push({ path: '/' })
}

//点击一级
const handleClick = (v) => {
  let params = {
    labelId: v.id,
    pageNum: '1',
    pageSize: '9999'
  }
  listArticleByLabelApi(params).then((res) => {
    if (res.code == 200) {
      counterStore.homeList = res.data.articleList
    }
  })
}
//点击二级
const handleClickItem = (v) => {
  let params = {
    labelId: v.id,
    pageNum: '1',
    pageSize: '10000'
  }
  listArticleByLabelApi(params).then((res) => {
    if (res.code == 200) {
      counterStore.homeList = res.data.articleList
    }
  })
}
const recommendLabel = ref({})

//热门推荐
const clickRecommendLabel = (v) => {
  let params = {
    labelId: v.id,
    pageNum: '1',
    pageSize: '10000'
  }
  listArticleByLabelApi(params).then((res) => {
    if (res.code == 200) {
      counterStore.homeList = res.data.articleList
    }
  })
}

//点击首页logo
const goHome = () => {
  router.push('/')
  emit('update:title', localTitle.value) //点击图标重新加载数据
}

//点击特效
const clickStyle = () => {
  document.getElementById('myButton').addEventListener('click', function () {
    var myElement = document.getElementById('myButton')
    myElement.classList.add('animate__animated', 'animate__fadeIn')
    setTimeout(() => {
      document.querySelector('.animate__animated').className = 'changing' //重复点击设置样式名字为空
    }, 20)
  })
}

//背景色显示隐藏
const changeStyle = ref(false)
const watchScrollColor = () => {
  const scrollTop = window.scrollY // 当前滚动条位置
  // console.log(scrollTop)
  if (scrollTop > 97) {
    changeStyle.value = true
  } else {
    changeStyle.value = false
  }
}

const isShow = ref(false) //验证框显示隐藏
const vcodeType = ref(0) //验证框参数

const onClose = () => {
  isShow.value = false
  vcodeType.value = 0
}
const onSuccess = () => {
  onClose() // 验证成功，手动关闭模态框
  dialogVisible.value = true
  vcodeType.value = 1
  clickCode()
  isShow.value = false
}

//获取屏幕宽度 适配调整
const pageWidth = ref()
const onLoad = () => {
  pageWidth.value = window.innerWidth
}

onMounted(() => {
  listLabelApi().then((res) => {
    if (res.code == 200) {
      listLabel.value = res.data
    }
  })
  token.value = localStorage.getItem('token')
  recommendLabelApi().then((res) => {
    if (res.code == 200) {
      recommendLabel.value = res.data
    }
  })
  window.addEventListener('scroll', _.throttle(watchScrollColor, 20))
  onLoad()
})
</script>

<template>
  <header>
    <div class="container-page">
      <!-- 头部导航 -->
      <div class="hesders-box">
        <!-- nav的版心盒子 -->
        <div class="nav-box">
          <!-- 搜索框背景 -->
          <div id="topBg" v-if="changeStyle"></div>
          <!-- logo -->
          <!-- pc样式 -->
          <div class="top-nav-box" v-if="pageWidth > 600">
            <el-affix :offset="12">
              <img
                src="@/assets/header/logo.png"
                class="left-logo"
                @click="goHome()"
              />
            </el-affix>
            <div v-if="!token">
              <img src="@/assets/header/cctv.png" class="right-logo" />
              <el-button class="logoing" @click="loginChange(1)"
                >登录</el-button
              >
            </div>
            <div v-else>
              <img src="@/assets/header/cctv.png" class="right-logo" />
              <el-dropdown>
                <el-button
                  class="logoing loginUs"
                  @click="gopersonalparticulars()"
                >
                  我的</el-button
                >
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="loginOut()"
                      >退出登录</el-dropdown-item
                    >
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </div>
          <el-affix :offset="0" style="width: 100%" v-else>
            <img
              src="../../assets/header/bgc.png"
              alt=""
              style="height: 8.8rem; width: 100%; position: absolute"
            />
            <div class="top-nav-box">
              <img
                src="@/assets/header/logo.png"
                class="left-logo"
                @click="goHome()"
              />
              <div style="display: flex">
                <div v-if="!token">
                  <img src="@/assets/header/cctv.png" class="right-logo" />
                  <el-button class="logoing" @click="loginChange(1)"
                    >登录</el-button
                  >
                </div>
                <div v-else>
                  <img src="@/assets/header/cctv.png" class="right-logo" />
                  <el-dropdown>
                    <el-button
                      class="logoing loginUs"
                      @click="gopersonalparticulars()"
                    >
                      我的</el-button
                    >
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item @click="loginOut()"
                          >退出登录</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
                <div
                  class="dropdownLabel"
                  style="
                    display: flex;
                    align-items: center;
                    padding: 1rem 0rem 0 1rem;
                    box-sizing: border-box;
                  "
                  v-if="
                    route.path != '/personalparticulars' &&
                    route.path != '/articledetails'
                  "
                >
                  <el-dropdown trigger="click">
                    <span class="el-dropdown-link">
                      <img
                        style="width: 2rem; height: 2rem"
                        class="dropdownImg"
                        src="../../assets/login/组 456@2x.png"
                        alt=""
                      />
                    </span>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item
                          v-for="item in listLabel"
                          v-bind:key="item.id"
                          style="cursor: pointer"
                          @click="handleClick(item)"
                        >
                          {{ item.labelName }}</el-dropdown-item
                        >
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </div>
            </div>
          </el-affix>
          <!-- 搜索 -->
          <!-- >600的pc样式 -->
          <div class="search-box" v-if="pageWidth > 600">
            <el-affix :offset="0">
              <img
                src="@/assets/header/search.png"
                class="search-icon"
                @click="handleSearch"
              />
              <img src="@/assets/header/deer.png" class="input-icon" />

              <img src="@/assets/header/input.png" class="inputpng" />
              <el-input
                class="input"
                placeholder="请输入要搜索的文章标题..."
                v-model="localTitle"
                @keyup.enter="handleSearch"
              />
            </el-affix>
          </div>
          <el-affix
            v-else
            style="width: 100%; display: flex; align-items: center"
            :offset="70"
          >
            <div class="search-box" style="margin: 0 auto">
              <img
                src="@/assets/header/search.png"
                class="search-icon"
                @click="handleSearch"
              />
              <img src="@/assets/header/deer.png" class="input-icon" />
              <img src="@/assets/header/input.png" class="inputpng" />
              <el-input
                class="input"
                placeholder="请输入要搜索的文章标题..."
                v-model="localTitle"
                @keyup.enter="handleSearch"
              />
            </div>
          </el-affix>
          <!-- 指南 -->
          <ul class="app-header-nav">
            <div class="hotlabel">
              <li
                v-for="item in recommendLabel"
                v-bind:key="item.id"
                @click="clickRecommendLabel(item)"
              >
                {{ item.labelName }}
              </li>
            </div>
          </ul>
          <div class="nav-img">
            <div class="flower">
              <img src="@/assets/header/flower.png" class="left-flower" />
              <img src="@/assets/header/flower.png" class="left-flower" />
              <img src="@/assets/header/flower.png" class="left-flower" />
            </div>
            <img src="@/assets/header/panda.png" class="right-panda" />
          </div>
        </div>
      </div>
      <!-- 标签 -->
      <div
        class="nav-tag"
        v-if="
          route.path != '/personalparticulars' &&
          route.path != '/articledetails'
        "
      >
        <div class="tag-box">
          <div class="tag">
            <el-dropdown
              v-for="item in listLabel"
              v-bind:key="item.id"
              style="cursor: pointer"
            >
              <span class="el-dropdown-link" @click="handleClick(item)">
                {{ item.labelName }}
                <img src="@/assets/header/jianto.png" class="el-icon--right" />
              </span>
              <template #dropdown v-if="item.childLabelList.length > 0">
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="list in item.childLabelList"
                    v-bind:key="list.id"
                    @click="handleClickItem(list)"
                  >
                    {{ list.labelName }}
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
    <el-dialog
      v-model="dialogVisible"
      width="0"
      :show-close="false"
      :before-close="handleClose"
    >
      <div class="login-content">
        <div class="login-prompt" v-if="loginTitle == 1">
          <div class="top">
            <div class="login-title">验证码登录</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -215px"
            />
            <img
              src="@/assets/login/组 201@2x.png"
              class="el-icon--right"
              id="closeIcon"
              @click="handleClose()"
            />
          </div>
          <div class="center">
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 邮箱</span>
                  <el-input
                    v-model="email"
                    size="large"
                    class="input"
                  ></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="threeCharacters"> 验证码</span>
                  <el-input
                    class="input"
                    size="large"
                    v-model="emailCode"
                    style="width: 120px"
                  ></el-input>
                </div>
              </div>
              <div v-if="disabledCode" class="getCode" @click="clickCode()">
                获取验证码
              </div>
              <div class="getCode" v-else style="cursor: default">
                {{ codeStatus }}
              </div>
            </div>
            <div id="myButton">
              <img
                @click="loginClick"
                style="cursor: pointer"
                src="@/assets/login/组 360@2x.png"
                class="el-icon--right"
              />
            </div>
          </div>
          <div class="bottom">
            <div @click="loginChange(1)">
              验证码登录
              <img src="@/assets/login/组 204@2x.png" class="el-icon--bpttom" />
            </div>
            <div @click="loginChange(2)">
              密码登录
              <img src="@/assets/login/组 204@2x.png" class="el-icon--bpttom" />
            </div>
            <div @click="loginChange(3)">注册</div>
          </div>
        </div>
        <div class="login-prompt" v-if="loginTitle == 2">
          <div class="top">
            <div class="login-title">密码登录</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -215px"
            />
            <img
              src="@/assets/login/组 201@2x.png"
              class="el-icon--right"
              id="closeIcon"
              @click="handleClose()"
            />
          </div>
          <div class="center">
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 邮箱</span>
                  <el-input v-model="email" class="input"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 密码</span>
                  <el-input class="input" v-model="password"></el-input>
                </div>
              </div>
              <div class="forgetPw" @click="loginChange(4)">忘记密码?</div>
            </div>
            <div id="myButton">
              <img
                style="cursor: pointer"
                src="@/assets/login/组 360@2x.png"
                class="el-icon--right"
                @click="passwordLoginClick()"
              />
            </div>
          </div>
          <div class="bottom">
            <div @click="loginChange(1)">
              验证码登录
              <img src="@/assets/login/组 204@2x.png" class="el-icon--bpttom" />
            </div>
            <div @click="loginChange(2)">
              密码登录
              <img src="@/assets/login/组 204@2x.png" class="el-icon--bpttom" />
            </div>
            <div @click="loginChange(3)">注册</div>
          </div>
        </div>
        <div class="login-prompt" v-if="loginTitle == 3">
          <div class="top">
            <div class="login-title">注册</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -215px"
            />
            <img
              src="@/assets/login/组 201@2x.png"
              class="el-icon--right"
              id="closeIcon"
              @click="handleClose()"
            />
          </div>
          <div class="center">
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 昵称</span>
                  <el-input class="input" v-model="nickName"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 邮箱</span>
                  <el-input class="input" v-model="email"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="threeCharacters"> 验证码</span>
                  <el-input class="input" v-model="verifyCode"></el-input>
                </div>
              </div>
              <div v-if="disabledCode" class="getCode" @click="clickCode()">
                获取验证码
              </div>
              <div class="getCode" v-else style="cursor: default">
                {{ codeStatus }}
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 密码</span>
                  <el-input class="input" v-model="password"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="fourCharacters"> 确认密码</span>
                  <el-input
                    class="input"
                    v-model="verifyPassword"
                    style="margin-left: 12px"
                  ></el-input>
                </div>
              </div>
            </div>
            <div id="myButton">
              <img
                @click="registerClick()"
                style="cursor: pointer"
                src="@/assets/login/组 359@2x.png"
                class="el-icon--right"
              />
              <span @click="registerClick()" class="registerBtn">注册</span>
            </div>
          </div>
          <div class="bottom">
            <div @click="loginChange(1)">
              验证码登录
              <img src="@/assets/login/组 204@2x.png" class="el-icon--bpttom" />
            </div>
            <div @click="loginChange(2)">
              密码登录
              <img src="@/assets/login/组 204@2x.png" class="el-icon--bpttom" />
            </div>
            <div @click="loginChange(3)">注册</div>
          </div>
        </div>
        <div class="login-prompt" v-if="loginTitle == 4" style="">
          <div class="top">
            <div class="login-title">重置密码</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -215px"
            />
            <img
              src="@/assets/login/组 201@2x.png"
              class="el-icon--right"
              id="closeIcon"
              @click="handleClose()"
            />
          </div>
          <div class="center">
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div style="display: flex; justify-content: space-between">
                  <span class="twoCharacters"> 邮箱</span>
                  <el-input class="input" v-model="email"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="threeCharacters"> 验证码</span>
                  <el-input
                    class="input"
                    style="width: 120px"
                    v-model="verifyCode"
                  ></el-input>
                </div>
              </div>
              <div v-if="disabledCode" class="getCode" @click="clickCode()">
                获取验证码
              </div>
              <div class="getCode" v-else style="cursor: default">
                {{ codeStatus }}
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="twoCharacters"> 密码</span>
                  <el-input class="input" v-model="password"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div class="inputContent">
                <div>
                  <span class="fourCharacters"> 确认密码</span>
                  <el-input
                    class="input"
                    v-model="verifyPassword"
                    style="margin-left: 20px"
                  ></el-input>
                </div>
              </div>
            </div>
            <div id="myButton">
              <img
                @click="updatePasswordClick()"
                style="cursor: pointer"
                src="@/assets/login/组 360@2x.png"
                class="el-icon--right"
              />
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
    <div id="puzzle-vcode">
      <Vcode :show="isShow" @success="onSuccess" @close="onClose" />
    </div>
  </header>
</template>
<style lang="scss" scoped>
.container-page {
  width: 100vw;
  height: 100%;
  // overflow-y: auto;

  .hesders-box {
    width: 100%;
    height: 17.5rem;
    background-image: url(../../assets/header/bgc.png);
    background-size: cover; // 使用 cover 来覆盖整个容器，可能会裁剪图片
    background-position: center;
    background-repeat: no-repeat; // 防止背景图片重复
    display: flex;
    justify-content: center;

    .nav-box {
      width: 75rem;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;

      .top-nav-box {
        width: 100%;
        margin-top: 30px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: relative;

        .logoing {
          position: absolute;
          right: 17px;
          top: 31px;
          background-color: #ccddff;
          border-radius: 5px;
          width: 35px;
          height: 30px;
        }

        .left-logo {
          width: 178px;
          height: 32px;
          cursor: pointer;
          padding-left: 10px;
        }

        .right-logo {
          width: 61px;
          height: 70px;
        }
      }

      .search-box {
        // width: 840px;
        // height: 50px;
        width: 800px;
        height: 45px;
        margin-top: 40px;
        position: relative;

        .inputpng {
          height: 59px;
          width: 840px;
          z-index: 22;
        }

        .input {
          width: 830px;
          height: 48px;
          position: absolute;
          left: 5px;
          top: 5px;

          // ::v-deep(.el-input .el-input--small .input) {
          //   border: none;
          // }

          // ::v-deep(.el-input__wrapper .is-focus) {
          //   border: none;
          // }

          ::v-deep(.el-input__wrapper) {
            border-radius: 100px;
            // border: none;
            box-shadow: 0 0 0 0px var(--el-border-color) inset;
          }
        }

        .search-icon {
          position: absolute;
          width: 18px;
          top: 18px;
          right: 10px;
          z-index: 88;
        }

        .input-icon {
          position: absolute;
          top: -28px;
          left: 0;
          width: 150px;
          height: 27px;
        }
      }

      .app-header-nav {
        width: 820px;
        display: flex;
        position: relative;
        margin-top: 22px;
        .hotlabel {
          display: flex;
        }
        li {
          margin-right: 30px;
          text-align: left;
          list-style-type: none;
          cursor: pointer;
          color: #999999;

          a {
            font-size: 16px;
            display: inline-block;
            text-decoration: none;
            font-size: 14px;
            color: #999999;

            font-family:
              Source Han Sans,
              Source Han Sans;
            // &:hover {
            //   color: $xtxColor;
            //   border-bottom: 1px solid $xtxColor;
            // }
          }
        }
        .dropdownLabel {
          display: none;
        }
      }

      .nav-img {
        display: flex;
        width: 100%;
        justify-content: space-between;
        position: relative;

        .flower {
          height: 29px;
          width: 25;
        }

        .left-flower {
          margin-right: 30px;
          height: 29px;
          width: 25;
        }

        .right-panda {
          position: absolute;
          width: 160px;
          height: 160px;
          top: -110px;
          right: 0px;
        }
      }
    }
  }

  .nav-tag {
    background-color: #f2f6fa;
    display: flex;
    justify-content: center;
    box-shadow: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
    .tag-box {
      width: 1200px;
      height: 100%;
      display: flex;
      flex-direction: column;
      align-items: center;

      .tag {
        display: flex;
        flex-wrap: wrap;
        .el-icon--right {
          height: 15px;
          width: 15px;
        }

        .example-showcase .el-dropdown-link {
          cursor: pointer;
          // color: var(--el-color-primary);
          display: flex;
          align-items: center;
        }

        ::v-deep(.el-dropdown) {
          width: 200px;
          height: 50px;
          background-color: #f2f5fa;
          border: none;
          box-shadow: 0 0 0 0px var(--el-border-color) inset;
          // color: 0 0 0 0px var(--el-text-color-regular) inset;
          color: 0 0 0 0px var(--el-text-color-regular);
          display: flex;
          justify-content: center;
          align-items: center;
          font-weight: 500;
          color: #333333;
          font-size: 17px;
          font-family:
            Source Han Sans,
            Source Han Sans;

          // .el-select__placeholder.is-transparent {
          //   user-select: none;
          //   // color: #333333;
          //   font-weight: 500;
          //   font-size: 14px;
          //   font-family:
          //     Source Han Sans,
          //     Source Han Sans;
          // }

          // ::v-deep(.el-select__selected-item el-select__placeholder is-transparent) {
          //   color: #333333;
          // }
        }
        ::v-deep(.el-dropdown:hover) {
          background-color: #ffffff;
        }
        //去掉黑框
        ::v-deep(.el-tooltip__trigger:focus) {
          outline: none; // unset 这个也行
        }
      }
    }
  }
}

.login-content:nth-child(1) {
  height: 394px;
}
.login-content:nth-child(2) {
  height: 394px;
}
.login-content:nth-child(3) {
  height: 620px;
}
.login-content:nth-child(4) {
  height: 526px;
}

.login-prompt {
  width: 370px;
  background: #ffffff;
  border-radius: 30px 30px 30px 30px;
  border: 6px solid #000000;
  z-index: 99;
  display: flex;
  align-items: center;
  flex-direction: column;
  margin: auto;
  margin-left: -200px;
  .top {
    .login-title {
      width: 140px;
      height: 41px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 500;
      font-size: 24px;
      color: #333333;
      font-style: normal;
      text-transform: none;
      margin-top: 30px;
      margin-bottom: 30px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    img {
      width: 130px;
      height: 130px;
      position: absolute;
      /* left: 380px;
      top: -53px; */
      left: 324px;
      top: -6px;
    }
    #closeIcon {
      width: 15px;
      height: 15px;
      position: absolute;
      left: -165px;
      top: 66px;
      cursor: pointer;
    }
  }
  .center {
    display: flex;
    align-items: center;
    flex-direction: column;
    font-size: 18px;
    font-weight: 400;
    font-family:
      Source Han Sans,
      Source Han Sans;
    color: #333333;
    .inputContent {
      position: absolute;
      top: 11px;
      left: 20px;
      div {
        display: flex;
        justify-content: space-between;
        align-items: center;
      }
      .input {
        width: 210px;
        ::v-deep(.el-input__wrapper) {
          height: 100%;
        }
      }
      .twoCharacters {
        width: 40px;
      }
      .threeCharacters {
        width: 55px;
      }
      .fourCharacters {
        width: 72px;
      }
    }
    .el-icon--right {
      width: 312px;
      height: 44px;
      margin-bottom: 30px;
    }
    .forgetPw {
      position: absolute;
      top: 51px;
      right: 8px;
      font-size: 17px;
      cursor: pointer;
    }
    .registerBtn {
      cursor: pointer;
      position: absolute;
      left: -11px;
      // padding-top: 5px;
      width: 56px;
      height: 41px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 550;
      font-size: 22px;
      color: #333333;
      line-height: 41px;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }
    .login-text {
      width: 56px;
      height: 41px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 500;
      font-size: 28px;
      color: #333333;
      line-height: 41px;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }
    ::v-deep(.el-input__wrappe) {
      width: 240px;
      padding: 0;
      height: 29px;
    }
    .getCode {
      position: absolute;
      cursor: pointer;
      width: 94px;
      // height: 29px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 400;
      font-size: 18px;
      color: #333333;
      line-height: 29px;
      text-align: left;
      font-style: normal;
      text-transform: none;
      top: 9px;
      right: 20px;
    }
    .input {
      width: 220px;
      height: 29px;
      position: absolute;
      left: 59px;
      //top: -3px;
      font-size: 18px;
      color: #333333;

      ::v-deep(.el-input__wrapper) {
        border-radius: 100px;
        // border: none;
        box-shadow: 0 0 0 0px var(--el-border-color) inset;
      }
      ::v-deep(.el-input__inner) {
        color: #333333;
      }
    }
  }
  .bottom {
    display: flex;
    width: 300px;
    height: 22px;
    font-family:
      Source Han Sans,
      Source Han Sans;
    font-weight: 400;
    font-size: 18px;
    color: #333333;
    /* line-height: 26px; */
    align-items: center;
    font-style: normal;
    text-transform: none;
    margin-bottom: 30px;
    div {
      cursor: pointer;
    }
    img {
      width: 4px;
      height: 22px;
      margin: -3px 20px;
    }
  }
}
::v-deep(.el-dialog) {
  /* --el-dialog-bg-color: #ffffff00; */
  --el-dialog-bg-color: #9d9fa000;
  .el-overlay {
    z-index: 100 !important;
  }
}

#topBg {
  position: fixed;
  width: 100vw;
  height: 59px;
  background: #fff;
  top: 1;
  z-index: 2;
  box-shadow: var(--el-box-shadow-light);
}

::v-deep(.vue-puzzle-vcode) {
  z-index: 30227 !important;
}

/* 移动端 100-500px */
@media screen and (min-width: 100px) and (max-width: 500px) {
  .container-page {
    .hesders-box {
      height: auto;
      .nav-box {
        width: 100vw;

        .top-nav-box {
          width: 100%;
          margin-top: 0;
          // padding: 0 1.5rem 0 1rem;
          padding: 1.075rem 1.5rem 0 1rem;
          box-sizing: border-box;
          .logoing {
            position: absolute;
            right: 5.6rem;
            top: 3rem;
            background-color: #ccddff;
            border-radius: 0.3125rem;
            width: 2.1875rem;
            height: 1.875rem;
          }
          .loginUs {
            right: 1rem;
            top: 1.9375rem;
          }
          .left-logo {
            width: 11.1rem;
            height: 2rem;
            cursor: pointer;
            padding-left: 0.625rem;
          }

          .right-logo {
            width: 3.8rem;
            height: 4.37rem;
          }
        }

        .search-box {
          width: 94%;
          // height: 100%;
          height: 3rem;
          // margin-top: 3.03rem !important;
          margin-top: 0;
          position: relative;
          z-index: 130;
          .inputpng {
            height: 100%;
            width: 100%;
            z-index: 120 !important;
          }

          .input {
            width: 98%;
            height: 85%;
            position: absolute;
            left: 0.24rem;
            top: 0.24rem;

            ::v-deep(.el-input__wrapper) {
              border-radius: 8.3rem;
              box-shadow: 0 0 0 0px var(--el-border-color) inset;
            }
          }

          .search-icon {
            position: absolute;
            width: 1.125rem;
            top: 0.8rem;
            right: 0.925rem;
            z-index: 88;
          }

          .input-icon {
            display: none;
            position: absolute;
            top: -2.33rem;
            left: 0;
            width: 12.5rem;
            height: 2.25rem;
          }
        }

        .app-header-nav {
          width: 100vw !important;
          margin-top: 0.5015rem;
          margin-bottom: 0.1rem;
          padding: 0 0.825rem;
          box-sizing: border-box;
          display: flex;
          justify-content: space-between;
          li {
            margin-right: 1.875rem;
            a {
              font-size: 0.875rem;
            }
          }
          .dropdownLabel {
            display: block;
          }
          .hotlabel {
            display: none;
          }
        }

        .nav-img {
          // display: flex;
          display: none;
          width: 100%;
          height: 1.875rem;
          padding-left: 1rem;
          box-sizing: border-box;
          .flower {
            height: 1.8125rem;
          }

          .left-flower {
            margin-right: 1.8725rem;
            height: 1.8125rem;
          }

          .right-panda {
            width: 7rem;
            height: 7rem;
            top: -4.45rem;
            right: 0px;
          }
        }
      }
    }
    .nav-tag {
      display: none;
      .tag-box {
        width: 1200px;
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;

        .tag {
          ::v-deep(.el-dropdown) {
            width: 10.41rem;
            height: 3.125rem;
            font-size: 1.0625rem;
          }
          ::v-deep(.el-dropdown:hover) {
            background-color: #ffffff;
          }
          //去掉黑框
          ::v-deep(.el-tooltip__trigger:focus) {
            outline: none; // unset 这个也行
          }
          .el-icon--right {
            height: 0.9375rem;
            width: 0.9375rem;
          }
        }
      }
    }
  }
  #topBg {
    display: none;
  }
  .login-prompt {
    width: 23.125rem;
    border-radius: 1.875rem;
    border: 0.375rem solid #000000;
    margin-left: -12.5rem;
    .top {
      .login-title {
        width: 8.75rem;
        height: 2.5625rem;
        font-weight: 500;
        font-size: 1.5rem;
        margin-top: 1.875rem;
        margin-bottom: 1.875rem;
      }
      img {
        width: 8.125rem;
        height: 8.125rem;
        left: 25.25rem;
        top: -0.375rem;
      }
      #closeIcon {
        width: 0.9375rem;
        height: 0.9375rem;
        left: -10.3125rem;
        top: 4.125rem;
      }
    }
    .center {
      font-size: 1.125rem;
      font-weight: 400;
      .inputContent {
        position: absolute;
        top: 0.6875rem;
        left: 1.25rem;
        .input {
          width: 13.125rem;
        }
        .twoCharacters {
          width: 2.5rem;
        }
        .threeCharacters {
          width: 3.4375rem;
        }
        .fourCharacters {
          width: 4.5rem;
        }
      }
      .el-icon--right {
        width: 19.5rem;
        height: 2.75rem;
        margin-bottom: 1.875rem;
      }
      .forgetPw {
        top: 3.1875rem;
        right: 0.5rem;
        font-size: 1.0625rem;
      }
      .registerBtn {
        left: -0.6875rem;
        width: 3.5rem;
        height: 2.5625rem;
        font-weight: 550;
        font-size: 1.375rem;
        line-height: 2.5625rem;
      }
      .login-text {
        width: 3.5rem;
        height: 2.5625rem;
        font-size: 1.75rem;
        line-height: 2.5625rem;
      }
      ::v-deep(.el-input__wrappe) {
        width: 15rem;
        padding: 0;
        height: 1.8125rem;
      }
      .getCode {
        width: 5.875rem;
        font-size: 1.125rem;
        line-height: 1.8125rem;
        top: 0.5625rem;
        right: 1.25rem;
      }
      .input {
        width: 13.75rem;
        height: 1.8125rem;
        left: 3.6875rem;
        font-size: 1.125rem;

        ::v-deep(.el-input__wrapper) {
          border-radius: 6.25rem;
        }
      }
    }
    .bottom {
      width: 18.75rem;
      height: 1.375rem;
      font-size: 1.125rem;
      margin-bottom: 1.875rem;
      div {
        display: flex;
        text-align: center;
      }
      img {
        width: 0.25rem;
        height: 1.375rem;
        margin: -0.1875px 1.25rem;
      }
    }
  }
  .login-content:nth-child(1) {
    height: 24.625rem;
  }
  .login-content:nth-child(2) {
    height: 24.625rem;
  }
  .login-content:nth-child(3) {
    height: 38.75rem;
  }
  .login-content:nth-child(4) {
    height: 32.875rem;
  }
}
</style>
