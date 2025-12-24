<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true"
                 @submit.native.prevent>
          <el-form-item label="应用编码">
            <el-input
                v-model="queryParams.appCode"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="应用名称">
            <el-input
                v-model="queryParams.appName"
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
          border
          v-loading="loading"
          :data="appList"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="appCode" label="应用编码" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="appName" label="应用名称" align="center" min-width="120"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="contextPath" label="上下文路径" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="loginUrl" label="登录地址" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="status" :label="t('org.status')" align="center" width="80">
          <template #default="scope">
            <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                class="success"/></el-icon></span>
            <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center" width="200" fixed="right">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="资源授权">
              <el-button link icon="Connection" type="primary" @click="handleApiBinding(scope.row)"></el-button>
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
    <!--新增或修改对话框-->
    <appEdit :title="title" :open="open" :formId="id" @dialogOfClosedMethods="dialogOfClosedMethods"></appEdit>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs} from "vue";
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {deleteBatch, list} from "@/api/api-service/apps";
import {set2String} from "@/utils"
import appEdit from "./edit.vue";
import {useRouter} from 'vue-router'

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const router: any = useRouter(); // 获取路由实例

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);
const appList: any = ref<any>([]);
const open: any = ref(false);
const apiBindOpen: any = ref(false);
const loading: any = ref(true);
const title: any = ref("");
const id: any = ref(undefined);
const total: any = ref(0);
const ids: any = ref<any>([]);
const selectionlist: any = ref<any>([]);

/**
 * 获取列表
 */
function getList(): any {
  list(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      appList.value = res.data.rows;
      total.value = res.data.records;
    }
  })
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
  queryParams.value.appName = undefined;
  queryParams.value.appCode = undefined;
  handleQuery();
}

function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function apiBindDialogOfClosedMethods(val: any): any {
  apiBindOpen.value = false;
}

function handleAdd(): any {
  id.value = undefined;
  title.value = t('jbx.text.add')
  open.value = true;
}

function handleUpdate(row: any): any {
  id.value = row.id;
  title.value = t('jbx.text.edit')
  open.value = true;
}


const handleApiBinding = (row: any): any => {
  // 确保 router 实例存在并且是有效的
  if (router && typeof router.push === 'function') {
    router.push(`/app/app-manage/apiBinding?appId=${row.id}&appName=${row.appName}`);
  } else {
    console.error('Router instance is not available.');
  }
}


/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds: any = set2String(ids.value);
    return deleteBatch(setIds);
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.appName + t('org.deleteTip2')).then(function () {
    return deleteBatch(row.id);
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
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
