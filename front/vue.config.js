const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  assetsDir: 'static',
  parallel: false,
  publicPath: './',

  devServer: {
    proxy: {
      '/api': {
        target: 'http://localhost:8081', // 开发环境代理到本地后端
        changeOrigin: true,
        pathRewrite: { '^/api': '' }
      }
    }
  },

  // 根据环境设置API基础URL
  chainWebpack: config => {
    config.plugin('define').tap(args => {
      args[0]['process.env'].VUE_APP_API_BASE_URL = JSON.stringify(
          process.env.NODE_ENV === 'production'
              ? '/api'  // 生产环境使用相对路径，由Nginx代理
              : 'http://localhost:8081'  // 开发环境使用本地后端
      );
      return args;
    });
  }
});
