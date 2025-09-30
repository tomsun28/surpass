<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <!--          <el-form-item label="发放标签" prop="label">
                      <el-input
                          v-model="queryParams.label"
                          placeholder="请输入发放标签"
                          clearable
                          @keyup.enter="handleQuery"
                      />
                    </el-form-item>-->
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
        <el-button type="primary"
                   @click="submitForm()">
          汇总工资
        </el-button>
      </div>

      <el-table v-loading="loading" :data="dataList" show-summary :summary-method="getSummaries" border stripe>
        <el-table-column fixed prop="belongDate" label="月份" align="center" width="80"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="label" label="标签" align="center" width="80"
                                 :show-overflow-tooltip="true">
          <template #default="scope">
            <div v-if="scope.row.label === 'salary'">职工</div>
            <div v-if="scope.row.label === 'labor'">兼职</div>
           </template>
        </el-table-column>
        <el-table-column fixed prop="peopleNumber" label="人数" align="center" width="70"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <!--        <el-table-column :label="$t('jbx.text.description')" align="center" width="110"
                                 :show-overflow-tooltip="true" prop="description"/>-->
        <el-table-column label="应发金额" align="center">
          <el-table-column prop="payBasic" label="工资" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payBasic + scope.row.payPost + scope.row.payMerit + scope.row.laborFee) }}
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
          <el-table-column prop="totalSocialInsurance" label="代扣社保" align="center" width="110"
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
        <!--        <el-table-column prop="taxDeduction" label="税务抵扣" align="center" width="110"
                                 :show-overflow-tooltip="true">
                  <template #default="scope">
                    {{ formatAmount(scope.row.taxDeduction) }}
                  </template>
                </el-table-column>
                -->
        <el-table-column prop="payAmount" label="应发工资" align="right" width="110"
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
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column fixed="right" prop="businessExpenditureCosts" label="公司成本" align="center" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <b>{{ formatAmount(scope.row.businessExpenditureCosts) }}</b>
          </template>
        </el-table-column>
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
        <el-form-item prop="belongDate" label="工资所属月份" :required="true">
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
    <el-dialog v-model="voucherFlag" width="500px" append-to-body :title="editTitle" :close-on-click-modal="false"
               @close="cancel">
      <el-form :model="formVoucher" :rules="rulesVoucher" ref="voucherRef" inline-message>
        <el-form-item prop="voucherType" :required="true">
          <el-radio-group v-model="formVoucher.voucherType">
            <el-radio-button label="计提工资费用凭证" :value="0"/>
            <el-radio-button label="发放工资凭证" :value="1"/>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
        <el-button type="primary" @click="submitVoucherForm">{{ t('org.confirm') }}</el-button>
      </div>
    </el-dialog>

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
import {formatAmount} from "@/utils";
import {reactive, ref, toRefs} from "vue";
import * as voucherApis from "@/api/system/voucher/voucher";
import {fetchPage, generateVoucherSubmit, saveSummary, salarySummary} from "@/api/system/hr/employee-summary";
import {useI18n} from "vue-i18n";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import {useRouter} from "vue-router";
import {h} from 'vue'
import type {VNode} from 'vue'
import voucherEdit from "@/views/voucher/voucher-edit.vue";

const router: any = useRouter();
const editFlag: any = ref(false);
const voucherFlag: any = ref(false);
const editTitle: any = ref('');
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const voucherRef = ref<InstanceType<typeof ElForm> | null>(null);
let tableSummary: any = ref([]);
const voucherOpen = ref(false);
const voucherPreviewMode = ref(false);
const voucherTitle = ref("");

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
const {t} = useI18n()
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    belongDateRange: [new Date().getFullYear() + "-01", getCurrentYearMonth()],//默认查本年数据
    pageSizeOptions: [10, 20, 50]
  },
  form: {
    belongDate: getCurrentYearMonth()
  },
  formVoucher: {
    voucherType: 0
  },
  voucherForm: {},
  rules: {
    belongDate: [
      {required: true, message: "请选择月份", trigger: "blur"},
    ],
  },
  rulesVoucher: {
    voucherType: [
      {required: true, message: "请选择生成凭证的方式", trigger: "blur"},
    ],
  }
});

const {queryParams, form, formVoucher, voucherForm, rules, rulesVoucher} = toRefs(data);
const dataList = ref([]);
const loading = ref(true);
const total = ref(0);
const id: any = ref(undefined);

function getList() {
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
  salarySummary(queryParams.value).then((res: any) => {
    console.log(res.data);
    tableSummary = res.data;
  });
}

const getSummaries = () => {
  const sums: (string | VNode)[] = []

  if (tableSummary != null) {
    let sumsIndex = 0;
    sums[sumsIndex++] = h('div', {style: {textDecoration: 'underline'}}, ['合计',])
    console.log("tableSummary " + tableSummary);
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = "";
    sums[sumsIndex++] = tableSummary.payBasic + tableSummary.payPost + tableSummary.payMerit + tableSummary.laborFee;
    sums[sumsIndex++] = tableSummary.payBasic;
    sums[sumsIndex++] = tableSummary.payPost;
    sums[sumsIndex++] = tableSummary.payMerit;
    sums[sumsIndex++] = tableSummary.laborFee;
    sums[sumsIndex++] = tableSummary.bonus;
    sums[sumsIndex++] = tableSummary.overtime;
    sums[sumsIndex++] = tableSummary.allowance;
    sums[sumsIndex++] = tableSummary.backPay;
    sums[sumsIndex++] = h('div', {
      style: {
        color: tableSummary.totalSocialInsurance > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.totalSocialInsurance),]);
    sums[sumsIndex++] = h('div', {
      style: {
        color: tableSummary.providentFund > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.providentFund),]);
    sums[sumsIndex++] = h('div', {
      style: {
        color: tableSummary.attendance > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.attendance),]);
    sums[sumsIndex++] = h('div', {
      style: {
        color: tableSummary.otherDeductions > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.otherDeductions),]);
    sums[sumsIndex++] = h('div', {
      style: {
        color: tableSummary.personalTax > 0 ? 'red' : '',
      }
    }, [formatAmount(tableSummary.personalTax),]);
    sums[sumsIndex++] = tableSummary.businessSocialInsurance;
    sums[sumsIndex++] = tableSummary.businessProvidentFund;
    sums[sumsIndex++] = formatAmount(tableSummary.payAmount);
    sums[sumsIndex++] = formatAmount(tableSummary.taxableWages);
    sums[sumsIndex++] = formatAmount(tableSummary.totalAmount);
    sums[sumsIndex++] = h('div', {
      style: {
        textDecoration: 'underline',
        fontWeight: "bold"
      }
    }, [formatAmount(tableSummary.businessExpenditureCosts),]);
  }

  return sums
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  // queryParams.value.label = undefined;
  queryParams.value.belongDateRange = undefined;
  handleQuery();
}

function totalSalary() {
  editFlag.value = true;
  editTitle.value = "合计"
}

function cancel(): any {
  editFlag.value = false;
  voucherFlag.value = false;
  resetForm();
}

function resetForm() {
  form.value = {
    belongDate: getCurrentYearMonth()
  };
  formVoucher.value = {
    voucherType: 0
  };
  formRef?.value?.resetFields();
  voucherRef?.value?.resetFields();
}

function getCurrentYearMonth() {
  const date = new Date();
  date.setMonth(date.getMonth() - 1);
  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, '0');
  return `${year}-${month}`;
}

function submitForm() {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(successMessage);
      cancel();
    } else {
      modal.msgError(res.message);
    }
  };

  const operation: any = saveSummary;
  const successMessage: any = "操作成功"
  operation(form.value).then((res: any) => handleResponse(res, successMessage));

}

function submitVoucherForm() {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(res.data);
      cancel();
    } else {
      modal.msgError(res.message);
    }
  };
  console.log("validate " + formVoucher.value.voucherType);
  voucherRef?.value?.validate((valid: any) => {
    console.log("valid " + valid);
    if (valid) {
      const operation: any = generateVoucherSubmit;
      const successMessage: any = "操作成功"
      //formVoucher.value.id = id.value;
      operation(formVoucher.value).then((res: any) => handleResponse(res, successMessage));
    }
  });
}

function generateVoucher(row: any, voucherType: number) {
  generateVoucherSubmit({id: row.id, voucherType: voucherType}).then((res: any) => {
    console.log(res.data);
    if (res.code === 0) {
      getList();
      modal.msgSuccess("操作成功");
      viewVoucher(res.data)
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

getList();
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
