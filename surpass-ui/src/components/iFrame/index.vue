<template>
  <div v-loading="loading" :style="'height:' + height">
    <iframe 
      :src="url" 
      frameborder="no" 
      style="width: 100%; height: 100%" 
      scrolling="auto" />
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import modal from "@/plugins/modal";
const props: any = defineProps({
  src: {
    type: String,
    required: true
  }
})

const height: any = ref(document.documentElement.clientHeight - 94.5 + "px;")
const loading: any = ref(true)
const url: any = computed(() => props.src)

onMounted(() => {
  setTimeout(() => {
    loading.value = false;
  }, 300);
  window.onresize = function temp(): any {
    height.value = document.documentElement.clientHeight - 94.5 + "px;";
  };
})
</script>
