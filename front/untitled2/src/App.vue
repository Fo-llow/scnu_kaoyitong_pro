<template>
  <div id="app" v-cloak>
    <router-view />
  </div>
</template>

<script>

export default {
  name: 'App',
  created() {
    if (this._isMobile()) {
      alert("请在电脑端打开该网站！")
      console.log("手机端");

    } else {
      console.log("pc端");
    }
  },
  methods: {
    // 判断手机端还是PC端
    _isMobile() {
      let flag = navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i)
      return flag;
    }
  },
}
</script>

<style lang="less" scoped>
*{margin:0; padding:0;}
[v-clock]{
  display: none;
}
</style>
