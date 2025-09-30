<template>
  <div>
    <el-dropdown trigger="click" @command="handleSetSize">
      <div class="size-icon--style">
        <svg-icon class-name="size-icon" icon-class="size" />
      </div>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item v-for="item of sizeOptions" :key="item.value" :disabled="size === item.value" :command="item.value">
            {{ item.label }}
          </el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script setup lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import modal from "@/plugins/modal";
import useAppStore from "@/store/modules/app";

const appStore: any = useAppStore();
const size: any = computed(() => appStore.size);
const route: any = useRoute();
const router: any = useRouter();
const {proxy} = getCurrentInstance()!;
const sizeOptions: any = ref([
  { label: "较大", value: "large" },
  { label: "默认", value: "default" },
  { label: "稍小", value: "small" },
]);

function handleSetSize(size: any): any {
  modal.loading("正在设置布局大小，请稍候...");
  appStore.setSize(size);
  setTimeout("window.location.reload()", 1000);
}
</script>

<style lang='scss' scoped>
@import "@/assets/styles/variables.module";

.size-icon--style {
  font-size: 18px;
  line-height: $base-navbar-height;
  padding-right: 7px;
}
</style>