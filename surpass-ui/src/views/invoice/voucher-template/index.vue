<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-row>
            <el-form-item label="发票号码">
              <el-input
                  v-model="queryParams.templateName"
                  placeholder="请输入发票号码"
                  clearable
                  @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
              <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
            </el-form-item>
          </el-row>
          <el-row>
            <el-form-item>
              <el-radio-group v-model="queryParams.direction" @change="getList">
                <el-radio-button value="INPUT">进项发票</el-radio-button>
                <el-radio-button value="OUTPUT">销项发票</el-radio-button>
              </el-radio-group>
            </el-form-item>
          </el-row>
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
        <el-table-column type="selection" width="50" align="center" fixed/>
        <el-table-column prop="templateName" min-width="100" align="center" label="模板名称"
                         :show-overflow-tooltip="true"/>
        <el-table-column prop="wordHead" min-width="40" align="center" label="凭证字"
                         :show-overflow-tooltip="true"/>
        <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column prop="isDefault" min-width="40" align="center" label="优先级"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <span v-if="scope.row.isDefault === 1">默认模板</span>
            <el-button link type="primary" v-else size="small" @click="setDefault(scope.row)">设为默认</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="110" fixed="right">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" type="danger" link @click="handleDelete(scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <edit-form :title="title" :open="open"
               :form-id="id"
               :word-heads="word_heads"
               :dept-options="subjectList"
               :directions="salary_directions"
               :selected-values="selected_values"
               :invoice-direction="queryParams.direction"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
  </div>
</template>

<script setup lang="ts">
import {ElForm} from "element-plus";
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {useI18n} from "vue-i18n";
import {delTemplate, fetchPage, setTemplateAsDefault} from "@/api/invoice/voucher-template";
import modal from "@/plugins/modal";
import EditForm from "@/views/invoice/voucher-template/edit.vue";
import * as subjectApi from "@/api/system/standard/standard-subject";
import booksSetStore from "@/store/modules/bookStore";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {word_heads, salary_directions, selected_values}
    = proxy?.useDict("word_heads", 'salary_directions', 'selected_values');
const currBookStore = booksSetStore()

const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const dataList = ref([]);
const open = ref(false);
const data: any = reactive({
  queryParams: {
    direction: 'INPUT',
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});
const {queryParams} = toRefs(data);
const ids = ref([]);
const selectionlist: any = ref<any>([]);
// 会计科目数据
const subjectList = ref<any>([])

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.templateName = undefined;
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

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

function handleAdd() {
  let subtitle = "进项发票";
  switch (queryParams.value.direction) {
    case "INPUT":
      // 不改，保持默认“销项发票”
      break;
    case "OUTPUT":
      subtitle = "销项发票";
      break;
    default:
      // 当以上所有情况都不匹配时执行
      break;
  }
  id.value = undefined;
  title.value = "新增发票模板 - " + subtitle;
  open.value = true;
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return delTemplate({listIds: ids.value});
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

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function getSubjectList() {
  //传入当前账套ID
  subjectApi.getTree({
    bookId: currBookStore.bookId
  }).then((res: any) => {
    subjectList.value = res.data
  })
}

function setDefault(row: any) {
  const data = {
    id: row.id,
    direction: queryParams.value.direction,
  }
  setTemplateAsDefault(data).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess("设定成功")
    } else {
      modal.msgError("设定失败")
    }
  })
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除模板ID为"' + _ids + '"的数据项？').then(function () {
    return delTemplate({listIds: [_ids]});
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

function handleUpdate(row: any) {
  let subtitle = "进项发票";
  switch (queryParams.value.direction) {
    case "INPUT":
      // 不改，保持默认“销项发票”
      break;
    case "OUTPUT":
      subtitle = "销项发票";
      break;
    default:
      // 当以上所有情况都不匹配时执行
      break;
  }
  id.value = row.id;
  title.value = "修改发票模板 - " + subtitle;
  open.value = true;
}


getSubjectList();
getList()
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
