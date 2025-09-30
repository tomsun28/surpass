<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
        <fund-balance style="height: 350px"></fund-balance>
      </el-col>
      <el-col :xs="24" :sm="24" :md="16" :lg="12" :xl="12">
        <receivable style="height: 350px"></receivable>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
        <expected-available-funds style="height: 350px"></expected-available-funds>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
        <net-profit style="height: 450px"></net-profit>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
        <revenue-cost style="height: 450px"></revenue-cost>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
        <cost style="height: 450px"></cost>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="6" :xl="6">
        <added-tax style="height: 450px"></added-tax>
      </el-col>
      <el-col :span="24">
        <other-subjects style="height: 450px"></other-subjects>
      </el-col>
    </el-row>
    <Footer position="relative"></Footer>
  </div>
</template>


<script setup name="Index" lang="ts">
import {ref, getCurrentInstance} from "vue";
import {useI18n} from 'vue-i18n'
import {useRoute, useRouter} from "vue-router";
import Footer from "@/components/Footer/index.vue"
import FundBalance from "@/views/dashboard/accounting/fund_balance.vue";
import receivable from "@/views/dashboard/accounting/receivable.vue";
import ExpectedAvailableFunds from "@/views/dashboard/accounting/expected_available_funds.vue";
import NetProfit from "@/views/dashboard/accounting/net_profit.vue";
import RevenueCost from "@/views/dashboard/accounting/revenue_cost.vue";
import Cost from "@/views/dashboard/accounting/cost.vue";
import AddedTax from "@/views/dashboard/accounting/added_tax.vue";
import OtherSubjects from "@/views/dashboard/accounting/other_subjects.vue";

const {t} = useI18n()
const route: any = useRoute();
const router: any = useRouter();
const {proxy} = getCurrentInstance()!;


</script>
<style scoped lang="scss">
:deep(.el-card) {
  --el-card-border-radius: 10px;
}

.el-col {
  margin-bottom: 20px;
}
</style>
