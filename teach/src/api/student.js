import request from '@/utils/request'

// 练习相关接口
export function getStudentExerciseList(params) {
  return request({
    url: '/student/exercise/list',
    method: 'get',
    params
  })
}

/* 获取练习列表响应示例
{
  "code": 200,
  "data": {
    "list": [{
      "id": 1,
      "paperTitle": "第一章练习",
      "paperDesc": "第一章知识点练习",
      "questionCount": 5,
      "totalScore": 50,
      "startTime": "2024-03-20 00:00:00",
      "endTime": "2024-03-25 23:59:59",
      "status": 1,  // 0未开始 1进行中 2已结束
      "answered": false // 是否已答题
    }],
    "total": 1
  }
}
*/

export function getExerciseDetail(id) {
  return request({
    url: `/student/exercise/detail/${id}`,
    method: 'get'
  })
}

/* 获取练习详情响应示例
{
  "code": 200,
  "data": {
    "id": 1,
    "paperTitle": "第一章练习",
    "paperDesc": "第一章知识点练习",
    "questions": [{
      "id": 1,
      "questionTypeId": 1, // 1单选 2多选 3填空 4解答
      "content": "下列选项中，哪个是正确的？",
      "options": [
        { "value": "A", "label": "选项1" },
        { "value": "B", "label": "选项2" },
        { "value": "C", "label": "选项3" },
        { "value": "D", "label": "选项4" }
      ],
      "score": 10
    }]
  }
}
*/

export function submitExerciseAnswer(data) {
  return request({
    url: '/student/exercise/submit',
    method: 'post',
    data
  })
}

/* 提交练习答案请求示例
{
  "exerciseId": 1,
  "answers": [{
    "questionId": 1,
    "answer": "A"  // 单选
  }, {
    "questionId": 2,
    "answer": "A,B,C"  // 多选
  }, {
    "questionId": 3,
    "answer": ["答案1", "答案2"]  // 填空
  }, {
    "questionId": 4,
    "answer": "这是解答题答案"  // 解答
  }]
}
响应示例
{
  "code": 200,
  "data": {
    "score": 85,
    "questions": [{
      "questionId": 1,
      "correct": true,
      "score": 10,
      "correctAnswer": "A"
    }]
  }
}
*/

// 测试相关接口
export function getStudentTestList(params) {
  return request({
    url: '/student/test/list',
    method: 'get',
    params
  })
}

/* 获取测试列表响应示例
{
  "code": 200,
  "data": {
    "list": [{
      "id": 1,
      "paperTitle": "期中测试",
      "paperDesc": "第一学期期中测试",
      "questionCount": 10,
      "totalScore": 100,
      "startTime": "2024-03-20 09:00:00",
      "endTime": "2024-03-20 11:00:00",
      "duration": 120,
      "status": 1,  // 0未开始 1进行中 2已结束
      "submitted": false // 是否已提交
    }],
    "total": 1
  }
}
*/

export function getTestDetail(id) {
  return request({
    url: `/student/test/detail/${id}`,
    method: 'get'
  })
}

/* 获取测试详情响应示例
{
  "code": 200,
  "data": {
    "id": 1,
    "paperTitle": "期中测试",
    "paperDesc": "第一学期期中测试",
    "duration": 120,
    "endTime": "2024-03-20 11:00:00",
    "questions": [{
      "id": 1,
      "questionTypeId": 1,
      "content": "下列选项中，哪个是正确的？",
      "options": [
        { "value": "A", "label": "选项1" },
        { "value": "B", "label": "选项2" },
        { "value": "C", "label": "选项3" },
        { "value": "D", "label": "选项4" }
      ],
      "score": 10
    }]
  }
}
*/

export function submitTestAnswer(data) {
  return request({
    url: '/student/test/submit',
    method: 'post',
    data
  })
}

/* 提交测试答案请求示例
{
  "testId": 1,
  "answers": [{
    "questionId": 1,
    "answer": "A"  // 单选
  }, {
    "questionId": 2,
    "answer": "A,B,C"  // 多选
  }, {
    "questionId": 3,
    "answer": ["答案1", "答案2"]  // 填空
  }, {
    "questionId": 4,
    "answer": "这是解答题答案"  // 解答
  }],
  "duration": 95  // 实际用时(分钟)
}
响应示例
{
  "code": 200,
  "message": "提交成功",
  "data": {
    "score": 85
  }
}
*/

export function getTestResult(paperId, userId) {
  return request({
    url: `/student/get/${paperId}/${userId}`,
    method: 'get'
  })
}

/* 获取测试结果响应示例
{
  "code": 200,
  "data": {
    "score": 85,
    "questions": [{
      "id": 1,
      "content": "题目内容",
      "questionTypeId": 1,
      "score": 10,
      "studentAnswer": "A",
      "correctAnswer": "A",
      "correct": true,
      "obtainedScore": 10
    }]
  }
}
*/

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

export function getExerciseResult(id) {
  return request({
    url: `/student/exercise/result/${id}`,
    method: 'get'
  })
}

/* 获取练习结果响应示例
{
  "code": 200,
  "data": {
    "score": 85,
    "questions": [{
      "id": 1,
      "content": "题目内容",
      "questionTypeId": 1,
      "score": 10,
      "studentAnswer": "A",
      "correctAnswer": "A",
      "correct": true,
      "obtainedScore": 10
    }]
  }
}
*/

export function saveTestProgress(data) {
  return request({
    url: '/student/test/save-progress',
    method: 'post',
    data
  })
}

/* 保存测试进度请求示例
{
  "testId": 1,
  "answers": [{
    "questionId": 1,
    "answer": "A"
  }],
  "duration": 30  // 已用时间(分钟)
}
响应示例
{
  "code": 200,
  "message": "保存成功"
}
*/ 