<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import {
  loginByVerifyCodeApi /* 验证码登录 */,
  sendVerifyCodeApi /* 发送邮箱验证码 */,
  loginByPassword /* 密码登录 */,
  registerApi /* 注册 */,
  updatePasswordApi /* 重置密码 */
} from '@/api/login2/loginApi.js'
import { listLabelApi } from '@/api/ClassificationManage/ClassificationManageApi.js'

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
  emit('update:title', localTitle.value)
}
// 监听 localTitle 的变化，并可选地通过自定义事件通知父组件
watch(localTitle, (newValue) => {
  emit('update:title', newValue)
})

const router = useRouter()
const loginTitle = ref(0)
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
  const params = { email: email.value }
  const res = await sendVerifyCodeApi(params)
  if (res.data.code == 200) {
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

//验证码登录
const loginClick = async () => {
  const params = {
    email: email.value,
    verifyCode: emailCode.value
  }
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
//密码登录
const passwordLoginClick = async () => {
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
//注册
const registerClick = async () => {
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
//重置密码
const updatePasswordClick = async () => {
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

  // clearInterval(timer());
  // window.clearInterval(timer());
  // disabledCode.value='获取验证码'
  // console.log('关闭了模态框');
}

onMounted(() => {
  listLabelApi().then((res) => {
    if (res.code == 200) {
      listLabel.value = res.data
    }
  })
  setTimeout(() => {
    // debugger;
  }, 2000)
})
</script>

<template>
  <header>
    <div class="container-page">
      <!-- 头部导航 -->
      <div class="hesders-box">
        <!-- nav的版心盒子 -->
        <div class="nav-box">
          <!-- logo -->
          <div class="top-nav-box">
            <img src="@/assets/header/logo.png" class="left-logo" />
            <img src="@/assets/header/cctv.png" class="right-logo" />
            <el-button
              v-if="token"
              class="logoing"
              @click="gopersonalparticulars()"
            >
              我的</el-button
            >
            <el-button v-else class="logoing" @click="loginChange(1)"
              >登录</el-button
            >
          </div>
          <!-- 搜索 -->

          <div class="search-box">
            <img
              src="@/assets/header/search.png"
              class="search-icon"
              @click="handleSearch"
            />
            <!-- <img src="@/assets/header/search.png" class="search-icon" /> -->
            <img src="@/assets/header/deer.png" class="input-icon" />
            <el-affix :offset="0">
              <img src="@/assets/header/input.png" class="inputpng" />
              <el-input
                class="input"
                placeholder="请输入要搜索的文章标题..."
                v-model="localTitle"
                @keyup.enter="handleSearch"
              />
            </el-affix>
            <!-- <el-input class="input" placeholder="请输入要搜索的文章标题..." /> -->
          </div>

          <!-- 指南 -->
          <ul class="app-header-nav">
            <li class="home">
              <RouterLink to="/">指南教程</RouterLink>
            </li>
            <li>
              <RouterLink to="/">最新流量</RouterLink>
            </li>
            <li>
              <RouterLink to="/">事实上时时处处</RouterLink>
            </li>
            <li>
              <RouterLink to="/">滴答滴答滴答的</RouterLink>
            </li>
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
      <div class="nav-tag">
        <div class="tag-box">
          <div class="tag">
            <el-dropdown v-for="item in listLabel" v-bind:key="item.id">
              <span class="el-dropdown-link">
                {{ item.labelName }}
                <img src="@/assets/header/jianto.png" class="el-icon--right" />
              </span>
              <template #dropdown v-if="item.childLabelList.length > 0">
                <el-dropdown-menu>
                  <el-dropdown-item
                    v-for="list in item.childLabelList"
                    v-bind:key="list.id"
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
        <div class="login-prompt" v-if="loginTitle == 1" style="height: 494px">
          <div class="top">
            <div class="login-title">验证码登录</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -250px"
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 60px"> 邮箱</span>
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 验证码</span>
                  <el-input
                    class="input"
                    size="large"
                    v-model="emailCode"
                    style="width: 200px"
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
            <div>
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
        <div class="login-prompt" v-if="loginTitle == 2" style="height: 494px">
          <div class="top">
            <div class="login-title">密码登录</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -250px"
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 60px"> 邮箱</span>
                  <el-input v-model="email" class="input"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 密码</span>
                  <el-input class="input" v-model="password"></el-input>
                </div>
              </div>
              <div class="forgetPw" @click="loginChange(4)">忘记密码?</div>
            </div>
            <div>
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
        <div class="login-prompt" v-if="loginTitle == 3" style="height: 780px">
          <div class="top">
            <div class="login-title">注册</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -250px"
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 60px"> 昵称</span>
                  <el-input class="input" v-model="nickName"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 60px"> 邮箱</span>
                  <el-input class="input" v-model="email"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 验证码</span>
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 密码</span>
                  <el-input class="input" v-model="password"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 确认密码</span>
                  <el-input
                    class="input"
                    v-model="verifyPassword"
                    style="margin-left: 37px"
                  ></el-input>
                </div>
              </div>
            </div>
            <div>
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
        <div class="login-prompt" v-if="loginTitle == 4" style="height: 626px">
          <div class="top">
            <div class="login-title">重置密码</div>
            <img
              src="@/assets/login/image2x.png"
              class="el-icon--right"
              style="margin-left: -250px"
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 60px"> 邮箱</span>
                  <el-input class="input" v-model="email"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 验证码</span>
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
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 密码</span>
                  <el-input class="input" v-model="password"></el-input>
                </div>
              </div>
            </div>
            <div style="position: relative">
              <img src="@/assets/login/组 174@2x.png" class="el-icon--right" />
              <div style="position: absolute; top: 14px; left: 20px">
                <div style="display: flex; justify-content: space-between">
                  <span style="width: 100px"> 确认密码</span>
                  <el-input
                    class="input"
                    v-model="verifyPassword"
                    style="margin-left: 37px"
                  ></el-input>
                </div>
              </div>
            </div>
            <div>
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
  </header>
</template>
<style lang="scss" scoped>
.container-page {
  width: 100vw;
  height: 100%;
  // overflow-y: auto;

  .hesders-box {
    width: 100%;
    height: 280px;
    background-image: url(../../assets/header/bgc.png);
    background-size: cover; // 使用 cover 来覆盖整个容器，可能会裁剪图片
    background-position: center;
    background-repeat: no-repeat; // 防止背景图片重复
    display: flex;
    justify-content: center;

    .nav-box {
      width: 1200px;
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
          width: 200px;
          height: 35px;
        }

        .right-logo {
          width: 61px;
          height: 70px;
        }
      }

      .search-box {
        width: 840px;
        height: 50px;
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
          right: 43px;
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

        li {
          margin-right: 30px;
          text-align: left;
          list-style-type: none;

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
          width: 110px;
          height: 110px;
          top: -67px;
          right: 0px;
        }
      }
    }
  }

  .nav-tag {
    /* width: 100%; */
    /* height: 50px; */
    background-color: #f2f6fa;
    display: flex;
    justify-content: center;

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
          // cursor: pointer;
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
          font-size: 22px;
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
      }
    }

    // ::v-deep(.el-select) {
    //   width: 200px;
    //   height: 82px;
    // }
  }
}
.login-prompt {
  width: 486px;
  background: #ffffff;
  border-radius: 30px 30px 30px 30px;
  border: 6px solid #000000;
  z-index: 99;
  display: flex;
  align-items: center;
  flex-direction: column;
  /* position: absolute; */
  /* top: 0;
  right: 0;
  bottom: 0;
  left: 0; */
  margin: auto;
  margin-left: -250px;
  .top {
    .login-title {
      width: 140px;
      height: 41px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 500;
      font-size: 28px;
      color: #333333;
      font-style: normal;
      text-transform: none;
      margin-top: 50px;
      margin-bottom: 50px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    img {
      width: 160px;
      height: 160px;
      position: absolute;
      /* left: 380px;
      top: -53px; */
      left: 405px;
      top: -16px;
    }
    #closeIcon {
      width: 20px;
      height: 20px;
      position: absolute;
      left: -199px;
      top: 66px;
      cursor: pointer;
    }
  }
  .center {
    display: flex;
    align-items: center;
    flex-direction: column;
    font-size: 20px;
    font-weight: 400;
    font-family:
      Source Han Sans,
      Source Han Sans;
    color: #333333;
    .el-icon--right {
      width: 412px;
      height: 54px;
      margin-bottom: 40px;
    }
    .forgetPw {
      position: absolute;
      top: 63px;
      right: 8px;
      font-size: 18px;
      cursor: pointer;
    }
    .registerBtn {
      cursor: pointer;
      position: absolute;
      left: -2px;
      padding-top: 5px;
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
      /* top: 16px;
      right: 24px;
      width: 22px;
      height: 22px; */
      cursor: pointer;
      width: 105px;
      height: 29px;
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 400;
      font-size: 20px;
      color: #333333;
      line-height: 29px;
      text-align: left;
      font-style: normal;
      text-transform: none;
      top: 12px;
      right: 25px;
    }
    .input {
      width: 280px;
      height: 29px;
      position: absolute;
      left: 59px;
      //top: -3px;
      font-size: 20px;
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
}
</style>
