<template>
  <div class="forgot">
    <div class="forget-content">
      <el-form ref="forgotForm" :model="form" :rules="forgotRules" class="forgot-form">
        <div v-if="active === 1" style="width: 410px">
          <el-form-item prop="mobile">
            <el-input :prefix-icon="Cellphone" :placeholder="t('user.placeholder.mobile')"
                      size="large"
                      v-model="form.mobile"></el-input>
          </el-form-item>
          <el-form-item prop="mobileOtp">
            <el-input v-model="form.mobileOtp" size="large" :placeholder="t('forgot.placeholder.mobileOtp')"
                      @keyup.enter.native="onNextReset">
              <template #append>
                <el-button slot="append" @click="sendCode" :disabled="!canClick" class="send-btn">
                  {{ content }}
                </el-button>
              </template>
            </el-input>
          </el-form-item>
        </div>
        <div v-if="active === 3" style="width: 410px">
          <el-form :rules="passwordRules" :model="newPwdForm" ref="passwordForm" label-width="100px">
            <div style="text-align: center;margin-bottom: 30px">{{ t('forgot.setNew') }}</div>
            <el-form-item :label="t('forgot.new')" prop="password" style="margin-bottom: 30px">
              <el-input v-model="newPwdForm.password" show-password>
              </el-input>
            </el-form-item>
            <el-form-item :label="t('forgot.confirmNew')" prop="confirmPassword" style="margin-bottom: 25px">
              <el-input v-model="newPwdForm.confirmPassword" show-password>
              </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div class="forgot-footer">
          <div>
            <el-button type="primary"
                       v-if="active === 1"
                       @click="onNextReset"
                       style="width: 100px"
                       class="button-account">{{ t('forgot.next') }}
            </el-button>
            <el-button type="primary" v-if="active === 3"
                       @click="confirm"
                       class="button-account"
                       style="width:100px">
              {{ t('text.submit') }}
            </el-button>
          </div>
          <el-button class="button-account"
                     @click="backToLogin"
                     style="width: 100px">
            {{ t('forgot.back') }}
          </el-button>
        </div>

        <el-form-item>
          <div style="width: 410px;margin-top: 20px;line-height: 26px" v-show="active === 1">
            <el-row>
              {{ t('forgot.mobileTi1') }}
            </el-row>
            <el-row class="mt5">
              {{ t('forgot.mobileTip2') }}
            </el-row>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import modal from "@/plugins/modal";
import {Cellphone} from "@element-plus/icons-vue";
import {getPwdPolicy, sendSmsCode, setNewPassword, validateCaptcha} from "@/api/login.js";
import {validateFields, validatePass} from "@/api/commonApi.js"
import {useI18n} from "vue-i18n";
import {ElForm} from "element-plus";
import {useRouter} from "vue-router"

const {t} = useI18n();
const router: any = useRouter();

const {proxy} = getCurrentInstance()!;
const passwordForm: any = ref<InstanceType<typeof ElForm> | null>(null);

const data: any = reactive({
  form: {},
  forgotRules: {
    mobile: [
      {required: true, message: t('forgot.mobileEmpty'), trigger: "blur"},
      {pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: t('user.rule.mobile'), trigger: ['blur', 'change']}
    ],
    mobileOtp: [
      {required: true, message: t('forgot.placeholder.mobileOtp'), trigger: 'blur'}
    ],
  },
  newPwdForm: {},
  passwordRules: {
    password: [
      {required: true, message: t('user.rule.pwd'), trigger: 'blur'},
      {
        validator: (rule: any, value: any, callback: any) => {
          validatePass(rule, value, callback, pwdPolicy.value);
        },
        trigger: ['blur', 'change']
      }
    ],
    confirmPassword: [
      {required: true, trigger: "blur", message: t('forgot.pwdAgain')},
      {required: true, validator: equalToPassword, trigger: ['blur', 'change']}
    ]
  }
});

const {form, forgotRules, newPwdForm, passwordRules} = toRefs(data);

//发送标识
const canClick: any = ref(true);
//发送提示语
const content: any = ref(t('forgot.sendCode'));

const active: any = ref(1);
const totalTime: any = ref(60);
//密码策略
const pwdPolicy: any = ref<any>({})
//临时验证ID
const tempId: any = ref(undefined)

//60秒倒计时
function startCountdown(): any {
  let totalTimeTemp: any = totalTime.value;
  canClick.value = false;
  const countdownInterval: any = setInterval(() => {
    totalTimeTemp--;
    content.value = totalTimeTemp + t('forgot.waitAgain');
    if (totalTimeTemp < 0) {
      clearInterval(countdownInterval);
      //重新弹出滑动验证框
      content.value = t('forgot.sendAgain');
      totalTime.value = 60;
      canClick.value = true;
    }
  }, 1000);
}

function sendCode(): any {
  let fields: any = ['mobile']

  validateFields(proxy, fields, 'forgotForm').then((valid: any) => {
    if (valid) {
      let params: any = {
        mobile: form.value.mobile,
        otpType: 'forgot',
      }
      sendSmsCode(params).then((res: any) => {
        if (res.code === 200) {
          modal.msgSuccess(t('forgot.sendSuccess'))
          startCountdown();
        } else {
          modal.msgError(res.message)
        }
      })
    }
  })
}

function onNextReset(): any {
  let params: any = {};
  let fieldsToValidate: any = ['mobile', 'mobileOtp'];
  params = {
    mobile: form.value.mobile,
    mobileOtp: form.value.mobileOtp,
    otpType: 'forgot'
  }

  validateFields(proxy, fieldsToValidate, 'forgotForm')
      .then((valid: any) => {
        if (valid) {
          return validateCaptcha(params);
        } else {
          throw new Error('Validation failed');
        }
      })
      .then((res: any) => {
        if (res.code === 200) {
          tempId.value = res.data;
          active.value = 3;
        } else {
          modal.msgError(res.message);
        }
      })
      .catch((error: any) => {
      });
}

function confirm(): any {
  let params: any = {
    password: newPwdForm.value.password,
    mobile: form.value.mobile,
    tempId: tempId.value
  };

  passwordForm.value.validate((valid: any) => {
    if (valid) {
      setNewPassword(params).then((res: any) => {
        if (res.code === 200) {
          modal.msgSuccess(t('forgot.setSuccess'));
          backToLogin();
        } else {
          modal.msgError(res.message);
        }
      })
    }
  })
}

function backToLogin(): any {
  router.push("/login")
}

//获取密码策略
function passwordPolicy(): any {
  getPwdPolicy().then((res: any) => {
    if (res.code === 200) {
      pwdPolicy.value = res.data;
    } else {
      modal.msgError(t('profile.pwdPolicy'))
      backToLogin();
    }
  })
}

function equalToPassword(rule: any, value: any, callback: any): any {
  if (value === '') {
    callback(new Error(t('forgot.pwdAgainInput')));
  } else if (newPwdForm.value.password !== value) {
    callback(new Error('forgot.notSame'));
  } else {
    callback();
  }
}

passwordPolicy();
</script>

<style lang="scss" scoped>
.forgot {
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.forgot::before {
  content: "";
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-image: url("../assets/images/backstage3.jpg");
  background-size: cover;
  background-position: center;
  //transform: scaleX(-1);
  z-index: -1; /* 确保背景图在内容后面 */
}

.forget-content {
  width: 90%;
  display: flex;
  justify-content: center;
}

.forgot-form {
  box-shadow: 3px 3px 30px 0 rgba(0, 0, 0, 0.16);
  border-radius: 8px;
  padding: 32px 36px 36px;
  background-color: #FFFFFF;
  position: relative;
  color: #333333;

  .send-btn {
    color: #333333;
  }
}

.forgot-footer {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-top: 20px;
  align-items: center;
}

.button-account {
  height: 36px;
  padding: 0;
}
</style>
