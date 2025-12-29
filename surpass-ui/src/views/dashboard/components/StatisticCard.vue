<template>
  <div class="statistic-card" :style="{ backgroundColor: color }">
    <div class="statistic-content">
      <div class="statistic-icon">
        <svg-icon :icon-class="icon" class-name="icon" />
      </div>
      <div class="statistic-info">
        <div class="statistic-value">
          <AutoCounter
              :startAmount="0"
              :endAmount="value"
              :duration="2000"
              :autoinit="true"
              class="value"
          />
        </div>
        <div class="statistic-title">{{ title }}</div>
      </div>
    </div>
    <div v-if="growth !== undefined" class="statistic-growth">
      <span :class="['growth-text', growth >= 0 ? 'positive' : 'negative']">
        {{ growth >= 0 ? '↑' : '↓' }} {{ Math.abs(growth) }}%
      </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue'
import AutoCounter from 'vue3-autocounter'

interface Props {
  title: string
  value: number
  icon: string
  color: string
  growth?: number
}

defineProps<Props>()
</script>

<style scoped lang="scss">
.statistic-card {
  border-radius: 8px;
  padding: 20px;
  color: #fff;
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;

  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.15);
  }

  .statistic-content {
    display: flex;
    align-items: center;
    gap: 16px;

    .statistic-icon {
      .icon {
        font-size: 40px;
        opacity: 0.9;
      }
    }

    .statistic-info {
      flex: 1;

      .statistic-value {
        .value {
          font-size: 32px;
          font-weight: bold;
          line-height: 1.2;
        }
      }

      .statistic-title {
        font-size: 14px;
        opacity: 0.9;
        margin-top: 4px;
      }
    }
  }

  .statistic-growth {
    margin-top: 8px;
    text-align: right;

    .growth-text {
      font-size: 12px;
      padding: 2px 8px;
      border-radius: 12px;
      background-color: rgba(255, 255, 255, 0.2);

      &.positive {
        color: #FFFFFF;
      }

      &.negative {
        color: #333333;
      }
    }
  }
}
</style>
