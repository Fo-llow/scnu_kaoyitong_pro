<template>
  <div class="wp cl" v-if="seen">
    <el-container>
      <el-header>
        <Header>
        </Header>
      </el-header>
      <el-container>

        <el-main style="margin-top: 80px">
          <template>
            <el-page-header content="全部公告" @back="goBack" />
          </template>
          <el-form :model="param"  ref="find" label-width="80px" style="margin-top: 10px;margin: 0 auto;text-align: center;">
            <el-form-item label="" prop="find">

              <el-input type="text" placeholder="请输入关键词" size="small"
                        v-model="param.find" style="width: 350px"></el-input>
              <el-button type="primary" size="small" @click="search">
                <i class="el-icon-search"></i>
              </el-button>
            </el-form-item>
          </el-form>

        <div style="margin-left: 140px;">
              <el-table :data="tableData" style="width: 80%;margin: 0 auto;" id="table" >
                <el-table-column prop="time" label="发布时间" width="200" />
                <el-table-column prop="title" label="公告标题" width="500" />
                <el-table-column prop="operation" label="操作" width="80">
                  <template  #default="scope">
                    <!--
                    <el-button type="primary" circle >
                      <i class="el-icon-edit"></i>
                    </el-button>
                    -->
                    <el-tooltip
                        effect="dark"
                        content="查看详情"
                        placement="top-start"
                    >
                      <el-button type="success" circle @click="LookAnnounce(scope.$index)">
                        <i class="el-icon-view"></i>
                      </el-button>
                    </el-tooltip>
                  </template>
                </el-table-column>
              </el-table>
              <br />
              <template style="width: 100%">
                <div style="display:block;text-align: center;">

                  <el-pagination background layout="prev, pager, next" :total="pagenum"
                                 :hide-on-single-page="false" :current-page="page" :page-size="5"
                                 @current-change="showPage" >
                  </el-pagination>
                </div>
              </template>
        </div>
        </el-main>

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
      type:1,
      title:"",
      body:"",
      author:"",
      time:"",
      seen:true,
      param:{
        find:""
      },
      page:1,
      pagenum:0,
      tableData:[]
    }

  },
  mounted(){
    //this.GetAnnounce();
    this.showPage(1);
    this.calcPage()
  },
  methods:{
    search(){
      if( this.param.find == "" ){
        this.type = 1
        this.$message({
          message: '查找内容不能为空',
          type: 'error'
        });
        return;
      }

      this.type = 2
      const load = this.$loading({
        lock: true,
        text: '查找公告信息中……',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
        target:'#table'
      });
      let params = new URLSearchParams()
      params.append("token",this.$cookies.get("token"))
      params.append("page","1")
      params.append("find",this.param.find)
      this.axios.post("searchannounce",params
      ).then((response) => {
        load.close()
        //console.log(response.data)
        if (response.data['msg'] == "ok") {
          this.tableData = []
          this.calcPage()
          for( let i = 0 ; i < response.data["announce"].length ;  i++ ) {
            this.tableData.push({
              title: response.data["announce"][i]['title'],
              time: response.data["announce"][i]['time'],
              author: response.data["announce"][i]['author'],
              body: response.data["announce"][i]['body'].slice(0, 80) + "……",      //显示前100个字符
              file: response.data["announce"][i]['file'],
              isemail: response.data["announce"][i]['isemail']
            })
          }

        }
      })
    },
    //查看公告
    LookAnnounce(index){
      let time = this.tableData[index]['time']
      let title = this.tableData[index]['title']
      this.$router.push("/announcement?title="+title+"&time="+time+"&token="+this.$cookies.get("token"));
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
    //计算总页数
    calcPage(){
      if( this.type == 1 ){
        let params = new URLSearchParams()
        params.append("token",this.$cookies.get("token"))
        this.axios.post("announcenum",params
        ).then((response) => {
          //console.log(response.data)
          if (response.data['msg'] == "ok") {
            this.pagenum = parseInt(response.data['num'])
          }
        })
      }else{
        let params = new URLSearchParams()
        params.append("token",this.$cookies.get("token"))
        params.append("find",this.param.find)
        this.axios.post("searchannouncenum",params
        ).then((response) => {
          //console.log(response.data)
          if (response.data['msg'] == "ok") {
            this.pagenum = parseInt(response.data['num'])
          }
        })
      }

    },
    //显示第几页的内容
    showPage(page){
      if( this.type == 1 ){
        //开启loading
        const load = this.$loading({
          lock: true,
          text: '获取公告信息中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
          target:'#table'
        });
        let params = new URLSearchParams()
        params.append("token",this.$cookies.get("token"))
        params.append("page",page)      //每页显示5个
        this.axios.post("showsomeannounce",
            params
        ).then((response) => {
          load.close()
          //console.log(response.data)
          if (response.data['msg'] == "ok") {
            this.tableData = []
            for( let i = 0 ; i < response.data["announce"].length ;  i++ ) {
              this.tableData.push({
                title: response.data["announce"][i]['title'],
                time: response.data["announce"][i]['time'],
                author: response.data["announce"][i]['author'],
                body: response.data["announce"][i]['body'].slice(0, 80) + "……",      //显示前100个字符
                file: response.data["announce"][i]['file'],
                isemail: response.data["announce"][i]['isemail']
              })
            }
          }
        })
      }else{
        const load = this.$loading({
          lock: true,
          text: '查找公告信息中……',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)',
          target:'#table'
        });
        let params = new URLSearchParams()
        params.append("token",this.$cookies.get("token"))
        params.append("page",page)
        params.append("find",this.param.find)
        this.axios.post("searchannounce",params
        ).then((response) => {
          load.close()
          //console.log(response.data)
          if (response.data['msg'] == "ok") {
            this.tableData = []
            for( let i = 0 ; i < response.data["announce"].length ;  i++ ) {
              this.tableData.push({
                title: response.data["announce"][i]['title'],
                time: response.data["announce"][i]['time'],
                author: response.data["announce"][i]['author'],
                body: response.data["announce"][i]['body'].slice(0, 80) + "……",      //显示前100个字符
                file: response.data["announce"][i]['file'],
                isemail: response.data["announce"][i]['isemail']
              })
            }

          }
        })
      }

    },
    //返回上一页
    goBack(){
      this.$router.go(-1);
    },
    //获取指定公告
    GetAnnounce() {
      //调用获取get后面的参数
      let title  = this.$route.query.title
      let time = this.$route.query.time
      console.log(title+" "+time)

      //请求
      let params = new URLSearchParams();
      params.append('time',time);
      params.append('title',title);
      this.axios.post("getannounce",
          params
      ).then((response) => {
        //console.log(response.data)
        if (response.data['msg'] === "ok") {
          this.title = response.data["title"]
          this.body = response.data["body"]
          this.author = response.data["author"]
          this.time = response.data["time"]
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
  border-bottom-color: rgb(211, 208, 204);
}
.title {
  margin-top: 20px;
  padding-left: 15px;
  padding-bottom: 20px;
  border-bottom: 1px solid #EBECED;
  border-bottom-color: rgb(235, 236, 237);
}
.title p {
  line-height: 15px;
  color: #9FA3A6;
}
.title span {
  padding: 0px 5px;
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
  width: 640px;
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
  width: 580px;
  margin: 0 auto;
  line-height: 30px;
  overflow: hidden;
  font-family: 宋体,SimSun; font-size: 16px;
}
.el-table::before{
  width: auto;
}

</style>