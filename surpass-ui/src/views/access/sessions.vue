<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="$t('jbx.users.username')" prop="username">
          <el-input
              v-model="queryParams.username"
              placeholder=""
              clearable
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="$t('jbx.users.displayName')" prop="displayName">
          <el-input
              v-model="queryParams.displayName"
              placeholder=""
              clearable
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{ $t('jbx.text.query') }}
          </el-button><!-- type="primary" -->
          <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="danger"
            plain

            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('jbx.text.delete') }}
        </el-button><!-- icon="el-icon-delete" -->
      </div>

      <el-table v-loading="loading" border :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="sessionId" :label="$t('jbx.history.loginSessionid')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="120"/>
        <el-table-column prop="username" :label="$t('jbx.history.loginUsername')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="90"/>
        <el-table-column prop="displayName" :label="$t('jbx.history.loginDisplayname')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="90"/>
        <el-table-column prop="loginType" :label="$t('jbx.history.loginLogintype')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="90"/>
        <el-table-column prop="ipAddr" :label="$t('jbx.history.loginSourceip')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="90"/>
        <el-table-column prop="browser" :label="$t('jbx.history.loginBrowser')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="90"/>
        <el-table-column prop="platform" :label="$t('jbx.history.loginPlatform')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="120"/>
        <el-table-column prop="message" :label="$t('jbx.history.loginMessage')" align="center"
                         min-width="50"
                         :show-overflow-tooltip="true"
        />
        <el-table-column prop="operateTime" :label="$t('jbx.history.loginLogintime')" align="center"
                         :show-overflow-tooltip="true"
                         min-width="120"/>
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

<script setup name="Access-sessions" lang="ts">
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  apiSessionList,
  apiDelSession
} from "@/api/access/sessions";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const {t} = useI18n()

const list: any = ref<any>([]);
const loading: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    displayName: undefined,
    username: undefined
  },
});

const {queryParams} = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  apiSessionList(queryParams.value).then((res: any) => {
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
  queryRef?.value?.resetFields();
  ;
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const sessionId: any = row.sessionId || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return apiDelSession(sessionId);
  }).then(() => {
    getList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {
  });
}


/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) => item.sessionId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

getList();

</script>

