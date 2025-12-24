<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="appRef" label-width="110px" inline-message>
        <el-form-item prop="appName" label="应用编码" :required="true">
          <el-input v-model="form.appCode"/>
        </el-form-item>
        <el-form-item prop="appName" label="应用名称" :required="true">
          <el-input v-model="form.appName"/>
        </el-form-item>
        <el-form-item prop="contextPath" label="上下文路径" :required="true">
          <el-input v-model="form.contextPath" placeholder="请输入应用上下文路径，如：/portal"/>
        </el-form-item>
        <el-form-item prop="loginUrl" label="登录地址">
          <el-input v-model="form.loginUrl"/>
        </el-form-item>
        <el-form-item prop="status" :label="$t('jbx.text.status.status')">
          <el-switch
              :width="44"
              v-model="form.status"
              :active-value="1"
              :inactive-value="0"
              active-icon-class="el-icon-close"
              inactive-icon-class="el-icon-check">
          </el-switch>
        </el-form-item>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch
} from "vue";
import {addApp, getApp, updateApp} from "@/api/api-service/apps";
import {useI18n} from "vue-i18n";
import exAttrsForm from "@/components/expandAttrsForm.vue";
import {ElForm} from "element-plus";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const appRef: any = ref<InstanceType<typeof ElForm> | null>(null);
const childEx: any = ref<InstanceType<typeof exAttrsForm> | null>(null);
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: Boolean,
  formId: {
    default: undefined
  }
});


const emit: any = defineEmits(['dialogOfClosedMethods']);

const data: any = reactive({
  form: {
    status: 1,
    appName: null,
    appCode: null,
    contextPath: null,
    loginUrl: null
  },
  rules: {
    appCode: [
      {required: true, message: "请输入应用编码", trigger: "blur"},
    ],
    appName: [
      {required: true, message: "请输入应用名称", trigger: "blur"},
    ],
    contextPath: [
      {required: true, message: "请输入应用上下文路径", trigger: "blur"},
    ]
  }
})

const {form, rules} = toRefs(data)
const dialogStatus: any = ref(false)

const isEdit: any = ref(false);
// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          isEdit.value = true;
          //修改,查询当前应用
          getApp(props.formId).then((res: any) => {
            if (res.code === 0) {
              form.value = res.data;
            }
          })
        } else {
          isEdit.value = false;
          reset();
        }
      } else {
        isEdit.value = false;
        reset();
      }
    },
    {immediate: true}
);


function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

/** 重置操作表单 */
function reset(): any {
  form.value = {
    status: 1,
    appName: null,
    appCode: null,
    contextPath: null,
    loginUrl: null
  };
  appRef?.value?.resetFields();
}

/** 提交表单 */
function submitForm(): any {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      proxy?.$modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      proxy?.$modal.msgError(res.message);
    }
  };

  appRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation: any = props.formId ? updateApp : addApp;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}
</script>

<style lang="scss" scoped>

</style>
