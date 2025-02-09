<template>
  <div class="answer-container">
    <div class="paper-info">
      <h2>{{ paper.paperName }}</h2>
      <div class="info-row">
        <span>总分：{{ paper.totalScore }}分</span>
        <span>题目数：{{ paper.questions?.length || 0 }}</span>
      </div>
    </div>

    <div class="question-list">
      <div v-for="(question, index) in paper.questions" :key="index" class="question-item">
        <div class="question-header">
          <span class="question-no">第{{ index + 1 }}题</span>
          <span class="question-type">({{ getTypeName(question.questionTypeId) }})</span>
          <span class="question-score">{{ question.questionCount }}分</span>
        </div>

        <div class="question-content">问：{{ question.questionContent }}</div>

        <div class="answer-area">
          <!-- 单选题 -->
          <template v-if="question.questionTypeId === 1">
            <el-radio-group v-model="answers[index]">
              <el-radio 
                v-for="(option, optionIndex) in getOptions(question.questionAnswer)" 
                :key="optionIndex"
                :label="getOptionLabel(optionIndex)"
              >
                {{ getOptionLabel(optionIndex) }}. {{ option }}
              </el-radio>
            </el-radio-group>
          </template>

          <!-- 填空题 -->
          <template v-if="question.questionTypeId === 3">
            <el-input 
              v-model="answers[index]"
              placeholder="请输入答案"
            />
          </template>

          <!-- 解答题 -->
          <template v-if="question.questionTypeId === 4">
            <el-input
              type="textarea"
              v-model="answers[index]"
              :rows="6"
              placeholder="请输入答案"
            />
          </template>
        </div>
      </div>
    </div>

    <div class="action-bar">
      <el-button type="primary" @click="handleSubmit">提交答案</el-button>
    </div>
  </div>
</template>

<script>
import { getExerciseDetail, submitExerciseAnswer } from '@/api/student'
import { Message } from 'element-ui'

export default {
  name: 'ExerciseAnswer',
  data() {
    return {
      paper: {
        paperName: '',
        totalScore: 0,
        questions: []
      },
      answers: [],
      exerciseTypes: [
        { type_id: 1, type_name: '单选题' },
        { type_id: 3, type_name: '填空题' },
        { type_id: 4, type_name: '解答题' }
      ],
      isResult: false, // 是否是查看结果模式
      score: 0 // 总分
    }
  },
  created() {
    // 判断是否是查看结果模式
    this.isResult = this.$route.query.type === 'result'
    if (this.isResult) {
      this.score = this.$route.query.score
    }
    this.getDetail()
  },
  methods: {
    getTypeName(typeId) {
      const type = this.exerciseTypes.find(t => t.type_id === typeId)
      return type ? type.type_name : '未知'
    },
    getDetail() {
      const exerciseId = this.$route.params.id
      getExerciseDetail(exerciseId).then(res => {
        this.paper = res.data
        // 初始化答案数组
        this.answers = new Array(this.paper.questions.length).fill('')
      })
    },
    getOptionLabel(index) {
      return String.fromCharCode(65 + index) // 65 是字符 'A' 的 ASCII 码
    },
    getOptions(answerStr) {
      try {
        // 解析字符串形如 "[=1,=2,=3][null]" 
        const matches = answerStr.match(/\[(.*?)\]/g);
        if (matches && matches.length > 0) {
          // 取第一个中括号内的内容并分割成数组
          const options = matches[0].slice(1, -1).split(',');
          return options.map(opt => opt.trim().replace('=', '')); // 去除空格和等号
        }
        return [];
      } catch (error) {
        console.error('解析选项出错:', error);
        return [];
      }
    },
    handleSubmit() {
      // 构造提交的答案数据
      const submitData = {
        paperId: this.$route.params.id,
        userId: JSON.parse(localStorage.getItem('userInfo')).userId,
        answers: this.paper.questions.map((question, index) => ({
          ...question,
          answer: this.answers[index] || ''
        }))
      }

      submitExerciseAnswer(submitData).then(() => {
        Message.success('提交成功')
        // 提交成功后返回练习列表页
        this.$router.push('/student/exercise')
      }).catch(() => {
        Message.error('提交失败')
      })
    }
  }
}
</script>

<style lang="less" scoped>
.answer-container {
  padding: 20px;
  background: #fff;
  border-radius: 4px;

  .paper-info {
    margin-bottom: 30px;
    padding-bottom: 15px;
    border-bottom: 1px solid #ebeef5;

    h2 {
      margin: 0 0 15px;
      color: #303133;
    }

    .info-row {
      color: #606266;
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .question-item {
    margin-bottom: 30px;
    padding: 20px;
    background: #f8f9fb;
    border-radius: 4px;

    .question-header {
      margin-bottom: 15px;

      .question-no {
        font-weight: bold;
        color: #303133;
      }

      .question-type {
        margin-left: 10px;
        color: #909399;
      }

      .question-score {
        float: right;
        color: #f56c6c;
      }
    }

    .question-content {
      margin-bottom: 20px;
      line-height: 1.6;
      color: #303133;
    }

    .answer-area {
      .el-radio-group {
        display: flex;
        flex-direction: column;
        gap: 12px;

        .el-radio {
          margin-left: 0 !important;
          
          /deep/ .el-radio__label {
            white-space: normal;
            padding-left: 8px;
          }
        }
      }
    }
  }

  .action-bar {
    margin-top: 30px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;
    text-align: center;

    .el-button {
      min-width: 120px;
    }
  }
}
</style> 