<!--年度结账列表-->
<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-tabs
          v-model="activeName"
          type="card"
          class="demo-tabs"
          @tab-click="handleClick"
      >
        <el-tab-pane label="期末处理" name="carry-forward">carry</el-tab-pane>
        <el-tab-pane label="结账" name="settle-period">settle</el-tab-pane>
        <el-tab-pane label="结账列表" name="settle-list">
          <div class="queryForm">
            <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">

              <el-form-item label="选择年度" prop="reportDate"
                            v-if="queryParams.periodType === 'year'">
                <el-date-picker
                    @change="handleQuery"
                    v-model="queryParams.date"
                    type="year"
                    style="width: 100px"
                    :clearable="false"
                    value-format="YYYY"
                    placeholder="选择年">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="handleQuery">查询</el-button>
              </el-form-item>
            </el-form>
          </div>
          <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column label="月份" align="center" prop="period">
              <template #default="scope">
                {{ scope.row.period }} 月
              </template>
            </el-table-column>
            <el-table-column :label="$t('jbx.text.status.status')" align="center" prop="status">
              <template #default="scope">
                <span v-if="scope.row.status == 6"><el-icon color="green" size="24"><CircleCheck/></el-icon></span>
                <span v-if="scope.row.status == 4"><el-icon color="#808080" size="24"><WarningFilled/></el-icon></span>
                <span v-if="scope.row.status == 1"><el-icon color="#67C23A" size="24"><Promotion/></el-icon></span>
                <span v-if="scope.row.status == 2"><el-icon color="#E6A23C" size="24"><Clock/></el-icon></span>
              </template>
            </el-table-column>
            <!--
            <el-table-column :label="$t('jbx.text.action')" width="150"  align="center"
                             class-name="small-padding fixed-width">
             <template #default="scope">
                <el-button
                  @click="handleUpdate(scope.row)">
                  {{ $t('jbx.text.edit') }}
                </el-button>
              </template>
            </el-table-column>
            -->
          </el-table>
          <pagination
              v-show="total > 0"
              :total="total"
              v-model:page="queryParams.pageNumber"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
          />

        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script lang="ts" setup>
import {getCurrentInstance, ref, toRefs, reactive} from 'vue'
import type {TabsPaneContext} from 'element-plus'
import {useRoute, useRouter} from "vue-router";
import * as settlementApi from "@/api/system/book/settlement";
import bookStore from "@/store/modules/bookStore";
import {parseTime} from "@/utils/Jinbooks";

const {proxy} = getCurrentInstance();
const currBookStore = bookStore()
const currentTerm = ref(currBookStore.termCurrent || parseTime(new Date(), "{y}-{m}"));

const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(false);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

const activeName = ref('settle-list')
const router: any = useRouter();

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

const {queryParams, form, rules} = toRefs(data);


/** 分页列表 */
function getList(): any {
  loading.value = true;
  queryParams.value.year = (queryParams.value.date + "").substring(0, 4);
  console.log(queryParams.value.year)
  settlementApi.fetch(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.records;
      total.value = res.data.total;
    } else {
      //proxy?.$modal.msgSuccess(res.message);
    }
  });
}

/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 添加分组 */
function handleAdd(): any {
  open.value = true;
  // title.value = t('jbx.text.add');
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {

  const id: any = row.id || ids.value;
  /*
  get(id).then((res: any) =>  {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
  */
};

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  /* modal.confirm(t('jbx.confirm.text.delete')).then(function () {
     return del(id);
   }).then(() => {
     getList();
     modal.msgSuccess(t('jbx.alert.operate.success'));
   }).catch(() => {});
   */
}


/** 搜索按钮操作 */
function handleQuery() {

  getList();
}

const handleClick = (tab: TabsPaneContext, event: Event) => {
  proxy.$tab.openPage('/settlement/' + tab.paneName)
}

getList();
</script>

<style>
.demo-tabs > .el-tabs__content {

}
</style>