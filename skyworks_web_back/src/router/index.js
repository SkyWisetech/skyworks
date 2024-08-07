import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/modules/user'
// import HomeView from '../views/HomeView.vue'
// 配置History模式调这个
// createWebHistory  地址栏不带#
// 配置hash模式调这个
// createWebHashHistory  地址栏带#
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      component: () => import('@/views/login/LoginPage.vue') //登录
    },
    {
      path: '/',
      component: () => import('@/views/layout/LayoutContainer.vue'), //框架
      redirect: '/',
      children: [
        {
          path: '/layout',
          component: () => import('@/views/layout/LayoutContainer.vue') //框架
        },
        {
          path: '/',
          component: () => import('@/views/home/home.vue') //首页
        },
        {
          path: '/articlemanage',
          name: '/articlemanage',
          component: () => import('@/views/articlemanage/ArticleManages.vue') //文章管理
        },
        {
          path: '/articlecomment',
          name: 'articlecomment',
          component: () =>
            import('@/views/articlemanage/articlecomment/ArticleComment.vue') //文章查看评论
        },
        {
          path: '/classificationmanage',

          component: () =>
            import('@/views/classificationmanage/ClassificationManage.vue') //分类管理
        },
        {
          path: '/articlepublish',
          name: 'articlepublish',
          component: () => import('@/views/articlepublish/ArticlePublish.vue') //文章发布
        },
        {
          path: '/user',
          redirect: '/user/usermanage',
          component: () => import('@/views/usermanage/index.vue'), //用户管理
          children: [
            {
              path: '/user/usermanage',
              component: () => import('@/views/usermanage/UserManage.vue') //查看用户评论
            },
            {
              path: '/user/checkComment/:id',
              component: () => import('@/views/usermanage/CheckComment.vue') //查看用户评论
            }
          ]
        }
      ]
    }
  ]
})
// 登录访问拦截=>默认直接放行
// 根据返回值决定：是放行还是拦截
// 1.undefined/true  直接放行
// 2.false  拦回from的地址页面
// 3.具体路径/路径对象 拦截到对应的地址   '/login{name:'login'}'
router.beforeEach((to, from, next) => {
  // 如果没有token，其访问的事非登录页  拦截到登录    其他情况正常放行
  if (to.path == '/login') {
    next()
  } else {
    const userStore = useUserStore()
    if (!userStore.token) {
      return next('/login')
    }
  }
  next()
})
export default router
