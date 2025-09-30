<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item :label="$t('jbx.smsprovider.provider')" prop="provider">
              <el-select v-model="form.provider" placeholder="" clearable>
                <el-option
                  v-for="dict in smsprovider"
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
            <el-form-item :label="$t('jbx.smsprovider.message')" prop="message">
              <el-input v-model="form.message" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.smsprovider.templateId')" prop="templateId">
              <el-input v-model="form.templateId" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="AppKey" prop="appKey">
              <el-input v-model="form.appKey" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="AppSecret" prop="appSecret">
              <el-input v-model="form.appSecret" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.smsprovider.signName')" prop="signName">
              <el-input v-model="form.signName" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12" v-if="form.provider === 'tencentcloud'">
            <el-form-item :label="$t('jbx.smsprovider.smsSdkAppId')" prop="smsSdkAppId">
              <el-input v-model="form.smsSdkAppId" placeholder=""/>
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

<script setup name="SecuritySmsprovider" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import {getSecuritySmsprovider, updateSecuritySmsprovider} from "@/api/security/smsprovider";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()
const { smsprovider } = proxy.useDict("smsprovider");

const loading: any = ref(true);
const data: any = reactive({
  form: {
  },
  rules: {
    message: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    status: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    appKey: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
    appSecret: [
      {required: true, message: "Not empty", trigger: "blur"}
    ],
  },
});

const { form, rules } = toRefs(data);

/** 分页列表 */
function get(): any {
  loading.value = true;
  getSecuritySmsprovider().then((res: any) =>  {
    form.value = res.data
    loading.value = false;
  });
}


/** 提交按钮 */
function submitForm(): any {
  loading.value = true
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      updateSecuritySmsprovider(form.value).then((response: any) =>  {
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
