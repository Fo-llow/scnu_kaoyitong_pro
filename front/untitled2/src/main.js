import Vue from 'vue'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

import axios from 'axios'
import VueAxios from 'vue-axios'
import Vuex from 'vuex'
import store from './Vuex'
import common from './common'   // 引入全局文件common.js
import 'lib-flexible'           // 在vue项目里面的src目录下的main.js全局引用
import "./public.css"           // 引入全局css
import "./mobile.css"           // 引入全局css
//import 'amfe-flexible'
//cookies配置（https://blog.csdn.net/weixin_46371752/article/details/122321152）
import cookies from 'vue-cookies'
import * as fun from './public';
//导出表格：https://blog.csdn.net/qq_45616003/article/details/118939568
import FileSaver from "file-saver";
import * as XLSX from "xlsx";
import App from './App.vue'
import router from "./router"
import echarts from "echarts";
Vue.prototype.$echarts = echarts;


Vue.prototype.$common = common;    // 全局变量
axios.defaults.baseURL = common.baseURL;


Vue.prototype.$cookies = cookies;

Vue.prototype.$public = fun;        //全局引入js:https://www.jianshu.com/p/00bc0a63a50f



Vue.prototype.$fileSaver = FileSaver
Vue.prototype.$xlsx = XLSX


Vue.use(Vuex)
Vue.use(VueAxios, axios)
Vue.use(ElementUI)//全局使用ElementUI
Vue.config.productionTip = false


new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
