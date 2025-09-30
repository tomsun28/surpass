<template >
  <router-view />
</template>

<script lang="ts" setup>
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";

defineComponent({
  name: "ParentView",
})
</script>
