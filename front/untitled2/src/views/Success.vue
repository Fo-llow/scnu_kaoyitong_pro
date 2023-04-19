<template>
  <div>
    <el-container>
      <el-header style="height: 40px">
        <Header> </Header>
        <div style="right: 76px; top: 55px; position: absolute; z-index: 3">
          <el-dropdown>
            <div class="user">
              <el-avatar icon="el-icon-user">
                <i class="el-icon-arrow-down"></i>
              </el-avatar>
              <div class="userinfo">{{ help.name }}</div>
            </div>

            <el-dropdown-menu>
              <!-- el-dropdown-item这里的按钮点击事件需要添加.native，否则会报错 -->
              <el-dropdown-item @click.native="goInfo"
                >个人信息</el-dropdown-item
              >
              <el-dropdown-item @click.native="goModify"
                >修改密码</el-dropdown-item
              >
              <el-dropdown-item @click.native="logout"
                >退出登录</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside
          width="200px"
          style="min-width: 200px; max-width: 200px; margin-top: 90px"
        >
          <el-menu
            default-openeds="['1']"
            background-color="#fff"
            text-color="#000000"
            active-text-color="#409EFF"
            :default-active="$route.path"
            @open="handleOpen"
            @close="handleClose"
            router
          >
            <el-menu-item index="/success/successindex">
              <i class="el-icon-house"></i>
              首页</el-menu-item
            >
            <el-submenu index="1">
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span slot="title">报考信息</span>
              </template>
              <el-menu-item index="/success/signupinfo">
                <i class="el-icon-edit"></i>
                我要报名</el-menu-item
              >
              <el-menu-item index="/success/registerstatus">
                <i class="el-icon-notebook-2"></i>
                报考状态</el-menu-item
              >
            </el-submenu>
            <el-menu-item index="/success/ai">
              <i class="el-icon-chat-dot-round"></i>
              小易智能询问</el-menu-item
            >
            <!--
            <el-submenu v-for="(item, i) in nav" :key="i" :index="item.index">
              <template slot="title">
                <i class="el-icon-place"></i>
                <span>{{ item.title }}</span>
              </template>
              <el-menu-item v-for="(option, j) in item.options" :key="j" :index="`${item.index}/${j+1}`">{{option}}</el-menu-item>
            </el-submenu>
            -->
          </el-menu>
                                                
        </el-aside>
        <el-container>
          <el-main style="margin-top: 90px;">
            <!-- 路由占位符 -->
            <router-view></router-view>
          </el-main>
          <el-footer>
            <Footer></Footer>
          </el-footer>
        </el-container>
      </el-container>
    </el-container>
  </div>
</template>

<script>
import Header from "@/components/Header";
import Footer from "@/components/Footer";
import ElementUI from "element-ui";

export default {
  name: "Success",
  components: { Header, Footer },
  data() {
    return {
      // 数组等类型才能引用传值，后面的变量实际没用
      help: { name: "", other: "" }, // 数组才能引用传参
      nav: [
        {
          title: "报考信息",
          index: "/success/signupinfo",
          options: ["我要报名", "报名情况"],
        },
        {
          title: "本校学生",
          index: 2,
          options: ["毕业论文", "证书证明"],
        },
      ],
      Height: 0,
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
  methods: {
    //点击个人信息
    goInfo() {
      //console.log("asd")
      this.$router.push("/success/personinfo");
    },
    //点击修改密码
    goModify() {
      //开启loading
      const load = this.$loading({
        lock: true,
        text: "正在向邮箱发送密码重置码……",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      });
      //发送短信
      let params = new URLSearchParams();
      params.append("token", this.$cookies.get("token"));
      params.append("username", this.$store.state.user.username);
      this.axios({
        method: "post",
        url: "forgetpass",
        withCredentials: true,
        data: params,
        dataType: "JSONP", // 处理Ajax跨域问题,
      }).then((response) => {
        load.close();
        //console.log(response.data)
        //console.log(response.data['msg'])
        if (response.data["msg"] == "ok") {
          //console.log("help")
          // 设置全局user
          this.$store.commit("forget", this.$store.state.user.username);
          ElementUI.Message({
            message: "已向您的邮箱发送重置码，请进入邮箱查看",
            type: "success",
          });
          this.$router.push("/forgetpass?which=2");
        } else {
          //注册失败
          //this.error_msg = response.data['error']
          ElementUI.Message({
            message: "您的邮箱错误或者还未登陆，无法进行修改密码",
            type: "error",
          });
        }
      });
    },
    //点击退出登陆
    logout() {
      this.$store.commit("logout");
      this.$cookies.remove("token");
      //返回首页
      this.$router.push("/index");
    },
    /*
    getParams(){
      // 取到路由带过来的参数
      this.$forceUpdate()
      //console.log(this.$store.state.user.name)
      this.name = this.$store.state.user.name;
    },*/
    // 点击按钮,切换菜单的折叠和展开
    toggleCollpase() {
      this.isCollapse = !this.isCollapse;
    },
    saveActivePath(activePath) {
      window.sessionStorage.setItem("activePath", activePath);
      this.activePath = activePath;
      //console.log(this.$route.path)
    },
    handleOpen(key, keyPath) {
      console.log(key, keyPath);
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    },
  },
};
</script>

<style lang="less" scoped>
@import "../public.css";
// .el-container {
//   /*设置内部填充为0，几个布局元素之间没有间距*/
//   // padding: 0px;
//   /*外部间距也是如此设置*/
//   // margin: auto;
//   height: 100%;
//   width: 100%;
//   position: relative;
// }
// .Contain {
//   position: absolute;
//   width: 100%;
//   min-height: 100%;
//   display: flex;
//   flex-direction: column; //column 显示为列 方向为在垂直方向上从上到下
//   justify-content: space-between; //最左最右靠边，中间间距相等
// }
// .el-footer {
//   display: flex;
//   height: 4vh;
//   width: 100%;
//   align-items: center;
// }
// .el-aside {
//   position: relative;
// }
.user {
  margin-top: 10px;
  align-content: right;
  position: relative;
}
.userinfo {
  color: #ffffff;
  text-align: center;
  float: right;
  margin: 10px;
}







.chatbot {
  position: fixed;
  bottom: 20px;
  right: 20px;
  width: 400px;
  height: 500px;
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
  z-index: 9999;
}

.chatbot-header {
  height: 40px;
  line-height: 40px;
  padding: 0 20px;
  font-size: 16px;
  color: #fff;
  background-color: #409eff;
  border-top-left-radius: 10px;
  border-top-right-radius: 10px;
  cursor: move;
}

.chatbot-body {
  height: 460px;
  overflow-y: auto;
}

.chatbot-dialog-item {
  margin: 10px;
  display: flex;
}

.chatbot-dialog-item-user {
  flex: 1;
  text-align: right;
  color: #999;
  margin-right: 10px;
}

.chatbot-dialog-item-ai {
  flex: 1;
  color: #333;
  margin-left: 10px;
}

.chatbot-input {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 10px;
}
</style>