<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item :label="$t('jbx.apps.adapterName') + '：'" prop="instName">
              <el-input v-model="form.instName" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.fullName') + '：'" prop="fullName">
              <el-input v-model="form.fullName" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="LOGO：" prop="logo">
              <el-input v-model="form.logo" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.backgroundImage') + '：'" prop="backgroundImage">
              <el-input v-model="form.backgroundImage" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.domain') + '：'" prop="domain">
              <el-input v-model="form.domain" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.frontTitle') + '：'" prop="frontTitle">
              <el-input v-model="form.frontTitle" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.consoleDomain') + '：'" prop="consoleDomain">
              <el-input v-model="form.consoleDomain" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.consoleTitle') + '：'" prop="consoleTitle">
              <el-input v-model="form.consoleTitle" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.defaultUri') + '：'" prop="defaultUri">
              <el-input v-model="form.defaultUri" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.organizations.contact') + '：'" prop="configName">
              <el-input v-model="form.contact" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.phone') + '：'" prop="phone">
              <el-input v-model="form.phone" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.institutions.email') + '：'" prop="email">
              <el-input v-model="form.email" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.users.workStreetAddress') + '：'" prop="address">
              <el-input v-model="form.address" placeholder=""/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">{{$t('jbx.text.submit')}}</el-button>
      </div>
    </el-card>
  </div>
</template>



<script setup name="Config-institutions" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import {institutionsGetCurrent, institutionsUpdateCurrent} from "@/api/config/institutions";

import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()
const { captcha_type } = proxy.useDict("captcha_type");

const loading: any = ref(true);
const data: any = reactive({
  form: {
  },
  rules: {
    instName: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    fullName: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    logo: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    backgroundImage: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    domain: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    frontTitle: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    consoleDomain: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    consoleTitle: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
  },
});

const { form, rules } = toRefs(data);

/** 分页列表 */
function get(): any {
  loading.value = true;
  institutionsGetCurrent().then((res: any) =>  {
    form.value = res.data
    loading.value = false;
  });
}


/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      institutionsUpdateCurrent(form.value).then((response: any) =>  {
        modal.msgSuccess(t('jbx.alert.operate.success'));
        get()
      });
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
