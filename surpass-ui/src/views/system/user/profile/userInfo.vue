<template>
   <el-form ref="userRef" :model="form" :rules="rules" label-width="80px">
     <el-form-item :label="t('user.username')" prop="username">
       <el-input v-model="form.username" disabled/>
     </el-form-item>
     <el-form-item :label="t('user.displayName')" prop="displayName">
       <el-input v-model="form.displayName" maxlength="20" />
     </el-form-item>
      <el-form-item :label="t('user.nickname')" prop="nickName">
         <el-input v-model="form.nickName" maxlength="20" />
      </el-form-item>
      <el-form-item :label="t('user.mobile')" prop="mobile">
         <el-input v-model="form.mobile" />
      </el-form-item>
      <el-form-item :label="t('user.email')" prop="email">
         <el-input v-model="form.email" />
      </el-form-item>
      <el-form-item :label="t('user.gender')">
        <el-radio-group v-model="form.gender">
          <el-radio-button v-for="item in genderOptions"
                           :label="item.label"
                           :value="item.value">

          </el-radio-button>
        </el-radio-group>
      </el-form-item>
     <el-form-item :label="t('user.birthDate')">
       <el-date-picker
           style="width: 100%"
           v-model="form.birthDate"
           type="date"
           format="YYYY-MM-DD"
           value-format="YYYY-MM-DD"
           :placeholder="t('profile.rule.birthDate')">
       </el-date-picker>
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
import {  updateUser } from "@/api/system/user";
import {useI18n} from "vue-i18n";

const props: any = defineProps({
  user: {
    type: Object
  },
  genderOptions: {
    type: Array,
    default: []
  }
});

// 定义 emits
const emit: any = defineEmits(['profileDisplay']);

const {proxy} = getCurrentInstance()!;

const {t} = useI18n();

const form: any = ref<any>({});
const rules: any = ref({
  displayName: [{required: true, message: t('user.rule.displayName'), trigger: "blur"}, {
    min: 2,
    max: 20,
    message: t('user.rule.displayNameLength'),
    trigger: "blur"
  }],
  nickName: [{
    min: 0,
    max: 20,
    message: t('user.rule.nickNameLength'),
    trigger: "blur"
  }],
  email: [
    { type: "email", message: t('user.rule.email'), trigger: ["blur", "change"] }],
  mobile: [
    { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: t('user.rule.mobile'), trigger: "blur" }],
});


/** 提交按钮 */
function submit(): any {
  proxy.$refs.userRef.validate((valid: any) =>  {
    if (valid) {
      updateUser(form.value).then((res: any) =>  {
        if (res.code === 200) {
          modal.msgSuccess(t('org.success.update'));
          emit('profileDisplay', form.value);
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
};

// 回显当前登录用户信息
watch(() => props.user, (user: any) =>  {
  if (user) {
    form.value = {
      username: user.username,
      displayName: user.displayName,
      nickName: user.nickName,
      mobile: user.mobile,
      email: user.email,
      gender: user.gender,
      birthDate: user.birthDate
    };
  }
},{ immediate: true });
</script>
