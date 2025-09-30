<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="50%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px">
        <el-form-item label="规则名称" prop="ruleName">
          <el-input v-model="form.ruleName" placeholder="请输入规则名称"></el-input>
        </el-form-item>
        <el-form-item label="规则描述">
          <el-input
              v-model="form.ruleDescription"
              maxlength="100"
              type="textarea"
              show-word-limit
              placeholder="请输入规则描述"
          ></el-input>
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

      <!-- 公式构建区 -->
      <div class="section-margin">
        <h4 class="section-title">构建计算公式</h4>

        <!-- 可选项目 -->
        <div class="subsection-margin">
          <h5 class="subsection-title">工资项目：</h5>
          <div class="flex-container">
            <el-button
                v-for="item in salaryTypes"
                :key="item.id"
                @click="addItem(item)"
                size="small"
                :type="item.type === 'deduction' ? 'danger' : 'primary'"
                plain
                class="salary-type-btn"
            >
              {{ item.name }}
            </el-button>
          </div>
        </div>

        <!-- 运算符 -->
        <div class="subsection-margin">
          <h4 class="subsection-title">运算符：</h4>
          <div class="flex-container">
            <el-button
                v-for="op in operators"
                :key="op.symbol"
                @click="addOperator(op)"
                type="info"
                plain
                class="salary-type-btn"
            >
              {{ op.name }}
            </el-button>
          </div>
        </div>

        <!-- 公式显示区 -->
        <div class="formula-container">
          <draggable
              v-model="selectedItems"
              item-key="id"
              class="draggable-container"
              handle=".handle"
          >
            <template #item="{ element, index }">
              <div
                  class="item-container"
              >
                <GripVertical class="handle drag-icon" style="color: lightgray;"/>
                <span :class="{'deduction-text': element.type === 'deduction',
                'other-text': element.type !== 'deduction' && element.type !== 'operator'}">{{ element.name }}</span>
                <X class="delete-icon" @click="removeItem(index)"/>
              </div>
            </template>
          </draggable>
        </div>

        <!-- 公式预览 -->
        <div class="subsection-title" style="margin-top: 16px">
          当前公式：{{ formulaString }}
        </div>
      </div>
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
import {getCurrentInstance, PropType, reactive, ref, toRefs, watch, computed} from "vue";
import {ElForm} from "element-plus";
import draggable from 'vuedraggable'
import {GripVertical, X} from 'lucide-vue-next'
import {getOne, addOne, updateOne} from "@/api/config/formula";

const {t} = useI18n()
const emit: any = defineEmits(['dialogOfClosedMethods'])
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const dialogStatus: any = ref(false);
const {proxy} = getCurrentInstance()!;


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
  salaryTypes: {
    type: Array as PropType<{ id: string; type: string; name: string }[]>,
    default: () => [],
  },
  operators: {
    type: Array as PropType<{ id: string; name: string }[]>,
    default: () => [],
  }
})

const data: any = reactive({
  form: {
    status: 1,
  },
  rules: {
    ruleName: [
      {required: true, trigger: "blur", message: "请填写规则名称"}
    ]
  }
})

type Item = {};
type Operator = {};
type SelectedItem = Item | Operator;
const selectedItems = ref<SelectedItem[]>([]);
// 添加计算项
const addItem = (item: any) => {
  selectedItems.value.push({
    ...item
  })
}

// 添加运算符
const addOperator = (operator: any) => {
  selectedItems.value.push({
    ...operator,
    type: 'operator'
  })
}

// 删除项目
const removeItem = (index: any) => {
  selectedItems.value.splice(index, 1)
}

// 计算最终公式字符串
const formulaString = computed(() => {
  return selectedItems.value
      .map(item => item.name)
      .join(' ')
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
            selectedItems.value = res.data.formulaItems
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
    status: 1,
  };
  selectedItems.value = [];
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
      const operation: any = props.formId ? updateOne : addOne;
      const successMessage: any = props.formId
          ? t('org.success.update')
          : t('org.success.add');

      const newRule = {
        ...form.value,
        formulaItems: [...selectedItems.value],
        // formula: JSON.stringify(formulaJson),
        formulaString: formulaString.value
      }
      operation(newRule).then((res: any) => handleResponse(res, successMessage));
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

.section-margin {
  margin-bottom: 24px; /* mb-6 */
}

.section-title {
  font-size: 18px; /* text-lg */
  font-weight: 500; /* font-medium */
  margin-bottom: 16px; /* mb-4 */
}

.subsection-margin {
  margin-bottom: 16px; /* mb-4 */
}

.subsection-title {
  font-size: 14px; /* text-sm */
  color: #666; /* text-gray-600 */
  margin-bottom: 8px; /* mb-2 */
}

.flex-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px; /* gap-2 */
}

:deep(.salary-type-btn[class*=el-button]) {
  margin: 0 !important;
  height: 32px !important;
  line-height: 32px !important;
}

.formula-container {
  border: 1px solid #e2e8f0;
  border-radius: 8px;
  padding: 16px;
  min-height: 80px;
  background-color: #f8fafc;
}

.draggable-container {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.item-container {
  display: flex;
  align-items: center;
  gap: 4px;
  background-color: white;
  border: 1px solid #e2e8f0;
  border-radius: 4px;
  padding: 4px 8px;
}

.deduction-text {
  color: #ef4444;
}

.other-text {
  color: dodgerblue;
}

.drag-icon {
  width: 16px;
  height: 16px;
  cursor: move;
}

.delete-icon {
  width: 16px;
  height: 16px;
  cursor: pointer;
  color: lightgray; /* 默认颜色为灰色 */
  transition: color 0.3s ease; /* 添加过渡效果 */
}

.delete-icon:hover {
  color: #ef4444;
}
</style>
