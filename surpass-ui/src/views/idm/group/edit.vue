<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
        <el-form-item prop="roleCode" :label="t('jbx.text.id')" :required="true">
          <el-input v-model="form.roleCode" :disabled="isEdit"/>
        </el-form-item>
        <el-form-item prop="roleName" :label="t('jbx.roles.name')" :required="true">
          <el-input v-model="form.roleName"/>
        </el-form-item>
        <el-form-item prop="category" :label="t('jbx.roles.category.category')">
          <el-select v-model="form.category" style="width: 100%">
            <el-option
                v-for="item in group_category_options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="description" :label="$t('jbx.text.description')">
          <el-input v-model="form.description" type="textarea" :rows="4"/>
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
import { ElForm } from "element-plus";
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {addGroup, getOneGroup, updateGroup} from "@/api/system/group.js";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const emit: any = defineEmits(['dialogOfClosedMethods'])

const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  formId: {
    default: undefined
  },
  deptOptions: {
    type: Array,
    default: [],
  },
  group_category_options: {
    type: Array,
    default: [],
  }
})

const data: any = reactive({
  form: {
    pattern: 'static'
  },
  rules: {
    roleName: [
      { required: true, message: t('jbx.roles.nameError'), trigger: "blur" },
    ],
    roleCode: [
      { required: true, message: t('jbx.roles.codeError'), trigger: "blur" },
    ],
  }
})

const {form, rules} = toRefs(data);
const isEdit: any = ref(false);
const dialogStatus: any = ref(false);

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          isEdit.value = true;
          getOneGroup(props.formId).then((res: any) =>  {
            form.value = res.data;
            // 直接使用条件检查和赋值方式更新对象属性
          /*  if (form.value.resumeTime !== '') {
              form.value.picker_resumeTime = form.value.resumeTime;
            }
            if (form.value.suspendTime !== '') {
              form.value.picker_suspendTime = form.value.suspendTime;
            }*/
            // 处理 orgIdsList 字符串拆分并过滤空值
            // 优化后的代码
            form.value.orgIdsList = form.value.orgIdsList?.split(',').filter(Boolean) || [];
          });

        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

const defaultProps: any = ref({
  value: 'key',
  children: 'children',
  label: 'title',
})

/** 重置操作表单 */
function reset(): any {
  form.value = {
    pattern: 'static'
  };
  isEdit.value = false;
  formRef?.value?.resetFields();
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function submitForm(): any {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      modal.msgError(res.message);
    }
  };

  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      const { orgIdsList, picker_resumeTime, picker_suspendTime } = form.value;
      // 更新表单字段
      Object.assign(form.value, {
        orgIdsList: orgIdsList?.join(",") || "",
      /*  resumeTime: picker_resumeTime,
        suspendTime: picker_suspendTime,*/
        sortIndex: 1,
        switch_status: true,
        status: 1,
      });

      const operation: any = props.formId ? updateGroup : addGroup;

      const successMessage: any = props.formId ? t('org.success.update') : t('org.success.add');

      operation(form.value).then((res: any) =>  handleResponse(res, successMessage));

      dialogOfClosedMethods(true);
    }
  });
}
</script>
