<script setup>
// import
// import { useRoute, useRouter } from 'vue-router'
// // 1.获取路由对象 router useRouter
// // const router = useRouter()
// // 1.获取路由参数 route useRoute
// const route = useRoute()
// const router = useRouter()

// const golist = () => {
//   console.log(router, route)
//   router.push('/list')
// }
// // import { useUserStore } from '@/stores/user.js'
// import { useUserStore } from '@/stores/modules/user'
// const userstore = useUserStore()
// console.log('1111', userstore)
</script>

<template>
  <div>
    <router-view></router-view>

    <!-- 我是APP333444
    <el-button @click="$router.push('/home')">跳首页</el-button>
    <el-button @click="golist">跳列表页</el-button>
    <p>{{ userstore.token }}</p>
    <el-button @click="userstore.setToken('ccccllll')">登录</el-button> -->
    <!-- <el-button @click="userstore.removeToken()">退出</el-button> -->
  </div>
</template>
