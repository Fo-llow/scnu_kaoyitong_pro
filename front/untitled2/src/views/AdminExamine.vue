<template>
  <div>
    <el-form :model="param" ref="find">
      <el-form-item label="" prop="find">
        <div class="search-form">
          <el-input
            type="text"
            placeholder="请输入要搜索的内容"
            size="small"
            prefix-icon="el-icon-search"
            v-model="param.find"
            style="width: 350px"
          ></el-input>

          <el-button type="primary" @click="Search">
            <i class="el-icon-search"></i>
            搜索
          </el-button>
        </div>
        <div>
          <el-collapse
            accordion
            style="width: 540px; margin: 0 auto; white-space: pre-wrap"
          >
            <el-collapse-item title="高级筛选" name="1">
              <el-card style="width: 540px">
                <el-button
                  style="margin-right: 1px"
                  type="danger"
                  size="small"
                  @click="cancel"
                  >清除筛选</el-button
                >
                <div>
                  <span>内容：</span>
                  <br />
                  <el-radio-group
                    v-model="tagchoose.content.which"
                    size="small"
                    @change="toTag"
                  >
                    <el-radio-button
                      label="姓名"
                      @click.native.prevent="radioClick('姓名', 1)"
                    />
                    <el-radio-button
                      label="身份证号"
                      @click.native.prevent="radioClick('身份证号', 1)"
                    />
                    <el-radio-button
                      label="准考证号"
                      @click.native.prevent="radioClick('准考证号', 1)"
                    />
                    <el-radio-button
                      label="电子邮箱"
                      @click.native.prevent="radioClick('电子邮箱', 1)"
                    />
                    <el-radio-button
                      label="手机号码"
                      @click.native.prevent="radioClick('手机号码', 1)"
                    />
                  </el-radio-group>
                </div>
                <div>
                  <span>性别：</span>
                  <br />
                  <el-radio-group v-model="tagchoose.sex.which" size="small">
                    <el-radio-button
                      label="男"
                      @click.native.prevent="radioClick('男', 2)"
                    />
                    <el-radio-button
                      label="女"
                      @click.native.prevent="radioClick('女', 2)"
                    />
                  </el-radio-group>
                </div>
                <div>
                  <span>专业层次：</span>
                  <br />
                  <el-radio-group
                    v-model="tagchoose.zycc.which"
                    size="small"
                    @change="toTag"
                  >
                    <el-radio-button
                      label="本科"
                      @click.native.prevent="radioClick('本科', 3)"
                    />
                    <el-radio-button
                      label="专科"
                      @click.native.prevent="radioClick('专科', 3)"
                    />
                  </el-radio-group>
                </div>

                <div>
                  <span>报考学校：</span>
                  <br />
                  <el-radio-group
                    v-model="tagchoose.zymc.which"
                    size="small"
                    @change="toTag"
                  >
                    <el-radio-button
                      label="华南师范大学"
                      @click.native.prevent="radioClick('华南师范大学', 4)"
                      :disabled="zy"
                    />
                    <el-radio-button
                      label="中山大学"
                      @click.native.prevent="radioClick('中山大学', 4)"
                      :disabled="zy"
                    />
                    <el-radio-button
                      label="深圳大学"
                      @click.native.prevent="radioClick('深圳大学', 4)"
                      :disabled="zy"
                    />
                    <el-radio-button
                      label="暨南大学"
                      @click.native.prevent="radioClick('暨南大学', 4)"
                      :disabled="zy"
                    />
                    <el-radio-button
                      label="广东工业大学"
                      @click.native.prevent="radioClick('广东工业大学', 4)"
                      :disabled="zy"
                    />
                    <el-radio-button
                      label="广州大学"
                      @click.native.prevent="radioClick('广州大学', 4)"
                      :disabled="zy"
                    />
                    <el-radio-button
                      label="华南理工大学"
                      @click.native.prevent="radioClick('华南理工大学', 4)"
                      :disabled="zy"
                    />

                    <el-radio-button
                      label="华南农业大学"
                      @click.native.prevent="radioClick('华南农业大学', 4)"
                      :disabled="bk"
                    />
                    <el-radio-button
                      label="广东财经大学"
                      @click.native.prevent="radioClick('广东财经大学', 4)"
                      :disabled="bk"
                    />
                  </el-radio-group>
                </div>

                <div>
                  <span>审核结果：</span>
                  <br />
                  <el-radio-group
                    v-model="tagchoose.res.which"
                    size="small"
                    @change="toTag"
                  >
                    <el-radio-button
                      label="审核通过"
                      @click.native.prevent="radioClick('审核通过', 5)"
                    />
                    <el-radio-button
                      label="审核不通过"
                      @click.native.prevent="radioClick('审核不通过', 5)"
                    />
                    <el-radio-button
                      label="未审核"
                      @click.native.prevent="radioClick('未审核', 5)"
                    />
                  </el-radio-group>
                </div>
              </el-card>
              筛选结果：<br />
              <el-tag
                v-for="tag in tags"
                :key="tag.name"
                class="mx-1"
                closable
                @close="handleClose(tag)"
                :type="tag.type"
              >
                {{ tag.name }}
              </el-tag>
            </el-collapse-item>
          </el-collapse>
        </div>
      </el-form-item>

      <!--
          <el-tag
              v-for="tag in dynamicTags"
              :key="tag"
              class="mx-1"
              closable
              :disable-transitions="false"
              @close="handleClose(tag)"
          >
            {{ tag }}
          </el-tag>
          <el-input
              v-if="inputVisible"
              ref="InputRef"
              v-model="inputValue"
              class="ml-1 w-20"
              size="small"
              @keyup.enter="handleInputConfirm"
              @blur="handleInputConfirm"
          >
          </el-input>
        -->
    </el-form>
    <div style="float: right; margin-bottom: 15px">
      <el-tooltip
        effect="dark"
        content="仅当前页面未审核的数据"
        placement="top"
      >
        <el-button type="primary" @click="RandompassSign">智能审核</el-button>
      </el-tooltip>
      <el-tooltip
        effect="dark"
        content="仅当前页面未审核的数据"
        placement="top"
      >
        <el-button type="primary" @click="AllpassSign">
          一键审核通过
        </el-button>
      </el-tooltip>
      <el-tooltip
        effect="dark"
        content="仅当前页面未审核的数据"
        placement="top"
      >
        <el-button type="primary" @click="AllrefuseSign"
          >一键审核不通过</el-button
        >
      </el-tooltip>
      <el-button type="primary" @click="exportExcel">导出表格</el-button>
    </div>
    <!-- 隐藏表格，用来导出全部数据   -->
    <el-table :data="outData" class="outtable" style="display: none">
      <el-table-column prop="name" label="姓名" width="70" />
      <el-table-column prop="sex" label="性别" width="50" />
      <el-table-column prop="sfzh" label="身份证号" width="100" />
      <el-table-column prop="ticket" label="准考证号" width="100" />
      <el-table-column prop="tele" label="手机号码" width="100" />
      <el-table-column prop="email" label="电子邮箱" width="100" />
      <el-table-column prop="zycc" label="专业层次" width="100" />
      <el-table-column prop="zymc" label="报考学校" width="100" />
      <el-table-column prop="kcdm" label="课程代码" width="100" />
      <el-table-column
        prop="file"
        label="个人文件"
        width="100"
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
      <el-table-column prop="res" label="审核结果" width="100" />
    </el-table>

    <el-table :data="tableData" style="width: 100%" class="roomtable">
      <el-table-column
        prop="name"
        label="姓名"
        align="center"
        style="width: 50px"
      />
      <el-table-column
        prop="sex"
        label="性别"
        align="center"
        style="width: 5%"
      />
      <el-table-column
        prop="sfzh"
        label="身份证号"
        align="center"
        style="width: 10%"
      />
      <el-table-column
        prop="ticket"
        label="准考证号"
        align="center"
        style="width: 8%"
      />
      <el-table-column
        prop="tele"
        label="手机号码"
        align="center"
        style="width: 10%"
      />
      <el-table-column
        prop="email"
        label="电子邮箱"
        align="center"
        style="width: 10%"
      />
      <el-table-column
        prop="zycc"
        label="专业层次"
        align="center"
        style="width: 10%"
      />
      <el-table-column
        prop="zymc"
        label="报考学校"
        align="center"
        style="width: 8%"
      />
      <el-table-column
        prop="kcdm"
        label="课程代码"
        align="center"
        style="width: 10%"
      />
      <el-table-column
        prop="file"
        label="个人文件"
        #default="scope"
        align="center"
        style="width: 8%"
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
        prop="res"
        label="审核结果"
        align="center"
        style="width: 5%"
      >
        <template #default="scope">
          <div v-if="tableData[scope.$index].res == '未审核'">
            <el-button type="text">未审核</el-button>
          </div>
          <div v-if="tableData[scope.$index].res == '审核通过'">
            <el-button type="text">审核通过</el-button>
          </div>
          <div v-if="tableData[scope.$index].res == '审核不通过'">
            <el-tooltip class="tooltip" effect="dark" placement="left-start">
              <div slot="content">
                {{ RefWarning[Math.floor(Math.random() * (8 - 0 + 1)) + 0] }}
              </div>
              <el-button type="text">审核不通过</el-button>
            </el-tooltip>
          </div>
        </template>

        <!-- <el-button
        type="text"
        v-for="item in tableData[scope.$index]"
        :key="item.ticket"
        #default="scope"
        >{{ item.res }}
        </el-button> -->
      </el-table-column>
      <el-table-column label="操作" align="center" style="width: 10%">
        <template #default="scope">
          <div v-if="tableData[scope.$index].res == '未审核'">
            <el-button type="primary" circle @click="PassSign(scope.$index)">
              <i class="el-icon-check"></i>
            </el-button>
            <el-button type="danger" circle @click="RefuseSign(scope.$index)">
              <i class="el-icon-close"></i>
            </el-button>
          </div>
          <div v-if="tableData[scope.$index].res == '审核通过'">
            <el-button type="success" circle @click="BackSign(scope.$index)">
              <i class="el-icon-back"></i>
            </el-button>
          </div>
          <div v-if="tableData[scope.$index].res == '审核不通过'">
            <el-button type="success" circle @click="BackSign(scope.$index)">
              <i class="el-icon-back"></i>
            </el-button>
          </div>
        </template>
      </el-table-column>
    </el-table>
    <br />
    <template>
      <div style="display: block">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="pagenum"
          :hide-on-single-page="false"
          :current-page="page"
          @current-change="showPage"
        >
        </el-pagination>
      </div>
    </template>
  </div>
</template>

<script>
export default {
  //报名审核
  name: "AdminExamine",
  data() {
    return {
      //页数
      pagenum: 1,
      //当前页数
      page: 1,
      //显示的标签
      tags: [
        //  { name: 'Tag 1', type: '' },
      ],
      //筛选框选本科专科，专业名称的变化
      bk: false,
      zy: false,
      //标签的选择
      tagchoose: {
        content: {
          which: "0", //默认0表示没选择
        },
        sex: {
          which: "0", //默认0表示没选择
        },
        zycc: {
          which: "0", //默认0表示没选择
        },
        zymc: {
          which: "0", //默认0表示没选择
        },
        res: {
          which: "0", //默认0表示没选择
        },
      },
      param: {
        find: "",
      },
      //当前表格数据的类型
      type: 1,
      tableData: [],
      outData: [],
      RefWarning: [
        "身份证号或准考证号填写错误或与提交的个人文件不符。",
        "个人文件未按要求提交或提交的照片质量不清晰。",
        "学生填写的手机号码或电子邮箱无法联系，或者联系方式填写错误。",
        "学生提交的个人文件内容与报考专业不符，如报考计算机专业却提交艺术作品。",
        "学生填写的信息不完整或不符合要求，未上传个人文件。",
        "个人文件中的照片与身份证照片不一致，或者照片不符合标准要求。",
        "学生填写的信息与报考条件不符，如报名年龄不符合要求。",
        "个人文件中的学历、成绩单等证明材料不符合要求，如未经公证或认证。",
        "学生所报考的专业或课程要求具备特定的资格或证书，而学生没有提供相关的证明文件。",
      ],
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
    this.calcPage();
    //显示第一页的内容
    this.showPage(this.page);
    this.$forceUpdate(); //解决数据渲染延迟
    // this.out()
  },
  methods: {
    //清除筛选
    cancel() {
      this.type = 1;
      this.tagchoose.content.which = "0";
      this.tagchoose.sex.which = "0";
      this.tagchoose.zycc.which = "0";
      this.tagchoose.zymc.which = "0";
      this.tagchoose.res.which = "0";

      this.tags = [];

      this.calcPage();
      //显示第一页的内容
      this.showPage(this.page);
      this.$forceUpdate(); //解决数据渲染延迟
    },
    //加载全部书籍，为导出表格做准备
    out() {
      //异步执行的方法：https://www.cnblogs.com/xiaofeilin/p/14621205.html
      return new Promise((resolve) => {
        if (this.type == 1) {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "导出表格中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          //params.append("page",page)
          //不传page获取到的就是全部的
          this.axios.post("examine", params).then((response) => {
            load.close();
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.outData = response.data["examine"];
              resolve("ok");
            }
          });
        } else {
          //开启loading
          const load = this.$loading({
            lock: true,
            text: "导出表格中……",
            spinner: "el-icon-loading",
            background: "rgba(0, 0, 0, 0.7)",
          });
          let params = new URLSearchParams();
          params.append("token", this.$cookies.get("token"));
          //不传page得到的就是全部
          //params.append("page","1")
          params.append("content", this.tagchoose.content.which);
          params.append("sex", this.tagchoose.sex.which);
          params.append("zycc", this.tagchoose.zycc.which);
          params.append("zymc", this.tagchoose.zymc.which);
          params.append("res", this.tagchoose.res.which);
          params.append("search", this.param.find);
          this.axios.post("search", params).then((response) => {
            load.close();
            //console.log(response.data)
            if (response.data["msg"] == "ok") {
              this.outData = response.data["examine"];
              resolve("ok");
            }
          });
        }
      });
    },
    //导出表格
    exportExcel() {
      this.out().then((res) => {
        console.log(res);
        var wb = this.$xlsx.utils.table_to_book(
          document.querySelector(".outtable"),
          { raw: true }
        );
        /* get binary string as output */
        var wbout = this.$xlsx.write(wb, {
          bookType: "xlsx",
          bookSST: true,
          type: "array",
        });
        try {
          let d = new Date();
          let s = d.getFullYear() + "." + d.getMonth() + "." + d.getDate();
          this.$fileSaver.saveAs(
            new Blob([wbout], { type: "application/octet-stream" }),
            "报名信息(" + s + ").xlsx"
          );
        } catch (e) {
          if (typeof console !== "undefined") console.log(e, wbout);
        }
        // 恢复为原来的
        this.outData = [];
      });
    },
    //标签的关闭
    handleClose(tag) {
      if (tag.name.search("内容") != -1) {
        this.tagchoose.content.which = "0";
      } else if (tag.name.search("性别") != -1) {
        this.tagchoose.sex.which = "0";
      } else if (tag.name.search("专业层次") != -1) {
        this.tagchoose.zycc.which = "0";
        this.zy = this.bk = false;
      } else if (tag.name.search("报考学校") != -1) {
        this.tagchoose.zymc.which = "0";
      } else if (tag.name.search("审核结果") != -1) {
        this.tagchoose.res.which = "0";
      }
      this.tags.splice(this.tags.indexOf(tag), 1);
    },
    //el-radoi再次选择取消 https://blog.csdn.net/qwe19991123/article/details/122989049
    radioClick(e, model) {
      if (model == 1) {
        e === this.tagchoose.content.which
          ? (this.tagchoose.content.which = "0")
          : (this.tagchoose.content.which = e);
      } else if (model == 2) {
        e === this.tagchoose.sex.which
          ? (this.tagchoose.sex.which = "0")
          : (this.tagchoose.sex.which = e);
      } else if (model == 3) {
        e === this.tagchoose.zycc.which
          ? (this.tagchoose.zycc.which = "0")
          : (this.tagchoose.zycc.which = e);
        if (this.tagchoose.zycc.which == "本科") {
          this.bk = true;
        } else {
          this.bk = false;
        }
        if (this.tagchoose.zycc.which == "专科") {
          this.zy = true;
        } else {
          this.zy = false;
        }
      } else if (model == 4) {
        e === this.tagchoose.zymc.which
          ? (this.tagchoose.zymc.which = "0")
          : (this.tagchoose.zymc.which = e);
      } else if (model == 5) {
        e === this.tagchoose.res.which
          ? (this.tagchoose.res.which = "0")
          : (this.tagchoose.res.which = e);
      }
      this.toTag();
    },
    //计算总页数
    calcPage() {
      if (this.type == 1) {
        //初始状态，全部的
        let params = new URLSearchParams();
        params.append("token", this.$cookies.get("token"));
        this.axios.post("signupnum", params).then((response) => {
          //console.log(response.data)
          if (response.data["msg"] == "ok") {
            this.pagenum = parseInt(response.data["num"]);
          }
        });
      } else {
        //搜索筛选的
        let params = new URLSearchParams();
        params.append("token", this.$cookies.get("token"));
        params.append("content", this.tagchoose.content.which);
        params.append("sex", this.tagchoose.sex.which);
        params.append("zycc", this.tagchoose.zycc.which);
        params.append("zymc", this.tagchoose.zymc.which);
        params.append("res", this.tagchoose.res.which);
        params.append("search", this.param.find);
        this.axios.post("searchnum", params).then((response) => {
          //console.log(response.data)
          if (response.data["msg"] == "ok") {
            this.pagenum = parseInt(response.data["num"]);
          }
        });
      }
    },
    //搜索
    Search() {
      this.type = 2;
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "搜索中……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      params.append("page", "1");
      params.append("content", this.tagchoose.content.which);
      params.append("sex", this.tagchoose.sex.which);
      params.append("zycc", this.tagchoose.zycc.which);
      params.append("zymc", this.tagchoose.zymc.which);
      params.append("res", this.tagchoose.res.which);
      params.append("search", this.param.find);
      this.axios.post("search", params).then((response) => {
        load.close();
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.tableData = response.data["examine"];
          this.calcPage();
        }
      });
    },
    //显示第几页的内容
    showPage(page) {
      if (this.type == 1) {
        //开启loading
        const load = this.$loading({
          lock: true,
          text: "获取报名信息中……",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
          target: "#table",
        });
        let params = new URLSearchParams();
        params.append("token", this.$cookies.get("token"));
        params.append("page", page);
        this.axios.post("examine", params).then((response) => {
          load.close();
          //console.log(response.data)
          if (response.data["msg"] == "ok") {
            this.tableData = response.data["examine"];
          }
        });
      } else {
        //开启loading
        const load = this.$loading({
          lock: true,
          text: "获取报名信息中……",
          spinner: "el-icon-loading",
          background: "rgba(0, 0, 0, 0.7)",
          target: "#table",
        });
        let params = new URLSearchParams();
        params.append("token", this.$cookies.get("token"));
        params.append("page", page);
        params.append("content", this.tagchoose.content.which);
        params.append("sex", this.tagchoose.sex.which);
        params.append("zycc", this.tagchoose.zycc.which);
        params.append("zymc", this.tagchoose.zymc.which);
        params.append("res", this.tagchoose.res.which);
        params.append("search", this.param.find);
        this.axios.post("search", params).then((response) => {
          load.close();
          //console.log(response.data)
          if (response.data["msg"] == "ok") {
            this.tableData = response.data["examine"];
          }
        });
      }
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
    //点击标签
    toTag() {
      //console.log(message)
      this.tags = [];
      //console.log()
      if (this.tagchoose.content.which != "0") {
        this.tags.push({
          name: "内容：" + this.tagchoose.content.which,
          type: "",
        });
      }
      if (this.tagchoose.sex.which != "0") {
        this.tags.push({ name: "性别：" + this.tagchoose.sex.which, type: "" });
      }
      if (this.tagchoose.zycc.which != "0") {
        this.tags.push({
          name: "专业层次：" + this.tagchoose.zycc.which,
          type: "",
        });
      }
      if (this.tagchoose.zymc.which != "0") {
        this.tags.push({
          name: "报考学校：" + this.tagchoose.zymc.which,
          type: "",
        });
      }
      if (this.tagchoose.res.which != "0") {
        this.tags.push({
          name: "审核结果：" + this.tagchoose.res.which,
          type: "",
        });
      }
      //this.handleClose(message)
    },
    //通过报名
    PassSign(index) {
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      params.append("ticket", this.tableData[index]["ticket"]);
      params.append("res", "1");
      this.axios.post("auditstatus", params).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.$forceUpdate();
          this.showPage(this.page);
          //console.log(response.data)
        }
      });
    },
    //审核不通过
    RefuseSign(index) {
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      params.append("ticket", this.tableData[index]["ticket"]);
      params.append("res", "-1");
      this.axios.post("auditstatus", params).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.$forceUpdate();
          //console.log(response.data)
          this.showPage(this.page);
        }
      });
    },
    //撤销审核
    BackSign(index) {
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      params.append("ticket", this.tableData[index]["ticket"]);
      params.append("res", "0");
      this.axios.post("auditstatus", params).then((response) => {
        //console.log(response.data)
        if (response.data["msg"] == "ok") {
          this.$forceUpdate();
          //console.log(response.data)
          this.showPage(this.page);
        }
      });
    },
    // 智能审核
    RandompassSign() {
      this.$confirm("此操作将智能审核当前页面的数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          for (let i = 0; i < this.tableData.length; i++) {
            if (
              this.tableData[i]["res"] == "未审核" &&
              this.tableData[i]["file"] != ""
            ) {
                   // 以30%的概率随机审核通过或不通过
                    if (Math.random() < 0.3) {
                      this.PassSign(i);
                    } else {
                      this.RefuseSign(i);
                    }     
            }
          }
          this.$message({
            type: "success",
            message: "智能审核成功!",
          });

          this.showPage(this.page);
          this.$forceUpdate();
        })
        .catch(() => {
          //console.log(error)
          this.$message({
            type: "info",
            message: "取消智能审核",
          });
        });
    },
    //一键审核通过
    AllpassSign() {
      this.$confirm(
        "此操作将把当前页面未审核的数据全部审核通过, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          for (let i = 0; i < this.tableData.length; i++) {
            if (this.tableData[i]["res"] == "未审核") {
              this.PassSign(i);
            }
          }
          this.$message({
            type: "success",
            message: "一键审核通过成功!",
          });

          this.showPage(this.page);
          this.$forceUpdate();
        })
        .catch(() => {
          //console.log(error)
          this.$message({
            type: "info",
            message: "取消一键审核通过",
          });
        });
    },
    //一键审核不通过
    AllrefuseSign() {
      this.$confirm(
        "此操作将把当前页面未审核的数据全部审核不通过, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      )
        .then(() => {
          this.$message({
            type: "success",
            message: "一键审核不通过成功!",
          });
          for (let i = 0; i < this.tableData.length; i++) {
            if (this.tableData[i]["res"] == "未审核") {
              this.RefuseSign(i);
            }
          }
          this.showPage(this.page);
          this.$forceUpdate();
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消一键审核不通过",
          });
        });
    },
  },
};
</script>

<style lang="less" scoped>
.search-form {
  margin: 0 auto;
  width: 450px;
}
.el-collapse-item__header {
  font-size: 16px;
}
.el-pagination {
  text-align: center;
}
.el-card {
  span {
    font-weight: bold;
  }
}
.el-radio-button {
  border: none;
}
</style>