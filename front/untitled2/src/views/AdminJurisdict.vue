<template>
  <div>
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="网站开放时间" name="first">
        <el-table class="opentime-table" :data="timeData" id="timetable">
          <el-table-column prop="begin" label="当前开始时间" align="center" />
          <el-table-column prop="end" label="当前结束时间" align="center" />
          <el-table-column prop="operation" label="操作" align="center">
            <template>
              <el-button type="danger" circle @click="deleteTime">
                <i class="el-icon-close"></i>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <div>
          <el-form
            :model="param"
            :rules="rules2"
            ref="time"
            label-width="80px"
            style="width: 300px; margin: 0 auto; text-align: center"
          >
            <el-form-item
              label="开始时间"
              prop="begin"
              style="margin-top: 30px"
            >
              <el-date-picker
                v-model="param.begin"
                type="datetime"
                placeholder="“选择开始时间”"
              >
              </el-date-picker>
            </el-form-item>

            <el-form-item label="结束时间" prop="end">
              <el-date-picker
                v-model="param.end"
                type="datetime"
                placeholder="选择结束时间"
              >
              </el-date-picker>
            </el-form-item>
            <div style="text-align: center">
              <el-button type="primary" @click="setTime">确认</el-button>
              <el-button type="primary">取消</el-button>
            </div>
          </el-form>
        </div>
      </el-tab-pane>

      <el-tab-pane label="当前管理员" name="second">
        <el-button type="primary" style="margin-top: 10px" @click="addAdmin"
          >添加管理员</el-button
        >
        <el-table
          class="admin-table"
          :data="tableData"
          style="width: 70%; margin-top: 0 auto"
          id="admintable"
        >
          <el-table-column prop="account" label="管理员名" align="center" />
          <el-table-column prop="permission" label="权限" align="center" />
          <el-table-column prop="note" label="备注" align="center" />
          <el-table-column prop="operation" label="操作" align="center">
            <template #default="scope">
              <!--
      <el-button type="primary" circle >
        <i class="el-icon-edit"></i>
      </el-button>
      -->
              <el-button
                type="danger"
                icon="Delete"
                circle
                @click="deleteAdmin(scope.$index)"
              >
                <i class="el-icon-delete"></i>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-dialog
          :visible="dialogFormVisible"
          title="添加管理员"
          @close="dialogFormVisible = false"
          :close-on-click-modal="true"
          :append-to-body="true"
        >
          <el-form
            :model="form"
            :rules="rules"
            ref="addadmin"
            label-width="80px"
          >
            <el-form-item label="登录名" prop="username">
              <el-input
                type="text"
                placeholder="请输入新管理员登录名"
                size="small"
                prefix-icon="el-icon-user"
                v-model="form.username"
              ></el-input>
            </el-form-item>

            <el-form-item label="密码" prop="password">
              <el-input
                type="password"
                placeholder="建议密码8位以上，且有数字，特殊字符以及大小写字母"
                size="small"
                show-password
                prefix-icon="el-icon-lock"
                v-model="form.password"
              ></el-input>
            </el-form-item>
            <el-form-item label="确认密码" prop="qrpassword">
              <el-input
                type="password"
                placeholder="再次确认密码。"
                size="small"
                show-password
                prefix-icon="el-icon-lock"
                v-model="form.qrpassword"
              ></el-input>
            </el-form-item>

            <el-form-item label="权限" prop="permission">
              <el-select
                v-model="form.permission"
                placeholder="请选择权限"
                size="large"
              >
                <el-option label="超级管理员" value="1"></el-option>
                <el-option label="普通管理员" value="2"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item label="备注" prop="note">
              <el-input
                type="text"
                placeholder="请输入该管理员的备注"
                size="small"
                prefix-icon="el-icon-tickets"
                v-model="form.note"
              ></el-input>
            </el-form-item>

            <el-form-item label="您的密码" prop="username">
              <el-input
                type="password"
                placeholder="请输入您的密码以确认身份"
                size="small"
                prefix-icon="el-icon-key"
                show-password
                v-model="form.oldpassword"
              ></el-input>
            </el-form-item>
          </el-form>

          <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button type="primary" @click="SubmitAdd">确认添加</el-button>
            </span>
          </template>
        </el-dialog>
      </el-tab-pane>

      <el-tab-pane label="历史操作" name="third">
        <el-input
          id="log"
          disabled="true"
          rows="15"
          type="textarea"
          placeholder="历史操作记录"
          v-model="log"
          style="margin-top: 10px;"
        />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ElementUI from "element-ui";

export default {
  //权限管理
  name: "AdminJurisdict",
  data() {
    // 验证第二次输入密码
    var validatePass2 = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请再次输入一次密码"));
        // password 是表单上绑定的字段
      } else if (value !== this.form.password) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    // 验证时间
    var validateTime = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入结束时间"));
        // password 是表单上绑定的字段
      } else if (this.param.begin > value) {
        callback(new Error("开始时间不能大于结束时间"));
      } else {
        callback();
      }
    };
    return {
      activeName: "first",
      //提交开发时间
      param: {
        begin: "",
        end: "",
      },
      //历史记录
      log: "",
      //表格上显示的开发时间
      timeData: [
        {
          begin: "无",
          end: "无",
        },
      ],
      time1: "",
      time2: "",
      form: {
        username: "",
        password: "",
        qrpassword: "",
        permission: "",
        note: "",
        oldpassword: "",
      },
      //添加管理员的规则
      rules: {
        username: [
          { required: true, message: "请输入管理员登录名", trigger: ["blur"] },
          {
            pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
            message: "用户名不能出现特殊字符",
            trigger: ["blur", "change"],
          },
        ],
        password: [
          { required: true, message: "请输入管理员密码", trigger: ["blur"] },
          { min: 6, message: "密码不能小于6位", trigger: ["blur", "change"] },
          {
            pattern: new RegExp(/^\S*$/, "g"),
            message: "密码不能出现空格",
            trigger: ["blur", "change"],
          },
        ],
        qrpassword: [
          // {required:true,message:"请再输入一次密码",trigger:"blur"},
          { required: true, validator: validatePass2, trigger: "blur" },
        ],
        oldpassword: [
          { required: true, message: "请输入您的密码", trigger: ["blur"] },
          { min: 6, message: "密码不能小于6位", trigger: ["blur", "change"] },
          {
            pattern: new RegExp(/^\S*$/, "g"),
            message: "密码不能出现空格",
            trigger: ["blur", "change"],
          },
        ],
      },
      //设置时间的规则
      rules2: {
        begin: [
          { required: true, message: "请输入开始时间", trigger: ["blur"] },
        ],
        end: [
          { required: true, validator: validateTime, trigger: "blur" },
          //{ required:true,message:"请输入结束时间",trigger:["blur"]},
        ],
      },
      dialogFormVisible: false,
      tableData: [],
    };
  },
  created() {
    // 进行身份验证（可以解决刷新和直接进入页面问题）
    this.$public.tokenadminlogin2(
      this.$cookies,
      this.axios,
      this.$store,
      this.$router,
      this.$message
    );
  },
  mounted() {
    this.getTime();
    this.showAdmin();
    this.showlog();
  },
  methods: {
    //删除时间
    deleteTime() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "正在删除……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("deleteopentime", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.timeData = [
            {
              begin: "无",
              end: "无",
            },
          ];
        }
      });
    },
    //获取时间
    getTime() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "加载开放时间中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: "#timetable",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("getopentime", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          let data = [
            {
              begin: response.data["begin"],
              end: response.data["end"],
            },
          ];
          this.timeData = data;
        }
      });
    },
    //设置时间
    setTime() {
      let error = 0;
      this.$refs.time.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写时间信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      //this.param.begin.Format("yyyy-MM-dd hh:mm:ss")
      //(new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18
      this.$confirm(
        "请确定是否将网站的开发时间设置为：" +
          String(this.param.begin) +
          "~" +
          this.param.end,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "设置网站开放时间中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("begin", this.param.begin);
          params.append("end", this.param.end);
          params.append("token", this.$cookies.get("token"));
          this.axios.post("setopentime", params).then((response) => {
            load.close();
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.getTime();
              this.$forceUpdate(); //解决数据渲染延迟
            }
          });
          this.$message({
            type: "success",
            message: "设置成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消设置",
          });
        });
    },
    //删除管理员
    deleteAdmin(index) {
      this.$confirm("此操作将永久删除该管理员, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "删除该管理员中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          params.append("delaccount", this.tableData[index]["account"]);
          this.axios.post("deleteadmin", params).then((response) => {
            load.close();
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.showAdmin();
              this.$message({
                type: "success",
                message: "删除成功!",
              });
            } else {
              this.$message({
                type: "error",
                message: "删除失败",
              });
            }
          });
        })
        .catch(() => {});
    },
    addAdmin() {
      //1级为最高，可以添加用户
      if (this.$store.state.adminuser.permission != 1) {
        ElementUI.Message({
          message: "您的权限不足，无法添加管理员",
          type: "error",
        });
      } else {
        this.dialogFormVisible = true;
      }
    },
    showlog() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "加载历史操作中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: "#log",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("showlog", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          //注意这里是length，不是size
          for (let i = 0; i < response.data["log"].length; i++) {
            this.log += response.data["log"][i] + "\n";
            this.$forceUpdate(); //解决数据渲染延迟
            //console.log(this.log)
          }
        }
      });
    },
    //提交表单
    SubmitAdd() {
      let error = 0;
      this.$refs.addadmin.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写管理员信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      let params = new URLSearchParams();
      /*
      * token
        newusername
        newpassword
        permission
        oldpassword
        note
      * */
      //params.append("oldusername",this.$store.state.adminuser.username)
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "添加管理员中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      params.append("oldpassword", this.form.oldpassword);
      params.append("newusername", this.form.username);
      params.append("newpassword", this.form.password);
      params.append("permission", this.form.permission);
      params.append("note", this.form.note);
      params.append("token", this.$cookies.get("token"));

      this.axios.post("addadmin", params).then((response) => {
        load.close();

        if (response.data["msg"] == "ok") {
          this.$message({
            type: "success",
            message: "添加成功!",
          });
          this.showAdmin();
          this.$forceUpdate(); //解决数据渲染延迟
        } else {
          //
          this.$message({
            type: "error",
            message: "添加失败," + response.data["error"] + "!",
          });
        }
      });
    },

    //显示当前管理员
    showAdmin() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "加载管理员信息中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: "#admintable",
      });
      //1级为最高，可以显示其他用户
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("showadmin", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.tableData = response.data["admin"];
        }
      });
    },
  },
};
</script>

<style scoped>
.opentime-table {
  width: 50%;
  margin: 0 auto;
  margin-bottom: 25px;
  margin-top: 10px;
}
.admin-table {
  width: 80%;
  margin: 0 auto;
  margin-bottom: 25px;
}
</style>