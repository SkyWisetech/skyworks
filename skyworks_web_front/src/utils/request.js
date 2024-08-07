import axios from 'axios'
import { useUserStore } from '@/stores/modules/user'
import { ElMessage } from 'element-plus'
import router from '../router/index'
// const baseURL = 'http://192.168.1.5:8080/skyworks' //测试环境
const baseURL = 'http://207.148.115.202:80/skyworks' //线上环境

const instance = axios.create({
  // 1. 基地址，超时时间
  baseURL,
  timeout: 100000
})
// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    // console.log('请求拦截器', config)
    // 2. 携带token
    const userStore = useUserStore()
    if (userStore.token || localStorage.getItem('token')) {
      config.headers.token = userStore.token || localStorage.getItem('token')
    }
    return config
  },
  (err) => Promise.reject(err)
)
// 响应拦截器
instance.interceptors.response.use(
  (res) => {
    // console.log('响应拦截器', res)
    // 3. 处理业务失败
    //4. 摘取核心响应数据
    // 后端还没开发好，先瞎写
    if (res.data.code === 0 || res.data.code === 200) {
      return res
    } else if (res.data.status === 401) {
      // 5. 处理401错误 权限不足/token过期
      router.replace('/login')
    } else {
      // 处理业务失败，给错误提示，抛出错误
      // 后端还没开发好，先瞎写
      ElMessage.error(res.data.message || res.data.msg || '服务异常')
      return Promise.reject(res.data)
    }
  },
  (err) => {
    // 5. 处理401错误 权限不足/token过期
    if (err.response?.status === 401) {
      router.push('/login')
    }

    // 错误的默认提示，只给提示
    ElMessage.error(err.response.data.message || '服务异常')

    return Promise.reject(err)
  }
)

export default instance
