<template>
  <el-card shadow="hover" header-class="el-card-header">
    <template #header>
      <div class="card-title">
        <span>预计可用资金</span>
        <el-select disabled style="width: 120px" v-model="queryParams.accountPeriod" placeholder="">
          <template #label>
            <span>{{ accountPeriod }}</span>
          </template>
          <template v-for="(item, index) in statistics_period" :key="index">
            <el-option :label="item.label" :value="item.value"></el-option>
          </template>
        </el-select>
      </div>
    </template>

    <div class="card-content" v-loading="loading">
      <div class="card-content-item bold">
        <div class="flex justify-items-center">
          <span>预计可用资金</span>
          <el-tooltip content=""
                      placement="top">
            <template #content>
              <span>
                根据所选期间期末，企业的资金及往来款项情况估算出的预计可用资金。
              </span>
            </template>
            <el-icon>
              <Warning/>
            </el-icon>
          </el-tooltip>
        </div>
        <div>{{ formatAmount(resData.balance) }}</div>
      </div>

      <div class="card-content-item calc-formula">
        <div class="text-center">
          <div>现有资金</div>
          <div>{{ formatAmount(resData.cashBalance) }}</div>
        </div>
        <div>+</div>
        <div class="text-center">
          <div>
            <span>短期应收款</span>
            <el-tooltip content=""
                        placement="top">
              <template #content>
              <span>
                企业短期的应收款，包括应收账款、其他应收款、应收票据、应收利息、应收股利
              </span>
              </template>
              <el-icon>
                <Warning/>
              </el-icon>
            </el-tooltip>
          </div>
          <div>{{ formatAmount(resData.accountsReceivable) }}</div>
        </div>
        <div>-</div>
        <div class="text-center">
          <div>
            <span>短期应付款</span>
            <el-tooltip content=""
                        placement="top">
              <template #content>
              <span>
                企业短期的应付款项，包括应付账款、其他应付款、应付职工薪酬、应交税金、应付利息、应付股利
              </span>
              </template>
              <el-icon>
                <Warning/>
              </el-icon>
            </el-tooltip>
          </div>
          <div>{{ formatAmount(resData.accountsPayable) }}</div>
        </div>
      </div>

      <div class="card-content-item ratio">
        <div class="text-center">
          <div>
            <span>现金比率</span>
            <el-tooltip content=""
                        placement="top">
              <template #content>
              <span>
                年初至最近已结账期间的现金比率，反映企业现金及现金等价物对流动负债的保障程度。
                <br/>计算公式为：现金比率=(货币资金+交易性金融资产+应收票据)／流动负债
              </span>
              </template>
              <el-icon>
                <Warning/>
              </el-icon>
            </el-tooltip>
          </div>
          <div class="bold">{{resData.cashRatio}}%</div>
          <div>
            较同期：{{resData.cashRatioLastYear}}%
          </div>
        </div>
        <el-divider direction="vertical" style="height: 100%"></el-divider>
        <div class="text-center">
          <div>
            <span>速动比率</span>
            <el-tooltip content=""
                        placement="top">
              <template #content>
              <span>
                年初至最近已结账期间的速动比率，反映企业速动资产（流动资产中扣除存货部分）对流动负债的保障程度。
                <br/>计算公式为：速动比率=(流动资产-存货)/流动负债
              </span>
              </template>
              <el-icon>
                <Warning/>
              </el-icon>
            </el-tooltip>
          </div>
          <div class="bold">{{resData.quickRatio}}%</div>
          <div>
            较同期：{{ resData.quickRatioLastYear}}%
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>


<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, computed, onMounted} from "vue";
import bookStore from "@/store/modules/bookStore";
import {getAccountPeriod} from "@/utils/Surpass";
import {Warning} from "@element-plus/icons-vue"
import {formatAmount} from "@/utils";
import {statisticsAbleCash} from "@/api/dashboard"

const currBookStore = bookStore()
const {proxy} = getCurrentInstance()!;
const loading = ref(false);
const {statistics_period} = proxy!.useDict("statistics_period");

interface DataState {
  queryParams: {
    bookId: string;
    accountPeriod: string;
    periodType: string;
    reportDate: string;
  };
  resData: {
    balance: number;
    cashBalance: number;
    accountsReceivable: number;
    accountsPayable: number;
    cashRatio: number;
    cashRatioLastYear: number;
    quickRatio: number;
    quickRatioLastYear: number;
  };
}

const data = reactive<DataState>({
  queryParams: {
    bookId: currBookStore.bookId,
    accountPeriod: "currentPeriod",
    periodType: "month",
    reportDate: currBookStore.termCurrent
  },
  resData: {
    balance: 0,
    cashBalance: 0,
    accountsReceivable: 0,
    accountsPayable: 0,
    cashRatio: 0.00,
    cashRatioLastYear: 0.00,
    quickRatio: 0.00,
    quickRatioLastYear: 0.00
  }
});
const {queryParams, resData} = toRefs(data);
const accountPeriod = computed(() => {
  return getAccountPeriod(queryParams, currBookStore)
})

const getList = () => {
  loading.value = true
  const period = accountPeriod.value
  statisticsAbleCash(queryParams.value).then((res: any) => {
    resData.value = res.data
  }).finally(() => {
    loading.value = false
  })
}

onMounted(() => {
  getList()
})
</script>
<style scoped lang="scss">
:deep(.el-card__header) {
  padding: 4px 4px 4px 20px;
  background-color: #1890ff44;
  height: 30px;
}

:deep(.el-select__wrapper) {
  background-color: transparent;
  box-shadow: none;
}

.card-title {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-content {

  &-item {
    font-size: 0.9em;
    height: 30px;
    display: flex;
    justify-content: space-between;
    align-items: center;

    &.bold {
      font-size: 1em;
      font-weight: bold;
    }

    .bold {
      font-weight: bold;
      font-size: 1.2em;
    }

    &.calc-formula {
      margin: 20px 0;
      background-color: #EEEEEE88;
      border-radius: 10px;
      padding: 40px 10px;
      line-height: 1.5em;
    }

    &.ratio {
      justify-content: space-around;
      margin-top: 40px;
    }
  }
}
</style>
