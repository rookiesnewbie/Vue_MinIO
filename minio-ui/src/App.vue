<template>
  <div id="app">
    <router-view v-if="isRouterAlive"></router-view>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  name: 'app',
  provide() {
    return {
      reload: this.reload
    }
  },
  computed: {
    ...mapGetters(['accountId'])
  },
  watch: {
    accountId() {
      this.initWebSocket()
    }
  },
  data() {
    return {
      isRouterAlive: true,
      ws: null,
      leave_page: false,
      is_first_in_page: true // 判断是否是第一次进入页面，在 websocket 连接成功时判断是否调用子路由
    }
  },
  methods: {
    reload() {
      this.isRouterAlive = false
      this.$nextTick(function() {
        this.isRouterAlive = true
      })
    },
    // 初始化 websocket
    initWebSocket() {
      if ('WebSocket' in window) {
        if (this.accountId <= 0) {
          return
        }
        const url = 'ws://localhost:8889/websocket/' + this.accountId
        this.ws = new WebSocket(url)
        this.ws.onopen = () => {
          this.$wsk.setWs(this.ws) // 连接成功后，赋值给全局对象 websocket
          console.log('连接 websocket 成功')
          // this.$modal.notify('连接websocket成功')
          if (this.is_first_in_page) return
          setTimeout(() => {
            console.log('重新调用组件中的方法进行发送信息')
            this.send_data()
          }, 100)
        }
        this.ws.onclose = e => {
          console.log('监听到websocket断开', e)
          // 如果是离开页面或者刷新的情况下，则不做重连，否则重连
          if (this.leave_page) {
            console.log('断开连接', e)
          } else {
            console.log('正在重连中')
            this.is_first_in_page = false // 设置为 false, 这样重连后就会调用组件中的方法来订阅信息
            this.initWebSocket()
          }
        }
      } else {
        // 浏览器不支持 WebSocket
        this.$modal.notify({ title: '不支持', message: '您的浏览器不支持 WebSocket，建议使用谷歌或者Microsoft Edge浏览器' })
      }
      setTimeout(() => {
        // 其他代码块
        return this.send_data()
      }, 100)
    },
    // 发送消息
    send_data() {
      const data = {
        type: 'send_data'
      }
      this.$wsk.ws.send(JSON.stringify(data))
      this.$wsk.ws.onmessage = this.websocketonmessage
    },
    websocketonmessage(event) {
      // console.log(event.data) // 接受消息
      this.$modal.notify({ title: '通知', message: event.data })
    }
  },
  destroyed() {
    this.leave_page = true
    this.ws.close()
    this.ws = null
    this.$wsk.setWs(this.ws)
  }
}
</script>

<style lang="scss">
// normalize.css 样式格式化
@import '../node_modules/normalize.css/normalize.css';
// 全局自定义的css样式
@import './styles/index.scss';
</style>
