import request from '/src/utils/request.js'

export const searchUser = (data) => request.post('/user/listUser', data)

export const updateUser = (data) => request.post('/user/updateUserInfo', data)
export const getUserCommentList = (data) =>
  request.post('comment/listComment', data)
export const delComment = (id) =>
  request.post('comment/deleteComment', { commentId: id })
