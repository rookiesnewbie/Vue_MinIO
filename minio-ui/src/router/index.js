import Vue from 'vue'
import Router from 'vue-router'
// in development env not use Lazy Loading,because Lazy Loading too many pages will cause webpack hot update too slow.so only in production use Lazy Loading

/* layout */
import Layout from '../views/layout/Layout'

const _import = require('./_import_' + process.env.NODE_ENV)

Vue.use(Router)

/**
 * icon : the icon show in the sidebar
 * hidden : if `hidden:true` will not show in the sidebar
 * redirect : if `redirect:noRedirect` will not redirect in the levelBar
 * noDropDown : if `noDropDown:true` will not has submenu in the sidebar
 * meta : `{ permission: ['a:xx'] }`  will control the page permission
 **/



export const constantRouterMap = [
  { path: '/login', component: _import('login/index'), hidden: true },
  { path: '/register', component: _import('register/Register'), hidden: true },
  { path: '/404', component: _import('errorPage/404'), hidden: true },
  { path: '/401', component: _import('errorPage/401'), hidden: true },
  {
    path: '',
    component: Layout,
    redirect: 'dashboard',
    icon: 'dashboard',
    noDropDown: true,
    children: [{
      path: 'dashboard',
      name: '首页',
      component: _import('dashboard/index'),
      meta: { title: 'dashboard', noCache: true }
    }]
  },
   {
    path: '/manageCenter',
    component: Layout,
    redirect: '/manageCenter/employee',
    icon: 'manage',
    // noDropDown: true,
    name: '系统管理',
    meta: { permission: ['manage'] },
    children: [{
      path: 'employee',
      name: '员工管理',
      icon: 'emp-manage',
      component: _import('system/employee/list'),
    }]
  },

  
  {
    path: '/fileCenter',
    component: Layout,
    redirect: '/fileCenter/personal',
    icon: 'file-manage',
    name: '文件管理',
    meta: { permission: ['file'] },
    children: [{
      path: 'personal',
      name: '个人文件',
      icon: 'el-icon-files',
      component: _import('system/file/personal'),
    }, {
      path: 'share',
      name: '共享文件',
      icon: 'el-icon-share',
      component: _import('system/file/share'),
    }, {
      path: 'recycleBin',
      name: '回收站',
      icon: 'el-icon-delete',
      component: _import('system/file/recycleBin'),
    }]
  },

 

  {
    path: '/accountCenter',
    component: Layout,
    redirect: '/accountCenter/index',
    hidden: true,
    children: [{
      path: 'index',
      name: '账户中心',
      component: _import('accountCenter/index')
    }]
  }
]

export default new Router({
  mode: 'history', // 后端支持可开 // 去掉url中的#
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRouterMap
  // routes
})

export const asyncRouterMap = [
  {
    path: '/manageCenter',
    component: Layout,
    redirect: '/manageCenter/employee',
    icon: 'manage',
    // noDropDown: true,
    name: '系统管理',
    meta: { permission: ['manage'] },
    children: [{
      path: 'employee',
      name: '员工管理',
      icon: 'emp-manage',
      component: _import('system/employee/list'),
    }]
  },

  
  {
    path: '/fileCenter',
    component: Layout,
    redirect: '/fileCenter/personal',
    icon: 'file-manage',
    name: '文件管理',
    meta: { permission: ['file'] },
    children: [{
      path: 'personal',
      name: '个人文件',
      icon: 'el-icon-files',
      component: _import('system/file/personal'),
    }, {
      path: 'share',
      name: '共享文件',
      icon: 'el-icon-share',
      component: _import('system/file/share'),
    }, {
      path: 'recycleBin',
      name: '回收站',
      icon: 'el-icon-delete',
      component: _import('system/file/recycleBin'),
    }]
  },

 

  {
    path: '/accountCenter',
    component: Layout,
    redirect: '/accountCenter/index',
    hidden: true,
    children: [{
      path: 'index',
      name: '账户中心',
      component: _import('accountCenter/index')
    }]
  }

]
