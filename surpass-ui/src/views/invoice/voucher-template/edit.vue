<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="990px"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-row>
          <el-col :span="12">
            <el-form-item label="模板名称" prop="templateName" :required="true">
              <el-input v-model="form.templateName" placeholder="请填写模板名称" style="width: 100%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="wordHead" label="凭证字" :required="true">
              <el-select v-model="form.wordHead" clearable style="width: 100%" disabled>
                <el-option
                    v-for="dict in wordHeads"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
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
          </el-col>
<!--          <el-col :span="12">
            <el-form-item prop="status" label="默认模板">
              <el-switch
                  disabled
                  :width="44"
                  v-model="form.isDefault"
                  :active-value="1"
                  :inactive-value="0"
                  active-icon-class="el-icon-close"
                  inactive-icon-class="el-icon-check">
              </el-switch>
            </el-form-item>
          </el-col>-->
        </el-row>
      </el-form>

      <el-table
          :data="form.items"
          border
          size="small"
          @cell-click="cellClick"
          :row-style="{ height: '46px' }"
      >
        <el-table-column label="摘要" align="center" prop="summary" width="250" :show-overflow-tooltip="true">
          <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'summary')">
                {{ scope.row.summary }}
              </span>
            <el-input
                v-else
                v-model="scope.row.summary"
                placeholder="请输入"
                @blur="closeEditAll"
            />
          </template>
        </el-table-column>
        <el-table-column prop="subjectName" label="科目" align="center" width="300" :show-overflow-tooltip="true">
          <template #default="scope">
            <el-cascader
                style="width: 100%"
                filterable
                v-model="scope.row.subjectCode"
                :options="deptOptions"
                :props="defaultProps"
            />
          </template>
        </el-table-column>
        <el-table-column label="方向" align="center" prop="direction" width="100">
          <template #default="scope">
            <!-- 非编辑状态显示 label -->
            <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'direction')">
             {{ directions?.find(d => d.value === scope.row.direction)?.label || '' }}
            </span>
            <!-- 编辑状态 -->
            <el-select
                v-else
                v-model="scope.row.direction"
                placeholder="选择"
                @blur="closeEditAll"
            >
              <el-option
                  v-for="dict in directions"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="取值" align="center" prop="selectedValue" width="200">
          <template #default="scope">
            <!-- 非编辑状态显示 label -->
            <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'selectedValue')">
             {{ selectedValues?.find(d => d.value === scope.row.selectedValue)?.label || '' }}
            </span>
            <!-- 编辑状态 -->
            <el-select
                v-else
                v-model="scope.row.selectedValue"
                placeholder="选择"
                @blur="closeEditAll"
            >
              <el-option
                  v-for="dict in selectedValues"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </template>
        </el-table-column>
        <!-- 操作 -->
        <el-table-column label="操作" align="center" width="100">
          <template #default="scope">
            <el-popconfirm
                title="确认删除吗？"
                @confirm="deleteItem(scope.$index)"
            >
              <template #reference>
                <el-button size="small" icon="Delete"></el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <el-button icon="Plus" style="width: 100%" @click="addNewItem"></el-button>
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
import {reactive, ref, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import {getOne, saveOne, updateOne} from "@/api/invoice/voucher-template";
import {LabeledValue} from "@/types/commnon";
import Template from "@/views/hr/salary-voucher-rules/template.vue";
import modal from "@/plugins/modal";

const emit: any = defineEmits(['dialogOfClosedMethods']);
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n();

const props = withDefaults(defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
  wordHeads?: LabeledValue[];
  directions?: LabeledValue[];
  selectedValues?: LabeledValue[];
  deptOptions: {
    type: Array<any>,
    default: [],
  },
  invoiceDirection?: string;
}>(), {
  invoiceDirection: 'INPUT'
});

interface FormModel {
  templateName: string,
  wordHead: string,
  status: number
  isDefault: number
  items: Array<any>
}

interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    templateName: '',
    wordHead: '记',
    status: 1,
    isDefault: 0,
    items: []
  },
  rules: {}
});

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
  filterable: true
})
const {form, rules} = toRefs(data);
const loading: any = ref(false);

// 当前正在编辑的单元格
const editingCell = ref<{ rowIndex: number | null; prop: string | null }>({
  rowIndex: null,
  prop: null,
});


// 初始化或表单打开
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getOne(props.formId).then((res: any) => {
            form.value = res.data;
            // 确保序号按顺序排列
            updateSeqNo();
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

function reset() {
  form.value = {
    templateName: '',
    wordHead: '记',
    status: 1,
    isDefault: 0,
    items: []
  };
  formRef?.value?.resetFields();
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

// 点击单元格时进入编辑模式
const cellClick = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  if (column.property) {
    const rowIndex = form.value.items.indexOf(row);
    editingCell.value = {rowIndex, prop: column.property};
  }
  event.stopPropagation();
};

// 在添加新行时初始化默认值
function addNewItem() {
  form.value.items.push({
    seqNo: form.value.items.length + 1, // 按列表长度+1设置序号
    direction: '1',
    subjectCode: '', // 修正字段名
    summary: '',
    selectedValue: 'TOTAL_AMOUNT', // 修正字段名
  });
}

// 关闭所有编辑状态
function closeEditAll() {
  editingCell.value = {rowIndex: null, prop: null};
}

// 添加删除项目的方法
function deleteItem(index: number) {
  form.value.items.splice(index, 1);
  // 删除后重新排序seqNo
  updateSeqNo();
}

// 添加更新序号的方法
function updateSeqNo() {
  form.value.items.forEach((item, index) => {
    item.seqNo = index + 1;
  });
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
      const formData = {...form.value, direction: props.invoiceDirection}
      operation(formData).then((res: any) => handleResponse(res, successMessage));
    }
  });
}
</script>
