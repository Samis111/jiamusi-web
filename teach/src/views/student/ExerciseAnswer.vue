<template>
  <div class="answer-container">
    <div class="paper-info">
      <h2>{{ paper.title }}</h2>
      <div class="info-row">
        <span>总分：{{ paper.totalScore }}分</span>
        <span>题目数：{{ paper.questions.length }}</span>
      </div>
    </div>

    <div class="question-list">
      <div v-for="(question, index) in paper.questions" :key="index" class="question-item">
        <div class="question-header">
          <span class="question-no">第{{ index + 1 }}题</span>
          <span class="question-type">({{ getTypeName(question.questionTypeId) }})</span>
          <span class="question-score">{{ question.score }}分</span>
        </div>

        <div class="question-content">{{ question.questionContent }}</div>

        <div class="answer-area">
          <!-- 单选题 -->
          <template v-if="question.questionTypeId === 1">
            <el-radio-group v-model="answers[index]">
              <el-radio 
                v-for="option in question.options" 
                :key="option.value"
                :label="option.value"
              >
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </template>

          <!-- 多选题 -->
          <template v-if="question.questionTypeId === 2">
            <el-checkbox-group v-model="answers[index]">
              <el-checkbox
                v-for="option in question.options"
                :key="option.value"
                :label="option.value"
              >
                {{ option.label }}
              </el-checkbox>
            </el-checkbox-group>
          </template>

          <!-- 填空题 -->
          <template v-if="question.questionTypeId === 3">
            <div v-for="(blank, blankIndex) in question.blanks" :key="blankIndex" class="blank-item">
              <span class="blank-label">填空{{ blankIndex + 1 }}:</span>
              <el-input 
                v-model="answers[index][blankIndex]"
                placeholder="请输入答案"
              />
            </div>
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
      <el-button @click="$router.go(-1)">返回</el-button>
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
        title: '',
        totalScore: 0,
        questions: []
      },
      answers: [],
      exerciseTypes: [
        { type_id: 1, type_name: '单选题' },
        { type_id: 2, type_name: '多选题' },
        { type_id: 3, type_name: '填空题' },
        { type_id: 4, type_name: '解答题' }
      ]
    }
  },
  created() {
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
        this.answers = this.paper.questions.map(q => {
          if (q.questionTypeId === 2) return [] // 多选题
          if (q.questionTypeId === 3) return new Array(q.blanks.length).fill('') // 填空题
          return '' // 单选题和解答题
        })
      })
    },
    handleSubmit() {
      // 检查是否所有题目都已答
      const hasEmpty = this.answers.some((ans, index) => {
        if (Array.isArray(ans)) {
          return ans.length === 0 || ans.some(a => !a)
        }
        return !ans
      })

      if (hasEmpty) {
        Message.warning('还有题目未完成，请检查')
        return
      }

      submitExerciseAnswer({
        exerciseId: this.$route.params.id,
        answers: this.answers
      }).then(() => {
        Message.success('提交成功')
        this.$router.push('/student/exercise')
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
      
      span {
        margin-right: 20px;
      }
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
      .el-radio-group,
      .el-checkbox-group {
        display: flex;
        flex-direction: column;
        gap: 12px;
      }

      .blank-item {
        margin-bottom: 15px;

        .blank-label {
          display: inline-block;
          width: 80px;
          color: #606266;
        }

        .el-input {
          width: calc(100% - 90px);
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
      margin: 0 10px;
    }
  }
}
</style> 