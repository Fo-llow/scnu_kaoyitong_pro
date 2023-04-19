<template>
  <div style="height: 100vh">
    <el-container>
      <el-header style="height: 130px">
        <Header></Header>
      </el-header>
      <el-container v-show="show == 1">
        <el-main style="width: 980px">
          <div class="content">
            <div class="TopPic"></div>
            <div class="contentWrap">
              <div class="colum_1">
                <span style="font-size: 20px; font-weight: bold"
                  >考易通自学考试报名系统</span
                >
                <p class="introTxt">
                  本系统用于自考大学专业的社会人士或在读学生等群体的报名、管理和结果查询等。
                  <br /><br />
                  第一次使用本系统进行报名时，请先注册。<span
                    style="color: #da4837; font-weight: bold"
                    >同一身份证号每年只允许注册一次</span
                  >
                  ，注册成功后，可用于报考相关的类别。
                  <br /><br />
                  注册时的用户名和密码将是考生在本系统申请和查询结果等信息的唯一依据。
                </p>
                <br /><br /><br /><br /><br /><br />
                <div class="regSection">
                  还不是本系统的用户？
                  <el-button type="danger" @click="goRegister">
                    注册新用户
                  </el-button>
                </div>
              </div>
            </div>
          </div>
        </el-main>
        <el-main>
          <!--
      prop：表单域 model 字段，在使用 validate、resetFields 方法的情况下，该属性是必填的
      -->
          <!-- direction="vertical"-->
          <div class="table">
            <div class="sontable">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="考生账号登录" name="first">
                  <span class="STYLE2" style="font-size: 14px; color: #cc0000"
                    >&nbsp;{{ error_msg }}</span
                  >

                  <el-form
                    :model="param"
                    :rules="rules"
                    ref="login"
                    label-width="80px"
                  >
                    <el-form-item label="用户名" prop="username">
                      <el-input
                        type="text"
                        placeholder="请输入准考证号"
                        size="small"
                        prefix-icon="el-icon-user"
                        v-model="param.username"
                      ></el-input>
                    </el-form-item>
                    <el-form-item label="密码" prop="password">
                      <el-input
                        type="password"
                        placeholder="请输入密码"
                        size="small"
                        show-password
                        prefix-icon="el-icon-lock"
                        v-model="param.password"
                      ></el-input>
                    </el-form-item>

                    <el-form-item label="验证码" prop="yzm">
                      <el-col :span="12">
                        <el-input
                          type="text"
                          placeholder="验证码"
                          prefix-icon="el-icon-picture"
                          style="width: 100px"
                          v-model="param.yzm"
                        ></el-input>
                      </el-col>
                      <el-col :span="12">
                        <Canvas></Canvas>
                      </el-col>
                    </el-form-item>
                  </el-form>
                  <div style="text-align: center; margin: 0 auto">
                    <el-button
                      type="primary"
                      @click.enter="SubmitForm"
                      @keyup.enter="keyDown"
                      >登录</el-button
                    >&nbsp;
                    <el-button type="primary" @click="goForget"
                      >忘记用户名/密码</el-button
                    >
                  </div>
                </el-tab-pane>
                
                <el-tab-pane label="人脸识别" name="second">
                  <div class="camera">
                    <video ref="video" autoplay></video>
                    <canvas ref="canvas"></canvas>
                  </div>
                  <div class="controls">
                    <el-button type="primary" @click="startCamera"
                      >开始摄像头</el-button
                    >
                    <el-button type="success" @click=" fakeFaceRecognition"
                      >开始识别</el-button
                    >
                  </div>
                </el-tab-pane>
                <el-tab-pane label="微信登录" name="third">
                      <div class="qr-code">
                        <div style="text-align:center;">请打开微信扫一扫</div>
                        <img v-show="!showTip" src="../assets/a.png" style="display:block; margin:0 auto; width:200px; height:200px;" alt="微信登录二维码" @click="startLogin" />
                        <div v-show="showTip" class="tip">{{ tipText }}</div>
                      </div>
                    </el-tab-pane>
              </el-tabs>
            </div>
            <div style="margin-top: 25px; text-align: center">
              <el-link
                type="default"
                style="text-align: center"
                @click="goAdminLogin"
                >管理员登录</el-link
              >
            </div>
          </div>
        </el-main>
      </el-container>
      <el-container v-show="show == 0">
        <div class="content">
          <div class="TopPic"></div>
          <div class="contentWrap">
            <div class="colum_1" style="margin-left: 15%">
              <h1>考易通自学考试报名系统</h1>
              <p class="introTxt"></p>
              <el-result
                icon="warning"
                title="系统暂未开放"
                sub-title="当前时间未开放报名系统"
              ></el-result>
            </div>
          </div>
        </div>
      </el-container>

      <el-foot>
        <div style="text-align: center; margin-top: 20px"></div>
        <Footer></Footer>
      </el-foot>
    </el-container>
  </div>
</template>


<script>
//布局链接：https://blog.csdn.net/weixin_39237340/article/details/121666311
//参考链接：https://blog.csdn.net/qq_33820545/article/details/108529789
//https://blog.csdn.net/weixin_39237340/category_11504745.html
import Footer from "@/components/Footer";
import Header from "@/components/Header";
import Canvas from "@/components/Canvas";
import ElementUI from "element-ui";

export default {
  name: "login",
  components: { Canvas, Header, Footer },
  data() {
    return {
      showTip: false,
      tipText: "",
      timer: null,
      isWhere: -1,
      isWhere2: -1,
      video: null,
      canvas: null,
      stream: null,
      show: "0",
      param: {
        username: "",
        password: "",
        yzm: "",
        error_msg: "",
      },
      //定义校验规则
      rules: {
        username: [
          { required: true, message: "请输入准考证号", trigger: ["blur"] },
          {
            pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
            message: "用户名不能出现特殊字符",
            trigger: ["blur", "change"],
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: ["blur"] },
          { min: 6, message: "密码不能小于6位", trigger: ["blur", "change"] },
          {
            pattern: new RegExp(/^\S*$/, "g"),
            message: "密码不能出现空格",
            trigger: ["blur", "change"],
          },
        ],
        yzm: [
          { required: true, message: "请输入验证码", trigger: ["blur"] },
          {
            pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,
            message: "验证码不能出现特殊字符",
            trigger: ["blur", "change"],
          },
          {
            min: 4,
            max: 4,
            message: "请输入4位验证码",
            trigger: ["blur", "change"],
          },
        ],
      },
      activeName: "first",
    };
  },
  mounted() {
    this.JudgeTime(); //需要触发的函数
    this.video = this.$refs.video;
    this.canvas = this.$refs.canvas;
    window.addEventListener("keydown", this.keyDown);
    window.addEventListener('mousemove', this.handleMouseMove);
  },
  methods: {
    keyDown(e) {
      if (e.keyCode == 13 || e.keyCode == 100) {
        //需要执行的登录方法
        this.submitForm();
      }
    },

    handleMouseMove (event) {
            const x = event.clientX
            const y = event.clientY
            const width = window.innerWidth
            const height = window.innerHeight
            const xRatio = x / width
            const yRatio = y / height
            if (xRatio < 0.5 && yRatio < 0.5) {
              // 鼠标在左上角区域
              // return '公告'
              this.isWhere = 1
            } else if (xRatio >= 0.5 && yRatio < 0.5) {
              // 鼠标在右上角区域
              // return '问题与建议'
              this.isWhere = 2
            } else if (xRatio < 0.5 && yRatio >= 0.5) {
              // 鼠标在左下角区域
              // return '作业'
              this.isWhere = 3
            } else {
              // 鼠标在右下角区域
              // return '系统帮助'
              this.isWhere = 4
            }
          },
    startCamera() {
      navigator.mediaDevices
        .getUserMedia({ video: true })
        .then((stream) => {
          this.stream = stream;
          this.video.srcObject = stream;
          this.video.play();
        })
        .catch((error) => {
          console.log(error);
        });
    },
    fakeFaceRecognition() {
                  const loading = this.$loading({
                    lock: true,
                    text: "正在人脸识别，请稍后...",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                  });

                  setTimeout(() => {
                    loading.close();

                    // 根据鼠标位置进行不同的操作
                    //const mouseX = event.clientX;
                    //const width = window.innerWidth;
                if(this.isWhere ==1) {
                      // 鼠标在右半边屏幕，模拟返回后端信息，并跳转到系统首页
                      const params = new URLSearchParams();
                      params.append("username", "202021320200");
                      params.append("password", "123456");
                      params.append("yzm", "axsd");
                      this.axios({
                        method: "post",
                        url: "login",
                        withCredentials: true,
                        data: params,
                        dataType: "JSONP"
                      }).then(response => {
                        loading.close();
                        if (response.data["msg"] === "ok") {
                          this.$message.success("登录成功");
                          this.$cookies.set("token", response.data["token"], "1d");
                          this.$router.push({ path: "/success/successindex" });
                        } else {
                          this.$message.error(response.data["error"]);
                          this.Canvas.methods.createCode();
                          this.$forceUpdate();
                        }
                      });
                    }
                    else
                    {
                      // 鼠标在左半边屏幕，提示人脸识别失败
                      this.$message.error("人脸识别失败，数据库中未录入该人脸");
                    } 
                  }, 3000);
                },

 startLogin() {
     
  const loading = this.$loading({
                   lock: true,
                    text: "扫码成功，正在登陆，请稍后",
                    spinner: "el-icon-loading",
                    background: "rgba(0, 0, 0, 0.7)"
                  });
       this.timer = setTimeout(() => {
        loading.close();
         this.showTip = false;
          this.tipText = "扫码成功，请稍后";
          this.login();
       }, 3000);
     },
      login() {

       
       if(this.isWhere ==1)
        {
                    // 鼠标在右侧，进行登录
                this.showTip = true;
                this.tipText = "正在登录，请稍后";
                setTimeout(() => {
                  const params = new URLSearchParams();
                  params.append("username", "202021320200");
                  params.append("password", "123456");
                  params.append("yzm", "axsd");
                  this.axios({
                    method: "post",
                    url: "login",
                    withCredentials: true,
                    data: params,
                    dataType: "JSONP",
                  }).then((response) => {
                    this.showTip = false;
                    if (response.data["msg"] === "ok") {
                      this.$message.success("登录成功");
                      this.$cookies.set("token", response.data["token"], "1d");
                      this.$router.push({ path: "/success/successindex" });
                    } else {
                      this.$message.error(response.data["error"]);
                    }
                  });
                }, 500);
          }
        else
        {
            // 鼠标在其他侧，提示注册
        this.$message.error("您的微信用户还未注册，请先注册");
        this.tipText = "即将跳转到注册页面";
        setTimeout(() => {
          this.$router.push("/register");
        }, 1000);
        }
        },           
    JudgeTime() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "加载网页中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });

      this.axios.post("opentime").then((response) => {
        //console.log(response.data)
        //console.log(response.data['msg'])

        if (response.data["msg"] == "ok") {
          //是开放时间
          this.show = "1";
        } else {
          //不是开放时间
          this.show = "0";
        }
        this.$forceUpdate(); //解决数据渲染延迟
        load.close();
      });
    },
    // 提交登录表单
    SubmitForm() {
      // 先判断表单是否填写正常
      let error = 0;
      this.$refs.login.validate((valid) => {
        if (!valid) {
          //console.log("As")
          this.$message({
            type: "error",
            message: "请先正确填写登录信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      //开启loading
      const load = this.$loading({
        lock: true,
        text: "登录中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      //解决axios提交到后端为null的情况
      let params = new URLSearchParams();
      params.append("username", this.param.username);
      params.append("password", this.param.password);
      params.append("yzm", this.param.yzm);

      this.axios({
        method: "post",
        url: "login",
        withCredentials: true,
        data: params,
        dataType: "JSONP", // 处理Ajax跨域问题,
      }).then((response) => {
        //console.log(response.data)
        /*
        email，msg，name，ticket，username
        * */
        //console.log(response.data['msg'])
        load.close();
        if (response.data["msg"] == "ok") {
          //账户密码正确
          //没用到，向下个页面传递参数:https://www.cnblogs.com/hthuang/p/7646472.html
          // 设置全局user
          this.$store.commit("login", response.data);
          ElementUI.Message({
            message: "登录成功",
            type: "success",
          });
          //token 存储进 sessionStorage 中
          //sessionStorage.setItem("token",response.data['token']);
          //token 存储进 cookies 中
          this.$cookies.set("token", response.data["token"], "1d");
          this.$router.push({
            path: "/success/successindex",
          }); //跳转到成功页面
        } else {
          //登录失败
          ElementUI.Message({
            message: response.data["error"],
            type: "error",
          });
          this.error_msg = response.data["error"];
          this.Canvas.methods.createCode(); //换验证码,没换成功？
          this.$forceUpdate(); //解决数据渲染延迟
          //this.$forceUpdate()
        }
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
  },
};
</script>

<style lang="less" scoped>
@import "../mobile.css";

//按钮关系
.relabtn {
  margin-left: 20px;
}
//表单那块——登陆
.table {
  background-color: #eaedf1;
  margin-top: 20px;
  height: 370px;
  font-size: 14px;
  color: #424242;
  line-height: 34px;

  //解决自适应,使登录框永远在右边那块
  max-width: 400px;
  z-index: 2; //z-index 属性指定一个元素的堆叠顺序。
  width: 100%;
  position: absolute;
  right: 40px;
}
.sontable {
  margin-top: 20px;
  margin-left: 20px;
}
.regSection {
  display: inline-block;
  // padding: 0 15px;
  line-height: 30px;
  font-weight: bold;
  font-size: 14px;
  margin: 0 auto;
  margin-left: 30%;
}
.headerWrap {
  height: 65px;
  margin: auto;
  padding-top: 30px;
  width: 980px;
}
.content {
  margin: auto;
  width: 980px;
}
.contentWrap {
  padding-top: 25px;
}

.colum_1 {
  margin-left: 10vh;
  float: left;
  display: inline;
  width: 80%;
}

.colum_2 {
  float: left;
  _display: inline;
  width: 326px;
  margin-left: 20px;
}
#h1 {
  font-size: 20px;
}
.introTxt {
  border-bottom: 1px solid #dddddd;
  color: #666;
  padding: 20px 0;
  margin: 0 auto;
  line-height: 20px;
  font-size: 17px;
  margin-top: 20px;
}
.blue-bg {
  width: 100%;
  height: 130px;
  background: #017fca;
  position: absolute;
  top: 0;
  left: 11px;
  z-index: 0;
} /*layout-bg*/

.main-navigation ul li a:hover {
  background: #4ab8ff !important;
}
.main-navigation ul li a:active {
  background: #4ab8ff !important;
}

/* main-navigation*/
.main-navigation {
  width: 100%;
  height: 50px;
  text-align: center;
  background: #1b4568;
}
.main-navigation .main-nav {
  width: 1170px;
  margin: 0px auto;
}
.main-navigation .menu li {
  height: 50px;
  font-size: 16px;
  text-align: center;
  width: 150px;
  list-style: none;
  display: inline-block;
  position: relative;
}
.main-navigation .menu li.nav-current {
  background: #4ab8ff;
}
.main-navigation .menu li:hover {
  background: #4ab8ff;
}
.main-navigation .menu li:active {
  background: #4ab8ff;
}
.main-navigation .menu li a {
  color: #ffffff;
  line-height: 3em;
  display: block;
  padding: 0px;
}
.main-navigation .menu li a:visited {
  background: #4ab8ff;
}
.main-navigation .menu li:hover > a {
  text-decoration: none;
}
.main-navigation .menu li ul {
  visibility: hidden;
  background: #4ab8ff;
  padding: 7px 0px;
  margin: 0;
  position: absolute;
  text-align: left;
  padding-left: 20px;
  padding-right: 0px;
  top: 120%;
  width: 160px;
  z-index: 999;
  border-top: none;
}
.main-navigation .menu li ul li {
  width: 200px;
  text-align: left;
  display: block;
  margin: 0;
}
.main-navigation .menu li ul li a {
  /* line-height: 2.5em;*/
  color: #505050;
}
.main-navigation .menu li ul:hover > a {
  color: #e67e22;
}
.main-navigation .menu li:hover ul {
  visibility: visible;
  opacity: 1; /* filter: alpha(opacity=100);*/
  top: 100%;
}
.main-navigation .row {
  margin: 0;
}

.el-form {
  width: 250px;
}
.el-input {
  width: 240px;
}

.el-container {
  /*设置内部填充为0，几个布局元素之间没有间距*/
  padding: 0px;
  /*外部间距也是如此设置*/
  margin: 0px;
  height: 100%;
  width: 100%;
}
/*
.el-header {
  display: flex;
  justify-content: space-between;
  padding-left: 0;
  background-color: #017FCA;
  align-items: center;
  color: #fff;
  //font-size: 20px;
  //标题的字体及大小
  font-size:40px;
  font-family: 华文行楷;
  height: 130px;
  > div {
    display: flex;
    align-items: center;
    img {
      width: 110px;
      height: 110px;
      border-radius: 50%;
    }
    span {
      margin-left: 15px;
    }
  }
}*/
.el-aside {
  // background-color: #333744;
  .el-menu {
    border-right: none;
  }
}
.el-main {
}

.camera {
  position: relative;
  width: 100%;
  height: 0;
  padding-bottom: 56.25%;
  background-color: black;
  overflow: hidden;
}

.camera video {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.camera canvas {
  position: absolute;
  top: 0;
  left: 0;
}

.controls {
  margin-top: 20px;
  text-align: center;
}
</style>