<script setup>
import { ref, onMounted, watch } from 'vue'
import { listArticle } from '@/api/home/homearticle'
import { useRouter } from 'vue-router'
import { useCounterStore } from '@/stores/modules/homeList'
const counterStore = useCounterStore()
// 首页标题模糊查询
const list = ref({
  title: '',
  pageNum: '1',
  pageSize: '100'
})
const listarticles = ref([])
const isLoading = ref(false) // 加载状态
const articlelist = async () => {
  isLoading.value = true // 设置加载状态为true
  const {
    data: { code, data, msg }
  } = await listArticle(list.value)
  setTimeout(() => {
    listarticles.value = data
    console.log(data)
    isLoading.value = false // 数据加载完成后设置加载状态为false
  }, 1000) // 假设请求需要1秒

  console.log('****', code, data, msg)
}
const router = useRouter()
const toArticleDetails = (id) => {
  router.push({
    name: 'articledetails',
    params: {
      id: id
    }
  })
}

watch(
  () => router,
  (newValue, oldValue) => {
    if (newValue) {
      // } else {
    }
    console.log(newValue)
    console.log(oldValue)
  }
)

onMounted(() => {
  articlelist()
})
</script>
<template>
  <!-- 头部 -->
  <HeaderPages></HeaderPages>
  <!-- 内容 -->
  <main>
    <div class="content-box">
      <div class="grid-container">
        <div v-if="isLoading" class="loading">拼命加载中...</div>
        <div
          class="grid-item"
          v-for="item in listarticles"
          :key="item.id"
          @click="toArticleDetails(item.id)"
        >
          <!-- <RouterLink :to="`/articledetails/${item.id}`" class="router"> -->
          <div class="grid-content">
            <el-image
              v-if="item.imageName"
              :src="counterStore.baseURL + item.imageName"
              class="image"
              fit="fill"
              lazy="true"
            />
            <div class="tags">{{ item.labelName }}</div>
            <div style="padding: 5px">
              <span style="font-size: 16px">{{ item.title }}</span>
              <p style="font-size: 13px; color: #999999">
                {{ item.updateTime }}
              </p>
              <div class="collect-comment">
                <div class="icons">
                  <!-- <el-image src="../../assets/main/collect1.png" fit="fill" lazy="true" /> -->
                  <img src="@/assets/main/like.png" class="imgs" />
                  <span> {{ item.upvoteNum }}</span>
                </div>
                <div class="icons">
                  <img src="@/assets/main/collect1.png" class="imgs" />
                  <span> {{ item.collectNum }}</span>
                </div>
                <div class="icons">
                  <img src="@/assets/main/chat.png" class="imgs" />
                  <span> {{ item.commentNum }}</span>
                </div>
              </div>
            </div>
          </div>
          <!-- </RouterLink> -->
        </div>
        <!-- <el-button type="primary" @click="test">测试接口</el-button> -->
      </div>
    </div>
  </main>
  <!-- 底部 -->
  <FooterPages></FooterPages>
</template>

<style lang="scss" scoped>
.content-box {
  display: flex;
  justify-content: center;

  .grid-container {
    margin-top: 20px;
    width: 1200px;
    column-count: 4;
    column-gap: 20px;
    padding: 0;
    box-sizing: border-box;

    .loading {
      text-align: center;
      padding: 20px;
      color: #ccc;
    }

    .grid-item {
      // background-color: pink;
      border: 2px solid #7554e6;
      margin-bottom: 30px;
      break-inside: avoid;
      width: 100%;
      box-sizing: border-box;
      border-radius: 14px;

      // .router {
      // /* 或者，更具体地针对router-link */
      // .router-link-exact-active,
      // .router-link-active {
      //   text-decoration: none;
      //   color: inherit;
      //   /* 或者使用具体的颜色值 */
      // }
      text-decoration: none;
      color: inherit;

      /* 或者你想要的任何颜色 */
      .grid-content {
        position: relative;

        .image {
          width: 100%;
          border-radius: 12px 12px 0 0;
        }

        .tags {
          position: absolute;
          top: 3%;
          left: 3%;
          width: 76px;
          height: 24px;
          color: #fff;
          font-size: 14px;
          text-align: center;
          background-color: #7554e6;
          border-radius: 4px;
        }

        .collect-comment {
          display: flex;

          .icons {
            margin-right: 15px;
          }

          .imgs {
            height: 17px;
            width: 17px;
          }
        }
      }

      // }
    }
  }
}
</style>
