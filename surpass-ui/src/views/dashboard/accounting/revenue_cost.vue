<template>
  <el-card shadow="hover" header-class="el-card-header">
    <template #header>
      <div class="card-title">
        <span>收入成本</span>
        <el-select style="width: 120px" v-model="queryParams.accountPeriod" placeholder="" @change="getList">
          <template #label>
            <span>{{ accountPeriod }}</span>
          </template>
          <template v-for="(item, index) in statistics_period" :key="index">
            <el-option :label="item.label" :value="item.value"></el-option>
          </template>
        </el-select>
      </div>
    </template>

    <div class="card-content" v-loading="loading">
      <div class="card-content-item">
        <div>
          <div class="flex justify-items-center">
            <span>收入</span>
            <el-tooltip content=""
                        placement="top">
              <template #content>
              <span>
                所选期间内的营业收入，数据来源为利润表
              </span>
              </template>
              <el-icon>
                <Warning/>
              </el-icon>
            </el-tooltip>
          </div>
          <div class="bold">{{ formatAmount(resData.balance) }}</div>
        </div>
        <div class="small">
          <div>较上期：{{resData.balanceLast}}%</div>
          <div>较同期：{{resData.balanceLastYear}}%</div>
        </div>
      </div>
      <div class="card-content-item">
        <div>
          <div class="flex justify-items-center">
            <span>成本</span>
            <el-tooltip content=""
                        placement="top">
              <template #content>
              <span>
                所选期间内的营业成本，数据来源为利润表
              </span>
              </template>
              <el-icon>
                <Warning/>
              </el-icon>
            </el-tooltip>
          </div>
          <div class="bold">{{ formatAmount(resData.balanceOperatingCosts) }}</div>
          <div>毛利率：{{resData.balanceRatio}}%</div>
        </div>
        <div class="small">
          <div>较上期：{{resData.balanceRatioLast}}%</div>
          <div>较同期：{{resData.balanceRatioLastYear}}%</div>
        </div>
      </div>

      <div ref="chartRef" style="width: 100%; height: 300px"></div>
    </div>
  </el-card>
</template>


<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, computed, onMounted, onBeforeUnmount} from "vue";
import {useI18n} from 'vue-i18n'
import bookStore from "@/store/modules/bookStore";
import {getAccountPeriod} from "@/utils/Jinbooks";
import {Warning} from "@element-plus/icons-vue"
import {formatAmount} from "@/utils";
import * as echarts from 'echarts'
import {statisticsRevenueCost} from "@/api/dashboard"
import {BaseValue} from "@/types/FundBalance";

const {t} = useI18n()
const currBookStore = bookStore()
const {proxy} = getCurrentInstance()!;
const chartRef = ref(null)
let chartInstance: any = null
const {statistics_period} = proxy!.useDict("statistics_period");
const loading = ref(false)

interface DataState {
  queryParams: {
    bookId: string;
    accountPeriod: string;
    periodType: string;
    reportDate: string;
  };
  resData: {
    balance: number;
    balanceOperatingCosts: number;
    balanceLastYear: number;
    balanceLast: number;
    balanceRatio: number;
    balanceRatioLastYear: number;
    balanceRatioLast: number;
    balanceList: BaseValue[];
    balanceRatioList: BaseValue[];
  };
}

const data = reactive<DataState>({
  queryParams: {
    bookId: currBookStore.bookId,
    accountPeriod: "currentPeriod",
    periodType: "month",
    reportDate: currBookStore.termCurrent
  },
  resData: {
    balance: 0,
    balanceOperatingCosts: 0,
    balanceLastYear: 0,
    balanceLast: 0,
    balanceRatio: 0,
    balanceRatioLastYear: 0,
    balanceRatioLast: 0,
    balanceList: [],
    balanceRatioList: [],
  }
});
const {queryParams, resData} = toRefs(data);
const accountPeriod = computed(() => {
  return getAccountPeriod(queryParams, currBookStore)
})
// 初始化 chart（只执行一次）
const initChart = (option: any) => {
  if (chartRef.value && !chartInstance) {
    chartInstance = echarts.init(chartRef.value)
    chartInstance.setOption(option)
    window.addEventListener('resize', handleResize)
  } else {
    chartInstance.setOption(option, true); // true 表示不做合并，完全覆盖
  }
}
// 窗口大小变化自动 resize
const handleResize = () => {
  chartInstance.resize()
}
const getList = () => {
  loading.value = true
  const period = accountPeriod.value
  statisticsRevenueCost(queryParams.value).then((res: any) => {
    resData.value = res.data
    const option = {
      title: {
        text: '收入成本',
        left: 'left',
        top: "0%",
        textStyle: {fontSize: 14}
      },
      tooltip: {
        trigger: 'axis',
        axisPointer: {type: 'shadow'}
      },
      legend: {
        data: ['收入', '成本'],
        right: 0,
        top: "0%",
      },
      grid: {
        left: '10%',
        right: '10%',
        bottom: '31%',
        top: "23.5%",
      },
      xAxis: [
        {
          type: 'category',
          data: resData.value.balanceList.map(d => d.name),
          axisTick: {alignWithLabel: true}
        }
      ],
      yAxis: {
        type: 'value',
        name: '金额/万'
      },
      series: [
        {
          name: '收入',
          type: 'bar',
          data: resData.value.balanceList.map(d => ({
            value: d.value,
            itemStyle: {
              color: d.value < 0 ? 'red' : '#91ccff'
            }
          })),
          barWidth: 15
        },
        {
          name: '成本',
          type: 'bar',
          data: resData.value.balanceRatioList.map(d => ({
            value: d.value,
            itemStyle: {
              color: d.value < 0 ? 'red' : '#91ccff'
            }
          })),
          barWidth: 15
        }
      ]
    }
    initChart(option)
  }).finally(() => {
    loading.value = false
  })
}

onMounted(() => {
  getList()
})
onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  if(chartInstance){
    chartInstance.dispose()
    chartInstance = null
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
  justify-content: space-between;
  align-items: center;
}

.card-content {

  &-item {
    font-size: 0.9em;
    height: 46px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

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
