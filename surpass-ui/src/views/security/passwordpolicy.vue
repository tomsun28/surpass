<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="30">
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.minLength')" prop="minLength">
              <el-input-number :min="0" v-model="form.minLength" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.maxLength')" prop="maxLength">
              <el-input-number :min="0" v-model="form.maxLength" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.lowerCase')" prop="lowerCase">
              <el-input-number :min="0" v-model="form.lowerCase" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.upperCase')" prop="upperCase">
              <el-input-number :min="0" v-model="form.upperCase" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.digits')" prop="digits">
              <el-input-number :min="0" v-model="form.digits" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.specialChar')" prop="specialChar">
              <el-input-number :min="0" v-model="form.specialChar" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.occurances')" prop="occurances">
              <el-input-number :min="0" v-model="form.occurances" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.expiration')" prop="expiration">
              <el-input-number :min="0" v-model="form.expiration" placeholder=""/>
              {{$t('jbx.text.day')}}
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.history')" prop="history">
              <el-input-number :min="0" v-model="form.history" placeholder=""/>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.username')" prop="username">
              <el-switch
                v-model="form.username"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.dictionary')" prop="dictionary">
              <el-switch
                v-model="form.dictionary"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.alphabetical')" prop="alphabetical">
              <el-switch
                v-model="form.alphabetical"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.numerical')" prop="numerical">
              <el-switch
                v-model="form.numerical"
                :active-value="1"
                :inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.passwordpolicy.qwerty')" prop="qwerty">
              <el-switch
                v-model="form.qwerty"
                :active-value="1"
                :inactive-value="0"
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

import {getSecurityPasswordpolicy, updateSecurityPasswordpolicy} from "@/api/security/passwordpolicy";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()


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
  getSecurityPasswordpolicy().then((res: any) =>  {
    form.value = res.data
    loading.value = false;
  });
}


/** 提交按钮 */
function submitForm(): any {
  loading.value = true
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      updateSecurityPasswordpolicy(form.value).then((response: any) =>  {
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
