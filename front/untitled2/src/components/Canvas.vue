<template>
  <div style="margin-left: 18px">
    <img id="canvas"  ref="canvas" @click="createCode()" title="看不清换一张" style="width: 100px;height: 40px">
  </div>
</template>

<script>
export default {
  name: "Canvas",
  //组件的生命周期   在实例创建完成之后
  //已经完全被创建出来了
  mounted:function () {
    //console.log("测试")
    this.createCode()
  },
  data(){
    return {
      codeImg:"",
    }
  },
  methods: {
    createCode() {
        this.$refs.canvas.src = this.$store.state.baseurl + "/getVerifyCode?"+new Date().getTime();
    } ,
  }



}
</script>

<style scoped>

</style>