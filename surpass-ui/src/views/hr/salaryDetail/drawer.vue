<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="85%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
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
      <div class="btn-form">
        <el-button
            type="primary"
            @click="rePreview"
        >重新计算预览明细
        </el-button>
      </div>
      <el-table v-loading="loading" :data="dataList" border stripe>
<!--        <el-table-column fixed type="selection" width="55" align="center"/>-->
        <el-table-column fixed prop="employeeNumber" label="工号" align="center" width="150"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="employeeName" label="姓名" align="center" width="120"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="belongDate" label="工资所属月份" align="center" width="110"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="bankCardNo" label="银行卡号" align="center" width="180"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="应发金额" align="center">
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
          <el-table-column prop="bonus" label="奖金" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.bonus) }}
            </template>
          </el-table-column>
          <el-table-column prop="overtime" label="加班补贴" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.overtime) }}
            </template>
          </el-table-column>
          <el-table-column prop="allowance" label="津贴" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.allowance) }}
            </template>
          </el-table-column>
          <el-table-column prop="laborFee" label="劳务费" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.laborFee) }}
            </template>
          </el-table-column>
          <el-table-column prop="backPay" label="补发工资" align="center" width="80"
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
              {{ formatAmount(scope.row.totalSocialInsurance) }}
            </template>
          </el-table-column>
          <el-table-column prop="providentFund" label="代扣公积金" align="center" width="100"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.providentFund) }}
            </template>
          </el-table-column>
          <el-table-column prop="attendance" label="请假考勤" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.attendance) }}
            </template>
          </el-table-column>
          <el-table-column prop="otherDeductions" label="其他扣额" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.otherDeductions) }}
            </template>
          </el-table-column>
          <el-table-column prop="personalTax" label="个税" align="center" width="80"
                           :show-overflow-tooltip="true">
            <template #default="scope">
              {{ formatAmount(scope.row.personalTax) }}
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
        <el-table-column label="操作" align="center" fixed="right" width="120">
          <template #default="scope">
            <el-button @click="handleUpdate(scope.row)">
              {{ t('org.edit') }}
            </el-button>
<!--            <el-button type="danger" @click="handleDelete(scope.row)">{{ t('org.button.delete') }}
            </el-button>-->
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
      <el-dialog v-model="editFlag" width="800px" append-to-body :title="editTitle" :close-on-click-modal="false"
                 @close="cancel">
        <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message class="scrollable-form">
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="工号">
                <el-input v-model="form.employeeNumber" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="姓名">
                <el-input v-model="form.employeeName" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="工资所属月份">
                <el-input v-model="form.belongDate" disabled/>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="银行卡">
                <el-input v-model="form.bankCardNo" disabled/>
              </el-form-item>
            </el-col>
          </el-row>
          <h4 class="section-title" style="color: dodgerblue">应发金额</h4>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="基本工资">
                <el-input-number :min="0" :precision="2" v-model="form.payBasic" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="岗位工资">
                <el-input-number :min="0" :precision="2" v-model="form.payPost" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="绩效">
                <el-input-number :min="0" :precision="2" v-model="form.payMerit" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="奖金">
                <el-input-number :min="0" :precision="2" v-model="form.bonus" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="加班补贴">
                <el-input-number :min="0" :precision="2" v-model="form.overtime" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="津贴">
                <el-input-number :min="0" :precision="2" v-model="form.allowance" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="劳务费">
                <el-input-number :min="0" :precision="2" v-model="form.laborFee" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="补发工资">
                <el-input-number :min="0" :precision="2" v-model="form.backPay" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <h4 class="section-title" style="color: orangered">应扣金额</h4>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="请假考勤">
                <el-input-number :min="0" :precision="2" v-model="form.attendance" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="其他扣额">
                <el-input-number :min="0" :precision="2" v-model="form.otherDeductions" style="width: 100%">
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="代扣社保合计">
                <el-input-number :min="0" :precision="2" v-model="form.totalSocialInsurance" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="代扣公积金">
                <el-input-number :min="0" :precision="2" v-model="form.providentFund" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="个税">
                <el-input-number :min="0" :precision="2" v-model="form.personalTax" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <h4 class="section-title" style="color: green">其他</h4>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="社保（公司）">
                <el-input-number :precision="2" v-model="form.businessSocialInsurance" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="公积金（公司）">
                <el-input-number :precision="2" v-model="form.businessProvidentFund" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="span">
              <el-form-item label="税务抵扣">
                <el-input-number :precision="2" v-model="form.taxDeduction" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="span">
              <el-form-item label="应税工资">
                <el-input-number :precision="2" v-model="form.taxableWages" style="width: 100%" disabled>
                  <template #suffix>
                    <span>元</span>
                  </template>
                </el-input-number>
              </el-form-item>
            </el-col>
          </el-row>
            <el-form-item label="实发合计">
              <el-input-number :precision="2" v-model="form.totalAmount" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
          <el-button @click="reCalculate">重新计算</el-button>
          <el-button type="primary" @click="updateForm">{{ t('org.confirm') }}</el-button>
        </div>
      </el-dialog>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitAddBatch" :disabled="dataList.length === 0">确认生成</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {getCurrentInstance, reactive, ref, toRefs, watch} from "vue";

import {useI18n} from "vue-i18n";
import {
  createDetailTemp,
  createFinalDetail,
  fetchPage,
  getCurrentDetail, reCalculateSalary,
  updateDetailTemp
} from "@/api/system/hr/salary-detail";
import {ElForm} from "element-plus";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const emit: any = defineEmits(['dialogOfClosedMethods'])
const props: any = defineProps({
  title: {
    type: String,
    default: ""
  },
  open: {
    type: Boolean,
    default: false
  }
})

const dialogStatus: any = ref(false);
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  form: {
  },
  rules: {
  }
});
const dataList = ref([]);
const loading = ref(true);
const total = ref(0);
const {queryParams, form, rules} = toRefs(data)
const editFlag: any = ref(false);
const editTitle: any = ref('');
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const id: any = ref(undefined);
const span = ref(12);

const formatAmount = (value: number | null): string => {
  if (value != null) {
    return value.toLocaleString('en-US', {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    })
  }
  return ''
}

// 计算费用的方法
const calculateFees = () => {
  //实发工资
  form.value.totalAmount =
      Number((form.value.payBasic +
          form.value.payPost +
          form.value.payMerit +
          form.value.bonus +
          form.value.overtime +
          form.value.allowance +
          form.value.laborFee +
          form.value.backPay -
          form.value.totalSocialInsurance -
          form.value.providentFund -
          form.value.attendance -
          form.value.otherDeductions -
          form.value.personalTax))
}

watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        loading.value = true;
        createDetailTemp({}).then((response: any) => {
          if (response.code === 0) {
            getList();
          }
        });
      } else {
        reset();
      }
    },
    {immediate: true}
);

/*// 监听缴费基数和比例的变化
watch(
    [
      () => form.value.payBasic,
      () => form.value.payPost,
      () => form.value.payMerit,
      () => form.value.bonus,
      () => form.value.overtime,
      () => form.value.allowance,
      () => form.value.laborFee,
      () => form.value.backPay,
      () => form.value.totalSocialInsurance,
      () => form.value.providentFund,
      () => form.value.attendance,
      () => form.value.otherDeductions,
      () => form.value.personalTax
    ],
    () => {
      calculateFees()
    }
)*/

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

//修改表单
function updateForm(): any {
  updateDetailTemp(form.value).then((response: any) => {
    if (response.code === 0) {
      proxy?.$modal.msgSuccess(response.data);
      cancel();
      handleQuery();
    }
  })
}

function submitAddBatch(): any {
  createFinalDetail().then((res: any) => {
    if (res.code === 0) {
      proxy?.$modal.msgSuccess(res.data);
      dialogOfClosedMethods(true);
    }
  })
}

/** 重置操作表单 */
function reset(): any {

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
  getOneDetail();
  editTitle.value = "修改工资明细"
  editFlag.value = true;
}

function getOneDetail() {
  getCurrentDetail(id.value).then((res: any) => {
    form.value = res.data;
  })
}

function handleDelete(row: any) {

}

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

function cancel(): any {
  editFlag.value = false;
  resetForm();
}

function reCalculate(): any {
  reCalculateSalary(form.value).then((response: any) => {
    if (response.code === 0) {
      form.value = response.data;
    }
  })
}

function resetForm() {
  formRef?.value?.resetFields();
}

function rePreview() {

}
</script>

<style lang="scss" scoped>
.section-title {
  font-size: 16px; /* text-lg */
  font-weight: 500; /* font-medium */
  margin-bottom: 16px; /* mb-4 */
}

.scrollable-form {
  max-height: 700px; /* 设置你想要的固定高度 */
  overflow-y: auto;  /* 启用垂直滚动 */
  padding-right: 10px; /* 为滚动条留出空间 */
}

/* 美化滚动条样式（可选） */
.scrollable-form::-webkit-scrollbar {
  width: 6px;
}

.scrollable-form::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.scrollable-form::-webkit-scrollbar-thumb {
  background: #c0c4cc;
  border-radius: 3px;
}

.scrollable-form::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

.btn-form {
  margin-bottom: 10px;
}

</style>
