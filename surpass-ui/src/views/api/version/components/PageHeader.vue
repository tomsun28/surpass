<template>
  <div class="page-header">
    <div class="header-content">
      <div class="header-info">
        <span class="page-description" @click="handleBack">
          <svg-icon icon-class="left"></svg-icon>
          <span>返回</span>
        </span>
      </div>
    </div>
  </div>
</template>

<script setup>
import SvgIcon from "@/components/SvgIcon/index.vue";

const emit = defineEmits(['back']);

const handleBack = () => {
  emit('back');
};
</script>

<style scoped lang="scss">
.page-header {
  background: #fff;
  padding: 12px;
  border-bottom: 1px solid #e4e7ed;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin: 0 auto;
}

.header-info {
  flex: 1;
}

.page-description {
  color: #909399;
  font-size: 14px;
  cursor: pointer;
}
</style>