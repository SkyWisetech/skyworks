<script setup>
import { ref, onMounted, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { listArticle, collectApi, likeApi } from '@/api/home/homearticle'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/modules/user'
const { token } = useUserStore()

import { useCounterStore } from '@/stores/modules/homeList'
const counterStore = useCounterStore()
import _ from 'lodash'
// 首页文章列表
const list = ref({
  title: '',
  pageNum: '1',
  pageSize: '10'
})
const showEndOfContent = ref(false) //触底文字
const totalPage = ref(0) //用于存储总页数
const total = ref(0) // 用于存储文章总数
const listarticles = ref([]) //文章列表
const isLoading = ref(false)
const temp = ref(false) //加载中样式
const dearchValue = ref({})
const dearchShow = ref(0) //判断pinia里存储文章列表有无变化 有1无0
// 初始加载数据
const articlelist = async () => {
  if (isLoading.value) return
  isLoading.value = true
  temp.value = true
  const {
    data: { data }
  } = await listArticle(list.value)
  // console.log(data)
  //监听Pinia数据有无变化
  if (dearchShow.value) {
    listarticles.value.value = dearchValue.value
  } else {
    listarticles.value = [...listarticles.value, ...data.articleList]
  }

  total.value = data.totalCount
  totalPage.value = data.totalPage
  list.value.pageNum++ //移动到下一页
  temp.value = false
  isLoading.value = false
}
//查看文章详情
const router = useRouter()
const toArticleDetails = (id) => {
  if (localStorage.getItem('token')) {
    router.push({
      path: '/articledetails',
      query: {
        id
      }
    })
  } else {
    ElMessage.warning('请先登录后再查看文章')
  }
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
    // console.log('已经触底，执行函数')
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

//点赞 ❥
const onLike = async (id, state) => {
  if (!token) {
    ElMessage.warning('请先登陆再点赞哦')
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
  // console.log('data---点赞接口', data)
}
//收藏
const onCollect = async (id, state) => {
  if (!token) {
    ElMessage.warning('请先登陆再收藏哦')
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

watch(
  () => counterStore.homeList,
  (newValue) => {
    if (newValue) {
      listarticles.value = newValue || {}
      dearchShow.value = 1
      // console.log(newValue)
      // console.log(oldValue)
      // } else {
    }
    console.log(newValue)
    // console.log(oldValue)
  }
)

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
  <main style="background: #fff">
    <div v-if="isLoading" class="loading">拼命加载中...</div>
    <div v-masonry class="masonry" v-if="listarticles.length > 0">
      <div
        v-masonry-tile
        class="masonry-tile"
        v-for="(item, index) in listarticles"
        @click="toArticleDetails(item.id)"
        :key="index"
        :style="{
          width: `${100 / 4}%`
        }"
      >
        <div class="masonry-content" v-if="item.imageName">
          <div v-if="item.labelName" class="tags">
            {{ item.labelName }}
          </div>
          <img
            class="masonry-img"
            :src="counterStore.baseURL + item.imageName"
            alt=""
          />
          <div class="masonry-titleContent">
            <span class="listTitle">{{
              item.title ? item.title : '标题为空~'
            }}</span>
            <p class="masonry-time">
              {{ item.updateTime }}
            </p>
            <div class="collect-comment">
              <!-- 点赞 -->
              <div
                @click.stop="onLike(item.id, item.isUpvoted)"
                class="icons jump"
              >
                <div v-if="item.isUpvoted">
                  <img
                    v-if="index % 4 == 0"
                    src="@/assets/main/like_active.png"
                    class="imgs"
                  />
                  <!-- 蓝色 -->
                  <img
                    v-else-if="index % 4 == 1"
                    src="@/assets/main/组 441@2x.png"
                    class="imgs"
                  />
                  <!-- 黄色 -->
                  <img
                    v-else-if="index % 4 == 2"
                    src="@/assets/main/组 453@2x.png"
                    class="imgs"
                  />
                  <!-- 粉色 -->
                  <img
                    v-else-if="index % 4 == 3"
                    src="@/assets/main/组 449@2x.png"
                    class="imgs"
                  />
                  <!-- 绿色 -->
                  <!-- <img
                      v-else-if="index % 4 == 0"
                      src="@/assets/main/组 445@2x.png"
                      @click="cilckColor(index)"
                      class="imgs"
                    /> -->
                  <img v-else src="@/assets/main/like.png" class="imgs" />
                </div>
                <img v-else src="@/assets/main/like.png" class="imgs" />
                <span> {{ item.upvoteNum }}</span>
              </div>
              <!-- 收藏 -->
              <div
                @click.stop="onCollect(item.id, item.isCollected)"
                class="icons jump"
              >
                <div v-if="item.isCollected">
                  <img
                    src="@/assets/main/collect.png"
                    class="imgs"
                    v-if="index % 4 == 0"
                  />
                  <!-- 蓝色 -->
                  <img
                    src="@/assets/main/组 440@2x (1).png"
                    class="imgs"
                    v-if="index % 4 == 1"
                  />
                  <!-- 黄色 -->
                  <img
                    src="@/assets/main/组 452@2x.png"
                    class="imgs"
                    v-if="index % 4 == 2"
                  />
                  <!-- 粉色 -->
                  <img
                    src="@/assets/main/组 448@2x.png"
                    class="imgs"
                    v-if="index % 4 == 3"
                  />
                </div>
                <img v-else src="@/assets/main/collect1.png" class="imgs" />
                <span> {{ item.collectNum }}</span>
              </div>
              <div @click="toCommentPage" class="icons jump">
                <img src="@/assets/main/chat.png" class="imgs" />
                <span> {{ item.commentNum }}</span>
              </div>
              <div class="icons jump">
                <img src="@/assets/main/组 436@2x.png" class="imgs" />
                <span> {{ item.viewNum }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div
      class="content-box"
      v-else
      style="height: calc(100vh - 628px); display: flex; align-items: center"
    >
      暂无数据...
    </div>
  </main>
  <!-- loading -->
  <div class="bt-loading" v-if="temp" v-loading="temp">拼命加载中...</div>
  <div class="end-of-content" v-if="showEndOfContent">
    文章已加载完，到底了~
  </div>
  <!-- 底部 -->
  <FooterPages style="background: #fff"></FooterPages>
  <el-backtop :bottom="100">
    <div
      class="backtop"
      style="
        background-color: var(--el-bg-color-overlay);
        box-shadow: var(--el-box-shadow-lighter);
        text-align: center;
        color: #1989fa;
      "
    >
      <el-icon><CaretTop /></el-icon>
      <!-- 回到顶部 -->
    </div>
  </el-backtop>
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
  margin-bottom: 20px;
  min-height: calc(100vh - 560px);
}
.grid-item:nth-child(4n + 1) {
  border: 4px solid #7554e6;
  .tags {
    background: #7554e6;
  }
}
.grid-item:nth-child(4n + 2) {
  border: 4px solid #4b87f7;
  .tags {
    background: #4b87f7;
  }
}
.grid-item:nth-child(4n + 3) {
  border: 4px solid #ffaa1f;
  .tags {
    background: #ffaa1f;
  }
}
.grid-item:nth-child(4n + 4) {
  border: 4px solid #ff79b9;
  .tags {
    background: #ff79b9;
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

.end-of-content {
  color: #ccc;
  display: flex;
  justify-content: center;
  width: 100%;
  margin-top: 50px;
  margin-bottom: 40px;
}
.backtop {
  font-size: 45px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  .el-icon {
    top: -2px;
  }
}

.masonry {
  width: 1150px;
  margin: 12px auto;
  .masonry-tile {
    display: flex;
    flex-direction: column;
    width: 22% !important;
    margin: 10px;
    border-radius: 14px 14px 14px 14px;
    cursor: pointer;
    .masonry-content {
      min-height: 120px;
      .masonry-titleContent {
        padding: 15px 8px 8px;
        .listTitle {
          font-size: 16px;
          font-weight: 500;
          color: #333333;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 3;
          -webkit-box-orient: vertical;
        }
        .masonry-time {
          font-size: 13px;
          color: #999999;
        }
      }
      .masonry-img {
        width: 100%;
        border-radius: 10px 10px 0 0;
      }
      .tags {
        position: absolute;
        top: 10px;
        left: 10px;
        height: 24px;
        color: #fff;
        font-size: 14px;
        text-align: center;
        border-radius: 4px;
        font-weight: 400;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 0 5px;
      }
      .collect-comment {
        display: flex;
        .jump {
          cursor: pointer;
          display: flex;
          align-items: center;
          img {
            margin-right: 3px;
          }
        }

        .icons {
          margin-right: 12px;
        }

        .imgs {
          height: 17px;
          width: 17px;
        }
      }
    }
  }
  .masonry-tile:nth-child(4n + 1) {
    border: 4px solid #7554e6;
    .tags {
      background: #7554e6;
    }
  }
  .masonry-tile:nth-child(4n + 2) {
    border: 4px solid #4b87f7;
    .tags {
      background: #4b87f7;
    }
  }
  .masonry-tile:nth-child(4n + 3) {
    border: 4px solid #ffaa1f;
    .tags {
      background: #ffaa1f;
    }
  }
  .masonry-tile:nth-child(4n + 4) {
    border: 4px solid #ff79b9;
    .tags {
      background: #ff79b9;
    }
  }
}
/* 移动端 100-500px */
@media screen and (min-width: 100px) and (max-width: 500px) {
  .masonry {
    width: 100%;
    margin: 0.75rem auto;
    // margin-left: 0.6rem;
    .masonry-tile {
      display: flex;
      flex-direction: column;
      width: 44% !important;
      margin: 0.525rem 0.525rem 0.525rem 0.825rem;
      border-radius: 0.875rem;
      // background: #4b87f7;
      cursor: pointer;
      .masonry-content {
        min-height: 7.5rem;
        .masonry-titleContent {
          padding: 0.9375rem 0.5rem 0.5rem;
          .listTitle {
            font-size: 1rem;
          }
          .masonry-time {
            font-size: 0.8125rem;
          }
        }
        .masonry-img {
          width: 100%;
          border-radius: 0.625rem 0.625rem 0 0;
        }
        .tags {
          position: absolute;
          top: 0.625rem;
          left: 0.625rem;
          height: 1.5rem;
          font-size: 0.875rem;
          border-radius: 0.25rem;
          font-weight: 400;
          padding: 0 0.3125rem;
        }
        .collect-comment {
          display: flex;
          .jump {
            img {
              margin-right: 0.1875rem;
            }
          }

          .icons {
            margin-right: 0.75rem;
          }

          .imgs {
            height: 1.0625rem;
            width: 1.0625rem;
          }
        }
      }
    }
    .masonry-tile:nth-child(4n + 1) {
      border: 0.25rem solid #7554e6;
    }
    .masonry-tile:nth-child(4n + 2) {
      border: 0.25rem solid #4b87f7;
    }
    .masonry-tile:nth-child(4n + 3) {
      border: 0.25rem solid #ffaa1f;
    }
    .masonry-tile:nth-child(4n + 4) {
      border: 0.25rem solid #ff79b9;
    }
  }
  .backtop {
    font-size: 2.8125rem;
    border-radius: 50%;
    right: 1rem;

    .el-icon {
      top: -2px;
      height: 1.1em;
      width: 1.1em;
    }
  }

  .content-box {
    min-height: calc(100vh - 45rem);
  }
}
/* 移动端 100-500px */
@media screen and (min-width: 100px) and (max-width: 376px) {
  .masonry-tile {
    margin: 0.525rem 0.525rem 0.525rem 0.725rem !important;
    // background: pink;
  }
}
</style>
