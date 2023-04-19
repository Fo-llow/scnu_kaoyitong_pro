<template>

  <div>
      <i class = "el-icon-s-ticket"></i>
      <el-col :span="12"><div>ads</div></el-col>
      <el-col :span="12"><div>adsa</div></el-col>
  </div>

</template>

<script>
export default {
  name: "CS" ,
  data(){
    return {
      param:{
        username:"",
        password:"",
        ymz:""
      },
      //定义校验规则
      rules:{
        username:[
          {required:true,message:"请输入准考证号",trigger:"blur"}
        ],
        password:[
          {required:true,message:"请输入密码",trigger:"blur"}
        ],
        yzm:[
          {required:true,message:"请输入验证码",trigger:"blur"}
        ],
      }
    }

  },
  methods:{
    Submit(){

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

<style lang="less"  scoped>

  .el-container{
    /*设置内部填充为0，几个布局元素之间没有间距*/
    padding: 0px;
    /*外部间距也是如此设置*/
    margin: 0px;
    height:100%;
    width:100%;

  }
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
  }
  .el-aside {
    background-color: #333744;
  .el-menu {
    border-right: none;
  }
  }
  .el-main {
    background-color: #eaedf1;
  }
</style>