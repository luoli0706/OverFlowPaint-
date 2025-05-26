const { defineConfig } = require('@vue/cli-service')
module.exports = {
    assetsDir: 'static',
  parallel: false,
  publicPath: './',
  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081', // Spring Boot服务器地址
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // 重写路径
        }
      }
    }
  }
}
