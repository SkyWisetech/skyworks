<script setup>
import * as echarts from 'echarts'
import { onMounted, ref } from 'vue'
import { homeList, getTotalCount } from '@/api/home/home'
//声明周期函数，自动执行初始化
onMounted(() => {
  fetchData()
  getTotalCounts()
  // init()
})
//图表初始化函数
function init() {
  // 基于准备好的dom，初始化echarts实例
  let Chart = echarts.init(document.getElementById('main'))
  // 绘制图表
  const options = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      orient: 'horizontal', // 保持水平（这也是默认值）
      top: '90%',
      left: 'center', // 尝试居中对齐
      data: [
        {
          name: '日增文章数量',
          icon: 'circle'
        },
        {
          name: '日增点赞数量',
          icon: 'circle'
        },
        {
          name: '日增评论数量',
          icon: 'circle'
        },
        {
          name: '日增用户数量'
        }
      ],
      textStyle: {
        color: '#CACACA' // 设置 legend 文本的颜色
      }
    },
    xAxis: {
      type: 'category',
      data: dailyDateLisrt.value,
      axisLabel: {
        color: '#CACACA ' // 设置字体颜色
      }
    },
    yAxis: {
      type: 'value',
      min: 0,
      // max: 400,
      // interval: 100,
      axisLabel: {
        color: '#CACACA ' // 设置字体颜色
      }
    },
    series: [
      {
        name: '日增文章数量',
        type: 'bar',
        barWidth: '10',
        itemStyle: {
          color: '#2935f5', // 设置颜色
          barBorderRadius: [10, 10, 0, 0]
        },
        tooltip: {
          valueFormatter: function (value) {
            return value
          }
        },
        data: newArticleLisrt.value
      },
      {
        name: '日增点赞数量',
        type: 'bar',
        barWidth: '10',
        itemStyle: {
          color: '#4e89f7',
          barBorderRadius: [10, 10, 0, 0]
        },
        tooltip: {
          valueFormatter: function (value) {
            return value
          }
        },
        data: newUpvoteLisrt.value
      },
      {
        name: '日增评论数量',
        type: 'bar',
        barWidth: '10',
        itemStyle: {
          color: '#74defc',
          barBorderRadius: [10, 10, 0, 0]
        },
        tooltip: {
          valueFormatter: function (value) {
            return value
          }
        },
        data: newCommentLisrt.value
      },
      {
        name: '日增用户数量',
        type: 'line',
        tooltip: {
          valueFormatter: function (value) {
            return value
          }
        },
        // 设置线条样式
        lineStyle: {
          color: '#74defc',
          type: 'dashed' // 线条类型为虚线
        },
        data: newUserLisrt.value,
        symbol: 'circle', // 折线图的标记形状
        smooth: true // 是否平滑曲线显示
      }
    ]
  }

  // 渲染图表
  options && Chart.setOption(options)
}
// 首页头
const getTotalList = ref([])
const getTotalCounts = async () => {
  const res = await getTotalCount()
  getTotalList.value = res.data.data
}
// 图表/表格数据
const newArticleLisrt = ref([])
const newUpvoteLisrt = ref([])
const newCommentLisrt = ref([])
const newUserLisrt = ref([])
const dailyDateLisrt = ref([])
const tableData = ref([])

const fetchData = async () => {
  const res = await homeList()
  tableData.value = res.data.data
  console.log(res, '333')
  tableData.value.forEach((item) => {
    if (item.newArticle) newArticleLisrt.value.push(item.newArticle)
    if (item.newUpvote) newUpvoteLisrt.value.push(item.newUpvote)
    if (item.newComment) newCommentLisrt.value.push(item.newComment)
    if (item.newUser) newUserLisrt.value.push(item.newUser)
    if (item.dailyDate) dailyDateLisrt.value.push(item.dailyDate)
  })

  init()
}
</script>
<template>
  <div class="home">
    <header class="header">
      <el-row :gutter="25">
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="content">
              <div>
                <span>文章总数量</span>
                <h2>{{ getTotalList.articleTotalNum }}</h2>
              </div>
              <div class="images">
                <img src="@/assets/home/articletotal.png" class="logo-img" />
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="content">
              <div>
                <span>点赞总数量</span>
                <h2>{{ getTotalList.upvoteTotalNum }}</h2>
              </div>
              <div class="images">
                <img src="@/assets/home/upvotetotal.png" class="logo-img" />
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="content">
              <div>
                <span>评论总数量</span>
                <h2>{{ getTotalList.commentTotalNum }}</h2>
              </div>
              <div class="images">
                <img src="@/assets/home/commenttotal.png" class="logo-img" />
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card shadow="hover">
            <div class="content">
              <div>
                <span>用户总数量</span>
                <h2>{{ getTotalList.userTotalNum }}</h2>
              </div>
              <div class="images">
                <img src="@/assets/home/usertotal.png" class="logo-img" />
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </header>

    <main>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card shadow="always" class="myechartscard">
            <div id="main"></div>
          </el-card>
        </el-col>
      </el-row>
    </main>
    <footer>
      <el-row :gutter="20">
        <el-col :span="24">
          <el-card shadow="always" class="mycard">
            <el-table
              :data="tableData"
              style="width: 100%"
              :default-sort="{ prop: 'createTime', order: 'ascending' }"
            >
              <el-table-column
                prop="dailyDate"
                label="日期"
                width="210"
                align="center"
              />
              <el-table-column
                prop="newArticle"
                label="日增文章数量"
                width="210"
                align="center"
              />
              <el-table-column
                prop="newUpvote"
                label="日增点赞数量"
                width="210"
                align="center"
              />
              <el-table-column
                prop="newComment"
                label="日增评论数量"
                width="210"
                align="center"
              />
              <el-table-column
                width="210"
                prop="newUser"
                label="日增用户数量"
                align="center"
              />
            </el-table>
          </el-card>
        </el-col>
      </el-row>
    </footer>
  </div>
</template>

<style lang="scss" scoped>
.home {
  height: 80vh;
  width: 100%;

  .header {
    ::v-deep(.el-card) {
      margin-bottom: 30px;
      border-radius: 10px;
    }

    .content {
      display: flex;
      justify-content: space-around;
      align-items: center;
      .images {
        background-color: aqua;
        height: 90px;
        width: 90px;
        border-radius: 25px;
        .logo-img {
          width: 100%;
          height: 100%;
        }
      }
    }
  }

  .myechartscard {
    width: 100%;
    height: 100%;
    border-radius: 10px;

    #main {
      height: 300px;
      width: 100%;
      top: -28px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  .mycard {
    width: 100%;
    width: 100%;
    margin: 30px 0 0 0;
    border-radius: 10px;
  }
}
</style>
