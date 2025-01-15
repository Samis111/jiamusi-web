<template>
  <div class="app-container">
    <div class="filter-container">
      <el-select v-model="listQuery.type" placeholder="选择类型" clearable class="filter-item" style="width: 130px">
        <el-option label="练习" value="exercise" />
        <el-option label="测试" value="test" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div>

    <el-tabs v-model="activeTab" @tab-click="handleTabClick">
      <el-tab-pane label="成绩列表" name="list">
        <el-table
          :data="list"
          border
          style="width: 100%"
          v-loading="listLoading"
        >
          <el-table-column prop="name" label="名称" min-width="200" show-overflow-tooltip />
          <el-table-column prop="type" label="类型" width="100">
            <template slot-scope="{row}">
              {{ row.type === 'exercise' ? '练习' : '测试' }}
            </template>
          </el-table-column>
          <el-table-column prop="score" label="得分" width="100" align="center" />
          <el-table-column prop="totalScore" label="总分" width="100" align="center" />
          <el-table-column prop="submitTime" label="提交时间" width="160" />
          <el-table-column label="操作" width="100" fixed="right">
            <template slot-scope="{row}">
              <el-button type="text" @click="handleDetail(row)">查看详情</el-button>
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
      </el-tab-pane>

      <el-tab-pane label="成绩统计" name="stats">
        <div class="stats-container">
          <div class="chart-wrapper">
            <div ref="scoreChart" class="chart"></div>
          </div>
          <div class="stats-info">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>统计信息</span>
              </div>
              <div class="stats-item">
                <span class="label">总练习/测试数：</span>
                <span class="value">{{ stats.totalCount }}</span>
              </div>
              <div class="stats-item">
                <span class="label">平均分：</span>
                <span class="value">{{ stats.averageScore }}</span>
              </div>
              <div class="stats-item">
                <span class="label">最高分：</span>
                <span class="value">{{ stats.maxScore }}</span>
              </div>
              <div class="stats-item">
                <span class="label">最低分：</span>
                <span class="value">{{ stats.minScore }}</span>
              </div>
              <div class="stats-item">
                <span class="label">及格率：</span>
                <span class="value">{{ stats.passRate }}%</span>
              </div>
            </el-card>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <el-dialog title="答题详情" :visible.sync="detailVisible" width="60%">
      <div v-if="detail">
        <div class="detail-header">
          <h3>{{ detail.name }}</h3>
          <p>得分：{{ detail.score }} / {{ detail.totalScore }}</p>
          <p>提交时间：{{ detail.submitTime }}</p>
        </div>
        <div class="question-list">
          <div v-for="(item, index) in detail.questions" :key="index" class="question-item">
            <div class="question-content">
              <span class="question-no">{{ index + 1 }}. </span>
              {{ item.content }}
            </div>
            <div class="answer-info">
              <p>你的答案：{{ item.studentAnswer }}</p>
              <p>正确答案：{{ item.correctAnswer }}</p>
              <p>得分：{{ item.score }} / {{ item.totalScore }}</p>
            </div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getStudentScoreList, getScoreDetail, getStudentStats } from '@/api/student'
import * as echarts from 'echarts'

export default {
  name: 'StudentScore',
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
        type: undefined
      },
      detailVisible: false,
      detail: null,
      stats: {
        totalCount: 0,
        averageScore: 0,
        maxScore: 0,
        minScore: 0,
        passRate: 0
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getStudentScoreList(this.listQuery).then(response => {
        this.list = response.data.items
        this.total = response.data.total
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
        this.detail = response.data
      })
    },
    handleTabClick() {
      if (this.activeTab === 'stats') {
        this.getStats()
      }
    },
    getStats() {
      getStudentStats().then(response => {
        this.stats = response.data
        this.initChart()
      })
    },
    initChart() {
      const chart = echarts.init(this.$refs.scoreChart)
      chart.setOption({
        title: {
          text: '成绩趋势'
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: this.stats.dates
        },
        yAxis: {
          type: 'value',
          min: 0,
          max: 100
        },
        series: [{
          data: this.stats.scores,
          type: 'line',
          smooth: true
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
</style> 