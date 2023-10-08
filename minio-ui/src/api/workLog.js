import request from '@/utils/request'

/**
 * 日志API
 */

const baseUrl = '/workLog'

// 日志分页查询
export function getWorkLogList(params) {
  return request({
    url: baseUrl,
    method: 'get',
    params
  })
}

// 新增日志
export function addWorkLog(data) {
  return request({
    url: baseUrl,
    method: 'post',
    data
  })
}

// 获取日志详情
export function getWorlLogById(id) {
  return request({
    url: baseUrl + '/' + id,
    method: 'get'
  })
}

// 更新日志
export function updateWorkLog(data) {
  return request({
    url: baseUrl,
    method: 'put',
    data
  })
}

// 获取我收到的日志
export function getReceiveWorkLogList(params) {
  return request({
    url: baseUrl + '/receive',
    method: 'get',
    params
  })
}

// 已读或评论收到的日志
export function readOrCommentReceiveLog(data) {
  return request({
    url: baseUrl + '/receive',
    method: 'put',
    data
  })
}

// 获取日志详情及其评论列表
export function getWorkLogWithCommentsById(id) {
  return request({
    url: baseUrl + '/workLogWithComments/' + id,
    method: 'get'
  })
}
