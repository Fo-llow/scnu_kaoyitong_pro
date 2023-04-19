<template>
  <div>
    <template>
      <br><br>
      <el-steps
        :active="steps"
        style="width: 800px; margin-top: 80px; margin: 0 auto"
      >
        <el-step title="步骤1" description="开始报名"></el-step>
        <el-step title="步骤2" description="等待审核"></el-step>
        <el-step title="步骤3" description="报名结果"></el-step>
      </el-steps>
      <br><br><br>
      <div v-show="steps == 1" style="margin:0 auto;">
        <el-card
        style="
            width: 400px;
            text-align: center;
            margin-top: 100px;
            margin: 0 auto;
          "
>
          <span style="text-align: center;font-size: 20px; font-weight: bold">您当前尚未开始报名</span>
          <br><br><br> 进入报名页面&ensp;&ensp;
          <el-button type="danger" @click="goSignUp">我要报名</el-button>
        </el-card>
      </div>
      <div v-show="steps == 2" style="margin-left: 25%; margin-top: 100px">
        <template>
          <el-card style="width: 420px">
            <el-descriptions
              title="报名信息"
              style="width: 380px"
              width="3px"
              :column="1"
              size="large"
              label-align="center"
              align="center"
              border
            >
              <el-descriptions-item label="姓名">{{
                infoData.name
              }}</el-descriptions-item>
              <el-descriptions-item label="性别">{{
                infoData.sex
              }}</el-descriptions-item>
              <el-descriptions-item label="身份证号">{{
                infoData.sfzh
              }}</el-descriptions-item>
              <el-descriptions-item label="准考证号">{{
                infoData.ticket
              }}</el-descriptions-item>
              <el-descriptions-item label="手机号码">{{
                infoData.tele
              }}</el-descriptions-item>
              <el-descriptions-item label="专业层次">{{
                infoData.zycc
              }}</el-descriptions-item>
              <el-descriptions-item label="报考学校">{{
                infoData.zymc
              }}</el-descriptions-item>
              <el-descriptions-item label="课程代码">{{
                infoData.kcdm
              }}</el-descriptions-item>
              <el-descriptions-item label="提交文件">
                <el-link
                  v-for="(f, key) in segmenteFile(infoData.file)"
                  :key="key"
                  target="_blank"
                  :href="f.fsrc"
                  >{{ f.fname }}</el-link
                >
              </el-descriptions-item>
            </el-descriptions>
            <el-button
              type="danger"
              style="margin-left: 30%; margin-top: 20px"
              @click="modifySign"
              >我要修改</el-button
            >
          </el-card>
        </template>
      </div>

      <el-dialog
        :visible="dialogFormVisible"
        title="修改报名信息"
        @close="dialogFormVisible = false"
        :close-on-click-modal="true"
        :append-to-body="true"
      >
        <el-form
          :model="form"
          :rules="rules"
          ref="modifysignup"
          label-width="80px"
        >
          <el-form-item label="姓名" prop="name">
            <el-input
              type="text"
              placeholder="请填写考生本人真实姓名，非常重要"
              size="small"
              prefix-icon="el-icon-s-ticket"
              v-model="form.name"
            ></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="sex">
            <div>
              <el-radio v-model="form.sex" label="1" size="large">男</el-radio>
              <el-radio v-model="form.sex" label="2" size="large">女</el-radio>
            </div>
          </el-form-item>
          <el-form-item label="身份证号" prop="sfzh">
            <el-input
              type="text"
              placeholder="请填写考生本人身份证号，非常重要，末位为X的请大写"
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
              disabled="true"
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
              placeholder="密码找回和接收公告等，非常重要"
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
            <el-select v-model="form.zymc" placeholder="Select" size="small">
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
          <el-tag style="color: red"
            >注意：如果需要修改，所有提交过的文件需要重新提交</el-tag
          >
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
              show-file-list="true"
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
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="diaCancel">取消</el-button>
            <el-button type="primary" @click="SubmitModify">确认修改</el-button>
          </span>
        </template>
      </el-dialog>

      <div v-show="steps == 3" style="margin-top: 100px;">
        <el-card
          style="
            width: 400px;
            text-align: center;
            margin-top: 100px;
            margin: 0 auto;
          "
        >
          <span style="text-align: center; font-size: 20px; font-weight: bold"
            >审核结果</span
          >
          <br /><br /><br />
          <span v-show="result == 1" style="font-size: 15px">
            您的报名已审核通过，请注意留意后续公告！
          </span>
          <span v-show="result == -1">
            您的报名审核不通过，请重新检查报名信息！

            <el-button type="danger" @click="regoSignUp">重新报名</el-button>
          </span>
        </el-card>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  //报名情况
  name: "RegisterStatus",
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
      //修改对话框
      dialogFormVisible: false,
      options_zymc: [],

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
      //审核结果：1为审核通过，-1为审核不通过
      result: "1",
      //步骤二中显示的报名情况
      infoData: {
        name: "",
        sex: "",
        sfzh: "",
        ticket: "",
        tele: "",
        email: "",
        zycc: "",
        zymc: "",
        kcdm: "",
        file: "",
      },
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
  mounted: function () {
    //判断当前审核结果
    this.JudgeRes(); //需要触发的函数
  },
  methods: {
    //重新报名
    regoSignUp() {
      this.$confirm("重新报名会删除原来的报名信息，是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "删除原有报名信息中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          this.axios.post("deletesignup", params).then((response) => {
            load.close();
            //console.log(response.data)
            //console.log(response.data['msg'])

            if (response.data["msg"] == "ok") {
              this.goSignUp(); //进入报名界面
              this.$forceUpdate(); //解决数据渲染延迟
            }
          });
          this.$message({
            type: "success",
            message: "提交成功，请耐心等待审核结果!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消提交",
          });
        });
    },
    //分割文件  file是所有的字符串地址
    segmenteFile(file) {
      if (file == null) return null; //注意加上，否则文件为空的话就会报错不显示
      let array = file.split("|");
      //console.log(array)
      //console.log(array.length)
      let list = [];
      for (let i = 0; i < array.length; i++) {
        let names = array[i].split("_");
        list.push({
          fname: names[1],
          fsrc:
            this.$store.state.baseurl +
            array[i] +
            "?token=" +
            this.$cookies.get("token"),
        });
      }
      //console.log(list)
      return list;
    },
    //判断审核结果
    JudgeRes() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "正在获取报名情况……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("signupres", params).then((response) => {
        load.close();
        //console.log(response.data)
        //console.log(response.data['msg'])

        if (response.data["msg"] == "ok") {
          //  res表示审核的结果（0表示未审核，1表示审核通过，-1表示审核不通过，2表示没开始报名）
          if (response.data["res"] == "2") {
            //未报名
            this.steps = "1";
          } else if (response.data["res"] == "0") {
            //未审核
            this.steps = "2";
            //开启loading
            const load = this.$loading({
              lock: true,
              text: "加载报名信息中……",
              spinner: "el-icon-loading",
              background: "rgba(0, 0, 0, 0.7)",
            });
            let param = new URLSearchParams();
            param.append("token", this.$cookies.get("token"));
            this.axios.post("signupinfo", param).then((response) => {
              load.close();
              console.log(this.infoData);
              this.infoData.name = response.data["name"];
              this.infoData.sex = response.data["sex"];
              this.infoData.ticket = response.data["ticket"];
              this.infoData.sfzh = response.data["sfzh"];
              this.infoData.tele = response.data["tele"];
              this.infoData.email = response.data["email"];
              this.infoData.zycc = response.data["zycc"];
              this.infoData.zymc = response.data["zymc"];
              this.infoData.kcdm = response.data["kcdm"];
              this.infoData.file = response.data["file"];
            });
          } else if (
            response.data["res"] == "1" ||
            response.data["res"] == "-1"
          ) {
            //审核结果
            this.steps = "3";
            this.result = response.data["res"];
          }
        }
        this.$forceUpdate(); //解决数据渲染延迟
      });
    },
    //取消对话框
    diaCancel() {
      this.dialogFormVisible = false;
      //this.JudgeRes();
      //location.reload();
      this.$forceUpdate();
    },
    //进入修改报名的对话框
    goSignUp() {
      this.$router.push("/success/signupinfo");
    },
    //添加选项
    AddOption() {
      if (this.form.zycc == "本科") {
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
      } else if (this.form.zycc == "专科") {
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
    Change() {
      //this.GetCode();
      this.form.zymc = "";
      if (this.form.zycc == "本科") {
        this.options_zymc = [
          {
            value: "华南师范大学",
            label: "华南师范大学大学",
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
      } else if (this.form.zycc == "专科") {
        this.options_zymc = [
          {
            value: "华南师范大学",
            label: "华南师范大学大学",
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
      }
    },
    //点击修改报名信息
    modifySign() {
      this.fileList = [];

      this.getCode(); //获取课程代码
      this.dialogFormVisible = true;
      //这里变成了深拷贝，不可以这样做!!!
      //this.form  = this.infoData

      this.form.name = this.infoData.name;
      this.form.sex = this.infoData.sex;
      this.form.ticket = this.infoData.ticket;
      this.form.sfzh = this.infoData.sfzh;
      this.form.tele = this.infoData.tele;
      this.form.email = this.infoData.email;
      this.form.zycc = this.infoData.zycc;

      this.AddOption();
      this.$forceUpdate(); //解决数据渲染延迟

      this.form.zymc = this.infoData.zymc;
      this.form.kcdm = this.infoData.kcdm;
      this.form.file = this.infoData.file;

      //修改一些对应的东西
      //1.性别对应表单的
      if (this.infoData.sex == "男") {
        this.form.sex = "1";
      } else {
        this.form.sex = "2";
      }
      //2.课程对应表单的
      this.form.kcdm = [];
      //this.form.kcdm = this.infoData.kcdm
      //是这个逗号吗？？ 现在还不太确定
      let array = this.infoData.kcdm.toString().split(",");
      for (let i = 0; i < array.length; i++) {
        //his.$set(this.form.kcdm, array[i], [])
        this.form.kcdm.push(array[i]);
      }
      console.log(this.form.kcdm);
      console.log(this.infoData.kcdm);
      this.$forceUpdate(); //解决数据渲染延迟
    },
    //上交修改的信息
    SubmitModify() {
      let error = 0;
      this.$refs.modifysignup.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写修改的报名信息",
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
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "提交中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
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
          //console.log(configs)
          this.axios
            .post("modifyupload", this.param, configs)
            .then((response) => {
              load.close();
              //console.log(response.data)
              if (response.data["msg"] === "ok") {
                this.dialogFormVisible = false;
                this.JudgeRes();
                this.$forceUpdate(); //解决数据渲染延迟
              }
            });
          this.$message({
            type: "success",
            message: "提交成功，请耐心等待审核结果!",
          });
          this.fileList = [];
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消提交",
          });
        });
    },
    //获取课程代码
    getCode() {
      //console.log("正在获取课程代码" )
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("showcode", params).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.kcdm = response.data["course"];
        }
      });
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
  },
};
</script>

<style scoped>
</style>