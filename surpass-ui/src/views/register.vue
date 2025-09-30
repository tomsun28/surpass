<template>
  <div class="register">
    <div class="top-box">
      <div class="title">
        <img :src="staticAppInfo.logo" alt=""/>
        {{ staticAppInfo.consoleTitle || t('appTitle') }}
      </div>
      <div class="right">
      </div>
    </div>

    <el-form ref="registerRef" :model="registerForm" :rules="registerRules" class="register-form">
      <el-form-item prop="instName">
        <el-input
            v-model="registerForm.instName"
            placeholder="机构名称"
            @keyup.enter="handleRegister"
            class="input-with-select"
        >
          <template #prepend>
            <el-select v-model="registerForm.instType"
                       size="large"
                       style="width: 90px;margin: 0">
              <el-option
                  v-for="item in inst_types"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="username">
        <el-input
            v-model="registerForm.username"
            type="text"
            auto-complete="off"
            placeholder="手机号"
        >
          <template #prepend>
            <svg-icon icon-class="user"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
            v-model="registerForm.password"
            type="password"
            auto-complete="off"
            show-password
            placeholder="密码"
            @keyup.enter="handleRegister"
        >
          <template #prepend>
            <svg-icon icon-class="password"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="confirmPassword">
        <el-input
            v-model="registerForm.confirmPassword"
            type="password"
            auto-complete="off"
            show-password
            placeholder="确认密码"
            @keyup.enter="handleRegister"
        >
          <template #prepend>
            <svg-icon icon-class="password"/>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item prop="email">
          <el-input
              v-model="registerForm.email"
              type="text"
              auto-complete="off"
              placeholder="邮箱"
          >
            <template #prepend>
              <el-icon><Message /></el-icon>
            </template>
          </el-input>
      </el-form-item>
      <el-form-item prop="captcha" v-if="captchaEnabled">
        <el-input
            v-model="registerForm.captcha"
            auto-complete="off"
            placeholder="验证码"
            style="width: 63%"
            @keyup.enter="handleRegister"
        >
          <template #prepend>
            <svg-icon icon-class="validCode2" color="#000000"/>
          </template>
        </el-input>
        <div class="register-code">
          <img :src="codeUrl" @click="getCode" class="register-code-img" alt=""/>
        </div>
      </el-form-item>
      <el-form-item prop="emailOtp">
        <el-input v-model="registerForm.emailOtp" auto-complete="off" placeholder="邮箱验证码">
          <template #prepend>
            <el-icon><Clock /></el-icon>
          </template>
          <template #append>
            <el-button slot="append" @click="sendCodeEmail" :disabled="!canClickEmail" class="send-btn">
              {{ contentEmail }}
            </el-button>
          </template>
        </el-input>
      </el-form-item>
      <el-form-item style="width:100%;">
        <el-button
            :loading="loading"
            type="primary"
            style="width:100%;"
            @click.prevent="handleRegister"
            class="register-btn"
        >
          <span v-if="!loading">注 册</span>
          <span v-else>注 册 中...</span>
        </el-button>
        <div style="float: right;">
          <router-link class="link-type" :to="'/login'">使用已有账户登录</router-link>
        </div>
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <!--  底部  -->
    <Footer></Footer>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive} from "vue";
import {ElForm, ElMessageBox} from 'element-plus';
import {getCodeImg, loginPreGet, register, sendEmailCode} from '@/api/login';
import {useRouter} from 'vue-router';
import {useI18n} from "vue-i18n";
import logoUrl from "@/assets/logo/logo.png";
import appStore from "@/store/modules/app";
import Footer from "@/components/Footer/index.vue";
import {validateFields} from "@/api/commonApi";
import modal from "@/plugins/modal";


const router: any = useRouter();
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {inst_types}
    = proxy?.useDict("inst_types");
const staticAppInfo: any = ref({
  logo: logoUrl,
  consoleTitle: ""
});
const state: any = ref("");
const registerRef = ref<InstanceType<typeof ElForm> | null>(null);
const totalTimeEmail = ref(60);
const canClickEmail = ref(true);
const contentEmail = ref("发送验证码");
const tempUsername = ref("");


interface RegisterForm {
  username: string;
  password: string;
  confirmPassword: string;
  captcha: string;
  instType: number;
  instName: string;
  emailOtp: string;
  email: string;
}

const registerForm: any = reactive<RegisterForm>({
  username: '',
  password: '',
  confirmPassword: '',
  captcha: '',
  instType: 0,
  instName: '',
  emailOtp: '',
  email: ''
});

const equalToPassword: any = (rule: any, value: string, callback: Function) => {
  if (registerForm.password !== value) {
    callback(new Error('两次输入的密码不一致'));
  } else {
    callback();
  }
};

const registerRules: any = {
  username: [
    { required: true, trigger: 'blur', message: '请输入您的手机号' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的11位手机号', trigger: 'blur' }
  ],
  password: [
    {required: true, trigger: 'blur', message: '请输入您的密码'},
    {min: 5, max: 20, message: '用户密码长度必须介于 5 和 20 之间', trigger: 'blur'},
    {pattern: /^[^<>"'|\\]+$/, message: '不能包含非法字符：< > \" \' \\\ |', trigger: 'blur'}
  ],
  confirmPassword: [
    {required: true, trigger: 'blur', message: '请再次输入您的密码'},
    {validator: equalToPassword, trigger: 'blur'}
  ],
  email: [
    {required: true, message: '请输入邮箱地址', trigger: "blur"},
    {validator: checkEmail, trigger: 'blur'}
  ],
  captcha: [{required: true, trigger: 'blur', message: '请输入图形验证码'}],
  emailOtp: [{required: true, trigger: 'blur', message: '请输入邮箱验证码'}],
  instName: [{required: true, trigger: 'blur', message: '请输入机构名称'}],
};

const codeUrl: any = ref('');
const loading: any = ref(false);
const captchaEnabled: any = ref(false);

function handleRegister() {
  let fields = ['username', 'password', 'confirmPassword', 'instName', 'email', 'emailOtp']
  validateFields(proxy, fields, 'registerRef').then((valid: boolean) => {
    if (valid) {
      loading.value = true;
      const data = {
        ...registerForm,
        tempUsername: tempUsername.value
      }
      register(data).then((res: any) => {
        const username: any = registerForm.username;
        ElMessageBox.alert(`<font color='red'>恭喜你，您的账号 ${username} 注册成功！</font>`, '系统提示', {
          dangerouslyUseHTMLString: true,
          type: 'success',
        }).then(() => {
          router.push('/login');
        }).catch(() => {
        });
      }).catch(() => {
        loading.value = false;
        if (captchaEnabled.value) {
          getCode();
        }
      });
    }
  });
}

function getState(): any {
  loginPreGet().then((res: any) => {
    if (res.code === 0) {
      staticAppInfo.value = res.data.inst
      staticAppInfo.value.logo = logoUrl

      appStore().setAppInfo(staticAppInfo.value)
      state.value = res.data.state
      captchaEnabled.value = res.data.captcha
      if (captchaEnabled.value) {
        getCode();
      }
    }
  });
}

function getCode() {
  getCodeImg(state.value, captchaEnabled.value).then((res: any) => {
    if (captchaEnabled.value) {
      codeUrl.value = res.data.image;
      state.value = res.data.state
    }
  });
}

function sendCodeEmail() {
  let fields = ['captcha', 'email'];
  let params = {
    _allow_anonymous: true,
    email: registerForm.email,
    state: state.value,
    captcha: registerForm.captcha,
  };
  sendCodeCommon(fields, params, sendEmailCode, startCountdownEmail);
}

function sendCodeCommon(fields: any, params: any, sendFn: any, startCountdownFn: any) {
  validateFields(proxy, fields, 'registerRef').then((valid: boolean) => {
    if (valid) {
      canClickEmail.value = false;
      sendFn(params)
          .then((res: any) => {
            if (res.code === 0) {
              startCountdownFn();
              tempUsername.value = res.message;
              modal.msgSuccess("发送成功");
            }
          })
          .catch((err: any) => {
            canClickEmail.value = true;
            if (err.code === 103) {
              modal.msgError("图形验证码错误");
            } else {
              modal.msgError("发送失败");
            }
            // 重新获取验证码
            if (captchaEnabled.value) {
              getCode();
            }
          });
    }
  });
}

function startCountdownEmail() {
  let totalTimeTemp = totalTimeEmail.value;
  canClickEmail.value = false;
  const countdownInterval = setInterval(() => {
    totalTimeTemp--;
    contentEmail.value = totalTimeTemp + "s后重新发送";
    if (totalTimeTemp < 0) {
      clearInterval(countdownInterval);
      //重新弹出滑动验证框
      contentEmail.value = "重新发送验证码";
      totalTimeEmail.value = 60;
      canClickEmail.value = true;
    }
  }, 1000);
}

function checkEmail (rule: any,value: any,callback: any){
  //验证邮箱的正则表达式
  const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
  if(regEmail.test(value)){
    //合法的邮箱
    callback();
  }
  //邮箱不合法
  callback(new Error(t('validationEmailWrongFormat')))
}


getState();
</script>

<style lang='scss' scoped>
.register {
  position: relative;
  overflow: hidden;
  height: 100%;
  width: 100%;
  color: #000000d9;
  background-image: url('../assets/images/login-bg.jpg');
  background-size: 100% 100%;
}

.register::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: #f0f2f5;
  z-index: -1; /* 确保背景图在内容后面 */
}

.register-form {
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

.register-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}

.register-code {
  width: 33%;
  height: 40px;
  float: right;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

.el-register-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-size: 12px;
  letter-spacing: 1px;
}

.register-code-img {
  height: 40px;
  padding-left: 12px;
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

.register-btn {
  height: 40px;
  padding: 6.4px 15px;
  font-size: 16px;
  border-radius: 2px;
}

:deep .input-with-select .el-input-group__prepend {
  background-color: var(--el-fill-color-blank);
  padding: 0!important;
}
</style>
