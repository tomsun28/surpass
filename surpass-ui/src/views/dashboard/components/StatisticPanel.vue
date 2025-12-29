<template>
  <el-row :gutter="20" class="statistic-panel">
    <el-col :xs="12" :sm="4" v-for="stat in statistics" :key="stat.title">
      <StatisticCard
        :title="stat.title"
        :value="stat.value"
        :icon="stat.icon"
        :color="stat.color"
        :growth="stat.growth"
      />
    </el-col>
  </el-row>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import StatisticCard from './StatisticCard.vue'

interface StatisticItem {
  title: string
  value: number
  icon: string
  color: string
  growth?: number
}

interface Props {
  statistics: StatisticItem[]
}

defineProps<Props>()
</script>

<style scoped lang="scss">
.statistic-panel {
  margin-bottom: 24px;
}
</style>