<!--
 * new page
 * @since: 2024-07-23
 * collect.vue
-->
<template>
  <div class="container">
    <el-empty v-if="collectList?.length == 0" description="还没有收藏文章" />

    <div v-for="item in collectList" :key="item.id" class="artical">
      <div @click="onDetail(item.id)">{{ item.title || '--' }}</div>
      <span v-if="isDeling && currId == item.id">取消中..</span>
      <span v-else @click="unCollect(item.id)">取消收藏</span>
    </div>
    <Pagination @getPageInfo="getPages" :totalProp="totalCount" />
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { onMounted, ref, reactive } from 'vue'
import Pagination from './pagination.vue'
import { delCollect, getCollectList } from '@/api/personal'
let isDeling = ref(false)
let currId = ref(null)
let totalCount = ref(0)
let pageInfo = reactive({ pageNum: 1, pageSize: 2 })
let collectList = ref([])
const router = useRouter()
defineProps({
  modelValue: Boolean
})
const emit = defineEmits(['update:modelValue', 'changeCollect'])
// 获取评论列表
onMounted(() => {
  getList()
})
// 获取列表
const getList = async () => {
  const timer = setTimeout(() => {
    emit('update:modelValue', true)
  }, 1000)
  const res = await getCollectList(pageInfo)
  clearTimeout(timer)
  const { data, code } = res.data
  console.log('获取评论列表返回值', data, code)
  collectList.value = data.articleList
  totalCount.value = data.totalCount
  emit('update:modelValue', false)
}
// 取消收藏
const unCollect = async (id) => {
  isDeling.value = true
  currId.value = id
  const res = await delCollect(id)
  const { code } = res.data
  isDeling.value = false
  if (code == 200) {
    getList()
    totalCount.value--
    emit('changeCollect', 'collectNum', totalCount.value)
  }
}

const getPages = (pages) => {
  console.log('获取评论分页信息', pages)
  pageInfo.pageNum = pages
  getList()
}
const onDetail = (id) => {
  // 页面跳转
  router.push({
    name: 'articledetails',
    query: {
      id
    }
  })
}
</script>

<style lang="scss" scoped>
.artical {
  cursor: pointer;
  color: #333;
  padding-bottom: 20px;
  padding-right: 30px;
  margin-bottom: 20px;
  border-bottom: 1px solid #eee;
  span:nth-child(2) {
    margin: 0 10px;
  }
  > span {
    float: right;
    color: #999;
    font-size: 12px;
  }
  .imgs {
    float: right;
    width: 18px;
    height: 18px;
  }
}
</style>
