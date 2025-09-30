<template>
  <div>
    <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper">
      <el-menu
          class="app-el-menu"
          :default-active="activeMenu"
          :collapse="isCollapse"
          :background-color="sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground"
          :text-color="sideTheme === 'theme-dark' ? variables.menuColor : variables.menuLightColor"
          :unique-opened="true"
          :active-text-color="theme"
          :collapse-transition="false"
          mode="vertical">
        <sidebar-item
            v-for="(route, index) in sidebarRouters"
            :key="route.path + index"
            :item="route"
            :base-path="isParentView(route) ? '' : route.path"
        />

        <el-menu-item index="toggleSide" @click="toggleSideBar">
          <el-tooltip :content="appStore.sidebar.opened ? '收缩' : '展开'">
            <hamburger :style="{marginLeft: appStore.sidebar.opened ? '80px': '0', padding: '0'}"
                       :is-active="appStore.sidebar.opened"/>
          </el-tooltip>
        </el-menu-item>
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script setup lang="ts">
import {computed, ref, onMounted} from "vue"
import {useRoute} from "vue-router";

import SidebarItem from './SidebarItem.vue'
import variables from '@/assets/styles/variables.module.scss'
import useAppStore from '@/store/modules/app'
import useSettingsStore from '@/store/modules/settings'
import usePermissionStore from '@/store/modules/permission'
import Hamburger from "@/components/Hamburger/index.vue";

const route = useRoute();
const appStore = useAppStore()
const settingsStore = useSettingsStore()
const permissionStore = usePermissionStore()

const sidebarRouters = computed(() => permissionStore.sidebarRouters);
const showLogo = computed(() => settingsStore.sidebarLogo);
const sideTheme = computed(() => settingsStore.sideTheme);
const theme = computed(() => settingsStore.theme);
const isCollapse = computed(() => !appStore.sidebar.opened);

const activeMenu = computed(() => {
  const {meta, path} = route;
  return path;
})

function toggleSideBar() {
  appStore.toggleSideBar()
}

const isParentView = (route: any) => {
  if (!route.raw) {
    return false
  }

  return !route.raw.requestUrl
}

onMounted(() => {
  console.log(sidebarRouters.value)
})
</script>
<style scoped lang="scss">

</style>