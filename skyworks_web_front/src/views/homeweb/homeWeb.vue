<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { listArticle, collectApi, likeApi } from '@/api/home/homearticle'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/modules/user'
const { token } = useUserStore()
import _ from 'lodash'
// 首页文章列表
const list = ref({
  title: '',
  pageNum: '1',
  pageSize: '10'
})
const showEndOfContent = ref(false)
const totalPage = ref(0) //用于存储总页数
const total = ref(0) // 用于存储文章总数
const listarticles = ref([])
const isLoading = ref(false)
const temp = ref(false)
const articlelist = async () => {
  if (isLoading.value) return
  isLoading.value = true
  temp.value = true
  const {
    data: { data }
  } = await listArticle(list.value)
  console.log(data, '首页')
  listarticles.value = [...listarticles.value, ...data.articleList]
  total.value = data.totalCount
  totalPage.value = data.totalPage
  list.value.pageNum++ //移动到下一页
  temp.value = false
  isLoading.value = false

  console.log('listarticles', listarticles.value, listarticles.value.length)
}
const router = useRouter()
const toArticleDetails = (id) => {
  router.push({
    path: '/articledetails',
    query: {
      id
    }
  })
}
// 触底加载
const handleScroll = () => {
  // 检查用户是否滚动到页面底部
  const scrollTop = window.scrollY // 当前滚动条位置
  const windowHeight = window.innerHeight // 窗口可视区域高度
  const documentHeight = document.documentElement.scrollHeight // 整个文档的高度

  // 计算滚动条底部位置
  const scrolledToBottom = scrollTop + windowHeight >= documentHeight

  if (scrolledToBottom && list.value.pageNum <= totalPage.value) {
    // 当用户到达页面底部时，执行相应的操作
    console.log('已经触底，执行函数')
    articlelist().then(() => {
      // 检查是否加载了所有页面
      if (list.value.pageNum > totalPage.value) {
        showEndOfContent.value = true
      }
    })
  }
}
// 首页搜索
const searchListArticleData = async () => {
  list.value.pageNum = 1
  list.value.pageSize = 100
  const {
    data: { data }
  } = await listArticle(list.value)
  console.log(data, '首页标题模糊查询')
  setTimeout(() => {
    listarticles.value = data.articleList
    total.value = data.totalCount
  }, 1000)
}

const onLike = async (id, state) => {
  if (!token) {
    ElMessage.error('请先登陆再点赞哦')
    return
  }
  // 点赞接口
  const { data } = await likeApi({ articleId: id, isUpvote: !state })
  if (data.code == 200) {
    const newData = []
    listarticles.value.forEach((artical, index) => {
      if (artical.id == id) {
        artical.isUpvoted ? artical.upvoteNum-- : artical.upvoteNum++
        artical.isUpvoted = !artical.isUpvoted
        newData[index] = artical
      } else {
        newData.push(artical)
      }
    })
    listarticles.value = newData
  }
  console.log('data---点赞接口', data)
}
const onCollect = async (id, state) => {
  if (!token) {
    ElMessage.error('请先登陆再收藏哦')
    return
  }
  // 收藏接口
  const { data } = await collectApi({ articleId: id, isCollect: !state })
  if (data.code == 200) {
    const newData = []
    listarticles.value.forEach((artical, index) => {
      if (artical.id == id) {
        artical.isCollected ? artical.collectNum-- : artical.collectNum++
        artical.isCollected = !artical.isCollected
        newData[index] = artical
      } else {
        newData.push(artical)
      }
    })
    listarticles.value = newData
  }
}

const handleTitleUpdate = (newTitle) => {
  list.value.title = newTitle
  // 可以在这里调用
  searchListArticleData()
}
onMounted(() => {
  window.addEventListener('scroll', _.throttle(handleScroll, 200))
  // 初始加载数据
  articlelist()
})
</script>
<template>
  <!-- 头部 -->
  <HeaderPages
    :title="list.title"
    @update:title="handleTitleUpdate"
  ></HeaderPages>
  <!-- 内容 -->
  <main>
    <div v-if="isLoading" class="loading">拼命加载中...</div>
    <div class="content-box">
      <div class="grid-container">
        <div
          class="grid-item"
          v-for="item in listarticles"
          :key="item.id"
          @click="toArticleDetails(item.id)"
        >
          <div class="grid-content">
            <!-- <el-image
              v-if="item.imageName"
              :src="`http://192.168.1.5:8080/skyworks` + item.imageName"
              class="image"
              fit="fill"
              lazy="true"
            /> -->
            <el-image
              v-if="item.imageName"
              :src="`http://207.148.115.202:80/skyworks` + item.imageName"
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
                <div
                  @click.stop="onLike(item.id, item.isUpvoted)"
                  class="icons jump"
                >
                  <img
                    v-if="item.isUpvoted"
                    src="@/assets/main/like_active.png"
                    class="imgs"
                  />
                  <img v-else src="@/assets/main/like.png" class="imgs" />
                  <span> {{ item.upvoteNum }}</span>
                </div>
                <div
                  @click.stop="onCollect(item.id, item.isCollected)"
                  class="icons jump"
                >
                  <img
                    v-if="item.isCollected"
                    src="@/assets/main/collect.png"
                    class="imgs"
                  />
                  <img v-else src="@/assets/main/collect1.png" class="imgs" />
                  <span> {{ item.collectNum }}</span>
                </div>
                <div @click="toCommentPage" class="icons">
                  <img src="@/assets/main/chat.png" class="imgs" />
                  <span> {{ item.commentNum }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </main>
  <!-- loading -->
  <div class="bt-loading" v-if="temp" v-loading="temp">拼命加载中...</div>
  <div class="end-of-content" v-if="showEndOfContent">
    文章已加载完，到底了~
  </div>
  <!-- 底部 -->
  <FooterPages></FooterPages>
</template>

<style lang="scss" scoped>
.loading {
  // text-align: center;
  display: flex;
  justify-content: center;
  padding: 20px;
  color: #ccc;
}
.content-box {
  display: flex;
  justify-content: center;

  .grid-container {
    position: relative;
    margin-top: 20px;
    width: 1200px;
    column-count: 4;
    column-gap: 20px;
    padding: 0;
    box-sizing: border-box;

    .grid-item {
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
          .jump {
            cursor: pointer;
          }

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
// 触底加载底下的loding
.bt-loading {
  color: #ccc;
  display: flex;
  justify-content: center;
  width: 100%;
}
::v-deep(.el-loading-parent--relative) {
  display: flex;
  justify-content: center;
}
::v-deep(.el-loading-mask) {
  background-color: #f5f5f5;
}
.end-of-content {
  color: #ccc;
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 50px;
}
</style>
