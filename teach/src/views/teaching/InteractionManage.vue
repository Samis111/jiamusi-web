<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="请输入标题关键字" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        新建讨论
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="topicTitle" label="标题" />
      <el-table-column prop="topicCreatorId" label="创建者" />
      <el-table-column prop="topicCreateTime" label="创建时间" width="160" />
      <el-table-column prop="number" label="回复数" width="80" align="center" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status === '1' ? 'success' : 'info'">
            {{ row.status === '1' ? '进行中' : '已结束' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleView(row)">查看</el-button>
          <el-button type="text" @click="handleUpdate(row)">编辑</el-button>
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
          <el-button type="text" @click="handleStateOpen(row)">结束</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form ref="dataForm" :model="temp" label-width="100px" :rules="rules">
        <el-form-item label="标题" prop="topicTitle">
          <el-input v-model="temp.topicTitle" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus === 'create' ? createData() : updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <el-dialog title="详情" :visible.sync="detailVisible" width="50%">
      <div v-if="detail">
        <div class="detail-header">
          <h3>讨论的问题：{{ detail.topicTitle }}</h3>
          <p>
            <span>创建者: {{ detail.creator }}</span>
            <span style="margin-left: 20px">创建时间: {{ detail.createTime }}</span>
          </p>
        </div>
        <div class="detail-content">
          {{ detail.content }}
        </div>
        <div class="reply-list">
          <div v-for="(reply, index) in detail.replies" :key="index" class="reply-item">
            <p class="reply-info">
              <span>{{ reply.creator }}</span>
              <span>{{ reply.createTime }}</span>
            </p>
            <div class="reply-content">{{ reply.content }}</div>
          </div>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getInteractionList, createInteraction, updateInteraction, deleteInteraction, getInteractionDetail } from '@/api/teaching'
import { Message } from 'element-ui'

export default {
  name: 'InteractionManage',
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
      dialogStatus: '',
      dialogTitle: '',
      detailVisible: false,
      detail: null,
      replyContent: '',
      temp: {
        id: undefined,
        topicTitle: '',
        status: 1
      },
      rules: {
        topicTitle: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
    console.log(JSON.parse(localStorage.getItem('userInfo')).userId)
    
  },
  methods: {
    getList() {
      this.listLoading = true
      getInteractionList(this.listQuery).then(response => {
        this.list = response.data
    
        this.listLoading = false
      }).catch(() => {
        this.listLoading = false
      })
    },
    handleStateOpen(){

    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.temp = {
        // 获取用户信息
        topicCreatorId: JSON.parse(localStorage.getItem('userInfo')).userId,
        topicTitle: '',
        status: 1,
      }
      this.dialogStatus = 'create'
      this.dialogTitle = '新建讨论'
      this.dialogVisible = true
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑讨论'
      this.dialogVisible = true
    },
    handleView(row) {
      this.detailVisible = true
      // 获取讨论详情
      getInteractionDetail(row.topicId).then(response => {
        this.detail = response.data
      })
      
    },
    handleDelete(row) {
      this.$confirm('确认删除该讨论?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteInteraction(row.id).then(() => {
          Message.success('删除成功')
          this.getList()
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createInteraction(this.temp).then(() => {
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
          updateInteraction(this.temp).then(() => {
            this.dialogVisible = false
            Message.success('更新成功')
            this.getList()
          })
        }
      })
    },
    
  }
}
</script>

<style lang="less" scoped>
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

.detail-content {
  margin-bottom: 30px;
  line-height: 1.6;
}

.reply-list {
  margin: 20px 0;

  .reply-item {
    margin-bottom: 15px;
    padding: 10px;
    background: #f5f7fa;
    border-radius: 4px;

    .reply-info {
      margin: 0 0 10px;
      color: #606266;
      font-size: 13px;

      span {
        margin-right: 15px;
      }
    }

    .reply-content {
      color: #303133;
    }
  }
}

.reply-form {
  margin-top: 20px;
}
</style>