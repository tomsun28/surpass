<template>
  <el-drawer
      v-model="drawer"
      :title="t('sync.common.button.logs')"
      size="80%"
  >
    <div class="app-container1" style="margin-top: -10px">
      <el-form :model="queryParams" ref="queryRef"  :inline="true">
        <el-form-item :label="t('sync.logs.label.trackingUniqueId')" prop="trackingUniqueId">
          <el-input
              v-model="queryParams.trackingUniqueId"
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="t('sync.logs.label.syncType')" prop="syncType">
          <el-select
              v-model="queryParams.syncType"
              clearable
              style="width: 240px"
          >
            <el-option
                v-for="dict in sys_sync_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('sync.logs.label.syncActionType')" prop="syncActionType">
          <el-select
              v-model="queryParams.syncActionType"
              clearable
              style="width: 240px"
          >
            <el-option
                v-for="dict in sys_sync_action_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="t('sync.logs.label.syncResultStatus')" prop="syncResultStatus">
          <el-select
              v-model="queryParams.syncResultStatus"
              clearable
              style="width: 240px"
          >
            <el-option
                v-for="dict in sys_sync_result_status"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{t('text.query')}}</el-button>
          <el-button @click="resetQuery">{{t('text.reset')}}</el-button>
        </el-form-item>
      </el-form>
      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="list" >
        <el-table-column :label="t('sync.logs.label.id')" prop="id" :show-overflow-tooltip="true" width="250" />
        <el-table-column :label="t('sync.logs.label.trackingUniqueId')" prop="trackingUniqueId" :show-overflow-tooltip="true" width="250" />
        <el-table-column :label="t('sync.logs.label.syncData')" prop="syncData" :show-overflow-tooltip="true" />
        <el-table-column :label="t('sync.logs.label.syncType')" prop="syncType" :show-overflow-tooltip="true" width="100" >
          <template #default="scope">
            <dict-tag :options="sys_sync_type" :value="scope.row.syncType" />
          </template>
        </el-table-column>
        <el-table-column :label="t('sync.logs.label.syncActionType')" prop="syncActionType" :show-overflow-tooltip="true" width="100" >
          <template #default="scope">
            <dict-tag :options="sys_sync_action_type" :value="scope.row.syncActionType" />
          </template>
        </el-table-column>
        <el-table-column :label="t('sync.logs.label.syncResultStatus')" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.syncResultStatus === 0"><el-icon color="green"><SuccessFilled class="success" /></el-icon></span>
            <span v-if="scope.row.syncResultStatus === 1"><el-icon color="#808080"><CircleCloseFilled /></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="t('sync.logs.label.createTime')" prop="createTime"  width="180" :show-overflow-tooltip="true" />
        <el-table-column :label="t('sync.logs.label.syncMessage')" prop="syncMessage" :show-overflow-tooltip="true" />

      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>
  </el-drawer>
</template>

<script setup name="sync-logs" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import {logsFetch} from "@/api/system/synchronizers";

import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const router: any = useRouter();

const {proxy} = getCurrentInstance()!;

const notAuthUserInfoRef: any = ref(undefined);

const { sys_normal_disable ,sys_sync_action_type,sys_sync_result_status,sys_sync_type} = proxy.useDict(
    "sys_normal_disable","sys_sync_action_type",'sys_sync_result_status','sys_sync_type');

const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);

const drawer: any = ref(false)
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

const data: any = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    synchronizerId: undefined,
    trackingUniqueId: undefined,
    syncType: undefined,
    syncResultStatus: undefined,
    syncActionType: undefined
  }
});
const { queryParams, form, rules } = toRefs(data);

function openLogs(synchronizerId: any): any {
  queryParams.value.synchronizerId = synchronizerId;
  drawer.value = true;
  list.value = [];
  total.value = 0;
  getList()
}

/** 查询分组列表 */
function getList(): any {
  loading.value = true;
  logsFetch(queryParams.value).then((res: any) =>  {
    loading.value = false;
    if (res.success) {
      list.value = res.data.records;
      total.value = res.data.total;
    }
  });
}

/** 搜索按钮操作 */
function handleQuery(): any {
  queryParams.value.pageNum = 1;
  getList();
}



/** 重置按钮操作 */
function resetQuery(): any {
  queryRef?.value?.resetFields();;
  handleQuery();
}

defineExpose({
  openLogs
})


</script>
