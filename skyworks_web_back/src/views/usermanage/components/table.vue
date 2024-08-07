<template>
  <el-table
    v-loading="loadingProp"
    class="user-table"
    :data="tableProps"
    style="width: 100%"
  >
    <el-table-column prop="id" label="用户ID" width="180" />
    <el-table-column prop="nickName" label="昵称" width="180" />
    <el-table-column prop="email" label="邮箱" />
    <el-table-column prop="createTime" label="注册日期" />
    <el-table-column fixed="right" label="操作" min-width="120">
      <template #default="scope">
        <el-button
          type="info"
          link
          size="small"
          @click="checkComment(scope.row.id)"
          >查看评论</el-button
        >
        <el-button type="info" link size="small" @click="onForbid(scope.row)">{{
          scope.row.isBlack === 0 ? '禁止登陆' : '登录解禁'
        }}</el-button>
      </template>
    </el-table-column>
  </el-table>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { updateUser } from '@/api/usermanage/userManage.js'
const router = useRouter()
defineProps({
  loadingProp: {
    type: Boolean,
    default: false
  },
  tableProps: {
    type: Object,
    default: () => {
      return { id: '', nickName: '', email: '', createTime: '' }
    }
  }
})

const emit = defineEmits(['refresh'])
// 查看评论
const checkComment = (id) => {
  // 页面跳转
  router.push(`/user/checkComment/${id}`)
}
// 查看评论限制时间
// const checkCommentDate = () => {}
// 拉黑
// const onBlack = () => {}
// 禁止登陆
const onForbid = async (user) => {
  const query = {
    id: user.id,
    updateType: 3,
    nickName: user.nickName,
    forbidCommentDay: '',
    isBlack: user.isBlack === 0 ? 1 : 0
  }
  console.log('请求参数query--', query)
  const { data } = await updateUser(query)
  if (data.code == 200) {
    emit('refresh')
  }
}
</script>
