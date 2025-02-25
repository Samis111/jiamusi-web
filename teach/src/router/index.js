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
            meta: { title: '练习管理' }
          },
          {
            path: 'test',
            name: 'TestManage',
            component: () => import('@/views/teaching/TestManage'),
            meta: { title: '在线测试' }
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
  },
  {
    path: '/student',
    component: Layout,
    redirect: '/student/exercise',
    children: [
      {
        path: 'exercise',
        name: 'StudentExercise',
        component: () => import('@/views/student/Exercise'),
        meta: { title: '在线练习' }
      },
      {
        path: 'exercise/answer/:id',
        name: 'ExerciseAnswer',
        component: () => import('@/views/student/ExerciseAnswer'),
        meta: { title: '练习答题', activeMenu: '/student/exercise' }
      },
      {
        path: 'test',
        name: 'StudentTest',
        component: () => import('@/views/student/Test'),
        meta: { title: '在线测试' }
      },
      {
        path: 'test/answer/:id',
        name: 'TestAnswer',
        component: () => import('@/views/student/TestAnswer'),
        meta: { title: '测试答题', activeMenu: '/student/test' }
      },
      {
        path: 'score',
        name: 'StudentScore', 
        component: () => import('@/views/student/Score'),
        meta: { title: '我的成绩' }
      },
      {
        path: 'interaction',
        name: 'StudentInteraction',
        component: () => import('@/views/student/Interaction'),
        meta: { title: '教学互动' }
      }
    ]
  }
]

const router = new VueRouter({
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 获取用户信息
  const userInfo = router.app.$store?.state.user.userInfo
  
  // 如果访问登录页，直接放行
  if (to.path === '/login') {
    next()
    return
  }
  
  // 如果没有角色信息，重定向到登录页
  if (!userInfo?.role) {
    next('/login')
    return
  }
  
  // 根据角色判断可访问的路由
  const { role } = userInfo
  if (role === 'student' && to.path.startsWith('/teaching')) {
    next('/student/exercise')
    return
  }
  
  if (role === 'teacher' && to.path.startsWith('/student')) {
    next('/teaching/interaction')
    return
  }
  
  // 放行
  next()
})

export default router
