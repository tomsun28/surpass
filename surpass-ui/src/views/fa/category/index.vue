<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item label="分类编码">
            <el-input v-model="queryParams.categoryCode"/>
          </el-form-item>
          <el-form-item label="分类名称">
            <el-input v-model="queryParams.categoryName"/>
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
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0" type="danger">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList"
                border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="categoryCode" label="分类编码" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="categoryName" label="分类名称" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="depreciationSubjectName" label="折旧方法" align="center" min-width="60"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag-number :options="depreciation_method" :value="scope.row.depreciationMethod"/>
          </template>
        </el-table-column>
        <el-table-column prop="depreciationYears" label="预计使用年限" align="center" min-width="70"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="residualRate" label="残值率" align="center" min-width="50"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <span>{{scope.row.residualRate}}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="assetSubjectName" label="固定资产科目" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="depreciationSubjectName" label="累计折旧科目" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="expenseSubjectName" label="折旧费用科目" align="center" min-width="100"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column label="操作" align="center" width="110">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" type="danger" link @click="handleDelete(scope.row)"></el-button>
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
               :methods="depreciation_method"
               :dept-options="deptOptions"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
  </div>
</template>
<script setup lang="ts">
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {useI18n} from "vue-i18n";
import {delLead, fetchPage} from "@/api/fa/category";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import {useRouter} from "vue-router";
import EditForm from "./edit.vue"
import {getTree} from "@/api/system/standard/standard-subject";
import bookStore from "@/store/modules/bookStore";
import {TreeNode} from "@/types/commnon";

const currBookStore = bookStore()
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {depreciation_method}
    = proxy?.useDict("depreciation_method");
const router = useRouter()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  form: {},
  rules: {},
});
const dataList = ref([]);
const open = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {queryParams, form, rules} = toRefs(data);
const deptOptions: any = ref<any[]>([]);


/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.categoryCode = undefined;
  queryParams.value.categoryName = undefined;
  handleQuery();
}

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = "修改资产类别";
  open.value = true;
}

function handleShow(row: any) {
  router.push({path: '/lead/detail', query: {id: row.id}});
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除分类编号为"' + _ids + '"的数据项？').then(function () {
    return delLead({listIds: [_ids]});
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}


/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return delLead({listIds: ids.value});
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function handleAdd() {
  id.value = undefined;
  title.value = "新增资产类别";
  open.value = true;
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}


function getSetTree() {
  getTree({bookId: currBookStore.bookId}).then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
  });
}


getList()
getSetTree();
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 10px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
