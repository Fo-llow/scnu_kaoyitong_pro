<template>
  <div>
    <div class="infotable">
      <div style="position: relative; width: 65%">
      <el-table
        :data="AnnounceData"
        :header-cell-style="{ background: '#3b6496', color: '#fff' }"
      >
        <el-table-column prop="link" label="通知公告">
          <template #default="scope">
            <el-link
              @click="LookAnnounce(AnnounceData[scope.$index].link)"
              target="_blank"
              :title="AnnounceData[scope.$index].title"
              >{{
                "【" +
                AnnounceData[scope.$index].time +
                "】" +
                AnnounceData[scope.$index].title
              }}</el-link
            >
          </template>
          <!-- <span>名字</span> -->
        </el-table-column>

        <el-table-column prop="" width="80">
          <template slot="header">
            <!-- <span>名字</span> -->
            <!--          <a href="" target="_blank" class="buttonText">更多</a>-->
            <el-link
              type="primary"
              style="text-align: center; color: #fff; font-weight: 700"
              @click="goMore"
              >更多</el-link
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-card class="welcome-card">
        <div>
          <div style="display: flex">
            <el-avatar icon="el-icon-user" :size="40"></el-avatar>
            <p class="name">{{ help.name }}，欢迎回来！</p>
          </div>
          <div>
            <p class="loginfo">
              登录时间：<span>{{ getDate() }}</span>
            </p>
          </div>
        </div>
      </el-card>
    </div>

    <div class="img-div">
      <div style="position: relative; width: 65%">
        <el-carousel indicator-position="outside" height="400px">
          <el-carousel-item>
            <div
              style="
                color: #000000;
                text-align: center;
                overflow: hidden;
                width: 100%;
              "
            >
              <img style="width: 100%; height: auto" src="../assets/cs1.jpg" />
            </div>
          </el-carousel-item>

          <el-carousel-item>
            <div
              style="
                color: #000000;
                text-align: center;
                overflow: hidden;
                width: 100%;
              "
            >
              <img style="width: 100%; height: auto" src="../assets/cs3.jpg" />
            </div>
          </el-carousel-item>
          <el-carousel-item>
            <div
              style="
                color: #000000;
                text-align: center;
                overflow: hidden;
                width: 100%;
              "
            >
              <img style="width: 100%; height: auto" src="../assets/cs4.png" />
            </div>
          </el-carousel-item>
          <el-carousel-item>
            <div
              style="
                color: #000000;
                text-align: center;
                overflow: hidden;
                width: 100%;
              "
            >
              <img style="width: 100%; height: auto" src="../assets/cs5.png" />
            </div>
          </el-carousel-item>
        </el-carousel>
      </div>
      <el-card class="links">
        <div slot="header" class="links-title">
          <span style="font-weight: bold; font-size: 20px">相关链接</span>
        </div>
        <div>
          <el-link
            class="links-content"
            type="primary"
            style="font-weight: bold"
            href="https://eea.gd.gov.cn/zxks/index.html"
            >广东省教育考试院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://sce.sysu.edu.cn/"
            >中山大学高等教育中心</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://jyxy.jnu.edu.cn/main.htm"
            >暨南大学教育学院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://gdou.scnu.edu.cn/web/newindex/index.jsp"
            >华南师范大学教育学院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://jxjy.scau.edu.cn/"
            >华南农业大学教育学院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://jxjyxy.gdut.edu.cn/index.htm"
            >广东工业大学教育学院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://cce.szu.edu.cn/index.htm"
            >深圳大学教育学院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="http://cj.gzhu.edu.cn/"
            >广州大学教育学院</el-link
          ><br />
          <el-link
            class="links-content"
            type="info"
            href="https://ae.gdufe.edu.cn/main.htm"
            >广东财经大学教育学院</el-link
          ><br />
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  //考生成功后的首页
  name: "SuccessIndex",
  data() {
    return {
      AnnounceData: [],
      help: { name: "", other: "" },
    };
  },
  created() {
    // 进行身份验证（可以解决刷新和直接进入页面问题）
    this.$public.tokenlogin(
      this.$cookies,
      this.axios,
      this.$store,
      this.$router,
      this.$message,
      this.help
    );
  },
  mounted() {
    this.GetAnnounce();
  },
  methods: {
    getDate() {
      let time = new Date();
      var dateArr = [
        time.getMonth() + 1,
        time.getDate(),
        time.getHours(),
        time.getMinutes(),
        time.getSeconds(),
      ];
      for (var i = 0; i < dateArr.length; i++) {
        if (dateArr[i] >= 1 && dateArr[i] <= 9) {
          dateArr[i] = "0" + dateArr[i];
        }
      }
      let timestring =
        time.getFullYear() +
        "-" +
        dateArr[0] +
        "-" +
        dateArr[1] +
        " " +
        dateArr[2] +
        ":" +
        dateArr[3] +
        ":" +
        dateArr[4];
      return timestring;
    },
    goMore() {
      this.$router.push("/more");
    },
    //获得公告
    GetAnnounce() {
      let param = new URLSearchParams();
      param.append("token", this.$cookies.get("token"));
      this.axios.post("showannounce", param).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          //this.tableData = response.data["announce"]
          this.AnnounceData = [];
          //最多显示五条
          let length = response.data["announce"].length;
          if (length > 5) length = 5;

          for (let i = 0; i < length; i++) {
            this.AnnounceData.push({
              title: response.data["announce"][i]["title"],
              time: response.data["announce"][i]["time"],
              link:
                "/announcement?title=" +
                response.data["announce"][i]["title"] +
                "&time=" +
                response.data["announce"][i]["time"] +
                "&token=" +
                this.$cookies.get("token"),
            });
          }
        }
      });
    },
    LookAnnounce(link) {
      this.$router.push(link);
    },
  },
};
</script>

<style scoped>
#main {
  float: left;
  width: 680px;
}
.colblock h2 .more {
  line-height: 20px;
}
.r {
  float: right;
}
.colblock {
  background-color: rgb(224, 223, 220);
  background-image: none;
  border-color: rgb(200, 196, 192);
}
.colblock {
  margin-bottom: 25px;
  overflow: hidden;
  font-family: Microsoft Yahei;
  background: #fff;
  background-color: rgb(255, 255, 255);
  background-image: none;
  border: solid #dedede;
  border-top-color: rgb(222, 222, 222);
  border-top-width: medium;
  border-right-color: rgb(222, 222, 222);
  border-right-width: medium;
  border-bottom-color: rgb(222, 222, 222);
  border-bottom-width: medium;
  border-left-color: rgb(222, 222, 222);
  border-left-width: medium;
  border-width: 0 1px 1px;
  border-radius: 4px;
}
.cl {
  zoom: 1;
}
.welcome-card {
  margin-left: 30px;
  margin-right: 10px;
  text-align: center;
  width: 30%;
  height: auto;
  right: 0;
}
.name {
  font-size: 16px;
  font-weight: bold;
  margin-left: 20px;
  text-align: center;
}
.infotable {
  width: 100%;
  display: flex;
  margin-bottom: 20px;
}
.el-table {
  float: left;
  /* width: 70%; */
  margin-right: 40px;
  overflow-x: auto;
}
.img-div {
  position: relative;
  display: flex;
}
.el-carousel-item {
  line-height: 400px;
}
.links {
  margin-left: 30px;
  text-align: center;
  width: 30%;
  height: auto;
  right: 0;
  /* float: right; */
  /* position: absolute; */
  /* padding-right: 20px; */
  margin-right: 10px;
}
.el-card__header {
  /* background-color: #3b6496; */
}
.links-content {
  font-size: 15px;
  margin: 8px;
}
</style>