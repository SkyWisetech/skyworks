<script setup>
import { ref, onMounted } from 'vue'
import { viewComment, delComment } from '@/api/articlemanage/articlemanage'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
const isLoading = ref(false)
const isDeling = ref(false)

let totalNum = ref(0)
const breadcrumbItems = ref([
  { text: '文章管理', to: '/articlemanage' },
  { text: '查看评论' } // 假设有一个Dashboard页面
])
// const pagesInfo = reactive({ pageSize: 10, pageNum: 1 })
const route = useRoute()
// 查看评论
const listData = ref({
  articleId: '',
  pageNum: 1,
  pageSize: 10
})
const tableData = ref([])
listData.value.articleId = route.query.id
const viewArticleComment = async () => {
  isLoading.value = true
  const res = await viewComment(listData.value)
  isLoading.value = false
  console.log(res)

  if (res.data.code == 200) {
    tableData.value = res.data.data.commentList
    totalNum.value = res.data.data.totalCount
  }
}
// 分页查询
const onPage = (currentPage) => {
  listData.value.pageNum = currentPage
  viewArticleComment()
}
const handlePageChange = (newPage) => {
  listData.value.pageNum = newPage
  viewArticleComment()
}

const handlePageSizeChange = (newSize) => {
  listData.value.pageSize = newSize
  viewArticleComment()
}
// 删除评论
const onDel = async (commentId) => {
  console.log(commentId)
  isDeling.value = true
  const res = await delComment({ commentId: commentId })
  isDeling.value = false
  if (res.data.code === 200) {
    ElMessage.success('删除评论成功')
    viewArticleComment()
  } else {
    ElMessage.error('删除评论失败')
  }
  console.log(res, '1111')
}
onMounted(() => {
  viewArticleComment()
})
</script>

<template>
  <NavigationBars :breadcrumbItems="breadcrumbItems"> </NavigationBars>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>评论</span>
        <div class="extra">
          <slot name="extra"></slot>
        </div>
      </div>
    </template>
    <div class="container">
      <el-table
        v-loading="loadingProp"
        class="custom-table"
        :data="tableData"
        style="width: 100%; height: 500px"
      >
        <el-table-column prop="title" label="用户名" width="180" />
        <el-table-column prop="content" label="评论详情" width="380" />
        <el-table-column prop="createTime" label="日期" width="180" />
        <el-table-column fixed="right" label="操作" min-width="120">
          <template #default="{ row }">
            <el-popconfirm
              confirm-button-text="是的"
              cancel-button-text="取消"
              @confirm="onDel(row.id)"
              title="确定删除评论吗"
            >
              <template #reference>
                <el-button :icon="Delete" plain type="danger">删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="没有数据" />
        </template>
      </el-table>
      <!-- <PagiNations @getPage="getPageInfo" :totalProp="totalNum" />
        -->
      <!-- 分页 -->

      <PagiNations
        @getPage="onPage"
        :pageNum="listData.pageNum"
        :page-size="listData.pageSize"
        :totalProp="totalNum"
        @update:currentPage="handlePageChange"
        @update:pageSize="handlePageSizeChange"
      />
    </div>
  </el-card>
</template>
<style scoped lang="scss">
.page-container {
  // min-height: 90%;
  box-sizing: border-box;
  margin-top: 15px;
  border-radius: 20px 20px 20px 20px;
}
.custom-table {
  ::v-deep(.el-table__row) {
    height: 46.7px; /* 设置为你想要的行高 */
  }
}
.container {
  padding: 0px 40px;
  background: #ffffff;
  border-radius: 20px;
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
