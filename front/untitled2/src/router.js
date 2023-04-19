import Vue from "vue";
import Router from "vue-router";

// import Home from "./views/Home.vue";
import Login from "@/views/Login";
import Register from "@/views/Register";
import CS from "@/views/CS";
import Forget from "@/views/Forget";
import Success from "@/views/Success";
import Forgetpass from "@/views/Forgetpass";
import Signupinfo from "@/views/Signupinfo";
import AdminLogin from "@/views/AdminLogin";
import AdminSuccess from "@/views/AdminSuccess";
import Fail404 from "@/views/Fail404";
import RegisterStatus from "@/views/RegisterStatus";
import ai from "@/views/ai";

import AdminManage from "@/views/AdminManage";
import AdminAnnounce from "@/views/AdminAnnounce";
import AdminJurisdict from "@/views/AdminJurisdict";
import AdminExamine from "@/views/AdminExamine";
import SuccessIndex from "@/views/SuccessIndex";
import PersonInfo from "@/views/PersonInfo";
import Announcement from "@/views/Announcement";
import AdminIndex from "@/views/AdminIndex";
import More from "@/views/More";
import AdminDataview from "@/views/AdminDataview";

Vue.use(Router);

export default new Router({
    routes: [
        {
            path: "/",
            component: Login
        },
        //登陆界面，即首页
        {
            path: "/login",
            component: Login
        },
        //登陆界面，即首页
        {
            path: "/index",
            component: Login
        },
        //注册界面
        {
            path: "/register",
            component: Register
        },
        //点击忘记密码后出现的
        {
            path: "/forget",
            component: Forget
        },
        //输入身份证后的页面
        {
            path: "/forgetpass",
            component: Forgetpass
        },
        //登陆成功后界面
        {
            path: "/success",
            component: Success,
            children:[              //路由的嵌套，实现页面局部刷新
                //登录首页
                {
                    path: "successindex",
                    component: SuccessIndex
                },
                //报名界面
                {
                    path: "signupinfo",
                    component: Signupinfo
                },
                //报名状态
                {
                    path: "registerstatus",
                    component: RegisterStatus
                },
                {
                    path: "ai",
                    component: ai
                },
                //个人信息页面
                {
                    path:"personinfo",
                    component:PersonInfo
                },
                //查看公告
                {
                    path: "announcement",
                    component: Announcement
                },
            ]
        },
        //管理员界面
        {
            path: "/adminlogin",
            component: AdminLogin
        },
        //具体公告页面
        {
            path: "/announcement",
            component: Announcement
        },
        //更多公告页面
        {
            path: "/more",
            component: More
        },
        //管理员登陆成功界面
        {
            path: "/adminsuccess",
            component: AdminSuccess,
            /*
            <el-menu-item index="">报名管理</el-menu-item>
            <el-menu-item index="">报名审核</el-menu-item>
            <el-menu-item index="">公告发布</el-menu-item>
            <el-menu-item index="">机房管理</el-menu-item>
            <el-menu-item index="">权限管理</el-menu-item>
            */
            children:[              //路由的嵌套，实现页面局部刷新
                //报名管理
                {
                    path: "adminmanage",
                    component: AdminManage
                },
                //报名审核
                {
                    path: "adminexamine",
                    component: AdminExamine
                },
                //公告发布
                {
                    path: "adminannounce",
                    component: AdminAnnounce
                },
                //权限管理
                {
                    path: "adminjurisdict",
                    component: AdminJurisdict
                },
                //查看公告
                {
                    path: "announcement",
                    component: Announcement
                },
                //管理员首页
                {
                    path: "adminindex",
                    component: AdminIndex
                },
                // 数据可视化
                {
                    path:"admindataview",
                    component:AdminDataview
                }
            ]
        },

        //404界面
        {
            path: "/*",
            name: '404',
            component: Fail404
        },
        //测试界面，不用理
        {
            path: "/CS",
            component: CS
        },

    ]
});