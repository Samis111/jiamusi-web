<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input
        v-model="listQuery.keyword"
        placeholder="请输入用户名/邮箱"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-select v-model="listQuery.role" placeholder="选择角色" clearable class="filter-item" style="width: 130px">
        <el-option
          v-for="item in roleOptions"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        新建用户
      </el-button>
    </div>

    <el-table
      :data="list"
      border
      style="width: 100%"
      v-loading="listLoading"
    >
      <el-table-column prop="username" label="用户名" width="150" />
      <el-table-column prop="email" label="邮箱" min-width="200" />
      <el-table-column prop="role" label="角色" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.role === 'teacher' ? 'success' : 'info'">
            {{ getRoleName(row.role) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="create_time" label="创建时间" width="160" />
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.status === 1 ? 'success' : 'danger'">
            {{ row.status === 1 ? '正常' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleUpdate(row)">编辑</el-button>
          <el-button 
            type="text" 
            @click="handleStatus(row)"
          >{{ row.status === 1 ? '禁用' : '启用' }}</el-button>
          <el-button type="text" @click="handleResetPwd(row)">重置密码</el-button>
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

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible">
      <el-form ref="dataForm" :model="temp" label-width="100px" :rules="rules">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="temp.username" :disabled="dialogStatus==='update'" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="temp.email" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="temp.role" placeholder="请选择角色">
            <el-option
              v-for="item in roleOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="dialogStatus==='create'">
          <el-input v-model="temp.password" type="password" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="dialogStatus==='create'?createData():updateData()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { 
  getUserList, 
  createUser, 
  updateUser, 
  updateUserStatus,
  resetUserPassword 
} from '@/api/teaching'
import { Message } from 'element-ui'

export default {
  name: 'UserManage',
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
        role: undefined
      },
      dialogVisible: false,
      dialogStatus: '',
      dialogTitle: '',
      roleOptions: [
        { value: 'admin', label: '管理员' },
        { value: 'teacher', label: '教师' },
        { value: 'student', label: '学生' }
      ],
      temp: {
        id: undefined,
        username: '',
        email: '',
        role: '',
        password: '',
        status: 1
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        email: [
          { required: true, message: '请输入邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ],
        role: [{ required: true, message: '请选择角色', trigger: 'change' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getRoleName(role) {
      const roleMap = {
        admin: '管理员',
        teacher: '教师',
        student: '学生'
      }
      return roleMap[role] || '未知'
    },
    getList() {
      this.listLoading = true
      getUserList(this.listQuery).then(response => {
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
    handleCreate() {
      this.temp = {
        id: undefined,
        username: '',
        email: '',
        role: '',
        password: '',
        status: 1
      }
      this.dialogStatus = 'create'
      this.dialogTitle = '新建用户'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleUpdate(row) {
      this.temp = Object.assign({}, row)
      this.dialogStatus = 'update'
      this.dialogTitle = '编辑用户'
      this.dialogVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleStatus(row) {
      const status = row.status === 1 ? 0 : 1
      const statusText = status === 1 ? '启用' : '禁用'
      this.$confirm(`确认${statusText}该用户?`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateUserStatus(row.id, status).then(() => {
          Message.success(`${statusText}成功`)
          this.getList()
        })
      })
    },
    handleResetPwd(row) {
      this.$confirm('确认重置该用户的密码?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resetUserPassword(row.id).then(() => {
          Message.success('密码重置成功')
        })
      })
    },
    createData() {
      this.$refs['dataForm'].validate((valid) => {
        if (valid) {
          createUser(this.temp).then(() => {
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
          updateUser(this.temp).then(() => {
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
.filter-item {
  margin-right: 10px;
}
</style> 