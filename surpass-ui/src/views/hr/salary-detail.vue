<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item label="姓名" prop="displayName">
            <el-input
                v-model="queryParams.employeeName"
                placeholder="请输入员工姓名"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="工号" prop="employeeNumber">
            <el-input
                v-model="queryParams.employeeNumber"
                placeholder="请输入工号"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="所属月份" prop="belongDate">
            <el-date-picker
                v-model="queryParams.belongDateRange"
                type="monthrange"
                unlink-panels
                range-separator="至"
                start-placeholder="起始月份"
                end-placeholder="结束月份"
                value-format="YYYY-MM"
                :shortcuts="shortcuts"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button type="success" plain @click="exportTaxItems">
          {{ $t('jbx.text.export') }}
        </el-button>
        <!--        <el-button type="primary"
                    @click="totalSalary">
                  合计本月工资
                </el-button>-->
        <!--        <el-button
                    type="primary"
                    @click="createSalaryDetail"
                >预览本月工资
                </el-button>
                <el-button
                    type="danger"
                    :disabled="ids.length === 0"
                    @click="onBatchDelete"
                >{{ t('org.button.deleteBatch') }}
                </el-button>-->
      </div>
      <el-table v-loading="loading" :data="dataList" show-summary :summary-method="getSummaries"
                border
                @selection-change="handleSelectionChange">
        <!--        <el-table-column type="selection" width="55" align="center"/>-->
        <el-table-column fixed prop="belongDate" label="月份" align="center" width="80"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="employeeNumber" label="工号" align="center" width="150"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="employeeName" label="姓名" align="center" width="120"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="员工类型" align="center" prop="employeeType" min-width="80">
          <template #default="scope">
            <dict-tag-number :options="employee_types" :value="scope.row.employeeType"/>
          </template>
        </el-table-column>
        <el-table-column label="应发金额" align="center">
          <el-table-column prop="payPost" label="工资" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <b v-if="scope.row.employeeType === 'NORMAL'||scope.row.employeeType === 'INTERN'||scope.row.employeeType === 'RETIREMENT'">
                {{ formatAmount(scope.row.payBasic + scope.row.payPost + scope.row.payMerit) }}</b>
              <b v-if="scope.row.employeeType === 'PARTTIME'">{{ formatAmount(scope.row.laborFee) }}</b>
            </template>
          </el-table-column>
          <el-table-column prop="payBasic" label="基本工资" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payBasic) }}
            </template>
          </el-table-column>
          <el-table-column prop="payPost" label="岗位工资" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payPost) }}
            </template>
          </el-table-column>
          <el-table-column prop="payMerit" label="绩效" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payMerit) }}
            </template>
          </el-table-column>
          <el-table-column prop="laborFee" label="劳务费" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.laborFee) }}
            </template>
          </el-table-column>
          <el-table-column prop="bonus" label="奖金" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.bonus) }}
            </template>
          </el-table-column>
          <el-table-column prop="overtime" label="加班补贴" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.overtime) }}
            </template>
          </el-table-column>
          <el-table-column prop="allowance" label="津贴" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.allowance) }}
            </template>
          </el-table-column>
          <el-table-column prop="backPay" label="补发工资" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.backPay) }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="应扣金额" align="center">
          <el-table-column prop="totalSocialInsurance" label="代扣社保合计" align="center" width="110"
                           :show-overflow-tooltip="true">
            <template #default="scope">
            <span :class="{ 'red-font': scope.row.totalSocialInsurance > 0 }">{{ formatAmount(scope.row.totalSocialInsurance) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="providentFund" label="代扣公积金" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.providentFund > 0 }">{{ formatAmount(scope.row.providentFund) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="attendance" label="请假考勤" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.attendance > 0 }">{{ formatAmount(scope.row.attendance) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="otherDeductions" label="其他扣额" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.otherDeductions > 0 }">{{ formatAmount(scope.row.otherDeductions) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="personalTax" label="个税" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.personalTax > 0 }">{{ formatAmount(scope.row.personalTax) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="企业缴纳" align="center">
          <el-table-column prop="businessSocialInsurance" label="社保（公司）" align="center" width="110"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.businessSocialInsurance) }}
            </template>
          </el-table-column>
          <el-table-column prop="businessProvidentFund" label="公积金（公司）" align="center" width="120"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.businessProvidentFund) }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="taxDeduction" label="税务抵扣" align="center" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.taxDeduction) }}
          </template>
        </el-table-column>
        <el-table-column prop="payAmount" label="应发工资" align="center" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.payAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="taxableWages" label="应税工资" align="center" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.taxableWages) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="实发合计" align="center" width="110"
                         :show-overflow-tooltip="true" fixed="right">
          <template #default="scope">
            {{ formatAmount(scope.row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="businessExpenditureCosts" label="公司成本" align="center" width="110"
                         :show-overflow-tooltip="true" fixed="right">
          <template #default="scope">
            {{ formatAmount(scope.row.businessExpenditureCosts) }}
          </template>
        </el-table-column>
        <el-table-column label="收票凭证" align="center" width="100"
                         :show-overflow-tooltip="true" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.employeeType === 'PARTTIME' && (scope.row.accrualVoucherId === null ||scope.row.accrualVoucherId ==='')"
                       type="text"
                       @click="generateVoucher(scope.row,2)">
              生成
            </el-button>
            <el-button v-if="scope.row.employeeType === 'PARTTIME' && scope.row.accrualVoucherId !== null &&scope.row.accrualVoucherId !==''"
                       type="text"
                       @click="viewVoucher(scope.row.accrualVoucherId)">
              查看
            </el-button>
             <el-button v-if="scope.row.employeeType === 'PARTTIME' && scope.row.accrualVoucherId !== null &&scope.row.accrualVoucherId !==''"
                       type="text"
                       @click="deleteVoucher(scope.row,2)">
              删除
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="发放凭证" align="center" width="100"
                         :show-overflow-tooltip="true" fixed="right">
            <template #default="scope">
              <el-button v-if="scope.row.employeeType === 'PARTTIME' && (scope.row.salaryVoucherId === null ||scope.row.salaryVoucherId ==='')"
                         type="text"
                         @click="generateVoucher(scope.row,3)">
                生成
              </el-button>
              <el-button v-if="scope.row.employeeType === 'PARTTIME' && scope.row.salaryVoucherId !== null &&scope.row.salaryVoucherId !==''"
                         type="text"
                         @click="viewVoucher(scope.row.salaryVoucherId)">
                查看
              </el-button>
              <el-button v-if="scope.row.employeeType === 'PARTTIME' && scope.row.salaryVoucherId !== null &&scope.row.salaryVoucherId !==''"
                         type="text"
                         @click="deleteVoucher(scope.row,3)">
                删除
              </el-button>
            </template>
        </el-table-column>
        <!--        <el-table-column label="操作" align="center" fixed="right" width="180">
                  <template #default="scope">
                    <el-button @click="handleUpdate(scope.row)">
                      {{ t('org.edit') }}
                    </el-button>
                                <el-button type="danger" @click="handleDelete(scope.row)">{{ t('org.button.delete') }}
                                </el-button>
                  </template>
                </el-table-column>-->
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          :page-sizes="queryParams.pageSizeOptions"
          @pagination="getList"
      />
    </el-card>
    <el-dialog v-model="editFlag" width="500px" append-to-body :title="editTitle" :close-on-click-modal="false"
               @close="cancel">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
        <el-form-item prop="belongDate" label="所属月份" :required="true">
          <el-date-picker
              style="width: 100%"
              v-model="form.belongDate"
              type="month"
              format="YYYY-MM"
              value-format="YYYY-MM"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </el-dialog>
    <!--    <drawerTable :title="title" :open="open"
                   @dialogOfClosedMethods="dialogOfClosedMethods"></drawerTable>
        <edit-form :title="title" :open="openForm"
                   :form-id="id"
                   @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>-->
    <!-- 添加或修改凭证记录对话框 -->
    <el-drawer :title="voucherTitle" v-model="voucherOpen" :close-on-click-modal="false" @close="getList" size="1200px">
      <template #header>
        <h4>{{ voucherTitle }}</h4>
      </template>
      <voucher-edit v-if="voucherOpen" v-model="voucherForm" :edit="!voucherPreviewMode" :dialog="true" :auto="false"
                    @submit="submitForm"></voucher-edit>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {useI18n} from "vue-i18n";
import {delSalary, exportSalary, fetchPage, salarySummary, generateVoucherSubmit,deleteVoucherSubmit} from "@/api/system/hr/employee-salary";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import {formatAmount} from "@/utils";
import {useRouter} from "vue-router";
import {h} from 'vue'
import type {VNode} from 'vue'
import booksSetStore from "@/store/modules/bookStore";
import * as voucherApis from "@/api/system/voucher/voucher";
import voucherEdit from "@/views/voucher/voucher-edit.vue";

const router: any = useRouter();
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const currBookStore = booksSetStore()
const {employee_types}
    = proxy?.useDict("employee_types");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    belongDateRange: [getCurrentYearMonth(), getCurrentYearMonth()],//默认查当前期数据
    pageSizeOptions: [10, 20, 50]
  },
  form: {
    belongDate: getCurrentYearMonth()
  },
  rules: {
    label: [
      {required: true, message: "请输入名称", trigger: "blur"},
    ],
  },
  voucherForm: {},

});
const voucherOpen = ref(false);
const voucherPreviewMode = ref(false);
const voucherTitle = ref("");
const dataList = ref([]);
const open = ref(false);
const openForm = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const editFlag: any = ref(false);
const editTitle: any = ref('');
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {queryParams, form, rules, voucherForm} = toRefs(data);
let accOptions: any = ref([]);
let tableSummary: any = ref([]);
const shortcuts = [
  {
    text: '本月',
    value: [new Date(), new Date()],
  },
  {
    text: '本年',
    value: () => {
      const end = new Date()
      const start = new Date(new Date().getFullYear(), 0)
      return [start, end]
    },
  },
  {
    text: '过去6个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 6)
      return [start, end]
    },
  },
]

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.employeeName = undefined;
  queryParams.value.employeeNumber = undefined;
  queryParams.value. belongDateRange = [getCurrentYearMonth(), getCurrentYearMonth()];
  handleQuery();
}

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });

  salarySummary(queryParams.value).then((res: any) => {
    tableSummary = res.data;
  });
}

const getSummaries = () => {
  const sums: (string | VNode)[] = []

  if (tableSummary != null) {
    sums[0] = h('div', {style: {textDecoration: 'underline'}}, ['合计',])
    sums[1] = '人数：' + tableSummary.peopleNumber;
    sums[2] = ''
    sums[3] = ''
    sums[4] = tableSummary.payBasic + tableSummary.payPost + tableSummary.payMerit + tableSummary.laborFee;
    sums[5] = tableSummary.payBasic;
    sums[6] = tableSummary.payPost;
    sums[7] = tableSummary.payMerit;
    sums[8] = tableSummary.laborFee;
    sums[9] = tableSummary.bonus;
    sums[10] = tableSummary.overtime;
    sums[11] = tableSummary.allowance;
    sums[12] = tableSummary.backPay;
    sums[13] = h('div', {
      style: {
        color: tableSummary.totalSocialInsurance > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.totalSocialInsurance),]);
    sums[14] = h('div', {
      style: {
        color: tableSummary.providentFund > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.providentFund),]);
    sums[15] = h('div', {
      style: {
        color: tableSummary.attendance > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.attendance),]);
    sums[16] = h('div', {
      style: {
        color: tableSummary.otherDeductions > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.otherDeductions),]);
    sums[17] = h('div', {
      style: {
        color: tableSummary.personalTax > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.personalTax),]);
    sums[18] = tableSummary.businessSocialInsurance;
    sums[19] = tableSummary.businessProvidentFund;
    sums[20] = tableSummary.taxDeduction;
    sums[21] = tableSummary.payAmount;
    sums[22] = tableSummary.taxableWages;
    sums[23] = formatAmount(tableSummary.totalAmount);
    sums[24] = h('div', {
      style: {
        textDecoration: 'underline',
        fontWeight: "bold"
      }
    }, [formatAmount(tableSummary.businessExpenditureCosts),]);
  }

  return sums
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  openForm.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function createSalaryDetail() {
  open.value = true;
  title.value = "预览/编辑";
}

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = "修改工资明细";
  openForm.value = true;
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除工资明细信息编号为"' + _ids + '"的数据项？').then(function () {
    return delSalary({listIds: [_ids]});
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}


/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return delSalary({listIds: ids.value});
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function handleAdd() {
  id.value = undefined;
  title.value = "新增工资明细";
  openForm.value = true;
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

function exportTaxItems() {
  editFlag.value = true;
  editTitle.value = "导出"
}

function cancel(): any {
  editFlag.value = false;
  resetForm();
}

function resetForm() {
  form.value = {
    belongDate: getCurrentYearMonth()
  }
  formRef?.value?.resetFields();
}

function getCurrentYearMonth() {
/*  const date = new Date();
  date.setMonth(date.getMonth() - 1);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}-${month}`;*/
  return currBookStore.termCurrent;
}

function submitForm() {
  exportSalary(form.value).then((data: any) => {
    if (!data || data.size === 0) {
      modal.msgError("导出失败：返回数据为空"); // 提示用户
      return; // 不执行下载
    }

    // **检查返回的数据是否是 JSON，而不是 Excel**
    const isJson = data.type.includes('application/json');
    if (isJson) {
      modal.msgError("导出失败：返回数据为空"); // 提示用户
      return;
    }

    //关闭窗口
    editFlag.value = false;

    const blob: any = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'});
    const url: any = URL.createObjectURL(blob);
    const a: any = document.createElement('a');
    a.href = url;
    a.download = 'salary_' + form.value.belongDate;
    a.click();
    URL.revokeObjectURL(a.href);
    a.remove();
  })
}

function generateVoucher(row: any, voucherType: number) {
  generateVoucherSubmit({id: row.id, voucherType: voucherType}).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess("操作成功");
      viewVoucher(res.data)
    }
  });
}

function deleteVoucher(row: any, voucherType: number) {
  deleteVoucherSubmit({id: row.id, voucherType: voucherType}).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess("操作成功");
    }
  });
}

/** 查看操作 */
function viewVoucher(voucherId: any) {
  voucherApis.getOneVoucher(voucherId).then((response: any) => {
    response.data.items.forEach((t: any) => {
      t.subjectCode = t.subjectName.split("-")[0]
      // t.detailedSubjectCode = t.detailedAccounts.split("-")[0]
    })
    voucherForm.value = response.data
    voucherOpen.value = true;
    voucherPreviewMode.value = false;
    voucherTitle.value = "凭证记录";
  });
}

getList()
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.red-font {
  color: red;
}
</style>
