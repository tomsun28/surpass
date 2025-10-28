<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true"
                 @submit.native.prevent>
          <el-form-item :label="t('subjectCategory')" style="width: 220px;">
            <el-select v-model="queryParams.category" clearable @change="handleQuery">
              <el-option
                  v-for="dict in subjects_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
            <!--
              <el-radio-group v-model="queryParams.category" @change="handleQuery">
                <el-radio-button v-for="item in subjects_category"
                                 :key="item.value"
                                 :label="item.label" :value="item.value"/>
              </el-radio-group>
              -->
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
      <div class="button-top">
        <el-button
            @click="handleAdd"
            type="primary">
          新增科目
        </el-button>
        <el-button
            @click="handleReorgDisplayName"
            type="primary">
          重组全称
        </el-button>
        <el-button
            :disabled="ids.length === 0"
            @click="handleRemove"
            type="danger">
          批量移除
        </el-button>
      </div>
      <el-table v-loading="loading" :data="subjectsList" border @selection-change="handleSelectionChange"
                :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
                row-key="id"
                height="730">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="code" :label="t('subjectCode')" align="left" width="140"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="name" :label="t('subjectName')" align="left" min-width="100"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <el-tooltip :content="scope.row.displayName" placement="top" effect="light">{{ scope.row.name }}
            </el-tooltip>
          </template>
        </el-table-column>
        <el-table-column prop="category" :label="t('subjectCategory')" align="left" min-width="70">
          <template #default="scope">
            <dict-tag-number :options="subjects_category" :value="scope.row.category"/>
          </template>
        </el-table-column>
        <el-table-column prop="direction" :label="t('subjectBalanceDirection')" align="center" min-width="50">
          <template #default="scope">
            <el-tag type="warning" v-if="scope.row.direction == 0">{{ t('subjectDirectionNone') }}</el-tag>
            <el-tag type="warning" v-if="scope.row.direction == 1">{{ t('subjectDebit') }}</el-tag>
            <el-tag type="success" v-if="scope.row.direction == 2">{{ t('subjectCredit') }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" :label="t('subjectLevel')" align="center" min-width="45">
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
                      :value="scope.row.auxiliary && scope.row.auxiliary.startsWith('[') ? JSON.parse(scope.row.auxiliary).map((t:any) => {return t.value}): ''"/>
          </template>
        </el-table-column>
        <el-table-column prop="parentName" :label="t('subjectParent')" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" width="80" align="center" fixed="right">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="移除">
              <el-button link icon="Delete" type="danger" @click="onDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!--      <pagination-->
      <!--          v-show="total > 0"-->
      <!--          :total="total"-->
      <!--          v-model:page="queryParams.pageNumber"-->
      <!--          v-model:limit="queryParams.pageSize"-->
      <!--          :page-sizes="queryParams.pageSizeOptions"-->
      <!--          @pagination="getList"-->
      <!--      />-->
      </el-card>
      <el-drawer v-if="editFlag" v-model="editFlag" size="450px" :title="editTitle"
                 :close-on-click-modal="false"
                 @close="cancel">
        <template #header>
          <h4>{{ editTitle }}</h4>
        </template>

        <el-form :model="form" :rules="rules" ref="formRef" label-width="110px" inline-message>
          <el-form-item prop="category" :label="$t('subjectCategory')" :required="true">
            <el-select v-model="form.category" clearable>
              <el-option
                  v-for="dict in subjects_category"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item prop="code" :label="t('subjectCode')" :required="true">
            <el-input v-model="form.code"/>
          </el-form-item>
          <el-form-item prop="name" :label="t('subjectName')" :required="true">
            <el-input v-model="form.name"/>
          </el-form-item>
          <el-form-item prop="displayName" :label="t('subjectDisplayName')">
            <el-input v-model="form.displayName" disabled/>
          </el-form-item>
          <el-form-item prop="pinyinCode" :label="t('subjectPinyinCode')">
            <el-input v-model="form.pinyinCode"/>
          </el-form-item>
          <el-form-item prop="pinyinDisplayCode" :label="t('subjectPinyinDisplayCode')">
            <el-input v-model="form.pinyinDisplayCode" disabled/>
          </el-form-item>
          <el-form-item prop="direction" :label="$t('subjectBalanceDirection')">
            <el-radio-group :disabled="form.hasVoucher" v-model="form.direction">
              <el-radio value="0">{{ t('subjectDirectionNone') }}</el-radio>
              <el-radio value="1">{{ t('subjectDebit') }}</el-radio>
              <el-radio value="2">{{ t('subjectCredit') }}</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="auxiliary" :label="$t('subjectAuxiliary')">
            <el-select :disabled="form.hasVoucher" v-model="form.auxiliary" clearable multiple>
              <template #header>
                <div style="display: flex;justify-content: space-between;padding: 0 40px 0 10px">
                  <span>类型名称</span>
                  <span>是否必填</span>
                </div>
              </template>
              <el-option
                  v-for="dict in subjects_category_dicts"
                  :key="dict.id"
                  :label="dict.label"
                  value-key="value"
                  :value="dict">
                <span style="float: left">{{ dict.label }}</span>
                <el-switch @click.stop
                           style="float: right;color: var(--el-text-color-secondary);margin-right: 10px;"
                           v-model="dict.must" @change="handle_subjects_category_dicts(dict)"/>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item prop="classify" :label="t('subjectClassify')">
            <el-input v-model="form.classify"/>
          </el-form-item>
          <el-form-item prop="scope" :label="t('subjectScope')">
            <el-input v-model="form.scope"/>
          </el-form-item>
          <el-form-item prop="parentId" :label="$t('subjectParent')">
            <el-tree-select
                ref="resTreeRef"
                v-model="form.parentId"
                :data="deptOptions"
                :props="defaultProps"
                check-strictly
                value-key="id"
                show-checkbox
                clearable
            />
          </el-form-item>
          <el-form-item prop="isCash" label="是否为现金科目">
            <el-radio-group v-model="form.isCash" :disabled="form.hasVoucher">
              <el-radio :value="1">是</el-radio>
              <el-radio :value="0">否</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item prop="status" :label="$t('jbx.text.status.status')">
            <el-switch
                :width="44"
                v-model="form.status"
                :active-value="1"
                :inactive-value="0"
                active-icon-class="el-icon-close"
                inactive-icon-class="el-icon-check">
            </el-switch>
          </el-form-item>
        </el-form>

        <template #footer>
          <div class="dialog-footer">
            <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
            <el-button type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
          </div>
        </template>
      </el-drawer>
  </div>
</template>
<script setup lang="ts">
import modal from "@/plugins/modal";

import {ref, getCurrentInstance, reactive, toRefs, watch} from "vue";
import {useI18n} from "vue-i18n";
import {
  getTree,
} from "@/api/system/standard/standard-subject";
import {ElForm} from "element-plus";
import DictTagNumber from "@/components/DIctTagNumber/index.vue";
import bookStore from "@/store/modules/bookStore";
import {
  deleteBatch,
  getSetSubject,
  listSubjectsBySet,
  saveSubject,
  updateSubject,
  reorgDisplayName
} from "@/api/system/book/book-subject";
import {handleTree} from "@/utils/Surpass";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const currBookStore = bookStore()

const {subjects_category, subjects_auxiliary}
    = proxy?.useDict("subjects_category", "subjects_auxiliary");
let subjects_category_dicts = reactive(subjects_auxiliary.value.map((t: any) => {
  return {
    id: t.value + (Math.random() * 9999).toFixed(0),
    label: t.label,
    value: t.value,
  }
}))

const emit: any = defineEmits(['hatsDrawerClose'])

const formRef = ref<InstanceType<typeof ElForm> | null>(null);

const data: any = reactive({
  queryParams: {
    pageSize: 100000,
    pageNumber: 1,
    category: 1,
  },
  form: {
    direction: "1",
    status: 1,
    auxiliary: [],
    isCash: 0
  },
  rules: {
    name: [
      {required: true, message: t('subjectNameError'), trigger: "blur"},
    ],
    code: [
      {required: true, message: t('subjectCodeError'), trigger: "blur"},
    ],
    category: [
      {required: true, message: t('subjectCategoryError'), trigger: "blur"},
    ],
  }
})
const {queryParams, form, rules} = toRefs(data)
const dialogStatus: any = ref(false);
const subjectsList: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true);
const editFlag: any = ref(false);
const editTitle: any = ref('');
const ids: any = ref<any>([]);
const id: any = ref(undefined);
const selectionlist: any = ref<any>([]);
const deptOptions: any = ref<any[]>([]);
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
  disabled: disabledFilter
})

queryParams.value.bookId = currBookStore.bookId;

console.log(queryParams.value.bookId);

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('hatsDrawerClose', val);
}

function reset(): any {
  queryParams.value = {
    pageSize: 10000,
    pageNumber: 1,
    category: 1,
  };
  loading.value = true;
}

function getList(): any {
  listSubjectsBySet(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      subjectsList.value = handleTree(res.data.records, "id", "parentId", "children")
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function handleAdd(): any {
  updateDicts()
  id.value = undefined;
  form.value = {
    direction: "1",
    status: 1,
    bookId: currBookStore.bookId,
    auxiliary: [],
    isCash: 0
  };
  editTitle.value = "新增科目"
  editFlag.value = true;
}

function getOneSetSubject() {
  getSetSubject({id: id.value, bookId: currBookStore.bookId}).then((res: any) => {
    try {
      res.data.auxiliary = res.data.auxiliary ? JSON.parse(res.data.auxiliary) : [];
      res.data.auxiliary.forEach((t: any) => {
        subjects_category_dicts.forEach((v: any) => {
          if (v.value === t.value) {
            v.must = t.must
          }
        })
      })
    } catch (err) {
      res.data.auxiliary = []
    }

    form.value = res.data;
    form.value.bookId = currBookStore.bookId;
  })
}

function onDelete(row: any): any {
  modal.confirm("是否确认删除名称为\"" + row.name + "\"的会计科目?").then(function () {
    return deleteBatch({listIds: [row.id], bookId: currBookStore.bookId});
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
function handleRemove(): any {
  modal.confirm("是否确认删除这些数据？").then(function () {
    return deleteBatch({listIds: ids.value, bookId: currBookStore.bookId});
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

function cancel(): any {
  editFlag.value = false;
  resetForm();
}

function resetForm() {
  formRef?.value?.resetFields();
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/**
 * 查询
 */
function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

/**
 * 重置
 */
function resetQuery(): any {
  queryParams.value.code = undefined;
  queryParams.value.name = undefined;
  queryParams.value.category = 1;
  handleQuery();
}

function updateDicts() {
  subjects_category_dicts = reactive(subjects_auxiliary.value.map((t: any) => {
    return {
      id: t.value + (Math.random() * 9999).toFixed(0),
      label: t.label,
      value: t.value,
    }
  }))
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  children?: TreeNode[]; // 子节点可能不存在
}

function getSetTree() {
  getTree({bookId: currBookStore.bookId}).then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
    form.value.parentId = undefined;
  });
}

function disabledFilter(data: any, node: any): any {
  if (!currBookStore.bookId) {
    return false;
  }
  return new RegExp(`/${currBookStore.bookId}(/|$)`).test(data.idPath)
}

function handleUpdate(row: any): any {
  id.value = row.id;
  updateDicts()
  getOneSetSubject();
  editTitle.value = "修改会计科目"
  editFlag.value = true;
}

function submitForm(): any {
  const handleResponse: any = (res: any, successMessage: any) => {
    if (res.code === 0) {
      getList();

      proxy?.$modal.msgSuccess(successMessage);
      editFlag.value = false;
    } else {
      proxy?.$modal.msgError(res.message);
    }
  };

  formRef?.value?.validate((valid: any) => {
    if (valid) {
      const operation: any = id.value ? updateSubject : saveSubject;
      const successMessage: any = currBookStore.bookId
          ? t('org.success.update')
          : t('org.success.add');
      const formData = {...form.value, bookId: currBookStore.bookId}
      formData.auxiliary = formData.auxiliary && formData.auxiliary instanceof Array
          ? JSON.stringify(formData.auxiliary) : '[]';
      operation(formData).then((res: any) => handleResponse(res, successMessage));
    }
  });
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

function handle_subjects_category_dicts(item: any) {
  const auxiliary = form.value.auxiliary || []
  let find = false
  auxiliary.forEach((t: any) => {
    if (t.value === item.value) {
      t.must = item.must
      find = true
    }
  })
  if (!find) {
    auxiliary.push(item)
  }
}

getList();
getSetTree();

</script>

<style lang="scss" scoped>
.button-top {
  margin-bottom: 10px;
}
</style>
