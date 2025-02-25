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
          <el-tag :type="getStatusType(row)">
            {{ getStatusText(row) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="totalScore" label="总分" width="100" align="center">
        <template slot-scope="{row}">
          {{ row.totalScore }}
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

    <el-dialog title="测试结果" :visible.sync="dialogVisible" width="70%">
      <div v-if="resultData" class="test-result">
        <div class="result-header">
          <h3>得分：{{ resultData.totalScore }} 分</h3>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getStudentTestList, getTestResult } from '@/api/student'

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
        keyword: '',
        userId: JSON.parse(localStorage.getItem('userInfo')).userId
      },
      dialogVisible: false,
      resultData: null,
      currentTime: new Date().getTime(),
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getStatusType(status) {
      const statusMap = {
        0: 'success', // 开始
        1: 'info'     // 结束
      }
      return statusMap[status.status]
    },
    getStatusText(status) {
      const statusMap = {
        0: '进行中',
        1: '已结束'
      }

      if (this.currentTime > new Date(status.endtime).getTime()) {
        return statusMap[1]
      }

  
      return statusMap[status.status]
    },
  
    getActionText(row) {
      console.log(row)
      // 如果已答题，显示查看分数
      if (row.newStatus === 1) {
        return '查看分数'
      }



      // 否则显示开始答题
      return '开始测试'
    },
    canTakeTest(row) {

      // if (this.currentTime > new Date(row.endtime).getTime()) {
      //   return false
      // }
      // 如果已结束，禁用按钮
      if (row.status === 1) {
        return false
      }
      return true
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
      if (row.newStatus === 1) {
        // 查看分数，显示弹框
        this.showTestResult(row.paperId)
      } else {
        // 开始答题，跳转到答题页
        this.$router.push({
          path: `/student/test/answer/${row.paperId}`,
          query: { type: 'answer' }
        })
      }
    },
    showTestResult(paperId) {
      const userId = JSON.parse(localStorage.getItem('userInfo')).userId
      getTestResult(paperId, userId).then(response => {
        this.resultData = response.data
        this.dialogVisible = true
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

  .question-item {
    margin-bottom: 20px;
    padding: 15px;
    background: #f8f9fb;
    border-radius: 4px;

    .question-content {
      margin-bottom: 10px;
      line-height: 1.6;

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

.dialog-footer {
  margin-top: 20px;
  text-align: right;
}
</style>