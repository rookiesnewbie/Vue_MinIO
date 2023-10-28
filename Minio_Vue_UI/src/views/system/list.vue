<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!--部门数据-->
      <!-- <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="deptName" placeholder="请输入部门名称" clearable size="small" prefix-icon="el-icon-search" style="margin-bottom: 20px"/>
        </div>
        <div class="head-container">
          <el-tree :data="deptOptions" :props="defaultProps" :expand-on-click-node="false" :filter-node-method="filterNode"
                   ref="tree" default-expand-all @node-click="handleNodeClick"/>
        </div>
      </el-col> -->
      <!--用户数据-->
      <el-col :span="20" :xs="24">
        <el-form
          :model="listQuery"
          ref="queryForm"
          size="small"
          :inline="true"
          v-show="showSearch"
          label-width="68px"
        >
          <el-form-item label="用户姓名" prop="name">
            <el-input
              v-model="listQuery.name"
              placeholder="请输入用户姓名"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="手机号码" prop="phone">
            <el-input
              v-model="listQuery.phone"
              placeholder="请输入手机号码"
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select
              v-model="listQuery.status"
              placeholder="用户状态"
              clearable
              style="width: 240px"
            >
              <el-option
                v-for="item in CommonStatusDict"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery"
              >搜索</el-button
            >
            <el-button icon="el-icon-refresh" @click="resetQuery"
              >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
              >新增</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              icon="el-icon-upload2"
              size="mini"
              @click="handleImport"
              >导入</el-button
            >
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              icon="el-icon-download"
              size="mini"
              @click="handleExport"
              :loading="exportLoading"
              
              >导出</el-button
            >
          </el-col>
          <!-- <right-toolbar
            :showSearch.sync="showSearch"
            @queryTable="getEmployeeList"
            :columns="columns"
          ></right-toolbar> -->
        </el-row>

        <el-table v-loading="listLoading" :data="employeeList">
          <el-table-column
            label="用户id"
            align="center"
            key="id"
            prop="id"
            v-if="columns[0].visible"
          />
          <el-table-column
            label="姓名"
            align="center"
            key="name"
            prop="name"
            v-if="columns[1].visible"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="性别"
            align="center"
            key="sex"
            prop="sex"
            v-if="columns[2].visible"
          >
            <template slot-scope="scope">
              <el-tag
                :disable-transitions="true"
                type="success"
                v-if="scope.row.sex == 1"
                >女</el-tag
              >
              <el-tag :disable-transitions="true" v-if="scope.row.sex == 2"
                >男</el-tag
              >
            </template>
          </el-table-column>
          <el-table-column
            label="创建日期"
            align="center"
            prop="entryDate"
            v-if="columns[3].visible"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.entryDate, "{y}-{m}-{d}") }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="手机号码"
            align="center"
            key="phone"
            prop="phone"
            v-if="columns[4].visible"
            width="120"
          />
          <el-table-column
            label="邮箱"
            align="center"
            key="email"
            prop="email"
            v-if="columns[5].visible"
            width="120"
            :show-overflow-tooltip="true"
          />
          <el-table-column
            label="出生日期"
            align="center"
            prop="birthday"
            v-if="columns[6].visible"
            width="160"
          >
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.birthday, "{y}-{m}-{d}") }}</span>
            </template>
          </el-table-column>
          <el-table-column
            label="状态"
            key="status"
            v-if="columns[7].visible"
            align="center"
          >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="0"
                :inactive-value="1"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                >修改</el-button
              >
              <el-dropdown
                @command="
                  (command) => handleCommand(command, scope.$index, scope.row)
                "
              
              >
                <span class="el-dropdown-link">
                  <i class="el-icon-d-arrow-right el-icon--right"></i>更多
                </span>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item
                    command="handleDelete"
                    v-if="
                      scope.row.id !== 1 
                    "
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    >删除</el-dropdown-item
                  >
                  <el-dropdown-item
                    command="handleResetPwd"
                    size="mini"
                    type="text"
                    icon="el-icon-key"
                    >重置密码</el-dropdown-item

                  >
                </el-dropdown-menu>
              </el-dropdown>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total > 0"
          :total="total"
          :page.sync="listQuery.pageNo"
          :limit.sync="listQuery.pageSize"
          @pagination="getEmployeeList"
        />
      </el-col>
    </el-row>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="637px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="用户姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入用户姓名" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="用户性别" prop="sex">
              <el-select v-model="form.sex" placeholder="请选择">
                <el-option
                  v-for="item in SexDict"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="手机号码" prop="phone">
              <el-input
                v-model="form.phone"
                placeholder="请输入手机号码"
                maxlength="11"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input
                v-model="form.email"
                placeholder="请输入邮箱"
                maxlength="50"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="出生日期" prop="birthday">
              <el-date-picker
                type="date"
                v-model="form.birthday"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="创建日期" prop="entryDate">
              <el-date-picker
                type="date"
                v-model="form.entryDate"
                placeholder="选择日期"
                value-format="yyyy-MM-dd"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item
              v-if="form.id === undefined"
              label="账户昵称"
              prop="nickname"
            >
              <el-input v-model="form.nickname" placeholder="请输入账户昵称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="form.id === undefined"
              label="账户密码"
              prop="password"
            >
              <el-input
                v-model="form.password"
                placeholder="请输入账户密码"
                type="password"
                show-password
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item label="备注">
              <el-input
                v-model="form.remark"
                type="textarea"
                placeholder="请输入内容"
              ></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 用户导入对话框 -->
    <el-dialog
      :title="upload.title"
      :visible.sync="upload.open"
      width="400px"
      append-to-body
    >
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />
            是否更新已经存在的用户数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link
            type="primary"
            :underline="false"
            style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate"
            >下载模板</el-link
          >
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { COMMON_STATUS_DICT, SEX_TYPE_DICT } from "@/utils/dict";
import { CommonStatusEnum } from "@/utils/constants";
import empApi from "@/api/employee";
import { update } from "@/api/user";

import { getBaseHeader } from "@/utils/request";

import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Employee",
  components: { Treeselect },
  data() {
    return {
      // 遮罩层
      listLoading: true,
      // 导出遮罩层
      exportLoading: false,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 用户表格数据
      employeeList: null,
      // 弹出层标题
      title: "",
      // 部门树选项
      deptOptions: undefined,
      // 是否显示弹出层
      open: false,
      // 部门名称
      deptName: undefined,
      // 默认密码
      initPassword: undefined,
      // 日期范围
      dateRange: [],
      // 状态数据字典
      statusOptions: [],
      // 性别状态字典
      sexOptions: [],
      // 岗位选项
      postOptions: [],
      // 角色选项
      roleOptions: [],
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "name",
      },
      // 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: getBaseHeader(),
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/employee/import",
      },
      // 查询参数
      listQuery: {
        pageNo: 1,
        pageSize: 10,
        name: undefined,
        phone: undefined,
        status: undefined,
        deptId: undefined,
      },
      // 列信息
      columns: [
        { key: 0, label: `用户id`, visible: true },
        { key: 1, label: `姓名`, visible: true },
        { key: 2, label: `性别`, visible: true },
        { key: 3, label: `创建日期`, visible: true },
        { key: 4, label: `手机号码`, visible: true },
        { key: 5, label: `邮箱`, visible: true },
        { key: 6, label: `出生日期`, visible: true },
        { key: 7, label: `状态`, visible: true },
      ],
      // 表单校验
      rules: {
        name: [
          { required: true, message: "用户姓名不能为空", trigger: "blur" },
        ],
        nickname: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "用户密码不能为空", trigger: "blur" },
        ],
        email: [
          {
            type: "email",
            message: "'请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        phone: [
          {
            pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/,
            message: "请输入正确的手机号码",
            trigger: "blur",
          },
        ],
      },
      // 是否显示弹出层（角色权限）
      openRole: false,

      // 枚举
      CommonStatusEnum: CommonStatusEnum,
      // 字典
      CommonStatusDict: COMMON_STATUS_DICT,
      SexDict: SEX_TYPE_DICT,
    };
  },
  watch: {
    // 根据名称筛选部门树
    deptName(val) {
      this.$refs.tree.filter(val);
    },
  },
  created() {
    this.getEmployeeList();
    // this.getTreeselect();
  },
  methods: {
    // 更多操作
    handleCommand(command, index, row) {
      switch (command) {
        case "handleUpdate":
          this.handleUpdate(row); // 修改用户信息
          break;
        case "handleDelete":
          this.handleDelete(row); // 红号变更
          break;
        case "handleResetPwd":
          this.handleResetPwd(row);
          break;
        case "handleRole":
          this.handleRole(row);
          break;
        default:
          break;
      }
    },
    /** 查询用户列表 */
    getEmployeeList() {
      this.listLoading = true;
      empApi
        .getEmployeeList(
          this.addDateRange(this.listQuery, [
            this.dateRange[0] ? this.dateRange[0] : undefined,
            this.dateRange[1] ? this.dateRange[1] : undefined,
          ])
        )
        .then((response) => {
          this.employeeList = response.data.list;
          this.total = response.data.total;
          this.listLoading = false;
        })
        .catch((res) => {
          this.$message.error("加载用户列表失败");
        });
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.listQuery.deptId = data.id;
      this.getEmployeeList();
    },
    // 用户状态修改
    handleStatusChange(row) {
      const text = row.status === CommonStatusEnum.ENABLE ? "启用" : "停用";
      this.$confirm('确认要"' + text + '""' + row.name + '"用户吗?')
        .then(function () {
          return empApi.updateEmpStatus(row.id, row.status);
        })
        .then(() => {
          this.$message.success(text + "成功");
        })
        .catch(function () {
          row.status =
            row.status === CommonStatusEnum.ENABLE
              ? CommonStatusEnum.DISABLE
              : CommonStatusEnum.ENABLE;
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 取消按钮（角色权限）
    cancelRole() {
      this.openRole = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        deptId: undefined,
        name: undefined,
        nickname: undefined,
        password: undefined,
        phone: undefined,
        email: undefined,
        sex: undefined,
        birthday: undefined,
        entryDate: undefined,
        status: "0",
        remark: undefined,
        positionIds: [],
        roleIds: [],
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.listQuery.pageNo = 1;
      this.getEmployeeList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      // 获得下拉数据
      // this.getTreeselect();
      // 打开表单，并设置初始化
      this.open = true;
      this.title = "添加用户";
      this.form.password = this.initPassword;
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      // this.getTreeselect();
      const id = row.id;
      empApi.getEmpDetail(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改用户信息";
        this.form.birthday = this.parseTime(this.form.birthday, "{y}-{m}-{d}");
        this.form.entryDate = this.parseTime(
          this.form.entryDate,
          "{y}-{m}-{d}"
        );
        this.form.password = "";
        this.form.nickname = "";
        this.form.deptId = this.form.deptId === 0 ? null : this.form.deptId;
      });
    },
    /** 重置密码按钮操作 */
    handleResetPwd(row) {
      this.$prompt('请输入"' + row.name + '"的新密码', "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
      })
        .then(({ value }) => {
          update({ id: row.id, password: value }).then((response) => {
            this.$message.success("修改成功，新密码是：" + value);
          });
        })
        .catch(() => {});
    },
    /** 分配用户角色操作 */
    handleRole(row) {
      this.reset();
      const id = row.id;
      // 处理了 form 的用户 name 和 nickname 的展示
      this.form.id = id;
      this.form.name = row.name;
      this.form.nickname = row.nickname;
      // 打开弹窗
      this.openRole = true;
      // 获得角色列表
      getEnableRoleList().then((response) => {
        // 处理 roleOptions 参数
        this.roleOptions = [];
        this.roleOptions.push(...response.data);
      });
      // 获得角色拥有的菜单集合
      getRoleIdsByAccountId(id).then((response) => {
        // 设置选中
        this.form.roleIds = response.data;
      });
    },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id !== undefined) {
            empApi.updateEmployee(this.form).then((response) => {
              this.$message.success("修改成功");
              this.open = false;
              this.getEmployeeList();
            });
          } else {
            empApi.addEmployee(this.form).then((response) => {
              this.$message.success("新增成功");
              this.open = false;
              this.getEmployeeList();
            });
          }
        }
      });
    },
    /** 提交按钮（角色权限） */
    submitRole: function () {
      if (this.form.id !== undefined) {
        assignAccountRole({
          accountId: this.form.id,
          roleIds: this.form.roleIds,
        }).then((response) => {
          this.$message.success("分配角色成功");
          this.openRole = false;
          this.getEmployeeList();
        });
      }
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$confirm('是否确认删除用户编号为"' + ids + '"的数据项?')
        .then(function () {
          return empApi.deleteEmployeeById(ids);
        })
        .then(() => {
          this.getEmployeeList();
          this.$message.success("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      const listQuery = this.addDateRange(this.listQuery, [
        // this.dateRange[0] ? this.dateRange[0] + ' 00:00:00' : undefined,
        // this.dateRange[1] ? this.dateRange[1] + ' 23:59:59' : undefined
        this.dateRange[0] ? this.dateRange[0] : undefined,
        this.dateRange[1] ? this.dateRange[1] : undefined,
      ]);
      this.$confirm("是否确认导出所有用户数据项?")
        .then(() => {
          this.exportLoading = true;
          return empApi.exportUser(listQuery);
        })
        .then((response) => {
          this.$download.excel(response.data, "用户数据.xls");
          this.exportLoading = false;
        })
        .catch(() => {});
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "用户导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      empApi.importTemplate().then((response) => {
        this.$download.excel(response.data, "用户导入模板.xls");
      });
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      if (response.code !== 200) {
        this.$modal.msgError(response.message);
        return;
      }
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      // 拼接提示语
      const data = response.data;
      let text = "创建成功数量：" + data.createNicknames.length;
      for (const nickname of data.createNicknames) {
        text += "<br />&nbsp;&nbsp;&nbsp;&nbsp;" + nickname;
      }
      text += "<br />更新成功数量：" + data.updateNicknames.length;
      for (const nickname of data.updateNicknames) {
        text += "<br />&nbsp;&nbsp;&nbsp;&nbsp;" + nickname;
      }
      text +=
        "<br />更新失败数量：" + Object.keys(data.failureNicknames).length;
      for (const nickname in data.failureNicknames) {
        text +=
          "<br />&nbsp;&nbsp;&nbsp;&nbsp;" +
          nickname +
          "：" +
          data.failureNicknames[nickname];
      }
      this.$alert(text, "导入结果", { dangerouslyUseHTMLString: true });
      this.getEmployeeList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 格式化部门的下拉框
    normalizer(node) {
      return {
        id: node.id,
        label: node.name,
        children: node.children,
      };
    },
  },
};
</script>
<style scoped>
.el-tag + .el-tag {
  margin-left: 10px;
}
</style>
