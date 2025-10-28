<template>
  <el-row :gutter="10">
    <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
      <el-card style="height: 100%" shadow="hover" header-class="el-card-header">
        <template #header>
          <div class="card-title">
            <span>应收</span>
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
              <span>应收账款</span>
              <el-tooltip content=""
                          placement="top">
                <template #content>
              <span>
                截止至所选期间的期末，”应收账款“科目余额。
              </span>
                </template>
                <el-icon>
                  <Warning/>
                </el-icon>
              </el-tooltip>
            </div>
            <div>{{ formatAmount(recvData.balance) }}</div>
          </div>
          <template v-for="(item, index) in recvData.subjectBalance" :key="index">
            <div class="card-content-item">
              <div>{{ item.name }}</div>
              <div>{{ formatAmount(item.value) }}</div>
            </div>
          </template>
          <template v-if="recvData.subjectBalance.length < 5" v-for="index in (5 - recvData.subjectBalance.length)"
                    :key="'em' + index">
            <div class="card-content-item">
              <div>--</div>
              <div>--</div>
            </div>
          </template>

          <div class="card-content-item bold">
            <div class="flex justify-items-center">
              <span>平均周转天数</span>
              <el-tooltip content=""
                          placement="top">
                <template #content>
              <span>
                年初截止上一期期间的应收账款周转天数，反映企业回款速度，天数越小则回款速度越快。
                <br/>计算公式为：年初至所选期末总天数/(2*营业收入/(期初应收账款+期末应收账款))
              </span>
                </template>
                <el-icon>
                  <Warning/>
                </el-icon>
              </el-tooltip>
            </div>
            <div>{{ recvData.cycleDays }}天</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :xs="24" :sm="24" :md="12" :lg="12" :xl="12">
      <el-card style="height: 100%" shadow="hover" header-class="el-card-header">
        <template #header>
          <div class="card-title">
            <span>应付</span>
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
              <span>应付账款</span>
              <el-tooltip content=""
                          placement="top">
                <template #content>
              <span>
                截止至所选期间的期末，”应付账款“科目余额。
              </span>
                </template>
                <el-icon>
                  <Warning/>
                </el-icon>
              </el-tooltip>
            </div>
            <div>{{ formatAmount(payData.balance) }}</div>
          </div>
          <template v-for="(item, index) in payData.subjectBalance" :key="index">
            <div class="card-content-item">
              <div>{{ item.name }}</div>
              <div>{{ formatAmount(item.value) }}</div>
            </div>
          </template>
          <template v-if="payData.subjectBalance.length < 5" v-for="index in (5 - payData.subjectBalance.length)"
                    :key="'em' + index">
            <div class="card-content-item">
              <div>--</div>
              <div>--</div>
            </div>
          </template>
        </div>
      </el-card>
    </el-col>
  </el-row>
</template>


<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, computed, onMounted} from "vue";
import bookStore from "@/store/modules/bookStore";
import {getAccountPeriod} from "@/utils/Surpass";
import {Warning} from "@element-plus/icons-vue"
import {formatAmount} from "@/utils";
import {BaseValue} from "@/types/FundBalance";
import {statisticsAccountsReceivable, statisticsAccountsPayable} from "@/api/dashboard"

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
  recvData: {
    balance: number;
    cycleDays: number;
    subjectBalance: BaseValue[];
  };
  payData: {
    balance: number;
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
  recvData: {
    balance: 0,
    cycleDays: 0,
    subjectBalance: [],
  },
  payData: {
    balance: 0,
    subjectBalance: [],
  },
});
const {queryParams, recvData, payData} = toRefs(data);
const accountPeriod = computed(() => {
  return getAccountPeriod(queryParams, currBookStore)
})

const getList = () => {
  const period = accountPeriod.value
  loading.value = true
  statisticsAccountsReceivable(queryParams.value).then((res: any) => {
    recvData.value = res.data
  }).finally(() => {
    loading.value = false
  })
  statisticsAccountsPayable(queryParams.value).then((res: any) => {
    payData.value = res.data
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
  }
}
</style>
