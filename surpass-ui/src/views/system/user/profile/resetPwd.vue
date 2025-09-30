<template>
   <el-form ref="pwdRef" :model="user" :rules="rules" label-width="80px">
      <el-form-item :label="t('profile.oldPwd')" prop="oldPassword">
         <el-input v-model="user.oldPassword" :placeholder="t('profile.placeholder.oldPwd')" type="password" show-password />
      </el-form-item>
      <el-form-item :label="t('profile.newPwd')" prop="newPassword">
         <el-input v-model="user.newPassword" :placeholder="t('profile.placeholder.newPwd')" type="password" show-password />
      </el-form-item>
      <el-form-item :label="t('profile.confirmPwd')" prop="confirmPassword">
         <el-input v-model="user.confirmPassword" :placeholder="t('profile.placeholder.confirmPwd')" type="password" show-password/>
      </el-form-item>
      <el-form-item>
      <el-button type="primary" @click="submit">{{t('text.save')}}</el-button>
      <el-button type="danger" @click="close">{{t('text.close')}}</el-button>
      </el-form-item>
   </el-form>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {changePass} from "@/api/system/user";
import {validatePass} from "@/api/commonApi";
import {useI18n} from "vue-i18n";

const props: any = defineProps({
  pwdPolicy: {
    type: Object,
    default: {}
  }
})

const {proxy} = getCurrentInstance()!;

const {t} = useI18n();

const user: any = reactive({
  oldPassword: undefined,
  newPassword: undefined,
  confirmPassword: undefined
});

const equalToPassword: any = (rule: any, value: any, callback: any) => {
  if (user.newPassword !== value) {
    callback(new Error(t('profile.rule.twicePwd')));
  } else {
    callback();
  }
};

const notAllowedEqualToPassword: any = (rule: any, value: any, callback: any) => {
  if (user.newPassword === user.oldPassword) {
    callback(new Error(t('profile.rule.oldNew')));
  } else {
    callback();
  }
};

const validateWhitespace: any = (rule: any, value: any, callback: any) => {
  // 使用正则表达式检查输入是否只包含空格
  if (/^\s+$/.test(value)) {
    callback(new Error(t('profile.rule.whiteSpace')));
  } else {
    callback();
  }
}

const rules: any = ref({
  oldPassword: [
    { required: true, trigger: "blur", message: t('profile.rule.oldPwd') },
    { validator: notAllowedEqualToPassword, trigger: ['blur', 'change'] },
    { validator: validateWhitespace, trigger: ['blur', 'change'] }
  ],
  newPassword: [
    { required: true, message: t('user.rule.pwd'), trigger: 'blur' },
    { validator: (rule: any, value: any, callback: any) => validatePass(rule, value, callback, props.pwdPolicy), trigger: ['blur', 'change'] },
    { validator: notAllowedEqualToPassword, trigger: ['blur', 'change'] }
  ],
  confirmPassword: [
    { required: true, message: t('profile.rule.confirmPwd'), trigger: "blur" },
    { validator: equalToPassword, trigger: "blur" }
  ]
});

/** 提交按钮 */
function submit(): any {
  proxy.$refs.pwdRef.validate((valid: any) =>  {
    if (valid) {
      let params: any = {
        oldPassword: user.oldPassword,
        newPassword: user.newPassword
      }
      changePass(params).then((res: any) =>  {
        if (res.code === 200) {
          modal.msgSuccess(t('org.success.update'));
        } else {
          modal.msgError(res.message);
        }
      });
    }
  });
};

/** 关闭按钮 */
function close(): any {
  proxy.$tab.closePage();
}

</script>
