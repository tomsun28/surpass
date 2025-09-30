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
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="createSalaryDetail"
        >生成工资预览
        </el-button>
        <el-button
            type="danger"
            @click="handleDelete"
            :disabled="ids.length === 0"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
        <el-button
            type="primary"
            plain
            @click="pushSalaryDetail"
        >推送工资明细</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border stripe @selection-change="handleSelectionChange">
        <el-table-column fixed type="selection" width="55" align="center"/>
        <el-table-column fixed prop="belongDate" label="月份" align="center" width="80">
        </el-table-column>
        <el-table-column fixed prop="employeeNumber" label="工号" align="center" width="150"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="employeeName" label="姓名" align="center" width="120"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="员工类型" align="center" prop="employeeType" min-width="80">
              <template #default="scope"  >
                <dict-tag-number :options="employee_types" :value="scope.row.employeeType"/>
              </template>
        </el-table-column>
        <el-table-column label="应发金额" align="center">
          <el-table-column prop="payPost" label="工资" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <b  v-if="scope.row.employeeType === 'NORMAL'">{{ formatAmount(scope.row.payBasic+scope.row.payPost+scope.row.payMerit) }}</b>
              <b  v-if="scope.row.employeeType === 'INTERN'">{{ formatAmount(scope.row.payBasic+scope.row.payPost+scope.row.payMerit) }}</b>
              <b  v-if="scope.row.employeeType === 'RETIREMENT'">{{ formatAmount(scope.row.payBasic+scope.row.payPost+scope.row.payMerit) }}</b>
              <b  v-if="scope.row.employeeType === 'PARTTIME'">{{ formatAmount(scope.row.laborFee) }}</b>
            </template>
          </el-table-column>
          <el-table-column prop="payBasic" label="基本工资" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payBasic) }}
            </template>
          </el-table-column>
          <el-table-column prop="payPost" label="岗位工资" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payPost) }}
            </template>
          </el-table-column>
          <el-table-column prop="payMerit" label="绩效" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.payMerit) }}
            </template>
          </el-table-column>
          <el-table-column prop="laborFee" label="劳务费" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.laborFee) }}
            </template>
          </el-table-column>

          <el-table-column prop="bonus" label="奖金" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.bonus) }}
            </template>
          </el-table-column>
          <el-table-column prop="overtime" label="加班补贴" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.overtime) }}
            </template>
          </el-table-column>
          <el-table-column prop="allowance" label="津贴" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.allowance) }}
            </template>
          </el-table-column>
          <el-table-column prop="backPay" label="补发工资" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.backPay) }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="应扣金额" align="center">
          <el-table-column prop="totalSocialInsurance" label="代扣社保合计" align="right" width="110"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.totalSocialInsurance > 0 }">{{ formatAmount(scope.row.totalSocialInsurance) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="providentFund" label="代扣公积金" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.providentFund > 0 }">{{ formatAmount(scope.row.providentFund) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="attendance" label="请假考勤" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.attendance > 0 }">{{ formatAmount(scope.row.attendance) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="otherDeductions" label="其他扣额" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.otherDeductions > 0 }">{{ formatAmount(scope.row.otherDeductions) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="personalTax" label="个税" align="right" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              <span :class="{ 'red-font': scope.row.personalTax > 0 }">{{ formatAmount(scope.row.personalTax) }}</span>
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column label="企业缴纳" align="center">
          <el-table-column prop="businessSocialInsurance" label="社保（公司）" align="right" width="110"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.businessSocialInsurance) }}
            </template>
          </el-table-column>
          <el-table-column prop="businessProvidentFund" label="公积金（公司）" align="right" width="120"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.businessProvidentFund) }}
            </template>
          </el-table-column>
        </el-table-column>
        <el-table-column prop="taxDeduction" label="税务抵扣" align="right" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.taxDeduction) }}
          </template>
        </el-table-column>
         <el-table-column prop="payAmount" label="应发工资" align="right" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.payAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="taxableWages" label="应税工资" align="right" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.taxableWages) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="实发合计" align="right" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="businessExpenditureCosts" label="公司成本" align="right" width="110"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.businessExpenditureCosts) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="80">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="移除">
              <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
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
    <edit-form :title="editTitle" :open="editFlag"
               :form-id="id"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
  </div>
</template>

<script setup lang="ts">
import {ElForm} from "element-plus";
import {reactive, ref, toRefs,getCurrentInstance} from "vue";
import {batchDelete, createDetailTemp, createFinalDetail, fetchPage} from "@/api/system/hr/salary-detail";
import {useI18n} from "vue-i18n";
import {formatAmount} from "@/utils";
import editForm from "./calc-salary/edit.vue";
import modal from "@/plugins/modal";
import {useRouter} from "vue-router";

const router: any = useRouter();
const {t} = useI18n()
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {proxy} = getCurrentInstance()!;
const {employee_types}
    = proxy?.useDict( "employee_types");
const {queryParams} = toRefs(data);
const dataList = ref([]);
const loading = ref(true);
const total = ref(0);
const id: any = ref(undefined);
const editFlag: any = ref(false);
const editTitle: any = ref('');
const ids = ref([]);
const selectionlist: any = ref<any>([]);

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.employeeName = undefined;
  queryParams.value.employeeNumber = undefined;
  handleQuery();
}

function getList() {
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

function handleUpdate(row: any) {
  id.value = row.id;
  editTitle.value = "修改工资明细"
  editFlag.value = true;
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  editFlag.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}


function createSalaryDetail() {
  loading.value = true;
  createDetailTemp({}).then((response: any) => {
    if (response.code === 0) {
      getList();
      modal.msgSuccess("已生成");
      loading.value = false;
    }
  });
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  const _ids = row.id ? [row.id] : ids.value;
  modal.confirm('是否确认删除？').then(function () {
    return batchDelete({listIds: _ids});
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

// 多选框选中数据
function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

function pushSalaryDetail() {
  createFinalDetail().then((res: any) => {
    if (res.code === 0) {
      modal.msgSuccess(res.data);
      router.push({path: "/hr/salary-detail"});
    }
  })
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
