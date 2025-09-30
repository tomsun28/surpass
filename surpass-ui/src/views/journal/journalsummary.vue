<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form :model="queryParams" ref="queryRef" inline v-show="showSearch" label-width="68">
        <el-form-item :label="$t('jbx.journalsummary.yearPeriod')" prop="yearPeriodPicker">
          <el-date-picker v-model="queryParams.yearPeriodPicker" placeholder="" type="month" value-format="YYYY-MM"
                          style="width: 130px" @keyup.enter.native="handleQuery"/>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
          <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('jbx.text.delete') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="list" show-summary :summary-method="getSummaries"
                border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.journalsummary.yearPeriod')" align="center" prop="yearPeriod"/>
        <el-table-column :label="$t('jbx.journalaccout.category.category')" align="center" prop="category">
          <template #default="scope">
            <span v-if="scope.row.category == 'deposit'">{{ $t('jbx.journalaccout.category.deposit') }}</span>
            <span v-if="scope.row.category == 'cash'">{{ $t('jbx.journalaccout.category.cash') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalsummary.accName')" align="left" prop="accName"/>
        <el-table-column :label="$t('jbx.journalsummary.openingBalance')" align="right" prop="openingBalance">
          <template #default="scope">
            {{ formatAmount(scope.row.openingBalance) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalsummary.income')" align="right" prop="income">
          <template #default="scope">
            {{ formatAmount(scope.row.income) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalsummary.expenditure')" align="right" prop="expenditure">
          <template #default="scope">
            {{ formatAmount(scope.row.expenditure) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalsummary.closingBalance')" align="right" prop="closingBalance">
          <template #default="scope">
            {{ formatAmount(scope.row.closingBalance) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalsummary.yearIncome')" align="right" prop="yearIncome">
          <template #default="scope">
            {{ formatAmount(scope.row.yearIncome) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalsummary.yearExpenditure')" align="right" prop="yearExpenditure">
          <template #default="scope">
            {{ formatAmount(scope.row.yearExpenditure) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center"
                         class-name="small-padding fixed-width" fixed="right" width="80">
          <template #default="scope">
            <!--<el-button
              @click="handleUpdate(scope.row)">
              {{ $t('jbx.text.edit') }}
            </el-button>
            -->
            <el-tooltip content="删除">
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
          @pagination="getList"
      />
    </el-card>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('jbx.journalsummary.yearPeriod')" prop="yearPeriodPicker">
          <el-date-picker v-model="form.yearPeriodPicker" placeholder="" type="month" value-format="YYYY-MM"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SecuritySocialsprovider" lang="ts">
import {ElForm} from "element-plus";
import dayjs from 'dayjs'
import {ref, getCurrentInstance, reactive, toRefs} from "vue";
import modal from "@/plugins/modal";
import * as journalSummaryService from "@/api/journal/journalsummaryservice";
import {formatAmount} from "@/utils"
import {useI18n} from "vue-i18n";
import {h} from 'vue'
import type {VNode} from 'vue'

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");
let tableSummary: any = ref([]);

const data: any = reactive({
  form: {
    id: undefined,
    scanCode: 'false',
    display: 'false',
    sortIndex: 1,
    status: 1
  },
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    yearPeriodPicker: dayjs(new Date()).format("YYYY-MM"),
    providerName: undefined
  },
  rules: {
    icon: [{required: true, message: 'Not empty', trigger: "blur"}],
    provider: [{required: true, message: 'Not empty', trigger: "blur"}],
    providerName: [{required: true, message: 'Not empty', trigger: "blur"}],
    clientId: [{required: true, message: 'Not empty', trigger: "blur"}],
    clientSecret: [{required: true, message: 'Not empty', trigger: "blur"}]
  },
});

const {queryParams, form, rules} = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  journalSummaryService.fetch(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.tableData.records;
      total.value = res.data.tableData.total;
      tableSummary = res.data.tableSummary;
    }
  });
}


/** 搜索按钮操作 */
function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery(): any {
  queryRef?.value?.resetFields();
  ;
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return journalSummaryService.del(id);
  }).then(() => {
    getList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {
  });
}


/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 重置新增的表单以及其他数据  */
function reset(): any {
  form.value = {
    id: undefined,
    yearPeriodPicker: dayjs(new Date()).format("YYYY-MM"),
    direction: "i",
    scanCode: 'false',
    display: 'false',
    sortIndex: 1,
    status: 1
  };
  console.log(form.value);
  formRef.value?.clearValidate();
}

/** 添加分组 */
function handleAdd(): any {
  reset();
  open.value = true;
  title.value = t('jbx.text.add');
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  reset();
  const id: any = row.id || ids.value;
  journalSummaryService.get(id).then((res: any) => {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
};


/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) => {
    if (valid) {
      if (form.value.id != undefined) {
        journalSummaryService.update(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        journalSummaryService.summaryAccount(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 取消按钮 */
function cancel(): any {
  open.value = false;
  reset();
}

const getSummaries = () => {
  const sums: (string | VNode)[] = []
  sums[0] = h('div', {style: {textDecoration: 'underline'}}, ['合计',])
  sums[1] = 'N/A'
  sums[2] = ''
  sums[3] = ''
  sums[4] = formatAmount(tableSummary.openingBalance);
  sums[5] = formatAmount(tableSummary.income);
  sums[6] = formatAmount(tableSummary.expenditure);
  sums[7] = formatAmount(tableSummary.closingBalance);
  sums[8] = formatAmount(tableSummary.yearIncome);
  sums[9] = formatAmount(tableSummary.yearExpenditure);

  return sums
}

getList();

</script>
<style scoped>
::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

.common-card {
  margin-bottom: 15px;
}

::v-deep(.common-card form .el-form-item--default) {
  margin-bottom: 0px;
}
</style>
