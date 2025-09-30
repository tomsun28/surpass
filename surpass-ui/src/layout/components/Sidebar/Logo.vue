<template>
  <div class="sidebar-logo-container" :class="{ 'collapse': collapse }"
       :style="{ backgroundColor: sideTheme === 'theme-dark' ? variables.menuBackground : variables.menuLightBackground }">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" alt=""/>
        <h1 v-else class="sidebar-title"
            :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">
          {{ appTitle }}</h1>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <img v-if="logo" :src="logo" class="sidebar-logo" alt=""/>
        <h1 class="sidebar-title"
            :style="{ color: sideTheme === 'theme-dark' ? variables.logoTitleColor : variables.logoLightTitleColor }">
          {{ appTitle }}</h1>
      </router-link>
    </transition>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from "vue"
import variables from '@/assets/styles/variables.module.scss'
import useSettingsStore from '@/store/modules/settings'
import useAppStore from '@/store/modules/app'

defineProps({
  collapse: {
    type: Boolean,
    required: true
  }
})

const appStore = useAppStore()
const settingsStore = useSettingsStore();
const sideTheme = computed(() => settingsStore.sideTheme);
const appTitle = computed(() => appStore.appTitle);
const logo = computed(() => appStore.logo);
</script>

<style lang="scss" scoped>
@import "@/assets/styles/variables.module";

.sidebarLogoFade-enter-active {
  transition: opacity 1.5s;
}

.sidebarLogoFade-enter,
.sidebarLogoFade-leave-to {
  opacity: 0;
}

.sidebar-logo-container {
  position: relative;
  width: 100%;
  height: $base-navbar-height;
  line-height: $base-navbar-height;
  background: #2b2f3a;
  text-align: center;
  overflow: hidden;

  & .sidebar-logo-link {
    height: 100%;
    width: 100%;

    & .sidebar-logo {
      width: 50px;
      height: 50px;
      vertical-align: middle;
    }

    & .sidebar-title {
      display: inline-block;
      margin: 0;
      color: #fff;
      line-height: $base-navbar-height;
      font-size: 20px;
      font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      vertical-align: middle;
      font-weight: bolder;
      letter-spacing: 2px;
    }
  }

  &.collapse {
    .sidebar-logo {
      margin-right: 0;
    }
  }
}
</style>