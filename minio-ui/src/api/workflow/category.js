import request from '@/utils/request'

/**
 * 流程分类API
 */

const baseUrl = '/workflow/category'

// 查询流程分类列表
export function listCategory(query) {
  return request({
    url: baseUrl + '/list',
    method: 'get',
    params: query
  })
}

// 获取所有流程分类，不分页
export function listAllCategory() {
  return request({
    url: baseUrl + '/listAll',
    method: 'get'
  })
}

// 查询流程分类详细
export function getCategory(categoryId) {
  return request({
    url: baseUrl + '/' + categoryId,
    method: 'get'
  })
}

// 新增流程分类
export function addCategory(data) {
  return request({
    url: baseUrl,
    method: 'post',
    data: data
  })
}

// 修改流程分类
export function updateCategory(data) {
  return request({
    url: baseUrl,
    method: 'put',
    data: data
  })
}

// 删除流程分类
export function delCategory(categoryIds) {
  return request({
    url: baseUrl + '/' + categoryIds,
    method: 'delete'
  })
}
