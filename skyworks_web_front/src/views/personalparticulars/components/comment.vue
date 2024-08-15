<!--
 * new page
 * @since: 2024-07-23
 * comment.vue
-->
<template>
  <div class="container">
    <el-empty v-if="commentList?.length == 0" description="还没有评论" />
    <div v-for="item in commentList" :key="item.id" class="comment">
      <div class="title">
        <p class="title-text">{{ item.title || '--' }}</p>
        <span class="title">{{ item.createTime }}</span>
      </div>

      <div class="del-text">
        <div>{{ item.content }}</div>
        <div>
          {{ item.commentContent }}
          <span v-if="isDeling && currId == item.id">删除中..</span>
          <el-popconfirm
            v-else
            title="确认删除此条评论？"
            confirm-button-text="是的"
            cancel-button-text="取消"
            @confirm="onDel(item.id)"
          >
            <template #reference>
              <span class="delSpan">删除评论</span>
            </template>
          </el-popconfirm>
        </div>
      </div>
    </div>
    <Pagination :totalProp="totalCount" @getPageInfo="getPages" />
  </div>
</template>

<script setup>
import { reactive, onMounted, ref } from 'vue'
import Pagination from './pagination.vue'
import { delComment, getCommentList } from '@/api/personal'
import { useCounterStore } from '@/stores/modules/homeList'
const userData = useCounterStore()
let { modelValue } = defineProps({
  modelValue: Boolean
})
const emit = defineEmits(['update:modelValue', 'changeComment'])
let commentList = ref([])
let isDeling = ref(false)
let currId = ref(null)
let totalCount = ref(0)
let pageInfo = reactive({
  pageNum: 1,
  pageSize: 10,
  userId: userData.userData.id
})
onMounted(async () => {
  // 获取评论列表
  getList()
})
// 查询列表
const getList = async () => {
  const timer = setTimeout(() => {
    emit('update:modelValue', true)
  }, 1000)
  const res = await getCommentList(pageInfo)
  clearTimeout(timer)
  const { data, code } = res.data
  console.log('获取评论列表返回值', data, code)
  commentList.value = data.commentList
  totalCount.value = data.totalCount
  modelValue && emit('update:modelValue', false)
}
const getPages = (pages) => {
  console.log('获取评论分页信息', pages)
  pageInfo.pageNum = pages
  getList()
}
const onDel = async (id) => {
  // 调用删除接口
  console.log('评论id', id)
  currId.value = id
  isDeling.value = true
  const res = await delComment(id)
  const { code } = res.data
  isDeling.value = false
  if (code == 200) {
    getList()
    totalCount.value--
    emit('changeComment', 'commentNum', totalCount.value)
  }
}
</script>

<style scoped lang="scss">
.comment {
  p {
    margin: 0;
  }
  border-bottom: 1px solid #eee;
  padding-top: 20px;
  padding-bottom: 10px;
  .title {
    display: flex;
    flex-direction: column;
    .title-text {
      margin: 0;
      margin-bottom: 4px;
      font-weight: 500;
      span {
        font-size: 16px;
      }
    }
  }
  span {
    color: #999;
    font-size: 12px;
  }
  .del-text {
    display: flex;
    justify-content: space-between;
    margin-top: 12px;
    .delSpan {
      cursor: pointer;
      font-size: 14px;
    }
    text-align: end;
  }
}
.comment:nth-child(1) {
  padding-top: 0;
}

/* 移动端 100-500px */
@media screen and (min-width: 100px) and (max-width: 500px) {
  .comment {
    padding-top: 1.25rem;
    padding-bottom: 0.625rem;
    .title {
      .title-text {
        margin-bottom: 0.25rem;

        span {
          font-size: 1rem;
        }
      }
    }
    span {
      font-size: 0.75rem;
    }
    .del-text {
      margin-top: 0.75rem;
      .delSpan {
        font-size: 0.875rem;
      }
    }
  }
}
</style>
