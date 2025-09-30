<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item label="SMTP" prop="smtpHost">
              <el-input v-model="form.smtpHost" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.port')" prop="port">
              <el-input v-model="form.port" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.account')" prop="account">
              <el-input v-model="form.account" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.credentials')" prop="credentials">
              <el-input v-model="form.credentials" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.protocol')" prop="protocol">
              <el-input v-model="form.protocol" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.organizations.id')" prop="encoding">
              <el-input v-model="form.encoding" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.sender')" prop="sender">
              <el-input v-model="form.sender" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.users.status')" prop="status">
              <el-switch
                  v-model="form.status"
                  :active-value="1"
                  :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="SSL" prop="sslSwitch">
              <el-switch
                v-model="form.sslSwitch"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" :loading="loading" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
      </div>
    </el-card>
  </div>
</template>


<script setup name="SecurityEmailsenders" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import {getSecurityEmailsenders, updateSecurityEmailsenders} from "@/api/security/emailsender";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()

const loading: any = ref(true);
const data: any = reactive({
  form: {
  },
  rules: {
    smtpHost: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    port: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    account: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    credentials: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    protocol: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    encoding: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    sender: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
  },
});

const { form, rules } = toRefs(data);

/** 分页列表 */
function get(): any {
  loading.value = true;
  getSecurityEmailsenders().then((res: any) =>  {
    form.value = res.data
    loading.value = false;
  });
}


/** 提交按钮 */
function submitForm(): any {
  loading.value = true
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      updateSecurityEmailsenders(form.value).then((response: any) =>  {
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
