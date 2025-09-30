<template>
  <div>
    <svg-icon icon-class="question" @click="goto" />
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import modal from "@/plugins/modal";
const url: any = ref('http://doc.ruoyi.vip/ruoyi-vue');

function goto(): any {
  window.open(url.value)
}
</script>