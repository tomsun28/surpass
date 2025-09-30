<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true">
          <el-form-item :label="t('accountingStandardName') + '：'" prop="name">
            <el-input
                v-model="queryParams.name"
                placeholder=""
                clearable
                @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
            <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
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
      <el-table v-loading="loading" :data="standardList"
                border
                @selection-change="handleSelectionChange"
                highlight-current-row>
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="t('accountingStandardName')" prop="name" align="center" min-width="100"
                         :show-overflow-tooltip="true"/>
        <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
          <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="t('jbx.text.action')" align="center" width="80">
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
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
  </div>
</template>

<script setup lang="ts">
import {useI18n} from "vue-i18n";
import {getCurrentInstance, reactive, ref, toRefs} from "vue";
import {
  deleteBatch,
  listStandards
} from "@/api/system/standard/standard";
import modal from "@/plugins/modal";
import editForm from "./standard/edit.vue";
import SvgIcon from "@/components/SvgIcon/index.vue";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const {subjects_category, subjects_auxiliary}
    = proxy?.useDict("subjects_category", "subjects_auxiliary");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);

const standardList: any = ref<any>([]);
const loading: any = ref(true);
const ids: any = ref<any>([]);
const selectionlist: any = ref<any>([]);
const total: any = ref(0);
const id: any = ref(undefined);
const title: any = ref("");
const open: any = ref(false);
const openSubject: any = ref(false);
//树型规则，false=遵循父子规则，true=不遵循
const deptOptions: any = ref<any[]>([]);


/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 分页列表 */
function getList(): any {
  loading.value = true;
  listStandards(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      standardList.value = res.data.records;
      total.value = res.data.total;
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
  queryParams.value.name = undefined;
  handleQuery();
}

function handleAdd(): any {
  id.value = undefined;
  title.value = t('jbx.text.add')
  open.value = true;
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  id.value = row.id;
  title.value = t('org.titleEdit');
  open.value = true;
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
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

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  openSubject.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
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

getList();
</script>

<style lang="scss" scoped>

::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

.auths {
  display: flex;
}

.auths .cleft {
  width: 50%;
}

.md {
  color: #ccc;
  font-size: 12px;
  margin-left: 20px
}

</style>
