<template>
  <div class="conditionBox">
    <li class="conditionItem">
      <h3 class="conditionTitle">会议室编号：</h3>
      <el-input placeholder="请输入会议室编号" v-model="roomCode" @change="changeRoom()"
        size="mini" clearable style="width: 250px; padding-top: 3.5px;">
        <i slot="prefix" class="el-icon-search" title="点击搜索" @click="changeRoom()" />
      </el-input>
    </li>
    <li class="conditionItem">
      <h3 class="conditionTitle">会议设施：</h3>
      <ul class="facility">
        <el-checkbox-group v-model="equipmentNames" @change="changeRoom()">
          <el-checkbox v-for="(item, index) in facilityList" :key="index" :label="item"/>
        </el-checkbox-group>
      </ul>
    </li>
  </div>
</template>

<script>
  import { listAllTypes } from '@/api/conferenceEquipment'

  export default {
    name: 'vRoomConFilter',
    data() {
      return {
        // 选择设备列表
        equipmentNames: [],
        facilityList: [], // 设施列表
        roomCode: this.insertRoomCode || undefined
      }
    },
    components: {

    },
    props: {
      insertRoomCode: ''
    },
    methods: {
      changeRoom() {
        this.$emit('change')
      }
    },
    mounted() {
      listAllTypes().then(response => {
        this.facilityList = response.data
      })
    },
    created() {}
  }

</script>

<style lang="less">

</style>
<style lang="less" scoped>
  @import './index.less';
  @import './reset.css';

  // 筛选条件样式
  .conditionBox {
    margin-bottom: 5px;
 
    .conditionItem {
      display: flex;

      .conditionTitle {
        font-size: 14px;
        padding: 10px 0;
        width: 90px;
        color: @fontColor;
      }

      .facility {
        padding-top: 10px;
      }

      .el-icon-search {
        padding: 9.5px 4px;
        cursor: pointer;
        font-size: 16px;
      }
    }
  }

</style>
