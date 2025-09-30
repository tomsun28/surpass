<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-form-item label="凭证类型" :required="true">
          <el-select v-model="form.voucherType" style="width: 100%" @change="changeType">
            <el-option v-for="item in voucherTypes" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="凭证字" :required="true">
          <el-select v-model="form.wordHead" style="width: 100%" disabled>
            <el-option v-for="item in wordHeads" :label="item.label" :value="item.value"/>
          </el-select>
        </el-form-item>
        <el-form-item label="摘要" prop="summary" :required="true">
          <el-input v-model="form.summary" placeholder="请输入摘要"/>
        </el-form-item>
        <el-form-item label="借/贷" prop="direction" :required="true">
          <el-radio-group v-model="form.direction">
            <el-radio value="1">{{ t('subjectDebit') }}</el-radio>
            <el-radio value="2">{{ t('subjectCredit') }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="subjectId" label="科目" :required="true">
          <el-cascader style="width: 100%" filterable
                       v-model="form.subjectId"
                       :options="deptOptions"
                       :props="defaultProps"
                       />
        </el-form-item>
        <el-form-item prop="status" :label="$t('jbx.text.status.status')" :required="true">
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
import {getCurrentInstance, reactive, ref, toRefs, watch} from "vue";
import {ElForm} from "element-plus";
import {addRule, getById, updateRule} from "@/api/system/hr/voucher-rule";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const resTreeRef: any = ref<any>({});
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
  voucherTypes: {
    type: Array<any>,
    default: [],
  },
  wordHeads: {
    type: Array<any>,
    default: []
  },
  deptOptions: {
    type: Array,
    default: [],
  },
})

const data: any = reactive({
  form: {
    voucherType: 0,
    summary: "计提{yy}年{mm}月工资",
    wordHead: '记',
    status: 1,
    direction: "1",
  },
  rules: {
    summary: [{required: true, message: '请输入摘要', trigger: "blur"}],
    subjectId: [{required: true, message: '请选择科目', trigger: "blur"}],
  }
})
const defaultProps: any = ref({
  expandTrigger: 'hover',
  label: 'name',
  value: 'id',
  children: 'children',
  checkStrictly: false,
  emitPath: false,
  defaultExpandAll: true,
  showAllLevels: false,
  clearable: false,
  filterable: true
})
const {form, rules} = toRefs(data);
const dialogStatus: any = ref(false);

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getById(props.formId).then((res: any) => {
            form.value = res.data;
          })
        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

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
      const operation: any = props.formId ? updateRule : addRule;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}

/** 重置操作表单 */
function reset(): any {
  form.value = {
    voucherType: 0,
    summary: "计提{yy}年{mm}月工资",
    wordHead: '记',
    status: 1,
    direction: "1"
  };
  formRef?.value?.resetFields();
}

function changeType() {
  if (form.value.voucherType === 0) {
    form.value.summary = "计提{yy}年{mm}月工资"
  } else {
    form.value.summary = "发放{yy}年{mm}月工资"
  }
}

function disabledFilter(data: any, node: any): any {
  return (data.auxiliary != null && data.auxiliary != '[]' && data.auxiliary != '');
}
</script>


