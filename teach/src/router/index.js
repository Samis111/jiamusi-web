import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '@/layout'

Vue.use(VueRouter)

const routes = [
  {
    path: '/login',
    component: () => import('@/views/Login.vue'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/teaching/interaction',
    children: [
      {
        path: '/teaching',
        component: { render: h => h('router-view') },
        children: [
          {
            path: 'interaction',
            name: 'InteractionManage',
            component: () => import('@/views/teaching/InteractionManage'),
            meta: { title: '教学交互管理' }
          },
          {
            path: 'courseware',
            name: 'CoursewareManage', 
            component: () => import('@/views/teaching/CoursewareManage'),
            meta: { title: '多媒体课件管理' }
          },
          {
            path: 'exercise',
            name: 'ExerciseManage',
            component: () => import('@/views/teaching/ExerciseManage'),
            meta: { title: '在线练习管理' }
          },
          {
            path: 'test',
            name: 'TestManage',
            component: () => import('@/views/teaching/TestManage'),
            meta: { title: '测试管理' }
          },
          {
            path: 'score',
            name: 'ScoreManage',
            component: () => import('@/views/teaching/ScoreManage'),
            meta: { title: '成绩管理' }
          },
          {
            path: 'user',
            name: 'UserManage',
            component: () => import('@/views/teaching/UserManage'),
            meta: { title: '用户管理' }
          }
        ]
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 获取token
  const token = localStorage.getItem('token')
  
  // 如果访问登录页，直接放行
  if (to.path === '/login') {
    next()
    return
  }
  
  // 如果没有token，重定向到登录页
  // if (!token) {
  //   next('/login')
  //   return
  // }
  
  // 有token，放行
  next()
})

export default router
