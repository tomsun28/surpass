<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="" prop="periodType">
            <el-radio-group v-model="queryParams.periodType" @change="handlePeriodType">
              <el-radio-button label="月度" value="month"></el-radio-button>
              <el-radio-button label="季度" value="quarter"></el-radio-button>
              <el-radio-button label="年度" value="year"></el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="选择月度" prop="reportDate"
                        v-if="queryParams.periodType === 'month'">
            <el-date-picker
                style="width: 130px"
                v-model="queryParams.date"
                type="month"
                :clearable="false"
                value-format="YYYY-MM"
                format="YYYY年MM期"
                :disabled-date="disabledDate"
                @change="handleQuery"
                placeholder="选择月">
            </el-date-picker>
          </el-form-item>

          <el-form-item label="选择季度" prop="reportDate"
                        v-if="queryParams.periodType === 'quarter'">
            <el-date-picker
                style="width: 100px"
                v-model="queryParams.date"
                :clearable="false"
                type="year"
                format="YYYY"
                value-format="YYYY"
                :disabled-date="disabledDate"
                :prefix-icon="customPrefix"
                @change="handleQuery"
                placeholder="选择年">
            </el-date-picker>
            <el-radio-group style="margin-left: 10px"
                            v-model="queryParams.reportQuarter" @change="handleQuery">
              <el-radio-button label="第一季度" value="Q1"></el-radio-button>
              <el-radio-button label="第二季度" value="Q2"></el-radio-button>
              <el-radio-button label="第三季度" value="Q3"></el-radio-button>
              <el-radio-button label="第四季度" value="Q4"></el-radio-button>
            </el-radio-group>
          </el-form-item>

          <el-form-item label="选择年度" prop="reportDate"
                        v-if="queryParams.periodType === 'year'">
            <el-date-picker
                @change="handleQuery"
                v-model="queryParams.date"
                type="year"
                style="width: 100px"
                :clearable="false"
                :disabled-date="disabledDate"
                value-format="YYYY"
                placeholder="选择年">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">刷新</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <!--        <el-button type="primary" @click="handleExport">导出</el-button>-->
      </div>
      <div style="display: flex;justify-content: flex-start">
        <div style="width: 300px;display: inline-block">
          <el-input
              style="width: 95%;margin-bottom: 10px"
              v-model="filterSubject"
              placeholder="快速搜索"
              suffix-icon="Search">
          </el-input>
          <el-tree
              v-if="subjectList.length > 0"
              ref="treeRef"
              style="height: 60vh;overflow-y: auto"
              :data="subjectList"
              :props="defaultProps"
              node-key="code"
              :current-node-key="subjectList[0].code"
              :filter-node-method="filterNodeMethod"
              @node-click="handleTreeNodeClick"
              :expand-on-click-node="false"
          />
        </div>
        <div style="width: calc(100% - 320px);margin-left: 20px;display: inline-block">
          <el-table v-loading="loading" :data="recordsList" height="570"
                    row-key="id"
                    show-summary
                    :summary-method="handleSummaryMethod2"
                    :span-method="objectSpanMethod" border>
            <el-table-column label="日期" align="center" prop="voucherDate"/>
            <el-table-column label="凭证字号" align="left" header-align="center" prop="word"/>
            <el-table-column label="摘要" align="left" header-align="center" prop="summary"/>
            <el-table-column label="借方金额" align="right" prop="debitAmount">
              <template #default="scope">
                {{ formatAmount(scope.row.debitAmount, '') }}
              </template>
            </el-table-column>
            <el-table-column label="贷方金额" align="right" prop="creditAmount">
              <template #default="scope">
                {{ formatAmount(scope.row.creditAmount, '') }}
              </template>
            </el-table-column>
            <el-table-column label="余额" align="right" prop="subjectBalance">
              <template #default="scope">
                {{ formatAmount(scope.row.subjectBalance, '') }}
              </template>
            </el-table-column>
          </el-table>

          <pagination
              v-show="total>0"
              :total="total"
              v-model:page="queryParams.pageNumber"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
          />
        </div>
      </div>

    </el-card>
  </div>
</template>

<script setup name="VoucherItems" lang="ts">
import * as subjectApi from "@/api/system/standard/standard-subject"
import * as apis from "@/api/system/voucher/voucher";
import {parseTime, getCurrentQuarter, handleTree} from '@/utils/Jinbooks'
import {h, ref, shallowRef, reactive, toRefs, watch} from 'vue'
import {formatAmount} from "@/utils"
import {useRouter, useRoute} from "vue-router";
import booksSetStore from "@/store/modules/bookStore";
import Template from "@/views/hr/salary-voucher-rules/template.vue";
import {TableColumnCtx, TreeInstance} from "element-plus";
import {handleSummaryMethod, SummaryMethodProps} from "@/utils/Subjects";

const router = useRouter();
const route = useRoute();
const currBookStore = booksSetStore()
const loading = ref(true);
const showSearch = ref(true);
const total = ref(0);
const recordsList = ref<any>([]);
const filterSubject = ref<any>("");
// 会计科目数据
const subjectList = ref<any>([])
const spanDataMap = ref<any>({})
const treeRef = ref<TreeInstance>()

const defaultProps = {
  children: 'children',
  label: 'name',
}

interface Tree {
  [key: string]: any
}

interface SpanMethodProps {
  row: any
  column: TableColumnCtx<any>
  rowIndex: number
  columnIndex: number
}

const data = reactive({
  form: {},
  queryParams: {
    pageNumber: 1,
    pageSize: 20,
    orderByColumn: "voucherDate,wordHead,wordNum",
    isAsc: "desc,desc,desc",
    periodType: 'month',
    date: currBookStore.termCurrent,
    reportQuarter: getCurrentQuarter(),
    reportDate: currBookStore.termCurrent,
    subjectCode: route.query.subjectCode,
  },
});

const {queryParams} = toRefs(data);

const customPrefix = shallowRef({
  render() {
    return h('p', '年')
  },
})
const handlePeriodType = (value: string) => {
  if (value === 'month' && queryParams.value.date.length < 7) {
    queryParams.value.date = queryParams.value.date + "-01"
  }
  handleQuery()
}
const disabledDate = (time: any) => {
  const now = new Date(currBookStore.termCurrent + "-01")
  const start = new Date(currBookStore.termStart + "-01")
  return start.getTime() > time.getTime() || time.getTime() > now.getTime();
}

/** 查询凭证记录列表 */
function getList() {
  loading.value = true;
  apis.listVoucherSubLedger(queryParams.value).then((response: any) => {
    recordsList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
    // 初始化合并数据（首次渲染或数据变化时调用一次）
    spanDataMap.value = getSpanMap(recordsList.value, ['voucherDate', 'word'])
    console.log(spanDataMap.value)
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  if (queryParams.value.periodType === 'quarter') {
    queryParams.value.reportDate = queryParams.value.date.substring(0, 4) + ' ' + queryParams.value.reportQuarter
  } else {
    queryParams.value.reportDate = queryParams.value.date
  }
  getList();
}

function handleExport() {

}

function handleSummaryMethod2(param: SummaryMethodProps) {
  return handleSummaryMethod(param, recordsList.value, 0, [3, 4, 5])
}

const filterNodeMethod = (value: string, data: Tree) => {
  if (!value) return true
  value = value + ""
  return data.name.includes(value)
      || data.code.includes(value)
      || (data.pinyinCode && data.pinyinCode.toLowerCase().includes(value))
}

const handleTreeNodeClick = (data: Tree) => {
  queryParams.value.subjectCode = data.code
  handleQuery()
}

/**
 * 合并单元格
 * @param row
 * @param column
 * @param rowIndex
 * @param columnIndex
 */
const objectSpanMethod = ({
                            row,
                            column,
                            rowIndex,
                            columnIndex,
                          }: SpanMethodProps) => {
  const colProp = column.property
  if (['voucherDate', 'word'].includes(colProp)) {
    const colSpanInfo = spanDataMap.value.get(colProp)
    const rowspan = colSpanInfo?.get(rowIndex) ?? 1
    return {
      rowspan: rowspan,
      colspan: 1,
    }
  }

}

// 处理合并信息
const getSpanMap = (data: any[], keys: string[]) => {
  const spanMap = new Map<string, Map<number, number>>()

  keys.forEach(key => {
    const map = new Map<number, number>()
    let prev: any = null
    let spanCount = 0
    let start = 0

    data.forEach((item, index) => {
      if (item[key] === prev) {
        spanCount++
      } else {
        if (spanCount > 0) {
          map.set(start, spanCount + 1)
          for (let i = start + 1; i < start + 1 + spanCount; i++) {
            map.set(i, 0)
          }
        }
        start = index
        spanCount = 0
        prev = item[key]
      }
    })

    // 处理最后一组
    if (spanCount > 0) {
      map.set(start, spanCount + 1)
      for (let i = start + 1; i < start + 1 + spanCount; i++) {
        map.set(i, 0)
      }
    } else {
      map.set(start, 1)
    }

    spanMap.set(key, map)
  })

  return spanMap
}

watch(filterSubject, (val) => {
  treeRef.value!.filter(val)
})

subjectApi.getTree({
  bookId: currBookStore.bookId
}).then((res: any) => {
  subjectList.value = [{
    "id": "-1",
    "parentId": null,
    "name": "所有科目",
    "displayName": "所有科目",
  }, ...res.data]
  queryParams.value.subjectCode = ""
  getList();
})
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

</style>
