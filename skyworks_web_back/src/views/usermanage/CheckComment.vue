<!--
 * new page
 * @since: 2024-07-24
 * checkComment.vue
-->
<template>
  <div class="container">
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/user' }">用户管理</el-breadcrumb-item>
      <el-breadcrumb-item>评论</el-breadcrumb-item>
    </el-breadcrumb>
    <el-table
      v-loading="loadingProp"
      class="user-table"
      :data="tableData"
      style="width: 100%"
    >
      <el-table-column prop="title" label="文章标题" width="180" />
      <el-table-column prop="content" label="评论详情" width="380" />
      <el-table-column prop="createTime" label="注册日期" width="180" />
      <el-table-column fixed="right" label="操作" min-width="120">
        <template #default="scope">
          <el-popconfirm
            confirm-button-text="是的"
            cancel-button-text="取消"
            @confirm="onDel(scope.row.id)"
            title="确定删除评论吗"
          >
            <template #reference>
              <el-button type="info" link size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <PagiNations
      :pageNum="pagesInfo.pageNum"
      :pageSize="pagesInfo.pageSize"
      @getPage="getPageInfo"
      :totalProp="totalNum"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { delComment, getUserCommentList } from '@/api/usermanage/userManage.js'
import PagiNations from '@/components/PagiNations.vue'
const isLoading = ref(false)
const isDeling = ref(false)
const userId = ref(null)
let totalNum = ref(0)
const pagesInfo = reactive({ pageSize: 10, pageNum: 1 })
let tableData = ref([])
// 获取查看ID
const route = useRoute()
userId.value = route.params.id

onMounted(() => {
  getList()
})
// 获取列表
const getList = async () => {
  isLoading.value = true
  const { data } = await getUserCommentList({
    userId: userId.value,
    ...pagesInfo
  })
  isLoading.value = false
  if (data.code == 200) {
    tableData.value = data.data.commentList
    totalNum.value = data.data.totalCount
  }
  console.log('获取用户评论列表---', data)
}
// 分页查询
const getPageInfo = (page) => {
  pagesInfo.pageNum = page
  getList()
}
const onDel = async (id) => {
  // 删除评论
  isDeling.value = true
  const { data } = await delComment(id)
  isDeling.value = false
  const { code } = data
  if (code == 200) {
    getList()
  }
}
</script>

<style scoped lang="scss">
.container {
  padding: 30px 40px;
  background: #ffffff;
  border-radius: 20px;
  .user-table {
    margin-top: 30px;
  }
  ::v-deep .el-table--small .cell {
    display: flex;
    justify-content: center;
  }
  ::v-deep .el-table tr {
    color: #333;
    font-weight: 500;
  }
}
</style>
