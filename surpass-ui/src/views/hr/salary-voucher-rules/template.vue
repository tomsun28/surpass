<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="65%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <div class="queryForm">
        <el-form :model="form" ref="formRef" :inline="true" label-width="68px">
          <el-form-item label="凭证类型">
            <el-select v-model="form.voucherType" style="width: 200px" @change="handleChangeType">
              <el-option v-for="item in voucherTypes" :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="凭证字">
            <el-select v-model="form.wordHead" style="width: 200px" disabled>
              <el-option v-for="item in voucherTypes" :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
        </el-form>
        <el-table v-loading="loading" :data="dataList" border>
          <el-table-column prop="summary" label="摘要" align="left" min-width="90"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <el-input v-model="scope.row.summary" style="width: 100%"></el-input>
            </template>
          </el-table-column>
          <el-table-column prop="direction" label="借/贷" align="center" min-width="40">
            <template #default="scope">
              <el-select v-model="scope.row.direction" style="width: 100%">
                <el-option v-for="item in salary_directions" :label="item.label" :value="item.value"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="subjectName" label="科目" align="left" min-width="110" :show-overflow-tooltip="true">
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
          <el-table-column prop="selectedValue" label="取值" align="center" min-width="60"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <el-select v-model="scope.row.selectedValue" style="width: 100%">
                <el-option v-for="item in salaryValuesList" :label="item.label" :value="item.value"/>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" header-align="center" width="120">
            <template #default="scope">
              <div >
                <div>
                  <el-tooltip content="新增">
                    <el-button type="primary"
                               link @click="handleAdd()" icon="Plus"></el-button>
                  </el-tooltip>
                  <el-tooltip content="移除" v-if="dataList.length > 1">
                    <el-button type="primary"
                               link  @click="handleDel(scope.$index)" icon="Delete"></el-button>
                  </el-tooltip>
                </div>
              </div>
            </template>
          </el-table-column>
        </el-table>
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
import {getCurrentInstance, reactive, ref, toRefs, watch} from "vue"
import {addRule, getById, updateRule} from "@/api/system/hr/voucher-rule";
import {ElForm} from "element-plus";
import {delConfigBalanceSheet} from "@/api/system/standard/standard-statement-balance-sheet";
import modal from "@/plugins/modal";
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;

const {salary_values, salary_directions, labor_fee_values}
    = proxy?.useDict( "salary_values", "salary_directions", "labor_fee_values");
const emit: any = defineEmits(['dialogOfClosedMethods'])
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
interface SalaryValueItem {
  label: string
  value: string | number
}

const salaryValuesList = ref<SalaryValueItem[]>([])

const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  },
  deptOptions: {
    type: Array,
    default: [],
  },
  voucherTypes: {
    type: Array<any>,
    default: [],
  },
  wordHeads: {
    type: Array<any>,
    default: []
  },
  formId: {
    default: undefined
  },
})
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
const data: any = reactive({
  form: {
    voucherType: 0,
    wordHead: '记'
  },
})

const {form} = toRefs(data);
const dialogStatus: any = ref(false);
const loading = ref(false);
// 1. 首先，为数据项定义一个明确的接口类型
interface VoucherItem {
  id: null;
  summary: string;
  direction: string;
  subjectCode: string | null;
  selectedValue: string;
  // 其他可能的字段...
  [key: string]: any; // 这允许添加其他可能从后端返回的字段
}

// 2. 为dataList提供正确的类型
const dataList = ref<VoucherItem[]>([]);
const typeTextMap: Record<number, string> = {
  0: "计提{yy}年{mm}月工资",
  1: "发放{yy}年{mm}月工资",
  2: "收票{yy}年{mm}月兼职人员工资",
  3: "发放{yy}年{mm}月兼职人员工资"
}

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getById(props.formId).then((res: any) => {
            form.value = res.data;
            dataList.value = res.data.employeeSalaryVoucherRules;
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

/** 重置操作表单 */
function reset(): any {
  form.value = {
    voucherType: 0,
    wordHead: '记'
  }

  // 先根据 voucherType 给 salaryValuesList 赋值
  if (form.value.voucherType === 0 || form.value.voucherType === 1) {
    salaryValuesList.value = salary_values.value
  } else {
    salaryValuesList.value = labor_fee_values.value
  }

  // 取第一个值
  const selectedValue = salaryValuesList.value.length > 0 ? salaryValuesList.value[0].value : null

  // 设置 dataList
  const summaryTemplate = typeTextMap[form.value.voucherType] || ""

  dataList.value = [
    {
      id: null,
      summary: summaryTemplate,
      direction: "1",
      subjectCode: props.deptOptions?.[0]?.code || null,
      selectedValue: selectedValue,
    }
  ]

  formRef?.value?.resetFields()
}



function addRecord() {
  const summaryTemplate = typeTextMap[form.value.voucherType] || "";

  // 根据 voucherType 先切换数据源
  if (form.value.voucherType === 0 || form.value.voucherType === 1) {
    salaryValuesList.value = salary_values.value;
  } else {
    salaryValuesList.value = labor_fee_values.value;
  }

  // 如果 salaryValuesList 为空，安全处理（防止访问 undefined）
  const selectedValue = salaryValuesList.value.length > 0 ? salaryValuesList.value[0].value : null;

  // 新增一条记录
  dataList.value.push({
    id: null,
    summary: summaryTemplate,
    direction: "1",
    subjectCode: props.deptOptions?.[0]?.code || null,
    selectedValue: selectedValue,
  });
}

function handleChangeType() {
  // 根据 voucherType 先切换数据源
  if (form.value.voucherType === 0 || form.value.voucherType === 1) {
    salaryValuesList.value = salary_values.value;
  } else {
    salaryValuesList.value = labor_fee_values.value;
  }
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function submitForm() {
  if (dataList.value.length < 2) {
    modal.msgWarning("请至少录入两条数据！");
    return;
  }

  // 检查是否至少有一贷一借
  const hasDebit = dataList.value.some((item: any) => item.direction == '1');
  const hasCredit = dataList.value.some((item: any) => item.direction == '2');

  if (!hasDebit || !hasCredit) {
    modal.msgWarning("请至少录入一条借方和一条贷方数据！");
    return;
  }

  const dataSave = {
    ...form.value,
    salaryTemplateDetailDtos: dataList.value
  }

  if (props.formId) {
    //修改
    updateRule(dataSave).then((response: any) => {
      if (response.code === 0) {
        modal.msgSuccess("修改成功")
        dialogOfClosedMethods(true);
        reset();
      } else {
        modal.msgError(response.message)
      }
    })
  } else {
    addRule(dataSave).then((response: any) => {
      if (response.code === 0) {
        modal.msgSuccess("新增成功")
        dialogOfClosedMethods(true);
        reset();
      } else {
        modal.msgError(response.message)
      }
    })
  }
}

function handleAdd() {
  addRecord();
}

function handleDel(index: any) {
  dataList.value.splice(index, 1)
}
</script>
