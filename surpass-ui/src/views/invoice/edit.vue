<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="75%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px" inline-message>
        <el-row>
          <el-col :span="12">
            <el-form-item label="开票日期" prop="invoiceDate" :required="true">
              <el-date-picker clearable style="width: 70%"
                              v-model="form.invoiceDate"
                              type="date"
                              value-format="YYYY-MM-DD"
                              placeholder="请选择开票日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="invoiceType" label="发票类型" :required="true">
              <el-select v-model="form.invoiceType" clearable style="width: 70%">
                <el-option
                    v-for="dict in invoice_types"
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
            <el-form-item label="发票号码" prop="invoiceNo" :required="true">
              <el-input v-model="form.invoiceNo" placeholder="请填写发票号码" style="width: 70%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发票代码" prop="invoiceCode" :required="true">
              <el-input v-model="form.invoiceCode" placeholder="请填写发票代码" style="width: 70%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item prop="status" label="发票状态" :required="true">
              <el-select v-model="form.status" clearable style="width: 70%">
                <el-option
                    v-for="dict in filteredStatus"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <h5 class="section-title" style="color: gray" v-if="props.invoiceDirection === 'INPUT'">购方信息</h5>
        <el-row v-if="props.invoiceDirection === 'INPUT'">
          <el-col :span="12">
            <el-form-item label="购方名称" prop="buyerName" :required="true">
              <el-input v-model="form.buyerName" placeholder="请填写购方名称" style="width: 70%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="购方税号" prop="buyerTaxNo">
              <el-input v-model="form.buyerTaxNo" placeholder="请填写购方税号" style="width: 70%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <h5 class="section-title" style="color: gray" v-if="props.invoiceDirection === 'OUTPUT'">销方信息</h5>
        <el-row v-if="props.invoiceDirection === 'OUTPUT'">
          <el-col :span="12">
            <el-form-item label="销方名称" prop="sellerName" :required="true">
              <el-input v-model="form.sellerName" placeholder="请填写销方名称" style="width: 70%"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="销方税号" prop="sellerTaxNo">
              <el-input v-model="form.sellerTaxNo" placeholder="请填写销方税号" style="width: 70%"/>
            </el-form-item>
          </el-col>
        </el-row>
        <!--统计行-->
        <h5 class="section-title" style="color: gray">发票明细</h5>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="金额合计" prop="totalAmount" :required="true">
              <el-input v-model="form.totalAmount" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="税额" prop="taxAmount" :required="true">
              <el-input v-model="form.taxAmount" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="价税合计" prop="totalWithTax" :required="true">
              <el-input v-model="form.totalWithTax" disabled></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-table
            :data="form.items"
            border
            size="small"
            @cell-click="cellClick"
            :row-style="{ height: '46px' }"
        >
          <el-table-column label="商品名称" align="center" prop="itemName" width="190" :show-overflow-tooltip="true">
            <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'itemName')">
                {{ scope.row.itemName }}
              </span>
              <el-input
                  v-else
                  v-model="scope.row.itemName"
                  placeholder="请输入"
                  @blur="closeEditAll"
              />
            </template>
          </el-table-column>

          <el-table-column label="规格型号" align="center" prop="specModel" width="140" :show-overflow-tooltip="true">
            <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'specModel')">
                {{ scope.row.specModel }}
              </span>
              <el-input
                  v-else
                  v-model="scope.row.specModel"
                  placeholder="请输入"
                  @blur="closeEditAll"
              />
            </template>
          </el-table-column>

          <el-table-column label="单位" align="center" prop="unit" width="95">
            <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'unit')">
                {{ scope.row.unit }}
              </span>
              <el-input
                  v-else
                  v-model="scope.row.unit"
                  placeholder="请输入"
                  @blur="closeEditAll"
              />
            </template>
          </el-table-column>

          <!-- 数量 -->
          <el-table-column label="数量" align="center" prop="quantity" width="120">
            <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'quantity')">
                {{ scope.row.quantity }}
              </span>
              <el-input
                  v-else
                  v-model="scope.row.quantity"
                  placeholder="请输入"
                  @input="(val: string) => handleInput(val, scope.row, 'quantity')"
                  @blur="() => { formatField(scope.row, 'quantity'); closeEditAll(); }"
              />
            </template>
          </el-table-column>

          <!-- 单价 -->
          <el-table-column label="单价" align="center" prop="unitPrice" width="105">
            <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'unitPrice')">
                {{ scope.row.unitPrice }}
              </span>
              <el-input
                  v-else
                  v-model="scope.row.unitPrice"
                  placeholder="请输入"
                  @input="(val: string) => handleInput(val, scope.row, 'unitPrice')"
                  @blur="() => { formatField(scope.row, 'unitPrice'); closeEditAll(); }"
              />
            </template>
          </el-table-column>

          <!-- 金额（不含税）只读 -->
          <el-table-column label="金额" prop="amount" width="140" align="center"/>

          <!-- 税率 -->
          <el-table-column label="税率（%）" align="center" prop="taxRate" width="80">
            <template #default="scope">
              <span v-if="!(editingCell.rowIndex === scope.$index && editingCell.prop === 'taxRate')">
                {{ scope.row.taxRate }}
              </span>
              <el-input
                  v-else
                  v-model="scope.row.taxRate"
                  placeholder="请输入"
                  @input="(val: string) => handleInput(val, scope.row, 'taxRate')"
                  @blur="() => { formatField(scope.row, 'taxRate'); closeEditAll(); }"
              />
            </template>
          </el-table-column>

          <!-- 商品税额（只读） -->
          <el-table-column label="商品税额" prop="taxAmount" width="130" align="center"/>

          <!-- 价税合计（只读） -->
          <el-table-column label="价税合计" prop="totalWithTax" width="140" align="center"/>

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

        <el-button icon="Plus" style="width: 100%" @click="addNewItem">添加明细</el-button>
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
import { reactive, ref, toRefs, watch, onMounted, onBeforeUnmount, computed} from "vue";
import { useI18n } from "vue-i18n";
import modal from "@/plugins/modal";
import { getOne, saveOne, updateOne } from "@/api/invoice/invoice";
import { LabeledValue } from "@/types/commnon";

const emit: any = defineEmits(['dialogOfClosedMethods']);
const dialogStatus: any = ref(false);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n();

const props = withDefaults(defineProps<{
  title?: string;
  open?: boolean;
  formId?: string;
  invoice_types?: LabeledValue[];
  invoiceDirection?: string;
  invoice_status?: LabeledValue[];
}>(), {
  invoiceDirection: 'INPUT'
});

interface FormModel {
  invoiceDate: string,
  invoiceType: string,
  invoiceNo: string,
  invoiceCode: string,
  buyerName: string,
  buyerTaxNo: string,
  sellerName: string,
  sellerTaxNo: string,
  status: string,
  totalAmount: string,
  taxAmount: string,
  totalWithTax: string,
  items: Array<any>
}

interface FormState {
  form: FormModel
  rules: Record<string, any>
}

const data = reactive<FormState>({
  form: {
    invoiceDate: '',
    invoiceType: '',
    invoiceNo: '',
    invoiceCode: '',
    buyerName: '',
    buyerTaxNo: '',
    sellerName: '',
    sellerTaxNo: '',
    status: 'NEW',
    totalAmount: '',
    taxAmount: '',
    totalWithTax: '',
    items: []
  },
  rules: {}
});

const { form, rules } = toRefs(data);
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
          });
        } else {
          reset();
        }
      } else {
        reset();
      }
    },
    { immediate: true }
);

// 监听items数组变化，自动更新合计
watch(() => form.value.items, () => {
  updateTotals();
}, { deep: true });

function reset() {
  form.value = {
    invoiceDate: '',
    invoiceType: '',
    invoiceNo: '',
    invoiceCode: '',
    buyerName: '',
    buyerTaxNo: '',
    sellerName: '',
    sellerTaxNo: '',
    status: 'NEW',
    totalAmount: '',
    taxAmount: '',
    totalWithTax: '',
    items: []
  };
  formRef?.value?.resetFields();
}

const filteredStatus = computed(() => {
  if (props.invoiceDirection === 'OUTPUT') {
    return (props.invoice_status ?? []).filter(x =>
        ["NEW","ISSUED","COLLECTED","ARCHIVED","CANCELED","RED_INVOICE","REPLACED"]
            .includes(x.value as string)
    )
  }
  if (props.invoiceDirection === 'INPUT') {
    return (props.invoice_status ?? []).filter(x =>
        ["NEW","RECEIVED","PAID","ARCHIVED","CANCELED","RED_INVOICE"]
            .includes(x.value as string)
    )
  }
  return props.invoice_status ?? []
})

// 1. 添加计算属性来自动计算合计
const totals = computed(() => {
  let totalAmount = 0;      // 金额合计（不含税）
  let totalTaxAmount = 0;   // 税额合计
  let totalWithTax = 0;     // 价税合计

  form.value.items.forEach(item => {
    const amount = parseFloat(item.amount) || 0;
    const taxAmount = parseFloat(item.taxAmount) || 0;
    const itemTotal = parseFloat(item.totalWithTax) || 0;

    totalAmount += amount;
    totalTaxAmount += taxAmount;
    totalWithTax += itemTotal;
  });

  return {
    totalAmount: totalAmount.toFixed(2),
    taxAmount: totalTaxAmount.toFixed(2),
    totalWithTax: totalWithTax.toFixed(2)
  };
});

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

// 点击单元格时进入编辑模式
const cellClick = (row: any, column: any, cell: HTMLTableCellElement, event: Event) => {
  if (column.property) {
    const rowIndex = form.value.items.indexOf(row);
    editingCell.value = { rowIndex, prop: column.property };
  }
  event.stopPropagation();
};

// 关闭所有编辑状态
function closeEditAll() {
  editingCell.value = { rowIndex: null, prop: null };
}

// 点击表格外部关闭编辑
function handleClickOutside(event: MouseEvent) {
  const table = document.querySelector(".el-table");
  if (table && !table.contains(event.target as Node)) {
    closeEditAll();
  }
}

// 输入阶段：只做数字校验，不打断用户输入
function handleInput(val: string, row: any, field: 'quantity' | 'unitPrice' | 'taxRate') {
  // 保留数字和一个小数点
  let cleaned = val.replace(/[^\d.]/g, '').replace(/\.{2,}/g, '.');
  if (cleaned.includes('.')) {
    const [intPart, decPart] = cleaned.split('.');
    cleaned = intPart + '.' + decPart.slice(0, 2); // 小数点后限制两位
  }

  if (field === 'taxRate') {
    const num = parseFloat(cleaned) || 0;
    if (num > 100) cleaned = '100';
  }

  row[field] = cleaned;
  updateRow(row);
}

// 失焦阶段：补齐两位小数
function formatField(row: any, field: 'quantity' | 'unitPrice' | 'taxRate') {
  const num = parseFloat(row[field]);
  if (!isNaN(num)) row[field] = num.toFixed(2);
  updateRow(row);
}

// 更新金额、税额、价税合计
function updateRow(row: any) {
  const unitPrice = parseFloat(row.unitPrice);
  const quantity = parseFloat(row.quantity);
  const taxRate = parseFloat(row.taxRate);

  // 金额（不含税）
  row.amount = (!isNaN(unitPrice) && !isNaN(quantity))
      ? (unitPrice * quantity).toFixed(2)
      : '';

  const amountNum = parseFloat(row.amount) || 0;
  const taxRateNum = !isNaN(taxRate) ? taxRate : 0;

  // 税额
  row.taxAmount = amountNum > 0 ? (amountNum * taxRateNum / 100).toFixed(2) : '';

  const taxAmountNum = parseFloat(row.taxAmount) || 0;

  // 价税合计
  row.totalWithTax = amountNum > 0 ? (amountNum + taxAmountNum).toFixed(2) : '';

  // 更新合计数据
  updateTotals();
}

// 3. 添加更新合计的函数
function updateTotals() {
  form.value.totalAmount = totals.value.totalAmount;
  form.value.taxAmount = totals.value.taxAmount;
  form.value.totalWithTax = totals.value.totalWithTax;
}


// 添加删除项目的方法
function deleteItem(index: number) {
  form.value.items.splice(index, 1);
  updateTotals();
}

// 在添加新行时初始化默认值
function addNewItem() {
  form.value.items.push({
    itemName: '',
    specModel: '',
    unit: '',
    quantity: 0,
    unitPrice: 0,
    amount: 0,
    taxRate: 0,
    taxAmount: 0,
    totalAmount: 0,
    totalWithTax: 0
  });
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

onMounted(() => {
  document.addEventListener("click", handleClickOutside);
});
onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside);
});
</script>
