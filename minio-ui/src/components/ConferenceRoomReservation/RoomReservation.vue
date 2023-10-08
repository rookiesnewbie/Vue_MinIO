<template>
  <div class="resRoomMain">
    <div class="roomReserveMain">
      <v-room-con-filter ref="vRoomConFilter" @change="pageQuery()" :insert-room-code="this.$route.query.roomCode ? this.$route.query.roomCode : undefined"></v-room-con-filter>
      <v-room-plugin ref="vRoomPlugin" @click="pageQuery()" :room-lst="roomLst" @changeResInfo="changeResInfo"></v-room-plugin>

      <div id="roomResPag">
        <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize"
                @pagination="pageQuery"/>
      </div>

      
      <div id="resRoomInfo">
        <h1>会议室预订信息：</h1>
        <ul>
          <li class="resRoomInfoItem">
            <span class="resRoomInfoItemTitle">会议室名称：</span>
            <span>{{resRoomInfo.roomCode}}</span>
          </li>
          <li class="resRoomInfoItem">
            <span class="resRoomInfoItemTitle">预 订 时 间：</span>
            <span>{{resRoomInfo.date}}</span>
            <span v-if="resRoomInfo.week">（</span>
            <span>{{resRoomInfo.week}}</span>
            <span v-if="resRoomInfo.week">）</span>
            <span>{{resRoomInfo.startTime}}</span>
            <span v-if="resRoomInfo.startTime">——</span>
            <span>{{resRoomInfo.endTime}}</span>
          </li>
        </ul>
      </div>
      <div id="resBtnWrap">
        <el-button type="primary" size="large" @click="handleSureRes">确定预订</el-button>
      </div>

      <!-- 预订会议对话框 -->
    <el-dialog title="预订会议" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="92px">
        <el-form-item label="会议室编号" prop="roomCode">
          <el-input v-model="form.roomCode" disabled />
        </el-form-item>
        <el-form-item label="会议日期" prop="date">
          <el-input v-model="form.date" disabled />
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-input v-model="form.startTime" disabled />
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-input v-model="form.endTime" disabled />
        </el-form-item>
        <el-form-item label="会议主题" prop="subject">
          <el-input v-model="form.subject" />
        </el-form-item>
        <el-form-item label="会议预订人" prop="resEmpId">
          <el-select @change="currOperatorChange" v-model="form.resEmpId" placeholder="请输入拟稿人" clearable style="width: 100%">
            <el-option v-for="item in enableEmpList" :key="parseInt(item.id)" :label="item.name" :value="parseInt(item.id)" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">提 交</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    </div>
  </div>
</template>

<script>
import vRoomConFilter from './vRoomConFilter'
import vRoomPlugin from './vRoomPlugin'

import 'font-awesome/css/font-awesome.css'
import 'element-ui/lib/theme-chalk/index.css'

import { pageQueryForRes, addConferenceReservation } from '@/api/conferenceReservation'
import empApi from '@/api/employee'
import { mapGetters } from 'vuex'

export default {
  name: 'RoomResPlug',
  data() {
    return {
      roomLst: [],
      total: 0,
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        resDate: undefined
      },
      resRoomInfo: {
        date: undefined,
        week: undefined,
        startTime: undefined,
        endTime: undefined,
        roomCode: undefined,
        subject: undefined,
        resEmpId: undefined
      },
      open: false,
      form: {},
      // 员工选项
      enableEmpList: undefined,
      // 表单校验
      rules: {
        roomCode: [
          { required: true, message: '会议室编号不能为空', trigger: 'blur' }
        ],
        date: [
          { required: true, message: '会议日期不能为空', trigger: 'blur' }
        ],
        startTime: [
          { required: true, message: '开始时间不能为空', trigger: 'blur' }
        ],
        endTime: [
          { required: true, message: '结束时间不能为空', trigger: 'blur' }
        ],
        subject: [
          { required: true, message: '会议主题不能为空', trigger: 'blur' }
        ],
        resEmpId: [
          { required: true, message: '会议预订人不能为空', trigger: 'blur' }
        ]
      }
    }
  },
  components: {
    vRoomConFilter,
    vRoomPlugin
  },
  methods: {
    /** 查询员工选项列表 */
    getSelectOptions() {
      empApi.getEnableEmpList().then(response => {
        this.enableEmpList = response.data
      })
    },
    pageQuery() {
      const subValue = this.$refs.vRoomPlugin.dateLst
      for (let i = 0; i < subValue.length; i++) {
        if (subValue[i].active === true) {
          this.listQuery.resDate = subValue[i].date
          break
        }
      }
      this.listQuery.roomCode = this.$refs.vRoomConFilter.roomCode ? this.$refs.vRoomConFilter.roomCode : undefined
      this.listQuery.equipmentNames = this.$refs.vRoomConFilter.equipmentNames ? this.$refs.vRoomConFilter.equipmentNames : undefined
      pageQueryForRes(this.listQuery).then(response => {
        this.roomLst = response.data.list
        this.total = response.data.total
      })
    },
    changeResInfo(resRoomInfo) {
      this.resRoomInfo = resRoomInfo
    },
    // 表单重置
    reset() {
      this.form = {
        date: undefined,
        week: undefined,
        startTime: undefined,
        endTime: undefined,
        roomCode: undefined,
        subject: undefined,
        resEmpId: undefined
      }
      this.resetForm('form')
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    handleSureRes() {
      if (this.resRoomInfo.date === undefined || this.resRoomInfo.roomCode === undefined ||
      this.resRoomInfo.startTime === undefined || this.resRoomInfo.endTime === undefined) {
        this.$modal.msgWarning('请先确定会议预订信息')
        return
      }
      this.reset()
      this.form.resEmpId = this.accountId
      this.form.roomCode = this.resRoomInfo.roomCode
      this.form.date = this.resRoomInfo.date
      this.form.startTime = this.resRoomInfo.startTime
      this.form.endTime = this.resRoomInfo.endTime
      this.open = true
    },
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          addConferenceReservation(this.form).then(response => {
            this.$modal.msgSuccess('会议预订成功')
            this.open = false
            this.pageQuery()
          }).catch(error => {
            this.$modal.msgError('会议预订失败：' + error)
          })
        }
      })
    },
    // 预订人选中项发生变化
    currOperatorChange(val) {
      if (val) {
        this.$set(this.form, this.form.resEmpId, val.value)
      } else {
        this.$set(this.form, this.form.resEmpId, undefined)
      }
    }
  },
  mounted() {
    this.pageQuery() // 获取会议室信息
    this.getSelectOptions()
  },
  created() {},
  computed: {
    ...mapGetters(['accountId'])
  }
}

</script>

<style scoped lang="less">
  @import './index.less';
  // @import './reset.css';

  @sumW: 900px;
  @roomTopSideW: 120px;
  @topRightW: @sumW - @roomTopSideW;

  .resRoomMain {
    width: @centerW;
    margin: 20px auto;
    background-color: #fff;
    padding: 20px 0;
  }

  .roomReserveMain {
    width: @sumW;
    min-width: @sumW;
    margin: 0 auto;
  }

  //预订信息卡片
  #resRoomInfo {
    margin-top: 30px;
    margin-bottom: 20px;
    color: @fontColor;

    h1 {
      text-align: center;
      font-size: 16px;
      margin-bottom: 10px;
    }

    .resRoomInfoItem {
      padding: 5px 0;

      .resRoomInfoItemTitle {
        margin-right: 10px;
      }
      span {
        font-size: 14px;
      }
    }
  }


  #roomResPag {
    display: flex;
    justify-content: flex-end;
    padding: 15px 0;
  }

  #resBtnWrap {
    text-align: center;
    padding: 15px 0;
    // margin-right: 10px;
  }

  li {
    text-align: center;
    list-style-image: none;
    list-style-position: outside;
    list-style-type: none;
  }


  // screen >= 1200
  @media screen and (min-width:1200px) {}

  // 1200>= screen >=992
  @media screen and (max-width:1200px) {}

  @media screen and (max-width:992px) {}

  @media screen and (max-width:768px) {}

  @media screen and (max-width:480px) {}

</style>
