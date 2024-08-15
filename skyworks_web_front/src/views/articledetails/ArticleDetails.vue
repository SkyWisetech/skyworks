<script setup>
import {
  articleDetailList,
  insertCommentApi,
  listArticleCommentApi
} from '@/api/articledetails/articledetails'
import { likeApi, collectApi } from '@/api/home/homearticle'
import { useRoute } from 'vue-router'
import '@wangeditor/editor/dist/css/style.css' // 引入 css
import { onBeforeUnmount, ref, shallowRef, onMounted } from 'vue'
import { Editor } from '@wangeditor/editor-for-vue'
import { useCounterStore } from '@/stores/modules/homeList'
import { ElMessage } from 'element-plus'
// const counterStore = useCounterStore()
const articleId = ref({
  articleId: ''
})
const route = useRoute()
articleId.value = route.query.id
const articleDetails = ref(null)
const isUpvoted = ref()
const upvoteNum = ref()
const isCollected = ref()
const collectNum = ref()
const commentNum = ref()

const detailList = async () => {
  const res = await articleDetailList({ articleId: articleId.value })
  articleDetails.value = res.data.data
  isUpvoted.value = articleDetails.value.isUpvoted
  isCollected.value = articleDetails.value.isCollected
  upvoteNum.value = articleDetails.value.upvoteNum
  collectNum.value = articleDetails.value.collectNum
  commentNum.value = articleDetails.value.commentNum
}

//发布一级评论与二级评论
const commentContent = ref('') //回复文章输入框
const commentContentTwoId = ref() //回复的输入框控制隐藏&评论id
const commentContentTwo = ref('') //回复评论输入框
const testsComment = async (v) => {
  let params = {
    articleId: articleDetails.value.id,
    parentId: v ? commentContentTwoId.value : 0, //有id传入回复评论id 无则传入回复文章的参数0
    // parentId: v ? articleDetails.value.parentLabelId : 0, //有id传入回复评论id 无则传入回复文章的参数0
    // content: commentContent.value
    content: v ? commentContentTwo.value : commentContent.value //有id则传入回复评论内容 无则传入回复文章评论
  }
  const res = await insertCommentApi(params)
  if (res.code == 200) {
    ElMessage.success(res.msg)
    commentContent.value = null
    articleComment()
    commentContentTwoId.value = null
    commentContentTwo.value = null
  }
}
//展开回复输入框
const testsCommentTwo = async (v) => {
  commentContentTwoId.value = v ? v : null
}

//获取文章评论
const listArticleComment = ref(null)
const articleComment = async () => {
  const listArticleComments = await listArticleCommentApi({
    articleId: articleId.value
  })
  if (listArticleComments.code == 200) {
    listArticleComment.value = listArticleComments.data.articleCommentList
  }
}

// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef()
// 内容 HTML
const valueHtml = ref('<p>hello</p>')

// 模拟 ajax 异步获取内容
onMounted(() => {
  setTimeout(() => {
    // valueHtml.value = '<p>模拟 Ajax 异步设置内容</p>'
  }, 1500)
})

const editorConfig = { placeholder: '请输入内容...' }

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value
  // editor.disable()
  if (editor == null) return
  // editor.destroy()
})

console.log(articleDetails.value)

const handleCreated = (editor) => {
  // detailList()
  // editorRef.value = editor // 记录 editor 实例，重要！
  editorRef.value = articleDetails.value.content
  valueHtml.value = articleDetails.value.content
  editor.disable()
}

//点赞 ❥
const onLike = async (id, state) => {
  const { data } = await likeApi({ articleId: id, isUpvote: !state })
  if (data.code == 200) {
    detailList()
  }
}
//收藏
const onCollect = async (id, state) => {
  const { data } = await collectApi({ articleId: id, isCollect: !state })
  if (data.code == 200) {
    detailList()
  }
}

onMounted(() => {
  detailList()
  articleComment()
})
</script>
<template>
  <!-- 头部 -->
  <HeaderPages></HeaderPages>
  <!-- 内容 -->
  <main>
    <div class="content-box">
      <div class="grid-container" v-if="articleDetails">
        <div class="title">
          {{ articleDetails.title }}
        </div>
        <div class="min-header">
          <div>
            <span style="margin-right: 20px; font-size: 14px; color: #666666">{{
              articleDetails.updateTime
            }}</span>
            <span class="tags">{{ articleDetails.labelName }}</span>
          </div>
          <div class="collect-comment">
            <!-- 点赞 -->
            <div class="viewNums">
              <img src="@/assets/main/组 436@2x.png" class="imgs" />
              <span> {{ articleDetails.viewNum }}</span>
            </div>
            <div class="icons" style="display: none">
              <img
                v-if="articleDetails.isUpvoted"
                src="@/assets/main/like_active.png"
                class="imgs"
                @click.stop="
                  onLike(articleDetails.id, articleDetails.isUpvoted)
                "
              />
              <img
                v-else
                src="@/assets/main/like.png"
                class="imgs"
                @click.stop="
                  onLike(articleDetails.id, articleDetails.isUpvoted)
                "
              />
              <span style="font-size: 14px">
                {{ articleDetails.upvoteNum }}</span
              >
            </div>
            <!-- 收藏 -->
            <div class="icons" style="display: none">
              <img
                v-if="articleDetails.isCollected"
                src="@/assets/main/collect.png"
                class="imgs"
                @click.stop="
                  onCollect(articleDetails.id, articleDetails.isCollected)
                "
              />
              <img
                v-else
                src="@/assets/main/collect1.png"
                class="imgs"
                @click.stop="
                  onCollect(articleDetails.id, articleDetails.isCollected)
                "
              />
              <span style="font-size: 14px">
                {{ articleDetails.collectNum }}</span
              >
            </div>
          </div>
        </div>
        <div class="image-content" style="display: none">
          <el-image
            :src="useCounterStore.baseURL + articleDetails.imageName"
            style="width: 100%; height: 100%"
          >
          </el-image>
        </div>
        <div class="font-content">
          <Editor
            style="height: auto; overflow-y: hidden"
            v-model="valueHtml"
            :defaultConfig="editorConfig"
            mode="default"
            @onCreated="handleCreated"
          />
        </div>
        <div class="comment">
          <img src="@/assets/main/chat.png" class="chatimgs" />
          <span>评论</span>
        </div>

        <el-input
          placeholder="发布评论"
          type="textarea"
          class="input"
          resize="none"
          :rows="5"
          v-model="commentContent"
        ></el-input>
        <div class="buttons-cpntainer">
          <el-button type="primary" class="buttons" @click="testsComment()"
            >发布</el-button
          >
        </div>
        <div
          class="username"
          v-for="item in listArticleComment"
          v-bind:key="item.index"
        >
          <div style="color: #333333; font-weight: 400">
            {{ item.userName }}
          </div>
          <p style="font-size: 15px; padding: 0 49px">
            {{ item.content }}
          </p>

          <div class="reply">
            <div>{{ item.createTime }}</div>
            <div class="replys" @click="testsCommentTwo(item.id)">回复</div>
          </div>

          <div
            v-if="item.childCommentList.length > 0"
            class="commentContentStyle"
          >
            <div
              v-for="items in item.childCommentList"
              v-bind:key="items.index"
            >
              <div style="color: #333333; font-weight: 400">
                {{ items.userName }}
              </div>
              <p style="font-size: 15px; padding: 0 49px">
                {{ items.content }}
              </p>
              <div class="reply">
                <div>{{ items.createTime }}</div>
              </div>
            </div>
          </div>
          <!-- 二级发布 -->
          <div class="commentContentTwo" v-if="commentContentTwoId == item.id">
            <el-input
              placeholder="回复评论"
              type="textarea"
              class="input"
              resize="none"
              :rows="3"
              v-model="commentContentTwo"
            ></el-input>
            <div>
              <el-button
                type="primary"
                class="commentBtn"
                @click="testsComment(item.id)"
                >回复</el-button
              >
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="article-suspended-panel">
      <div class="panel-btn">
        <img
          v-if="isUpvoted"
          src="@/assets/main/like_active.png"
          class="imgs"
          @click.stop="onLike(articleDetails.id, isUpvoted)"
        />
        <img
          @click.stop="onLike(articleDetails.id, isUpvoted)"
          v-else
          src="@/assets/main/like.png"
          class="imgs"
        />
        <div class="bubble" v-if="upvoteNum">{{ upvoteNum }}</div>
      </div>
      <div class="panel-btn with-badge">
        <img
          v-if="isCollected"
          src="@/assets/main/collect.png"
          class="imgs"
          @click.stop="onCollect(articleDetails.id, isCollected)"
        />
        <img
          v-else
          src="@/assets/main/collect1.png"
          class="imgs"
          @click.stop="onCollect(articleDetails.id, isCollected)"
        />
        <div v-if="collectNum" class="bubble">{{ collectNum }}</div>
      </div>
      <div class="panel-btn with-badge">
        <img src="@/assets/main/chat.png" class="imgs" />
        <div v-if="commentNum" class="bubble">{{ commentNum }}</div>
      </div>
    </div>
  </main>
  <!-- 底部 -->
  <FooterPages v-if="route.path != '/articledetails'"></FooterPages>
</template>
<style lang="scss" scoped>
.content-box {
  display: flex;
  justify-content: center;

  .grid-container {
    margin-top: 20px;
    width: 60%;
    box-sizing: border-box;
    box-shadow: 1px -1px 9px 2px rgba(0, 0, 0, 0.05);
    background: #fff;
    padding: 20px 32px;

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
          margin-left: 30px;
          cursor: pointer;
        }

        .imgs {
          height: 16px;
          width: 16px;
        }
        .viewNums {
          display: flex;
          align-items: center;
          span {
            margin-left: 4px;
            font-size: 14px;
          }
        }
      }

      .tags {
        font-size: 14px;
        display: inline-block;
        // width: 72px;
        height: 20px;
        color: #ffffff;
        background: #7554e6;
        border-radius: 4px 4px 4px 4px;
        text-align: center;
        font-family:
          Source Han Sans,
          Source Han Sans;
        padding: 2px 10px;
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
      margin: 20px 0;
    }

    .comment {
      border-bottom: 1px solid #eeeeee;
      /* 下边框 */
      border-left: none;
      /* 左边框，设置为无 */
      border-right: none;
      /* 右边框，设置为无 */
      padding: 10px 0;
      align-items: center;
      .chatimgs {
        height: 16px;
        width: 16px;
        margin-right: 5px;
        align-items: center;
        display: flex;
      }
    }

    .input {
      margin-top: 15px;
      width: 100%;
      // height: 100px;
      border-radius: 10px 10px 10px 10px;
      // border: 1px solid #cacaca;
    }
    .buttons-cpntainer {
      display: flex;
      justify-content: end;
      margin-top: 20px;
      .buttons {
        font-size: 16px;
        width: 87px;
        height: 41px;
        line-height: 41px;
        background: #7554e6;
        border-radius: 10px 10px 10px 10px;
      }
    }

    .username {
      border-bottom: 1px solid #eeeeee;
      /* 下边框 */
      border-left: none;
      border-right: none;
      padding: 10px 0;
      margin-top: 15px;

      .reply {
        font-size: 14px;
        display: flex;
        justify-content: space-between;
        color: #999999;
        padding: 0 49px;
      }
      .replys {
        cursor: pointer;
      }
      .commentContentStyle {
        padding-left: 50px;
        margin-top: 20px;
      }
    }
  }
}
.article-suspended-panel {
  position: fixed;
  margin-left: 12%;
  top: 440px;
  align-items: center;
  .panel-btn {
    position: relative;
    width: 40px;
    height: 40px;
    background-color: #fff;
    background-position: 50%;
    background-repeat: no-repeat;
    border-radius: 50%;
    box-shadow: 1px 1px 9px 3px rgb(50 50 50 / 12%);
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 1.67rem;
    .imgs {
      width: 22px;
      height: 22px;
    }
    .bubble {
      background: #7554e6;
      color: #fff;
      height: 14px;
      min-width: 27px;
      position: absolute;
      font-size: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      top: -1px;
      border-radius: 12px;
      left: 30px;
    }
  }
  .with-badge {
    margin-top: 20px;
  }
}
.commentContentTwo {
  div {
    width: 100%;
    margin-top: 20px;
    display: flex;
    justify-content: end;
  }
}
/* 移动端 100-500px */
@media screen and (min-width: 100px) and (max-width: 500px) {
  .content-box {
    .grid-container {
      margin-top: 1rem;
      width: 100%;
      box-shadow: 1px -1px 9px 2px rgba(0, 0, 0, 0.05);
      background: #fff;
      padding: 1.25rem 1rem 4rem;

      .title {
        font-size: 1.625rem;
      }

      .min-header {
        margin-top: 1.25rem;

        .collect-comment {
          display: flex;

          .icons {
            margin-left: 1.875rem;
          }

          .imgs {
            height: 0.8rem;
            width: 1rem;
          }
          .viewNums {
            span {
              margin-left: 0.25rem;
              font-size: 0.875rem;
            }
          }
        }

        .tags {
          font-size: 0.875rem;
          height: 1.25rem;
          border-radius: 0.25rem;
          padding: 0.125rem 0.625rem;
        }
      }

      .image-content {
        margin-top: 1.5625rem;
        width: 100%;
        height: 34.375rem;
      }

      .font-content {
        font-size: 0.9375rem;
        margin: 1.25rem 0;
      }

      .comment {
        padding: 0.625rem 0;
        align-items: center;
        display: flex;
        .chatimgs {
          height: 1rem;
          width: 1rem;
          margin-right: 0.3175rem;
        }
      }

      .input {
        margin-top: 0.9375rem;
        width: 100%;
        border-radius: 0.625rem;
      }
      .buttons-cpntainer {
        margin-top: 1.25rem;
        .buttons {
          font-size: 1rem;
          width: 5.4375rem;
          height: 2.5625rem;
          line-height: 2.5625rem;
          border-radius: 0.625rem;
        }
      }

      .username {
        border-bottom: 1px solid #eeeeee;
        padding: 0.625rem 0;
        margin-top: 0.9375rem;

        .reply {
          font-size: 0.875rem;
          padding: 0 3.0625rem;
        }

        .commentContentStyle {
          padding-left: 3.125rem;
          margin-top: 1.25rem;
        }
      }
    }
  }
  .article-suspended-panel {
    // position: fixed;
    background: #fff;
    width: 100%;
    height: 4rem;
    top: calc(100% - 4rem);
    bottom: 0;
    display: flex;
    margin-left: 0;
    justify-content: space-around;
    box-shadow: 1px 1px 9px 3px rgb(50 50 50 / 12%);
    .panel-btn {
      // width: 3.5rem;
      height: 2.5rem;
      box-shadow: none;
      font-size: 2rem;
      display: flex;
      justify-content: space-between;
      .imgs {
        width: 1.9rem;
        height: 1.9rem;
      }
      .bubble {
        min-width: 0.1rem;
        font-size: 1.6rem;
        top: 0.7rem;
        left: 2.475rem;
        background: #fff;
        color: #181414;
        max-width: 2rem;
      }
    }
    .with-badge {
      margin-top: 0;
    }
  }
}
</style>
