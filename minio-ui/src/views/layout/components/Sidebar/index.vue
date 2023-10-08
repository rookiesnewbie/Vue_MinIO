<template>
  <!-- http://element-cn.eleme.io/#/zh-CN/component/menu -->
  <el-menu 
    mode="vertical" 
    background-color="rgb(48, 65, 86)"
    text-color="#fff"
    :default-active="$route.path"
    active-text-color="#409EFF"
  >
  <div style="height: 60px; line-height: 80px; text-align: center">
      <img src="../../../../assets/logo/logo.jpeg" alt="" style="width: 50px; position: relative; top: 5px;">
      <b style="color: white; margin-left: 5px;font-size: large;">OA System</b>
  </div>
    <sidebar-item :routes="permissionRouters" />
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'

export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters(['permissionRouters'])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-menu {
  min-height: 100%;
}
</style>
