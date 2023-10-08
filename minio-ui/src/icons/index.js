import Vue from 'vue'
import IconSvg from '@/components/Icon-svg'// svg组件

// 注册全局组件
Vue.component('icon-svg', IconSvg)

/*
require.context有三个参数：

    directory：说明需要检索的目录
    useSubdirectories：是否检索子目录
    regExp: 匹配文件的正则表达式
*/
const requireAll = requireContext => requireContext.keys().map(requireContext)
// const req = require.context('./svg', false, /\.svg$/)
const req = require.context('./svg', true, /\.svg$/)
requireAll(req)
