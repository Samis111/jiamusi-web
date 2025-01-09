<template>
  <el-container class="app-wrapper">
    <el-aside width="200px">
      <el-menu
        :default-active="$route.path"
        class="el-menu-vertical"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
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
      </el-menu>
    </el-aside>
    
    <el-container>
      <el-header>
        <div class="header-right">
          <el-dropdown @command="handleCommand">
            <span class="el-dropdown-link">
              {{ userInfo.username }}<i class="el-icon-arrow-down el-icon--right"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
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
    ...mapState('user', ['userInfo'])
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
  
  .el-header {
    background-color: #fff;
    border-bottom: 1px solid #e6e6e6;
    display: flex;
    align-items: center;
    justify-content: flex-end;
    padding: 0 20px;
    
    .header-right {
      .el-dropdown-link {
        cursor: pointer;
        color: #409EFF;
      }
    }
  }
  
  .el-main {
    background-color: #f0f2f5;
    padding: 20px;
  }
}
</style> 