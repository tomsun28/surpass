<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="40%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
        <el-form-item prop="name" label="账套名称" :required="true">
          <el-input v-model="form.name"/>
        </el-form-item>
        <el-form-item prop="standardId" label="会计准则" :required="true">
          <el-select v-model="form.standardId" clearable :disabled="props.formId">
            <el-option
                v-for="dict in accounting_standards"
                :key="dict.id"
                :label="dict.name"
                :value="dict.id"
            />
          </el-select>
<!--          <el-alert v-if="isEdit" type="info" show-icon :closable="false"
                    title="会计准则一旦发生更改，将影响当前账套下关联的所有会计科目。">
          </el-alert>-->
        </el-form-item>
        <el-form-item prop="enableDate" label="建账期间" :required="true">
          <el-date-picker
              style="width: 100%"
              v-model="form.enableDate"
              type="month"
              placeholder="选择账套启用年月"
              format="YYYY-MM"
              value-format="YYYY-MM"
          />
        </el-form-item>
        <el-form-item prop="vatType" label="纳税性质" :required="true">
          <el-select v-model="form.vatType" clearable>
            <el-option
                v-for="dict in vat_types"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item prop="companyName" label="单位名称" :required="true">
          <el-input v-model="form.companyName"/>
        </el-form-item>
        <el-form-item prop="creditCode" label="纳税人识别号">
          <el-input v-model="form.creditCode"/>
        </el-form-item>
        <el-form-item prop="industry" label="所属行业">
          <el-select v-model="form.industry" clearable>
            <el-option-group
                v-for="group in books_industry"
                :key="group.label"
                :label="group.label"
            >
              <el-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
              />
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item prop="address" label="单位地址">
          <el-input v-model="form.address"/>
        </el-form-item>
        <el-form-item prop="voucherReviewed" label="凭证审核">
          <el-radio-group v-model="form.voucherReviewed">
            <el-radio :value="0">关闭</el-radio>
            <el-radio :value="1">开启</el-radio>
          </el-radio-group>
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
        <el-button type="primary" :loading="loading" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {useI18n} from "vue-i18n";
import {getCurrentInstance, PropType, reactive, ref, toRefs, watch} from "vue";
import {ElForm} from "element-plus";
import {getOne, saveOne, updateOne} from "@/api/system/book/book";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const emit: any = defineEmits(['dialogOfClosedMethods'])
const loading: any = ref(false);

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
  accounting_standards: {
    type: Array as PropType<{ id: string; name: string }[]>,
    default: () => [],
  },
  vat_types: {
    type: Array as PropType<{ value: string; label: string }[]>,
    default: () => [],
  },
  books_industry: {
    type: Array,
    default: [],
  }
})

const data: any = reactive({
  form: {
    voucherReviewed: 0,
    status: 1
  },
  rules: {
    name: [
      {required: true, message: t('accountingStandardNameError'), trigger: "blur"},
    ],
    standardId: [
      {required: true, message: "会计准则不能为空", trigger: ['blur', 'change']},
    ],
    enableDate: [
      {required: true, message: "建账期间不能为空", trigger: ['blur', 'change']},
    ],
    vatType: [
      {required: true, message: "纳税性质不能为空", trigger: ['blur', 'change']},
    ],
    companyName: [
      {required: true, message: "单位名称不能为空", trigger: "blur"},
    ]
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
    voucherReviewed: 0,
    status: 1
  };
  isEdit.value = false;
  formRef?.value?.resetFields();
  loading.value = false;
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
