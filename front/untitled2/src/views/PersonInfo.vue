<template>
  <div style="height: 100vh">
    <template>
      <el-descriptions title="个人信息"
                       :column="2"
                       size="large"
                       style="width: 50%;margin: 0 auto;"
                       border>
        <el-descriptions-item label="用户名">{{infoData.username}}</el-descriptions-item>
        <el-descriptions-item label="姓名">{{infoData.name}}</el-descriptions-item>
        <el-descriptions-item label="性别">{{infoData.sex}}</el-descriptions-item>
        <el-descriptions-item label="身份证号">{{infoData.sfzh}}</el-descriptions-item>
        <el-descriptions-item label="手机号码">{{infoData.tele}}</el-descriptions-item>
        <el-descriptions-item label="电子邮箱">{{infoData.email}}</el-descriptions-item>
      </el-descriptions>
      <br />
      <div>
      <el-button type="primary" style="margin-left:40%;text-align: center;" @click="editInfo">完善/修改个人信息</el-button>
    </div>
    </template>

    <!-- 修改的对话框 -->
    <el-dialog :visible="dialogFormVisible" title="完善/修改个人信息" :close-on-click-modal="true"
               @close="dialogFormVisible=false"
                :append-to-body="true">
      <el-form :model="form"  :rules="rules" ref="modify" label-width="80px">
        <el-form-item label="准考证号" prop="username" >
          <el-input type="text" placeholder="可作为登录用户名，往届考生请重新注册" size="small"
                    prefix-icon="el-icon-user"
                    v-model="form.username" disabled></el-input>
        </el-form-item>
        <el-form-item label="姓名" prop="name">
          <el-input type="text" placeholder="用于密码找回等，非常重要" size="small"
                    prefix-icon="el-icon-s-ticket"
                    v-model="form.name"></el-input>
        </el-form-item>


        <el-form-item label="性别" prop="sex">
          <div>
            <el-radio v-model="form.sex" label="1" size="large">男</el-radio>
            <el-radio v-model="form.sex" label="2" size="large">女</el-radio>
          </div>
        </el-form-item>

        <el-form-item label="电子邮箱" prop="email">
          <el-input type="text" placeholder="用于密码找回等，非常重要" size="small"
                    prefix-icon="el-icon-message"
                    v-model="form.email"></el-input>
        </el-form-item>

        <el-form-item label="手机号码" prop="tele">
          <el-input type="text" placeholder="手机号码选填，用于报名" size="small"
                    prefix-icon="el-icon-phone"
                    v-model="form.tele"></el-input>
        </el-form-item>

        <el-form-item label="身份证号" prop="sfzh">
          <el-input type="text" placeholder="身份证号选填，用于报名" size="small"
                    prefix-icon="el-icon-postcard"
                    v-model="form.sfzh"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogFormVisible = false">取消</el-button>
            <el-button type="primary" @click="modifyInfo">确认修改</el-button>
          </span>
      </template>
    </el-dialog>
  </div>
</template>
<script>
//个人信息
export default {
  name: "PersonInfo",

  data(){
    var validateIdNo = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入身份证号'))
        return
      }
      var format = /^(([1][1-5])|([2][1-3])|([3][1-7])|([4][1-6])|([5][0-4])|([6][1-5])|([7][1])|([8][1-2]))\d{4}(([1][9]\d{2})|([2]\d{3}))(([0][1-9])|([1][0-2]))(([0][1-9])|([1-2][0-9])|([3][0-1]))\d{3}[0-9xX]$/;
      //号码规则校验
      if (!format.test(value)) {
        callback(new Error('请输入正确身份证号'));
      }
      //区位码校验
      //出生年月日校验  前正则限制起始年份为1900;
      var year = value.substr(6, 4),//身份证年
          month = value.substr(10, 2),//身份证月
          date = value.substr(12, 2),//身份证日
          time = Date.parse(month + '-' + date + '-' + year),//身份证日期时间戳date
          now_time = Date.parse(new Date()),//当前时间戳
          dates = (new Date(year, month, 0)).getDate();//身份证当月天数
      if (time > now_time || date > dates) {
        callback(new Error('请输入正确身份证号'));
      }
      //校验码判断
      var c = new Array(7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);  //系数
      var b = new Array('1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'); //校验码对照表
      var id_array = value.split("");
      var sum = 0;
      for (var k = 0; k < 17; k++) {
        sum += parseInt(id_array[k]) * parseInt(c[k]);
      }
      if (id_array[17].toUpperCase() != b[sum % 11].toUpperCase()) {
        callback(new Error('请输入正确身份证号'));
      }
      callback();
    }
    return {
      type:"",
      //个人信息的数据
      infoData:{
        username:"",
        name:"",
        sex:"",
        sfzh:"",
        tele:"",
        email:"",
      },
      //是否显示对话框
      dialogFormVisible:false,
      // 对话框里面的数据
      form:{},
      //修改规则
      rules:{
        name:[
          {required:true,message:"请输入姓名",trigger:"blur"},
          { pattern: /^[\u4E00-\u9FA5]+$/i,message: '请输入中文姓名',trigger:["blur","change"] },
        ],
        sex:[
          {required:true,message:"请选择性别",trigger:"blur"}
        ],
        sfzh:[
          {required:true,validator: validateIdNo,trigger:"blur"}
        ],
        tele:[
          { pattern: new RegExp(/^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/,'g'),
            message: '手机号码格式不正确',trigger:"change"}
        ],
        email:[
          {type:"email",message:"邮箱格式错误",trigger:"blur"},
          {required:true,message:"请输入电子邮箱",trigger:["blur","change"] }
        ],
      }
    }
  },
  created() {
    // 进行身份验证（可以解决刷新和直接进入页面问题）
    this.$public.tokenlogin2(this.$cookies,this.axios,this.$store,this.$router,this.$message)
  },
  mounted() {
    this.getInfo()
  },
  methods:{
    //获取个人信息
    getInfo(){
      //开启loading
      const load = this.$loading({
        lock: true,
        text: '加载个人信息中……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });

      let params = new URLSearchParams()
      params.append("token",this.$cookies.get("token"))
      this.axios.post("personinfo",
          params
      ).then((response) => {
        load.close()
        //console.log(response.data)
        //console.log(response.data['msg'])

        if( response.data['msg'] == "ok"){
          this.infoData.name = response.data['name']
          this.infoData.sex = response.data['sex']
          if( response.data['sex'] == "1" ){
            this.infoData.sex = "男"
          }else{
            this.infoData.sex = "女"
          }
          this.infoData.username = response.data['ticket']
          this.infoData.tele = response.data['tele']
          this.infoData.email = response.data['email']
          this.infoData.sfzh = response.data['sfzh']
          this.$forceUpdate()         //解决数据渲染延迟
        }
      })
    },
    //完善个人信息
    editInfo(){

      this.dialogFormVisible = true;
      if( this.infoData.sex == "男" ){
        this.infoData.sex = "1"
      }else{
        this.infoData.sex = "2"
      }
      this.form = this.infoData
      this.$forceUpdate()         //解决数据渲染延迟
    },
    //修改个人信息
    modifyInfo(){
      this.dialogFormVisible = false;
      if( this.infoData.username == this.form.username
          &&
          this.infoData.name == this.form.name
          &&
          this.infoData.sfzh == this.form.sfzh
          &&
          this.infoData.tele == this.form.tele
          &&
          this.infoData.email == this.form.email
          &&
          (( this.infoData.sex == "男" && this.form.sex == "1") || ( this.infoData.sex == "女" && this.form.sex == "2"))
      ){
        //没有什么变化
        this.$message({
          type: 'error',
          message: '修改失败，内容与之前一致'
        });
        return;
      }
      //console.log(index+" "+this.codeData[index]['code'])
      this.$confirm('此操作将修改该个人信息, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        //开启loading
        const load = this.$loading({
          lock: true,
          text: '修改中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });

        let params = new URLSearchParams()
        params.append("token",this.$cookies.get("token"))
        params.append('username',this.form.username);
        params.append('sex',this.form.sex);
        params.append('name',this.form.name);
        params.append('tele',this.form.tele);
        params.append('email',this.form.email);
        params.append('sfzh',this.form.sfzh);

        this.axios.post("modifyinfo",
            params
        ).then((response) => {
          load.close()
          //console.log(response.data)
          if (response.data['msg'] == "ok") {

            this.getInfo()
            this.$forceUpdate()         //解决数据渲染延迟
          }
        })
        this.$message({
          type: 'success',
          message: '修改成功!'
        });
        this.getInfo()
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消修改'
        });
      });
    }
  }
}
</script>

<style scoped>
.my-label {
  background-color:#3b6496;
  color:#3b6496;
}
</style>