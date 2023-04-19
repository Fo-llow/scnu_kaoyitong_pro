import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
    // 全局变量

    state: {
        //baseurl:"http://localhost:8080/spring_new_war_exploded/",
        baseurl: "http://8.134.142.145:8080/kaoyitong/",
        username:"",                //记录忘记密码的
        //记录考生登录成功的
        user: {
            username: '',
            name:'',
        },
        //记录管理员登录成功的
        adminuser:{
            username:"",
            permission:"",
        }
    },
    // 修改全局变量必须通过mutations中的方法
    // mutations只能采用同步方法
    mutations: {
        //忘记密码，记录一下用户名
        forget(state,payload){
            state.username = payload
        },
        //清空
        forgetSuccess(state){
            state.username = ""
        },
        //登录
        login (state, payload) {
            //console.log("asdasd" + payload)
            state.user = payload
        },
        //注销后清除user
        logout (state) {
            state.user = {
                    username: '',
                    email: '',
                    name:'',
                }
        },
        //登录
        adminlogin (state, payload) {
            //console.log("asdasd" + payload)
            state.adminuser = payload
        },
        //注销后清除user
        adminlogout (state) {
            state.adminuser = {
                username:"",
                permission:"",
            }
        }
    },
    // 异步方法用actions
    // actions不能直接修改全局变量，需要调用commit方法来触发mutation中的方法
    actions: {
        login (context, payload) {
            context.commit('login', payload)
        },
        logout (context) {
            context.commit('logout')
        }
    }
})

export default store
