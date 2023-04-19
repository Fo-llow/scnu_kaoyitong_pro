<template>
  <el-container direction="vertical" style="height: 1000px">
    <el-main>
      <!--
      prop：表单域 model 字段，在使用 validate、resetFields 方法的情况下，该属性是必填的
      -->
      <!-- direction="vertical"-->
      <div style="margin:0 auto;">
        <el-col :span="9">
          <div class="table">
            <div class="sontable">
              <span style="font-size: 20px;font-weight: bold;">报名信息</span>
              <el-divider></el-divider>
              <el-form
                :model="form"
                :rules="rules"
                ref="signup"
                label-width="80px"
                :disabled="this.steps !== '1'"
              >
                <el-form-item label="姓名" prop="name">
                  <el-input
                    type="text"
                    placeholder="请填写考生本人真实姓名"
                    size="small"
                    prefix-icon="el-icon-s-ticket"
                    v-model="form.name"
                  ></el-input>
                </el-form-item>
                <el-form-item label="性别" prop="sex">
                  <div>
                    <el-radio v-model="form.sex" label="1" size="large"
                      >男</el-radio
                    >
                    <el-radio v-model="form.sex" label="2" size="large"
                      >女</el-radio
                    >
                  </div>
                </el-form-item>
                <el-form-item label="身份证号" prop="sfzh">
                  <el-input
                    type="text"
                    placeholder="请填写考生本人身份证号"
                    size="small"
                    prefix-icon="el-icon-postcard"
                    v-model="form.sfzh"
                  ></el-input>
                </el-form-item>

                <el-form-item label="准考证号" prop="ticket">
                  <el-input
                    type="text"
                    placeholder="可作为登录用户名，往届考生请重新注册"
                    size="small"
                    prefix-icon="el-icon-tickets"
                    v-model="form.ticket"
                    disabled
                  ></el-input>
                </el-form-item>
                <el-form-item label="手机号码" prop="tele">
                  <el-input
                    type="text"
                    placeholder="接收申请结果等相关信息，"
                    size="small"
                    prefix-icon="el-icon-phone"
                    v-model="form.tele"
                  ></el-input>
                </el-form-item>
                <el-form-item label="电子邮箱" prop="email">
                  <el-input
                    type="text"
                    placeholder="密码找回和接收公告等"
                    size="small"
                    prefix-icon="el-icon-message"
                    v-model="form.email"
                  ></el-input>
                </el-form-item>
                <el-form-item label="专业层次" prop="zycc">
                  <el-select
                    v-model="form.zycc"
                    placeholder="Select"
                    size="small"
                    @change="Change"
                  >
                    <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item label="报考学校" prop="zymc">
                  <el-select
                    v-model="form.zymc"
                    placeholder="Select"
                    size="small"
                  >
                    <el-option
                      v-for="item in options_zymc"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-form-item>

                <el-form-item label="课程代码" prop="kcdm">
                  <el-checkbox-group v-model="form.kcdm">
                    <el-checkbox
                      v-for="item in kcdm"
                      :label="item.name + '(' + item.code + ')'"
                      :key="item.code"
                    >
                    </el-checkbox>
                  </el-checkbox-group>
                </el-form-item>
                <el-form-item label="文件上传" prop="file">
                  <el-upload
                    ref="upload"
                    list-type="text"
                    :on-preview="handlePictureCardPreview"
                    :before-upload="beforeUpload"
                    :on-remove="handleRemove"
                    :auto-upload="false"
                    :multiple="true"
                    :limit="3"
                    show-file-list
                  >
                    <el-button type="primary">点击上传</el-button>
                    <!--<el-button type="primary">上传个人照片</el-button>-->
                    <template #tip>
                      <div class="el-upload__tip">
                        JPG/PNG/PDF文件大小不得超过500KB。
                      </div>
                    </template>
                  </el-upload>
                </el-form-item>
                &nbsp;&nbsp;&nbsp;&nbsp;<el-button type="danger" @click="Submit"
                  >开始报名</el-button
                >
                &nbsp;&nbsp;&nbsp;<el-button type="primary" @click="goIndex"
                  >返回首页</el-button
                >
              </el-form>
            </div>
          </div>
        </el-col>
        <el-col :span="9">
          <div style="margin-left: 10px; margin-top: 100px">
            <el-card style="width: 400px; height: 380px">
              <span style="text-align: center; margin: 0 auto">欢迎报名</span>
              <ul>
                <li>请报考人员认真阅读本说明，按要求填写报名表。</li>
                <li style="color: red">
                  注意：提交的文件为承诺书等文件，请务必上传
                </li>
                <li>报考人所填写信息务必真实、准确、规范、清晰。</li>
                <li style="color: red">
                  请报考人员在提交前对信息的真实性进行核对，信息不实引起的相关责任由考生本人承担。
                </li>
                <li>报名成功请耐心等待审核，审核结果请查看报名信息。</li>
              </ul>
            </el-card>
          </div>
        </el-col>
      </div>
    </el-main>
  </el-container>
</template>

<script>
export default {
  name: "Signupinfo",
  data() {
    var validateIdNo = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入身份证号"));
        return;
      }
      var format =
        /^(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\d{4}(([1][9]\d{2})|([2]\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\d{3}[0-9xX]$/;
      //号码规则校验
      if (!format.test(value)) {
        callback(new Error("请输入正确身份证号"));
      }
      //区位码校验
      //出生年月日校验  前正则限制起始年份为1900;
      var year = value.substr(6, 4), //身份证年
        month = value.substr(10, 2), //身份证月
        date = value.substr(12, 2), //身份证日
        time = Date.parse(month + "-" + date + "-" + year), //身份证日期时间戳date
        now_time = Date.parse(new Date()), //当前时间戳
        dates = new Date(year, month, 0).getDate(); //身份证当月天数
      if (time > now_time || date > dates) {
        callback(new Error("请输入正确身份证号"));
      }
      //校验码判断
      330324200203163603
      var c = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2); //系数
      var b = new Array("1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2"); //校验码对照表
      var id_array = value.split("");
      var sum = 0;
      for (var k = 0; k < 17; k++) {
        sum += parseInt(id_array[k]) * parseInt(c[k]);
      }
      if (id_array[17].toUpperCase() != b[sum % 11].toUpperCase()) {
        callback(new Error("请输入正确身份证号"));
      }
      callback();
    };
    return {
      kcdm: [],
      param: "", // 表单要提交的参数
      //与表单绑定的
      form: {
        name: "",
        sex: "",
        sfzh: "",
        ticket: "",
        tele: "",
        email: "",
        zycc: "",
        zymc: "",
        kcdm: [],
      },
      steps: "1",
      //定义校验规则
      rules: {
        name: [
          { required: true, message: "请输入姓名", trigger: "blur" },
          {
            pattern: /^[\u4E00-\u9FA5]+$/i,
            message: "请输入中文姓名",
            trigger: ["blur", "change"],
          },
        ],
        sex: [{ required: true, message: "请选择性别", trigger: "blur" }],
        sfzh: [{ required: true, validator: validateIdNo, trigger: "blur" }],
        ticket: [
          { required: true, message: "请输入准考证号", trigger: "blur" },
          {
            pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
            message: "准考证号不能出现特殊字符",
            trigger: ["blur", "change"],
          },
          {
            pattern: new RegExp(/^\S*$/, "g"),
            message: "准考证号不能出现空格",
            trigger: ["blur", "change"],
          },
        ],
        tele: [
          {
            pattern: new RegExp(
              /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,
              "g"
            ),
            message: "手机号码格式不正确",
            trigger: ["blur", "change"],
          },
        ],
        email: [
          { type: "email", message: "邮箱格式错误", trigger: "blur" },
          {
            required: true,
            message: "请输入电子邮箱",
            trigger: ["blur", "change"],
          },
        ],
        zycc: [{ required: true, message: "请选择专业层次", trigger: "blur" }],
        zymc: [{ required: true, message: "请选择报考学校", trigger: "blur" }],
      },
      options: [
        {
          value: "本科",
          label: "本科",
        },
        {
          value: "专科",
          label: "专科",
        },
      ],
      fileList: [],
      options_zymc: [],
      Height: 0,
    };
  },
  created() {
    // 进行身份验证（可以解决刷新和直接进入页面问题）
    this.$public.tokenlogin2(
      this.$cookies,
      this.axios,
      this.$store,
      this.$router,
      this.$message
    );
  },
  mounted() {
    //加载课程代码
    this.GetCode();
    this.JudgeRes();
    this.$forceUpdate(); //解决数据渲染延迟
  },
  updated() {
    if (this.steps !== "1") {
      //说明已经报名
      this.$confirm(
        "您已经报名过了，无法继续报名，是否前往报名情况界面?",
        "提示",
        {
          confirmButtonText: "确定",
          type: "warning",
        }
      )
        .then(() => {
          this.$router.push("/success/registerstatus");
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消提交",
          });
        });
    }
  },
  methods: {
    //返回登陆成功的首页
    goIndex() {
      this.$router.push("/success/successindex");
    },
    //获取个人信息
    getInfo() {
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("personinfo", params).then((response) => {
        //console.log(response.data)
        //console.log(response.data['msg'])

        if (response.data["msg"] === "ok") {
          //填上数据
          this.form.name = response.data["name"];
          this.form.sex = response.data["sex"];
          this.form.ticket = response.data["ticket"];
          this.form.tele = response.data["tele"];
          this.form.email = response.data["email"];
          this.form.sfzh = response.data["sfzh"];
          this.$forceUpdate(); //解决数据渲染延迟
        }
      });
    },
    //获取报名状态
    /*Buttonstate(){
      let param = new URLSearchParams();
      param.append("token",this.$cookies.get("token"))
      this.axios.post("signupres",
          param
      ).then((response) => {
        //console.log(response.data)
        //console.log(response.data['msg'])
        this.$forceUpdate()         //解决数据渲染延迟
        if( response.data['msg'] === "ok"){
          //  res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
          if( response.data['res'] === "2" ){         //未报名
            this.$forceUpdate()
            //console.log("help")

            this.steps = "1";
          }else if( response.data['res'] === "0"  ){  //未审核
            this.steps = "2";
          }else if( response.data['res'] === "1" || response.data['res'] === "-1"  ){     //审核结果
            this.steps = "3";
          }
        }
      })
    },*/
    GetCode() {
      //console.log("正在获取课程代码" )
      let param = new URLSearchParams();
      param.append("token", this.$cookies.get("token"));
      this.axios.post("showcode", param).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] === "ok") {
          this.kcdm = response.data["course"];
        }
      });
    },
    Change() {
      this.GetCode();
      this.form.zymc = "";
      if (this.form.zycc === "本科") {
        this.options_zymc = [
          {
            value: "华南师范大学",
            label: "华南师范大学",
          },
          {
            value: "中山大学",
            label: "中山大学",
          },
          {
            value: "深圳大学",
            label: "深圳大学",
          },
          {
            value: "暨南大学",
            label: "暨南大学",
          },
          {
            value: "广东工业大学",
            label: "广东工业大学",
          },
          {
            value: "广州大学",
            label: "广州大学",
          },
          {
            value: "华南理工大学",
            label: "华南理工大学",
          },
        ];
      } else if (this.form.zycc === "专科") {
        this.options_zymc = [
          {
            value: "华南师范大学",
            label: "华南师范大学",
          },
          { value: "中山大学", label: "中山大学" },
          {
            value: "深圳大学",
            label: "深圳大学",
          },
          {
            value: "暨南大学",
            label: "暨南大学",
          },
          {
            value: "广东工业大学",
            label: "广东工业大学",
          },
          {
            value: "广州大学",
            label: "广州大学",
          },
          {
            value: "华南理工大学",
            label: "华南理工大学",
          },
        ];
      }
    },
    Submit() {
      let error = 0;
      this.$refs.signup.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写报名信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      /*if( this.fileList.length == 0 ){
        this.$message({
          type: 'error',
          message: '文件内容不能为空'
        });
        return
      }*/

      this.$confirm("提交信息无误，确认提交?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.$refs.upload.submit();
          //console.log(this.param)
          this.param.append("token", this.$cookies.get("token"));
          this.param.append("name", this.form.name);
          this.param.append("sex", this.form.sex);
          this.param.append("sfzh", this.form.sfzh);
          this.param.append("ticket", this.form.ticket);
          this.param.append("tele", this.form.tele);
          this.param.append("email", this.form.email);
          this.param.append("zycc", this.form.zycc);
          this.param.append("zymc", this.form.zymc);
          this.param.append("kcdm", this.form.kcdm);
          //    设置上传数据类型
          let configs = {
            headers: { "Content-Type": "multipart/form-data" },
          };

          this.axios.post("upload", this.param, configs).then((response) => {
            //console.log(response.data)
            if (response.data["msg"] === "ok") {
              this.$forceUpdate(); //解决数据渲染延迟
            }
          });
          this.$message({
            type: "success",
            message: "提交成功，请耐心等待审核结果!",
          });
          this.JudgeRes();
          this.$forceUpdate();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消提交",
          });
        });
    },
    ///返回首页
    goHome() {
      this.$router.push("/index");
    },
    //跳转注册界面
    goRegister() {
      this.$router.push("/register");
    },
    //跳转注册界面
    goAdminLogin() {
      this.$router.push("/adminlogin");
    },
    //跳转忘记密码器页面
    goForget() {
      this.$router.push("/forget");
    },
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    beforeUpload(file) {
      this.param = new FormData();
      this.fileList.push(file);
      let images = [...this.fileList];
      //console.log("images", images);

      //this.param.append("file1", images);

      images.forEach((img) => {
        this.param.append("file", img); // 把单个图片重命名，存储起来（给后台）
      });
      return false;
    },
    //判断审核结果
    JudgeRes() {
      let param = new URLSearchParams();
      param.append("token", this.$cookies.get("token"));
      this.axios.post("signupres", param).then((response) => {
        //console.log(response.data)
        //console.log(response.data['msg'])

        if (response.data["msg"] === "ok") {
          //  res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
          if (response.data["res"] === "2") {
            //未报名
            this.steps = "1";
            this.$forceUpdate();
            this.getInfo();
          } else if (response.data["res"] === "0") {
            //未审核
            this.steps = "2";
          } else if (
            response.data["res"] === "1" ||
            response.data["res"] === "-1"
          ) {
            //审核结果
            this.steps = "3";
          }
          this.$forceUpdate(); //解决数据渲染延迟
        }
      });
    },
  },
};
</script>


<style lang="less" scoped>
/*表单那块——注册*/
.table {
  // margin-left: 15px;
  margin-top: 10px;
  margin: 0 auto;
  width: 600px;
  font-size: 14px;
  line-height: 34px;
}
.sontable {
  margin-top: 10px;
  // margin-left:30px;
  margin: 0 auto;
}
.el-card {
  width: 270px;
  height: 320px;
  margin-top: 100px;
  margin-left: 150px;
  span {
    font-weight: bolder;
    font-size: 17px;
    font-size: 20px;
  }
  ul {
    padding-left: none;
    li {
      margin-top: 5px;
      font-size: 14px;
      color: #424242;
      line-height: 34px;
    }
  }
}
.el-input {
  width: 240px;
}
.el-main {
  margin: 0 auto;
}
</style>