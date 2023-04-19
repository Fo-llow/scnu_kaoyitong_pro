<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="历史公告" name="first">
        <el-table :data="tableData" style="width: 100%" id="table">
          <el-table-column prop="time" label="发布时间" align="center" />
          <el-table-column prop="title" label="公告标题" align="center" />
          <el-table-column prop="author" label="公告作者" align="center" />
          <el-table-column prop="body" label="公告内容" align="center" />
          <el-table-column
            prop="file"
            label="公告附件"
            align="center"
            #default="scope"
          >
            <el-link
              v-for="(f, key) in segmenteFile(scope.row.file)"
              :key="key"
              target="_blank"
              :href="f.fsrc"
              >{{ f.fname }}</el-link
            >
          </el-table-column>
          <el-table-column
            prop="isemail"
            label="是否发送考生邮箱"
            align="center"
          />
          <el-table-column prop="operation" label="操作" align="center">
            <template #default="scope">
              <!--
              <el-button type="primary" circle >
                <i class="el-icon-edit"></i>
              </el-button>
              -->
              <el-tooltip
                effect="dark"
                content="删除公告"
                placement="top-start"
              >
                <el-button
                  type="danger"
                  circle
                  @click="DelAnnounce(scope.$index)"
                >
                  <i class="el-icon-delete"></i>
                </el-button>
              </el-tooltip>
              <el-tooltip
                effect="dark"
                content="查看详情"
                placement="top-start"
              >
                <el-button
                  type="success"
                  circle
                  @click="LookAnnounce(scope.$index)"
                >
                  <i class="el-icon-view"></i>
                </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
        <br />
        <template style="width: 100%">
          <div style="display: block">
            <el-pagination
              background
              layout="prev, pager, next"
              :total="pagenum"
              :hide-on-single-page="false"
              :current-page="page"
              :page-size="5"
              @current-change="showPage"
            >
            </el-pagination>
          </div>
        </template>
      </el-tab-pane>

      <el-tab-pane label="发布公告" name="second">
        <div>
          <el-form
            :model="param"
            :rules="rules"
            ref="announce"
            label-width="80px"
          >
            <el-form-item class="small-item" label="公告时间" prop="time">
              <el-input
                type="text"
                placeholder="建议格式为xxxx-xx-xx xx:xx:xx"
                size="small"
                prefix-icon="el-icon-time"
                v-model="param.time"
              ></el-input>
            </el-form-item>
            <el-form-item class="small-item" label="公告标题" prop="title">
              <el-input
                type="text"
                placeholder="公告显示发布的标题"
                size="small"
                prefix-icon="el-icon-connection"
                v-model="param.title"
              ></el-input>
            </el-form-item>
            <el-form-item class="small-item" label="公告作者" prop="author">
              <el-input
                type="text"
                placeholder="公告显示发布的作者"
                size="small"
                prefix-icon="el-icon-s-promotion"
                v-model="param.author"
              ></el-input>
            </el-form-item>
            <el-form-item label="公告内容" prop="body">
              <el-input
                placeholder="公告显示发布的内容"
                size="small"
                prefix-icon="el-icon-printer"
                :autosize="{ minRows: 9, maxRows: 15 }"
                type="textarea"
                v-model="param.body"
              ></el-input>
            </el-form-item>
            <div class="uploadfile">
              <el-form-item label="附件上传" style="font-weight: bold;">
                <el-upload
                  ref="upload"
                  list-type="text"
                  :on-preview="handlePictureCardPreview"
                  :before-upload="beforeUpload"
                  :on-remove="handleRemove"
                  :auto-upload="false"
                  :multiple="true"
                  :limit="3"
                  show-file-list
                >
                  <el-button type="primary">点击上传</el-button>
                  <!--<el-button type="primary">上传个人照片</el-button>-->
                </el-upload>
                    <div class="el-upload__tip">文件大小不得超过500KB。</div>
              </el-form-item>
            </div>
            <div>
            <el-form-item prop="isEmail">
              <el-checkbox
                v-model="param.isemail"
                label="是否发送到考生邮箱"
                size="large"
                style="margin-right:20px"
              ></el-checkbox>
              <el-button type="danger" @click="Submit" style="position: relative;">发布</el-button>
            </el-form-item>
          </div>
          </el-form>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import ElementUI from "element-ui";

export default {
  //公告发布
  name: "AdminAnnounce",
  data() {
    return {
      pagenum: 0,
      page: 1,
      //页头显示哪个
      activeName: "first",
      //公告的内容
      tableData: [],
      fileList: [],
      form: "",
      //发布公告的表单
      param: {
        time: "",
        title: "",
        author: "",
        body: "",
        isemail: false,
      },
      //表单限定规则
      rules: {
        time: [
          {
            required: true,
            message: "请输入公告显示发布的时间",
            trigger: "blur",
          },
        ],
        title: [
          {
            required: true,
            message: "请输入公告显示发布的标题",
            trigger: "blur",
          },
        ],
        author: [
          {
            required: true,
            message: "请输入公告显示发布的标题",
            trigger: "blur",
          },
        ],
        body: [
          {
            required: true,
            message: "请输入公告显示发布的内容",
            trigger: "blur",
          },
        ],
      },
    };
  },
  created() {
    // 进行身份验证（可以解决刷新和直接进入页面问题）
    this.$public.tokenadminlogin2(
      this.$cookies,
      this.axios,
      this.$store,
      this.$router,
      this.$message
    );
  },
  mounted() {
    //this.GetAnnounce()
    this.showPage(1);
    this.calcPage();
    this.form = new FormData(); //放到最前面，就可以保证没有文件也可以
  },
  methods: {
    //计算总页数
    calcPage() {
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("announcenum", params).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.pagenum = parseInt(response.data["num"]);
        }
      });
    },
    //显示第几页的内容
    showPage(page) {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "获取公告信息中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: "#table",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      params.append("page", page); //每页显示5个
      this.axios.post("showsomeannounce", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.tableData = [];
          for (let i = 0; i < response.data["announce"].length; i++) {
            this.tableData.push({
              title: response.data["announce"][i]["title"],
              time: response.data["announce"][i]["time"],
              author: response.data["announce"][i]["author"],
              body: response.data["announce"][i]["body"].slice(0, 80) + "……", //显示前100个字符
              file: response.data["announce"][i]["file"],
              isemail: response.data["announce"][i]["isemail"],
            });
          }
        }
      });
    },
    //上传文件相关函数
    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },
    beforeUpload(file) {
      //console.log(file)

      this.fileList.push(file);

      let images = [...this.fileList];
      //console.log("images", images);

      //this.param.append("file1", images);

      images.forEach((img) => {
        this.form.append("file", img); // 把单个图片重命名，存储起来（给后台）
      });
      return false;
    },
    //分割文件(file是所有的字符串地址)
    segmenteFile(file) {
      if (file == null) return null; //注意加上，否则文件为空的话就会报错不显示
      let array = file.split("|");
      //console.log(array)
      //console.log(array.length)
      let list = [];
      for (let i = 0; i < array.length; i++) {
        let names = array[i].split("_");
        list.push({
          fname: names[1],
          fsrc:
            this.$store.state.baseurl +
            array[i] +
            "?token=" +
            this.$cookies.get("token"),
        });
      }
      //console.log(list)
      return list;
    },
    //获取公告
    GetAnnounce() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "获取公告中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
        target: "#table",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      this.axios.post("showannounce", params).then((response) => {
        load.close();
        if (response.data["msg"] == "ok") {
          //this.tableData = response.data["announce"]

          this.tableData = [];
          for (let i = 0; i < response.data["announce"].length; i++) {
            this.tableData.push({
              title: response.data["announce"][i]["title"],
              time: response.data["announce"][i]["time"],
              author: response.data["announce"][i]["author"],
              body: response.data["announce"][i]["body"].slice(0, 80) + "……", //显示前100个字符
              file: response.data["announce"][i]["file"],
              isemail: response.data["announce"][i]["isemail"],
            });
          }
          //console.log(this.tableData)
        }
      });
    },
    //删除公告
    DelAnnounce(index) {
      //console.log(index+" "+this.tableData[index])
      this.$confirm("此操作将删除该公告, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "删除该公告中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("time", this.tableData[index]["time"]);
          params.append("title", this.tableData[index]["title"]);
          params.append("token", this.$cookies.get("token"));
          this.axios.post("deleteannounce", params).then((response) => {
            load.close();
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.tableData = response.data["announce"];
              this.GetAnnounce();
              this.$forceUpdate(); //解决数据渲染延迟
            }
          });
          this.$message({
            type: "success",
            message: "删除成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除",
          });
        });
    },
    //查看公告
    LookAnnounce(index) {
      let time = this.tableData[index]["time"];
      let title = this.tableData[index]["title"];
      this.$router.push(
        "/announcement?title=" +
          title +
          "&time=" +
          time +
          "&token=" +
          this.$cookies.get("token")
      );
    },
    Submit() {
      let error = 0;
      this.$refs.announce.validate((valid) => {
        if (!valid) {
          this.$message({
            type: "error",
            message: "请先正确填写公告信息",
          });
          error = 1;
          return;
        } else {
          //验证通过调用保存接口
        }
      });
      if (error == 1) return;

      this.$confirm("请确认公告是否无误，是否发布", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "发布公告中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });

          setTimeout(() => {
              load.close();
            }, 2000);
          //解决axios提交到后端为null的情况
          //let params = new URLSearchParams()
          this.$refs.upload.submit();

          this.form.append("token", this.$cookies.get("token"));
          this.form.append("title", this.param.title);
          this.form.append("time", this.param.time);
          this.form.append("author", this.param.author);
          this.form.append("body", this.param.body);
          this.form.append("isemail", this.param.isemail);
          //console.log(this.form)
          //    设置上传数据类型
          let configs = {
            headers: { "Content-Type": "multipart/form-data" },
          };

          this.axios
            .post("addannounce", this.form, configs)
            .then((response) => {
              load.close();
              //console.log(response.data)
              //console.log(response.data['msg'])
              if (response.data["msg"] == "ok") {
                ElementUI.Message({
                  message: "成功发布公告",
                  type: "success",
                });
                //this.GetCode();
              } else {
                ElementUI.Message({
                  message: "发布公告失败，请检查后重试",
                  type: "error",
                });
              }
            });
        })
        .catch((error) => {
          console.log(error);
          this.$message({
            type: "info",
            message: "已取消发布",
          });
        });
    },
  },
};
</script>

<style scoped>
.el-input {
  width: 320px;
}
.el-pagination {
  text-align: center;
}
.el-form {
  text-align: center;
  border-radius: 8px;
  margin: 0 auto;
  width: 80%;
}
.small-item {
  width: 50%;
  margin: 0 auto;
  margin-bottom: 20px;
  margin-top: 10px;
}
.uploadfile {
  width: 30%;
  margin: 0 auto;
  margin-top: 10px;
}
.el-form-item__label {
  font-weight: bold;
}
</style>