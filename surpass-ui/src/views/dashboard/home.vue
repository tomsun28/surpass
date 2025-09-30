<template>
  <div class="home app-container">
    <div class="fir-data">
      <div class="fir-data-blue item">
        <p class="fir-data1">{{ dashboard.activeUsers }}</p>
        <p class="fir-data2">{{ $t('jbx.home.onlineUsers') }}</p>
        <img src="../../assets/images/dashboard/download.png" alt="">
      </div>
      <div class="fir-data-green item">
        <p class="fir-data1">{{ dashboard.dayCount }}</p>
        <p class="fir-data2">{{ $t('jbx.home.dayCount') }}</p>
        <img src="../../assets/images/dashboard/download.png" alt="">
      </div>
      <div class="fir-data-orange item">
        <p class="fir-data1">{{ dashboard.newUsers }}</p>
        <p class="fir-data2">{{ $t('jbx.home.newUsers') }}</p>
        <img src="../../assets/images/dashboard/download.png" alt="">
      </div>
      <div class="fir-data-pink item">
        <p class="fir-data1">{{ dashboard.onlineUsers }}</p>
        <p class="fir-data2">{{ $t('jbx.home.activeUsers') }}</p>
        <img src="../../assets/images/dashboard/download.png" alt="">
      </div>
    </div>

    <div class="sec app-container2">
      <p class="title">{{ $t('jbx.home.dayAccessCount') }}</p>
      <hr/>
      <div id="main1" style="width: 100%;height:350px;"></div>
    </div>
    <div class="thr app-container2">
      <p class="title">{{ $t('jbx.home.monthAccessCount') }}</p>
      <hr/>
      <div id="main2" style="width: 100%;height:350px;"></div>
    </div>
    <div class="four app-container2">
      <p class="title">{{ $t('jbx.home.monthProvinceAccessCount') }}</p>
      <hr/>
      <div class="four-content">
        <el-row>
          <el-col :span="12">
            <ChinaMap :data="dashboard"></ChinaMap>
          </el-col>
          <el-col :span="12">
            <el-table :data="dashboard.reportProvince" stripe style="width: 100%;height: 500px;">
              <el-table-column prop="number" :label="$t('jbx.home.number')" align="center" width="150">
              </el-table-column>
              <el-table-column prop="province" :label="$t('jbx.home.province')" align="center">
              </el-table-column>
              <el-table-column prop="pv" :label="$t('jbx.home.accessPV')" align="center" width="150">
              </el-table-column>
              <el-empty slot="empty" description="暂无数据"></el-empty>
            </el-table>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="last app-container2">
      <div class="five">
        <p class="title">{{ $t('jbx.home.monthAppCount') }}</p>
        <hr/>
        <el-table :data="dashboard.reportApp" stripe style="width: 100%">
          <el-table-column prop="reportName" :label="$t('jbx.home.appName')">
          </el-table-column>
          <el-table-column prop="reportCount" :label="$t('jbx.home.accessCount')">
          </el-table-column>
        </el-table>
      </div>
      <div class="five">
        <p class="title">{{ $t('jbx.home.monthBrowserCount') }}</p>
        <hr/>
        <el-table :data="dashboard.reportBrowser" stripe style="width: 100%">
          <el-table-column prop="reportName" :label="$t('jbx.home.browser')">
          </el-table-column>
          <el-table-column prop="reportCount" :label="$t('jbx.home.accessCount')">
          </el-table-column>
        </el-table>
      </div>
    </div>
    <Footer></Footer>
  </div>
</template>

<script lang="ts">
import * as echarts from 'echarts'

import {
  getDashboard
} from '@/api/dashboard.js'
import ChinaMap from './chinaMap.vue'
import Footer from '@/components/Footer/index.vue'

export default {
  name: 'DashboardHome',
  components: {
    ChinaMap,
    Footer
  },
  data() {
    return {
      dashboard: {
        reportApp: [],
        reportBrowser: [],
        reportProvince: [],
        reportDayHour: [],
        reportMonth: [],
        activeUsers: 0,
        dayCount: 0,
        newUsers: 0,
        onlineUsers: 0,
      }
    }
  },
  mounted: function () {
    getDashboard().then((res: any) => {
      this.dashboard = res.data
      this.myEcharts()
      this.myEcharts2()
    })
  },
  methods: {
    fmtHourText(text: string) {
      return String(parseInt(text)).padStart(2, '0') + "时";
    },

    myEcharts() {
      // 基于准备好的dom，初始化echarts实例
      let myChart: any = echarts.init(document.getElementById('main1'))

      // 指定图表的配置项和数据
      let option: any = {
        tooltip: {
          trigger: 'item',  //数据项图形触发
          backgroundColor: "#fff",
          borderWidth: 0,
          formatter: (params: any) => {
            // params 包含了数据项的所有信息
            let name = params.name || '0';
            const value = params.value ? params.value : 0;
            name = this.fmtHourText(name) + "-" + this.fmtHourText(parseInt(name) + 1)
            return `小时区间：<b>${name}</b><br/>访问次数：<b>${value || 0}次</b>`;
          }
        },
        grid: {
          top: "5%",
          left: "5%",
          right: "5%",
          bottom: "10%",
        },
        xAxis: {
          type: 'category',
          data: this.dashboard.reportDayHour.map((t: any) => {
            return this.fmtHourText(t.reportName)
          })
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: this.dashboard.reportDayHour.map((t: any) => {
            return t.reportCount
          }),
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#1890ff'
            }
          }
        }]
      }
      option && myChart.setOption(option)
      window.addEventListener("resize", function () {
        myChart.resize();
      })
    },

    myEcharts2() {
      // 基于准备好的dom，初始化echarts实例
      let myChart: any = echarts.init(document.getElementById('main2'))
      // 指定图表的配置项和数据
      let option: any = {
        tooltip: {
          trigger: 'item',  //数据项图形触发
          backgroundColor: "#fff",
          borderWidth: 0,
          formatter: (params: any) => {
            // params 包含了数据项的所有信息
            let name = params.name || '';
            const value = params.value ? params.value : 0;
            return `日期：<b>${name}</b><br/>次数：<b>${value || 0}次</b>`;
          }
        },
        grid: {
          top: "5%",
          left: "5%",
          right: "5%",
          bottom: "10%",
        },
        xAxis: {
          type: 'category',
          data: this.dashboard.reportMonth.map((t: any) => {
            return t.reportName
          })
        },
        yAxis: {
          type: 'value'
        },
        series: [{
          data: this.dashboard.reportMonth.map((t: any) => {
            return t.reportCount
          }),
          type: 'bar',
          itemStyle: {
            normal: {
              color: '#1890ff'
            }
          }
        }]
      }

      option && myChart.setOption(option)

      window.addEventListener("resize", function () {
        myChart.resize();
      })
    }
  }
}
</script>

<style scoped lang="scss">
.app-container {
  padding: 0;
  background: transparent;
}

.app-container2 {
  padding: 0 20px;
  background: #FFF;
  margin-bottom: 20px;

  .title {
    font-size: 16px;
    padding: 15px 0;
  }
}

.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}

.fir-data {
  //background-color: #f5f7fa;
  margin: 0;
  padding: 0 0 20px 0;
  width: auto;
  display: flex;

  .item {
    position: relative;
    padding: 16px;

    img {
      position: absolute;
      top: 40px;
      right: 20px;
      width: 40%;
      height: 40px;
    }
  }
}

.fir-data-blue,
.fir-data-green,
.fir-data-orange,
.fir-data-pink {
  width: 33.3%;
  color: #fff;
}

.fir-data-blue {
  background-color: #1890ff;
}

.fir-data-green {
  margin: 0 15px;
  background-color: #52c41a;
}

.fir-data-orange {
  margin-right: 15px;
  background-color: #fa8c16;
}

.fir-data-pink {
  background-color: #eb2f96;
}

.fir-data1 {
  font-size: 26px;
  padding: 0;
  margin: 0;
}

.fir-data2 {
  padding: 0;
}

.four-content {
  margin-top: 20px;
}

.last {
  display: flex;
  justify-content: space-between;

  .five {
    width: 48%;
  }
}

.foot {
  width: 100%;
}

.foot hr {
  margin-bottom: 20px;
}

.foot p {
  text-align: center;
  padding: 5px 0;
  margin: 0;
}
</style>
