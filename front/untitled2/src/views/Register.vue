<template>
  <div style="height: 100vh">
    <el-container>
      <el-header>
       <Header></Header>
      </el-header>
      <el-container>
        <el-main>
          <!--
          prop：表单域 model 字段，在使用 validate、resetFields 方法的情况下，该属性是必填的
          -->
          <!-- direction="vertical"-->

          <el-col :span="12">
            <div class="table">
              <div class="sontable">
                <h1>新用户注册</h1>
                <el-divider></el-divider>
                <el-form :model="param" :rules="rules" ref="register" label-width="80px">
                  <el-form-item label="准考证号" prop="username">
                    <el-input type="text" placeholder="可作为登录用户名" size="small"
                              prefix-icon="el-icon-user"
                              v-model="param.username"></el-input>
                  </el-form-item>
                  <el-form-item label="电子邮箱" prop="email">
                    <el-input type="text" placeholder="用于密码找回等" size="small"
                              prefix-icon="el-icon-message"
                              v-model="param.email"></el-input>
                  </el-form-item>
                  <el-form-item label="姓名" prop="name">
                    <el-input type="text" placeholder="请填写真实姓名" size="small"
                              prefix-icon="el-icon-s-ticket"
                              v-model="param.name"></el-input>
                  </el-form-item>

                  <el-form-item label="性别" prop="sex">
                    <div>
                      <el-radio v-model="param.sex" label="1" size="large">男</el-radio>
                      <el-radio v-model="param.sex" label="2" size="large">女</el-radio>
                    </div>
                  </el-form-item>
                  <el-form-item label="密码" prop="password">
                    <el-input type="password" placeholder="6位以上英文字母或数字" size="small" show-password prefix-icon="el-icon-lock"
                              v-model="param.password"></el-input>
                  </el-form-item>
                  <el-form-item label="确认密码" prop="qrpassword">
                    <el-input type="password" placeholder="请再次确认密码。" size="small" show-password prefix-icon="el-icon-lock"
                              v-model="param.qrpassword"></el-input>
                  </el-form-item>
                  <el-form-item label="验证码" prop="yzm">
                    <el-col :span="12" >
                      <el-input type="text" placeholder="验证码" prefix-icon="el-icon-picture" style="width: 100px"
                                v-model="param.yzm"></el-input>
                    </el-col>
                    <el-col :span="12">
                      <Canvas></Canvas>
                    </el-col>
                  </el-form-item>
                  &nbsp;&nbsp;<el-button type="danger" @click="Submit('register')" style="margin-left: 10px">注册新用户</el-button>
                  &nbsp;<el-button type="primary" @click="goHome()" style="margin-left: 10px">返回首页</el-button>
                </el-form>

              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div style="margin-left: 10px;margin-top: 100px">
              <el-card>
                <span>欢迎注册</span>
                <p>您好！欢迎您注册”考易通“自学考试报名系统！<br><br>
                  注册为本系统的用户，您可以根据注册时填写的用户名和密码登录本系统，可以进行本科报名等。<br><br>
                  提交前请牢记您的用户名和密码，它将是您登录系统唯一的依据。谢谢！ </p>
              </el-card>
            </div>
          </el-col>
        </el-main>
      </el-container>
      <el-foot>
        <Footer></Footer>
      </el-foot>
    </el-container>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ElementUI from 'element-ui';
import Canvas from "@/components/Canvas";



export default {

name: "Register",
  components: {Canvas, Header,Footer},
  data(){
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入一次密码'))
        // password 是表单上绑定的字段
      } else if (value !== this.param.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      param:{
        username:"",
        email:"",
        name:"",
        sex:"",
        password:"",
        qrpassword:"",
        ymz:""
      },
      //定义校验规则
      rules:{
        username:[
          {required:true,message:"请输入准考证号",trigger:"blur"},
          { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,message: '用户名不能出现特殊字符',trigger:"blur" },
          { pattern: new RegExp(/^\S*$/, 'g'), message: '用户名不能出现空格',trigger:"blur" }
        ],
        email:[
          {type:"email",message:"邮箱格式错误",trigger:"blur"},
          {required:true,message:"请输入电子邮箱",trigger:'blur' }
        ],
        name:[
          {required:true,message:"请输入姓名",trigger:"blur"},
          { pattern: /^[\u4E00-\u9FA5]+$/i,message: '请输入中文姓名',trigger:["blur","change"] },
        ],
        sex:[
          {required:true,message:"请选择性别",trigger:"blur"}
        ],
        password:[
          {required:true,message:"请输入密码",trigger:"blur"},
          {min:6,max:30,message:"密码长度必须在6到30之间",trigger:"blur"}
        ],
        qrpassword:[
         // {required:true,message:"请再输入一次密码",trigger:"blur"},
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
        yzm:[
          {required:true,message:"请输入验证码",trigger:"blur"},
          { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,message: '验证码不能出现特殊字符',trigger:"blur" },
          { min: 4, max: 4, message: '请输入4位验证码', trigger: 'blur' }
        ],
      }
    }

  },

  methods:{
    Submit(){
      let error = 0;
      this.$refs.register.validate((valid) => {
        if (!valid) {
          //console.log("As")
          this.$message({
            type: 'error',
            message: '请先正确填写注册信息'
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if( error == 1 )  return;

      //开启loading
      const load = this.$loading({
        lock: true,
        text: '注册中……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      let params = new URLSearchParams();
      params.append('ticket', this.param.username);
      params.append( 'userpass', this.param.password);
      params.append( 'email', this.param.email);
      // 1表示男，2表示女
      params.append( 'xb', this.param.sex);
      params.append('name', this.param.name);
      params.append('yzm', this.param.yzm);

      this.axios({
        method: 'post',
        url:"register",
        withCredentials:true,
        data:params,
        dataType:'JSONP',  // 处理Ajax跨域问题,
      }).then((response) => {
        load.close()
        //console.log(response.data)
        //console.log(response.data['msg'])
        if( response.data['msg'] == "ok"){        //账户密码正确

          ElementUI.Message({
            message: '注册成功，5秒后返回首页',
            type: 'success'
          });

          setTimeout(
            //这里写5秒后的
            this.$router.push("/login")         //跳转到成功页面
          , 5000);
        }else{          //注册失败
          //this.error_msg = response.data['error']
          ElementUI.Message({
            message: '注册失败，'+ response.data['error'],
            type: 'error'
          });
        }
      })
    },

    ///返回首页
    goHome(){
      this.$router.push("/");
    },
    //跳转注册界面
    goRegister(){
      this.$router.push("/register");
    },
    //跳转忘记密码器页面
    goForget(){
      this.$router.push("/");
    },
  }
}
</script>

<style lang="less" scoped>
/*表单那块——注册*/
.table{
  margin-top:20px;
  margin-left: 20px;
  width: 600px;
  font-size:14px;
  line-height:34px;
}
.sontable{
  margin-top:100px;
  margin-left:300px;
}
.el-card{
  width: 380px;
  height: auto;
  margin-top:250px;
  margin-left:150px;
 span{
    font-weight: bolder;
    font-size:17px;
 }
  p{
    margin-top:20px;
    font-size:14px;
    color:#424242;
    line-height:34px;
  }
}
.el-input{
  width:240px;
}
</style>