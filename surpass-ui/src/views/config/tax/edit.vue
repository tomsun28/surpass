<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="150px">
        <el-form-item label="层级：" prop="level" :required="true">
          <el-input-number :min="0" v-model="form.level" :disabled="true" controls-position="right">
          </el-input-number>
        </el-form-item>
        <el-form-item label="应纳税所得额级距：" prop="taxRange">
          <el-col :span="11">
            <el-input-number
                v-model="form.minNum"
                :min="0"
                controls-position="right"
                style="width: 100%"
            >
              <template #prefix>
                <span>￥</span>
              </template>
              <template #decrease-icon>
                <el-icon>
                  <Minus />
                </el-icon>
              </template>
              <template #increase-icon>
                <el-icon>
                  <Plus />
                </el-icon>
              </template>
            </el-input-number>
          </el-col>
          <el-col class="text-center" :span="2">
            <span class="text-gray-500">至</span>
          </el-col>
          <el-col :span="11">
            <el-input-number
                v-model="form.maxNum"
                :min="0"
                controls-position="right"
                style="width: 100%"
                placeholder="无上限金额的时候无需填写"
            >
              <template #prefix>
                <span>￥</span>
              </template>
              <template #decrease-icon>
                <el-icon>
                  <Minus />
                </el-icon>
              </template>
              <template #increase-icon>
                <el-icon>
                  <Plus />
                </el-icon>
              </template>
            </el-input-number>
          </el-col>
        </el-form-item>
        <el-form-item label="税率：" prop="taxRate" :required="true">
          <el-input-number
              v-model="form.taxRate"
              :min="0"
              :max="100"
              controls-position="right"
          >
            <template #suffix>
              <span>%</span>
            </template>
          </el-input-number>
        </el-form-item>
        <el-form-item prop="type" label="类型：">
          <el-radio-group v-model="form.type" disabled>
            <el-radio :value="0">工资薪金</el-radio>
            <el-radio :value="1">劳务报酬</el-radio>
          </el-radio-group>
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
import {addTax, getOne, updateTax} from "@/api/config/tax";
const {t} = useI18n()
const emit: any = defineEmits(['dialogOfClosedMethods'])
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const dialogStatus: any = ref(false);
const {proxy} = getCurrentInstance()!;
import {validateFields} from "@/api/commonApi"


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
  defaultLevel: {
    type: Number,
    default: 1
  },
  taxType: {
    type: Number,
    default: 0
  }
})

const data: any = reactive({
  form: {
    minNum: 0,
    level: props.defaultLevel
  },
  rules: {
    taxRate: [
      {required: true, trigger: "blur", message: "税率不能为空"}
    ]
  }
})

const {form, rules} = toRefs(data);

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

/** 重置操作表单 */
function reset(): any {
  form.value = {
    minNum: 0,
    level: props.defaultLevel,
    type: props.taxType
  };
  formRef?.value?.resetFields();
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
      const operation: any = props.formId ? updateTax : addTax;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');
      operation(form.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}
</script>

<style lang="scss" scoped>

.line-input {
  display: flex;
  align-items: center;
  gap: 8px;

  &-num {
    width: 10%;
  }
}
</style>
