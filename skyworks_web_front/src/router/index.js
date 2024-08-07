import { createRouter, createWebHistory } from 'vue-router'
// import HomeView from '../views/HomeView.vue'
// 配置History模式调这个
// createWebHistory  地址栏不带#
// 配置hash模式调这个
// createWebHashHistory  地址栏带#
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/loginweb',
      component: () => import('@/views/loginweb/LoginPage.vue') //登录
    },
    {
      path: '/',
      component: () => import('@/views/layout/LayoutContainer.vue'), //框架
      redirect: '/',
      children: [
        {
          path: '/',
          component: () => import('@/views/homeweb/homeWeb.vue') //首页
        },
        {
          path: '/articledetails',
          name: 'articledetails',
          component: () => import('@/views/articledetails/ArticleDetails.vue') //文章详情
        },
        {
          path: '/personalparticulars',
          component: () =>
            import('@/views/personalparticulars/PersonalParticulars.vue') //个人详情
        }
      ]
    }
  ]
})

export default router
