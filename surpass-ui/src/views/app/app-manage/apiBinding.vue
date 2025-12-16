<template>
  <div class="layout-container">
    <!-- 左侧菜单 -->
    <div class="side-menu">
      <h2 class="menu-title">应用 - {{appName}}</h2>
      <div
          v-for="item in tabs"
          :key="item.key"
          :class="['menu-item', { active: active === item.key }]"
          @click="active = item.key"
      >
        <component :is="item.icon" class="menu-icon" />
        <span>{{ item.label }}</span>
      </div>
    </div>

    <!-- 右侧内容 -->
    <div class="content-area">
      <component :is="activeComponent" :app-id="appId"/>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed, ref, onMounted} from 'vue'
import Api from './auth/Api.vue'
import Config from './auth/Config.vue'
import Role from './auth/Role.vue'
import {useRoute} from "vue-router";

const tabs = [
  { key: 'api', label: 'API', component: Api, icon: 'Link' },
  { key: 'config', label: '菜单', component: Config, icon: 'setting' },
  { key: 'role', label: '授权', component: Role, icon: 'role' }
]

const active = ref('api')
const route = useRoute();
const appId: any = ref(undefined);
const appName: any = ref(undefined);

const activeComponent = computed(() => {
  return tabs.find(t => t.key === active.value)?.component
})

// 生命周期
onMounted(() => {
  const q = route.query.appId
  const w = route.query.appName
  appId.value = typeof q === 'string' ? q : appId.value
  appName.value = typeof w === 'string' ? w : appName.value
})
</script>

<style scoped lang="scss">
.layout-container {
  display: flex;
  height: 100%;
  background: #f5f7fa;
}

.side-menu {
  width: 200px;
  background: #fff;
  box-shadow: 2px 0 8px rgba(0,0,0,0.05);
  padding: 8px;
}

.menu-item {
  height: 70px;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  margin-bottom: 4px;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.2s;
  color: #606266;
  font-size: 18px;

  &:hover {
    background: #f5f7fa;
  }

  &.active {
    background: dodgerblue;
    color: #fff;
    box-shadow: 0 2px 8px rgba(64, 158, 255, 0.3);

    .menu-icon {
      color: #fff;
    }
  }
}

.menu-icon {
  width: 25px;
  height: 25px;
  color: #606266;
}

.content-area {
  flex: 1;
  padding: 20px;
  overflow: auto;
}

.menu-title {
  text-align: center;
  padding: 16px 8px;
  margin: 0 0 12px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  word-wrap: break-word;
  word-break: break-all;
  line-height: 1.4;
  border-bottom: 1px solid #e4e7ed;
}
</style>
