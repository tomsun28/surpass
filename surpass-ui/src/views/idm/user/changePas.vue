<template>
  <el-form :model="form" :rules="rules" ref="formUser" label-width="100px" inline-message>
    <el-form-item prop="username" :label="$t('jbx.password.username')" :required="true">
      <el-input v-model="form.username" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item prop="displayName" :label="$t('jbx.password.displayName')" :required="true">
      <el-input v-model="form.displayName" :disabled="true"></el-input>
    </el-form-item>
    <el-form-item prop="password" :label="$t('jbx.password.password')" :required="true">
      <el-input v-model="form.password" show-password>
        <template #append>
          <el-button @click="generatePass">{{ $t('jbx.text.generate') }}</el-button>
        </template>
      </el-input>
    </el-form-item>
    <el-form-item prop="confirmPassword" :label="$t('jbx.password.confirmPassword')" :required="true">
      <el-input v-model="form.confirmPassword" show-password></el-input>
    </el-form-item>
  </el-form>
</template>

<script setup lang="ts">
import { ElForm } from "element-plus";
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";
import {changePass, generateOnePas} from "@/api/system/user";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const formUser = ref<InstanceType<typeof ElForm> | null>(null);

defineOptions({
  name: 'UserEdit'
})

const emit: any = defineEmits(['onSubmitSuccess'])

const props: any = defineProps({
  pasForm: {
    default: {}
  },
  pasFlag: {
    type: Boolean,
    default: false
  }
})

const equalToPassword: any = (rule: any, value: any, callback: any) => {
  if (form.value.password !== value) {
    callback(new Error(t('validationPasswordTwice')));
  } else {
    callback();
  }
};

const data: any = reactive({
  form: {},
  rules: {
    password: [
      { required: true, message: t('validationPasswordRequired'), trigger: 'blur' },
    ],
    confirmPassword: [
      {required: true, trigger: "blur", message: t('validationPasswordRequired')},
      {required: true, validator: equalToPassword, trigger: "blur"}
    ],
  },
})

const {form, rules} = toRefs(data)

watch(
    () => props.pasFlag,
    (val: any) => {
      if (val) {
        form.value = {
          confirmPassword: props.pasForm.confirmPassword,
          displayName: props.pasForm.displayName,
          id: props.pasForm.id,
          password: props.pasForm.password,
          switch_status: true,
          userId: props.pasForm.id,
          username: props.pasForm.username,
          sortIndex: 1
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

function reset(): any {
  form.value = {
    confirmPassword: null,
    displayName: null,
    id: null,
    password: null,
    sortIndex: null,
    status: null,
    switch_status: null,
    userId: null,
    username: null,
  };
  formUser?.value?.resetFields();;
}


function generatePass(): any {
  generateOnePas().then((res: any) =>  {
    form.value.password = res.data;
    form.value.confirmPassword = res.data;
  })
}

const submitForm: any = () => {
  formUser?.value?.validate((valid: any) =>  {
    if (valid) {
      changePass(form.value).then((res: any) =>  {
        if (res.code === 0) {
          modal.msgSuccess(t('jbx.alert.update.success'));
          emit('onSubmitSuccess');
        } else {
          modal.msgError(res.message);
        }
      })
    }
  })
};

defineExpose({submitForm})
</script>
