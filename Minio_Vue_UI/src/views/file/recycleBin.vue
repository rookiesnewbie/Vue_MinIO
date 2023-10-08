<template>
  <div class="app-container">
    <el-alert title="回收站中的文件仅保留七天，到期将自动删除，无法进行恢复" type="success" center show-icon style="margin-bottom: 10px;" />
    <el-row :gutter="20">
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleClearRecycleBin">清空回收站</el-button>
        </el-col>
        <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleCreateFolder"
                       >批量恢复</el-button>
          </el-col>
      </el-row>

      <el-table :data="fileList" empty-text="回收站中暂时没有文件" style="width: 100%;">
        <el-table-column type="selection" width="30"></el-table-column>
        <el-table-column align="right" width="45">
          <template slot-scope="scope">
            <span v-if="scope.row.type === '文件夹'" class="folder-icon">
              <i class="el-icon-folder"></i>
            </span>
            <span v-else class="file-icon">
              <i class="el-icon-document"></i>
            </span>
          </template>
        </el-table-column>
        <el-table-column label="名称" align="left" width="250" key="name" prop="name" :show-overflow-tooltip="true" />
        <el-table-column label="类型" align="center" key="type" prop="type" />
        <el-table-column label="大小" align="center" key="size" prop="size" />
        <el-table-column label="删除时间" align="center" key="deletedTime" prop="deletedTime">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.deletedTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button size="mini" type="text" icon="el-icon-download" @click="handleRecovery(scope.row)">恢复</el-button>
            <el-button size="mini" type="text" icon="el-icon-download"
              @click="handleThroughlyDelete(scope.row)">彻底删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total > 0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize"
        @pagination="getFileList" />

    </el-row>

  </div>
</template>

<script>

import { mapGetters } from 'vuex'
import { getRecycleFileList, deleteRecycleBinFile, recoveryFile, clearAllRecycleBin } from '@/api/recycleFile'

export default {
  name: 'RecycleBin',
  components: {},
  data() {
    return {
      // 遮罩层
      sureBtnLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      fileList: null,
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      listQuery: {
        pageNo: 1,
        pageSize: 10
      }
    }
  },
  watch: {},
  created() {
    this.getFileList()
  },
  computed: {
    ...mapGetters(['accountId'])
  },
  methods: {
    // 查询文件列表
    getFileList() {
      this.listQuery.empId = this.accountId
      getRecycleFileList(this.listQuery).then(response => {
        this.fileList = response.data.records
        this.total = response.data.total
      }).catch(res => {
        this.$message.error('加载文件列表失败')
      })
    },
    // handleQuery() {
    //   this.listQuery.pageNo = 1
    //   this.getFileList()
    // },
    // 恢复功能
    handleRecovery(row) {
      this.$confirm('是否确认恢复文件(夹)“' + row.name + '”').then(function () {
        return recoveryFile(row.id)
      }).then(() => {
        this.$message.success('文件恢复成功,您现在可在原位置找到它')
        this.getFileList()
      }).catch(error => {
        if (error === 'cancel') {
          this.$message.warning('已取消恢复')
        } else {
          this.$message.error('文件恢复失败' + error)
        }
      })
    },
    // 彻底删除功能
    handleThroughlyDelete(row) {
      this.$confirm('是否确认删除文件(夹)“' + row.name + '”?此次删除将不可恢复').then(function () {
        return deleteRecycleBinFile(row.id)
      }).then(() => {
        this.$message.success('文件删除成功')
        this.getFileList()
      }).catch(error => {
        if (error === 'cancel') {
          this.$message.warning('已取消删除')
        } else {
          this.$message.error('文件删除失败' + error)
        }
      })
    },
    // 清空回收站
    handleClearRecycleBin() {
      this.$confirm('是否确认要删除所有文件?此次删除将不可恢复').then(function () {
        return clearAllRecycleBin()
      }).then(() => {
        this.$message.success('回收站已清空')
        this.getFileList()
      }).catch(error => {
        if (error === 'cancel') {
          this.$message.warning('已取消回收站清空操作')
        } else {
          this.$message.error('回收站清空失败' + error)
        }
      })
    }
  }
}
</script>
<style scoped>
.el-icon-search {
  margin-top: 5.5px;
  margin-left: 3px;
  cursor: pointer;
  font-size: 16px;
}

.breadcrumb {
  margin-bottom: 15px;
}

.flex-start {
  display: flex;
  justify-content: flex-start;
}

.breadcrumb-icon {
  font-size: 22px;
  margin: -4px 0 -10px;
}

.breadcrumb-icon:hover {
  cursor: pointer;
  color: #409EFF;
  text-decoration: none;
}

.folder-hover-class:hover {
  cursor: pointer;
  color: #409EFF;
  text-decoration: none;
}

.el-tag+.el-tag {
  margin-left: 10px;
}

.folder-icon {
  font-size: 22px;
}

.file-icon {
  font-size: 22px;
}
</style>
