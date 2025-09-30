<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item :label="$t('jbx.ldapcontext.product')" prop="product">
              <el-select v-model="form.product" placeholder="" clearable>
                <el-option
                  v-for="dict in ldapcontext_cp"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
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
            <el-form-item :label="$t('jbx.users.workStreetAddress')" prop="providerUrl">
              <el-input v-model="form.providerUrl" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.ldapcontext.accountMapping')" prop="accountMapping">
              <el-switch
                v-model="form.accountMapping"
                active-value="YES"
                inactive-value="NO"
              ></el-switch>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.account')" prop="principal">
              <el-input v-model="form.principal" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.emailsenders.credentials')" prop="credentials">
              <el-input type="password" v-model="form.credentials" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.product === 'ActiveDirectory'">
            <el-form-item label="AD域名" prop="msadDomain">
              <el-input v-model="form.msadDomain" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.product !== 'ActiveDirectory'">
            <el-form-item :label="$t('jbx.ldapcontext.basedn')" prop="basedn">
              <el-input v-model="form.basedn" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.product !== 'ActiveDirectory'">
            <el-form-item :label="$t('jbx.ldapcontext.filters')" prop="filters">
              <el-input v-model="form.filters" placeholder=""/>
            </el-form-item>
          </el-col>


          <el-col :span="12">
            <el-form-item label="SSL" prop="sslSwitch">
              <el-switch
                v-model="form.sslSwitch"
                active-value="1"
                inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.sslSwitch === '1'">
            <el-form-item :label="$t('jbx.ldapcontext.trustStore')" prop="trustStore">
              <el-input v-model="form.trustStore" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.sslSwitch === '1'">
            <el-form-item :label="$t('jbx.ldapcontext.trustStorePassword')" prop="trustStorePassword">
              <el-input v-model="form.trustStorePassword" placeholder=""/>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" :loading="loading">{{ $t('jbx.text.submit') }}</el-button>
        <el-button  @click="test" :loading="loading">{{ $t('jbx.text.test') }}</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup name="SecurityLdapcontext" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import {getSecurityLdapcontext, updateSecurityLdapcontext,testSecurityLdapcontext} from "@/api/security/ldapcontext";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()
const { ldapcontext_cp } = proxy.useDict("ldapcontext_cp");

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
  getSecurityLdapcontext().then((res: any) =>  {
    form.value = res.data
    loading.value = false;
  });
}


/** 提交按钮 */
function submitForm(): any {
  loading.value = true
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      updateSecurityLdapcontext(form.value).then((response: any) =>  {
        modal.msgSuccess(t('jbx.alert.operate.success'));
        get()
        loading.value = false
      });
    } else {
      loading.value = false
    }
  });
}
function test(): any {
  loading.value = true
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      testSecurityLdapcontext(form.value).then((response: any) =>  {
        modal.msgSuccess(t('jbx.alert.operate.success'));
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
