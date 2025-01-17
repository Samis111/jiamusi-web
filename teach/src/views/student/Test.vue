<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="请输入试卷名称" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="paperName" label="试卷名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="starttime" label="开始时间" width="160" />
      <el-table-column prop="endtime" label="结束时间" width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="{row}">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalScore" label="总分得分" width="100" align="center">
        <template slot-scope="{row}">
          {{  row.totalScore }}
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleTest(row)" :disabled="!canTakeTest(row)">
            {{ getActionText(row) }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog :title="currentTest ? currentTest.paperName : ''" :visible.sync="dialogVisible" width="70%"
      :close-on-click-modal="false" :close-on-press-escape="false" :show-close="false">
      <div v-if="currentTest && !currentTest.submitted">
        <div class="test-info">
          <span>剩余时间：{{ remainingTime }}</span>
        </div>
        <div v-for="(question, index) in currentTest.questions" :key="index" class="question-item">
          <div class="question-content">
            <span class="question-no">{{ index + 1 }}. </span>
            {{ question.content }}
            <span class="question-score">({{ question.score }}分)</span>
          </div>
          <div class="answer-area">
            <template v-if="question.type === 1">
              <el-radio-group v-model="answers[index]">
                <el-radio v-for="(option, idx) in question.options" :key="idx" :label="option">{{ option }}</el-radio>
              </el-radio-group>
            </template>
            <template v-else>
              <el-input type="textarea" v-model="answers[index]" :rows="4" placeholder="请输入答案" />
            </template>
          </div>
        </div>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitTest">提交试卷</el-button>
        </div>
      </div>
      <div v-else-if="currentTest && currentTest.submitted" class="test-result">
        <div class="result-header">
          <h3>得分：{{ currentTest.score }}</h3>
        </div>
        <div v-for="(question, index) in currentTest.questions" :key="index" class="question-item">
          <div class="question-content">
            <span class="question-no">{{ index + 1 }}. </span>
            {{ question.content }}
          </div>
          <div class="answer-info">
            <p>你的答案：{{ question.studentAnswer }}</p>
            <p>正确答案：{{ question.correctAnswer }}</p>
            <p>得分：{{ question.score }} / {{ question.totalScore }}</p>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getStudentTestList, getTestDetail, submitTestAnswer } from '@/api/student'
import { Message } from 'element-ui'

export default {
  name: 'StudentTest',
  components: {
    Pagination
  },
  data() {
    return {
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: ''
      },
      dialogVisible: false,
      currentTest: null,
      answers: [],
      timer: null,
      remainingTime: ''
    }
  },
  created() {
    this.getList()
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
  },
  methods: {
    getStatusType(status) {
      const statusMap = {
        0: 'info',    // 未开始
        1: 'success', // 进行中
        2: 'warning'  // 已结束
      }
      return statusMap[status]
    },
    getStatusText(status) {
      const statusMap = {
        0: '未开始',
        1: '进行中',
        2: '已结束'
      }
      return statusMap[status]
    },
    getActionText(row) {
      if (row.submitted) return '查看详情'
      if (row.status === 1) return '开始考试'
      if (row.status === 0) return '未开始'
      return '已结束'
    },
    canTakeTest(row) {
      return row.submitted || row.status === 1
    },
    getList() {
      this.listLoading = true
      getStudentTestList(this.listQuery).then(response => {
        this.list = response.data
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleTest(row) {
      if (row.submitted) {
        this.showTestResult(row.id)
      } else {
        this.$router.push(`/student/test/answer/${row.id}`)
      }
    },
    showTestResult(testId) {
      getTestDetail(testId).then(response => {
        this.currentTest = response.data
        this.dialogVisible = true
      })
    },
    startTimer() {
      if (this.timer) clearInterval(this.timer)

      const endTime = new Date(this.currentTest.endtime).getTime()

      this.timer = setInterval(() => {
        const now = new Date().getTime()
        const distance = endTime - now

        if (distance < 0) {
          clearInterval(this.timer)
          this.submitTest()
          return
        }

        const hours = Math.floor(distance / (1000 * 60 * 60))
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60))
        const seconds = Math.floor((distance % (1000 * 60)) / 1000)

        this.remainingTime = `${hours}:${minutes}:${seconds}`
      }, 1000)
    },
    submitTest() {
      this.$confirm('确认提交试卷?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitTestAnswer({
          testId: this.currentTest.id,
          answers: this.answers
        }).then(() => {
          Message.success('提交成功')
          this.dialogVisible = false
          if (this.timer) clearInterval(this.timer)
          this.getList()
        })
      })
    }
  }
}
</script>

<style lang="less" scoped>
.test-info {
  margin-bottom: 20px;
  padding: 10px;
  background: #f5f7fa;
  border-radius: 4px;
  text-align: right;

  span {
    color: #f56c6c;
    font-size: 16px;
    font-weight: bold;
  }
}

.question-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;

  .question-content {
    margin-bottom: 10px;
    line-height: 1.6;

    .question-no {
      font-weight: bold;
    }

    .question-score {
      color: #909399;
      margin-left: 10px;
    }
  }

  .answer-area {
    .el-radio-group {
      display: flex;
      flex-direction: column;
      gap: 10px;
    }
  }

  .answer-info {
    color: #606266;

    p {
      margin: 5px 0;
    }
  }
}

.test-result {
  .result-header {
    margin-bottom: 20px;
    text-align: center;

    h3 {
      color: #f56c6c;
      font-size: 24px;
    }
  }
}

.dialog-footer {
  margin-top: 20px;
  text-align: right;
}
</style>