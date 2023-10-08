import request from '@/utils/request'

const baseUrl = '/attendance'

// 通过员工id和日期查询所有考勤记录
export function getAttListByMonthOrDate(params) {
  return request({
    url: baseUrl + '/getByMonthOrDate',
    method: 'get',
    params
  })
}

// 签到
export function clockIn(empId) {
  return request({
    url: baseUrl + '?empId=' + empId,
    method: 'post'
  })
}

// 签退
export function clockOut(empId) {
  return request({
    url: baseUrl + '?empId=' + empId,
    method: 'put'
  })
}


// 通过员工id和日期查询所有考勤记录
export function getAttList(query) {
  return request({
    url: baseUrl + '/getList',
    method: 'get',
    params: query
  })
}


//导出所有考勤记录
export function exportExcelAttendsList(query) {
  return request({
    url: baseUrl + '/export',
    method: 'get',
    params: query,
    responseType: 'blob'
  })
}

//某个考勤报表
export function EmpAttendReprot(query) {
  return request({
    url: baseUrl + '/report',
    method: 'get',
    params: query,
  })
}
