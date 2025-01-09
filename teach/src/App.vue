<template>
  <div id="app">
    <router-view />
  </div>
</template>

<style lang="less">
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
}

#app {
  height: 100%;
}

// 重置一些 element-ui 的默认样式
.el-menu {
  border-right: none !important;
}

.el-header {
  padding: 0 !important;
}
</style>
