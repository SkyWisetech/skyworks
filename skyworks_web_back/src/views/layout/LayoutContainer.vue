<script setup>
import { useRouter } from 'vue-router'
// import { ElMessageBox } from 'vue'
// import { useUserStore } from '@/stores/modules/user'
import { Plus, User, Crop, SwitchButton } from '@element-plus/icons-vue'
const router = useRouter()

const goToPublishArticle = () => {
  router.push(`/articlepublish`)
}
// 退出登录
// const userStore = useUserStore()
const onCommand = async () => {
  //   if (command === 'logout') {
  //     await ElMessageBox.confirm('你确认退出登录嘛？', '温馨提示', {
  //       type: 'warning',
  //       confirmButtonText: '确认',
  //       cancelButtonText: '取消'
  //     })
  //     userStore.removeToken()
  //     userStore.setUser({})
  //     router.push(`/login`)
  //   } else {
  //     router.push(`/user/${command}`)
  //   }
}
</script>
<template>
  <el-container class="layout-container">
    <el-aside width="200px">
      <div class="el-aside__logo"></div>
      <el-button
        type="primary"
        class="publisharticle"
        @click="goToPublishArticle"
      >
        <el-icon class="plus">
          <Plus />
        </el-icon>
        发布文章
      </el-button>
      <el-menu
        active-text-color="#333333 "
        :default-active="$route.path"
        text-color="#CACACA"
        router
      >
        <el-menu-item index="/">
          <el-icon>
            <Management />
          </el-icon>
          <span>首页</span>
        </el-menu-item>
        <el-menu-item index="/articlemanage">
          <el-icon>
            <Histogram />
          </el-icon>
          <span>文章管理</span>
        </el-menu-item>
        <!-- <el-sub-menu index="/user">
          <template #title>
            <el-icon>
              <Histogram />
            </el-icon>
            <span>文章管理</span>
          </template>
          <el-menu-item index="/articlemanage">
            <el-icon>
              <Histogram />
            </el-icon>
            <span>文章</span>
          </el-menu-item>
          <el-menu-item index="/articlepublish">
            <el-icon>
              <Promotion />
            </el-icon>
            <span>发布文章</span>
          </el-menu-item>
        </el-sub-menu> -->
        <el-menu-item index="/classificationmanage">
          <el-icon>
            <List />
          </el-icon>
          <span>分类管理</span>
        </el-menu-item>
        <el-menu-item index="/user/usermanage">
          <el-icon>
            <Avatar />
          </el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <!-- <el-sub-menu index="/user">
          <template #title>
            <el-icon>
              <UserFilled />
            </el-icon>
            <span>个人中心</span>
          </template>
<el-menu-item index="/user/profile">
  <el-icon>
    <User />
  </el-icon>
  <span>基本资料</span>
</el-menu-item>
<el-menu-item index="/user/avatar">
  <el-icon>
    <Crop />
  </el-icon>
  <span>更换头像</span>
</el-menu-item>
<el-menu-item index="/user/password">
  <el-icon>
    <EditPen />
  </el-icon>
  <span>重置密码</span>
</el-menu-item>
</el-sub-menu> -->
      </el-menu>
    </el-aside>
    <el-container>
      <el-header>
        <!-- <div class="administratorname">
          <span>管理员名称</span>
        </div> -->
        <el-dropdown placement="bottom-end" @command="onCommand">
          <!-- <span class="el-dropdown__box">
            <el-avatar :src="avatar" />
            <el-icon>
              <CaretBottom />
            </el-icon>
          </span> -->
          <div class="administratorname">
            <span>管理员名称</span>
            <el-icon>
              <CaretBottom />
            </el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile" :icon="User"
                >基本资料</el-dropdown-item
              >
              <el-dropdown-item command="avatar" :icon="Crop"
                >更换头像</el-dropdown-item
              >
              <!-- <el-dropdown-item command="password" :icon="EditPen" -->
              <el-dropdown-item command="password" :icon="Crop"
                >重置密码</el-dropdown-item
              >
              <el-dropdown-item command="logout" :icon="SwitchButton"
                >退出登录</el-dropdown-item
              >
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </el-header>
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
  height: 100vh;

  .el-aside__logo {
    height: 130px;
    margin-right: 30px;
  }

  .publisharticle {
    margin-left: 25px;
    height: 50px;
    width: 100px;
    font-weight: 600;

    .plus {
      font-size: 18px;
      font-weight: 700;
    }
  }

  .el-aside {
    background-color: #fff;

    &__logo {
      height: 120px;
      background: url('@/assets/logo.png') no-repeat center / 120px auto;
    }

    .el-menu {
      border-right: none;
    }
  }

  .el-header {
    background-color: #fff;
    display: flex;
    align-items: center;
    // justify-content: space-between;
    justify-content: flex-end;

    .el-dropdown__box {
      display: flex;
      align-items: center;

      .el-icon {
        color: #999;
        margin-left: 10px;
      }

      &:active,
      &:focus {
        outline: none;
      }
    }
  }

  .el-footer {
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    color: #666;
  }
}
</style>
