<template>
  <div @click="toggle">
    <svg-icon :icon-class="isFullscreen ? 'exit-fullscreen' : 'fullscreen'"/>
    <span style="margin-left: 5px">{{ isFullscreen ? "退出全屏" : '全屏' }}</span>
  </div>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import {useFullscreen} from '@vueuse/core'

const {isFullscreen, toggle} = useFullscreen();

defineComponent({name: "ScreenFull"})
</script>

<style lang='scss' scoped>
.screenfull-svg {
  display: inline-block;
  cursor: pointer;
  fill: #5a5e66;
  width: 20px;
  height: 20px;
  vertical-align: 10px;
}
</style>