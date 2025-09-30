<template>
  <el-container :class="classObj" class="app-wrapper" :style="{'--current-color': theme}">
    <el-header :height="variables.navBarHeight" :class="{'fixed-header':fixedHeader}">
      <Navbar/>
    </el-header>
    <el-container class="container-main">
      <el-aside :width="appStore.sidebar.opened ? variables.sideBarWidth : '60px'">
        <Sidebar v-if="!sidebar.hide" class="sidebar-container"/>
      </el-aside>
      <el-main>
        <app-main/>
        <settings ref="settingRef"/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import {computed, watch, watchEffect, ref, defineComponent} from "vue"
import variables from '@/assets/styles/variables.module.scss'
import {useWindowSize} from '@vueuse/core'
import Sidebar from "./components/Sidebar/index.vue"
import {AppMain, Navbar, Settings} from './components'

import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import {useRoute} from "vue-router";

const appStore = useAppStore()
const settingsStore = useSettingsStore()
const theme = computed(() => settingsStore.theme);
const sideTheme = computed(() => settingsStore.sideTheme);
const sidebar = computed(() => useAppStore().sidebar);
const device = computed(() => useAppStore().device);
const needTagsView = computed(() => settingsStore.tagsView);
const fixedHeader = computed(() => settingsStore.fixedHeader);
const route = useRoute();

const classObj = computed(() => ({
  hideSidebar: !sidebar.value.opened,
  openSidebar: sidebar.value.opened,
  withoutAnimation: sidebar.value.withoutAnimation,
  mobile: device.value === 'mobile'
}))

const {width, height} = useWindowSize();
const WIDTH = 992;

watch(() => device.value, () => {
  if (device.value === 'mobile' && sidebar.value.opened) {
    useAppStore().closeSideBar({withoutAnimation: false})
  }
})

watchEffect(() => {
  if (width.value - 1 < WIDTH) {
    useAppStore().toggleDevice('mobile')
    useAppStore().closeSideBar({withoutAnimation: true})
  } else {
    useAppStore().toggleDevice('desktop')
  }
})

function handleClickOutside() {
  useAppStore().closeSideBar({withoutAnimation: false})
}


// 定义组件类型
interface SettingsComponent {
  openSetting: () => void;
}

const settingRef = ref<InstanceType<typeof Settings> | null>(null);

function setLayout() {
  settingRef.value?.openSetting();
}

defineComponent({
  name: "Layout"
})
</script>

<style lang="scss" scoped>
@import "@/assets/styles/mixin.scss";
@import "@/assets/styles/variables.module.scss";

::v-deep(.el-header) {
  position: fixed;
  z-index: 1001;
  width: 100%;
}

.container-main {
  margin-top: $base-navbar-height;
}

.app-wrapper {

}

</style>