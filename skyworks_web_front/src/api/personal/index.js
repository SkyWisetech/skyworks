import request from '@/utils/request'

export const getPerson = () => request.post('user/getUser', {})
export const getCollectList = (pages) =>
  request.post('caffeine/collectArticleList', pages)
export const delCollect = (id) =>
  request.post('caffeine/collectArticle', { articleId: id, isCollect: false })

export const getCommentList = (pages) =>
  request.post('comment/listComment', pages)
export const delComment = (id) =>
  request.post('comment/deleteComment', { commentId: id })
// 更新用户信息
export const updateUser = (data) => request.post('user/updateUserInfo', data)
