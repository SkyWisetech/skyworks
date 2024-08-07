<template>
  <div>
    <HeaderPages />
    <!-- 内容 -->
    <main>
      <div class="content-box">
        <div class="grid-container">
          <div v-if="isLoading" class="loading">拼命加载中...</div>
          <!-- 正文区域 -->
          <div v-else class="personal-container">
            <!-- 标签 -->
            <div>
              <div class="tab">
                <div @click="onTab(0)" :class="{ active: currTab.person }">
                  个人信息
                </div>
                <div @click="onTab(1)" :class="{ active: currTab.collect }">
                  收藏({{ showNum.collectNum }})
                </div>
                <div @click="onTab(2)" :class="{ active: currTab.remark }">
                  评论({{ showNum.commentNum }})
                </div>
              </div>
            </div>
            <!-- 内容 -->
            <div class="content">
              <div v-if="contentLoading" class="loading">拼命加载中...</div>
              <!-- 个人信息 -->
              <Person
                @onFinish="getPersonInfo"
                :personProps="personObj"
                v-if="currTab.person"
              />
              <div v-if="currTab.collect">
                <Collect @changeCollect="change" v-model="contentLoading" />
              </div>
              <Comment
                @changeComment="change"
                v-model="contentLoading"
                v-if="currTab.remark"
              />
            </div>
          </div>
        </div>
      </div>
    </main>
    <FooterPages />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import Person from './components/person.vue'
import Collect from './components/collect.vue'
import Comment from './components/comment.vue'
import { getPerson } from '@/api/personal'
const currTab = reactive({
  person: true,
  collect: false,
  remark: false
})
let isLoading = ref(false)
let contentLoading = ref(false)
// 获取用户信息
const showNum = reactive({
  collectNum: 0,
  commentNum: 0
})
let personObj = reactive({})
onMounted(() => {
  getPersonInfo()
})
// 获取个人信息
const getPersonInfo = async () => {
  isLoading.value = true
  const res = await getPerson()
  const { data, code } = res.data
  console.log('获取用户信息', data)
  isLoading.value = false
  if (code == 200) {
    const { collectNum, commentNum } = data
    showNum.collectNum = collectNum
    showNum.commentNum = commentNum
    personObj = data
  }
}
// 修改展示收藏，点赞数据
const change = (key, value) => {
  showNum[key] = value
}

// 点击Tab
const onTab = (index) => {
  Object.keys(currTab).forEach((i, m) => {
    if (m == index) {
      currTab[i] = true
    } else {
      currTab[i] = false
    }
  })
}
</script>

<style lang="scss" scoped>
.active {
  color: #333;
}
.content-box {
  display: flex;
  justify-content: center;
  box-sizing: border-box;

  .grid-container {
    margin-top: 20px;
    width: 1200px;
    padding: 0;

    .loading {
      display: flex;
      justify-content: center;
      color: #ccc;
    }
    .personal-container {
      display: flex;
      justify-content: space-around;
      .tab {
        color: #999;
        cursor: pointer;
        padding: 16px 12px;
        background: #ffffff;
        border-radius: 11px;
        width: 100%;
        min-height: 120px;
        text-align: center;
        display: flex;
        flex-direction: column;
        justify-content: space-around;
      }
      .content {
        width: 80%;
        min-height: 217px;
        padding: 23px;
        background: #ffffff;
        border-radius: 11px;
        .personal {
          padding-top: 11px;
        }
      }
    }
  }
}
</style>
