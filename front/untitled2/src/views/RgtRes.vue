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
                <h2 style="color: rgb(224, 156, 94); --darkreader-inline-color: #ffbf75;text-align: center;">
                  {{rgtres}}
                </h2>
                <br>
                <div class="regbtn greyregbtn">
                  <el-button type="info" @click="goHome" >返回</el-button>

                </div>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div style="margin-left: 10px;margin-top: 100px">
              <el-card>
                <span>欢迎注册</span>
                <p>您好！欢迎您注册”考易通“自学考试报名系统。<br/>
                  注册为本系统的用户，您可以根据注册时填写的用户名和密码登录本系统，可以进行本科报名等。<br/>
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

export default {
  name: "RgtRes",
  components: { Header, Footer},
  data(){
    return {
      rgtres:"注册成功"
    }
  },
  methods:{
    ///返回首页
    goHome(){
      this.$router.push("/index");
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
  margin-top:250px;
  margin-left:150px;
span{
  font-weight: bolder;
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