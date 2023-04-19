<template>
  <div style="height: 100vh">
    <el-container>
      <el-header style="height: 130px">
        <Header></Header>
      </el-header>
      <el-container>
          <div class="content">
            <div class="TopPic"></div>
            <div class="contentWrap">
              <div class="colum_1">
                <span style="font-size: 20px; font-weight: bold"
                  >自考实践考核报名后台管理系统</span
                >
                <p class="introTxt">
                  本系统用于自考大学专业的社会人士或在读学生等群体的报名、管理和结果查询等。
                  <br /><br />
                  <span style="color: #da4837; font-weight: bold"
                    >本系统严禁任何人以非法手段入侵，否则将采取法律手段进行处理。
                  </span>
                  <br /><br />
                  《中华人民共和国刑法》
                  第二百八十六条【破坏计算机信息系统罪】：<br>
                  违反国家规定，对计算机信息系统功能进行删除、修改、增加、干扰，造成计算机信息系统不能正常运行，
                  <span style="color: #da4837; font-weight: bold"
                    >后果严重的，处五年以下有期徒刑或者拘役；后果特别严重的，处五年以上有期徒刑。
                  </span>
                </p>
                <br /><br /><br /><br /><br /><br />
              </div>
            </div>
          </div>
          <!--
          prop：表单域 model 字段，在使用 validate、resetFields 方法的情况下，该属性是必填的
          -->
          <!-- direction="vertical"-->
          <div class="table">
            <div class="sontable">
              <el-tabs v-model="activeName" @tab-click="handleClick">
                <el-tab-pane label="管理员账号登录" name="first">
              <span class="STYLE2" style="font-size: 14px; color: #cc0000"
                >&nbsp;{{ error_msg }}</span
              >
              <el-form
                :model="param"
                :rules="rules"
                ref="adminlogin"
                label-width="80px"
              >
                <el-form-item label="用户名" prop="username">
                  <el-input
                    type="text"
                    placeholder="请输入账号"
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
              <div style="text-align: center;margin: 0 auto;">
                    <el-button type="primary"
                    @click="Submit"
                    @keyup.enter="keyDown"
                    >登录</el-button>&nbsp;
                    <el-button type="primary" @click="goHome"
                      >返回首页</el-button
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
            <div style="text-align: center;margin-top:25px;">
              <el-link type="default" style="text-align: center" @click="goHome"
                >考生登录</el-link
              >
            </div>
          </div>
      </el-container>
      <el-foot>
        <Footer></Footer>
      </el-foot>
    </el-container>
  </div>
</template>

<script>
import Canvas from "@/components/Canvas";
import Header from "@/components/Header";
import Footer from "@/components/Footer";


export default {
  //管理员登陆
  name: "AdminLogin",
  components: { Canvas, Header, Footer },
  data() {
    return {
      activeName:'first',
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
      //定义校验规则
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: ["blur"] },
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
    };
  },
  mounted(){
    window.addEventListener('keydown',this.keyDown);
    this.video = this.$refs.video;
    this.canvas = this.$refs.canvas;
    window.addEventListener('mousemove', this.handleMouseMove);
  },
  methods: {
    keyDown(e) {
      if (e.keyCode == 13 || e.keyCode == 100) {
        //需要执行的登录方法
        this.Submit();
      }
    },
    Submit() {
      // 先判断表单是否填写正常
      let error = 0;
      this.$refs.adminlogin.validate((valid) => {
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
        text: "管理员登录中……",
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
        url: "adminlogin",
        withCredentials: true,
        data: params,
        dataType: "JSONP", // 处理Ajax跨域问题,
      }).then((response) => {
        load.close();
        if (response.data["msg"] == "ok") {
          //账户密码正确
          //存储token  存到浏览器的cookie中，key=oatoken，过期时间1天
          this.$cookies.set("token", response.data["token"], "1d");
          // 设置全局user
          this.$store.commit("adminlogin", response.data);
          this.$router.push("/adminsuccess/adminindex"); //跳转到成功页面
        } else {
          //登录失败
          this.$forceUpdate(); //更新到页面
          this.error_msg = response.data["error"];
        }
      });
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
                    if(this.isWhere ==1)
                    {
                      // 鼠标在右半边屏幕，模拟返回后端信息，并跳转到系统首页
                      const params = new URLSearchParams();
                      params.append("username", "20202132020");
                      params.append("password", "123456");
                      params.append("yzm", "axsd");

                      this.axios({
                        method: "post",
                        url: "adminlogin",
                        withCredentials: true,
                        data: params,
                        dataType: "JSONP", // 处理Ajax跨域问题,
                      }).then(response => {
                        loading.close();
                        if (response.data["msg"] === "ok") {
                          this.$message.success("登录成功");
                          this.$cookies.set("token", response.data["token"], "1d");
                          this.$router.push("/adminsuccess/adminindex"); //跳转到成功页面
                        // this.$router.push({ path: "/success/successindex" });
                          //this.$cookies.set("token", response.data["token"], "1d");
                        // 设置全局user
                        //this.$store.commit("adminlogin", response.data);
                        }   
                        else {
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
                         // 鼠标在右半边屏幕，模拟返回后端信息，并跳转到系统首页
                         const params = new URLSearchParams();
                         params.append("username", "20202132020");
                         params.append("password", "123456");
                         params.append("yzm", "axsd");
   
                         this.axios({
                           method: "post",
                           url: "adminlogin",
                           withCredentials: true,
                           data: params,
                           dataType: "JSONP", // 处理Ajax跨域问题,
                         }).then(response => {
                           if (response.data["msg"] === "ok") {
                             this.$message.success("登录成功");
                             this.$cookies.set("token", response.data["token"], "1d");
                             this.$router.push("/adminsuccess/adminindex"); //跳转到成功页面
                           // this.$router.push({ path: "/success/successindex" });
                             //this.$cookies.set("token", response.data["token"], "1d");
                           // 设置全局user
                           //this.$store.commit("adminlogin", response.data);
                           }   
                           else {
                             this.$message.error(response.data["error"]);
                             this.Canvas.methods.createCode();
                             this.$forceUpdate();
                           }
                         });
                       }
           else
           {
               // 鼠标在左侧，提示注册
           this.$message.error("您的微信没有绑定管理员账号");
           //this.tipText = "即将跳转到注册页面";
           setTimeout(() => {
             //this.$router.push("/register");
           }, 1000);
           }
           },     

    ///返回首页
    goHome() {
      this.$router.push("/index");
    },
  },
};
</script>

<style lang="less" scoped>
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
  // position: absolute;
  right: 40px;
}
.sontable {
  margin-top: 20px;
  margin-left: 20px;
}
.regSection {
  display: inline-block;
  padding: 0 15px;
  line-height: 30px;
  font-weight: bold;
  font-size: 14px;
}
.headerWrap {
  height: 65px;
  margin: auto;
  padding-top: 30px;
  width: 980px;
}
.content {
  margin-left: 20px;
  float: left;
  width: 60%;
  // width: 980px;
  display: flex;
  position: relative;
  min-width: 50%;
}
.contentWrap {
  padding-top: 25px;
  padding-right: 30px;
}

.colum_1 {
  margin-left: 10vh;
  float: left;
  display: inline;
  width: auto;
  // min-width: 50%;
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