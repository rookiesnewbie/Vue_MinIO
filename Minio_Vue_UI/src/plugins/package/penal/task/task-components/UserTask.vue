<template>
  <div style="margin-top: 16px">
    <el-row>
      <el-radio-group v-model="dataType" @change="changeDataType">
        <el-radio label="USERS">指定用户</el-radio>
        <el-radio label="ROLES">角色</el-radio>
        <el-radio label="DEPTS">部门</el-radio>
        <el-radio label="INITIATOR">发起人</el-radio>
      </el-radio-group>
    </el-row>
    <el-row>
      <div v-if="dataType === 'USERS'">
        <el-tag v-for="userText in selectedUser.text" :key="userText" effect="plain">
          {{userText}}
        </el-tag>
        <div class="element-drawer__button">
          <el-button size="mini" type="primary" icon="el-icon-plus" @click="onSelectUsers()">添加用户</el-button>
        </div>
      </div>
      <div v-if="dataType === 'ROLES'">
        <el-select v-model="roleIds" multiple size="mini" placeholder="请选择 角色">
          <el-option
            v-for="item in roleOptions"
            :key="item.id"
            :label="item.name"
            :value="`ROLE${item.id}`"
            :disabled="item.status === 1">
          </el-option>
        </el-select>
        <div class="element-drawer__button">
          <el-button size="mini" type="primary" icon="el-icon-plus" @click="handleSaveRoles()">保 存</el-button>
        </div>
      </div>
      <div v-if="dataType === 'DEPTS'">
        <tree-select
          :width="320"
          :height="400"
          size="mini"
          :data="deptTreeData"
          :defaultProps="deptProps"
          multiple
          clearable
          checkStrictly
          nodeKey="id"
          :checkedKeys="deptIds"
          @checked-change="checkedDeptChange">
        </tree-select>
      </div>
    </el-row>

    <!-- 候选用户弹窗 -->
    <el-dialog title="候选用户" :visible.sync="userOpen" width="60%" append-to-body>
      <el-row type="flex" :gutter="20">
        <!--部门数据-->
        <el-col :span="7">
          <el-card shadow="never" style="height: 100%">
            <div slot="header">
              <span>部门列表</span>
            </div>
            <div class="head-container">
              <el-input
                v-model="deptName"
                placeholder="请输入部门名称"
                clearable
                size="small"
                prefix-icon="el-icon-search"
                style="margin-bottom: 20px"
              />
              <el-tree
                :data="deptOptions"
                :props="deptProps"
                :expand-on-click-node="false"
                :filter-node-method="filterNode"
                ref="tree"
                default-expand-all
                @node-click="handleNodeClick"
              />
            </div>
          </el-card>
        </el-col>
        <el-col :span="17">
          <el-table ref="multipleTable" height="600" :data="userTableList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="50" align="center" />
            <el-table-column label="用户名" align="center" prop="name" />
            <el-table-column label="部门" align="center" prop="deptName" />
          </el-table>
          <pagination
            :total="userTotal"
            :page.sync="queryParams.pageNo"
            :limit.sync="queryParams.pageSize"
            @pagination="getUserList"
          />
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleTaskUserComplete">确 定</el-button>
        <el-button @click="userOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>

</template>

<script>
import empApi from '@/api/employee'
import { getEnableRoleList } from '@/api/role'
import { treeselect } from '@/api/department'
import TreeSelect from '@/components/TreeSelect'

const userTaskForm = {
  dataType: '',
  assignee: '',
  candidateUsers: '',
  candidateGroups: '',
  text: ''
  // dueDate: '',
  // followUpDate: '',
  // priority: ''
}

export default {
  name: 'UserTask',
  props: {
    id: String,
    type: String
  },
  components: { TreeSelect },
  data() {
    return {
      loading: false,
      dataType: 'USERS',
      selectedUser: {
        ids: [],
        text: []
      },
      userOpen: false,
      deptName: undefined,
      deptOptions: [],
      deptProps: {
        children: 'children',
        label: 'label'
      },
      deptTempOptions: [],
      userTableList: [],
      userTotal: 0,
      selectedUserDate: [],
      roleOptions: [],
      roleIds: [],
      deptTreeData: [],
      deptIds: [],
      // 查询参数
      queryParams: {
        pageNo: 1,
        pageSize: 10,
        deptId: undefined
      }
    }
  },
  watch: {
    id: {
      immediate: true,
      handler() {
        this.bpmnElement = window.bpmnInstances.bpmnElement
        this.$nextTick(() => this.resetTaskForm())
      }
    },
    deptName(val) {
      this.$refs.tree.filter(val)
    }
  },
  beforeDestroy() {
    this.bpmnElement = null
  },
  methods: {
    resetTaskForm() {
      // const bpmnElementObj = this.bpmnElement?.businessObject
      const bpmnElementObj = this.bpmnElement && this.bpmnElement.businessObject
      if (!bpmnElementObj) {
        return
      }
      this.dataType = bpmnElementObj['dataType']
      if (this.dataType === 'USERS') {
        const userIdData = bpmnElementObj['assignee'] || bpmnElementObj['candidateUsers']
        const userText = bpmnElementObj['text'] || []
        if (userIdData && userIdData.length > 0 && userText && userText.length > 0) {
          // this.selectedUser.ids = userIdData?.toString().split(',')
          // this.selectedUser.text = userText?.split(',')
          this.selectedUser.ids = userIdData && userIdData.toString().split(',')
          this.selectedUser.text = userText && userText.split(',')
        } else {
          this.selectedUser.ids = []
          this.selectedUser.text = []
        }
      } else if (this.dataType === 'ROLES') {
        this.getRoleOptions()
        const roleIdData = bpmnElementObj['candidateGroups'] || []
        if (roleIdData && roleIdData.length > 0) {
          this.roleIds = roleIdData.split(',')
        }
      } else if (this.dataType === 'DEPTS') {
        this.getDeptTreeData().then(() => {
          const deptIdData = bpmnElementObj['candidateGroups'] || []
          if (deptIdData && deptIdData.length > 0) {
            this.deptIds = deptIdData.split(',')
          }
        })
      }
    },
    updateElementTask() {
      const taskAttr = Object.create(null)
      for (const key in userTaskForm) {
        taskAttr[key] = userTaskForm[key]
      }
      window.bpmnInstances.modeling.updateProperties(this.bpmnElement, taskAttr)
    },
    /**
     * 查询部门下拉树结构
     */
    getDeptOptions() {
      return new Promise((resolve, reject) => {
        if (!this.deptOptions || this.deptOptions.length <= 0) {
          treeselect().then(response => {
            this.deptTempOptions = response.data
            this.deptOptions = response.data
            resolve()
          })
        } else {
          reject()
        }
      })
    },
    /**
     * 查询部门下拉树结构（含部门前缀）
     */
    getDeptTreeData() {
      function refactorTree(data) {
        return data.map(node => {
          const treeData = { id: `DEPT${node.id}`, label: node.label, parentId: node.parentId, weight: node.weight }
          if (node.children && node.children.length > 0) {
            treeData.children = refactorTree(node.children)
          }
          return treeData
        })
      }
      return new Promise((resolve, reject) => {
        if (!this.deptTreeData || this.deptTreeData.length <= 0) {
          this.getDeptOptions().then(() => {
            this.deptTreeData = refactorTree(this.deptOptions)
            resolve()
          }).catch(() => {
            reject()
          })
        } else {
          resolve()
        }
      })
    },
    /**
     * 查询角色下拉列表
     */
    getRoleOptions() {
      if (!this.roleOptions || this.roleOptions.length <= 0) {
        getEnableRoleList().then(response => {
          this.roleOptions = response.data
        })
      }
    },
    /** 查询用户列表 */
    getUserList() {
      empApi.getEmployeeList(this.queryParams).then(response => {
        this.userTableList = response.data.list
        this.userTotal = response.data.total
      })
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true
      return data.label.indexOf(value) !== -1
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.deptId = data.id
      this.getUserList()
    },
    // 关闭标签
    handleClose(tag) {
      this.selectedUserDate.splice(this.selectedUserDate.indexOf(tag), 1)
      this.$refs.multipleTable.toggleRowSelection(tag)
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.selectedUserDate = selection
    },
    onSelectUsers() {
      this.selectedUserDate = []
      // this.$refs.multipleTable?.clearSelection()
      this.$refs.multipleTable && this.$refs.multipleTable.clearSelection()
      this.getDeptOptions()
      this.userOpen = true
    },
    handleTaskUserComplete() {
      if (!this.selectedUserDate || this.selectedUserDate.length <= 0) {
        this.$modal.msgError('请选择用户')
        return
      }
      this.selectedUser.text = this.selectedUserDate.map(k => k.name) || []
      if (this.selectedUserDate.length === 1) {
        const data = this.selectedUserDate[0]
        userTaskForm.assignee = data.id
        userTaskForm.text = data.name
        userTaskForm.candidateUsers = null
      } else {
        userTaskForm.candidateUsers = this.selectedUserDate.map(k => k.id).join() || null
        userTaskForm.text = this.selectedUserDate.map(k => k.name).join() || null
        userTaskForm.assignee = null
      }
      this.updateElementTask()
      this.userOpen = false
    },
    handleSaveRoles() {
      if (!this.roleIds || this.roleIds.length <= 0) {
        this.$modal.msgError('请选择角色')
        return
      }
      userTaskForm.candidateGroups = this.roleIds.join() || null
      const textArr = this.roleOptions.filter(k => this.roleIds.indexOf(`ROLE${k.id}`) >= 0)
      // userTaskForm.text = textArr?.map(k => k.name).join() || null
      userTaskForm.text = textArr && textArr.map(k => k.name).join() || null
      this.updateElementTask()
      this.$modal.msgSuccess('成功指定角色')
    },
    checkedDeptChange(checkedIds, checkedData) {
      if (checkedIds && checkedIds.length > 0) {
        this.deptIds = checkedIds
      }
      if (checkedData && checkedData.length > 0) {
        userTaskForm.candidateGroups = checkedData.map(k => k.id).join() || null
        userTaskForm.text = checkedData.map(k => k.label).join() || null
        this.updateElementTask()
      }
    },
    changeDataType(val) {
      // 清空 userTaskForm 所有属性值
      Object.keys(userTaskForm).forEach(key => { userTaskForm[key] = null })
      userTaskForm.dataType = val
      if (val === 'USERS') {
        if (this.selectedUser && this.selectedUser.ids && this.selectedUser.ids.length > 0) {
          if (this.selectedUser.ids.length === 1) {
            userTaskForm.assignee = this.selectedUser.ids[0]
          } else {
            userTaskForm.candidateUsers = this.selectedUser.ids.join()
          }
          // userTaskForm.text = this.selectedUser.text?.join() || null
          userTaskForm.text = this.selectedUser.text && this.selectedUser.text.join() || null
        }
      } else if (val === 'ROLES') {
        this.getRoleOptions()
        if (this.roleIds && this.roleIds.length > 0) {
          userTaskForm.candidateGroups = this.roleIds.join() || null
          const textArr = this.roleOptions.filter(k => this.roleIds.indexOf(`ROLE${k.id}`) >= 0)
          // userTaskForm.text = textArr?.map(k => k.name).join() || null
          userTaskForm.text = textArr && textArr.map(k => k.name).join() || null
        }
      } else if (val === 'DEPTS') {
        this.getDeptTreeData()
        if (this.deptIds && this.deptIds.length > 0) {
          userTaskForm.candidateGroups = this.deptIds.join() || null
          const textArr = []
          const treeStarkData = JSON.parse(JSON.stringify(this.deptTreeData))
          this.deptIds.forEach(id => {
            let stark = []
            stark = stark.concat(treeStarkData)
            while (stark.length) {
              const temp = stark.shift()
              if (temp.children) {
                stark = temp.children.concat(stark)
              }
              if (id === temp.id) {
                textArr.push(temp)
              }
            }
          })
          // userTaskForm.text = textArr?.map(k => k.label).join() || null
          userTaskForm.text = textArr && textArr.map(k => k.label).join() || null
        }
      } else if (val === 'INITIATOR') {
        userTaskForm.assignee = '${initiator}'
        userTaskForm.text = '流程发起人'
      }
      this.updateElementTask()
    }
  }
}
</script>

<style scoped lang="scss">
.el-row .el-radio-group {
  margin-bottom: 15px;
  .el-radio {
    line-height: 28px;
  }
}
.el-tag {
  margin-bottom: 10px;
  + .el-tag {
    margin-left: 10px;
  }
}

</style>
