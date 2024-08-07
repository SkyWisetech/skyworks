<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listArticle, deleteArticle } from '@/api/articlemanage/articlemanage'
const loading = ref(false)
const router = useRouter()
// 查询文章（列表）
const listData = ref({
  title: '', //文章标题，可模糊查询
  upvote: 0, //点赞数，大于等于此点赞数
  startDate: '', //创建文章开始日期
  endDate: '', //创建文章结束日期
  pageNum: 1, //分页-当前页数
  pageSize: 10 //分页-一页大小
})
const tableData = ref([])
const total = ref(0) // 用于存储文章总数
const page = ref(0) // 用于存储文章总页数
const listArticleData = async () => {
  loading.value = true
  try {
    const res = await listArticle(listData.value)
    console.log(res, '页面')
    tableData.value = res.data.data.articleList
    total.value = res.data.data.totalCount
    page.value = res.data.data.totalPage
    console.log(total.value, '3333')
  } catch {
    ElMessage.error('error')
  }
  loading.value = false
}
// 分页查询
const onPage = (currentPage) => {
  listData.value.pageNum = currentPage
  listArticleData()
}
const handlePageChange = (newPage) => {
  console.log('newPage===', newPage)
  listData.value.pageNum = newPage
  listArticleData()
}

const handlePageSizeChange = (newSize) => {
  console.log('newSize====', newSize)
  listData.value.pageSize = newSize
  listArticleData()
}
// 搜索
const searchListArticleData = async () => {
  loading.value = true
  listData.value.pageNum = 1
  listData.value.pageSize = 3
  const res = await listArticle(listData.value)
  total.value = res.data.data.totalCount
  tableData.value = res.data.data.articleList
  console.log(res, '搜索')
  loading.value = false
}
const search = () => {
  searchListArticleData()
}
// 重置
const resetSearch = () => {
  listData.value.title = null
  listData.value.upvote = null
  listData.value.startDate = null
  listData.value.endDate = null
  listArticleData()
}
// 编辑文章
const onEditArticle = (id) => {
  router.push({
    path: '/articlepublish',
    query: {
      id
    }
  })
}
// 删除文章
const onDeleteArticle = async (id) => {
  const res = await deleteArticle({ articleId: id })
  if (res.data.code === 200) {
    ElMessage.success('删除成功')
    listArticleData()
  } else {
    ElMessage.error('删除失败')
  }
}
// 查看文章

const onViewComments = (id) => {
  router.push({
    path: '/articlecomment',
    query: {
      id
    }
  })
}

onMounted(() => {
  listArticleData()
})
</script>

<template>
  <el-card class="page-container">
    <el-form inline>
      <el-form-item label="文章标题：" style="font-weight: 700">
        <el-input
          placeholder="请输入标题"
          v-model="listData.title"
          @keyup.enter="search"
        ></el-input>
      </el-form-item>
      <el-form-item label="创建开始日期:" style="font-weight: 700">
        <el-date-picker
          v-model="listData.startDate"
          type="date"
          placeholder="请选择日期"
          value-format="YYYY-MM-DD"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="创建结束日期:" style="font-weight: 700">
        <el-date-picker
          v-model="listData.endDate"
          type="date"
          placeholder="请选择日期"
          value-format="YYYY-MM-DD"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          style="margin: 0 5px 0 20px; height: 35px' "
          @click="search"
          >搜索</el-button
        >
        <el-button @click="resetSearch" style="height: 35px">重置</el-button>
      </el-form-item>
    </el-form>

    <el-table
      style="width: 100%"
      v-loading="loading"
      height="500px"
      :data="tableData"
      class="custom-table"
    >
      <el-table-column label="标题" prop="title">
        <!-- <template #default="{ row }">
          <el-link type="primary" :underline="false">{{ row.title }}</el-link>
        </template> -->
      </el-table-column>
      <el-table-column label="评论(数量)" prop="commentNum"></el-table-column>
      <el-table-column label="点赞(数量)" prop="upvoteNum"></el-table-column>
      <el-table-column label="收藏(数量)" prop="collectNum"></el-table-column>
      <el-table-column label="创建日期" prop="createTime"></el-table-column>
      <el-table-column label="更新日期" prop="updateTime"></el-table-column>
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button plain type="primary" @click="onViewComments(row.id)"
            >查看评论</el-button
          >
          <el-button plain type="primary" @click="onEditArticle(row.id)"
            >编辑</el-button
          >
          <el-popconfirm
            title="确定要删除这篇文章嘛?"
            @confirm="onDeleteArticle(row.id)"
          >
            <template #reference>
              <el-button plain type="danger">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="没有数据" />
      </template>
    </el-table>
    <!-- 分页 -->

    <PagiNations
      @getPage="onPage"
      :pageNum="listData.pageNum"
      :page-size="listData.pageSize"
      :totalProp="total"
      @update:currentPage="handlePageChange"
      @update:pageSize="handlePageSizeChange"
    />
  </el-card>
</template>
<style lang="scss" scoped>
.page-container {
  min-height: 96%;
  box-sizing: border-box;
  margin-top: 15px;
  border-radius: 20px 20px 20px 20px;
  .custom-table {
    margin-top: 20px;
    ::v-deep(.el-table__row) {
      height: 46.7px; /* 设置为你想要的行高 */
      // line-height: 60px; /* 可选，根据需要调整行内文本的垂直居中 */
    }
  }
  ::v-deep(.el-form-item--small .el-form-item__label) {
    height: 35px;
    line-height: 35px;
    // display: flex;
    // align-items: center;
  }
  ::v-deep(.el-input) {
    height: 35px;
    width: 186px;
    border-radius: 6px 6px 6px 6px;
  }

  ::v-deep(.el-table__cell) {
    text-align: center;
    color: #333333;
  }
}
</style>
