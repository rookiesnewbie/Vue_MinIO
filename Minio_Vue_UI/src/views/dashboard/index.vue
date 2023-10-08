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
              <p class="firstChild">欢迎您，<span class="col">{{ name }}</span>，祝你开心每一天！</p>
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
    </div>
    
    <div style="margin-top:10px;">

      <el-card style="color: #409EFF">
          <h1><i class="el-icon-user-solid" /> 用户总数</h1>
          <h1 style="padding: 10px 0; text-align: center; font-weight: bold;margin-bottom: 10px;">
            {{total}}
          </h1>
      </el-card>

      <el-card style="margin-top:10px">
        <div id="file" class="inline-div"></div>
        <div id="account" class="inline-div"></div>
      </el-card>
      
    </div>
    
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import FlipClock from '@/components/FlipClock/FlipClock'
import * as echarts from 'echarts';

import {accountTotal} from '@/api/user'
import {filetTotal} from '@/api/file'
export default {
  name: 'Dashboard',
  components: {
    FlipClock
  },
  data(){
    return{
      total: 0,
    }
  },
  computed: {
    ...mapGetters([
      'name'
    ])
  },
  created() {
    this.$store.dispatch('user/getInfo')
    
  },

  mounted(){
    this.fileDetails()
    this.accountTotal()
  },

  methods:{
    fileDetails(){
      filetTotal().then(res =>{
        if(res.code === 200){
            var chartDom = document.getElementById('file');
            var myChart = echarts.init(chartDom);
            var option;

            option = {
              title: {
                text: '文件统计图',
                subtext: 'File Data',
                left: 'center'
              },
              tooltip: {
                trigger: 'item'
              },
              legend: {
                orient: 'vertical',
                left: 'left'
              },
              series: [
                {
                  name: '总数',
                  type: 'pie',
                  radius: '50%',
                  data: [
                    { value: res.data.folder, name: '文件夹' },
                    { value: res.data.photo, name: '图片' },
                    { value: res.data.video, name: '视频' },
                    { value: res.data.mp3, name: '音频' },
                    { value: res.data.word, name: '文档' }
                  ],
                  emphasis: {
                    itemStyle: {
                      shadowBlur: 10,
                      shadowOffsetX: 0,
                      shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                  }
                }
              ]
            };

            option && myChart.setOption(option);
        }
      })
      
  
    },

    accountTotal(){
      accountTotal().then(reslut =>{
        if(reslut.code === 200){
          this.total = reslut.data
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>

  .inline-div {
    width: 500px;
    height: 400px;
    margin-left: 20px;
    display: inline-block;
  }

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
