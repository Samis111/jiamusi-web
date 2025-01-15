<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.questionContent" placeholder="请输入练习标题" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.questionTypeId" placeholder="选择题目类型" clearable class="filter-item"
        style="width: 130px">
        <el-option v-for="item in exerciseTypes" :key="item.type_id" :label="item.type_name" :value="item.type_id" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        添加题库
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="questionContent" label="练习标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="questionTypeId" label="题目类型" width="120">
        <template slot-scope="{row}">
          {{ getExerciseTypeName(row.questionTypeId) }}
        </template>
      </el-table-column>
      <el-table-column prop="questionAnswer" label="答案" width="100" align="center" />
      <!-- <el-table-column prop="questionCreatorId" label="创建者" width="120" /> -->
      <el-table-column prop="questionCreateTime" label="创建时间" width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'info'">
            {{ row.status === 1 ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleEdit(row)">编辑</el-button>
          <el-button type="text"  @click="handlePublish(row)">状态更改</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="60%">
      <el-form ref="dataForm" :model="temp" label-width="100px" :rules="rules">
        <el-form-item label="问题" prop="questionContent">
          <el-input v-model="temp.questionContent" />
        </el-form-item>
        <el-form-item label="答案" prop="questionAnswer">
          <el-input v-model="temp.questionAnswer" />
        </el-form-item>

        <el-form-item label="题目类型" prop="questionTypeId">
          <el-select v-model="temp.questionTypeId" placeholder="请选择题目类型">
            <el-option v-for="item in exerciseTypes" :key="item.type_id" :label="item.type_name"
              :value="item.type_id" />
          </el-select>
        </el-form-item>


        <!-- <el-form-item label="题目列表">
          <div v-for="(question, index) in temp.questions" :key="index" class="question-item">
            <el-input type="textarea" v-model="question.content" :rows="2" placeholder="请输入题目内容" />
            <div class="options" v-if="temp.type === 1">
              <el-input v-for="(option, idx) in question.options" :key="idx" v-model="question.options[idx]"
                placeholder="选项内容" />
            </div>
            <el-input v-model="question.answer" :type="temp.type === 1 ? 'text' : 'textarea'"
              :rows="temp.type === 1 ? 1 : 3" placeholder="正确答案" />
            <el-button type="danger" @click="removeQuestion(index)">删除题目</el-button>
          </div>
          <el-button type="primary" @click="addQuestion">添加题目</el-button>
        </el-form-item> -->



      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog title="答题情况" :visible.sync="answersVisible" width="70%">
      <div v-if="currentAnswers" class="answers-container">
        <el-table :data="currentAnswers" border>
          <el-table-column prop="student_name" label="学生姓名" width="120" />
          <el-table-column prop="question_content" label="题目" show-overflow-tooltip />
          <el-table-column prop="student_answer" label="学生答案" show-overflow-tooltip />
          <el-table-column prop="correct_answer" label="正确答案" show-overflow-tooltip />
          <el-table-column prop="answer_time" label="答题时间" width="160" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  getExerciseList,
  createExercise,
  updateExercise,
  deleteExercise,
  publishExercise
} from '@/api/teaching'
import { Message } from 'element-ui'

export default {
  name: 'ExerciseManage',
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
      answersVisible: false,
      currentAnswers: null,
      temp: {
        id: undefined,
        title: '',
        type: undefined,
        questions: [],
        status: 0
      },
      rules: {
        title: [{ required: true, message: '请输入练习标题', trigger: 'blur' }],
        type: [{ required: true, message: '请选择题目类型', trigger: 'change' }]
      }
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
      getExerciseList(this.listQuery).then(response => {
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
        title: '',
        type: undefined,
        questions: [],
        status: 0
      }
      this.dialogStatus = 'create'
      this.dialogTitle = '增加题库'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleEdit(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '修改题库'
      this.dialogVisible = true
    },
    handlePreview(row) {
      // 预览练习
    },
    handlePublish(row) {
      this.$confirm('确认发布该练习?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        publishExercise(row.questionId).then(() => {
          Message.success('发布成功')
          this.getList()
        })
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该问题?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteExercise(row.questionId).then(() => {
          Message.success('删除成功')
          this.getList()
        })
      })
    },
    handleAnswers(row) {
      this.answersVisible = true
      getExerciseAnswers(row.questionId).then(response => {
        this.currentAnswers = response.data
      })
    },
    addQuestion() {
      this.temp.questions.push({
        content: '',
        options: this.temp.questionTypeId === 1 ? ['', '', '', ''] : [],
        answer: ''
      })
    },
    removeQuestion(index) {
      this.temp.questions.splice(index, 1)
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          let username = window.sessionStorage.getItem('user');
          let user = JSON.parse(username);
          console.log(user)

          this.temp.questionCreatorId = user.userId;

          createExercise(this.temp).then(() => {
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
          updateExercise(this.temp).then(() => {
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
}

.answers-container {
  max-height: 600px;
  overflow-y: auto;
}
</style>