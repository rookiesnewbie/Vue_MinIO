import Vue from 'vue'
import ElementUI from 'element-ui'

import './styles/element-variables.scss'
import '@/assets/styles/ruoyi.scss' // ruoyi css

import App from './App'
import router from './router'
import store from './store'
import plugins from './plugins' // plugins
import '@/icons' // icon
import '@/permission' // 权限

import { default as request } from './utils/request'
import { hasPermission } from './utils/hasPermission'
import { parseTime, resetForm, addDateRange, addBeginAndEndTime, handleTree, getTime, parseDate } from '@/utils/ruoyi'

import lang from 'element-ui/lib/locale/lang/zh-CN'
import locale from 'element-ui/lib/locale'
import Pagination from '@/components/Pagination'
// 自定义表格工具扩展
import RightToolbar from '@/components/RightToolbar'

// 设置语言
locale.use(lang)

Vue.use(ElementUI, {
  size: 'small'
})

Vue.use(plugins)

import wsk from './utils/websocket.js' // 引入 websocket
Vue.prototype.$wsk = wsk // 挂载

// 全局方法挂载
Vue.prototype.request = request
Vue.prototype.hasPermission = hasPermission
Vue.prototype.parseTime = parseTime
Vue.prototype.resetForm = resetForm
Vue.prototype.addDateRange = addDateRange
Vue.prototype.addBeginAndEndTime = addBeginAndEndTime
Vue.prototype.handleTree = handleTree
Vue.prototype.getTime = getTime
Vue.prototype.parseDate = parseDate


// 全局组件挂载
Vue.component('Pagination', Pagination)
Vue.component('RightToolbar', RightToolbar)

// 生产环境时自动设置为 false 以阻止 web 在启动时生成生产提示
Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  template: '<App/>',
  components: { App }
})
