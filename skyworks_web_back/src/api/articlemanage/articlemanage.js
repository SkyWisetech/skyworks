// 文章查询
import request from '/src/utils/request.js'
export const listArticle = (data) => request.post('/article/listArticle', data)
// 编辑文章
export const updateArticle = (data) =>
  request.post('/article/updateArticle', data)
// 删除文章
export const deleteArticle = (articleId) =>
  request.post('/article/deleteArticle', articleId)
// 查看评论
export const viewComment = (data) => request.post('/comment/listComment', data)
// 删除评论
export const delComment = (articleId) =>
  request.post('/comment/deleteComment', articleId)
