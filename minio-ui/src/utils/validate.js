/**
 * url地址
 * @param url
 * @returns {boolean}
 */
export function isValidateURL(url) {
  const reg = /^(https?|ftp):\/\/([a-zA-Z0-9.-]+(:[a-zA-Z0-9.&%$-]+)*@)*((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[1-9]?[0-9])){3}|([a-zA-Z0-9-]+\.)*[a-zA-Z0-9-]+\.(com|edu|gov|int|mil|net|org|biz|arpa|info|name|pro|aero|coop|museum|[a-zA-Z]{2}))(:[0-9]+)*(\/($|[a-zA-Z0-9.,?'\\+&%$#=~_-]+))*$/
  return reg.test(url)
}

/**
 * 小写字母
 * @param str
 * @returns {boolean}
 */
export function isValidateLowerCase(str) {
  const reg = /^[a-z]+$/
  return reg.test(str)
}

/**
 * 大写字母
 * @param str
 * @returns {boolean}
 */
export function isValidateUpperCase(str) {
  const reg = /^[A-Z]+$/
  return reg.test(str)
}

/**
 * 邮箱
 * @param email
 * @returns {boolean}
 */
export function isValidateEmail(email) {
  const reg = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/
  return reg.test(email)
}

/**
 * 手机号
 * @param phone
 * @returns {boolean}
 */
export function isValidatePhone(phone) {
  const reg = /^1[3|4|5|7|8][0-9]{9}$/
  return reg.test(phone)
}

/**
 * 身份证号
 * @param idCard
 * @returns {boolean}
 */
export function isValidateIdCard(idCard) {
  const reg = /\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/
  return reg.test(idCard)
}

/**
 * 文件名称
 * @param {*} filename
 * @returns
 */
export function isValidatefilename(filename) {
  const reg = new RegExp(`[\\\\/:*?"<>|]`)
  return reg.test(filename)
}
