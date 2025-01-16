<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.keyword" placeholder="请输入标题关键字" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.type" placeholder="选择类型" clearable class="filter-item" style="width: 130px">
        <el-option label="讨论" value="discussion" />
        <el-option label="答疑" value="qa" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        {{ listQuery.type === 'qa' ? '提问' : '发起讨论' }}
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="topicTitle" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="type" label="类型" width="100">
        <template slot-scope="{row}">
          {{ row.type === 'discussion' ? '讨论' : '答疑' }}
        </template>
      </el-table-column>
      <el-table-column prop="creator" label="发起人" width="120" />
      <el-table-column prop="topicCreateTime" label="发起时间" width="160" />
      <el-table-column prop="number" label="回复数" width="80" align="center" />
      <el-table-column label="操作" width="100" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleView(row)">查看</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form ref="dataForm" :model="temp" label-width="100px" :rules="rules">
        <el-form-item label="标题" prop="title">
          <el-input v-model="temp.title" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input type="textarea" v-model="temp.content" :rows="4" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="createData">确认</el-button>
      </div>
    </el-dialog>

    <el-dialog title="详情" :visible.sync="detailVisible" width="50%">
      <div v-if="detail">
        <div class="detail-header">
          <h3>{{ detail.topicTitle }}</h3>
          <p>
            <span>发起人: {{ detail.creator }}</span>
            <span style="margin-left: 20px">发起时间: {{ detail.topicCreateTime }}</span>
          </p>
        </div>
        <div class="detail-content">
          {{ detail.number }}
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
        <div class="reply-form">
          <el-input type="textarea" v-model="replyContent" :rows="3" placeholder="请输入回复内容" />
          <el-button type="primary" @click="handleReply" style="margin-top: 10px">
            回复
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import {
  getStudentInteractionList,
  createStudentInteraction,
  getInteractionDetail,
  createInteractionReply
} from '@/api/student'
import { Message } from 'element-ui'

export default {
  name: 'StudentInteraction',
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
        type: 'discussion'
      },
      dialogVisible: false,
      dialogTitle: '',
      detailVisible: false,
      detail: null,
      replyContent: '',
      temp: {
        title: '',
        content: '',
        type: 'discussion'
      },
      rules: {
        title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入内容', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.listLoading = true
      getStudentInteractionList(this.listQuery).then(response => {
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

      let username = window.sessionStorage.getItem('user');
      console.log(username)
      // let user = JSON.stringify(username)
      let user = JSON.parse(username);
      console.log(user)

      this.temp = {
        topicTitle: '',
        content: '',
        topicCreatorId: user.userId,
        number: 0,
        status: 0,
        type: this.listQuery.type
      }
      this.dialogTitle = this.listQuery.type === 'qa' ? '提问' : '发起讨论'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createStudentInteraction(this.temp).then(() => {
            this.dialogVisible = false
            Message.success('创建成功')
            this.getList()
          })
        }
      })
    },
    handleView(row) {
      this.detailVisible = true
      getInteractionDetail(row.topicId).then(response => {
        this.detail = response.data
      })
    },
    handleReply() {
      if (!this.replyContent) {
        Message.warning('请输入回复内容')
        return
      }

      let username = window.sessionStorage.getItem('user');
      console.log(username)
      // let user = JSON.stringify(username)
      let user = JSON.parse(username);
      console.log(user)

      createInteractionReply({
        topicId: this.detail.topicId,
        replyContent: this.replyContent,
        replyCreatorId: user.userId
      }).then(() => {
        Message.success('回复成功')
        this.replyContent = ''
        this.handleView(this.detail)
      })
    }
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