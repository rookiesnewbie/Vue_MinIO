import Vue from 'vue'
import Router from 'vue-router'
import store from '@/store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { getToken } from '@/utils/auth'
Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: { title: 'Dashboard', icon: 'dashboard' }
    }]
  },

  {
    path: '/fileCenter',
    component: Layout,
    redirect: '/fileCenter/personal',
    name: 'fileCenter',
    icon: 'file-manage',
    meta: { title: '文件管理',  },
    children: [
      {
        path: 'personal',
        name: 'personal',
        component: () => import('@/views/file/personal'),
        meta: { title: '个人文件', icon: 'el-icon-files' }
      },
      {
        path: 'share',
        name: 'share',
        component: () => import('@/views/file/share'),
        meta: { title: '共享文件', icon: 'el-icon-share' }
      },
      {
        path: 'recycleBin',
        name: 'recycleBin',
        component: () => import('@/views/file/recycleBin'),
        meta: { title: '回收站', icon: 'el-icon-delete'},
      }
    ]
  },

]

const createRouter = () => new Router({
  mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}



const whiteList = ['/login'] // 白名单,不需要登录的路由

router.beforeEach((to, from, next) => {
  // 尝试获取cookie中的token
  if (getToken()) {
    // 有token
    if (to.path === '/login') {
      // 但下一跳是登陆页
      // 转到首页
      next({ path: '/' })
    } else {
      // 下一跳不是登陆页
      // VUEX被清除，没有角色名
      if (store.getters.name === '') {
        // 重新获取用户信息
        store.dispatch('user/getInfo').then(response => {
          // 生成路由
          next({ ...to })
        })
      } else {
        next()
      }
    }
  } else {
    // 如果前往的路径是白名单内的,就可以直接前往
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      // 如果路径不是白名单内的,而且又没有登录,就转到登录页
      next('/login')
      NProgress.done() // 结束Progress
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})

export default router
