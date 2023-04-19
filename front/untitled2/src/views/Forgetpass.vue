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

                <h1 v-if="which==1">找回用户名/密码</h1>
                <h1 v-if="which==2">修改密码</h1>
                <el-divider></el-divider>

                <div>
                  <el-form :model="param" :rules="rules" ref="forgetpass" label-width="80px">

                    <el-form-item label="重置码" prop="eyzm">
                      <el-input type="text" placeholder="注意重置码区分大小写" size="small"
                                prefix-icon="el-icon-picture"
                                v-model="param.eyzm"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="password">
                      <el-input type="password" placeholder="输入6位以上的密码。" size="small"
                                show-password prefix-icon="el-icon-lock"
                                v-model="param.password"></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" prop="qrpassword">
                      <el-input type="password" placeholder="再输入一遍密码。" size="small"
                                show-password prefix-icon="el-icon-lock"
                                v-model="param.qrpassword"></el-input>
                    </el-form-item>
                    &nbsp;&nbsp;&nbsp;
                    <el-button type="primary" @click="Submit">确认修改</el-button>
                    &nbsp;
                    <el-button type="primary" @click="goBack">返回上页</el-button>
                  </el-form>
                </div>


              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div style="margin-left: 10px;margin-top: 100px">
              <el-card width="300px">
                <span>注意</span>
                <p>我们会将密码重置码发送到您的注册时的电子邮箱
                  <br/>注意：根据用户名仅能找回本招生年度注册的密码
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
  name: "Forgetpass",
  components: { Header, Footer},
  data(){
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
        // password 是表单上绑定的字段
      } else if (value !== this.param.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      // 显示的类型
      which:"1",
      type:"",
      username:"",
      // 表单传值
      param:{
        eyzm:"",
        password:"",
        qrpassword:"",
      },
      //定义校验规则
      rules:{
        eyzm:[
          {required:true,message:"请输入邮箱收到的验证码",trigger:"blur"}
        ],
        password:[
          {required:true,message:"请输入密码",trigger:"blur"},
          {min:6,max:30,message:"密码长度必须在6到30之间",trigger:"blur"}
        ],
        qrpassword:[
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
      }
    }
  },
  mounted() {
    // 判断是哪个界面
    //调用获取get后面的参数
    let which  = this.$route.query.which
    if( which == "2" ){
      this.which = "2";
    }else{
      this.which = "1";
    }
  },
  methods:{
    // 返回上页
    goBack(){
      this.$router.go(-1);
    },
    Submit(){
      let error = 0;
      this.$refs.forgetpass.validate((valid) => {
        if (!valid) {
          //console.log("As")
          this.$message({
            type: 'error',
            message: '请先正确填写修改信息'
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
        text: '修改中……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      //console.log(this.$store.state.username)
      //解决axios提交到后端为null的情况
      let params = new URLSearchParams();
      params.append('username',this.$store.state.username);
      params.append('password', this.param.password);
      params.append('eyzm', this.param.eyzm);

      this.axios({
        method: 'post',
        url:"modifypass",
        withCredentials:true,
        data:params,
        dataType:'JSONP',  // 处理Ajax跨域问题,
      }).then((response) => {
        /*
        * {'msg':'ok'}
        * {'msg':'fail','error':'密码重置码错误'}
        */
        //console.log(response.data)
        //console.log(response.data['msg'])
        load.close()
        if( response.data['msg'] == "ok"){        //账户密码正确
          // 设置全局user
          this.$store.commit('login', response.data)
          if( this.which === "1" ){
            ElementUI.Message({
              message: '密码重置成功，5秒后返回首页',
              type: 'success'
            });
            setTimeout(
                //这里写5秒后的
                this.$router.push("/login")         //跳转到登录页面
                , 5000);
          }else{
            ElementUI.Message({
              message: '密码重置成功，5秒后返回上页',
              type: 'success'
            });
            setTimeout(
                //这里写5秒后的
                this.$router.go(-1)         //跳转到登录成功页面
                , 5000);
          }
        }else{      //修改失败
          ElementUI.Message({
            message: '密码重置失败，'+response.data['error']+"",
            type: 'error'
          });
        }

      })
    }
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