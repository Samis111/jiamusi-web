<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="请输入试卷标题" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        新建测试
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="paperName" label="试卷标题" min-width="100" show-overflow-tooltip />
      <el-table-column prop="paperNode" label="试卷说明" width="100" align="center" />
      <el-table-column prop="totalScore" label="总分" width="100" align="center" />
      <el-table-column prop="starttime" label="开始时间" width="160" />
      <el-table-column prop="endtime" label="结束时间" width="160" />
     
      <el-table-column label="操作" width="300" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleAddQuestion(row)">添加题目</el-button>
          <el-button type="text" @click="handleQuestionList(row)">题目列表</el-button>
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" @click="handlePublish(row)">{{ row.status === 1 ? '取消发布' : '发布' }}</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="50%">
      <el-form ref="dataForm" :model="temp" label-width="80px" :rules="rules">
        <el-form-item label="试卷标题" prop="paperName">
          <el-input v-model="temp.paperName" placeholder="请输入试卷标题" />
        </el-form-item>
        <el-form-item label="试卷说明" prop="paperNode">
          <el-input type="textarea" v-model="temp.paperNode" :rows="3" placeholder="请输入试卷说明" />
        </el-form-item>
        <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="temp.duration" :min="30" :max="180" :step="30" />
          <span class="unit">分钟</span>
        </el-form-item>
        <el-form-item label="开始时间" prop="starttime">
          <el-date-picker v-model="temp.starttime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endtime">
          <el-date-picker v-model="temp.endtime" type="datetime" placeholder="选择结束时间" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog title="题目列表" :visible.sync="questionListVisible" width="80%">
      <el-table :data="questionList" border>
        <el-table-column type="index" label="序号" width="50" align="center" />
        <el-table-column prop="questionContent" label="题目内容" min-width="100" show-overflow-tooltip />
        <el-table-column prop="questionTypeId" label="题型" width="100">
          <template slot-scope="{row}">
            {{ getTypeName(row.questionTypeId) }}
          </template>
        </el-table-column>
        <el-table-column prop="questionCount" label="分值" width="80" align="center" />
        <el-table-column label="操作" width="150" align="center">
          <template slot-scope="{row}">
            <el-button type="text" @click="handleEditQuestion(row)">编辑</el-button>
            <el-button type="text" @click="handleDeleteQuestion(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog :title="questionDialogTitle" :visible.sync="questionDialogVisible" width="70%">
      <div class="question-form">
        <el-form ref="questionForm" :model="questionTemp" label-width="100px" :rules="questionRules">
          <el-form-item label="题目类型" prop="questionTypeId">
            <el-select v-model="questionTemp.questionTypeId" placeholder="请选择题目类型">
              <el-option v-for="item in exerciseTypes" :key="item.type_id" :label="item.type_name"
                :value="item.type_id" />
            </el-select>
          </el-form-item>

          <el-form-item label="题目内容" prop="content">
            <el-input type="textarea" v-model="questionTemp.content" :rows="3" />
          </el-form-item>

          <!-- 选择题选项 -->
          <template v-if="questionTemp.questionTypeId === 1 || questionTemp.questionTypeId === 2">
            <el-form-item label="选项">
              <div v-for="(option, index) in questionTemp.options" :key="index" class="option-item">
                <el-input v-model="option.label" placeholder="选项内容">
                  <template slot="prepend">{{ String.fromCharCode(65 + index) }}</template>
                </el-input>
                <el-button type="text" @click="removeOption(index)" v-if="questionTemp.options.length > 2">
                  <i class="el-icon-delete"></i>
                </el-button>
              </div>
              <el-button type="text" @click="addOption" v-if="questionTemp.options.length < 6">
                <i class="el-icon-plus"></i> 添加选项
              </el-button>
            </el-form-item>
          </template>

          <!-- 填空题空格数 -->
          <template v-if="questionTemp.questionTypeId === 3">
            <el-form-item label="空格数量">
              <el-input-number v-model="questionTemp.blankCount" :min="1" :max="5" />
            </el-form-item>
          </template>

          <el-form-item label="答案" prop="answer">
            <template v-if="questionTemp.questionTypeId === 1">
              <!-- 单选答案 -->
              <el-radio-group v-model="questionTemp.answer">
                <el-radio v-for="(option, index) in questionTemp.options" :key="index" :label="index">
                  {{ String.fromCharCode(65 + index) }}
                </el-radio>
              </el-radio-group>
            </template>
            <template v-else-if="questionTemp.questionTypeId === 2">
              <!-- 多选答案 -->
              <el-checkbox-group v-model="questionTemp.answer">
                <el-checkbox v-for="(option, index) in questionTemp.options" :key="index" :label="index">
                  {{ String.fromCharCode(65 + index) }}
                </el-checkbox>
              </el-checkbox-group>
            </template>
            <template v-else>
              <!-- 填空和解答答案 -->
              <el-input type="textarea" v-model="questionTemp.answer" :rows="3" />
            </template>
          </el-form-item>

          <el-form-item label="分值" prop="score">
            <el-input-number v-model="questionTemp.score" :min="1" :max="100" />
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="questionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitQuestion">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  getTestList,
  createTest,
  updateTest,
  deleteTest,
  getTestResults,
  getAnswerDetail,
  getExerciseList,
  getTestQuestions,
  addTestQuestion,
  updateTestQuestion,
  deleteTestQuestion,
  addExerciseQuestion,
} from '@/api/teaching'
import { Message } from 'element-ui'

export default {
  name: 'TestManage',
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
      dialogVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      exerciseTypes: [
        { type_id: 1, type_name: '单选题' },
        { type_id: 2, type_name: '多选题' },
        { type_id: 3, type_name: '填空题' },
        { type_id: 4, type_name: '解答题' }
      ],
      resultsVisible: false,
      currentResults: null,
      answerDetailVisible: false,
      answerDetail: null,
      temp: {
        id: undefined,
        paperName: '',
        type: undefined,
        starttime: '',
        endtime: '',
        duration: 90,
        questions: [],
        status: 0
      },
      questions: [],
      rules: {
        paper_name: [{ required: true, message: '请输入试卷名称', trigger: 'blur' }],
        type: [{ required: true, message: '请选择题目类型', trigger: 'change' }],
        startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
        duration: [{ required: true, message: '请设置考试时长', trigger: 'blur' }]
      },
      currentPaper: null,
      questionListVisible: false,
      questionList: [],
      questionDialogTitle: '',
      questionTemp: {
        questionTypeId: undefined,
        content: '',
        options: [{ label: '' }, { label: '' }],
        blankCount: 1,
        answer: '',
        score: 5
      },
      questionRules: {
        questionTypeId: [{ required: true, message: '请选择题目类型', trigger: 'change' }],
        content: [{ required: true, message: '请输入题目内容', trigger: 'blur' }],
        answer: [{ required: true, message: '请输入答案', trigger: 'blur' }],
        score: [{ required: true, message: '请设置分值', trigger: 'change' }]
      },
  
    }
  },
  created() {
    this.getList()
    // this.infoExercis()
  },
  methods: {
    getExerciseTypeName(typeId) {
      const type = this.exerciseTypes.find(t => t.type_id === typeId)
      return type ? type.type_name : '未知'
    },
    infoExercis() {

      getExerciseList().then(res => {
        this.questions = res.data;
      })

    },
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
    getList() {
      this.listLoading = true
      getTestList(this.listQuery).then(response => {
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
    handleCreate() {
      this.temp = {
        id: undefined,
        paper_name: '',
        type: undefined,
        startTime: '',
        duration: 90,
        questions: [],
        status: 0
      }
      this.dialogStatus = 'create'
      this.dialogTitle = '新建测试'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑测试'
      this.dialogVisible = true
    },
    handlePreview(row) {
      // 预览测试
    },
    handleResults(row) {
      this.resultsVisible = true
      getTestResults(row.id).then(response => {
        this.currentResults = response.data
      })
    },
    handleAnswerDetail(row) {
      this.answerDetailVisible = true
      getAnswerDetail(row.id).then(response => {
        this.answerDetail = response.data
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该测试?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteTest(row.id).then(() => {
          Message.success('删除成功')
          this.getList()
        })
      })
    },
    addQuestion() {
      this.temp.questions.push({
        content: '',
        options: this.temp.type === 1 ? ['', '', '', ''] : [],
        answer: '',
        score: 5
      })
    },
    removeQuestion(index) {
      this.temp.questions.splice(index, 1)
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createTest(this.temp).then(() => {
            this.dialogVisible = false
            Message.success('创建成功')
            this.getList()
          })
        }
      })
    },
    updateData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          updateTest(this.temp).then(() => {
            this.dialogVisible = false
            Message.success('更新成功')
            this.getList()
          })
        }
      })
    },
    handleAddQuestion(row) {
      this.currentPaper = row
      this.questionTemp = {
        questionTypeId: undefined,
        content: '',
        options: [{ label: '' }, { label: '' }],
        blankCount: 1,
        answer: '',
        score: 5
      }
      this.questionDialogTitle = '添加题目'
      this.questionDialogVisible = true
    },
    handleQuestionList(row) {
      this.currentPaper = row
      this.questionListVisible = true
      this.getQuestionList()
    },
    getQuestionList() {

      console.log(this.currentPaper)

      getTestQuestions(this.currentPaper.paperId).then(response => {
        this.questionList = response.data
      })
    },
    handleEditQuestion(row) {
      this.questionTemp = { ...row }
      this.questionDialogTitle = '编辑题目'
      this.questionDialogVisible = true
    },
    handleDeleteQuestion(row) {
      this.$confirm('确认删除该题目?', '提示', {
        type: 'warning'
      }).then(() => {
        deleteTestQuestion(row.id).then(() => {
          Message.success('删除成功')
          this.getQuestionList()
        })
      })
    },
    submitQuestion() {
      this.$refs.questionForm.validate(valid => {
        if (valid) {
          // 处理答案格式
          let formattedAnswer = this.questionTemp.answer
          if (this.questionTemp.questionTypeId === 1) {
            formattedAnswer = String.fromCharCode(65 + this.questionTemp.answer)
          } else if (this.questionTemp.questionTypeId === 2) {
            formattedAnswer = this.questionTemp.answer
              .sort()
              .map(i => String.fromCharCode(65 + i))
              .join(',')
          }

          console.log(this.currentPaper)

          const questionData = {
            ...this.questionTemp,
            newquestionId: this.currentPaper.paperId,
            answer: formattedAnswer
          }

          console.log(questionData)

          const request = this.questionTemp.id
            ? updateTestQuestion(questionData)
            : addExerciseQuestion(questionData)

          request.then(() => {
            this.questionDialogVisible = false
            this.getQuestionList()
            Message.success('操作成功')
          })
        }
      })
    },
    addOption() {
      this.questionTemp.options.push({ label: '' })
    },
    removeOption(index) {
      this.questionTemp.options.splice(index, 1)
    },
    getTypeName(typeId) {
      const type = this.exerciseTypes.find(t => t.type_id === typeId)
      return type ? type.type_name : ''
    }
  }
}
</script>

<style lang="less" scoped>
.question-item {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background: #f8f9fb;

  .options {
    margin: 10px 0;

    .el-input {
      margin-bottom: 10px;
      width: calc(50% - 10px);
      margin-right: 20px;

      &:nth-child(2n) {
        margin-right: 0;
      }
    }
  }

  .question-footer {
    margin-top: 10px;
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.unit {
  margin-left: 10px;
  color: #606266;
}

.results-container {
  max-height: 600px;
  overflow-y: auto;
}

.answer-detail {
  .question-detail {
    margin-bottom: 20px;
    padding: 15px;
    background: #f8f9fb;
    border-radius: 4px;

    .question-content {
      margin-bottom: 10px;

      .question-no {
        font-weight: bold;
      }
    }

    .answer-info {
      color: #606266;

      p {
        margin: 5px 0;
      }
    }
  }
}
</style>