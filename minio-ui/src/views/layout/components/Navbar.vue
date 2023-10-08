<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger class="hamburger-container" :toggleClick="toggleSideBar" :isActive="sidebar.opened" />
    <Levelbar />
    <span class="currentDate">当前日期：{{ currentDate }}</span>
    <el-dropdown class="account-container">
      <span class="el-dropdown-link">
        当前用户：<span class="nickname">{{ nickname }}</span>
        &nbsp 设置<i class="el-icon-arrow-down el-icon-setting"></i>
      </span>
      <el-dropdown-menu slot="dropdown">
        <router-link class="inlineBlock" to="/accountCenter/index">
          <el-dropdown-item>个人中心</el-dropdown-item>
        </router-link>
        <el-dropdown-item divided>
          <span @click="logout" style="display:block;">退出系统</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-menu>
</template>

<script>
import { mapGetters } from 'vuex'
import Levelbar from './Levelbar'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Levelbar,
    Hamburger
  },
  data(){
    return{
      currentDate:''
    }
  },
  mounted() {
    const nowDate = new Date()
    this.currentDate = `${nowDate.getFullYear()}年${nowDate.getMonth() + 1}月${nowDate.getDate()}日`
  },
  computed: {
    ...mapGetters(['nickname', 'sidebar', 'avatar'])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$modal.confirm('是否确认退出?').then(() =>{
        this.$store.dispatch('Logout').then(() =>{
          location.reload() // 为了重新实例化vue-router对象 避免bug
          this.$message({
                      type: 'success',
                      message: '退出成功！！！'
            })
        })
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 55px;
  line-height: 55px;
  border-radius: 0 !important;
  .hamburger-container {
    line-height: 58px;
    height: 55px;
    float: left;
    padding: 0 10px;
  }
  .errLog-container {
    display: inline-block;
    position: absolute;
    right: 150px;
  }
  .account-container {
    height: 45px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .el-dropdown-link {
      cursor: pointer;
      color: #409eff;
      .nickname{
        color: brown;
        font-weight: bold;
        color: #8a97f8;
      }
    }
    .el-icon-arrow-down {
      font-size: 12px;
    }
  }

  .currentDate{
    height: 45px;
    display: inline-block;
    position: absolute;
    right: 285px;
    font-weight: bold;
  }
}
</style>
