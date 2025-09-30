<template>
  <el-card shadow="hover" header-class="el-card-header">
    <template #header>
      <div class="card-title">
        <span>资金余额</span>
        <el-select style="width: 120px" v-model="queryParams.accountPeriod" placeholder="" @change="getList">
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
          <span>资金余额</span>
          <el-tooltip content=""
                      placement="top">
            <template #content>
              <span>
                截止至所选期间期末，各资金账户的余额总计。
              </span>
            </template>
            <el-icon>
              <Warning/>
            </el-icon>
          </el-tooltip>
        </div>
        <div>{{ formatAmount(resData.balance) }}</div>
      </div>
      <template v-for="(item, index) in resData.subjectBalance" :key="index">
        <div v-if="index < 5" class="card-content-item">
          <div>{{ item.name }}</div>
          <div>{{ formatAmount(item.value) }}</div>
        </div>
      </template>
      <template v-if="resData.subjectBalance.length < 5" v-for="index in (5 - resData.subjectBalance.length)"
                :key="'em' + index">
        <div class="card-content-item">
          <div>--</div>
          <div>--</div>
        </div>
      </template>

      <div class="card-content-item bold" style="margin-bottom: 10px">
        <div class="flex justify-items-center">
          <span>资金净收入</span>
          <el-tooltip content=""
                      placement="top">
            <template #content>
              <span>
                所选期间内，资金收入减去支出的金额。
              </span>
            </template>
            <el-icon>
              <Warning/>
            </el-icon>
          </el-tooltip>
        </div>
        <div>{{ formatAmount(resData.netIncomeFunds) }}</div>
      </div>
      <div class="card-content-item">
        <SliderBar :left-value="resData.incomeFunds" :right-value="resData.payoutFunds"></SliderBar>
      </div>
    </div>
  </el-card>
</template>


<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, computed, onMounted} from "vue";
import bookStore from "@/store/modules/bookStore";
import {getAccountPeriod} from "@/utils/Jinbooks";
import {Warning} from "@element-plus/icons-vue"
import {formatAmount} from "@/utils";
import SliderBar from "./SliderBar.vue"
import {statisticsFundBalance} from "@/api/dashboard"
import {BaseValue} from "@/types/FundBalance"

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
    netIncomeFunds: number;
    incomeFunds: number;
    payoutFunds: number;
    subjectBalance: BaseValue[];
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
    netIncomeFunds: 0,
    incomeFunds: 0,
    payoutFunds: 0,
    subjectBalance: [],
  }
});
const {queryParams, resData} = toRefs(data);
const accountPeriod = computed(() => {
  return getAccountPeriod(queryParams, currBookStore)
})

const getList = () => {
  const period = accountPeriod.value
  loading.value = true;
  statisticsFundBalance(queryParams.value).then((res: any) => {
    resData.value = res.data
  }).finally(() => {
    loading.value = false;
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
  }
}
</style>
