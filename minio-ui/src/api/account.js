import request from '@/utils/request'

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

export function login(accountForm) {
  return request({
    url: '/account/login',
    method: 'post',
    data: accountForm
  })
}

export function detail() {
  return request({
    url: '/account/detail',
    method: 'get'
  })
}

export function logout() {
  return request({
    url: '/account/logout',
    method: 'delete'
  })
}
