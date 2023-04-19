//公共js（调用方法:https://blog.csdn.net/sqlquan/article/details/95214944）

// 登录判断
export function tokenlogin(cookies,axios,store,router,message,help) {       // eslint-disable-line no-unused-vars
    
    if( cookies.get("token") == null || cookies.get("token") == undefined   ){           //为空,这里好像没用
        //直接返回首页
        message({
            type: 'error',
            message: '您还未登录，请先登录'
        });
        router.push("/login")
        return false;
    }

    let params = new URLSearchParams();
    params.append('token',cookies.get("token"));
    axios.post("tokenlogin",
        params,
    ).then((response) => {
        console.log(response.data)
        if (response.data['msg'] == "ok") {
            //将信息传过去
            store.commit('login', response.data)
            help.name = response.data['name']
        }else{
            //直接返回首页
            message({
                type: 'error',
                message: '您还未登录，请先登录'
            });
            router.push("/login")
            return false;
        }
    })
    return true;
}


// 登录判断（不需要管name的函数）
export function tokenlogin2(cookies,axios,store,router,message,help) {

    if( cookies.get("token") == null || cookies.get("token") == undefined   ){        //为空,这里好像没用
        //直接返回首页
        message({
            type: 'error',
            message: '您还未登录，请先登录'
        });
        router.push("/login")
        return false;
    }

    let params = new URLSearchParams();
    params.append('token',cookies.get("token"));
    axios.post("tokenlogin",
        params,
    ).then((response) => {
        console.log(response.data)
        if (response.data['msg'] == "ok") {
            //将信息传过去
            store.commit('login', response.data)
            help.name = response.data['name']
        }else{
            //直接返回首页
            message({
                type: 'error',
                message: '您还未登录，请先登录'
            });
            router.push("/login")
            return false;
        }
    })
    return true;
}

// 管理员登录判断
export function tokenadminlogin(cookies,axios,store,router,message,help) {       // eslint-disable-line no-unused-vars

    if( cookies.get("token") == null || cookies.get("token") == undefined   ){           //为空,这里好像没用
        //直接返回首页
        message({
            type: 'error',
            message: '您还未登录，请先登录'
        });
        router.push("/adminlogin")
        return false;
    }

    let params = new URLSearchParams();
    params.append('token',cookies.get("token"));
    axios.post("tokenadminlogin",
        params,
    ).then((response) => {
        console.log(response.data)
        if (response.data['msg'] == "ok") {
            //将信息传过去
            store.commit('adminlogin', response.data)
            help.name = response.data['username']
        }else{
            //直接返回首页
            message({
                type: 'error',
                message: '您还未登录，请先登录'
            });
            router.push("/adminlogin")
            return false;
        }
    })
    return true;
}


// 管理员登录判断（不需要管name的函数）
export function tokenadminlogin2(cookies,axios,store,router,message) {

    if( cookies.get("token") == null || cookies.get("token") == undefined   ){        //为空,这里好像没用
        //直接返回首页
        message({
            type: 'error',
            message: '您还未登录，请先登录'
        });
        router.push("/adminlogin")
        return false;
    }

    let params = new URLSearchParams();
    params.append('token',cookies.get("token"));
    axios.post("tokenadminlogin",
        params,
    ).then((response) => {
        console.log(response.data)
        if (response.data['msg'] == "ok") {
            //将信息传过去
            store.commit('adminlogin', response.data)
            // help.name = response.data['username']
        }else{
            //直接返回首页
            message({
                type: 'error',
                message: '您还未登录，请先登录'
            });
            router.push("/adminlogin")
            return false;
        }
    })
    return true;
}



