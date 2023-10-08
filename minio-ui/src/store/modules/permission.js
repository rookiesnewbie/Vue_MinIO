import {constantRouterMap } from '@/router/index'

/**
 * 通过meta.permission判断是否与当前用户权限匹配
 * 
 首先检查给定的路由对象route是否包含meta属性，并且该属性是否包含permission属性。
 如果route.meta.permission存在，则将permissionCodeList中的每个权限代码与该属性进行比较，使用数组方法some()来判断是否存在至少一个匹配的权限代码。
 如果route.meta.permission不存在，则认为该路由不需要任何权限，直接返回true。
 * @param permissionCodeList
 * @param route
 */
function hasPermission(permissionCodeList, route) {
  if (route.meta && route.meta.permission) {
    return permissionCodeList.some(permission => route.meta.permission.indexOf(permission) >= 0)
  } else {
    return true
  }
}

/**
 * 递归过滤异步路由表，返回符合用户角色权限的路由表
 * @param asyncRouterMap
 * @param permissionCodeList
 */
function filterAsyncRouter(constantRouterMap) {
  return constantRouterMap.filter(route => {
    // filter,js语法里数组的过滤筛选方法

    if (route.children && route.children.length) {
      // 如果这个路由下面还有下一级的话,就递归调用
      route.children = filterAsyncRouter(route.children)
      // 如果过滤一圈后,没有子元素了,这个父级菜单就也不显示了
      // return (route.children && route.children.length)    
      return true
    }
    return false
  })
}

const permission = {
  state: {
    routers: constantRouterMap, // 本用户所有的路由,包括了固定的路由和下面的addRouters
    addRouters: [] // 本用户的角色赋予的新增的动态路由
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      state.addRouters = routers
      state.routers = constantRouterMap.concat(routers) // 将固定路由和新增路由进行合并, 成为本用户最终的全部路由信息
    }
  },
  actions: {
    GenerateRoutes({ commit }, account) {
      return new Promise(resolve => {
        const roles = account.roleNames
        // 声明 该角色可用的路由
        let accessedRouters
        if (roles.some(role => role === '超级管理员')) {
          // 如果角色里包含'超级管理员',那么所有的路由都可以用
          // 其实管理员也拥有全部菜单,这里主要是利用角色判断,节省加载时间
          accessedRouters = asyncRouterMap
        } else {
          // 否则需要通过以下方法来筛选出本角色可用的路由
          accessedRouters = filterAsyncRouter(asyncRouterMap)
        }
        // 执行设置路由的方法
        commit('SET_ROUTERS', accessedRouters)
        resolve()
      })
    }
  }
}

export default permission
