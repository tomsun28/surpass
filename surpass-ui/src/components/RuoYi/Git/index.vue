<template>
  <div>
    <svg-icon icon-class="github" @click="goto" />
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import modal from "@/plugins/modal";
const url: any = ref('https://gitee.com/y_project/RuoYi-Vue');

function goto(): any {
  window.open(url.value)
}
</script>
