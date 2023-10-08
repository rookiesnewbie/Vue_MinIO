import request from '@/utils/request'

export function getAllPermissionList() {
  return request({
    url: '/permission/list',
    method: 'get'
  })
}
