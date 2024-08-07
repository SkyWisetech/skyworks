import request from '/src/utils/request.js'

export const userLoginService = ({ userName, password }) =>
  request.post('/user/adminLogin', { userName, password })
