<template>
  <div class="navbar">
    <div class="left-main">
      <Logo class="logo" :collapse="false"/>
    </div>

    <div class="right-menu">
      <el-divider direction="vertical"></el-divider>
      <div class="right-menu-item">
        <ScreenFull id="screenfull" class="right-menu-item hover-effect"></ScreenFull>
      </div>
      <el-divider direction="vertical"></el-divider>
      <div class="right-menu-item avatar-box">
        <el-dropdown placement="bottom">
          <div class="avatar-wrapper">
            <!--<img :src="userStore.avatar" class="user-avatar" alt=""/>-->
            <span style="margin-left: 5px">{{ userStore.name }}</span>
            <span>({{ userStore.username }})</span>
          </div>
          <template #dropdown>
             <el-dropdown-menu> 
<!--              <el-dropdown-item> -->
<!--                <router-link to="/user/profile"> -->
<!--                  <svg-icon icon-class="user"></svg-icon>-->
<!--                  <span style="margin-left: 5px">个人中心</span>-->
<!--                </router-link>-->
<!--             </el-dropdown-item> -->
<!--              <el-dropdown-item>-->
<!--                <svg-icon icon-class="setting"></svg-icon>-->
<!--                <span style="margin-left: 5px">个人设置</span>-->
<!--              </el-dropdown-item>-->

<!--              <el-dropdown-item>-->
<!--                <router-link to="/exception/trigger">-->
<!--                  <svg-icon icon-class="close-circle"></svg-icon>-->
<!--                  <span style="margin-left: 5px">触发错误</span>-->
<!--                </router-link>-->
<!--              </el-dropdown-item>-->
              <el-dropdown-item>
                <CleanSession class="right-menu-item hover-effect"/>
              </el-dropdown-item>
              <el-dropdown-item style="border-top: 1px solid #888888;">
                <div @click="logout">
                  <svg-icon icon-class="logout"></svg-icon>
                  <span style="margin-left: 5px">退出登录</span>
                </div>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

import {ElMessageBox} from 'element-plus'
import ScreenFull from '@/components/Screenfull/index.vue'
import CleanSession from '@/components/CleanSession/index.vue'
import useAppStore from '@/store/modules/app'
import * as userService from "@/api/system/user";
import useUserStore from '@/store/modules/user'
import {logoutApi} from "@/api/login";
import Logo from "./Sidebar/Logo.vue";
import SvgIcon from "@/components/SvgIcon/index.vue";

const appStore = useAppStore()
const userStore = useUserStore()

function toggleSideBar() {
  appStore.toggleSideBar()
}

function handleCommand(command: any) {
  switch (command) {
    case "setLayout":
      setLayout();
      break;
    case "logout":
      logout();
      break;
    default:
      break;
  }
}

function logout() {
  ElMessageBox.confirm('确定注销并退出系统吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(() => {
    logoutApi().then((res: any) => {
      if (res.code === 0) {
        userStore.logOut().then(() => {
          window.location.reload()
        })
      }
    });

  }).catch(() => {
  });
}

const emits = defineEmits(['setLayout'])

function setLayout() {
  emits('setLayout');
}

</script>

<style lang='scss' scoped>
@import "@/assets/styles/variables.module";

.navbar {
  position: fixed;
  z-index: 1001;
  width: 100%;
  height: $base-navbar-height;
  overflow: hidden;
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;

  .left-main {
    position: relative;
    height: $base-navbar-height;

    .hamburger-container {
      line-height: $base-navbar-height;
      height: 100%;
      float: left;
      cursor: pointer;
      transition: background 0.3s;
      -webkit-tap-highlight-color: transparent;

      &:hover {
        background: rgba(0, 0, 0, 0.025);
      }
    }

    .logo {
      float: left;
      text-align: left;
      margin-right: 30px;
      width: auto;
    }
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    margin-right: 30px;
    display: inline-flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      padding: 0 8px;
      color: #000000;
      cursor: pointer;
      outline: none;
      transition: background-color .3s;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }

      .svg-icon {
        font-size: 16px;
      }
    }

    .avatar-box {
      height: $base-navbar-height;
      line-height: normal;
    }

    .avatar-wrapper {
      height: $base-navbar-height;

      display: flex;
      justify-content: flex-start;
      align-items: center;
      cursor: pointer;

      .user-avatar {
        width: 24px;
        height: 24px;
        border-radius: 50%;
      }
    }
  }
}
</style>
