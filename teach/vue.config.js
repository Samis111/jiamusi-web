const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  //关闭eslint
  lintOnSave: false,

  devServer: {
    port: 8080,
    open: true,
    proxy: {
      '/api':{
        target:'http://localhost:8082',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '/'
        }
      } 
  
    },
  }



})
