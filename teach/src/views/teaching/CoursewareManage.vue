<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.coursewareName" placeholder="请输入课件名称" style="width: 200px;" class="filter-item"
        @keyup.enter.native="handleFilter" />
      <el-select v-model="listQuery.coursewareTypeId" placeholder="选择类型" clearable class="filter-item"
        style="width: 130px">
        <el-option v-for="item in coursewareTypes" :key="item.type_id" :label="item.type_name" :value="item.type_id" />
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
        搜索
      </el-button>
      <el-button class="filter-item" type="primary" icon="el-icon-plus" @click="handleCreate">
        上传课件
      </el-button>
    </div>

    <el-table :data="list" border style="width: 100%" v-loading="listLoading">
      <el-table-column prop="coursewareName" label="课件名称" min-width="200" show-overflow-tooltip />
      <el-table-column prop="type" label="类型">
        <template slot-scope="{row}">
          {{ getCoursewareTypeName(row.coursewareTypeId) }}
        </template>
      </el-table-column>
      <el-table-column prop="size" label="大小">
        <template slot-scope="{row}">
          {{ formatFileSize(row.size) }}
        </template>
      </el-table-column>
      <el-table-column prop="creatorName" label="上传者" width="120" />
      <el-table-column prop="coursewareCreateTime" label="上传时间" width="160" />
      <el-table-column label="操作" width="200">
        <template slot-scope="{row}">
          <el-button type="text" @click="handleDownload(row)">下载</el-button>
       
          <el-button type="text" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit"
      @pagination="getList" />

    <el-dialog title="上传课件" :visible.sync="uploadVisible">
      <el-form ref="uploadForm" :model="uploadData" :rules="uploadRules" label-width="80px">
        <el-form-item label="课件名称" prop="coursewareName">
          <el-input v-model="uploadData.coursewareName" placeholder="请输入课件名称" />
        </el-form-item>
        <el-form-item label="课件类型" prop="coursewareTypeId">
          <el-select v-model="uploadData.coursewareTypeId" placeholder="请选择课件类型">
            <el-option v-for="item in coursewareTypes" :key="item.type_id" :label="item.type_name"
              :value="item.type_id" />
          </el-select>
        </el-form-item>
      </el-form>
      <el-upload class="upload-demo" drag action="/api/file/upload"
        :headers="{ Authorization: `Bearer ${this.$store.state.user.token}` }" :data="uploadData" multiple
        :before-upload="handleBeforeUpload" :on-success="handleUploadSuccess" :on-error="handleUploadError">
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">支持 PDF、PPT、Word、视频等格式</div>
      </el-upload>
    </el-dialog>

    <el-dialog title="预览" :visible.sync="previewVisible" width="80%" top="5vh">
      <div v-if="previewUrl" class="preview-container">
        <iframe :src="previewUrl" frameborder="0"></iframe>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import Pagination from '@/components/Pagination'
import { getCoursewareList, uploadCourseware, deleteCourseware, addCourseware } from '@/api/teaching'
import { Message } from 'element-ui'

export default {
  name: 'CoursewareManage',
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
      uploadVisible: false,
      previewVisible: false,
      previewUrl: '',
      coursewareTypes: [
        { type_id: 1, type_name: 'PDF文档' },
        { type_id: 2, type_name: 'PPT演示文稿' },
        { type_id: 3, type_name: 'Word文档' },
        { type_id: 4, type_name: '视频文件' }
      ],
      uploadData: {
        coursewareName: '',
        coursewareTypeId: undefined
      },
      uploadRules: {
        name: [{ required: true, message: '请输入课件名称', trigger: 'blur' }],
        type: [{ required: true, message: '请选择课件类型', trigger: 'change' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getCoursewareTypeName(typeId) {
      const type = this.coursewareTypes.find(t => t.type_id === typeId)
      return type ? type.type_name : '其他'
    },
    formatFileSize(size) {
      if (size < 1024) {
        return size + 'B'
      } else if (size < 1024 * 1024) {
        return (size / 1024).toFixed(2) + 'KB'
      } else {
        return (size / 1024 / 1024).toFixed(2) + 'MB'
      }
    },
    getList() {
      this.listLoading = true
      getCoursewareList(this.listQuery).then(response => {
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
      this.uploadVisible = true
      this.uploadData = {
        name: '',
        type: undefined
      }
    },
    handleDownload(row) {
      console.log(row)
      window.open(row.coursewarePath)
    },
  
    handleBeforeUpload(file) {
      if (!this.uploadData.coursewareName || !this.uploadData.coursewareTypeId) {
        Message.warning('请先填写课件信息')
        return false
      }
      return true
    },
    handleDelete(row) {
      this.$confirm('确认删除该课件?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCourseware(row.coursewareId).then(() => {
          Message.success('删除成功')
          this.getList()
        })
      })
    },
    handleUploadSuccess(response) {
      Message.success('上传成功')

      let username = window.sessionStorage.getItem('user');
      console.log(username)
      // let user = JSON.stringify(username)
      let user = JSON.parse(username);
      console.log(user)


      // let user = JSON.parse(username);
      console.log(response.data)
      let data = {
        coursewareName: this.uploadData.coursewareName,
        coursewareTypeId: this.uploadData.coursewareTypeId,
        coursewarePath: response.data,
        coursewareCreatorId: user.userId,
        size: '',
      };

      addCourseware(data).then(res => {
        this.uploadVisible = false
        this.getList()
      })


    },
    handleUploadError() {
      Message.error('上传失败')
    }
  }
}
</script>

<style lang="less" scoped>
.preview-container {
  height: 80vh;

  iframe {
    width: 100%;
    height: 100%;
  }
}

.upload-demo {
  margin-top: 20px;
}
</style>