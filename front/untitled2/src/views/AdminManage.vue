<template>
  <el-tabs v-model="activeName" @tab-click="handleClick">
    <el-tab-pane label="当前课程代码" name="first">
      <div class="course-code">
        <el-table :data="codeData" id="table">
          <el-table-column
            class="coursename"
            width="300px"
            prop="name"
            label="课程名称"
            align="center"
          />
          <el-table-column
            class="coursecode"
            width="200px"
            prop="code"
            label="课程代码"
            align="center"
          />
          <el-table-column
            class="courseop"
            width="200px"
            prop="operation"
            label="操作"
            align="center"
          >
            <template #default="scope">
              <el-button type="primary" circle @click="editCode(scope.$index)">
                <i class="el-icon-edit"></i>
              </el-button>
              <el-button
                type="danger"
                icon="Delete"
                circle
                @click="deleteCode(scope.$index)"
              >
                <i class="el-icon-delete"></i>
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-tab-pane>
    <el-tab-pane label="添加课程代码" name="second">
      <div class="add-course-code">
        <el-form :model="param" :rules="rules" ref="addcode" label-width="80px">
          <el-form-item label="课程代码" prop="code">
            <el-input
              type="text"
              placeholder="请输入要添加的课程代码"
              size="small"
              prefix-icon="el-icon-notebook-1"
              v-model="param.code"
              style="width: 330px;"
            ></el-input>
          </el-form-item>
          <el-form-item label="课程名称" prop="name">
            <el-input
              type="text"
              placeholder="请输入要添加的课程名称"
              size="small"
              prefix-icon="el-icon-lollipop"
              v-model="param.name"
              style="width: 330px"
            ></el-input>
          </el-form-item>
          <br>
          <el-button type="danger" @click="Submit"
            >添加课程代码</el-button
          >
        </el-form>

        <el-dialog
          :visible="dialogFormVisible"
          title="修改课程代码"
          @close="dialogFormVisible = false"
          :close-on-click-modal="true"
          :append-to-body="true"
        >
          <el-form :model="form" :rules="rules2" ref="modifycode">
            <el-form-item label="课程代码" label-width="140px" prop="code">
              <el-input
                v-model="form.code"
                prefix-icon="el-icon-notebook-1"
                autocomplete="off"
              />
            </el-form-item>
            <el-form-item label="课程名称" label-width="140px" prop="name">
              <el-input
                v-model="form.name"
                prefix-icon="el-icon-lollipop"
                autocomplete="off"
              />
            </el-form-item>
          </el-form>
          <template #footer>
            <span class="dialog-footer">
              <el-button @click="dialogFormVisible = false">取消</el-button>
              <el-button type="primary" @click="modifyCode">确认修改</el-button>
            </span>
          </template>
        </el-dialog>
      </div>
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import ElementUI from "element-ui";

export default {
  //报名管理
  name: "AdminManage",
  data() {
    return {
      activeName: "first", // 默认查看当前课程代码
      //是否显示对话框
      dialogFormVisible: false,
      param: {
        code: "",
        name: "",
      },
      // 对话框里面的数据
      form: {
        code: "",
        name: "",
      },
      index: -1,
      //课程代码
      codeData: [],
      //添加课程代码规则
      rules: {
        code: [
          {
            required: true,
            message: "请输入要添加的课程代码",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "请输入要添加的课程名称",
            trigger: "blur",
          },
        ],
      },
      //修改课程代码规则
      rules2: {
        code: [
          {
            required: true,
            message: "请输入要添加的课程代码",
            trigger: "blur",
          },
        ],
        name: [
          {
            required: true,
            message: "请输入要添加的课程名称",
            trigger: "blur",
          },
        ],
      },
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
    this.GetCode();
    this.$forceUpdate(); //解决数据渲染延迟
  },
  methods: {
    //获取全部课程代码
    GetCode() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "加载课程代码中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: "#table",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      //console.log("正在获取课程代码" )
      this.axios.post("showcode", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.codeData = response.data["course"];
        }
      });
    },
    //修改课程代码
    modifyCode() {
      let error = 0;
      this.$refs.modifycode.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写修改课程信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      this.dialogFormVisible = false;
      let index = this.index;
      if (
        this.codeData[index]["code"] == this.form.code &&
        this.codeData[index]["name"] == this.form.name
      ) {
        //没有什么变化
        this.$message({
          type: "error",
          message: "修改失败，内容与之前一致",
        });
        return;
      }
      //console.log(index+" "+this.codeData[index]['code'])
      this.$confirm("此操作将修改该课程代码及名称, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "修改课程代码中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          params.append("oldcode", this.codeData[index]["code"]);
          params.append("oldname", this.codeData[index]["name"]);
          params.append("newcode", this.form.code);
          params.append("newname", this.form.name);

          this.axios.post("modifycode", params).then((response) => {
            load.close();
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.GetCode(); //重新获取
              this.$forceUpdate(); //解决数据渲染延迟
            }
          });
          this.$message({
            type: "success",
            message: "修改成功!",
          });
          this.GetCode();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消修改",
          });
        });
    },
    //编辑课程代码
    editCode(index) {
      this.index = index;

      this.dialogFormVisible = true;
      this.form.code = this.codeData[index]["code"];
      this.form.name = this.codeData[index]["name"];
      this.$forceUpdate(); //解决数据渲染延迟
    },
    //删除课程代码
    deleteCode(index) {
      //console.log(index+" "+this.codeData[index]['code'])
      this.$confirm("此操作将删除该课程, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          params.append("code", this.codeData[index]["code"]);
          params.append("name", this.codeData[index]["name"]);
          this.axios.post("deletecode", params).then((response) => {
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.codeData = response.data["course"];
              this.GetCode();
              this.$forceUpdate(); //解决数据渲染延迟
            }
          });
          this.$message({
            type: "success",
            message: "删除成功!",
          });
          this.GetCode();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //添加课程代码
    Submit() {
      let error = 0;
      this.$refs.addcode.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写添加课程信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      this.$confirm(
        "您要添加的课程为：" +
          this.param.code +
          "、" +
          this.param.name +
          "，是否确认无误",
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
            text: "添加课程中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          //解决axios提交到后端为null的情况
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          params.append("code", this.param.code);
          params.append("name", this.param.name);
          //console.log(params)
          this.axios.post("addcode", params).then((response) => {
            load.close();
            //console.log(response.data)
            //console.log(response.data['msg'])
            if (response.data["msg"] == "ok") {
              ElementUI.Message({
                message: "成功添加课程代码",
                type: "success",
              });
              this.$forceUpdate();
              this.GetCode();
            } else {
              ElementUI.Message({
                message: "添加课程代码失败，该课程已存在",
                type: "error",
              });
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消添加",
          });
        });
    },
  },
};
</script>

<style lang="less" scoped>
.course-code {
  // text-align: center;
  margin-top: 20px;
}
.add-course-code {
  text-align: center;
  margin-top: 20px;
}
.el-table {
  width: 60%;
  text-align: center;
  margin:0 auto;
}
.el-table::before{
  width: auto;
}
.el-form{
  margin: 0 auto;
  width: 440px;
}
</style>