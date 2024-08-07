<template>
  <div>
    <!-- 搜索区 -->
    <Search @getSearchInfo="onSubmit" />
    <!-- 表格区域 -->
    <el-card class="table-container">
      <Table
        @refresh="fetchTableData"
        :loadingProp="isLoading"
        :tableProps="tableData"
      />
      <!-- 分页区 -->
      <PagiNations
        :pageNum="pageInfo.pageNum"
        :pageSize="pageInfo.pageSize"
        :totalProp="totalNum"
        @getPage="getPageInfo"
      />
    </el-card>
  </div>
</template>

<script setup>
import Search from './components/search.vue'
import Table from './components/table.vue'
import PagiNations from '@/components/PagiNations.vue'
import dayjs from 'dayjs'
import { searchUser } from '@/api/usermanage/userManage'
import { ref, reactive, onBeforeMount } from 'vue'
// 定义表格数据
let isLoading = ref(false)
let tableData = ref([])
onBeforeMount(() => {
  fetchTableData()
})
// 请求表格数据
const fetchTableData = async () => {
  isLoading.value = true
  const query = handleData()
  console.log('查询参数', query)
  const { data: fetchData } = await searchUser(query)
  isLoading.value = false
  const { data, code } = fetchData
  console.log('fetchData====', fetchData)
  if (code == 200) {
    tableData.value = data.userList
    totalNum.value = data.totalCount
  }
}

// 处理请求参数
const handleData = () => {
  const { time } = searchInfo
  const data = {
    ...searchInfo,
    ...pageInfo,
    startDate: time ? dayjs(time[0]).format('YYYY-MM-DD') : '',
    endDate: time ? dayjs(time[1]).format('YYYY-MM-DD') : ''
  }
  return data
}

// 获取查询信息
let searchInfo = reactive({})
const onSubmit = (search) => {
  searchInfo = search
  pageInfo.pageNum = 1
  fetchTableData()
}
// 获取分页信息
const totalNum = ref(20)
const pageInfo = reactive({
  pageSize: 10,
  pageNum: 1
})
// 页面切换请求数据
const getPageInfo = (pageNum) => {
  pageInfo.pageNum = pageNum
  const data = handleData()
  console.log('查询参数', data)
  fetchTableData()
}
</script>

<style lang="scss" scoped>
.table-container {
  background: #ffffff;
  border-radius: 11px;
  margin-top: 17px;
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
