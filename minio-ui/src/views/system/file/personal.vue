<template>
  <div class="app-container">
    <el-row :gutter="20">
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleCreateFolder"
                       v-if="hasPermission('file:personal:add-folder')">新增文件夹</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-upload2" size="mini" @click="handleUploadFile"
                       v-if="hasPermission('file:personal:upload')">上传文件</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="primary" icon="el-icon-download" size="mini" @click="handleBatchDownload" :loading="exportLoading"
                       >批量下载</el-button>
          </el-col>
          <el-col :span="6">
          <!-- <div class="top-right-search"> -->
            <el-input v-model="listQuery.name" placeholder="搜索您的文件" size="mini" maxlength="255" :clearable="true" 
              @keyup.enter.native="handleQuery" v-if="hasPermission('file:personal:query')">
              <i slot="prefix" class="el-icon-search" title="点击搜索" @click="handleQuery"></i>
            </el-input>
          <!-- </div> -->
          </el-col>
          <right-toolbar @queryTable="getFileList" :hideSearch=true></right-toolbar>
        </el-row>
        <el-row>
          <el-card :body-style="{padding:'12px'}" class="breadcrumb" shadow="never">
            <div class="flex-start">
              <div class="breadcrumb-icon">
                <i class="el-icon-back" @click="returnParentFolder"/>
              </div>
              <el-breadcrumb separator="/">
                <el-breadcrumb-item v-for="(path, index) in paths" :key="path.name">
                  <span @click="jumpToFolder(index)" style="padding-left: 8px" class="folder-hover-class">
                    {{ path.name }}
                  </span>
                </el-breadcrumb-item>
              </el-breadcrumb>
            </div>
          </el-card>
        </el-row>

        <el-table :data="fileList" empty-text="暂时没有文件" style="width: 100%;">
          <!-- @row-click="enterFolder"> -->
          <el-table-column type="selection" width="30"></el-table-column>
          <el-table-column align="right" width="45">
            <template slot-scope="scope">
              <span v-if="scope.row.type === '文件夹'" class="folder-icon folder-hover-class" @click="enterFolder(scope.row)">
                <!-- <i class="el-icon-folder"></i> -->
                <icon-svg icon-class='文件夹' />
              </span>
              <span v-else class="file-icon">
                <!-- <i class="el-icon-document"></i> -->
                <icon-svg :icon-class="scope.row.type" />
              </span>
            </template>
          </el-table-column>
          <el-table-column label="名称" align="left" width="250" key="name" prop="name" :show-overflow-tooltip="true">
            <template slot-scope="scope" style="padding-left: 0">
              <span v-if="scope.row.type === '文件夹'" class="folder-hover-class" @click="enterFolder(scope.row)">
                {{ scope.row.name }}
              </span>
              <span v-else class="folder-hover-class" @click="previewFile(scope.row)">
                {{ scope.row.name }}
              </span>
            </template>
          </el-table-column>
          <!-- <el-table-column label="名称" align="left" width="250" key="name" prop="name" :show-overflow-tooltip="true" /> -->
          <el-table-column label="类型" align="center" key="type" prop="type" />
          <el-table-column label="大小" align="center" key="size" prop="size" />
          <el-table-column label="创建者" align="center" key="creator" prop="creator" />
          <el-table-column label="创建时间" align="center" key="createTime" prop="createTime">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.createTime) }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" width="160" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-download" @click="handleDownload(scope.row)" 
                         v-if="scope.row.type !== '文件夹' && hasPermission('file:personal:download')">下载</el-button>
              <el-dropdown  @command="(command) => handleCommand(command, scope.$index, scope.row)"
                            >
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="handleRename" v-if="hasPermission('file:personal:rename')"
                    size="mini" type="text" icon="el-icon-edit-outline">重命
                    名</el-dropdown-item>
                  <el-dropdown-item command="handleDelete" v-if="hasPermission('file:personal:delete')"
                    size="mini" type="text" icon="el-icon-delete">删除</el-dropdown-item>
                  <el-dropdown-item command="handleShare" v-if="scope.row.type !== '文件夹' && hasPermission('file:personal:share')"
                    size="mini" type="text" icon="el-icon-share">文件共享</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total>0" :total="total" :page.sync="listQuery.pageNo" :limit.sync="listQuery.pageSize"
                    @pagination="getFileList"/>
    </el-row>

    <!-- 新建文件夹 或者重命名 对话框 -->
    <el-dialog :title="form.title" :visible.sync="open" :close-on-click-modal="false" width="550px" @close="handleDialogClose">
      <el-form class="add-folder-form" :model="form" :rules="formRules" ref="addOrRenameFileForm" label-width="100px" label-position="top">
        <el-form-item v-if="form.isShare === false" :label="form.label" prop="filename">
          <el-input v-model="form.filename" :placeholder="form.placeholder" type="textarea" autosize maxlength="255" show-word-limit></el-input>
        </el-form-item>
        <el-form-item v-else label="文件共享至" prop="mountFolderId">
          <treeselect v-model="form.mountFolderId" :options="shareFolderOptions" :show-count="true"
                      placeholder="请选择共享位置" :normalizer="normalizer" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="handleDialogClose">取 消</el-button>
        <el-button type="primary" :loading="sureBtnLoading" @click="handleFolderDialogSure"
          v-if="form.title === '新建文件夹'">确 定</el-button>
        <el-button type="primary" :loading="sureBtnLoading" @click="handleShareDialogSure"
          v-else-if="form.isShare === true">确定</el-button>
        <el-button type="primary" :loading="sureBtnLoading" @click="handleRenameDialogSure"
          v-else>确 定</el-button>
      </div>
    </el-dialog>

    <!-- 上传文件对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" :close-on-click-modal="false" width="400px" append-to-body>
      <el-upload ref="upload" multiple :limit="3" :http-request="uploadFile" action="" :disabled="upload.isUploading"
        :on-remove="handleUploadRemove" :on-exceed="handleUploadExceed" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <span>上传的文件的大小不能超过100MB。</span>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 预览文件弹出框 -->
    <el-dialog :title="showFile.name" :visible.sync="dialogVisible" width="80%" height="100%">
      <embed v-if="showFile.type === 'pdf'" :src="showFile.path" :type="showFile.contentType" width="100%" height="600">
      <embed v-if="showFile.type === 'mp3'" :src="showFile.path" :type="showFile.contentType" width="100%" height="500">
      <embed v-if="showFile.type === 'mp4'" :src="showFile.path" :type="showFile.contentType" width="100%" height="500">
      <embed v-if="showFile.type === 'jpg'" :src="showFile.path" :type="showFile.contentType" width="60%" height="500">
      <embed v-if="showFile.type === 'png'" :src="showFile.path" :type="showFile.contentType" width="60%" height="500">
      <embed v-if="showFile.type === 'jpeg'" :src="showFile.path" :type="showFile.contentType" width="60%" height="500">
      <embed v-if="showFile.type === 'txt'" :src="showFile.path" :type="showFile.contentType" width="100%" height="500">
      <embed v-if="showFile.type === 'xml'" :src="showFile.path" :type="showFile.contentType" width="100%" height="500" >
      <doc-preview v-else-if="showFile.type==='doc'" :url="showFile.path"  type="office" width="100%" height="500"/>
      <doc-preview v-else-if="showFile.type==='docx'" :url="showFile.path"  type="office" width="100%" height="500"/>
      <doc-preview v-else-if="showFile.type==='xlsx'" :url="showFile.path"  type="office" width="100%" height="500"/>
      <doc-preview v-else-if="showFile.type==='xls'" :url="showFile.path"  type="office" width="100%" height="500"/>
      <doc-preview v-else-if="showFile.type==='pptx'" :url="showFile.path"  type="office" width="100%" height="500"/>
      <doc-preview v-else-if="showFile.type==='ppt'" :url="showFile.path"  type="office" width="100%" height="500"/>
    </el-dialog>
  
  
  
  </div>
</template>

<script>

import { mapGetters } from 'vuex'
import { getFileList, createFolder, renameFile, downloadFile, uploadFiles, removeFileToRecycleBin, listAllShareFolder, shareFileTo, previewFile } from '@/api/file'
import { isValidatefilename } from '@/utils/validate'

import Treeselect from '@riophae/vue-treeselect'
import '@riophae/vue-treeselect/dist/vue-treeselect.css'
import Embed from 'vue-embed'  //预览文件
import DocPreview from 'vue-doc-preview'  //预览word等office文件

export default {
  name: 'PersonalFile',
  components: { Treeselect ,Embed, DocPreview},
  data() {
    const validatefilename = (rule, value, callback) => {
      if (isValidatefilename(value)) {
        callback(new Error(`文件夹名称不能包含下列任何字符：\\/:*?"<>|`))
      } else {
        callback()
      }
    }
    return {
      // 遮罩层
      exportLoading: false,
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
      // 日期范围
      dateRange: [],
      // 表单参数
      form: {},
      // 共享文件夹下拉树选项
      shareFolderOptions: undefined,
      // 路径信息
      paths: [{ id: 0, name: '根目录' }],
      // 上传文件参数
      upload: {
        // 是否显示弹出层（上传文件）
        open: false,
        // 弹出层标题（上传文件）
        title: '',
        // 是否禁用上传
        isUploading: false,
        // upload多文件数组
        multipartFiles: [],
        // 文件上传数据
        uploadData: ''
      },
      // 查询参数
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        name: undefined,
        parentId: 0
      },
      // 是否显示弹出层（角色权限）
      openRole: false,
      // 表单校验
      formRules: {
        filename: [
          { required: true, message: '文件夹名称不能为空', trigger: 'blur' },
          { validator: validatefilename, trigger: ['blur', 'change'] }
        ],
        mountFolderId: [
          { required: true, message: '文件挂载位置不能为空', trigger: 'blur' }
        ]
      },
      showFile: {},
      dialogVisible: false
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
    getFileList(parentId) {
      if (parentId !== undefined && typeof parentId === 'number') {
        this.listQuery.parentId = parentId
      }
      this.listQuery.empId = this.listQuery.empId === undefined ? this.accountId : this.listQuery.empId
      getFileList(this.listQuery).then(response => {
        this.fileList = response.data.list
        this.total = response.data.total
      }).catch(res => {
        this.$modal.msgError('加载文件列表失败')
      })
    },
    // 返回上级文件夹
    returnParentFolder() {
      if (this.paths.length > 1) {
        this.paths.pop()
        this.getFileList(this.paths[this.paths.length - 1].id)
      }
    },
    // 面包屑点击路径跳转
    jumpToFolder(index) {
      if (index + 1 < this.paths.length) {
        this.getFileList(this.paths[index].id)
        this.paths.splice(index + 1)
      }
    },
    // 点击文件夹
    enterFolder(row) {
      if (row.type === '文件夹') {
        this.getFileList(row.id)
        this.paths.push({ id: row.id, name: row.name })
      }
    },
    // 更多操作
    handleCommand(command, index, row) {
      switch (command) {
        case 'handleRename':
          this.handleRename(row)
          break
        case 'handleDelete':
          this.handleDelete(row)
          break
        case 'handleShare':
          this.handleShare(row)
          break
        default:
          break
      }
    },
    // 取消按钮
    handleDialogClose() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        isShare: false,
        mountFolderId: undefined,
        filename: undefined,
        title: '新建文件夹',
        label: '文件夹名称',
        placeholder: '请输入文件夹名称',
        fileId: undefined
      }
      this.resetForm('addOrRenameFileForm')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.listQuery.pageNo = 1
      this.getFileList()
    },
    // 创建文件夹按钮操作
    handleCreateFolder() {
      this.reset()
      // 打开对话框并初始化
      this.open = true
    },
    // 重命名文件按钮操作
    handleRename(row) {
      this.reset()
      if (row.type === '文件夹') {
        this.form.title = '重命名文件夹'
      } else {
        this.form.title = '重命名文件'
        this.form.label = '文件名称'
        this.form.placeholder = '请输入文件名称'
      }
      this.form.fileId = row.id
      this.form.filename = row.name
      this.open = true
    },
    // 格式化共享文件夹下拉框
    normalizer(node) {
      return {
        id: node.id,
        label: node.name,
        children: node.children
      }
    },
    // 获取共享文件夹下拉树
    getTreeselect() {
      listAllShareFolder().then(response => {
        this.shareFolderOptions = []
        this.shareFolderOptions.push({
          id: 0,
          name: '共享根目录',
          children: this.handleTree(response.data, 'id')
        })
      })
    },
    // 文件共享按钮操作
    handleShare(row) {
      this.reset()
      this.getTreeselect()
      this.form.isShare = true
      this.form.title = '文件共享'
      this.form.fileId = row.id
      this.form.filename = row.name
      this.open = true
    },
    // 文件共享 对话框 —— 确认按钮
    handleShareDialogSure() {
      this.sureBtnLoading = true
      this.$refs['addOrRenameFileForm'].validate((valid) => {
        if (valid) {
          shareFileTo({
            fileId: this.form.fileId,
            mountFolderId: this.form.mountFolderId
          }).then(res => {
            this.sureBtnLoading = false
            this.$modal.msgSuccess('文件共享操作成功')
            this.$refs['addOrRenameFileForm'].resetFields()
            this.open = false
            this.handleQuery()
          }).catch(res => {
            this.sureBtnLoading = false
            this.$modal.msgError('文件共享操作失败')
          })
        } else {
          this.sureBtnLoading = false
          return false
        }
      })
    },
    // 创建文件夹 对话框 —— 确认按钮
    handleFolderDialogSure() {
      this.sureBtnLoading = true
      this.$refs['addOrRenameFileForm'].validate((valid) => {
        if (valid) {
          createFolder({
            filename: this.form.filename,
            parentId: this.paths[this.paths.length - 1].id,
            empId: this.accountId
          }).then(res => {
            this.sureBtnLoading = false
            this.$modal.msgSuccess('文件夹创建成功')
            this.$refs['addOrRenameFileForm'].resetFields()
            this.open = false
            this.handleQuery()
          }).catch(res => {
            this.sureBtnLoading = false
            this.$modal.msgError('文件夹创建失败')
          })
        } else {
          this.sureBtnLoading = false
          return false
        }
      })
    },
    // 重命名对话框 —— 确认按钮
    handleRenameDialogSure() {
      this.sureBtnLoading = true
      this.$refs['addOrRenameFileForm'].validate((valid) => {
        if (valid) {
          renameFile({
            filename: this.form.filename,
            id: this.form.fileId
          }).then(res => {
            this.sureBtnLoading = false
            this.$modal.msgSuccess(this.form.title + '成功')
            this.$refs['addOrRenameFileForm'].resetFields()
            this.open = false
            this.handleQuery()
          }).catch(res => {
            this.sureBtnLoading = false
            this.$modal.msgError(this.form.title + '失败')
          })
        } else {
          this.sureBtnLoading = false
          return false
        }
      })
    },
    // 下载文件
    handleDownload(row) {
      downloadFile(row.id).then(result => {
        debugger
        // 提取Headers中的文件名
        const disposition = result.headers['content-disposition']
        let fileName = disposition.substring(disposition.indexOf('filename=') + 9, disposition.length)
        // 编码过的 URI 进行解码
        fileName = decodeURI(fileName)
        // 对于<a>标签，只有 Firefox 和 Chrome（内核） 支持 download 属性
        // IE10以上支持blob，但是依然不支持download
        if (result.data) {
          // 创建a标签
          const link = document.createElement('a')
          const blob = new Blob([result.data], { type: 'application/octet-stream' })
          link.href = window.URL.createObjectURL(blob)
          document.body.appendChild(link)
          // 设置默认文件名
          link.setAttribute('download', fileName)
          // 执行下载
          link.click()
          // 释放url
          URL.revokeObjectURL(link.href)
          // 释放标签
          document.body.removeChild(link)
        }
      }).catch((error) => {
        console.log('文件下载失败 >>> error = ', error)
      })
    },
    // 上传文件按钮操作
    handleUploadFile() {
      this.upload.title = '上传文件'
      this.upload.open = true
    },
    // el-upload上传事件
    uploadFile(params) {
      this.uploadData.append('multipartFiles', params.file)
    },
    // el-upload移除文件事件
    handleUploadRemove(file, fileList) {
      this.multipartFiles = fileList
    },
    // 选取文件超过数量提示
    handleUploadExceed(files, fileList) {
      this.$modal.msgWarning('当前限制选择3个文件')
    },
    // 上传文件对话框确定按钮
    submitFileForm() {
      this.uploadData = new FormData()
      // 提交调用uploadFile函数
      this.$refs['upload'].submit()
      this.uploadData.append('empId', this.accountId)
      this.uploadData.append('parentId', this.paths[this.paths.length - 1].id)
      uploadFiles(this.uploadData).then(result => {
        this.$modal.msgSuccess('上传成功')
        this.upload.multipartFiles = []
        this.upload.open = false
        this.handleQuery()
        this.$refs['upload'].clearFiles()
      }).catch(error => {
        this.$modal.msgError('上传失败', error)
        this.upload.multipartFiles = []
      })
    },
    // 批量下载按钮
    handleBatchDownload() {
      //
    },
    // 删除文件 / 文件夹
    handleDelete(row) {
      this.$modal.confirm('是否确认删除文件(夹)“' + row.name + '"?').then(function() {
        return removeFileToRecycleBin(row.id)
      }).then(() => {
        this.$modal.msgSuccess('文件删除成功，七天内可在回收站进行恢复')
        this.handleQuery()
      }).catch(error => {
        if (error === 'cancel') {
          this.$modal.msgWarning('已取消删除')
        } else {
          this.$modal.msgError('文件删除失败' + error)
        }
      })
    },

    //预览文件
    previewFile(row){
      // debugger
      previewFile(row.id).then(result =>{
        this.showFile = result.data
        this.dialogVisible = true
        console.log("该文件的路径为：",this.showFile.path)
        console.log("该文件的路径为：",this.showFile)
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
.el-tag + .el-tag {
  margin-left: 10px;
}
.folder-icon {
    font-size: 22px;
}

.file-icon {
    font-size: 22px;
}
</style>
