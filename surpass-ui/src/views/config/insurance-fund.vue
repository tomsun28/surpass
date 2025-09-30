<template>
  <div class="app-container">
    <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
      <el-card class="common-card query-box">
        <template #header>
          <div>基础信息</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="缴纳基数" prop="payBase">
              <el-input-number v-model="form.payBase" style="width: 180px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用合计：" label-width="110">
              <b>{{ form.payBaseBusiness }}<span>元</span></b>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用合计：" label-width="110">
              <b>{{ form.payBasePersonal }}<span>元</span></b>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>养老保险</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="企业比例" prop="endowmentBusiness">
              <el-input-number
                  v-model="form.endowmentBusiness"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px"
              >
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人比例" prop="endowmentPersonal">
              <el-input-number
                  v-model="form.endowmentPersonal"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用" prop="endowmentBusinessFee">
              <el-input-number v-model="form.endowmentBusinessFee" style="width: 180px"
                               disabled
              >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用" prop="endowmentPersonalFee">
              <el-input-number v-model="form.endowmentPersonalFee" style="width: 180px" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>医疗保险</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="企业比例" prop="medicalBusiness">
              <el-input-number
                  v-model="form.medicalBusiness"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人比例" prop="medicalPersonal">
              <el-input-number
                  v-model="form.medicalPersonal"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用" prop="medicalBusinessFee">
              <el-input-number v-model="form.medicalBusinessFee" style="width: 180px"
                               disabled
              >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用" prop="medicalPersonalFee">
              <el-input-number v-model="form.medicalPersonalFee" style="width: 180px" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>失业保险</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="企业比例" prop="unemploymentBusiness">
              <el-input-number
                  v-model="form.unemploymentBusiness"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人比例" prop="unemploymentPersonal">
              <el-input-number
                  v-model="form.unemploymentPersonal"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用" prop="unemploymentBusinessFee">
              <el-input-number v-model="form.unemploymentBusinessFee" style="width: 180px"
                               disabled
              >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用" prop="unemploymentPersonalFee">
              <el-input-number v-model="form.unemploymentPersonalFee" style="width: 180px" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>工伤保险</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="企业比例" prop="employmentInjuryBusiness">
              <el-input-number
                  v-model="form.employmentInjuryBusiness"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人比例" prop="employmentInjuryPersonal">
              <el-input-number
                  v-model="form.employmentInjuryPersonal"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用">
              <el-input-number v-model="form.employmentInjuryBusinessFee" style="width: 180px"
                               disabled
              >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用">
              <el-input-number v-model="form.employmentInjuryPersonalFee" style="width: 180px" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>生育保险</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="企业比例" prop="maternityBusiness">
              <el-input-number
                  v-model="form.maternityBusiness"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人比例" prop="maternityPersonal">
              <el-input-number
                  v-model="form.maternityPersonal"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用">
              <el-input-number v-model="form.maternityBusinessFee" style="width: 180px"
                               disabled
              >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用" prop="unemploymentPersonalFee">
              <el-input-number v-model="form.maternityPersonalFee" style="width: 180px" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>大病医疗</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="公司费用">
              <el-input-number v-model="form.seriousMedicalBusiness" style="width: 180px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用">
              <el-input-number v-model="form.seriousMedicalPersonal" style="width: 180px">
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
      <el-card class="common-card query-box">
        <template #header>
          <div>住房公积金</div>
        </template>
        <el-row :gutter="20">
          <el-col :span="6">
            <el-form-item label="企业比例" prop="providentFundSupBusiness">
              <el-input-number
                  v-model="form.providentFundSupBusiness"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人比例" prop="providentFundSupPersonal">
              <el-input-number
                  v-model="form.providentFundSupPersonal"
                  :min="0"
                  :max="100"
                  :precision="1"
                  style="width: 180px">
                <template #suffix>
                  <span>%</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="公司费用">
              <el-input-number v-model="form.providentFundSupBusinessFee" style="width: 180px"
                               disabled
              >
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="个人费用">
              <el-input-number v-model="form.providentFundSupPersonalFee" style="width: 180px" disabled>
                <template #suffix>
                  <span>元</span>
                </template>
              </el-input-number>
            </el-form-item>
          </el-col>
        </el-row>
      </el-card>
    </el-form>

    <div class="dialog-footer">
      <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, nextTick} from "vue";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";

import {useI18n} from "vue-i18n";
import {configGetCurrent, configUpdateCurrent} from "@/api/config/insurance-fund";
import Template from "@/views/hr/salary-voucher-rules/template.vue";

const {proxy} = getCurrentInstance()!;
const {t} = useI18n()

const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const data: any = reactive({
  form: {},
  rules: {
    payBase: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    endowmentPersonal: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    endowmentBusiness: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    medicalPersonal: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    medicalBusiness: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    unemploymentPersonal: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    unemploymentBusiness: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    employmentInjuryPersonal: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    employmentInjuryBusiness: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    maternityPersonal: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    maternityBusiness: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    providentFundSupPersonal: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
    providentFundSupBusiness: [
      {required: true, message: "请填写", trigger: "blur"}
    ],
  },
});

const {form, rules} = toRefs(data);
const loading: any = ref(true);

function percentValue(feeValue: number, percentValue: number): Number {
  return Math.round(Number((feeValue * percentValue).toFixed(3))) / 100;
}

// 计算费用的方法
const calculateFees = () => {
  // 养老
  form.value.endowmentBusinessFee = percentValue(form.value.payBase, form.value.endowmentBusiness);
  form.value.endowmentPersonalFee = percentValue(form.value.payBase, form.value.endowmentPersonal);

  //医疗
  form.value.medicalBusinessFee = percentValue(form.value.payBase, form.value.medicalBusiness);
  form.value.medicalPersonalFee = percentValue(form.value.payBase, form.value.medicalPersonal);

  //失业
  form.value.unemploymentBusinessFee = percentValue(form.value.payBase, form.value.unemploymentBusiness)
  form.value.unemploymentPersonalFee = percentValue(form.value.payBase, form.value.unemploymentPersonal);
  //工伤
  form.value.employmentInjuryBusinessFee = percentValue(form.value.payBase, form.value.employmentInjuryBusiness);
  form.value.employmentInjuryPersonalFee = percentValue(form.value.payBase, form.value.employmentInjuryPersonal);

  //生育
  form.value.maternityBusinessFee = percentValue(form.value.payBase, form.value.maternityBusiness);
  form.value.maternityPersonalFee = percentValue(form.value.payBase, form.value.maternityPersonal);

  //住房公积金
  form.value.providentFundSupBusinessFee = percentValue(form.value.payBase, form.value.providentFundSupBusiness);
  form.value.providentFundSupPersonalFee = percentValue(form.value.payBase, form.value.providentFundSupPersonal);
  //合计
  form.value.payBaseBusiness = form.value.endowmentBusinessFee
      + form.value.medicalBusinessFee
      + form.value.unemploymentBusinessFee
      + form.value.employmentInjuryBusinessFee
      + form.value.maternityBusinessFee
      + form.value.providentFundSupBusinessFee
      + form.value.seriousMedicalBusiness;
  form.value.payBasePersonal = form.value.endowmentPersonalFee
      + form.value.medicalPersonalFee
      + form.value.unemploymentPersonalFee
      + form.value.employmentInjuryPersonalFee
      + form.value.maternityPersonalFee
      + form.value.providentFundSupPersonalFee
      + form.value.seriousMedicalPersonal;
}

// 监听缴费基数和比例的变化
watch(
    [
      () => form.value.payBase,
      () => form.value.endowmentBusiness,
      () => form.value.endowmentPersonal,
      () => form.value.medicalPersonal,
      () => form.value.medicalBusiness,
      () => form.value.unemploymentPersonal,
      () => form.value.unemploymentBusiness,
      () => form.value.employmentInjuryPersonal,
      () => form.value.employmentInjuryBusiness,
      () => form.value.maternityPersonal,
      () => form.value.maternityBusiness,
      () => form.value.providentFundSupPersonal,
      () => form.value.providentFundSupBusiness
    ],
    () => {
      calculateFees()
    }
)

function get(): any {
  loading.value = true;
  configGetCurrent().then((res: any) => {
    form.value = res.data
    nextTick(() => {
      calculateFees()
      loading.value = false;
    })
  });
}

/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) => {
    if (valid) {
      configUpdateCurrent(form.value).then((response: any) => {
        modal.msgSuccess(t('jbx.alert.operate.success'));
        get()
      });
    }
  });
}


get();
</script>

<style scoped lang="scss">
.dialog-footer {
  text-align: center;
}

.query-box {
  margin-bottom: 8px !important;
}
</style>
