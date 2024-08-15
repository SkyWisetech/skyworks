// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useCounterStore = defineStore('counter', () => {
  // 数据 （state）
  // const baseURL = 'http://192.168.1.5:8080/skyworks' //测试环境
  const baseURL = 'http://207.148.115.202:81/skyworks' //线上环境
  return { baseURL }
})
