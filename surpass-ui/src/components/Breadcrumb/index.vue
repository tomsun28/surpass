<template>
  <el-breadcrumb class="app-breadcrumb" separator="/">
    <transition-group name="breadcrumb">
      <el-breadcrumb-item v-for="(item,index) in levelList" :key="item.path">
        <span v-if="index === levelList.length - 1">{{ item.meta.title }}</span>
        <a class="no-redirect" v-else @click.prevent="handleLink(item)">{{ item.meta.title }}</a>
      </el-breadcrumb-item>
    </transition-group>
  </el-breadcrumb>
</template>

<script setup lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ref, watchEffect} from "vue";

const route: any = useRoute();
const router: any = useRouter();
const levelList: any = ref<any>([])

function getBreadcrumb(): any {
  console.log('Breadcrumb: Current route:', route);
  console.log('Breadcrumb: Matched routes:', route.matched);
  
  const breadcrumbList: any = [];
  
  // 添加首页
  breadcrumbList.push({path: '/index', meta: {title: '首页'}});
  
  // 获取当前激活的菜单路径（与左侧菜单保持一致）
  const activeMenuPath = getActiveMenuPath();
  console.log('Breadcrumb: Active menu path:', activeMenuPath);
  
  if (activeMenuPath && activeMenuPath !== '/index') {
    // 根据激活菜单路径构建面包屑层级
    const activePathParts = activeMenuPath.split('/').filter((part: any) => part && part !== 'index');
    
    let currentPath = '';
    for (let i = 0; i < activePathParts.length; i++) {
      currentPath += '/' + activePathParts[i];
      
      // 在matched数组中查找对应的路由信息
      const matchedRoute = findRouteByPath(currentPath);
      
      if (matchedRoute && matchedRoute.meta && matchedRoute.meta.title) {
        breadcrumbList.push({
          path: currentPath,
          meta: {title: matchedRoute.meta.title},
          redirect: matchedRoute.redirect
        });
      } else {
        // 如果没有找到匹配的路由，从路径生成标题
        const title = activePathParts[i].charAt(0).toUpperCase() + activePathParts[i].slice(1);
        breadcrumbList.push({
          path: currentPath,
          meta: {title: title},
          redirect: 'noRedirect'
        });
      }
    }
  }
  
  // 如果当前页面是激活菜单的子页面，添加当前页面到面包屑
  if (route.path !== activeMenuPath && route.path.startsWith(activeMenuPath + '/')) {
    const matchedRoute = findRouteByPath(route.path);
    if (matchedRoute && matchedRoute.meta && matchedRoute.meta.title) {
      breadcrumbList.push({
        path: route.path,
        meta: {title: matchedRoute.meta.title},
        redirect: matchedRoute.redirect
      });
    } else {
      // 从路径生成当前页面标题
      const pathParts = route.path.split('/').filter((part: any) => part);
      const lastPart = pathParts[pathParts.length - 1];
      const title = lastPart.charAt(0).toUpperCase() + lastPart.slice(1);
      breadcrumbList.push({
        path: route.path,
        meta: {title: title},
        redirect: 'noRedirect'
      });
    }
  }
  
  // 过滤掉breadcrumb为false的路由
  levelList.value = breadcrumbList.filter((item: any) => {
    const matchedRoute = findRouteByPath(item.path);
    if (matchedRoute && matchedRoute.meta && matchedRoute.meta.breadcrumb === false) {
      return false;
    }
    return true;
  });
  
  console.log('Breadcrumb: Final breadcrumb list:', levelList.value);
}

function isDashboard(route: any): any {
  const name: any = route && route.name
  if (!name) {
    return false
  }
  return name.trim() === 'Index'
}

// 获取当前激活菜单路径（与左侧菜单保持一致）
function getActiveMenuPath(): string {
  const {meta, path} = route;
  
  // 如果路由设置了activeMenu，则使用activeMenu
  if (meta && meta.activeMenu) {
    return meta.activeMenu;
  }
  
  // 使用路由的matched数组来找到最合适的激活菜单
  if (route.matched && route.matched.length > 0) {
    // 从matched数组中查找第一个在侧边栏路由中存在的路径
    for (let i = route.matched.length - 1; i >= 0; i--) {
      const match = route.matched[i];
      if (match.path && match.path !== '/' && match.path !== '/index') {
        // 检查这个路径是否在侧边栏路由中
        if (findRouteByPath(match.path)) {
          return match.path;
        }
      }
    }
  }
  
  // 如果没有找到合适的激活菜单，使用当前路径
  return path;
}

// 根据路径查找路由信息
function findRouteByPath(targetPath: string): any {
  return route.matched.find((item: any) => item.path === targetPath);
}

function handleLink(item: any): any {
  const {redirect, path} = item
  console.log('Breadcrumb: Clicked on', item.meta.title, 'path:', path, 'redirect:', redirect)
  
  if (redirect && redirect !== 'noRedirect') {
    router.push(redirect)
    return
  }
  
  // 使用真实路径进行跳转
  if (path) {
    router.push(path)
  }
}

watchEffect(() => {
  if (route.path.startsWith('/redirect/')) {
    return
  }
  getBreadcrumb()
})
getBreadcrumb();
</script>

<style lang='scss' scoped>
@import "@/assets/styles/variables.module";

.app-breadcrumb.el-breadcrumb {
  display: inline-block;
  line-height: $base-navbar-height;
  margin-right: 8px;

  span, a {
    cursor: pointer;
  }

  .no-redirect {
    cursor: pointer;
    color: #97a8be;
  }
}
</style>