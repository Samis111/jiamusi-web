<template>
  <div class="app-container">
    <!-- <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="请输入试卷名称" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
    </div> -->

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="paperName" label="试卷名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="totalScore" label="得分" width="100" align="center" />
      <el-table-column prop="total" label="总分" width="100" align="center" />
      <el-table-column prop="testTime" label="考试时间" width="160" />
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />
  </div>
</template>

<script>
import { getStudentScoreList } from '@/api/student'
import Pagination from '@/components/Pagination'

export default {
  name: 'StudentScore',
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
        userId: JSON.parse(localStorage.getItem('userInfo')).userId
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
        this.list = response.data
        this.total = response.data.total
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    }
  }
}
</script>

<style lang="less" scoped>
.filter-item {
  margin-right: 10px;
}
</style> 