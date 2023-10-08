import request from '@/utils/request'

export default {
  // 添加日程
  addSchedule(scheduleForm) {
    return request({
      url: '/schedule',
      method: 'post',
      data: scheduleForm
    })
  },
  
  // 更新日程信息
  updateSchedule(scheduleForm) {
    return request({
      url: '/schedule',
      method: 'put',
      data: scheduleForm
    })
  },
  // 获取empId员工的日程信息列表
  getScheduleListByEmpId(empId) {
    return request({
      url: '/schedule/' + empId,
      method: 'get'
    })
  },

  getScheduleByDate(scheduleForm){
    return request({
    url: '/schedule/detail',
    method: 'get',
    params: scheduleForm
    })   
  }
}

