/**
 * Unix时间戳转换成日期格式  unix2CurrentTime("1497232433000")
 * @param unixTime Unix时间戳
 * @return string yyyy-MM-dd HH:mm:ss
 */
export function unix2CurrentTime(unixTime) {
  const date = new Date(parseInt(unixTime))
  const y = date.getFullYear()
  let m = date.getMonth() + 1
  m = m < 10 ? ('0' + m) : m
  let d = date.getDate()
  d = d < 10 ? ('0' + d) : d
  let h = date.getHours()
  h = h < 10 ? ('0' + h) : h
  let minute = date.getMinutes()
  let second = date.getSeconds()
  minute = minute < 10 ? ('0' + minute) : minute
  second = second < 10 ? ('0' + second) : second
  return y + '-' + m + '-' + d + ' ' + h + ':' + minute + ':' + second
}

/**
 * 两个Unix时间戳差值
 * @param unixTimeStart Unix时间戳1
 * @param unixTimeEnd Unix时间戳2
 * @return string xx 小时 | xx 天
 */
export function unixDifference(unixTimeStart, unixTimeEnd) {
  const difference = (unixTimeEnd - unixTimeStart) / 1000
  if (difference >= 86400) {
    return difference / 86400 + '天'
  } else if (difference >= 3600) {
    return difference / 3600 + '小时'
  } else if (difference >= 60) {
    return difference / 60 + '分钟'
  } else {
    return difference + '秒'
  }
}

/**
 * 当前Unix时间戳差值
 * @param unixTimeEnd Unix时间戳
 * @return string | null xx天xx小时xx分钟xx秒
 */
export function nowDifference(unixTimeEnd) {
  const unixTimeStart = new Date().getTime()
  const difference = (unixTimeEnd - unixTimeStart) / 1000
  if (difference > 0) {
    let day = Math.floor(difference / (60 * 60 * 24))
    let hour = Math.floor(difference / (60 * 60)) - (day * 24)
    let minute = Math.floor(difference / 60) - (day * 24 * 60) - (hour * 60)
    let second = Math.floor(difference) - (day * 24 * 60 * 60) - (hour * 60 * 60) - (minute * 60)
    if (day <= 9) day = '0' + day
    if (hour <= 9) hour = '0' + hour
    if (minute <= 9) minute = '0' + minute
    if (second <= 9) second = '0' + second
    return day + '天' + hour + '小时' + minute + '分钟' + second + '秒'
  } else {
    return null
  }
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export function deepClone(source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'deepClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach(keys => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

export function isNumberStr(str) {
  return /^[+-]?(0|([1-9]\d*))(\.\d+)?$/g.test(str)
}

export const exportDefault = 'export default '

export const beautifierConf = {
  html: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'separate',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: false,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  },
  js: {
    indent_size: '2',
    indent_char: ' ',
    max_preserve_newlines: '-1',
    preserve_newlines: false,
    keep_array_indentation: false,
    break_chained_methods: false,
    indent_scripts: 'normal',
    brace_style: 'end-expand',
    space_before_conditional: true,
    unescape_strings: false,
    jslint_happy: true,
    end_with_newline: true,
    wrap_line_length: '110',
    indent_inner_html: true,
    comma_first: false,
    e4x: true,
    indent_empty_lines: true
  }
}

// 首字母大小
export function titleCase(str) {
  return str.replace(/( |^)[a-z]/g, L => L.toUpperCase())
}
