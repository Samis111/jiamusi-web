<template>
  <el-container class="app-wrapper">
    <el-aside :width="asideWidth">
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        :background-color="menuBgColor"
        :text-color="menuTextColor"
        active-text-color="#409EFF"
        router
      >
        <template v-if="userInfo.role === 'teacher'">
          <el-submenu index="/teaching">
            <template slot="title">
              <i class="el-icon-s-management"></i>
              <span>教学管理</span>
            </template>
            <el-menu-item index="/teaching/interaction">教学交互管理</el-menu-item>
            <el-menu-item index="/teaching/courseware">多媒体课件管理</el-menu-item>
            <el-menu-item index="/teaching/exercise">在线练习管理</el-menu-item>
            <el-menu-item index="/teaching/test">测试管理</el-menu-item>
            <el-menu-item index="/teaching/score">学生成绩管理</el-menu-item>
            <el-menu-item index="/teaching/user">用户信息管理</el-menu-item>
          </el-submenu>
        </template>
        <template v-if="userInfo.role === 'student'">
          <el-submenu index="/student">
            <template slot="title">
              <i class="el-icon-s-platform"></i>
              <span>学习中心</span>
            </template>
            <el-menu-item index="/student/exercise">
          
            </el-menu-item>
            <el-menu-item index="/student/test">
              <i class="el-icon-notebook-2"></i>
              <span>在线测试</span>
            </el-menu-item>
            <el-menu-item index="/student/score">
              <i class="el-icon-data-line"></i>
              <span>我的成绩</span>
            </el-menu-item>
            <el-menu-item index="/student/interaction">
              <i class="el-icon-chat-dot-round"></i>
              <span>教学互动</span>
            </el-menu-item>
          </el-submenu>
        </template>
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header :class="headerClass">
        <div class="header-right">
          <span class="welcome-text">欢迎您，{{ userInfo.username }}</span>
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              <i class="el-icon-user"></i>
              <i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item disabled>{{ roleText }}</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { mapState } from 'vuex'

export default {
  name: 'Layout',
  computed: {
    ...mapState('user', ['userInfo']),
    asideWidth() {
      return this.userInfo.role === 'student' ? '180px' : '200px'
    },
    menuBgColor() {
      return this.userInfo.role === 'student' ? '#1890ff' : '#304156'
    },
    menuTextColor() {
      return this.userInfo.role === 'student' ? '#fff' : '#bfcbd9'
    },
    headerClass() {
      return `app-header ${this.userInfo.role}-header`
    },
    roleText() {
      return this.userInfo.role === 'student' ? '学生' : '教师'
    }
  },
  methods: {
    handleCommand(command) {
      if (command === 'logout') {
        this.$store.dispatch('user/logout').then(() => {
          this.$router.push('/login')
        })
      }
    }
  }
}
</script>

<style lang="less" scoped>
.app-wrapper {
  height: 100vh;
  
  .el-menu-vertical {
    height: 100%;
    border-right: none;
  }
  
  .app-header {
    background-color: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 0 20px;
    
    &.student-header {
      background-color: #e6f7ff;
    }
    
    &.teacher-header {
      background-color: #f5f7fa;
    }
    
    .header-right {
      display: flex;
      align-items: center;
      
      .welcome-text {
        margin-right: 20px;
        color: #666;
      }
      
      .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
        display: flex;
        align-items: center;
        
        i {
          margin-left: 5px;
        }
      }
    }
  }
  
  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}

// 学生端特殊样式
.student-menu {
  /deep/ .el-submenu__title {
    i {
      color: #fff;
    }
    
    &:hover {
      background-color: #40a9ff !important;
    }
  }
  
  /deep/ .el-menu-item {
    i {
      margin-right: 10px;
    }
    
    &:hover {
      background-color: #40a9ff !important;
    }
    
    &.is-active {
      background-color: #096dd9 !important;
    }
  }
}
</style> 