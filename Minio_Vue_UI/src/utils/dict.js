/**
 *
 * 数据字典工具类
 */

/**
 * 角色类型字典
 */
export const ROLE_TYPE_DICT = [{
  value: 0,
  name: '系统内置',
  type: 'danger'
}, {
  value: 1,
  name: '自定义',
  type: 'primary'
}]

/**
 * 性别字典
 */
export const SEX_TYPE_DICT = [{
  value: 1,
  label: '女'
}, {
  value: 2,
  label: '男'
}]

/**
 * 全局通用状态字典
 */
export const COMMON_STATUS_DICT = [{
  value: 0,
  label: '正常'
}, {
  value: 1,
  label: '停用'
}]

export const CONFERENCE_EQUIPMENT_STATUS_DICT = [{
  value: 0,
  label: '空闲'
}, {
  value: 1,
  label: '使用中'
}, {
  value: 2,
  label: '损坏'
}]

/**
 * 公文类型字典
 */
export const MISSIVE_TYPE_DICT = [{
  value: 1,
  label: '通告'
}, {
  value: 2,
  label: '指示'
}, {
  value: 3,
  label: '议案'
}, {
  value: 4,
  label: '决议'
}, {
  value: 5,
  label: '命令'
}]

/**
 * 公文机密程度字典
 */
export const MISSIVE_SECRET_LEVEL_DICT = [{
  value: 1,
  label: '公开'
}, {
  value: 2,
  label: '部门'
}]

/**
 * 会议状态字典
 */
export const CONFERENCE_PROCESS_STATUS_DICT = [{
  value: 1,
  label: '未开始'
}, {
  value: 2,
  label: '进行中'
}, {
  value: 3,
  label: '已结束'
}]

/**
 * 日志类型字典
 */
export const WORK_LOG_TYPE_DICT = [{
  value: 1,
  label: '日报'
}, {
  value: 2,
  label: '周报'
}, {
  value: 3,
  label: '月报'
}]

/**
 * 登录日志类型字典
 */
export const Login_LOG_TYPE_DICT = [{
  value: 0,
  label: '成功'
}, {
  value: 1,
  label: '失败'
}]

/**
 * 考勤类型字典
 */
export const Attendance_LOG_TYPE_DICT = [{
  value: 0,
  label: '已签到'
}, {
  value: 1,
  label: '迟到'
},
{
  value: 2,
  label: '早退'
},
{
  value: 3,
  label: '缺勤'
},
{
  value: 4,
  label: '未签退'
}
]

