<template>
  <div class="resRoomPluginBox">
    <div class="resHead">
      <!-- <h2 class="resHeadRegionName">【福州】 会议室（ {{regionName}} ）</h2> -->
      <div class="identifyLst">
        <li class="identify" v-for="(item,key) in identifyLst" :key="key">
          <i :class="{'nooccupy':key ==1,'select':key == 2}"></i><span>{{item}}</span>
        </li>
      </div>
    </div>
    <div class="roomRerservation">
      <div class="roomTop">
        <div class="roomTopSide">
          <span class="titleTime">时间</span>
          <span class="titleName">名称</span>
        </div>
        <div class="roomTopTime">
          <ul class="roomDate">
            <li :key="key" class="dateItem" v-for="(date,key) in dateLst" :class="{'active':date.active}" @click="changeDate(key)"><span>{{date.week}}<br />{{date.date}}</span></li>
          </ul>
          <div class="arrowHourWrap">
            <i class="arrow arrowLeft fa fa-caret-left" :class="{'disabled': moveStep == 1}" @click="moveHour(true)"></i>
            <div class="roomTopHour">
              <ul class="hourNavLst" :class="{'stepOne':moveStep == 1,'stepTwo':moveStep == 2,'stepThree':moveStep == 3,'stepFour':moveStep == 4}">
                <li class="hourNavItem" :key="index" v-for="(item,index) in 24">{{index}}:00</li>
              </ul>
            </div>
            <i class="arrow arrowRight  fa fa-caret-right" :class="{'disabled': moveStep == 4}" @click="moveHour(false)"></i>
          </div>
        </div>
      </div>
      <div class="roomMain">
        <ul class="roomLst">
          <li class="roomItem" :key="key" v-for="(item,key) in roomLst">
            <h3 class="roomName" :title="item.roomCode">{{item.roomCode}}</h3>
            <div class="roomGridWrap">
              <ul class="roomGridLst" :class="{'stepOne':moveStep == 1,'stepTwo':moveStep == 2,'stepThree':moveStep == 3,'stepFour':moveStep == 4}">
                <div v-if="markLine.isShow" class="markLeftLine" :style="{'left':markLine.leftLine+'px'}"></div>
                <div v-if="markLine.isShow" class="markRightLine" :style="{'left':markLine.rightLine+'px'}"></div>
                <li class="roomGrid" v-for="(grid,index) in item.gridLst" :key="index" :class="{'isOccupy':grid.status == 1,'isSelect':grid.status == 2}"
                  @mousedown="mouseDownGrid(key,index,$event)" @mouseup='mouseUpGrid(key,index,$event)' @mouseover='mouseOverGrid(key,index)'></li>
              </ul>
            </div>
          </li>
        </ul>
        <div class="noneBox"></div>
      </div>
    </div>
  </div>
</template>

<script>
  import moment from 'moment'
  import $ from 'jquery'

  export default {
    name: 'vRoomPlugin',
    data() {
      return {
        regionName: null,
        isMoving: false, // 滑动状态
        triggerIndex: null, // 点击的行数
        moveStep: 3,
        initGrid: {
          startIndex: null,
          endIndex: null
        },
        markLine: {
          isShow: false,
          leftLine: 0,
          rightLine: 0
        },
        resRoomInfo: {
          region: null,
          date: null,
          week: null,
          startTime: null,
          endTime: null,
          roomName: null,
          user: null
        },
        identifyLst: ['被占用', '空闲', '选择中'],
        dateLst: []
      }
    },
    watch: {
      roomLst() {
        this.handleRoomLst() // 对数据进行处理
        this.hideMarkLine()
      }
    },
    props: {
      roomLst: {}
    },
    components: {},
    methods: {
      mouseOverGrid(key, index) {
        const isMoving = this.isMoving
        if (!isMoving) return false
        const triggerIndex = this.triggerIndex
        const curGrid = this.roomLst[triggerIndex] // 使得其他行的移动也可触发mouseDown行的状态变化
        const initStartIndex = this.initGrid.startIndex // 第一次点击的grid信息
        const initEndIndex = this.initGrid.endIndex
        const gridStatus = curGrid.gridLst[index].status
        let curStartIndex = curGrid.startSelectIndex
        let curEndIndex = curGrid.endSelectIndex
        if (gridStatus === 1) {
          curGrid.startSelectIndex = null
          curGrid.endSelectIndex = null
          curGrid.gridLst.forEach((grid, index) => {
            if (grid.status === 2) grid.status = 0
          })
          this.$message({
            message: '选择时间段中有被占用，请重新选择！',
            type: 'warning'
          })
          this.hideMarkLine()
          this.initResRoom()
          this.initState()
          return false
        } else {
          if (index <= initStartIndex) {
            curStartIndex = index
            curEndIndex = initEndIndex
          } else if (index >= initEndIndex) {
            curEndIndex = index
            curStartIndex = initStartIndex
          }
        }
        // 清除所有状态
        this.roomLst[triggerIndex].gridLst.forEach((grid, index) => {
          if (grid.status === 2) grid.status = 0
        })
        this.roomLst[triggerIndex].startSelectIndex = curStartIndex
        this.roomLst[triggerIndex].endSelectIndex = curEndIndex

        for (let i = curStartIndex; i <= curEndIndex; i++) {
          this.roomLst[triggerIndex].gridLst[i].status = 2
        }
        this.getMarkLinePostion(curStartIndex, curEndIndex) // 获取markLine的信息
      },
      mouseDownGrid(key, index, event) {
        this.triggerIndex = key // 保存当前触发行
        if (event.button !== 0) return false
        const curGrid = this.roomLst[key]
        let startSelectIndex = curGrid.startSelectIndex
        let endSelectIndex = curGrid.endSelectIndex
        const gridStatus = curGrid.gridLst[index].status
        this.removeOtherLineCss(key) // 清除所有选中样式
        this.isMoving = true // 启动滑动状态
        if (gridStatus === 1) {
          this.hideMarkLine() // 隐藏markLine
          this.$message({
            message: '时间已占用，请重新选择！',
            type: 'warning'
          })
          this.initResRoom()
          this.initState()
          return false // 如果是占用状态，不可点击
        } else if (gridStatus === 2) { // 如果是已选择状态
          if (index === startSelectIndex && index === endSelectIndex) { // 一行已选择的只有一个格子
            startSelectIndex = null
            endSelectIndex = null
            this.roomLst[key].gridLst[index].status = 0
            this.hideMarkLine() // 隐藏markLine
          } else if (index === startSelectIndex) {
            this.isMoving = false
            startSelectIndex = index + 1
            this.roomLst[key].gridLst[index].status = 0
          } else if (index === endSelectIndex) {
            this.isMoving = false
            endSelectIndex = index - 1
            this.roomLst[key].gridLst[index].status = 0
          }
        } else {
          if (startSelectIndex == null) {
            startSelectIndex = index
            endSelectIndex = index
          } else if (index <= startSelectIndex) {
            startSelectIndex = index
          } else if (index >= endSelectIndex) {
            endSelectIndex = index
          }
        }
        this.roomLst[key].startSelectIndex = startSelectIndex
        this.roomLst[key].endSelectIndex = endSelectIndex
        this.initGrid.startIndex = startSelectIndex // 用于全局保存当前grid的index信息
        this.initGrid.endIndex = endSelectIndex
        if (startSelectIndex != null) {
          for (let i = startSelectIndex; i <= endSelectIndex; i++) {
            if (this.roomLst[key].gridLst[i].status === 1) {
              this.roomLst[key].startSelectIndex = null
              this.roomLst[key].endSelectIndex = null
              curGrid.gridLst.forEach((grid, index) => {
                if (grid.status === 2) grid.status = 0
              })
              this.$message({
                message: '选择时间段中有被占用，请重新选择！',
                type: 'warning'
              })
              this.hideMarkLine()
              this.initResRoom()
              this.initState()
              break
            }
            this.roomLst[key].gridLst[i].status = 2
            this.getMarkLinePostion(startSelectIndex, endSelectIndex) // 获取markLine的信息
          }
        }
      },
      mouseUpGrid(key, index, event) {
        if (event.button !== 0) return false
        this.getResRoomInfo() // 获取会议室预订信息
        this.initState() // 初始化所有信息
        // this.triggerIndex = null //恢复初始值,屏蔽，当快速划过时会有小概率出现this.triggerIndex = null，导致this.roomLst[triggerIndex]无法获取正确的值
      },
      // 获取会议室预订信息
      getResRoomInfo() {
        const triggerIndex = this.triggerIndex
        if (triggerIndex == null) {
          return false
        }
        const triggerRoom = this.roomLst[triggerIndex]
        const startSelectIndex = triggerRoom.startSelectIndex
        const endSelectIndex = triggerRoom.endSelectIndex
        const roomCode = triggerRoom.roomCode
        if (startSelectIndex != null) {
          this.resRoomInfo.roomCode = roomCode
          this.resRoomInfo.startTime = this.getTimeFromIndex(startSelectIndex, 0)
          this.resRoomInfo.endTime = this.getTimeFromIndex(endSelectIndex, 1)
          this.dateLst.forEach((item, key) => {
            if (item.active) {
              this.resRoomInfo.week = item.week
              this.resRoomInfo.date = item.date
            }
          })
        } else {
          this.initResRoom() // 初始resRoomInfo
        }
        this.$emit('changeResInfo', this.resRoomInfo)
      },
      // 根据index获取开始结束时间
      getTimeFromIndex(index, type) {
        if (type) index = index + 1
        const hourTime = parseInt(index / 2)
        const complement = index % 2
        const minuteTime = complement === 1 ? '30' : '00'
        const time = hourTime + ':' + minuteTime
        return time
      },
      // 初始化预订信息
      initResRoom() {
        this.resRoomInfo.date = null
        this.resRoomInfo.week = null
        this.resRoomInfo.startTime = null
        this.resRoomInfo.endTime = null
        this.resRoomInfo.roomName = null
        this.resRoomInfo.region = null
        this.resRoomInfo.user = null
      },
      // 初始化相关信息
      initState() {
        this.isMoving = false // 关闭滑动状态
        this.initGrid.startIndex = null
        this.initGrid.endIndex = null
      },
      // 单行选择，清除其他行样式
      removeOtherLineCss(index) {
        this.roomLst.forEach((item, key) => {
          item.startSelectIndex = null
          item.endSelectIndex = null
          item.gridLst.forEach((grid, key) => {
            if (grid.status === 2) grid.status = 0
          })
        })
      },
      // 获取markLine的位置信息
      getMarkLinePostion(startSelectIndex, endSelectIndex) {
        const gridW = $('.roomGrid').outerWidth()
        this.markLine.leftLine = -1 + startSelectIndex * (gridW)
        this.markLine.rightLine = -1 + (endSelectIndex + 1) * (gridW)
        this.markLine.isShow = true
      },
      // 隐藏markLine
      hideMarkLine() {
        this.markLine.isShow = false
      },
      // 移动时间标尺
      moveHour(isMoveLeft) {
        if (isMoveLeft) {
          if (this.moveStep > 1) this.moveStep--
        } else {
          if (this.moveStep < 4) this.moveStep++
        }
      },
      // 获取会议室列表
      getRoomLst() {
        // this.roomLst = getRoomLst().roomLst
        this.$emit('click')
      },
      // 修改会议室列表的数据格式
      handleRoomLst() {
        const curDate = moment().format('YYYY/MM/DD')
        const curTime = moment().format('HH:mm')
        const activeDate = this.dateLst.find(date => date.active).date
        const curTimeArr = curTime.split(':')
        const alreadyPastTimeEndIndex = this.getIndexFromTime(curTimeArr[1] >= '30' ? curTimeArr[0] + ':30' : curTimeArr[0] + ':00', 1)
        this.roomLst.forEach((room, index) => {
          const gridLst = []
          for (let i = 0; i < 48; i++) {
            gridLst.push({
              // 0空闲
              status: 0
            })
          }
          room.startSelectIndex = null
          room.endSelectIndex = null
          room.gridLst = gridLst
          const resTimes = room.resTimes
          resTimes.forEach((time, key) => {
            const startTime = time.startTime
            const endTime = time.endTime
            const startIndex = this.getIndexFromTime(startTime, 0)
            const endIndex = this.getIndexFromTime(endTime, 1)
            for (let i = startIndex; i <= endIndex; i++) {
              // 1被占用
              room.gridLst[i].status = 1
            }
          })
          // 将当天当前时间之前的时间段设置为占用
          if (activeDate === curDate) {
            for (let i = 0; i <= alreadyPastTimeEndIndex; i++) {
              room.gridLst[i].status = 1
            }
          }
        })
      },
      getIndexFromTime(time, type) {
        const hourTime = parseInt(time.split(':')[0])
        let minuteTime
        if (type === 0) {
          minuteTime = time.split(':')[1] === '30' ? 1 : 0
        } else {
          minuteTime = time.split(':')[1] === '30' ? 0 : 1
        }
        // type为0表示time为起始时间，index需要加1；为1表示time为结束时间，index需要减1
        const index = type === 0 ? 2 * hourTime + minuteTime : 2 * hourTime - minuteTime
        return index
      },
      getDateLst() {
        for (let i = 0; i < 7; i++) {
          let active = false
          const date = moment().add(i, 'days').format('YYYY/MM/DD')
          const weekLst = ['日', '一', '二', '三', '四', '五', '六']
          const dateLocal = moment().add(i, 'days')
          const week = '星期' + weekLst[dateLocal._d.getDay()]
          if (i === 0) active = true
          this.dateLst.push({
            week,
            date,
            active
          })
        }
      },
      changeDate(key) {
        this.dateLst.forEach((item, key) => {
          item.active = false
        })
        this.dateLst[key].active = true
        this.getRoomLst()
        this.hideMarkLine()
      }

    },
    mounted() {
      this.getDateLst() // 获取日期列表
      // this.getRoomLst()
    },
    created() {}
  }

</script>

<style>

</style>
<style lang="less" scoped>
  @import './index.less';
  @import './reset.css';

  @disabledColor: #c8c9cc; //not-allowd颜色
  @hourColor: fadeout(@themeColor, 80%); //时间滚条颜色
  @markLineColor: #f76262; //markline颜色
  @barGridH: 25px;
  @roomTopSideW: 120px;
  @sumW: 900px;
  @topRightW: @sumW - @roomTopSideW;
  @gridW: (@topRightW - @barGridH - @barGridH) / 24;
  @gridH: @gridW;
  @baseBorder: 1px solid @baseColor;

  // *{user-select:none}
  // 预订表格插件样式
  .resRoomPluginBox {
    width: 100%;
  }

  .resHead {
    display: flex;

    .resHeadRegionName {
      font-weight: 400;
      color: @fontColor;
      font-size: 14px;
      position: relative;
      top: 10px;
    }

    .identifyLst {
      display: flex;
      justify-content: flex-end;
      flex: 1;

      .identify {
        padding: 8px 0;
        margin-left: 10px;

        i {
          display: inline-block;
          width: 15px;
          height: 15px;
          background-color: @disabledColor;
          position: relative;
          top: 3px;
          box-sizing: border-box;
        }

        i.nooccupy {
          background-color: #fff;
          border: 1px solid @disabledColor;
        }

        i.select {
          background-color: @themeColor;
        }

        span {
          font-size: 12px;
          margin-left: 2px;
          color: @fontColor;
        }
      }
    }
  }

  .roomRerservation {
    width: 100%;
    border: 1px solid @baseColor;
    background-color: #fff;
  }

  .roomTop {
    width: 100%;
    height: 70px;
    display: flex;
  }

  .roomTopSide {
    width: @roomTopSideW;
    border: @baseBorder;
    box-sizing: border-box;
    position: relative;
    height: 100%;

    &:before {
      content: "";
      position: absolute;
      left: 15px;
      top: -15px;
      width: 100%;
      height: 50px;
      box-sizing: border-box;
      border-bottom: @baseBorder;
      transform-origin: center center;
      transform: rotateZ(30deg) scale(1.18);
    }

    .titleTime,
    .titleName {
      position: absolute;
      font-size: 14px;
      color: @fontColor;
    }

    .titleTime {
      top: 13px;
      right: 20px;
    }

    .titleName {
      bottom: 15px;
      left: 20px
    }
  }

  .roomTopTime {
    flex: 1;
    height: 100%
  }

  .roomDate {
    height: calc(100% - @barGridH);
    display: flex;
    background-color: #fff;

    .dateItem {
      flex: 1;
      font-size: 13px;
      text-align: center;
      box-sizing: border-box;
      border: @baseBorder;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      cursor: pointer;
      color: @fontColor;
    }

    .dateItem.active {
      background-color: @themeColor2;
      color: #fff;
    }
  }

  .arrowHourWrap {
    width: 100%;
    display: flex;
    height: @barGridH;

    .arrow {
      width: @barGridH;
      height: 100%;
      box-sizing: border-box;
      border: @baseBorder;
      font-size: 22px;
      text-align: center;
      position: relative;
      cursor: pointer;
    }

    .arrow.disabled {
      color: @disabledColor;
      cursor: not-allowed;
    }

    .roomTopHour {
      box-sizing: border-box;
      width: calc(@topRightW - @barGridH - @barGridH);
      overflow: hidden;

      .hourNavLst {
        white-space: nowrap;
        position: relative;
        transition: all 0.3s;

        .hourNavItem {
          display: inline-block;
          width: calc(100% / 12);
          height: @barGridH;
          line-height: @barGridH;
          text-align: center;
          box-sizing: border-box;
          border: @baseBorder;
          font-size: 13px;
          background-color: @hourColor;
          color: @fontColor;
        }
      }

      .hourNavLst.stepFour {
        left: -(@topRightW - 2*@barGridH);
      }

      .hourNavLst.stepThree {
        left: -(@topRightW - 2*@barGridH)/24*16
      }

      .hourNavLst.stepTwo {
        left: -(@topRightW - 2*@barGridH)/24*8
      }

      .hourNavLst.stepOne {
        left: 0
      }
    }
  }

  .roomItem {
    height: @gridH;
    box-sizing: border-box;
    border-right: none;
    display: flex;

    .roomName {
      width: @roomTopSideW + @barGridH;
      box-sizing: border-box;
      text-align: center;
      font-size: 14px;
      height: @gridH;
      line-height: @gridH;
      font-weight: 400;
      border: @baseBorder;
      user-select: none;
      color: @fontColor;
      text-overflow: ellipsis;
      overflow: hidden;
      white-space: nowrap;
      box-sizing: border-box;
      padding: 0 10px;
    }

    .roomGridWrap {
      box-sizing: border-box;
      overflow: hidden;
      width: @sumW - @barGridH - 145px;

      .roomGridLst {
        white-space: nowrap;
        position: relative;
        transition: all 0.3s;
        position: relative;

        .markLeftLine,
        .markRightLine {
          width: 2px;
          height: 100%;
          background-color: @markLineColor;
          position: absolute;
          z-index: 10;
        }

        .roomGrid {
          display: inline-block;
          width: @gridW;
          height: @gridH;
          box-sizing: border-box;
          border: @baseBorder;
        }

        .roomGrid.isOccupy {
          background-color: @disabledColor;
          cursor: not-allowed;
        }

        .roomGrid.isSelect {
          background-color: @themeColor;
        }
      }

      .roomGridLst.stepFour {
        left: -(@topRightW - 2*@barGridH);
      }

      .roomGridLst.stepThree {
        left: -(@topRightW - 2*@barGridH)/24*16
      }

      .roomGridLst.stepTwo {
        left: -(@topRightW - 2*@barGridH)/24*8
      }

      .roomGridLst.stepOne {
        left: 0
      }

    }
  }

  .roomMain {
    height: 10*@gridH;
    display: flex;
    width: 100%;
    overflow: hidden;
  }

  .noneBox {
    width: @barGridH;
    border: @baseBorder;
    box-sizing: border-box
  }

</style>
