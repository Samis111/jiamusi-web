<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="请输入练习标题"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select v-model="listQuery.type" placeholder="选择题目类型" clearable class="filter-item" style="width: 130px">
        <el-option
          v-for="item in exerciseTypes"
          :key="item.type_id"
          :label="item.type_name"
          :value="item.type_id"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>

    <el-table
      :data="list"
      border
      style="width: 100%"
      v-loading="listLoading"
    >
      <el-table-column prop="questionContent" label="练习标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="type" label="题目类型" width="120">
        <template slot-scope="{row}">
          {{ getExerciseTypeName(row.questionTypeId) }}
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.answered ? 'success' : 'info'">
            {{ row.answered ? '已完成' : '未完成' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="150" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleAnswer(row)">
            {{ row.answered ? '查看答案' : '开始答题' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="listQuery.page"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="60%">
      <div v-if="currentExercise">
        <div class="question-content">
          {{ currentExercise.questionContent }}
        </div>
        <div class="answer-form" v-if="!currentExercise.answered">
          <!-- 单选题 -->
          <template v-if="currentExercise.questionTypeId === 1">
            <el-radio-group v-model="answer">
              <el-radio v-for="(option, index) in currentExercise.options" 
                :key="index" 
                :label="option.value">
                {{ option.label }}
              </el-radio>
            </el-radio-group>
          </template>
          
          <!-- 多选题 -->
          <template v-else-if="currentExercise.questionTypeId === 2">
            <el-checkbox-group v-model="answer">
              <el-checkbox v-for="(option, index) in currentExercise.options"
                :key="index"
                :label="option.value">
                {{ option.label }}
              </el-checkbox>
            </el-checkbox-group>
          </template>
          
          <!-- 填空题 -->
          <template v-else-if="currentExercise.questionTypeId === 3">
            <div v-for="(blank, index) in currentExercise.blanks" :key="index" class="blank-item">
              <span class="blank-label">填空{{ index + 1 }}:</span>
              <el-input v-model="answer[index]" placeholder="请输入答案" />
            </div>
          </template>
          
          <!-- 解答题 -->
          <template v-else-if="currentExercise.questionTypeId === 4">
            <el-input type="textarea" 
              v-model="answer"
              :rows="6"
              placeholder="请输入答案"
            />
          </template>

          <div class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitAnswer">提交答案</el-button>
          </div>
        </div>
        <div class="answer-detail" v-else>
          <p>你的答案：
            <template v-if="currentExercise.questionTypeId === 2">
              {{ formatMultiAnswer(currentExercise.studentAnswer) }}
            </template>
            <template v-else>
              {{ currentExercise.studentAnswer }}
            </template>
          </p>
          <p>正确答案：{{ currentExercise.correctAnswer }}</p>
          <p>得分：{{ currentExercise.score }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getStudentExerciseList, submitExerciseAnswer } from '@/api/student'
import { Message } from 'element-ui'

export default {
  name: 'StudentExercise',
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
        keyword: '',
        type: undefined
      },
      exerciseTypes: [
        { type_id: 1, type_name: '单选题' },
        { type_id: 2, type_name: '多选题' },
        { type_id: 3, type_name: '填空题' },
        { type_id: 4, type_name: '解答题' }
      ],
      dialogVisible: false,
      dialogTitle: '',
      currentExercise: null,
      answer: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getExerciseTypeName(typeId) {
      const type = this.exerciseTypes.find(t => t.type_id === typeId)
      return type ? type.type_name : '未知'
    },
    getList() {
      this.listLoading = true
      getStudentExerciseList(this.listQuery).then(response => {
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
    handleAnswer(row) {
      if (row.answered) {
        // 查看答题详情
        this.currentExercise = row
        this.dialogTitle = '答题详情'
        this.dialogVisible = true
      } else {
        // 跳转到答题页面
        this.$router.push(`/student/exercise/answer/${row.id}`)
      }
    },
    submitAnswer() {
      if (!this.answer) {
        Message.warning('请输入答案')
        return
      }
      submitExerciseAnswer({
        exerciseId: this.currentExercise.id,
        answer: this.answer
      }).then(() => {
        Message.success('提交成功')
        this.dialogVisible = false
        this.getList()
      })
    },
    formatMultiAnswer(answer) {
      if (Array.isArray(answer)) {
        return answer.join('、')
      }
      return answer
    }
  }
}
</script>

<style lang="less" scoped>
.question-content {
  margin-bottom: 20px;
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  line-height: 1.6;
}

.answer-form {
  .el-radio-group {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }
}

.answer-detail {
  padding: 15px;
  background: #f5f7fa;
  border-radius: 4px;
  
  p {
    margin: 5px 0;
    color: #606266;
  }
}

.dialog-footer {
  margin-top: 20px;
  text-align: right;
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
</style> 