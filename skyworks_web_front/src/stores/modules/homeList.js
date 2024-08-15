import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  // 数据 （state）
  const homeList = ref({}) //首页标签筛选瀑布流数据
  // const baseURL = 'http://192.168.1.5:8080/skyworks' //测试环境
  const baseURL = 'https://theworksof.com/skyworks' //线上环境

  const userData = ref() //用户信息

  // 修改数据的方法 （action）
  function increment(v) {
    homeList.value = v
  }
  const doubleCount = computed(() => homeList.value * 2)
  return { homeList, doubleCount, increment, baseURL, userData }
})
