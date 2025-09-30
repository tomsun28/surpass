<template>
  <el-dialog
      v-model="dialogShow"
      :title="t('jbx.text.select')"
      width="65%"
  >
    <div class="app-container1" style="margin-top: -10px">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="t('jbx.apps.appName')" prop="appName">
          <el-input
              v-model="queryParams.appName"
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{t('jbx.text.query')}}</el-button>
          <el-button @click="resetQuery">{{t('jbx.text.reset')}}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
              :disabled="curApps === undefined"
              @click="change"
              type="primary"
          >{{t('jbx.text.confirm')}}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              @click="handleClose"
          >{{t('jbx.text.close')}}</el-button>
        </el-col>
      </el-row>
      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="list"  ref="appsTable" @select="handleSelectionChangeApps">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column :label="t('jbx.apps.appName')" prop="appName" :show-overflow-tooltip="true" />
        <el-table-column :label="t('jbx.apps.protocol')" prop="protocol" :show-overflow-tooltip="true" />
      </el-table>
      <pagination
          style="margin-right: 10px"
          v-show="total > 0"
          :total="total"
          layout="prev, pager, next, total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </div>
  </el-dialog>
</template>

<script setup name="common-select-apps" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent, watchEffect} from "vue";
import modal from "@/plugins/modal";

import {listApps} from "@/api/apps";

import { useI18n } from 'vue-i18n'


const { t } = useI18n()

const router: any = useRouter();

const {proxy} = getCurrentInstance()!;

//定义父组件方法
const emit: any = defineEmits(["changeApps"]);

const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const dialogShow: any = ref(false)
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");
const curApps: any = ref(undefined);
const data: any = reactive({
  form: {},
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    appName: undefined
  }
});
const { queryParams, form, rules } = toRefs(data);



function openApps(): any {
  dialogShow.value = true;
  curApps.value = undefined;
  resetQuery()
}


/** 查询分组列表 */
function getList(): any {
  loading.value = true;
  listApps(queryParams.value).then((res: any) =>  {
    loading.value = false;
    list.value = res.data.records;
    total.value = res.data.total;
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

function handleClose(): any {
  dialogShow.value = false;
  queryParams.value.pageNumber = 1;
  queryRef?.value?.resetFields();;
}


/**
 * 账号单选
 */
function handleSelectionChangeApps(selection: any, row: any): any {
  proxy.$refs.appsTable.clearSelection()
  curApps.value = undefined;
  if (row) {
    proxy.$refs.appsTable.toggleRowSelection(row, true)
    curApps.value = row;
  }
}


function change(): any {
  //通知父组件
  emit('changeApps',curApps.value.id,curApps.value.appName);
  handleClose()
}

defineExpose({
  openApps
})

</script>
