import request from '@/utils/request'

// 教学交互管理
export function getInteractionList(params) {
  return request({
    url: '/interaction/list',
    method: 'get',
    params
  })
}

export function createInteraction(data) {
  return request({
    url: '/interaction/create',
    method: 'post',
    data
  })
}

export function updateInteraction(data) {
  return request({
    url: '/interaction/update',
    method: 'put',
    data
  })
}

export function deleteInteraction(id) {
  return request({
    url: `/interaction/delete/${id}`,
    method: 'delete'
  })
}

// 课件管理
export function getCoursewareList(params) {
  return request({
    url: '/courseware/list',
    method: 'get',
    params
  })
}

export function uploadCourseware(data) {
  return request({
    url: '/courseware/upload',
    method: 'post',
    data,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

export function addCourseware(data) {
  return request({
    url: '/courseware/save',
    method: 'post',
    data,
  })
}


export function deleteCourseware(id) {
  return request({
    url: `/courseware/delete/${id}`,
    method: 'delete'
  })
}

// 练习管理
export function getExerciseList(params) {
  return request({
    url: '/exercise/list',
    method: 'get',
    params
  })
}

export function createExercise(data) {
  return request({
    url: '/exercise/create',
    method: 'post',
    data
  })
}

export function updateExercise(data) {
  return request({
    url: '/exercise/update',
    method: 'put',
    data
  })
}

export function deleteExercise(id) {
  return request({
    url: `/exercise/delete/${id}`,
    method: 'delete'
  })
}

export function publishExercise(id) {
  return request({
    url: `/exercise/publish/${id}`,
    method: 'put'
  })
}

// 练习答题相关
export function getExerciseAnswers(id) {
  return request({
    url: `/exercise/answers/${id}`,
    method: 'get'
  })
}

// 测试管理
export function getTestList(params) {
  return request({
    url: '/test/list',
    method: 'get',
    params
  })
}

export function createTest(data) {
  return request({
    url: '/test/create',
    method: 'post',
    data
  })
}

export function updateTest(data) {
  return request({
    url: '/test/update',
    method: 'put',
    data
  })
}

export function deleteTest(id) {
  return request({
    url: `/test/delete/${id}`,
    method: 'delete'
  })
}

// 测试成绩相关
export function getTestResults(id) {
  return request({
    url: `/test/results/${id}`,
    method: 'get'
  })
}

export function getAnswerDetail(id) {
  return request({
    url: `/test/answer-detail/${id}`,
    method: 'get'
  })
}

// 获取试卷列表（用于成绩管理的筛选）
export function getPaperList() {
  return request({
    url: '/test/paper-list',
    method: 'get'
  })
}

// 成绩管理
export function getScoreList(params) {
  return request({
    url: '/score/list',
    method: 'get',
    params
  })
}

export function getScoreDetail(id) {
  return request({
    url: `/score/detail/${id}`,
    method: 'get'
  })
}

export function getScoreStats(params) {
  return request({
    url: '/score/stats',
    method: 'get',
    params
  })
}

export function exportScore(params) {
  return request({
    url: '/score/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}

// 用户管理
export function getUserList(params) {
  return request({
    url: '/user/list',
    method: 'get',
    params
  })
}

export function createUser(data) {
  return request({
    url: '/user/create',
    method: 'post',
    data
  })
}

export function updateUser(data) {
  return request({
    url: '/user/update',
    method: 'put',
    data
  })
}

export function updateUserStatus(id, status) {
  return request({
    url: `/user/status/${id}`,
    method: 'put',
    data: { status }
  })
}

export function resetUserPassword(id) {
  return request({
    url: `/user/reset-password/${id}`,
    method: 'put'
  })
} 