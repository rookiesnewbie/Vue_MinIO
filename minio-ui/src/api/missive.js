import request from '@/utils/request'

/**
 * 公文API
 */

const baseUrl = '/missive'

// 公文分页查询
export function getMissiveList(params) {
  return request({
    url: baseUrl,
    method: 'get',
    params
  })
}

// 新增公文
export function addMissive(data) {
  return request({
    url: baseUrl,
    method: 'post',
    data
  })
}

// 通过id获取公文
export function getMissiveById(id) {
  return request({
    url: baseUrl + '/' + id,
    method: 'get'
  })
}

// 更新公文
export function updateMissive(data) {
  return request({
    url: baseUrl,
    method: 'put',
    data
  })
}

// 通过id删除公文
export function deleteMissiveById(id) {
  return request({
    url: baseUrl + '/' + id,
    method: 'delete'
  })
}
