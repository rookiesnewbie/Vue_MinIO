import request from '@/utils/request'

export default {
  // 所有 的部门列表
  getAllDeptList(params) {
    return request({
      url: '/department',
      method: 'get',
      params
    })
  },
  // enable 的部门列表
  getEnableDeptList() {
    return request({
      url: '/department/listEnableDept',
      method: 'get'
    })
  },
  // 新增部门
  addDepartment(deptForm) {
    return request({
      url: '/department',
      method: 'post',
      data: deptForm
    })
  },
  // 获取部门详细信息
  getDeptDetail(deptId) {
    return request({
      url: '/department/get?id=' + deptId,
      method: 'get'
    })
  },
  // 更新部门信息
  updateDepartment(deptForm) {
    return request({
      url: '/department',
      method: 'put',
      data: deptForm
    })
  },
  // 删除部门
  deleteDepartment(deptId) {
    return request({
      url: '/department?id=' + deptId,
      method: 'delete'
    })
  }
}

// 查询部门下拉树结构
export function treeselect() {
  return request({
    url: '/department/treeselect',
    method: 'get'
  })
}
