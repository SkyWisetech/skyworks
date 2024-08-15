import request from '@/utils/request.js'

//文章详情查询
export const articleDetailList = (articleId) =>
  request.post('/article/getArticleById', articleId)

//获取文章评论
export const listArticleCommentApi = (articleId) =>
  request.post('comment/listArticleComment', articleId).then((res) => {
    return res.data
  })

//发布评论
export const insertCommentApi = (data) =>
  request.post('/comment/insertComment', data).then((res) => {
    return res.data
  })
