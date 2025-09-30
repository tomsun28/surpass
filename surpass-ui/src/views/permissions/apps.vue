<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="$t('jbx.apps.name') + '：'" prop="appName">
          <el-input
              v-model="queryParams.appName"
              placeholder=""
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}
          </el-button><!-- type="primary" -->
          <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="common-card">
      <el-table v-loading="loading" :data="list">
        <el-table-column :label="$t('jbx.apps.icon')" align="center" prop="icon" width="120">
          <template #default="scope">
            <img height="30" border="0px" :src="scope.row.imageUrl" />
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.apps.name')" align="center" prop="appName"/>
        <el-table-column :label="$t('jbx.emailsenders.protocol')" align="center" prop="protocol" />
        <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex" width="120"/>
        <el-table-column :label="$t('jbx.users.status')" align="center" prop="status" width="120">
          <template #default="scope">
            <span v-if="scope.row.status == 1"><el-icon color="green"><SuccessFilled class="success" /></el-icon></span>
            <span v-if="scope.row.status == 0"><el-icon color="#808080"><CircleCloseFilled /></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center"
                         class-name="small-padding fixed-width" width="350" >
          <template #default="scope">
            <el-button @click="onPermission(scope.row)" >{{t('jbx.apps.permission')}}</el-button>
            <el-button @click="onRolesMgmt(scope.row)" >{{t('jbx.apps.roles')}}</el-button>
            <el-button @click="onResourcesMgmt(scope.row)" >{{t('jbx.apps.resources')}}</el-button>
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
  </div>
</template>

<script setup name="Permissions-Apps" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  listApps,
} from "@/api/apps";

import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const { t } = useI18n()
const router: any = useRouter();

const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    appName: undefined
  }
});

const { queryParams } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  listApps(queryParams.value).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.records;
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
  queryRef?.value?.resetFields();;
  handleQuery();
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  reset();
  const id: any = row.id || ids.value;
  getAdapters(id).then((res: any) =>  {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
};



function onResourcesMgmt(row: any): any {
  const appId: any = row.id
  const appName: any = row.appName
  let otherQueryParams: any = {
    appId: appId,
    appName: appName
  }
  router.push({path: "/permissions/apps/resources", query: otherQueryParams});
};

function onRolesMgmt(row: any): any {
  const appId: any = row.id
  const appName: any = row.appName
  let otherQueryParams: any = {
    appId: appId,
    appName: appName
  }
  router.push({path: "/permissions/apps/roles", query: otherQueryParams});
};

function onPermission(row: any): any {
  const appId: any = row.id
  const appName: any = row.appName
  let otherQueryParams: any = {
    appId: appId,
    appName: appName
  }
  router.push({path: "/permissions/apps/permission", query: otherQueryParams});
};


getList();

</script>
<style scoped>
::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}
.common-card{
  margin-bottom: 15px;
}
::v-deep(.common-card form .el-form-item--default){
  margin-bottom: 0px;
}
</style>
