<template>
  <div class="left-menu-box">
    <el-scrollbar :class="sideTheme" wrap-class="scrollbar-wrapper">
      <!-- 加载状态 -->
      <div v-if="!permissionStore.routesLoaded || !sidebarRouters || sidebarRouters.length === 0"
           class="loading-container">
        <el-skeleton :rows="5" animated/>
      </div>

      <!-- 菜单内容 -->
      <el-menu
          v-else
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
const sideTheme = computed(() => settingsStore.sideTheme);
const theme = computed(() => settingsStore.theme);
const isCollapse = computed(() => !appStore.sidebar.opened);

const activeMenu = computed(() => {
  const {meta, path, matched} = route;

  // 确保路由数据已加载完成
  if (!permissionStore.routesLoaded || !sidebarRouters.value || sidebarRouters.value.length === 0) {
    console.log('Sidebar: Routes not loaded yet');
    return '';
  }

  // 如果路由设置了activeMenu，则使用activeMenu，否则使用当前路径
  if (meta && meta.activeMenu) {
    console.log('Sidebar: Using activeMenu from meta:', meta.activeMenu, path);
    return meta.activeMenu;
  }

  console.log('Sidebar: Current path:', path);
  console.log('Sidebar: Route matched:', matched);

  // 使用路由的matched数组来找到最合适的激活菜单
  if (matched && matched.length > 0) {
    // 从matched数组中查找第一个在侧边栏路由中存在的路径
    for (let i = matched.length - 1; i >= 0; i--) {
      const match = matched[i];
      if (match.path && match.path !== '/') {
        // 检查这个路径是否在侧边栏路由中
        const findPathInSidebar = (routes: any, targetPath: string): boolean => {
          for (const routeItem of routes) {
            if (routeItem.path === targetPath) {
              return true;
            }
            if (routeItem.children && routeItem.children.length > 0) {
              if (findPathInSidebar(routeItem.children, targetPath)) {
                return true;
              }
            }
          }
          return false;
        };

        if (findPathInSidebar(sidebarRouters.value, match.path)) {
          console.log('Sidebar: Found matching path in sidebar:', match.path);
          return match.path;
        }
      }
    }
  }

  // 如果matched中没有找到合适的路径，使用改进的路径匹配算法
  const findBestMatch = (routes: any, targetPath: string): string => {
    let bestMatch = '';
    let bestScore = 0;

    const traverseRoutes = (routeList: any, basePath = '') => {
      for (const routeItem of routeList) {
        const fullPath = basePath + routeItem.path;
        
        // 计算匹配分数
        let score = 0;
        
        // 精确匹配得分最高
        if (fullPath === targetPath) {
          score = 100;
        }
        // 路径前缀匹配
        else if (targetPath.startsWith(fullPath + '/')) {
          score = 50 + fullPath.length;
        }
        // 路径包含匹配
        else if (targetPath.includes(fullPath)) {
          score = 25 + fullPath.length;
        }

        console.log('Sidebar: Checking route:', fullPath, 'against:', targetPath, 'score:', score);

        if (score > bestScore) {
          bestScore = score;
          bestMatch = fullPath;
        }

        // 递归检查子路由
        if (routeItem.children && routeItem.children.length > 0) {
          traverseRoutes(routeItem.children, fullPath + '/');
        }
      }
    };

    traverseRoutes(routes);
    
    if (bestMatch) {
      console.log('Sidebar: Best match found:', bestMatch, 'with score:', bestScore);
      return bestMatch;
    }
    
    console.log('Sidebar: No suitable match found, using target path:', targetPath);
    return targetPath;
  };

  const result = findBestMatch(sidebarRouters.value, path);
  console.log('Sidebar: Final active menu:', result);
  return result;
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
  console.log('Sidebar mounted, routes:', sidebarRouters.value)
})

// 监听路由变化，确保菜单激活状态正确
watch(() => route.path, (newPath, oldPath) => {
  console.log('Sidebar: Route changed from', oldPath, 'to', newPath)
  console.log('Sidebar: Current activeMenu would be:', activeMenu.value)
})

// 监听路由加载完成状态
watch(() => permissionStore.routesLoaded, (loaded) => {
  if (loaded) {
    console.log('Sidebar: Routes loaded, current path:', route.path)
    console.log('Sidebar: Active menu should be:', activeMenu.value)
  }
})
</script>
<style scoped lang="scss">
@import "@/assets/styles/variables.module";

.loading-container {
  padding: 20px;
}
</style>