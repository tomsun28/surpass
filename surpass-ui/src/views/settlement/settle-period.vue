<!--结账-->
<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-tabs
          v-model="activeName"
          type="card"
          class="demo-tabs"
          @tab-click="handleClick"
      >
        <el-tab-pane label="期末处理" name="carry-forward"></el-tab-pane>
        <el-tab-pane label="结账" name="settle-period">
          <div class="queryForm">
            <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="150px">
              <el-form-item label="当前账期：">
                {{ currentTerm }}
              </el-form-item>
            </el-form>
          </div>
          <el-steps :active="active" finish-status="success">
            <el-step title="结账前自查"/>
            <el-step title="结账检查"/>
            <el-step title="结账"/>
          </el-steps>

          <el-table v-if="active == 0" :data="tableCheckData" border style="width: 100%">
            <el-table-column prop="item" label="检查项目" width="180"/>
            <el-table-column prop="content" label="检查内容"/>
          </el-table>
          <el-table v-loading="loadingVerify" v-if="active == 1 ||active == 2" :data="tableVerifyData" border
                    style="width: 100%">
            <el-table-column prop="id" label="序号"/>
            <el-table-column prop="item" label="检查项目"/>
            <el-table-column prop="result" label="结果">
              <template #default="scope">
                <span v-if="scope.row.result == true"><el-icon color="#67C23A"><Select/></el-icon></span>
                <span v-if="scope.row.result == false"><el-icon color="#F56C6C"><CloseBold/></el-icon></span>
              </template>
            </el-table-column>

          </el-table>
          <el-row v-if="isCheckout">
            <el-col v-if="checkoutResult" :sm="12" :lg="6">
              <el-result
                  icon="success"
                  title="结账成功"
                  sub-title="本期结账已经完成，进入下一个账期"
              ></el-result>
            </el-col>
            <el-col v-if="!checkoutResult" :sm="12" :lg="6">
              <el-result
                  icon="error"
                  title="结账失败"
                  sub-title="请检查相关要素，再重新结账"
              >
              </el-result>
            </el-col>
          </el-row>

          <div style="margin-top: 12px">
            <el-button v-if="active == 0" @click="next">下一步</el-button>
            <el-button v-if="active == 1" type="primary" v-loading="loadingVerify" @click="handleVerify">检查</el-button>
            <el-button v-if="active == 1 && isVerify" @click="next">下一步</el-button>
            <el-button v-if="active == 2" type="primary"  v-loading="checkoutButtonLoading" :disable="checkoutButtonLoading" @click="handleQuery">结账</el-button>
            <el-button v-if="active == 3" type="primary" @click="handleConfirm">确定</el-button>
          </div>
        </el-tab-pane>
        <el-tab-pane label="结账列表" name="settle-list"></el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {ref, toRefs, reactive, getCurrentInstance} from 'vue'
import type {TabsPaneContext} from 'element-plus'
import {useRoute, useRouter} from "vue-router";
import * as settlementApi from "@/api/system/book/settlement";
import bookStore from "@/store/modules/bookStore";
import { disable } from '@/api/system/apps';

const currBookStore = bookStore()
const currentTerm = ref(currBookStore.termCurrent);
const loading: any = ref(false);
const total: any = ref(0);
const list: any = ref<any>([]);
const active = ref(0)
const isVerify: any = ref(false);
const isCheckout: any = ref(false);
const checkoutResult: any = ref(false);
//const loadingInstance = ElLoading.service({});
const loadingVerify: any = ref(false);
const {proxy} = getCurrentInstance();
const checkoutButtonLoading = ref(false)

const data = reactive({
  //form: {...initFormData},
  queryParams: {
    periodType: 'year',
    date: currentTerm,
    year: (currentTerm + "").substring(0, 4),
    pageNumber: 1,
    pageSize: 10,
    providerName: undefined

  },
  rules: {
    yearPeriod: [
      {required: true, message: '期间不能为空', trigger: 'blur'}
    ],
  }
});

const activeName = ref('settle-period')
const router: any = useRouter();
const {queryParams, form, rules} = toRefs(data);

const tableCheckData = [
  {
    item: '检查银行和现金',
    content: '余额与银行对账单和库存现金盘点表比对，与出纳日记账对比',
  },
  {
    item: '检查往来款项',
    content: '核对供应商和客户的往来款项，外部往来催收，内部往来清理',
  },
  {
    item: '检查存货',
    content: '账实一致',
  },
  {
    item: '检查主营业务收入',
    content: '明细账本期发生额与税控系统发票统计表及无票收入备查账',
  },
  {
    item: '检查工资、五险一金',
    content: '检查工资是否计提发放、检查五险一金是否计提和缴纳',
  },
  {
    item: '检查折旧、摊销',
    content: '检查固定资产是否折旧，费用是否摊销，无形资产是否摊销。',
  },
  {
    item: '检查利息费用',
    content: '检查利息是否计提',
  },
  {
    item: '检查税费',
    content: '增值税是否结转、税金及附加是否计提，企业所得税是否计提',
  },
  {
    item: '检查原始凭证',
    content: '除结转和错账更正可以不附原始凭证，费用是否授权审批',
  },
  {
    item: '检查科目余额表',
    content: '检杳每一科目的发生金额、方向、余额大小、方向是否有异常需要从余额表穿透至明细账、记账凭证、原始凭证去核实异常原因。',
  },
];

const tableVerifyData: any = ref<any>([]);

function handleQuery() {
  queryParams.value.year = (queryParams.value.date + "").substring(0, 4);
  checkoutButtonLoading.value= true;
  settlementApi.checkout(queryParams.value).then((res: any) => {
    loading.value = false;
    checkoutButtonLoading.value= false;
    isCheckout.value = true;
    active.value++;
    if (res.code === 0) {
      checkoutResult.value = true;
    }
  }).catch((err: any) => {
    checkoutButtonLoading.value= false;
    loading.value = false;
    isCheckout.value = true;
    checkoutResult.value = false;
    active.value++;
  });
}

function handleVerify() {
  loadingVerify.value = true;
  settlementApi.verify().then((res: any) => {
    loading.value = false;
    loadingVerify.value = false;
    tableVerifyData.value = res.data;
    if (res.code == 0) {
      isVerify.value = true;
    }
  }).catch((err: any) => {
    loadingVerify.value = false;
    loading.value = false;
    tableVerifyData.value = err.data;
  })
}

function handleConfirm() {
  window.location.reload();
}

const next = () => {
  active.value++;
  console.log("step active " + active)
}

const handleClick = (tab: TabsPaneContext, event: Event) => {
  proxy.$tab.openPage('/settlement/' + tab.paneName)
}
</script>

<style>
.demo-tabs > .el-tabs__content {

}
</style>
