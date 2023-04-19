<template>
  <div>
    <div style="display: flex; margin-bottom: 10px">
      <el-card style="width: 58%; float: left">
        <p
          style="
            font-size: 18px;
            font-weight: bold;
            margin: 0 auto;
            text-align: center;
          "
        >
          访问量
        </p>
        <div
          ref="echart"
          id="visit"
          :style="{ float: 'left', width: '60%', height: '250px' }"
        ></div>
        <div style="margin-top: 20px; float: left">
          <span style="font-size: 20px; color: #999">总访问量</span><br />
          <span style="font-size: 35px; font-weight: bold">3,650,964</span
          ><br /><br /><br />
          <span style="font-size: 20px; color: #999">今日访问量</span><br />
          <span style="font-size: 35px; font-weight: bold">8156</span>
          <i class="el-icon-caret-top"></i>
        </div>
      </el-card>
      <el-card style="width: 38%; float: right; margin-left: 15px">
        <i class="el-icon-success"></i>
        <div style="float: left; margin-top: 40px; margin-left: 20px">
          <span style="font-size: 20px; color: #999"> 证件上传成功率 </span
          ><br /><br />
          <span style="font-size: 35px; font-weight: bold">95.6%</span>
          <i class="el-icon-caret-bottom"></i>
        </div>
      </el-card>
    </div>
    <div id="num" style="display: flex; margin-bottom: 10px">
      <el-card style="width: 38%; float: left">
        <p
          style="
            font-size: 18px;
            font-weight: bold;
            margin: 0 auto;
            text-align: center;
          "
        >
          报名审核通过人数
        </p>
        <div
          ref="echart"
          id="signupPer"
          :style="{ float: 'left', width: '100%', height: '300px' }"
        ></div>
      </el-card>
      <el-card style="width: 58%; float: right; margin-left: 15px">
        <p
          style="
            font-size: 18px;
            font-weight: bold;
            margin: 0 auto;
            text-align: center;
          "
        >
          报名人数变化趋势
        </p>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane label="年度" name="year">
            <div
              ref="echart"
              id="signupYear"
              :style="{ float: 'left', width: '100%', height: '300px' }"
            ></div>
          </el-tab-pane>
          <el-tab-pane label="季度" name="quarter">
            <div>
              <el-row style="text-align: center; margin: 0 auto">
                <el-select
                  v-model="value"
                  filterable
                  placeholder="请选择年份"
                  @change="signupQuartercharts"
                >
                  <el-option
                    v-for="item in options"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value"
                  >
                  </el-option>
                </el-select>
              </el-row>
              <el-row>
                <div
                  ref="echart"
                  id="signupQuarter"
                  :style="{ float: 'left', width: '100%', height: '300px' }"
                ></div
              ></el-row></div
          ></el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
    <div style="margin-bottom: 10px; height: 300px">
      <el-card style="width: 97%; height: 350px">
        <p
          style="
            font-size: 18px;
            font-weight: bold;
            margin: 0 auto;
            text-align: center;
          "
        >
          报名群体分析
        </p>
        <div
          ref="echart"
          id="age"
          :style="{ float: 'left', width: '38%', height: '300px' }"
        ></div>
        <div
          ref="echart"
          id="sex"
          :style="{ float: 'left', width: '25%', height: '300px' }"
        ></div>
        <div
          ref="echart"
          id="exp"
          :style="{ float: 'left', width: '35%', height: '300px' }"
        ></div>
      </el-card>
    </div>
  </div>
</template>

<script>
import * as echarts from "echarts";

export default {
  data() {
    return {
      activeName: "year",
      options: [
        {
          value: "2017",
          label: "2017",
        },
        {
          value: "2018",
          label: "2018",
        },
        {
          value: "2019",
          label: "2019",
        },
        {
          value: "2020",
          label: "2020",
        },
        {
          value: "2021",
          label: "2021",
        },
        {
          value: "2022",
          label: "2022",
        },
      ],
      Q2017: {
        yValue: [115.4, 119.6, 116.3, 119.48],
      },
      Q2018: {
        yValue: [135.23, 138.1, 135.2, 136.27],
      },
      Q2019: {
        yValue: [147.6, 151.23, 148.4, 149.53],
      },
      Q2020: {
        yValue: [149.4, 154.55, 147.36, 150.25],
      },
      Q2021: {
        yValue: [156.4, 154, 160.25, 155.13],
      },
      Q2022: {
        yValue: [291.1, 270.8, 301.6, 329.5],
      },
      value: "",
      // name: "张雪",
      // xData: ["2020-02", "2020-03", "2020-04", "2020-05"], //横坐标数据
      // yData: [30, 132, 80, 134], //纵坐标数据，与横坐标对应
    };
  },
  mounted() {
    this.signupPerCharts();
    this.signupQuartercharts();
    this.signupYearcharts();
    this.ageCharts();
    this.sexCharts();
    this.expCharts();
    this.visitCharts();
  },
  methods: {
    handleClick(tab, event) {
      console.log(tab, event);
      this.signupQuartercharts();
    },
    signupPerCharts() {
      const myChart = echarts.init(document.getElementById("signupPer")); // 图标初始化
      const option = {
        title: {
          left: "center",
          top: "center",
        },
        series: [
          {
            type: "pie",
            data: [
              {
                value: 86,
                name: "通过 86%",
              },
              {
                value: 14,
                name: "不通过 14%",
              },
            ],
            radius: ["40%", "70%"],
            itemStyle: {
              normal: {
                color: function (colors) {
                  var colorList = [
                    "#FFDB5C",
                    "#ff9f7f",
                    "#37A2DA",
                    "#9FE6B8",
                    "#67E0E3",
                    "#32C5E9",
                    "#fb7293",
                    "#E062AE",
                    "#E690D1",
                    "#e7bcf3",
                    "#9d96f5",
                    "#8378EA",
                    "#96BFFF",
                  ];
                  return colorList[colors.dataIndex];
                },
              },
            },
          },
        ],
      };
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    signupYearcharts() {
      const option = {
        trigger: "item",
        xAxis: {
          data: ["2017", "2018", "2019", "2020", "2021", "2022"],
        },
        yAxis: {
          name: "单位（万）",
          nameTextStyle: {
            color: "#aaa",
            nameLocation: "start",
          },
        },
        series: [
          {
            label: {
              show: true,
              position: "top",
            },
            type: "bar", //类型为柱状图
            data: [470.78, 544.8, 596.76, 601.56, 625.78, 1193],
          },
        ],
      };
      const myChart = echarts.init(document.getElementById("signupYear")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },

    signupQuartercharts() {
      var yValue = null;
      if (this.value === "2017") {
        yValue = this.Q2017.yValue;
      } else if (this.value === "2018") {
        yValue = this.Q2018.yValue;
      } else if (this.value === "2019") {
        yValue = this.Q2019.yValue;
      } else if (this.value === "2020") {
        yValue = this.Q2020.yValue;
      } else if (this.value === "2021") {
        yValue = this.Q2021.yValue;
      } else if (this.value === "2022") {
        yValue = this.Q2022.yValue;
      }
      const option = {
        trigger: "item",
        xAxis: {
          data: ["第一季度", "第二季度", "第三季度", "第四季度"],
        },
        yAxis: {
          type: "value",
          name: "单位（万）",
          nameTextStyle: {
            color: "#aaa",
            nameLocation: "start",
          },
        },
        series: [
          {
            label: {
              show: true,
              position: "top",
            },
            type: "bar", //类型为柱状图
            data: yValue,
          },
        ],
      };
      const myChart = echarts.init(document.getElementById("signupQuarter")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    ageCharts() {
      const option = {
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: ["40%", "70%"],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: "#fff",
              borderWidth: 2,
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 40,
                fontWeight: "bold",
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              { value: 0.1, name: "18岁以下 10%" },
              { value: 0.5, name: "18-25岁 50%" },
              { value: 0.3, name: "26-35岁 30%" },
              { value: 0.08, name: "36-45岁 8%" },
              { value: 0.02, name: "45岁以上 2%" },
            ],
          },
        ],
      };
      const myChart = echarts.init(document.getElementById("age")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    sexCharts() {
      const option = {
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: "50%",
            data: [
              { value: 69, name: "男 69%" },
              { value: 31, name: "女 31%" },
            ],
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
              normal: {
                color: function (colors) {
                  var colorList = [
                    "#759aa0",
                    "#dd6b66",
                    "#e69d87",
                    "#8dc1a9",
                    "#ea7e53",
                    "#eedd78",
                    "#73a373",
                    "#73b9bc",
                    "#7289ab",
                    "#91ca8c",
                    "#f49f42",
                  ];
                  return colorList[colors.dataIndex];
                },
              },
            },
          },
        ],
      };
      const myChart = echarts.init(document.getElementById("sex")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    expCharts() {
      const option = {
        tooltip: {
          trigger: "item",
        },
        series: [
          {
            name: "Access From",
            type: "pie",
            radius: "50%",
            data: [
              { value: 0.7, name: "专科及以上 70%" },
              { value: 0.3, name: "专科以下 30%" },
            ],
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: "rgba(0, 0, 0, 0.5)",
              normal: {
                color: function (colors) {
                  var colorList = [
                    "#67E0E3",
                    "#96BFFF",
                    "#E690D1",
                    "#32C5E9",
                    "#37A2DA",
                    "#9FE6B8",
                    "#FFDB5C",
                    "#ff9f7f",
                    "#fb7293",
                    "#E062AE",

                    "#e7bcf3",
                    "#9d96f5",
                    "#8378EA",
                  ];
                  return colorList[colors.dataIndex];
                },
              },
            },
          },
        ],
      };
      const myChart = echarts.init(document.getElementById("exp")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    visitCharts() {
      const option = {
        xAxis: {
          type: "category",
          show: false,
          boundaryGap: false,
          data: ["周一", "周二", "周三", "周四", "周五", "周六", "周日"],
        },
        yAxis: {
          type: "value",
          show: false,
        },
        splitLine: {
          show: false, // 不显示网格线
        },
        series: [
          {
            data: [8200, 9320, 9010, 9340, 7290, 9330, 8320],
            type: "line",
            areaStyle: {},
            itemStyle: {
              normal: {
                color: function (colors) {
                  var colorList = [
                    "#759aa0",
                    "#dd6b66",
                    "#e69d87",
                    "#8dc1a9",
                    "#ea7e53",
                    "#eedd78",
                    "#73a373",
                    "#73b9bc",
                    "#7289ab",
                    "#91ca8c",
                    "#f49f42",
                  ];
                  return colorList[colors.dataIndex];
                },
              },
            },
          },
        ],
      };
      const myChart = echarts.init(document.getElementById("visit")); // 图标初始化
      myChart.setOption(option); // 渲染页面
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
  },
};
</script>

<style lang="less" scoped>
.el-icon-caret-top {
  font-size: 25px;
  color: red;
  margin-left: 10px;
}
.el-icon-success {
  font-size: 150px;
  color: green;
  margin-top: 20px;
  float: left;
}
.el-icon-caret-bottom {
  font-size: 25px;
  color: green;
  margin-left: 10px;
}
</style>