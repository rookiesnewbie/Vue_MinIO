import request from '@/utils/request'

export default {
  // 添加员工
  addEmployee(employeeForm) {
    return request({
      url: '/employee',
      method: 'post',
      data: employeeForm
    })
  },
  // 员工列表
  getEmployeeList(params) {
    return request({
      url: '/employee',
      method: 'get',
      params
    })
  },
  // 更新员工信息
  updateEmployee(employeeForm) {
    return request({
      url: '/employee',
      method: 'put',
      data: employeeForm
    })
  },
  // 通过id删除员工
  deleteEmployeeById(employeeId) {
    return request({
      url: '/employee/' + employeeId,
      method: 'delete'
    })
  },
  // 展示所有 状态为 Enable 的员工
  getEnableEmpList() {
    return request({
      url: '/employee/listEnableEmp',
      method: 'get'
    })
  },
  // 获取员工详细信息
  getEmpDetail(employeeId) {
    return request({
      url: '/employee/get?id=' + employeeId,
      method: 'get'
    })
  },
  // 更新员工状态
  updateEmpStatus(id, status) {
    const data = {
      id,
      status
    }
    return request({
      url: '/employee/updateStatus',
      method: 'put',
      data: data
    })
  },
  // 导出员工
  exportUser(query) {
    return request({
      url: '/employee/export',
      method: 'get',
      params: query,
      responseType: 'blob'
    })
  },
  // 下载用户导入模板
  importTemplate() {
    return request({
      url: '/employee/getImportTemplate',
      method: 'get',
      responseType: 'blob'
    })
  }
}

// 获取empId员工所在部门enable员工列表
export function getDeptEnableEmpListByEmpId(empId) {
  return request({
    url: '/employee/listDeptEmpByEmpId/' + empId,
    method: 'get'
  })
}

// 获取所有员工列表
export function getEmployeeList1() {
  return request({
    url: '/employee/empList',
    method: 'get'
  })
}

