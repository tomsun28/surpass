<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="65%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="135px" inline-message>
        <h4 style="color: dodgerblue">基本信息</h4>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="资产编码" prop="code">
              <el-input v-model="form.code"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="资产名称" prop="assetName">
              <el-input v-model="form.assetName"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="资产分类" prop="categoryId">
              <category-select v-model="form.categoryId"  @select="handleCategorySelect"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
           <el-form-item label="规格型号" prop="categoryName">
            <el-input v-model="form.specification"/>
          </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="生产厂家" prop="categoryName">
              <el-input v-model="form.manufacturer"/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="购置日期" prop="purchaseDate">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.purchaseDate"
                              type="date"
                              format="YYYY-MM-DD"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择购置日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="资产状态" prop="assetStatus">
              <el-select v-model="form.assetStatus" clearable style="width: 100%">
                <el-option
                    v-for="dict in assetsStatus"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="存放地点" prop="location">
              <el-input v-model="form.location"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="保管人" prop="custodian">
              <UserSelect v-model="form.custodian"></UserSelect>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item prop="usageDepartmentList" label="使用部门">
              <el-tree-select
                  v-model="form.usageDepartmentList"
                  :data="deptOptions"
                  :props="treeProps"
                  show-checkbox
                  check-strictly
                  value-key="id"
                  placeholder="选择使用部门"
                  multiple
                  collapse-tags
                  collapse-tags-tooltip
              />
            </el-form-item>
          </el-col>
        </el-row>
        <h4 style="color: orangered">原值、净值、折旧方法</h4>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="原值" prop="originalValue">
              <el-input-number :min="0" :precision="2" v-model="form.originalValue" style="width: 100%" >
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="残值率" prop="residualRate">
              <el-input-number
                  v-model="form.residualRate"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 100%"
              >
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="净值">
              <el-input-number :min="0" :precision="2" v-model="form.netValue" style="width: 100%" >
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="折旧方法" prop="depreciationMethod">
              <el-select v-model="form.depreciationMethod" clearable style="width: 100%">
                <el-option
                    v-for="dict in methods"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="预计使用期数" prop="depreciationYears">
              <el-input-number
                  v-model="form.depreciationTerms"
                  :min="0"
                  style="width: 100%"
              >
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="折旧开始日期" prop="depreciationStartDate">
              <el-date-picker clearable style="width: 100%"
                              v-model="form.depreciationStartDate"
                              type="date"
                              format="YYYY-MM-DD"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择折旧开始日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <h4 style="color: goldenrod">凭证科目设置</h4>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item prop="assetSubjectCode" label="固定资产科目">
              <el-cascader style="width: 100%" filterable
                           v-model="form.assetSubjectCode"
                           :options="subjectOptions"
                           :props="defaultProps"
              />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item prop="depreciationSubjectCode" label="累计折旧科目">
              <el-cascader style="width: 100%" filterable
                           v-model="form.depreciationSubjectCode"
                           :options="subjectOptions"
                           :props="defaultProps"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item prop="expenseSubjectCode" label="折旧费用科目">
              <el-cascader style="width: 100%" filterable
                           v-model="form.expenseSubjectCode"
                           :options="subjectOptions"
                           :props="defaultProps"
              />
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item prop="purchaserSubjectCode" label="资产购入对方科目">
              <el-cascader style="width: 100%" filterable
                           v-model="form.purchaserSubjectCode"
                           :options="subjectOptions"
                           :props="defaultProps"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item prop="liquidationSubjectCode" label="资产清理科目">
              <el-cascader style="width: 100%" filterable
                           v-model="form.liquidationSubjectCode"
                           :options="subjectOptions"
                           :props="defaultProps"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="gutter">
          <el-col :span="span">
            <el-form-item label="备注">
              <el-input
                  type="textarea"
                  v-model="form.remark"
                  maxlength="100"
                  show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>
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
import {getOne, saveOne, updateOne} from "@/api/fa/info";
import {LabeledValue} from "@/types/commnon";
import Template from "@/views/hr/salary-voucher-rules/template.vue";
import CategorySelect from './select-component/category.vue'
import UserSelect from './select-component/user.vue'
import * as categoryApi from '@/api/fa/category'

const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const props = defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
  methods?: LabeledValue[];
  assetsStatus?: LabeledValue[];
  deptOptions?: [];
  subjectOptions?: [];
}>();


interface FormModel {
  code: string
  assetName: string
  categoryId: string
  specification: string
  manufacturer: string
  purchaseDate: string
  assetStatus: string
  location: string
  custodian: string
  usageDepartmentList: string[]
  originalValue: number
  residualRate: number
  netValue: number
  depreciationMethod: number
  depreciationTerms: number
  depreciationStartDate: string
  assetSubjectCode: string
  depreciationSubjectCode: string
  expenseSubjectCode: string
  purchaserSubjectCode: string
  liquidationSubjectCode: string
  remark: string
}


interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    code: '',
    assetName: '',
    categoryId: '',
    specification: '',
    manufacturer: '',
    purchaseDate: '',
    assetStatus: 'active',
    location: '',
    custodian: '',
    usageDepartmentList: [],
    originalValue: 0,
    residualRate: 5,
    netValue: 0,
    depreciationMethod: 1,
    depreciationTerms: 0,
    depreciationStartDate: '',
    assetSubjectCode: '',
    depreciationSubjectCode: '',
    expenseSubjectCode: '',
    purchaserSubjectCode: '',
    liquidationSubjectCode: '',
    remark: '',
  },
  rules: {
    code: [
      { required: true, message: '请输入资产编码', trigger: 'blur' },
      { min: 2, max: 25, message: '资产编码长度应为2-25个字符', trigger: 'blur' }
    ],
    assetName: [
      { required: true, message: '请输入资产名称', trigger: 'blur' },
      { min: 2, max: 30, message: '资产名称长度应为2-30个字符', trigger: 'blur' }
    ],
    categoryId: [
      { required: true, message: '请选择资产分类', trigger: 'change' }
    ],
    purchaseDate: [
      { required: true, message: '请选择购置日期', trigger: 'change' }
    ],
    originalValue: [
      { required: true, type: 'number', message: '请输入原值', trigger: 'blur' }
    ],
    residualRate: [
      { required: true, type: 'number', message: '请输入残值率', trigger: 'blur' },
      { type: 'number', min: 0, max: 100, message: '残值率应在0-100之间', trigger: 'blur' }
    ],
    depreciationMethod: [
      { required: true, type: 'number', message: '请选择折旧方法', trigger: 'change' }
    ],
    assetStatus: [
      { required: true, type: 'string', message: '请选择资产状态', trigger: 'change' }
    ],
    assetSubjectCode: [
      { required: true, type: 'string', message: '请选择固定资产科目', trigger: 'change' }
    ],
    depreciationSubjectCode: [
      { required: true, type: 'string', message: '请选择累计折旧科目', trigger: 'change' }
    ],
    expenseSubjectCode: [
      { required: true, type: 'string', message: '请选择折旧费用科目', trigger: 'change' }
    ],
    purchaserSubjectCode: [
      { required: true, type: 'string', message: '请选择资产购入对方科目', trigger: 'change' }
    ],
    liquidationSubjectCode: [
      { required: true, type: 'string', message: '请选择资产清理科目', trigger: 'change' }
    ],
  }
})
const {form, rules} = toRefs(data);
const span = ref(12);
const gutter = ref(20);
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

const treeProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name'
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
    code: '',
    assetName: '',
    categoryId: '',
    specification: '',
    manufacturer: '',
    purchaseDate: '',
    assetStatus: 'active',
    location: '',
    custodian: '',
    usageDepartmentList: [],
    originalValue: 0,
    residualRate: 5,
    netValue: 0,
    depreciationMethod: 1,
    depreciationTerms: 0,
    depreciationStartDate: '',
    assetSubjectCode: '',
    depreciationSubjectCode: '',
    expenseSubjectCode: '',
    purchaserSubjectCode: '',
    liquidationSubjectCode: '',
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

function handleCategorySelect(categoryId: string) {
  if (!categoryId) return;

  categoryApi.getOne(categoryId).then((res: any) => {
    const category = res.data;
    if (!category) return;

    form.value.depreciationMethod = category.depreciationMethod ?? 1
    form.value.depreciationTerms = category.depreciationYears ? category.depreciationYears * 12 : 0
    form.value.residualRate = category.residualRate ?? 5

    form.value.assetSubjectCode = category.assetSubjectCode ?? ''
    form.value.depreciationSubjectCode = category.depreciationSubjectCode ?? ''
    form.value.expenseSubjectCode = category.expenseSubjectCode ?? ''
  })
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

