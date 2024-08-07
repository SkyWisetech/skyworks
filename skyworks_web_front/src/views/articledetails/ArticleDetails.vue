<script setup>
import { articleDetailList } from '@/api/articledetails/articledetails'
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'

const articleId = ref({
  articleId: ''
})
const route = useRoute()
articleId.value = route.query.id
const articleDetails = ref(null)
const detailList = async () => {
  const res = await articleDetailList({ articleId: articleId.value })

  articleDetails.value = res.data.data
}
onMounted(() => {
  detailList()
})
</script>
<template>
  <!-- 头部 -->
  <HeaderPages></HeaderPages>
  <!-- 内容 -->
  <main>
    <div class="content-box">
      <div class="grid-container">
        <div v-if="articleDetails">
          <div class="title">
            {{ articleDetails.title }}
          </div>

          <div class="min-header">
            <div>
              <span
                style="margin-right: 20px; font-size: 14px; color: #666666"
                >{{ articleDetails.updateTime }}</span
              >
              <span class="tags">{{ articleDetails.labelName }}</span>
            </div>
            <div class="collect-comment">
              <div class="icons">
                <img src="@/assets/main/like.png" class="imgs" />
                <span style="font-size: 14px">
                  {{ articleDetails.upvoteNum }}</span
                >
              </div>

              <div class="icons">
                <img src="@/assets/main/collect1.png" class="imgs" />
                <span style="font-size: 14px">
                  {{ articleDetails.collectNum }}</span
                >
              </div>
            </div>
          </div>
          <div class="image-content">
            <el-image
              :src="
                `http://192.168.1.5:8080/skyworks` + articleDetails.imageName
              "
              style="width: 100%; height: 100%"
            >
              <!-- <el-image
              :src="
                `http://207.148.115.202:80/skyworks` + articleDetails.imageName
              "
              style="width: 100%; height: 100%"
            > -->
            </el-image>
          </div>
          <div class="font-content">
            <p>
              {{ articleDetails.content }}
            </p>
          </div>
          <div class="comment">
            <img src="@/assets/main/chat.png" class="chatimgs" />
            <span>评论</span>
          </div>
          <div>
            <el-input placeholder="发布评论" class="input"></el-input>
            <el-button type="primary" class="buttons" @click="tests"
              >发布</el-button
            >
          </div>
          <div class="username">
            <div style="color: #333333">用户名称</div>
            <p style="font-size: 15px; padding: 0 49px">
              置位置和角度可以显著提高扬声器的音质。虽然不同类型的扬声器需要独特的设置才能获得最佳的投置位置和角度可以显著提高扬声器的音质。虽然不同类型的扬声器需要独特的设置才能获得最佳的投置位置和角度可以显著提高扬声器的音质。虽然不同类型的扬声器需要独特的设置才能获得最佳的投
            </p>
            <div class="reply">
              <div>2023-12-21</div>
              <div>回复</div>
            </div>
          </div>
        </div>
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
    box-sizing: border-box;
    // background-color: pink;

    .title {
      font-family:
        Source Han Sans,
        Source Han Sans;
      font-weight: 500;
      font-size: 26px;
      color: #333333;
      // line-height: 58px;
      text-align: left;
      font-style: normal;
      text-transform: none;
    }

    .min-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-top: 20px;

      .collect-comment {
        display: flex;

        .icons {
          margin-left: 20px;
        }

        .imgs {
          height: 16px;
          width: 16px;
        }
      }

      .tags {
        font-size: 14px;
        display: inline-block;
        width: 72px;
        height: 20px;
        color: #ffffff;
        background: #7554e6;
        border-radius: 4px 4px 4px 4px;
        text-align: center;
      }
    }

    .image-content {
      margin-top: 25px;
      width: 1200px;
      height: 550px;
    }

    .font-content {
      color: #333333;
      font-size: 15px;
      font-family:
        Source Han Sans,
        Source Han Sans;
    }

    .comment {
      border-bottom: 2px solid #eeeeee;
      /* 下边框 */
      border-left: none;
      /* 左边框，设置为无 */
      border-right: none;
      /* 右边框，设置为无 */
      padding: 10px 0;

      .chatimgs {
        height: 16px;
        width: 16px;
        margin-right: 5px;
      }
    }

    .input {
      margin-top: 15px;
      width: 1200px;
      height: 100px;
      border-radius: 10px 10px 10px 10px;
      border: 1px solid #cacaca;
    }

    .buttons {
      font-size: 16px;
      width: 87px;
      height: 41px;
      background: #7554e6;
      border-radius: 10px 10px 10px 10px;
      margin: 16px 0 16px 1113px;
    }

    .username {
      border-bottom: 2px solid #eeeeee;
      /* 下边框 */
      border-left: none;
      /* 左边框，设置为无 */
      border-right: none;
      /* 右边框，设置为无 */
      padding: 10px 0;
      margin-top: 10px;

      .reply {
        font-size: 14px;
        display: flex;
        justify-content: space-between;
        color: #999999;
        padding: 0 49px;
      }
    }
  }
}
</style>
