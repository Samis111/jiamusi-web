import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/auto/login',
    method: 'post',
    data
  })
}

export function logout() {
  return request({
    url: '/auto/logout',
    method: 'post'
  })
} 