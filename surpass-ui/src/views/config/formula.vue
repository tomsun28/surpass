<template>
  <div class="app-container">
    <el-card class="common-card">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true"
                 @submit.native.prevent>
          <el-form-item label="规则名称">
            <el-input
                v-model="queryParams.ruleName"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ t('org.button.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="onBatchDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-table
          v-loading="loading"
          :data="dataList"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="ruleName" label="规则名称" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="ruleDescription" label="规则描述" align="center" min-width="150"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="formulaText" label="计算公式" align="center" min-width="200"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="status" :label="t('org.status')" align="center" min-width="35">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="180">
          <template #default="scope">
            <el-button @click="handleUpdate(scope.row)">
              {{ t('org.edit') }}
            </el-button>
            <el-button type="danger" @click="handleDelete(scope.row)">{{ t('org.button.delete') }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          :page-sizes="queryParams.pageSizeOptions"
          @pagination="getList"
      />
    </el-card>
    <edit-form :title="title" :open="open"
               :form-id="id"
               :salary-types="salaryTypes"
               :operators="operators"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
  </div>
</template>

<script setup lang="ts">
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {useI18n} from "vue-i18n";
import modal from "@/plugins/modal";
import {deleteBatch, fetchPage} from "@/api/config/formula";
import editForm from "./formula/edit.vue"

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});
const ids: any = ref<any>([]);
const dataList: any = ref<any>([]);
const loading: any = ref(false);
const title = ref("");
const id: any = ref(undefined);
const open = ref(false);
const total: any = ref(0);
const {salaryTypes, operators}
    = proxy?.useDict("salaryTypes", "operators");
const selectionlist: any = ref<any>([]);

const {queryParams} = toRefs(data);

function handleQuery() {
  queryParams.value.pageNumber = 1;
  getList();
}

function getList() {
  fetchPage(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      dataList.value = res.data.records;
      total.value = res.data.total;
    }
  })
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return deleteBatch({listIds: ids.value});
  }).then((res: any) =>  {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

/**
 * 重置
 */
function resetQuery(): any {
  queryParams.value.ruleName = undefined;
  handleQuery();
}

function handleAdd() {
  title.value = "新增";
  id.value = undefined;
  open.value = true;
}

function handleUpdate(row: any) {
  title.value = "编辑";
  id.value = row.id;
  open.value = true;
}

function handleDelete(row: any) {
  modal.confirm(t('org.deleteTip1') + row.ruleName + t('org.deleteTip2')).then(function () {
    return deleteBatch({listIds: [row.id]});
  }).then((res: any) =>  {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) =>  item.id);
}

getList();
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
