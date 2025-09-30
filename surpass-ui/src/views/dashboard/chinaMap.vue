<template>
  <div class="chinaMap" ref="chartRef" style="width: 100%;height: 500px;"></div>
</template>

<script lang="ts" setup>
import {ref, onMounted, watch} from 'vue';

import * as echarts from 'echarts'
import chinaJson from "@/assets/china-map.json"

const props = defineProps({
  data: {
    type: Object,
    default: () => ({}),
  }
});

const chart = ref<echarts.ECharts | null>(null);
const chartRef = ref<HTMLDivElement | null>(null);

const getOption = () => {
  return {
    series: [{
      type: 'map',
      map: 'china',
      layoutCenter: ['50%', '60%'], // 地图布局中心点
      layoutSize: 470,
      label: {
        show: true,
        color: '#ffffff',
        fontSize: 10,
      },
      itemStyle: {
        areaColor: '#eee',
        borderColor: '#c0c0c0',
      },
      roam: true,
      zoom: 1.2,
      emphasis: {
        label: {
          color: '#fff',
          fontSize: 12,
          fontWeight: 'bold'
        },
        itemStyle: {
          areaColor: '#FFdf33',
          borderColor: '#c0c0c0',
          borderWidth: 2
        }
      },
      data: props.data.reportProvince.map((item: any) => {
        return {
          name: item.reportName,
          value: item.reportCount
        }
      })
    }],
    visualMap: [{
      type: 'piecewise',
      show: true,
      pieces: [
        {min: 0, max: 100},
        {min: 100, max: 200},
        {min: 200, max: 300},
        {min: 300, max: 400},
        {min: 400, max: 500},
        {min: 500},
      ],
      textStyle: {
        color: '#828994'
      },
      itemWidth: 64, // 每个图元的宽度
      itemHeight: 12,
      top: "35%",
      left: "10",
      inRange: {
        borderRadius: 4,
        color: [
          '#9fb5ea',
          '#e6ac53',
          '#74e2ca',
          '#85daef',
          '#9feaa5',
          '#5475f5',
        ]
      },
    }],
    tooltip: {
      trigger: 'item',  //数据项图形触发
      backgroundColor: "#fff",
      borderWidth: 0,
      formatter: function (params: any) {
        // params 包含了数据项的所有信息
        const name = params.name || '未知区域';
        const value = params.value != null ? params.value : '无数据';
        return `🗺 地区：<b>${name}</b><br/>📊 数据：<b>${value || 0}次</b>`;
      }
    },
    toolbox: {
      show: false,
      orient: 'vertical',
      left: 'right',
      bottom: '0',
      feature: {
        restore: {},
        saveAsImage: {}
      }
    },
  }
}

const initCharts = () => {
  if (chartRef.value) {
    chart.value = echarts.init(chartRef.value);
    window.addEventListener("resize", function () {
      chart.value?.resize();
    })
    chart.value.setOption(getOption())
  }
}

watch(() => props.data, () => {
  if (chart.value) {
    chart.value.setOption(getOption())
  }
}, {immediate: true});

onMounted(() => {
  echarts.registerMap('china', chinaJson as any);
  initCharts();
});
</script>
