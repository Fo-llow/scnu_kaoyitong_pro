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
                <h1>找回用户名/密码</h1>
                <el-divider></el-divider>
                <el-form :model="param" :rules="rules" ref="forget" label-width="80px">
                  <el-form-item label="用户名" prop="username">
                    <el-input type="text" placeholder="输入注册时的考生准考证号。" size="small"
                              prefix-icon="el-icon-user"
                              v-model="param.username"></el-input>
                  </el-form-item>
                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                  &nbsp;&nbsp;&nbsp;<el-button type="primary" @click="Submit">找回密码</el-button>
                </el-form>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div style="margin-left: 10px;margin-top: 100px">
              <el-card>
                <span>注意</span>
                <p>我们会将密码重置的链接发送到您的注册时的电子邮箱
                  <br/>注意：根据准考证号仅能找回本招生年度注册的密码
                  <br/>一天之内发送重置密码链接不能超过5次. </p>
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
import ElementUI from "element-ui";
export default {
  name: "Forget",
  components: {Header,Footer},
  data(){
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
          { required:true,message:"请输入准考证号",trigger:["blur","change"]},
          { pattern: /^[\u4e00-\u9fa5a-zA-Z0-9]+$/,message: '用户名不能出现特殊字符',trigger:["blur","change"] },
          { pattern: new RegExp(/^\S*$/, 'g'), message: '用户名不能出现空格',trigger:["blur","change"] }
        ],
      }
    }

  },
  methods:{
    Submit(){
      let error = 0;
      this.$refs.forget.validate((valid) => {
        if (!valid) {
          //console.log("As")
          this.$message({
            type: 'error',
            message: '请先正确填写用户名'
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
        text: '正在向账户注册时的邮箱发送密码重置码……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      let params = new URLSearchParams();
      params.append('username', this.param.username);

      this.axios({
        method: 'post',
        url:"forgetpass",
        withCredentials:true,
        data:params,
        dataType:'JSONP',  // 处理Ajax跨域问题,
      }).then((response) => {
        //console.log(response.data)
        //console.log(response.data['msg'])
        load.close()
        if( response.data['msg'] == "ok"){
          // 设置全局user
          this.$store.commit('forget',this.param.username)
          ElementUI.Message({
            message: '找回成功，请查看邮箱的密码重置码',
            type: 'success'
          });

          this.$router.push("/forgetpass")

        }else{          //注册失败
          //this.error_msg = response.data['error']
          ElementUI.Message({
            message: '找回失败，' + response.data['error']+",5秒后自动返回首页",
            type: 'error'
          });

          setTimeout(
              //这里写5秒后的
              this.goHome
              , 5000);

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
  width: 270px;
  height: 320px;
  margin-top:100px;
  margin-left:150px;
  span{
    font-weight: bolder;
    font-size: 17px;
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