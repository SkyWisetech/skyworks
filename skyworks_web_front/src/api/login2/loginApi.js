import request from '@/utils/request.js'

// loginByVerifyCodeApi,/* 验证码登录 */
export const loginByVerifyCodeApi = ({ email, verifyCode }) =>
  request.post('/user/loginByVerifyCode', { email, verifyCode })
// sendVerifyCodeApi,  /* 发送邮箱验证码 */
export const sendVerifyCodeApi = ({ email }) =>
  request.post('/util/email/sendVerifyCode', { email })
// loginByPassword,/* 密码登录 */
export const loginByPassword = ({ email, password }) =>
  request.post('/user/loginByPassword', { email, password })
// registerApi,/* 注册 */
export const registerApi = ({
  nickName,
  email,
  verifyCode,
  password,
  verifyPassword
}) =>
  request.post('/user/register', {
    nickName,
    email,
    verifyCode,
    password,
    verifyPassword
  })
// updatePasswordApi,/* 重置密码 */
export const updatePasswordApi = ({
  email,
  verifyCode,
  password,
  verifyPassword
}) =>
  request.post('/user/updatePassword', {
    email,
    verifyCode,
    password,
    verifyPassword
  })
