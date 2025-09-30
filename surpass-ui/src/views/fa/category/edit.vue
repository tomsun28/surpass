<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-form-item label="类别编码" prop="categoryCode">
          <el-input v-model="form.categoryCode"/>
        </el-form-item>
        <el-form-item label="类别名称" prop="categoryName">
          <el-input v-model="form.categoryName"/>
        </el-form-item>
        <el-form-item label="备注">
          <el-input
              type="textarea"
              v-model="form.remark"
              maxlength="100"
              show-word-limit
          />
        </el-form-item>
        <el-form-item label="折旧方法" prop="depreciationMethod">
          <el-select v-model="form.depreciationMethod" clearable>
            <el-option
                v-for="dict in methods"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="预计使用年限" prop="depreciationYears">
          <el-input-number
              v-model="form.depreciationYears"
              :min="0"
              style="width: 60%"
          >
          </el-input-number>
        </el-form-item>
        <el-form-item label="残值率" prop="residualRate">
          <el-input-number
              v-model="form.residualRate"
              :min="0"
              :max="100"
              :precision="1"
              style="width: 60%"
          >
            <template #suffix>
              <span>%</span>
            </template>
          </el-input-number>
        </el-form-item>
        <el-form-item prop="assetSubjectCode" label="固定资产科目">
          <el-cascader style="width: 60%" filterable
                       v-model="form.assetSubjectCode"
                       :options="deptOptions"
                       :props="defaultProps"
          />
        </el-form-item>
        <el-form-item prop="depreciationSubjectCode" label="累计折旧科目">
          <el-cascader style="width: 60%" filterable
                       v-model="form.depreciationSubjectCode"
                       :options="deptOptions"
                       :props="defaultProps"
          />
        </el-form-item>
        <el-form-item prop="expenseSubjectCode" label="折旧费用科目">
          <el-cascader style="width: 60%" filterable
                       v-model="form.expenseSubjectCode"
                       :options="deptOptions"
                       :props="defaultProps"
          />
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
import {ElForm} from "element-plus";
import {reactive, ref, toRefs, watch, computed} from "vue";
import {useI18n} from "vue-i18n";
import modal from "@/plugins/modal";
import {getOne, saveOne, updateOne} from "@/api/fa/category";
import {LabeledValue} from "@/types/commnon";
import Template from "@/views/hr/salary-voucher-rules/template.vue";

const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const props = defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
  methods?: LabeledValue[];
  deptOptions?: [];
}>();


interface FormModel {
  categoryCode: string
  categoryName: string
  depreciationMethod: number
  depreciationYears: number
  residualRate: number
  assetSubjectCode: string
  depreciationSubjectCode: string
  expenseSubjectCode: string
  remark: string
}


interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    categoryCode: '',
    categoryName: '',
    depreciationMethod: 1,
    depreciationYears: 0,
    residualRate: 5,
    assetSubjectCode: '',
    depreciationSubjectCode: '',
    expenseSubjectCode: '',
    remark: '',
  },
  rules: {
    categoryCode: [
      { required: true, message: '请输入资产分类编码', trigger: 'blur' },
      { min: 2, max: 25, message: '资产分类编码长度应为2-25个字符', trigger: 'blur' }
    ],
    categoryName: [
      { required: true, message: '请输入资产分类名称', trigger: 'blur' },
      { min: 2, max: 30, message: '资产分类名称长度应为2-30个字符', trigger: 'blur' }
    ],
    depreciationMethod: [
      { required: true, type: 'number', message: '请选择折旧方法', trigger: 'change' }
    ],
    depreciationYears: [
      { required: true, type: 'number', message: '请输入预计使用年限', trigger: 'blur' },
      { type: 'number', min: 1, message: '预计使用年限不能小于1年', trigger: 'blur' }
    ],
    residualRate: [
      { required: true, type: 'number', message: '请输入残值率', trigger: 'blur' },
      { type: 'number', min: 0, max: 100, message: '残值率应在0-100之间', trigger: 'blur' }
    ],
    assetSubjectCode: [
      { required: true, message: '请选择固定资产科目', trigger: 'change' }
    ],
    depreciationSubjectCode: [
      { required: true, message: '请选择累计折旧科目', trigger: 'change' }
    ],
    expenseSubjectCode: [
      { required: true, message: '请选择折旧费用科目', trigger: 'change' }
    ],
    remark: [
      { max: 100, message: '备注长度不能超过100个字符', trigger: 'blur' }
    ]
  }
})
const {form, rules} = toRefs(data);
const span = ref(12);
const loading: any = ref(false);
const defaultProps: any = ref({
  expandTrigger: 'hover',
  label: 'name',
  value: 'code',
  children: 'children',
  checkStrictly: false,
  emitPath: false,
  defaultExpandAll: true,
  showAllLevels: false,
  clearable: false,
  filterable: true,
  disabled: disabledFilter
})

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getOne(props.formId).then((res: any) => {
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

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function reset() {
  form.value = {
    categoryCode: '',
    categoryName: '',
    depreciationMethod: 1,
    depreciationYears: 0,
    residualRate: 5,
    assetSubjectCode: '',
    depreciationSubjectCode: '',
    expenseSubjectCode: '',
    remark: '',
  }
  formRef?.value?.resetFields();
}

function disabledFilter(data: any, node: any): any {
  return data.isCash === 1
}

function submitForm() {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      modal.msgSuccess(successMessage);
      dialogOfClosedMethods(true);
      reset();
    } else {
      modal.msgError(res.message);
    }
    loading.value = false;
  };

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      loading.value = true;
      const operation: any = props.formId ? updateOne : saveOne;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}


</script>

<style scoped lang="scss">
.color-picker {
  display: flex;
  gap: 10px;
  align-items: center;
}

.color-circle {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  cursor: pointer;
  transition: border 0.2s;
}
</style>

