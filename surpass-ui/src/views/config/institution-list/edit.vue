<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-form-item prop="instName" label="机构名称">
          <el-input v-model="form.instName" disabled/>
        </el-form-item>
        <el-form-item prop="instType" label="机构类型">
          <el-select v-model="form.instType" style="width: 100%" disabled>
            <el-option
                v-for="item in instTypes"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="contact" label="联系人">
          <el-input v-model="form.contact"/>
        </el-form-item>
        <el-form-item prop="phone" label="电话">
          <el-input v-model="form.phone"/>
        </el-form-item>
        <el-form-item prop="email" label="邮箱">
          <el-input v-model="form.email"/>
        </el-form-item>
        <el-form-item prop="address" label="地址">
          <el-input v-model="form.address"/>
        </el-form-item>
        <el-form-item prop="maxBook" label="可建立账套数量" :required="true">
          <el-input-number v-model="form.maxBook" :min="1" :max="100"></el-input-number>
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
import {useI18n} from "vue-i18n";
import {getCurrentInstance, PropType, reactive, ref, toRefs, watch} from "vue";
import {ElForm} from "element-plus";
import {getOne, saveOne, updateOne} from "@/api/config/institutions";

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
  instTypes: {
    type: Array as PropType<{ value: string | number; label: string }[]>,
    default: () => []
  }
})

const data: any = reactive({
  form: {
    status: 1
  },
  rules: {
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
          getOne(props.formId).then((res: any) =>  {
            form.value = res.data;
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

/** 重置操作表单 */
function reset(): any {
  form.value = {
    status: 1
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
      proxy?.$modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      proxy?.$modal.msgError(res.message);
    }
  };

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation: any = props.formId ? updateOne : saveOne;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}
</script>
