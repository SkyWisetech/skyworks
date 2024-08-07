import request from '@/utils/request.js'
// 首页标题模糊查询
export const listArticle = (data) => request.post('/article/listArticle', data)
// 文章标签详情
export const listArticleByLabel = (labelId) =>
  request.post('/article/listArticleByLabel', labelId)
// 获取排名前四的标签
export const recommendLabel = () => request.post('/caffeine/recommendLabel')

// 点赞接口
export const likeApi = (data) => request.post('/caffeine/upvoteArticle', data)
// 收藏接口
export const collectApi = (data) =>
  request.post('caffeine/collectArticle', data)
