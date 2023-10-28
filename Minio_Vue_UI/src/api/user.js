import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/account/login',
    method: 'post',
    data
  })
}

export function getInfo() {
  return request({
    url: '/account/detail',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/vue-admin-template/user/logout',
    method: 'post'
  })
}

export function accountTotal() {
  return request({
    url: '/account/total',
    method: 'get'
  })
}

export function validatePassword(accountForm) {
  return request({
    url: '/account/password',
    method: 'post',
    data: accountForm
  })
}

export function update(accountForm) {
  return request({
    url: '/account/detail',
    method: 'put',
    data: accountForm
  })
}
