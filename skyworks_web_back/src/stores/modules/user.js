import { defineStore } from 'pinia'
import { ref } from 'vue'
// 用户模块 token setToken removeToken
export const useUserStore = defineStore(
  'big-user',
  () => {
    const token = ref('') //定义token
    const setToken = (newToken) => {
      token.value = newToken
    }
    const removeToken = () => {
      token.value = ''
    }
    // const setUser = (obj) => (user.value = obj)
    return {
      token,
      setToken,
      removeToken
      // setUser
    }
  },
  {
    persist: true // 持久化
  }
)
