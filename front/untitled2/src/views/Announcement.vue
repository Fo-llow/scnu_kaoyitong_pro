<template>
  <div class="wp cl" v-if="seen">
    <el-container>
      <el-header>
        <Header>
        </Header>
      </el-header>
      <el-container>
        <el-main>
          <br>
          <br>
          <br>
          <br>
        <template>
          <el-page-header content="公告" @back="goBack" />
        </template>
            <div clas="main" style="margin-left: 140px;margin-top: 10px">
              <div class="content cl">
                <div class="title">
                  <h1>{{title}}</h1>
                  <p>
                    <span>{{time}}</span>
                    &nbsp;&nbsp;&nbsp;&nbsp;
                    <span>{{author}}</span>
                  </p>
                  <div class="detail">
                    <pre class="article">{{body}}</pre>
                  </div>

                  <div v-if="file!=null" >
                    附件：
                    <el-link  v-for="(f,key) in segmenteFile(file)" :key="key" target="_blank" :href="f.fsrc" >{{f.fname}}</el-link>
                  </div>

                </div>
              </div>

            </div>

        </el-main>
        <!--
       <div style="margin-top: 100px">
          <el-form :model="param" :rules="rules" ref="login" label-width="80px" style="margin-right: 10px">
            <el-form-item label="" prop="username">

              <el-input type="text" placeholder="请输入关键词" size="small"
                        v-model="param.find" style="width: 350px"></el-input>
              <el-button type="primary" size="small">
                <i class="el-icon-search"></i>
              </el-button>
            </el-form-item>
          </el-form>
       </div>
       -->
      </el-container>

    </el-container>

  </div>

</template>

<script>
import Header from "@/components/Header";
export default {
  //公告页面
  name: "Announcement",
  components: {Header},
  data(){
    return {
      title:"",
      body:"",
      author:"",
      time:"",
      file:"",
      seen:true,
      param:{
        find:""
      },
      rule:{

      }
    }

  },
  mounted(){
    this.GetAnnounce();
  },
  methods:{
    //返回上一页
    goBack(){
      this.$router.go(-1);
    },
    //分割文件(file是所有的字符串地址)
    segmenteFile(file){
      if( file == null )  return null       //注意加上，否则文件为空的话就会报错不显示
      let array = file.split('|');
      //console.log(array)
      //console.log(array.length)
      let list = [];
      for(let i = 0 ; i < array.length ; i++ ){
        let names = array[i].split('_');
        list.push({'fname':names[1],'fsrc': this.$store.state.baseurl +array[i] + '?token='+this.$cookies.get("token") })
      }
      //console.log(list)
      return list;
    },

    //获取指定公告
    GetAnnounce() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: '加载公告中……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
      });
      //调用获取get后面的参数
      let title  = this.$route.query.title
      let time = this.$route.query.time
      let token = this.$route.query.token
      //console.log(title+" "+time)

      //请求
      let params = new URLSearchParams();
      params.append('time',time);
      params.append('token',token);
      params.append('title',title);
      this.axios.post("getannounce",
          params
      ).then((response) => {
        load.close()
        //console.log(response.data)
        if (response.data['msg'] == "ok") {
          this.title = response.data["title"]
          this.body = response.data["body"]
          this.author = response.data["author"]
          this.time = response.data["time"]
          this.file = response.data["file"]
        }
      })
    },

}
}
</script>

<style scoped>
.wp {
  margin: 0 auto;
  width: 1240px;
}
.cl {
  zoom: 1;
}
.main {
  float: right;
  background-color: #e1dfdb;
  background-image: none;
  border-top: 3px solid #1F76BD;
  border-top-color: rgb(31, 118, 189);
  box-shadow: 5px 5px 10px rgba(201, 201, 201, 0.40);
}
.main .content {
  width: 100%;
  padding: 0 30px;
  box-sizing: border-box;
  margin: 0 auto;
}
.main .content {
  width: 950px;
  margin: 0 45px;
}
.cl {
  zoom: 1;
}
.title {
  font-size: 23px;
  border-bottom-color: rgb(211, 208, 204);
}
.title {
  margin-top: 20px;
  padding-left: 15px;
  padding-bottom: 20px;
  border-bottom: 1px solid #EBECED;
  border-bottom-color: rgb(235, 236, 237);
}
pre {
  white-space: pre-wrap;
  word-wrap: break-word;
}
.title p {
  line-height: 15px;
  color: #9FA3A6;
  font-size: 17px;
}
.title span {
  padding: 0px 5px;
  font-size: 14px;
}
* {
  word-wrap: break-word;
}
* {
  scrollbar-color: #a5a19a #d6d4d0;
}
.detail, .detail .article {
  width: 100%;
}
.detail {
  font-size: 17px;
  width: 900px;
  margin-top: 40px;
  padding-bottom: 15px;
}
.l {
  float: left;
}
* {
  word-wrap: break-word;
}
* {
  scrollbar-color: #a5a19a #d6d4d0;
}
.article {
  width: 900px;
  margin: 0 auto;
  line-height: 30px;
  overflow: hidden;
  font-family: 宋体,SimSun; font-size: 16px;
}
</style>