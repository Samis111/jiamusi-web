<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="请输入学生姓名" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.paper_id" placeholder="选择试卷" clearable class="filter-item" style="width: 200px">
        <el-option v-for="item in papers" :key="item.paper_id" :label="item.paper_name" :value="item.paper_id" />
      </el-select>
      <el-select v-model="listQuery.type" placeholder="选择题目类型" clearable class="filter-item" style="width: 130px">
        <el-option v-for="item in exerciseTypes" :key="item.type_id" :label="item.type_name" :value="item.type_id" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="success" icon="el-icon-download" @click="handleExport">
        导出成绩
      </el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="成绩列表" name="list">
        <el-table :data="list" border style="width: 100%" v-loading="listLoading">
          <el-table-column prop="studentName" label="学生姓名" width="120" />
          <el-table-column prop="studentId" label="学号" width="120" />
          <el-table-column prop="paperName" label="试卷名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="type" label="题目类型" width="120">
            <template slot-scope="{row}">
              {{ getExerciseTypeName(row.type_id) }}
            </template>
          </el-table-column>
          <el-table-column prop="totalScore" label="得分" width="100" align="center" />
          <el-table-column prop="total" label="总分" width="100" align="center" />
          <el-table-column prop="testTime" label="考试时间" width="160" />
          <el-table-column label="操作" width="150">
            <template slot-scope="{row}">
              <el-button type="text" @click="handleDetail(row)">查看详情</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
          @pagination="getList" />
      </el-tab-pane>

      <el-tab-pane label="成绩统计" name="stats">
        <div class="stats-container">
          <div class="chart-wrapper">
            <div ref="scoreDistribution" class="chart"></div>
          </div>
          <div class="stats-info">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>统计信息</span>
                <el-select v-model="statsQuery.paper_id" placeholder="选择试卷" size="small"
                  style="float: right; width: 200px" @change="handleStatsChange">
                  <el-option v-for="item in papers" :key="item.paper_id" :label="item.paper_name"
                    :value="item.paper_id" />
                </el-select>
              </div>
              <div class="stats-item">
                <span class="label">参考人数：</span>
                <span class="value">{{ stats.total_students }}</span>
              </div>
              <div class="stats-item">
                <span class="label">平均分：</span>
                <span class="value">{{ stats.average_score }}</span>
              </div>
              <div class="stats-item">
                <span class="label">最高分：</span>
                <span class="value">{{ stats.max_score }}</span>
              </div>
              <div class="stats-item">
                <span class="label">最低分：</span>
                <span class="value">{{ stats.min_score }}</span>
              </div>
              <div class="stats-item">
                <span class="label">及格率：</span>
                <span class="value">{{ stats.pass_rate }}%</span>
              </div>
            </el-card>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="成绩详情" :visible.sync="detailVisible" width="50%">
      <div v-if="scoreDetail">
        <div class="detail-header">
          <h3>{{ scoreDetail.paper_name }}</h3>
          <p>学生：{{ scoreDetail.student_name }} ({{ scoreDetail.student_id }})</p>
          <p>得分：{{ scoreDetail.score }} / {{ scoreDetail.total_score }}</p>
          <p>考试时间：{{ scoreDetail.test_time }}</p>
        </div>
        <div class="question-list">
          <div v-for="(item, index) in scoreDetail.questions" :key="index" class="question-item">
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
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  getScoreList,
  getScoreDetail,
  getScoreStats,
  exportScore,
  getPaperList
} from '@/api/teaching'
import { Message } from 'element-ui'
import * as echarts from 'echarts'

export default {
  name: 'ScoreManage',
  components: {
    Pagination
  },
  data() {
    return {
      activeTab: 'list',
      list: [],
      total: 0,
      listLoading: false,
      listQuery: {
        page: 1,
        limit: 10,
        keyword: '',
        paper_id: undefined,
        type: undefined
      },
      papers: [],
      exerciseTypes: [
        { type_id: 1, type_name: '选择题' },
        { type_id: 2, type_name: '填空题' },
        { type_id: 3, type_name: '简答题' }
      ],
      detailVisible: false,
      scoreDetail: null,
      statsQuery: {
        paper_id: undefined
      },
      stats: {
        total_students: 0,
        average_score: 0,
        max_score: 0,
        min_score: 0,
        pass_rate: 0
      }
    }
  },
  created() {
    this.getList()
    this.getPapers()
  },
  methods: {
    getExerciseTypeName(typeId) {
      const type = this.exerciseTypes.find(t => t.type_id === typeId)
      return type ? type.type_name : '未知'
    },
    getPapers() {
      getPaperList().then(response => {
        this.papers = response.data
      })
    },
    getList() {
      this.listLoading = true
      getScoreList(this.listQuery).then(res => {

        console.log(res)

        this.list = res.data
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleDetail(row) {
      this.detailVisible = true
      getScoreDetail(row.id).then(response => {
        this.scoreDetail = response.data
      })
    },
    handleExport() {
      exportScore(this.listQuery).then(response => {
        const blob = new Blob([response.data])
        const link = document.createElement('a')
        link.href = window.URL.createObjectURL(blob)
        link.download = '成绩表.xlsx'
        link.click()
      })
    },
    handleTabClick() {
      if (this.activeTab === 'stats') {
        this.initCharts()
      }
    },
    handleStatsChange() {
      this.getStats()
      this.initCharts()
    },
    getStats() {
      getScoreStats(this.statsQuery).then(response => {
        this.stats = response.data
      })
    },
    initCharts() {
      const chart = echarts.init(this.$refs.scoreDistribution)
      chart.setOption({
        title: {
          text: '成绩分布'
        },
        tooltip: {},
        xAxis: {
          type: 'category',
          data: ['0-60', '60-70', '70-80', '80-90', '90-100']
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: [5, 20, 36, 10, 10],
          type: 'bar'
        }]
      })
    }
  }
}
</script>

<style lang="less" scoped>
.stats-container {
  display: flex;
  margin-top: 20px;

  .chart-wrapper {
    flex: 1;

    .chart {
      width: 100%;
      height: 400px;
    }
  }

  .stats-info {
    width: 300px;
    margin-left: 20px;

    .stats-item {
      margin-bottom: 15px;

      .label {
        color: #606266;
      }

      .value {
        margin-left: 10px;
        font-weight: bold;
        color: #303133;
      }
    }
  }
}

.detail-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;

  h3 {
    margin: 0 0 10px;
    color: #303133;
  }

  p {
    margin: 5px 0;
    color: #606266;
  }
}

.question-list {
  .question-item {
    margin-bottom: 20px;
    padding: 15px;
    background: #f5f7fa;
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

.filter-item {
  margin-right: 10px;
}
</style>