<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="180px">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.sessionValidity')" prop="sessionValidity">
              <el-input-number :min="0" v-model="form.sessionValidity" placeholder=""/>
              {{$t('jbx.text.hour')}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.tokenValidity')" prop="tokenValidity">
              <el-input-number :min="0" v-model="form.tokenValidity" placeholder=""/>
              {{$t('jbx.text.hour')}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.terminals')" prop="terminals">
              <el-input-number :min="0" v-model="form.terminals" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.isFirstPasswordModify')" prop="isFirstPasswordModify">
              <el-switch
                  v-model="form.isFirstPasswordModify"
                  :active-value="'Y'"
                  :inactive-value="'N'"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.captcha')" prop="captcha">
              <el-select v-model="form.captcha" style="width: 150px;">
                <el-option
                    v-for="dict in captcha_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.captchaMgt')" prop="captchaMgt">
              <el-select v-model="form.captchaMgt" style="width: 150px;">
                <el-option
                    v-for="dict in captcha_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.scanCode')" prop="scanCode">
              <el-select v-model="form.scanCode" style="width: 150px;">
                <el-option
                    v-for="dict in scanCode_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.loginAttempts')" prop="loginAttempts">
              <el-input-number :min="0" v-model="form.loginAttempts" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.isMobile')" prop="isMobile">
              <el-switch
                  v-model="form.isMobile"
                  :active-value="'Y'"
                  :inactive-value="'N'"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.isSocial')" prop="isSocial">
              <el-switch
                  v-model="form.isSocial"
                  :active-value="'Y'"
                  :inactive-value="'N'"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.isAutoLock')" prop="isAutoLock">
              <el-switch
                  v-model="form.isAutoLock"
                  :active-value="'Y'"
                  :inactive-value="'N'"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.lockInterval')" prop="lockInterval">
              <el-input-number :min="0" v-model="form.lockInterval" placeholder=""/>
              {{$t('jbx.text.minute')}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.passwordAttempts')" prop="passwordAttempts">
              <el-input-number :min="0" v-model="form.passwordAttempts" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.loginpolicy.passwordAttemptsCaptcha')" prop="passwordAttemptsCaptcha">
              <el-switch
                  v-model="form.passwordAttemptsCaptcha"
                  :active-value="'Y'"
                  :inactive-value="'N'"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
      </div>
    </el-card>
  </div>
</template>



<script setup name="SecurityPasswordpolicy" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import {getPolicy, updateSecurityPolicy} from "@/api/security/configloginpolicy";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()

const { captcha_type ,scanCode_type} = proxy.useDict("captcha_type","scanCode_type");

const loading: any = ref(true);
const data: any = reactive({
  form: {
  },
  rules: {
    product: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    status: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    providerUrl: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    accountMapping: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    principal: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    credentials: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    basedn: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    filters: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
  },
});

const { form, rules } = toRefs(data);

/** 分页列表 */
function get(): any {
  loading.value = true;
  getPolicy().then((res: any) =>  {
    form.value = res.data
    loading.value = false;
  });
}


/** 提交按钮 */
function submitForm(): any {
  loading.value = true
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      updateSecurityPolicy(form.value).then((response: any) =>  {
        modal.msgSuccess(t('jbx.alert.operate.success'));
        get()
        loading.value = false
      });
    } else {
      loading.value = false
    }
  });
}

get();

</script>
<style scoped>
::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}
.dialog-footer {
  text-align: center;
}
</style>
