<template>
  <div class="slider-bar">
    <!-- 条形图 -->
    <div class="bar-container" :style="{ borderRadius: borderRadius }">
      <div class="bar-progress" :style="progressStyle"></div>
      <div class="bar-divider"></div>
    </div>

    <!-- 标签 -->
    <div class="bar-labels">
      <div>{{ leftLabel }}：{{ formatNumber(leftValue) }}</div>
      <div>{{ rightLabel }}：{{ formatNumber(rightValue) }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
import {computed} from 'vue'

const props = defineProps({
  leftValue: {type: Number, required: true},
  rightValue: {type: Number, required: true},
  leftLabel: {type: String, default: '收入'},
  rightLabel: {type: String, default: '支出'},
  leftColorStart: {type: String, default: '#5AC8FA'},
  leftColorEnd: {type: String, default: '#007AFF'},
  borderRadius: {type: String, default: '9999px'},
})

const leftRatio = computed(() => {
  const total = props.leftValue + props.rightValue
  return total === 0 ? 0 : (props.leftValue / total) * 100
})

const progressStyle = computed(() => ({
  width: `${leftRatio.value}%`,
  background: `linear-gradient(to right, ${props.leftColorStart}, ${props.leftColorEnd})`,
  borderRadius: props.borderRadius,
}))

const formatNumber = (n: any) =>
    n.toLocaleString(undefined, {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2,
    })
</script>

<style scoped>
.slider-bar {
  width: 100%;
}

.bar-container {
  position: relative;
  height: 8px;
  background-color: #e5e7eb; /* 灰色背景 */
  overflow: hidden;
}

.bar-progress {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  transition: width 0.7s ease-in-out;
}

.bar-divider {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 50%;
  width: 1px;
  background-color: white;
  transform: translateX(-50%);
}

.bar-labels {
  display: flex;
  justify-content: space-between;
  margin-top: 6px;
  font-size: 14px;
  color: #4b5563;
}
</style>
