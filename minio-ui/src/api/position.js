import request from '@/utils/request'

export default {
  // 获取职位列表，带分页和搜索
  pageQuery(params) {
    return request({
      url: '/position',
      method: 'get',
      params
    })
  },
  // 获取职位列表，不带分页和搜索，用于下拉列表
  getPostList() {
    return request({
      url: '/position/list',
      method: 'get'
    })
  },
  // 获取职位详细信息
  getPostDetail(positionId) {
    return request({
      url: '/position/get?id=' + positionId,
      method: 'get'
    })
  },
  // 新增职位
  addPost(positionForm) {
    return request({
      url: '/position',
      method: 'post',
      data: positionForm
    })
  },
  // 更新职位信息
  updatePost(positionForm) {
    return request({
      url: '/position',
      method: 'put',
      data: positionForm
    })
  },
  // 导出职位
  exportPosition(query) {
    return request({
      url: '/position/export',
      method: 'get',
      params: query,
      responseType: 'blob'
    })
  },
  // 删除职位
  deletePost(positionId) {
    return request({
      url: '/position?id=' + positionId,
      method: 'delete'
    })
  }
}
