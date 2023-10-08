import request from '@/utils/request'

export function list(params) {
  return request({
    url: '/role',
    method: 'get',
    params
  })
}

// 分配账户角色
export function updateAccountRole(account) {
  return request({
    url: '/account/role',
    method: 'put',
    data: account
  })
}

// 获取所有enable的角色列表
export function getEnableRoleList() {
  return request({
    url: '/role/listEnableRole',
    method: 'get'
  })
}

// 获取账户所拥有的角色id列表
export function getRoleIdsByAccountId(id) {
  return request({
    url: '/account/role?id=' + id,
    method: 'get'
  })
}

// 给账户分配角色
export function assignAccountRole(params) {
  return request({
    url: '/account/role',
    method: 'put',
    data: params
  })
}

// 新增角色
export function addRole(params) {
  return request({
    url: '/role/addRole',
    method: 'get',
    params
  })
}

// 获取角色详细信息
export function getRoleById(id) {
  return request({
    url: '/role/getRoleById?id=' + id,
    method: 'get'
  })
}

// 更新角色信息
export function updateRole(roleForm) {
  return request({
    url: '/role/updateRole',
    method: 'put',
    data: roleForm
  })
}

// 删除角色
export function deleteRoleById(id) {
  return request({
    url: '/role/deleteRoleById?id=' + id,
    method: 'delete'
  })
}

// 更新角色状态
export function updateRoleStatus(data) {
  return request({
    url: '/role/updateStatus',
    method: 'put',
    data
  })
}

// 获取角色所关联的所有权限
export function getRoleRelatedPermissionIds(id) {
  return request({
    url: 'role/permission/getRoleRelatedPermissionIds?id=' + id,
    method: 'get'
  })
}

// 给角色分配指定的权限
export function assignRolePermission(data) {
  return request({
    url: 'role/permission/assign',
    method: 'put',
    data
  })
}
