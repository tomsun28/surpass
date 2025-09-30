<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="45%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="月份">
              <el-input v-model="form.belongDate" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="员工类型">
              <el-select v-model="form.employeeType" disabled>
                <el-option
                    v-for="dict in employee_types"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
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
            <el-form-item label="银行">
              <el-input v-model="form.bankName" disabled/>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="银行卡">
              <el-input v-model="form.bankCardNo" disabled/>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 class="section-title" style="color: dodgerblue">工资金额 {{(form.payBasic +
            form.payPost +
            form.payMerit +
            form.laborFee +
            form.bonus +
            form.overtime +
            form.allowance +
            form.backPay).toFixed(2)}} 应发金额 {{(form.payAmount||0.00).toFixed(2)}}</h4>
        <el-row :gutter="20">
          <el-col :span="span" v-if="form.employeeType !== 'PARTTIME'">
            <el-form-item label="基本工资">
              <el-input-number :min="0" :precision="2" v-model="form.payBasic" style="width: 100%" >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span" v-if="form.employeeType !== 'PARTTIME'">
            <el-form-item label="岗位工资">
              <el-input-number :min="0" :precision="2" v-model="form.payPost" style="width: 100%" >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span" v-if="form.employeeType !== 'PARTTIME'">
            <el-form-item label="绩效">
              <el-input-number :min="0" :precision="2" v-model="form.payMerit" style="width: 100%">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span" v-if="form.employeeType === 'PARTTIME'">
            <el-form-item label="劳务费">
              <el-input-number :min="0" :precision="2" v-model="form.laborFee" style="width: 100%">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="奖金">
              <el-input-number :min="0" :precision="2" v-model="form.bonus" style="width: 100%" :disabled="form.employeeType === 'PARTTIME'">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="加班补贴">
              <el-input-number :min="0" :precision="2" v-model="form.overtime" style="width: 100%" :disabled="form.employeeType === 'PARTTIME'">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>

        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="津贴">
              <el-input-number :min="0" :precision="2" v-model="form.allowance" style="width: 100%" :disabled="form.employeeType === 'PARTTIME'">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="补发工资">
              <el-input-number :min="0" :precision="2" v-model="form.backPay" style="width: 100%" :disabled="form.employeeType === 'PARTTIME'">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 class="section-title" style="color: orangered">应扣金额 {{ (form.attendance
            +form.otherDeductions
            +form.totalSocialInsurance
            +form.providentFund
            +form.personalTax).toFixed(2) }}</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="请假考勤">
              <el-input-number :min="0" :precision="2" v-model="form.attendance" style="width: 100%" :disabled="form.employeeType === 'PARTTIME'">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="其他扣额">
              <el-input-number :min="0" :precision="2" v-model="form.otherDeductions" style="width: 100%" :disabled="form.employeeType === 'PARTTIME'">
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
              <el-input :min="0" :precision="2" v-model="form.totalSocialInsurance" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="代扣公积金">
              <el-input :min="0" :precision="2" v-model="form.providentFund" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="个税">
              <el-input :min="0" :precision="2" v-model="form.personalTax" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <h4 class="section-title" style="color: green">其他</h4>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="社保（公司）">
              <el-input :precision="2" v-model="form.businessSocialInsurance" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="公积金（公司）">
              <el-input :precision="2" v-model="form.businessProvidentFund" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="税务抵扣">
              <el-input :precision="2" v-model="form.taxDeduction" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="应税工资">
              <el-input :precision="2" v-model="form.taxableWages" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="span">
            <el-form-item label="实发合计">
              <el-input :precision="2" v-model="form.totalAmount" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="span">
            <el-form-item label="公司成本">
              <el-input :precision="2" v-model="form.businessExpenditureCosts" style="width: 100%" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button @click="reCalculate" type="warning" plain>重新计算</el-button>
        <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">

import {getCurrentInstance, reactive, ref, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import {ElForm} from "element-plus";
import {getCurrentDetail, reCalculateSalary, updateDetailTemp} from "@/api/system/hr/salary-detail";

const {proxy} = getCurrentInstance()!;
const {employee_types}
    = proxy?.useDict( "employee_types");

const {t} = useI18n()
const emit: any = defineEmits(['dialogOfClosedMethods'])
const dialogStatus: any = ref(false);

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
  }
})

const data: any = reactive({
  form: {
  },
  rules: {
  }
});
const {form, rules} = toRefs(data);
const loading = ref(true);
const span = ref(12);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);



watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.formId) {
          getCurrentDetail(props.formId).then((res: any) => {
            form.value = res.data;
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


function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

function submitForm() {
  updateDetailTemp(form.value).then((response: any) => {
    if (response.code === 0) {
      proxy?.$modal.msgSuccess(response.data);
      dialogOfClosedMethods(true);
    }
  })
}


function reset() {
  formRef?.value?.resetFields();
}

function reCalculate(): any {
  reCalculateSalary(form.value).then((response: any) => {
    if (response.code === 0) {
      form.value = response.data;
    }
  })
}
</script>
