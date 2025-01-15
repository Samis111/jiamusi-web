<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.paperName" placeholder="请输入试卷名称" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />

      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        新建测试
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="paperName" label="试卷名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="type" label="题目类型" width="120">
        <template slot-scope="{row}">
          {{ getExerciseTypeName(row.type_id) }}
        </template>
      </el-table-column>
      <el-table-column prop="starttime" label="开始时间" />
      <el-table-column prop="endtime" label="结束时间" />
      <!-- <el-table-column prop="duration" label="考试时长">
        <template slot-scope="{row}">
          {{ row.duration }}分钟
        </template>
      </el-table-column> -->
      <el-table-column prop="status" label="状态">
        <template slot-scope="{row}">
          <el-tag :type="getStatusType(row.status)">
            {{ getStatusText(row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalScore" label="总分" width="80" align="center" />
      <el-table-column label="操作" width="250" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text" @click="handlePreview(row)">预览</el-button>
          <el-button type="text" @click="handleResults(row)">查看成绩</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="60%">
      <el-form ref="dataForm" :model="temp" label-width="100px">
        <el-form-item label="试卷名称" prop="paperName">
          <el-input v-model="temp.paperName" />
        </el-form-item>
        <el-form-item label="题目类型" prop="type">
          <el-select v-model="temp.type" placeholder="请选择题目类型">
            <el-option v-for="item in exerciseTypes" :key="item.type_id" :label="item.type_name"
              :value="item.type_id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开始时间" prop="starttime">
          <el-date-picker v-model="temp.starttime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <el-form-item label="结束时间" prop="endtime">
          <el-date-picker v-model="temp.endtime" type="datetime" placeholder="选择开始时间" />
        </el-form-item>
        <!-- <el-form-item label="考试时长" prop="duration">
          <el-input-number v-model="temp.duration" :min="30" :step="30" />
          <span class="unit">分钟</span>
        </el-form-item> -->
        <el-form-item label="题目列表">
          <div v-for="(question, index) in temp.questions" :key="index" class="question-item">
            <el-input type="textarea" v-model="question.content" :rows="2" placeholder="请输入题目内容" />
            <div class="options" v-if="temp.type === 1">
              <el-input v-for="(option, idx) in question.options" :key="idx" v-model="question.options[idx]"
                placeholder="选项内容" />
            </div>
            <el-input v-model="question.answer" :type="temp.type === 1 ? 'text' : 'textarea'"
              :rows="temp.type === 1 ? 1 : 3" placeholder="正确答案" />
            <div class="question-footer">
              <el-input-number v-model="question.score" :min="1" :max="100" size="small" placeholder="分值" />
              <el-button type="danger" size="small" @click="removeQuestion(index)">删除题目</el-button>
            </div>
          </div>
          <el-button type="primary" @click="addQuestion">选择题目</el-button>
        </el-form-item>


      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog title="成绩列表" :visible.sync="resultsVisible" width="70%">
      <div v-if="currentResults" class="results-container">
        <el-table :data="currentResults" border>
          <el-table-column prop="student_name" label="学生姓名" width="120" />
          <el-table-column prop="student_id" label="学号" width="120" />
          <el-table-column prop="score" label="得分" width="100" align="center" />
          <el-table-column prop="total_score" label="总分" width="100" align="center" />
          <el-table-column prop="submit_time" label="提交时间" width="160" />
          <el-table-column label="操作" width="100">
            <template slot-scope="{row}">
              <el-button type="text" @click="handleAnswerDetail(row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>

    <el-dialog title="答题详情" :visible.sync="answerDetailVisible" width="60%" append-to-body>
      <div v-if="answerDetail" class="answer-detail">
        <div v-for="(item, index) in answerDetail" :key="index" class="question-detail">
          <div class="question-content">
            <span class="question-no">{{ index + 1 }}. </span>
            {{ item.question_content }}
          </div>
          <div class="answer-info">
            <p>学生答案：{{ item.student_answer }}</p>
            <p>正确答案：{{ item.correct_answer }}</p>
            <p>得分：{{ item.score }} / {{ item.total_score }}</p>
          </div>
        </div>
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
  getExerciseList
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
        { type_id: 1, type_name: '选择题' },
        { type_id: 2, type_name: '填空题' },
        { type_id: 3, type_name: '简答题' }
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
      }
    }
  },
  created() {
    this.getList()
    this.infoExercis()
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