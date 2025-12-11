<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="60%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>应用授权 - {{ clientName }}</h4>
    </template>
    <template #default>
      <div class="auth-container">
        <el-card class="search-card">
          <el-form :inline="true">
            <el-form-item>
              <el-input
                  v-model="queryParams.appName"
                  placeholder="应用名称"
                  clearable
                  style="width: 200px"
                  @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                  v-model="queryParams.appCode"
                  placeholder="应用编码"
                  clearable
                  style="width: 200px"
                  @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                  v-model="queryParams.authorized"
                  placeholder="授权状态"
                  clearable
                  style="width: 150px"
              >
                <el-option label="已授权" :value="1"/>
                <el-option label="未授权" :value="0"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">查询</el-button>
              <el-button @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="app-list-card">
          <div class="stats-info">
            <span>已授权应用数: {{ authorizedCount }}</span>
          </div>

          <el-table
              v-loading="loading"
              :data="appList"
              border
              max-height="500"
          >
            <el-table-column prop="appCode" label="应用编码" align="center" min-width="120"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="appName" label="应用名称" align="center" min-width="150"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="授权状态" align="center" width="100">
              <template #default="scope">
                <el-tag v-if="scope.row.authorized" type="success">已授权</el-tag>
                <el-tag v-else type="info">未授权</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="授权" align="center" width="120">
              <template #default="scope">
                <el-switch
                    v-model="scope.row.authorized"
                    @change="handleAuthChange(scope.row)"
                />
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
      </div>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitAuth" :loading="saveLoading">保存</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup lang="ts">
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch
} from "vue";
import {getClientApps, saveClientApps} from "@/api/api-service/client";
import {list as getAppList} from "@/api/api-service/apps";
import {useI18n} from "vue-i18n";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;

const props: any = defineProps({
  open: Boolean,
  clientId: {
    default: undefined
  },
  clientName: {
    type: String,
    default: ""
  }
});

const emit: any = defineEmits(['dialogOfClosedMethods']);

const dialogStatus: any = ref(false);
const loading: any = ref(false);
const saveLoading: any = ref(false);
const appList: any = ref<any>([]);
const total: any = ref(0);
const authorizedCount: any = ref(0);

// 存储所有已授权的应用ID（包括当前页和其他页的）
const authorizedAppIds: any = ref<Set<any>>(new Set());

// 存储本次操作中修改的授权状态
const changedAuthorizations: any = ref<Map<any, boolean>>(new Map());

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    appName: undefined,
    appCode: undefined,
    authorized: undefined
  }
});

const {queryParams} = toRefs(data);

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.clientId) {
          loadAuthorizedApps();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

/**
 * 加载已授权的应用ID列表
 */
function loadAuthorizedApps(): any {
  loading.value = true;
  getClientApps(props.clientId).then((res: any) => {
    if (res.code === 0) {
      // 将已授权的应用ID存入Set
      authorizedAppIds.value = new Set((res.data || []).map((item: any) => item.appId));
      authorizedCount.value = authorizedAppIds.value.size;
      // 加载应用列表
      getList();
    } else {
      loading.value = false;
      proxy?.$modal.msgError("加载授权信息失败");
    }
  }).catch(() => {
    loading.value = false;
    proxy?.$modal.msgError("加载授权信息失败");
  });
}

/**
 * 获取应用列表（分页）
 */
function getList(): any {
  loading.value = true;
  getAppList(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      const apps = res.data.rows || [];
      total.value = res.data.records;

      // 为每个应用添加授权状态
      appList.value = apps.map((app: any) => {
        const appId = app.id;
        // 如果有修改记录，使用修改后的状态，否则使用原始授权状态
        const authorized = changedAuthorizations.value.has(appId)
            ? changedAuthorizations.value.get(appId)
            : authorizedAppIds.value.has(appId);

        return {
          ...app,
          authorized: authorized
        };
      });

      // 根据授权状态筛选
      if (queryParams.value.authorized !== undefined) {
        appList.value = appList.value.filter((app: any) => {
          return queryParams.value.authorized === 1 ? app.authorized : !app.authorized;
        });
      }

      loading.value = false;
    } else {
      loading.value = false;
      proxy?.$modal.msgError("加载应用列表失败");
    }
  }).catch(() => {
    loading.value = false;
    proxy?.$modal.msgError("加载应用列表失败");
  });
}

/**
 * 查询
 */
function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

/**
 * 重置查询
 */
function resetQuery(): any {
  queryParams.value.appName = undefined;
  queryParams.value.appCode = undefined;
  queryParams.value.authorized = undefined;
  handleQuery();
}

/**
 * 授权状态变化
 */
function handleAuthChange(row: any): any {
  const appId = row.id;
  const newStatus = row.authorized;

  // 记录修改
  changedAuthorizations.value.set(appId, newStatus);

  // 更新已授权数量统计
  if (newStatus) {
    authorizedAppIds.value.add(appId);
  } else {
    authorizedAppIds.value.delete(appId);
  }
  authorizedCount.value = authorizedAppIds.value.size;
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

/** 重置 */
function reset(): any {
  queryParams.value = {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    appName: undefined,
    appCode: undefined,
    authorized: undefined
  };
  appList.value = [];
  authorizedAppIds.value = new Set();
  changedAuthorizations.value = new Map();
  authorizedCount.value = 0;
  total.value = 0;
}

/** 提交授权 */
function submitAuth(): any {
  saveLoading.value = true;

  // 获取最终的授权应用ID列表
  const finalAuthorizedIds = Array.from(authorizedAppIds.value);

  saveClientApps({
    clientId: props.clientId,
    appIds: finalAuthorizedIds
  }).then((res: any) => {
    saveLoading.value = false;
    if (res.code === 0) {
      proxy?.$modal.msgSuccess("授权保存成功");
      // 清空修改记录
      changedAuthorizations.value.clear();
      dialogOfClosedMethods(true);
    } else {
      proxy?.$modal.msgError(res.message || "授权保存失败");
    }
  }).catch(() => {
    saveLoading.value = false;
    proxy?.$modal.msgError("授权保存失败");
  });
}
</script>

<style lang="scss" scoped>
.auth-container {
  .search-card {
    margin-bottom: 15px;
  }

  .app-list-card {
    .stats-info {
      margin-bottom: 15px;
      padding: 10px;
      background-color: #f5f7fa;
      border-radius: 4px;

      span {
        font-size: 14px;
        color: #606266;
        font-weight: bold;
      }
    }
  }
}
</style>
