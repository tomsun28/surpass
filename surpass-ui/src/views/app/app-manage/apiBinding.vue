<template>
  <div class="layout-container">
    <!-- 左侧菜单 -->
    <div class="side-menu">
      <div class="menu-header">
        <button class="back-btn" @click="handleBack">
          <svg class="back-icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M19 12H5M12 19l-7-7 7-7"/>
          </svg>
        </button>
        <h2 class="menu-title">应用 - {{appName}}</h2>
      </div>

      <div class="menu-list">
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
import Menu from './auth/Menu.vue'
import Role from './auth/Role.vue'
import {useRoute, useRouter} from "vue-router";

const tabs = [
  { key: 'api', label: 'Api', component: Api, icon: 'Link' },
  { key: 'menu', label: '菜单', component: Menu, icon: 'menu' },
  { key: 'role', label: '授权', component: Role, icon: 'role' }
]

const active = ref('api')
const route = useRoute();
const router = useRouter();
const appId: any = ref(undefined);
const appName: any = ref(undefined);

const activeComponent = computed(() => {
  return tabs.find(t => t.key === active.value)?.component
})

const handleBack = () => {
  router.back()
  // 或者跳转到指定页面: router.push('/app-list')
}

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
  display: flex;
  flex-direction: column;
}

.menu-header {
  padding: 12px 8px;
  border-bottom: 1px solid #e4e7ed;
  flex-shrink: 0;
}

.back-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 12px;
  margin-bottom: 12px;
  background: #f5f7fa;
  border: 1px solid #dcdfe6;
  border-radius: 6px;
  cursor: pointer;
  transition: all 0.2s;
  color: #606266;
  font-size: 14px;
  width: 100%;

  &:hover {
    background: #ecf5ff;
    border-color: #409eff;
    color: #409eff;
  }
}

.back-icon {
  width: 18px;
  height: 18px;
}

.menu-title {
  text-align: center;
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  word-wrap: break-word;
  word-break: break-all;
  line-height: 1.4;
}

.menu-list {
  flex: 1;
  overflow-y: auto;
  padding: 8px;
}

.menu-item {
  height: 65px;
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
</style>
