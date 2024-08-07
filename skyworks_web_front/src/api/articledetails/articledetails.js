import request from '@/utils/request.js'
export const articleDetailList = (articleId) =>
  request.post('/article/getArticleById', articleId)
