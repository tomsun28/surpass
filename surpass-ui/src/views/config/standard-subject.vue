<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true"
                 @submit.native.prevent>
          <el-form-item label="会计准则">
            <el-select v-model="queryParams.standardId" @change="handleQuery" style="width: 200px">
              <el-option
                  v-for="item in standardList"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="t('subjectCategory')">
            <el-select v-model="queryParams.category" @change="handleQuery" style="width: 150px">
              <el-option
                  v-for="item in subjects_category"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item :label="t('subjectCode')">
            <el-input
                v-model="queryParams.code"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item :label="t('subjectName')">
            <el-input
                v-model="queryParams.name"
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
            type="primary"
            @click="handleReorgDisplayName"
        >重组全称
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
          :data="subjectList"
          @selection-change="handleSelectionChange"
          :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
          row-key="id"
          default-expand-all
          border
          max-height="600"
      >
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column type="index" label="序号" align="center" width="60"></el-table-column>
        <el-table-column prop="code" :label="t('subjectCode')" align="left" width="200" :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="name" :label="t('subjectName')" align="left" min-width="100"
                         :show-overflow-tooltip="true">
            <template #default="scope">
            <el-tooltip :content="scope.row.displayName" placement="top" effect="light">{{scope.row.name}}</el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="category" :label="t('subjectCategory')" align="left" min-width="70">
          <template #default="scope">
            <dict-tag-number :options="subjects_category" :value="scope.row.category"/>
          </template>
        </el-table-column>
        <el-table-column prop="direction" :label="t('subjectBalanceDirection')" align="center" min-width="60">
          <template #default="scope">
            <el-tag type="warning" v-if="scope.row.direction == 0">{{ t('subjectDirectionNone') }}</el-tag>
            <el-tag type="warning" v-if="scope.row.direction == 1">{{ t('subjectDebit') }}</el-tag>
            <el-tag type="success" v-if="scope.row.direction == 2">{{ t('subjectCredit') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" :label="t('subjectLevel')" align="center" min-width="60">
          <template #default="scope">
            <span v-if="scope.row.level == 1">1</span>
            <span v-else-if="scope.row.level == 2">2</span>
            <span v-else-if="scope.row.level == 3">3</span>
            <span v-else>{{ t('textOther') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="auxiliary" :label="t('subjectAuxiliary')" align="center" min-width="70">
          <template #default="scope">
            <dict-tag :options="subjects_auxiliary"
                      :value="scope.row.auxiliary ? JSON.parse(scope.row.auxiliary).map((t:any) => {return t.value}): ''"/>
          </template>
        </el-table-column>
        <el-table-column prop="classify" :label="t('subjectClassify')" align="left" min-width="100"  :show-overflow-tooltip="true"></el-table-column>

        <el-table-column prop="scope" :label="t('subjectScope')" align="left" min-width="100"  :show-overflow-tooltip="true"></el-table-column>
        <!--
        <el-table-column prop="parentName" :label="t('subjectParent')" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
                         -->
        <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center" width="80">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="移除">
              <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
    <edit-form v-if="open" :title="title" :open="open"
               :form-id="id"
               :subOptions="deptOptions"
               :currentTreeParentId="idParent"
               :standard-list="standardList"
               :defaultStandardId="queryParams.standardId"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
  </div>
</template>

<script setup lang="ts">
import DictTagNumber from "@/components/DIctTagNumber/index.vue";
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {useI18n} from "vue-i18n";
import {getTree, listSubjects,reorgDisplayName} from "@/api/system/standard/standard-subject";
import {listStandardsAll} from "@/api/system/standard/standard";
import editForm from "./standard-subject/edit.vue"
import modal from "@/plugins/modal";
import {deleteBatch} from "@/api/system/standard/standard-subject";
import {handleTree, handleTreeToList} from "@/utils/Surpass";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {subjects_category, subjects_auxiliary} = proxy?.useDict("subjects_category", "subjects_auxiliary");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    category:1,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);
const subjectList: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const title: any = ref("");
const id: any = ref(undefined);
const idParent: any = ref(undefined);
const total: any = ref(0);
const ids: any = ref<any>([]);
const selectionlist: any = ref<any>([]);
//默认展开节点
const treeData: any = ref<any[]>([]);//当前选中节点
const deptOptions: any = ref<any[]>([]);
//会计准则
const standardList: any = ref<any>([]);

function handleAdd(): any {
  id.value = undefined;
  idParent.value = undefined;
  title.value = t('jbx.text.add')
  open.value = true;
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return deleteBatch({listIds: ids.value});
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

function handleDelete(row: any) {
  modal.confirm(t('org.deleteTip1') + row.name + t('org.deleteTip2')).then(function () {
    return deleteBatch({listIds: [row.id]});
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

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  id.value = row.id;
  idParent.value = row.parentId;
  title.value = t('org.titleEdit');
  open.value = true;
}

/**
 * 获取列表
 */
function getList(): any {
  listSubjects(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      subjectList.value = handleTree(res.data.records, "id", "parentId", "children");
      console.log(subjectList.value);
      total.value = res.data.total;
    }
  })
}

function handleReorgDisplayName(): any {
  loading.value = true;
  reorgDisplayName(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      modal.msgSuccess(res.data);
    }
    loading.value = false;
  })
}


/*获取准则列表*/
function getStandardList() {
  listStandardsAll({}).then((res: any) => {
    if (res.code === 0) {
      if (Array.isArray(res.data) && res.data.length > 0) {
        standardList.value = res.data;
        queryParams.value.standardId = standardList.value[0].id;
      } else {
        // 如果数据为空数组时，确保有默认处理逻辑
        standardList.value = [];
        queryParams.value.standardId = null; // 或设置为适当的默认值
      }
      getList(); // 确保在赋值完成后调用
      getSubjectTree();
    }
  });
}

function handleQuery() {
  queryParams.value.pageNumber = 1;
  getList();
  getSubjectTree();
}

/**
 * 重置
 */
function resetQuery(): any {
  queryParams.value.code = undefined;
  queryParams.value.name = undefined;
  queryParams.value.category = undefined;
  handleQuery();
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  idParent.value = undefined;
  if (val) {
    getSubjectTree();
    getList();
  }
}

function getIndent(row: any) {
  if (row.code) {
    return (row.code.length - 2).toFixed(0)
  } else {
    return 0;
  }
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  children?: TreeNode[]; // 子节点可能不存在
}

function getSubjectTree(): any {
  getTree({standardId: queryParams.value.standardId}).then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
  });
}

getStandardList();
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
