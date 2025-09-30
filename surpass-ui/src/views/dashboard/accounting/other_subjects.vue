<template>
  <el-card shadow="hover" header-class="el-card-header">
    <template #header>
      <div class="card-title">
        <span>其它科目指标</span>
        <el-cascader style="width: 200px;margin-left: 50px" filterable clearable
                     v-model="subjectCode"
                     :options="subjectList"
                     :props="cascaderSubjectPropsOwn"
                     @change="handleSubjectChange"
                     :filter-method="cascaderSubjectPropsOwn.filterMethod"/>
      </div>
    </template>

    <div class="card-content">
      <el-row>
        <el-col :span="16">
          <div ref="chartRef" style="width: 100%; height: 400px"></div>
        </el-col>
        <el-col :span="8">
          <div ref="chartRef2" style="width: 100%; height: 400px"></div>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>


<script setup lang="ts">
import {ref, reactive, toRefs, onMounted, onBeforeUnmount} from "vue";
import bookStore from "@/store/modules/bookStore";
import * as echarts from 'echarts'
import {cascaderSubjectProps} from "@/utils/Subjects"
import * as subjectApi from "@/api/system/standard/standard-subject";
import {statisticsOtherSubjects} from "@/api/dashboard"
import {BaseValue} from "@/types/FundBalance";

const cascaderSubjectPropsOwn = ref<any>({...cascaderSubjectProps})
cascaderSubjectPropsOwn.value.placement = "left"
cascaderSubjectPropsOwn.value.checkStrictly = true

const currBookStore = bookStore()
const chartRef = ref(null)
let chartInstance: any = null
const chartRef2 = ref(null)
let chartInstance2: any = null
const subjectCode = ref("")
// 会计科目数据
const subjectList = ref<any>([])

interface SubjectBalance {
  subjectName: string;
  subjectBalance: BaseValue[];
}

interface DataState {
  queryParams: {
    bookId: string;
    accountPeriod: string;
    periodType: string;
    reportDate: string;
    subjectCodes: string[];
  };
  resData: SubjectBalance[];
}

const data = reactive<DataState>({
  queryParams: {
    bookId: currBookStore.bookId,
    accountPeriod: "currentPeriod",
    periodType: "month",
    subjectCodes: [],
    reportDate: currBookStore.termCurrent
  },
  resData: [],
});
const {queryParams, resData} = toRefs(data);

const handleSubjectChange = (value: any) => {
  window.localStorage.setItem('fund_subject_code', subjectCode.value||'1002')
  queryParams.value.subjectCodes = [subjectCode.value]
  getList()
}
// 初始化 chart（只执行一次）
const initChart = (option: any, option2: any) => {
  if (chartRef.value && !chartInstance) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(option)

    chartInstance2 = echarts.init(chartRef2.value)
    chartInstance2.setOption(option2)
    window.addEventListener('resize', handleResize)
  } else {
    chartInstance.setOption(option, true); // true 表示不做合并，完全覆盖
    chartInstance2.setOption(option2, true); // true 表示不做合并，完全覆盖
  }
}
// 窗口大小变化自动 resize
const handleResize = () => {
  chartInstance.resize()
  chartInstance2.resize()
}
const getList = () => {
  statisticsOtherSubjects(queryParams.value).then((res: any) => {
    resData.value = res.data
    if (resData.value.length > 0) {
      const xAxisData = resData.value[0].subjectBalance.map((item) => {
        return item.name
      })
      const legendData = resData.value.map((item) => {
        return item.subjectName
      })
      const seriesData = resData.value.map((item) => {
        return {
          name: item.subjectName,
          type: 'line',
          data: item.subjectBalance.map((item) => {
            return item.value
          }),
        }
      })

      const option = {
        title: {
          text: '变化趋势',
          left: 'left',
          top: "6%",
          textStyle: {fontSize: 14}
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {type: 'shadow'}
        },
        legend: {
          data: legendData,
          right: 0,
          top: "6%",
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '15%',
          top: "25%",
        },
        xAxis: [
          {
            type: 'category',
            data: xAxisData,
            axisTick: {alignWithLabel: true}
          }
        ],
        yAxis: {
          type: 'value',
          name: '金额/万',
          position: 'left'
        },
        series: seriesData
      }
      // // 计算总金额
      const totalAmount = resData.value.map(item => {
        return item.subjectBalance[item.subjectBalance.length - 1]
      }).reduce((sum, item) => sum + item.value, 0);
      const option2 = {
        title: {
          text: '本期现金分析',
          subtext: '',
          left: 'center',
          top: "5%"
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'right',
          top: '10%'
        },
        series: [
          {
            name: '本期现金分析',
            type: 'pie',
            radius: ['35%', '60%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'center',
              formatter: [
                '{title|总额合计(万元)}',
                '{value|' + totalAmount.toLocaleString() + '}',
              ].join('\n'),
              rich: {
                title: {
                  fontSize: 14,
                  color: '#333',
                  lineHeight: 20
                },
                value: {
                  fontSize: 20,
                  color: '#333',
                  fontWeight: 'bold',
                  lineHeight: 30
                }
              }
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '40',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: resData.value.map(item => {
              return {
                name: item.subjectName,
                value: item.subjectBalance[item.subjectBalance.length - 1].value
              }
            })
          }
        ]
      }

      initChart(option, option2)
    }
  })
}

onMounted(() => {
  //传入当前账套ID
  subjectApi.getTree({
    bookId: currBookStore.bookId
  }).then((res: any) => {
    subjectList.value = res.data
    //默认科目 1002 银行存款
    subjectCode.value = window.localStorage.getItem('fund_subject_code') || "1002"
    console.log(window.localStorage.getItem('fund_subject_code')=='undefined')
    queryParams.value.subjectCodes = [subjectCode.value]
    getList()
  })
})

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if(chartInstance){
    chartInstance.dispose()
    chartInstance = null
  }
  if(chartInstance2){
    chartInstance2.dispose()
    chartInstance2 = null
  }
})
</script>
<style scoped lang="scss">
:deep(.el-card__header) {
  padding: 4px 4px 4px 20px;
  background-color: #1890ff44;
  height: 30px;
}

:deep(.el-select__wrapper) {
  background-color: transparent;
  box-shadow: none;
}

.card-title {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.card-content {

  &-item {
    font-size: 0.9em;
    height: 70px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;

    .bold {
      font-weight: bold;
      font-size: 1.5em;
    }

    .small {
      font-size: 0.9em;
      line-height: 1.5em;
    }
  }
}
</style>
