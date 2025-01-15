import request from '@/utils/request'

// 练习相关接口
export function getStudentExerciseList(params) {
  return request({
    url: '/student/exercise/list',
    method: 'get',
    params
  })
}

export function submitExerciseAnswer(data) {
  return request({
    url: '/student/exercise/submit',
    method: 'post',
    data
  })
}

// 测试相关接口
export function getStudentTestList(params) {
  return request({
    url: '/student/test/list',
    method: 'get',
    params
  })
}

export function getTestDetail(id) {
  return request({
    url: `/student/test/detail/${id}`,
    method: 'get'
  })
}

export function submitTestAnswer(data) {
  return request({
    url: '/student/test/submit',
    method: 'post',
    data
  })
}

// 成绩相关接口
export function getStudentScoreList(params) {
  return request({
    url: '/student/score/list',
    method: 'get',
    params
  })
}

export function getScoreDetail(id) {
  return request({
    url: `/student/score/detail/${id}`,
    method: 'get'
  })
}

export function getStudentStats() {
  return request({
    url: '/student/score/stats',
    method: 'get'
  })
}

// 互动相关接口
export function getStudentInteractionList(params) {
  return request({
    url: '/student/interaction/list',
    method: 'get',
    params
  })
}

export function createStudentInteraction(data) {
  return request({
    url: '/student/interaction/create',
    method: 'post',
    data
  })
}

export function getInteractionDetail(id) {
  return request({
    url: `/student/interaction/detail/${id}`,
    method: 'get'
  })
}

export function createInteractionReply(data) {
  return request({
    url: '/student/interaction/reply',
    method: 'post',
    data
  })
} 