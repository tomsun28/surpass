<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="100px">
          <el-form-item label="报销人">
            <employee-selector v-model="queryParams.employeeId" @change="getList"></employee-selector>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="queryParams.status" @change="getList" style="width: 200px" clearable>
              <el-option label="草稿" value="draft"/>
              <el-option label="已提交" value="submitted"/>
              <el-option label="已审批" value="approved"/>
              <el-option label="已驳回" value="rejected"/>
              <el-option label="已记账" value="posted"/>
              <el-option label="已付款" value="paid"/>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
            <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 列表 -->
    <el-card class="common-card">
      <div class="btn-form">
        <el-button type="primary" @click="handleAdd">{{ t('org.button.add') }}</el-button>
        <el-button type="success" :disabled="ids.length === 0">提交</el-button>
        <el-button type="success" :disabled="ids.length === 0">审批</el-button>
        <el-button type="success" :disabled="ids.length === 0">记账</el-button>
      </div>

      <el-table v-loading="loading" :data="reportList"  border @selection-change="handleSelectionChange" row-key="id" min-height="600">
        <el-table-column prop="employeeName" label="报销人" align="center"/>
        <el-table-column prop="title" label="标题" align="center"/>
        <el-table-column prop="totalAmount" label="金额" align="center"/>
        <el-table-column prop="status" label="状态" align="center">
          <template #default="scope">
            <span>{{ statusLabelMap[scope.row.status] || scope.row.status }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="submitTime" label="提交时间" align="center" width="180"/>
        <el-table-column prop="approveTime" label="审批时间" align="center" width="180"/>
        <el-table-column prop="accountingTime" label="记账时间" align="center" width="180"/>
        <el-table-column prop="paymentTime" label="付款时间" align="center" width="180"/>
        <el-table-column prop="remarks" label="备注" align="center"/>
        <el-table-column label="操作" align="center" width="120" fixed="right">
          <template #default="scope">
            <el-button type="text" @click="handleEdit(scope.row)" icon="Edit"></el-button>
            <el-button type="text" @click="handleDel(scope.row)" icon="Delete"></el-button>
          </template>
        </el-table-column>
        <template #empty>
          <div class="empty-text">暂无数据</div>
        </template>
      </el-table>

      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-card>

    <!-- 弹窗表单 -->
    <el-dialog v-model="dialog.visible" :title="dialog.title" width="1200px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="ID" prop="id" style="display: none">
          <el-input v-model="form.id"/>
        </el-form-item>
        <el-form-item label="报销人" prop="employeeId" :required="true">
          <employee-selector v-model="form.employeeId" @change="handleEmpUser"/>
        </el-form-item>
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title"/>
        </el-form-item>
        <el-form-item label="金额" prop="totalAmount" :required="true">
          <el-input-number style="width: 240px" :min="0" :precision="2" v-model="form.totalAmount">
            <template #suffix>
              元
            </template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="提交时间" prop="submitTime">
          <el-date-picker v-model="form.submitTime" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>

        <!-- 明细项表格 -->
        <el-form-item label="费用明细" required>
          <el-button type="primary" @click="openDetailDialog()">新增明细</el-button>
          <el-table :data="form.items" style="margin-top: 10px" border>
            <el-table-column label="费用类型" prop="expenseType">
              <template #default="scope">
                <dict-tag :options="expense_type" :value="scope.row.expenseType"></dict-tag>
              </template>
            </el-table-column>
            <el-table-column label="描述" prop="description"/>
            <el-table-column label="金额" prop="amount"/>
            <el-table-column label="科目" prop="accountCode">
              <template #default="scope">
                {{ getSubjectName(scope.row.accountCode) }}
              </template>
            </el-table-column>
            <el-table-column label="发票号" prop="invoiceNo"/>
            <el-table-column label="操作" align="center" width="120">
              <template #default="{ row, $index }">
                <el-button type="text" @click="openDetailDialog(row, $index)" icon="Edit"></el-button>
                <el-button type="text" @click="removeDetail($index)" icon="Delete"></el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status">
            <el-option label="草稿" value="draft"/>
            <el-option label="已提交" value="submitted"/>
            <el-option label="已审批" value="approved"/>
            <el-option label="已驳回" value="rejected"/>
            <el-option label="已记账" value="posted"/>
            <el-option label="已付款" value="paid"/>
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remarks">
          <el-input v-model="form.remarks" type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="buttonLoading" @click="submitForm">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialog.visible" :title="detailDialog.title" width="600px">
      <el-form :model="detailForm" ref="detailFormRef" :rules="detailRules" label-width="100px">
        <el-form-item label="费用类型" prop="expense_type">
          <el-select v-model="detailForm.expenseType" placeholder="请选择费用类型">
            <el-option v-for="(item, index) in expense_type" :key="index" :label="item.label"
                       :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="detailForm.description"/>
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input-number v-model="detailForm.amount" :min="0" :precision="2" style="width: 200px"/>
        </el-form-item>
        <el-form-item label="会计科目" prop="account_code">
          <el-tree-select
              ref="resTreeRef"
              v-model="detailForm.accountCode"
              :data="subjectOptions"
              :props="defaultProps"
              check-strictly
              value-key="id"
              show-checkbox
              clearable
          />
        </el-form-item>
        <el-form-item label="发票号" prop="invoiceNo">
          <el-input v-model="detailForm.invoiceNo"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="detailDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="saveDetail">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>


<script setup lang="ts">
import {ref, reactive, toRefs, getCurrentInstance} from 'vue'
import {ElForm} from 'element-plus'
import * as expenseReportApi from '@/api/expense/expenseReport'
import {useI18n} from 'vue-i18n'
import EmployeeSelector from "@/components/EmployeeSelector/index.vue"
import {parseTime} from "@/utils/Surpass"
import booksSetStore from "@/store/modules/bookStore";
import {getTree} from "@/api/system/standard/standard-subject";
import DictTag from "@/components/DictTag/index.vue";

const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const {proxy} = getCurrentInstance()!
const {expense_type} = proxy?.useDict("expense_type")
const currBookStore = booksSetStore()
const {t} = useI18n()
const loading = ref(false)
const buttonLoading = ref(false)
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryParams = reactive({
  pageNumber: 1,
  pageSize: 10,
  employeeId: '',
  status: ''
})
const total: any = ref(0);
const subjectOptions: any = ref<any[]>([]);
let subjectList: any = ref([]);
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
  disabled: disabledFilter
})

const form = reactive({
  id: '',
  employeeId: '',
  departmentId: '',
  title: '',
  totalAmount: 0,
  status: 'draft',
  submitTime: parseTime(new Date(), "{y}-{m}-{d} {h}:{i}:{s}"),
  approveTime: '',
  accountingTime: '',
  paymentTime: '',
  remarks: '',
  items: []
})

const rules = {
  employeeId: [{required: true, message: "请选择报销人", trigger: 'blur'}],
  departmentId: [{required: true, message: "请选择部门", trigger: 'blur'}],
  totalAmount: [{required: true, message: "请输入总金额", trigger: 'blur'}],
  status: [{required: true, message: "请选择状态", trigger: 'blur'}]
}

// 状态转中文标签映射
const statusLabelMap: Record<string, string> = {
  draft: '草稿',
  submitted: '已提交',
  approved: '已审批',
  rejected: '已驳回',
  posted: '已记账',
  paid: '已付款'
}

// 明细弹窗控制
const detailDialog = reactive({
  visible: false,
  title: '新增明细',
  editIndex: -1
})

const detailForm = reactive({
  expenseType: '',
  description: '',
  amount: 0,
  accountCode: '',
  invoiceNo: ''
})

const detailFormRef = ref()
const detailRules = {
  expenseType: [{required: true, message: '请选择费用类型', trigger: 'change'}],
  amount: [{required: true, message: '请输入金额', trigger: 'blur'}],
  accountCode: [{required: true, message: '请输入会计科目', trigger: 'blur'}],
  invoiceNo: [{required: true, message: '请输入发票号', trigger: 'blur'}]
}

// 打开新增/编辑明细弹窗
function openDetailDialog(row?: any, index = -1) {
  if (row) {
    Object.assign(detailForm, row)
    detailDialog.title = '编辑明细'
    detailDialog.editIndex = index
  } else {
    Object.assign(detailForm, {
      expenseType: '',
      description: '',
      amount: 0,
      accountCode: '',
      invoiceNo: ''
    })
    detailDialog.title = '新增明细'
    detailDialog.editIndex = -1
  }
  detailDialog.visible = true
}

// 保存明细
function saveDetail() {
  detailFormRef.value?.validate((valid: boolean) => {
    if (!valid) return
    const item = {...detailForm}
    if (detailDialog.editIndex >= 0) {
      // 编辑
      form.items[detailDialog.editIndex] = item
    } else {
      // 新增
      form.items.push(item)
    }
    detailDialog.visible = false
  })
}

// 删除明细
function removeDetail(index: number) {
  form.items.splice(index, 1)
}

const dialog = reactive({visible: false, title: ''})
const reportList = ref<any[]>([])
const formRef = ref<InstanceType<typeof ElForm>>()

function getList() {
  loading.value = true
  expenseReportApi.listExpenseReport({...queryParams}).then(res => {
    reportList.value = res.data.records
    total.value = res.data.total;
  }).finally(() => (loading.value = false))
}

function handleAdd() {
  Object.assign(form, {id: '', employeeId: '', departmentId: '', totalAmount: 0, status: 'draft', remarks: ''})
  dialog.visible = true
  dialog.title = '填写报销单'
}

function handleEdit(row: any) {
  expenseReportApi.getExpenseReport(row.id).then(res => {
    Object.assign(form, res.data)
    dialog.visible = true
    dialog.title = '编辑报销单'
  })
}

function handleDel(row: any) {
  expenseReportApi.deleteExpenseReport(row.id).then(() => {
    getList()
  })
}

function handleEmpUser(user: any) {
  form.departmentId = user.departmentId
}

function submitForm() {
  formRef.value?.validate(async valid => {
    if (valid) {
      buttonLoading.value = true
      const fn = form.id ? expenseReportApi.updateExpenseReport : expenseReportApi.saveExpenseReport
      fn(form).then((response: any) => {
        buttonLoading.value = false
        dialog.visible = false
        getList()
      }).catch(() => {
        buttonLoading.value = false
      })
    }
  })
}

function disabledFilter(data: any, node: any): any {
  if (!currBookStore.bookId) {
    return false;
  }
  return new RegExp(`/${currBookStore.bookId}(/|$)`).test(data.idPath)
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  name: string;
  children?: TreeNode[]; // 子节点可能不存在
}

function buildSubject(nodes: TreeNode[]) {
  for (let i = 0; i < nodes.length; i++) {
    let item = nodes[i];
    subjectList.value.push(item);
    if (item.children) {
      buildSubject(item.children);
    }
  }
}

function getSubjectName(subjectId: string): string {
  if (subjectId == null || subjectId === "") {
    return "";
  }
  for (let i = 0; i < subjectList.value.length; i++) {
    if (subjectList.value[i].id == subjectId) {
      return subjectList.value[i].name;
    }

  }
  return "";
}

function getSubjectTree() {
  getTree({bookId: currBookStore.bookId}).then((response: { data: TreeNode[] }) => {
    subjectOptions.value = response.data;
    buildSubject(response.data);
  });
}

/** 搜索按钮操作 */
function handleQuery(): any {
  queryParams.pageNumber = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery(): any {
  queryRef?.value?.resetFields();
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

getSubjectTree()
getList()
</script>
<style scoped>
</style>
