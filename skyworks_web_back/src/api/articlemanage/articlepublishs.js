import request from '@/utils/request.js'
//上传图片
export const uploadImage = (image) =>
  request.post('/util/image/uploadImage', image)

// 发布文章
export const insertArticle = ({ title, content, imageName, label }) =>
  request.post('/article/insertArticle', { title, content, imageName, label })
// 查询后台首页
export const layoutList = () => request.post('/dailyData/listDailyData')

// 查询标签
export const LabelClassification = () =>
  request.post('/label/listLabel', { data: {} })
// 查询文章详情
export const articleDetailList = (articleId) =>
  request.post('/article/getArticleById', articleId)
