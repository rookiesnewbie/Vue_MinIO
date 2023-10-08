<template>
  <div class="dashboard-container">
    <div class="dashboard-text">
      
      <!-- 头部信息 -->
      <el-card class="box-card">
        <div>
          <div class="fl headL">
            <div class="headImg">
              <!-- 处理默认图片,头像 用户头像-->
              <!-- <img v-imgerror="defaultImg" :src="staffPhoto"> -->
              <img src="../../assets/logo/logo.png">
            </div>
            <div class="headInfoTip">
              <p class="firstChild">欢迎您，<span class="col">{{ nickname }}</span>，祝你开心每一天！</p>
              <!-- 用户信息
              <p class="lastChild">
                {{ author }} | - 超管
              </p> -->
            </div>
          </div>
          <div class="fr" />
        </div>
        <FlipClock style="margin-top: 5px;"></FlipClock>
      </el-card>


      <!-- 日程提醒弹窗 -->
      <el-dialog title="今日日程提醒" :visible.sync="visible" width="30%">
            <el-card>
                <div v-html="details" style="line-height: 30px; text-align: left"></div>
                <el-button type="primary" style="margin-top: 90px; margin-left: 397px;" @click="confirm()">ok</el-button>
            </el-card>
      </el-dialog>

      <!-- 主要内容区域 -->
      <el-row type="flex" justify="space-between">
        <!-- 左侧区域内容 -->
        <el-col :span="13" style="padding-right: 26px">
          <!-- 工作日历 -->
          <el-card class="box-card">
            <div slot="header" class="header">
              <span>工作日历</span>
            </div>
            <!-- 放置日历组件 -->
            <WorkCalendar />
          </el-card>         
        </el-col>

          <!-- 右侧区域内容 -->
          <el-col :span="11">
          <!-- 公告 -->
          <el-card class="box-card">
            <div class="advContent">
              <div class="title">公告</div>
              <div class="contentItem">
                <ul class="noticeList">
                  <li v-for="item in missiveList" :key="item.id">
                    <div class="item">
                      <img src="../../assets/bigUserHeader.png" alt="">
                      <div>
                        <p>
                          <span class="col">{{ item.authorName }}</span> 发布了
                          {{ item.name }}
                        </p>
                        <p>发布时间：{{  parseTime(item.createTime) }}</p>
                      </div>

                      <el-button size="mini" type="text" icon="el-icon-search" @click="handleCheck(item.id)"
                     v-if="hasPermission('missive:receive:check')">查看</el-button>
                    </div>
                  </li>
                </ul>
              </div>
            </div>
          </el-card>
         
        </el-col>

      </el-row>

      <!-- 查看公文 -->
      <el-dialog title="查看公文" :visible.sync="openView" append-to-body>
          <template slot="title">
              {{ dialogValue.name }}
          </template>
          <div v-html="dialogValue.content"></div>
          <template slot="footer" class="dialog-footer">
              更新时间：{{ parseTime(dialogValue.updateTime) }}
          </template>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { getMissiveList, getMissiveById, deleteMissiveById } from '@/api/missive'
import empApi from '@/api/employee'
import scheduleApi from '@/api/schedule'
import { mapGetters, mapState } from 'vuex'
import WorkCalendar from '@/components/WorkCalendar'
import FlipClock from '@/components/FlipClock/FlipClock'
export default {
  name: 'dashboard',
  components: {
    WorkCalendar,
    FlipClock
  },
  data() {
    return {
      //当前登录用户信息 
      nickname: '',
      // 遮罩层
      listLoading: true,
      // 总条数
      total: 0,
      // 公文表格数据
      missiveList: [],
      // 员工选项
      enableEmpList: undefined,
      // 是否显示弹出层
      openView: false,
      // 查询参数
      listQuery: {
        pageNo: 1,
        pageSize: 3,
        code: undefined,
        type: undefined,
        status: undefined
      },
      // 日期范围
      dateRange: [],
      // 查看公文对话框数据
      dialogValue: {},
      details: '',
      visible: false,
      date: new Date().toJSON().split('T').join(' ').substr(0, 10)  //获取当前日期
    }
    
  },
  created() {
    let date = new Date().toJSON().split('T').join(' ').substr(0, 10)  //获取当前日期
    this.pageQuery()
    this.getSelectOptions()
    this.currentUser()
    this.showSchedule(date)
  },
  computed: {
    ...mapGetters(['accountId']),
    ...mapState({
      account: state => state.account
    })
  },
  methods: {
    //获取当前登录的用户信息
    currentUser(){
      this.nickname = this.account.nickname
      console.log('前登录的用户', this.nickname)
    },
    /** 查询部门下拉树结构 + 员工选项列表 */
    
    getSelectOptions() {
      empApi.getEnableEmpList().then(response => {
        this.enableEmpList = response.data
      })
    },
    /** 查询公文列表 */
    pageQuery() {
      this.listLoading = true
      this.listQuery.empId = this.accountId
      getMissiveList(this.addDateRange(this.listQuery, [
        this.dateRange[0] ? this.dateRange[0] + ' 00:00:00' : undefined,
        this.dateRange[1] ? this.dateRange[1] + ' 23:59:59' : undefined
      ])).then(response => {
        this.author = response.data.list.authorName
        this.missiveList = response.data.list
        this.total = response.data.total
        this.listLoading = false
        // debugger
        // console.log(this.missiveList)
      })
    },
    /** 查看按钮操作 */
    handleCheck(id) {
      this.dialogValue = {}
      getMissiveById(id).then(response => {
        this.dialogValue = response.data
        this.openView = true
      })
    },

    /**
     * 日程提醒
     * 
     */
    showSchedule(date) {
            // const nowDate = new Date().toJSON().split('T').join(' ').substr(0, 10)  //获取当前日期
            const storageDate = localStorage.getItem("confirm")
            let scheduleForm = {
                createTime: date,
                empId: this.accountId
            }
            
            scheduleApi.getScheduleByDate(scheduleForm).then(response => {
                this.details = response.data.content
                if(/^\s*$/.test(this.details) === false){
                  this.visible = true
                }
                console.log("具体日期的日程",response.data);
            })

           
        },

         /**
         * 确认日程提醒
         */
         confirm() {
            this.visible = false
            localStorage.setItem("confirm", this.date)
        }

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.headImg {
  float: left;
  width: 100px;
  height: 100px;
  border-radius: 50%;
  background: #999;

  img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
}

.headInfoTip {
  padding: 25px 0 0;
  margin-left: 120px;

  p {
    padding: 0 0 15px;
    margin: 0;

    &.firstChild {
      font-size: 24px;
      .col{
        font-weight: bold;
        color: #8a97f8;
      }
    }

    &.lastChild {
      font-size: 20px;
      color: #7f8c8d;
    }
  }
}

.box-card {
  padding: 5px 10px;
  margin-top: 20px;

  .header {
    span {
      color: #2c3e50;
      font-size: 24px;
    }

    .item {
      color: #97a8be;
      float: right;
      padding: 3px 0;
    }
  }

  .headTit {
    span {
      border-bottom: 4px solid #8a97f8;
      padding-bottom: 10px;
    }
  }
}

.advContent {
  background: #fff;
  border-radius: 5px 5px 0px 0px;

  .title {
    font-size: 20px;
    // margin-top: 65px;
    // padding: 20px;
  
    font-weight: bold;
    border-bottom: solid 1px #ccc;
  }

  .contentItem {
    padding: 0 30px;
    min-height: 350px;

    .item {
      display: flex;
      padding: 18px 0 10px;
      border-bottom: solid 1px #ccc;

      .col {
        color: #8a97f8;
      }

      img {
        width: 56px;
        height: 56px;
        border-radius: 50%;
        margin-right: 10px;
      }

      p {
        padding: 0 0 8px;
      }
    }
  }
}
</style>
