<template>
  <div class="login">
    <div class="top-box">
      <div class="title">
        <img :src="staticAppInfo.logo" alt=""/>
        {{ staticAppInfo.consoleTitle || t('appTitle') }}
      </div>
      <div class="right">
        <!--
        <el-tooltip content="语言选择" effect="dark" placement="bottom">
          <language class="right-menu-item hover-effect"></language>
        </el-tooltip>
        -->
      </div>
    </div>

    <el-form ref="loginRef" :model="loginForm" :rules="loginRules" class="login-form">
      <el-form-item prop="username">
        <el-input
            v-model="loginForm.username"
            type="text"
            auto-complete="off"
            :placeholder="t('login.textUsername')"
        >
          <template #prepend>
            <svg-icon icon-class="user2" color="#000000"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="loginForm.password"
            type="password"
            auto-complete="off"
            show-password
            :placeholder="t('login.textPassword')"
            @keyup.enter="handleLogin"
        >
          <template #prepend>
            <svg-icon icon-class="password2" color="#ffd700"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="code" v-if="captchaEnabled">
        <el-input
            v-model="loginForm.captcha"
            auto-complete="off"
            :placeholder="t('login.textCaptcha')"
            style="width: 63%"
            @keyup.enter="handleLogin"
        >
          <template #prepend>
            <svg-icon icon-class="validCode2" color="#000000"/>
          </template>
        </el-input>
        <div class="login-code">
          <img :src="codeUrl" @click="getCode" class="login-code-img" alt=""/>
        </div>
      </el-form-item>
      <!--      <div class="operater-wrapper">-->
      <!--        <el-checkbox v-model="loginForm.rememberMe">-->
      <!--          <span class="rememberMe">{{ $t('login.rememberMe') }}</span>-->
      <!--        </el-checkbox>-->
      <!--        <router-link to="/forgot" class="find-password">-->
      <!--          {{ t('forgot.title') }}-->
      <!--        </router-link>-->
      <!--      </div>-->
      <el-form-item style="width:100%;">
        <el-button
            class="login-btn"
            :loading="loading"
            type="primary"
            style="width:100%;"
            @click.prevent="handleLogin">
          <span v-if="!loading">{{ t("login.login") }}</span>
          <span v-else>{{ t("login.logging") }}</span>
        </el-button>
      </el-form-item>
      <el-form-item style="width:100%;" v-if="others.length > 0">
        <div class="other" style="width: 100%;display: flex;">
          其他方式
          <div class="item" v-for="other in others" @click="openOtherLogin(other.id)" :title="other.name"
               style="cursor: pointer">
            <img :src="privateImage(other.icon)" style="width: 28px;height: 28px" alt=""/>
          </div>
        </div>
      </el-form-item>
<!--      <div class="form-footer">
        <router-link to="/register" class="register-link">
          注册
        </router-link>
      </div>-->
    </el-form>
    <!--  底部  -->
    <Footer></Footer>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, watch} from "vue";
import {getCodeImg, loginPreGet, getThirdById} from "@/api/login";
import {privateImage} from "@/utils/Surpass";
import {ElMessage} from 'element-plus'
import Cookies from "js-cookie";
import {decrypt} from "@/utils/Jsencrypt";
import useUserStore from '@/store/modules/user'
import appStore from '@/store/modules/app'
import {useI18n} from 'vue-i18n'
import logoUrl from '@/assets/logo/logo.png'
import modal from "@/plugins/modal"

const {t} = useI18n()
import {getToken} from '@/utils/Auth'
import Footer from "@/components/Footer/index.vue"
import {useRoute, useRouter} from "vue-router";
import type {FormInstance, FormRules} from 'element-plus';

const userStore: any = useUserStore();
const route: any = useRoute();
const router: any = useRouter();
const {proxy} = getCurrentInstance()!;
const loginForm: any = ref({
  username: "",
  password: "",
  rememberMe: false,
  captcha: "",
  state: "",
  authType: 'normal'
});

const loginRules: any = reactive<FormRules>({
  username: [{required: true, trigger: "blur", message: t('login.textUsernameNotice')}],
  password: [{required: true, trigger: "blur", message: t('login.textPwdNotice')}],
  captcha: [{required: true, trigger: "change", message: t('login.textCodeNotice')}]
});
const state: any = ref("");
const staticAppInfo: any = ref({
  logo: logoUrl,
  consoleTitle: ""
});
const codeUrl: any = ref("");
const loading: any = ref(false);
// 验证码开关
const captchaEnabled: any = ref(false);
// 注册开关
const register: any = ref(false);
const redirect: any = ref("");

//其他登录方式
interface OtherItem {
  id: any;
  name: any;
  icon: any;
}

const others: any = ref<OtherItem[]>([]);

watch(route, (newRoute: any) => {
  redirect.value = newRoute.query && newRoute.query.redirect;
}, {immediate: true});

const loginRef: any = ref<FormInstance>();

function handleLogin(): any {
  if (!loginRef.value) return;
  loginRef.value.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      loginForm.value.state = state.value;

      // 调用action的登录方法
      userStore.login(loginForm.value).then((res: any) => {
        const query: any = route.query;
        const otherQueryParams: any = Object.keys(query).reduce((acc: any, cur: any) => {
          if (cur !== "redirect") {
            acc[cur] = query[cur];
          }
          return acc;
        }, {});
        if (redirect.value && redirect.value?.startsWith("http")) {
          location.replace(redirect.value)
        } else if (redirect.value) {
          router.push({path: redirect.value, query: otherQueryParams});
        } else {
          window.location.reload()
        }
      }).catch((err: any) => {
        console.error(err)
        ElMessage.error(err.message || "登录失败")
        loading.value = false;
        // 重新获取验证码
        if (captchaEnabled.value) {
          getCode();
        }
      }).finally(() => {
        loading.value = false
      });
    }
  });
}

function getCode(): any {
  getCodeImg(state.value, captchaEnabled.value).then((res: any) => {
    if (captchaEnabled.value) {
      codeUrl.value = res.data.image;
      state.value = res.data.state
    }
  });
}

function getCookie(): any {
  const username: any = Cookies.get("username");
  const password: any = Cookies.get("password");
  const rememberMe: any = Cookies.get("rememberMe");
  loginForm.value = {
    authType: "", captcha: "", state: "",
    username: username === undefined ? loginForm.value.username : username,
    password: password === undefined ? loginForm.value.password : decrypt(password),
    rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
  };
}

function getState(): any {
  loginPreGet().then((res: any) => {
    if (res.code === 0) {
      staticAppInfo.value = res.data.inst
      staticAppInfo.value.logo = logoUrl
      // import(`/${res.data.inst.logo}`).then((logo: any) =>  {
      //   staticAppInfo.value.logo = logo.default
      //   console.log(staticAppInfo.value)
      // })

      appStore().setAppInfo(staticAppInfo.value)
      state.value = res.data.state
      captchaEnabled.value = res.data.captcha
      if (captchaEnabled.value) {
        getCode();
      }
      // others.value = res.data.auths;
    }
  });

  // openFuncList().then((res: any) =>  {
  //   others.value = res.data.auths;
  // })
}

function openOtherLogin(id: any): any {
  getThirdById(id).then((res: any) => {
    if (res.code === 200) {
      const flag: any = window.open(res.data, '', "height=800, width=1400, top=100, left=100,toolbar=no, menubar=no, scrollbars=no, resizable=no, loca tion=no, status=no");
      const loop: any = setInterval(function () {
        if (flag?.closed) {
          clearInterval(loop);
          if (getToken()) {
            const query: any = route.query;
            const otherQueryParams: any = Object.keys(query).reduce((acc: any, cur: any) => {
              if (cur !== "redirect") {
                acc[cur] = query[cur];
              }
              return acc;
            }, {});
            router.push({path: redirect.value || "/", query: otherQueryParams});
          }
        }
      }, 3);
    } else {
      modal.msgError(res.message);
    }
  })
}

getState();
// getCookie();
</script>

<style lang='scss' scoped>
@use "sass:color";
.login {
  position: relative;
  overflow: hidden;
  height: 100%;
  width: 100%;
  color: #000000d9;
  background-image: url('../assets/images/login-bg.jpg');
  background-size: 100% 100%;
}

.login::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #f0f2f5;
  //background-size: cover;
  //background-position: center;
  //transform: scaleX(-1);
  z-index: -1; /* 确保背景图在内容后面 */
}

.top-box {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 0;
  font-weight: 600;
  font-size: 33px;
  font-family: Myriad Pro, Helvetica Neue, Arial, Helvetica, sans-serif;
  border-bottom: 1px solid #e5e5e5;
  min-height: 60px;
  text-shadow: 0 1px 0 #fff;

  .title {
    display: flex;
    justify-content: center;
    align-items: center;
    text-align: center;
    margin: 0 0 0 10%;

    img {
      width: 44px;
      margin-right: 10px;
    }
  }

  .right {
    margin: 0 10% 0 0;
  }
}

.login-form {
  margin: 70px auto;
  padding: 32px 20px 24px 20px;
  width: 380px;
  background-color: rgba(255, 255, 255, 0.7); // 半透明背景色
  border: 1px solid #eaeaea; // 边框
  border-radius: 10px;
  transition: box-shadow 0.3s ease; // 平滑过渡效果

  &:hover {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); // 鼠标悬停时的阴影
  }


  ::v-deep(.el-form-item__error) {
    color: #FF8888;
  }

  .el-input {
    height: 40px;

    ::v-deep(.el-input-group__prepend) {
      padding: 0 10px;
    }
  }

}

.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.login-code {
  width: 33%;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.login-code-img {
  height: 40px;
  padding-left: 12px;
}

.operater-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 14px;

  .find-password {
    cursor: pointer;
    font-size: 14px;
  }

  .rememberMe {
    font-size: 14px;
  }
}

.other {
  .item {
    margin: 0 6px;

    img {
      border-radius: 50%;
    }
  }

  .item:first-child {
    margin-left: 10px;
  }
}

.login-btn {
  height: 40px;
  padding: 6.4px 15px;
  font-size: 16px;
  border-radius: 2px;
}

$form-primary: #409EFF;

.form-footer {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;

  .register-link {
    color: $form-primary;
    font-weight: 500;
    font-size: 14px;
    cursor: pointer;
    transition: color 0.3s;

    &:hover {
      color: color.adjust(#409EFF, $lightness: -10%);
      text-decoration: underline;
    }
  }
}
</style>
