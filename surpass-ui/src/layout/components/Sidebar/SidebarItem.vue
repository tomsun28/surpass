<template>
  <div v-if="!item.hidden">
    <template
        v-if="hasOneShowingChild(item.children, item) && (!onlyOneChild.children || onlyOneChild.noShowingChildren) && !item.alwaysShow">
      <app-link v-if="onlyOneChild.meta" :to="resolvePath(onlyOneChild.path, onlyOneChild.query)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{ 'submenu-title-noDropdown': !isNest }">
          <svg-icon v-if="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"
                    :icon-class="onlyOneChild.meta.icon || (item.meta && item.meta.icon)"/>
          <template #title>
            <span class="menu-title" :title="hasTitle(onlyOneChild.meta.title)">
              {{ onlyOneChild.meta.title }}
            </span>
          </template>
        </el-menu-item>
      </app-link>
    </template>

    <el-sub-menu v-else ref="subMenu" :index="resolvePath(item.path)" teleported>
      <template v-if="item.meta" #title>
        <svg-icon v-if="item.meta && item.meta.icon" :icon-class="item.meta && item.meta.icon"/>
        <span class="menu-title" :title="hasTitle(item.meta.title)">{{ item.meta.title }}</span>
      </template>

      <sidebar-item
          v-for="(child, index) in item.children"
          :key="child.path + index"
          :is-nest="true"
          :item="child"
          :base-path="resolvePath(child.path)"
          class="nest-menu"
      />
    </el-sub-menu>
  </div>
</template>

<script setup lang="ts">
import {ref} from "vue"
import {isExternal} from '@/utils/Validate'
import AppLink from './Link'
import {getNormalPath} from '@/utils/Surpass'

const props = defineProps({
  // route object
  item: {
    type: Object,
    required: true
  },
  isNest: {
    type: Boolean,
    default: false
  },
  basePath: {
    type: String,
    default: ''
  }
})

const onlyOneChild = ref({
  children: null,
  meta: {
    title: null,
    icon: null,
  },
  path: null,
  noShowingChildren: null,
  query: null,
});

function hasOneShowingChild(children: any = [], parent: any) {
  if (!children) {
    children = [];
  }
  const showingChildren = children.filter((item: any) => {
    if (item.hidden) {
      return false
    } else {
      // Temp set(will be used if only has one showing child)
      onlyOneChild.value = item
      return true
    }
  })

  // When there is only one child router, the child router is displayed by default
  if (showingChildren.length === 1) {
    return true
  }

  // Show parent if there are no child router to display
  if (showingChildren.length === 0) {
    onlyOneChild.value = {...parent, path: '', noShowingChildren: true}
    return true
  }

  return false
}

function resolvePath(routePath: any, routeQuery?: any) {
  if (isExternal(routePath)) {
    return routePath
  }
  if (isExternal(props.basePath)) {
    return props.basePath
  }
  if (routeQuery) {
    let query = JSON.parse(routeQuery);
    return {path: getNormalPath(props.basePath + '/' + routePath), query: query}
  }
  return getNormalPath(props.basePath + '/' + routePath)
}

function hasTitle(title: any) {
  if (title.length > 5) {
    return title;
  } else {
    return "";
  }
}
</script>
