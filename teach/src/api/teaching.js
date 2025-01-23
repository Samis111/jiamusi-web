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

export function getInteractionDetail(id) {
  return request({
    url: `/interaction/detail/${id}`,
    method: 'get'
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
    url: '/NewTextPapers/list',
    method: 'get',
    params
  })
}

export function createExercise(data) {
  return request({
    url: '/NewTextPapers/save',
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
    url: '/teaching/test/list',
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
      "status": 0,  // 0草稿 1已发布
      "creatorId": 1,
      "createTime": "2024-03-15 10:00:00"
    }],
    "total": 1
  }
}
*/

export function createTest(data) {
  return request({
    url: '/teaching/test/create',
    method: 'post',
    data
  })
}
/* 创建测试请求示例
{
  "paperTitle": "期中测试",
  "paperDesc": "第一学期期中测试",
  "startTime": "2024-03-20 09:00:00",
  "endTime": "2024-03-20 11:00:00",
  "duration": 120
}
响应示例
{
  "code": 200,
  "message": "创建成功",
  "data": { "id": 1 }
}
*/

export function updateTest(data) {
  return request({
    url: '/teaching/test/update',
    method: 'post',
    data
  })
}
/* 更新测试请求示例
{
  "id": 1,
  "paperTitle": "期中测试(修改)",
  "paperDesc": "第一学期期中测试",
  "startTime": "2024-03-20 09:00:00",
  "endTime": "2024-03-20 11:00:00",
  "duration": 120
}
*/

export function deleteTest(id) {
  return request({
    url: `/teaching/test/delete/${id}`,
    method: 'post'
  })
}

export function getTestQuestions(testId) {
  return request({
    url: `/exercise/getNewExerciseQuestions/${testId}`,
    method: 'get'
  })
}
/* 获取试卷题目响应示例
{
  "code": 200,
  "data": {
    "list": [
      {
        "id": 1,
        "paperId": 1,
        "questionTypeId": 1, // 1单选 2多选 3填空 4解答
        "content": "下列选项中，哪个是正确的？",
        "options": [
          { "value": "A", "label": "选项1" },
          { "value": "B", "label": "选项2" },
          { "value": "C", "label": "选项3" },
          { "value": "D", "label": "选项4" }
        ],
        "answer": "A",
        "score": 10
      },
      {
        "id": 2,
        "paperId": 1,
        "questionTypeId": 2,
        "content": "以下哪些说法是正确的？",
        "options": [
          { "value": "A", "label": "选项1" },
          { "value": "B", "label": "选项2" },
          { "value": "C", "label": "选项3" }
        ],
        "answer": "A,B",
        "score": 10
      },
      {
        "id": 3,
        "paperId": 1,
        "questionTypeId": 3,
        "content": "请填空：___是编程中最基本的数据类型。",
        "blankCount": 1,
        "answer": "整数",
        "score": 5
      },
      {
        "id": 4,
        "paperId": 1,
        "questionTypeId": 4,
        "content": "请简述面向对象编程的三大特性。",
        "answer": "封装、继承、多态",
        "score": 15
      }
    ]
  }
}
*/

export function addTestQuestion(data) {
  return request({
    url: '/teaching/test/question/add',
    method: 'post',
    data
  })
}
/* 添加试题请求示例
{
  "paperId": 1,
  "questionTypeId": 1,
  "content": "下列选项中，哪个是正确的？",
  "options": [
    { "value": "A", "label": "选项1" },
    { "value": "B", "label": "选项2" },
    { "value": "C", "label": "选项3" },
    { "value": "D", "label": "选项4" }
  ],
  "answer": "A",
  "score": 10
}
*/

export function updateTestQuestion(data) {
  return request({
    url: '/teaching/test/question/update',
    method: 'put',
    data
  })
}
/* 更新试题请求示例
{
  "id": 1,
  "paperId": 1,
  "questionTypeId": 1,
  "content": "下列选项中，哪个是正确的？(修改)",
  "options": [...],
  "answer": "B",
  "score": 15
}
*/

export function deleteTestQuestion(id) {
  return request({
    url: `/teaching/test/question/delete/${id}`,
    method: 'delete'
  })
}

export function publishTest(id) {
  return request({
    url: `/teaching/test/publish/${id}`,
    method: 'put'
  })
}
/* 发布测试响应示例
{
  "code": 200,
  "message": "发布成功"
}
*/

// 测试成绩相关
export function getTestResults(testId) {
  return request({
    url: `/teaching/test/results/${testId}`,
    method: 'get'
  })
}
/* 获取测试结果响应示例
{
  "code": 200,
  "data": {
    "list": [{
      "studentId": 1,
      "studentName": "张三",
      "score": 85,
      "submitTime": "2024-03-20 10:30:00",
      "duration": 95
    }],
    "total": 1
  }
}
*/

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

export function getExerciseResults(exerciseId) {
  return request({
    url: `/teaching/exercise/results/${exerciseId}`,
    method: 'get'
  })
}
/* 获取练习结果响应示例
{
  "code": 200,
  "data": {
    "list": [{
      "studentId": 1,
      "studentName": "张三",
      "score": 85,
      "submitTime": "2024-03-20 10:30:00"
    }],
    "total": 1
  }
}
*/ 


export function addExerciseQuestion(data) {
  return request({
    url: `/exercise/createQuestions`,
    method: 'post',
    data:data,
  })
}


export function deleteExerciseQuestion(id) {
  return request({
    url: `/exercise/delete/`+id,
    method: 'post',
  })
}

export function getExerciseQuestions(id) {
  return request({
    url: `/exercise/getExerciseQuestions/`+id,
    method: 'get',
  })
}



export function updateTextExercise(data) {
  return request({
    url: `/NewTextPapers/updateById`,
    method: 'post',
    data
  })
}