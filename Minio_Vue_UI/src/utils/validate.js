/**
 * Created by PanJiaChen on 16/11/18.
 */

/**
 * @param {string} path
 * @returns {Boolean}
 */
export function isExternal(path) {
  return /^(https?:|mailto:|tel:)/.test(path)
}

/**
 * @param {string} str
 * @returns {Boolean}
 */
export function validUsername(str) {
  const valid_map = ['admin', 'editor']
  return valid_map.indexOf(str.trim()) >= 0
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
 * 文件名称
 * @param {*} filename
 * @returns
 */
export function isValidatefilename(filename) {
  const reg = new RegExp(`[\\\\/:*?"<>|]`)
  return reg.test(filename)
}
