// 查询近7天新增数据
import request from '/src/utils/request.js'
export const homeList = () => request.post('/dailyData/listDailyData')
// 获取文章、点赞、评论、用户总量
export const getTotalCount = () => request.post('/caffeine/getTotalCount')
