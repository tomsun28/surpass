<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="60%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>应用授权 - {{ clientName }}</h4>
    </template>
    <template #default>
      <div class="auth-container">
        <el-card class="search-card">
          <el-input
              v-model="searchKeyword"
              placeholder="搜索应用名称或编码"
              clearable
              style="width: 300px"
          >
            <template #prefix>
              <el-icon><Search /></el-icon>
            </template>
          </el-input>
        </el-card>

        <el-card class="app-list-card">
          <div class="stats-info">
            <span>全部应用: {{ totalApps }}</span>
            <span class="ml-20">已授权: {{ authorizedCount }}</span>
            <span class="ml-20">未授权: {{ totalApps - authorizedCount }}</span>
          </div>

          <el-table
              v-loading="loading"
              :data="filteredAppList"
              border
              max-height="500"
          >
            <el-table-column type="selection" width="55" align="center"
                             :selectable="checkSelectable"
                             @selection-change="handleSelectionChange"/>
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
            <el-table-column label="操作" align="center" width="120">
              <template #default="scope">
                <el-switch
                    v-model="scope.row.authorized"
                    @change="handleAuthChange(scope.row)"
                    active-text="授权"
                    inactive-text="取消"
                />
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </div>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="dialogOfClosedMethods(false)">{{ t('org.cancel') }}</el-button>
        <el-button type="primary" @click="submitAuth">{{ t('org.confirm') }}</el-button>
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
  watch,
  computed
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
const searchKeyword: any = ref("");
const appList: any = ref<any>([]);
const authorizedAppIds: any = ref<any>([]);

// 计算属性
const totalApps = computed(() => appList.value.length);
const authorizedCount = computed(() => appList.value.filter((app: any) => app.authorized).length);

const filteredAppList = computed(() => {
  if (!searchKeyword.value) {
    return appList.value;
  }
  return appList.value.filter((app: any) => {
    return app.appName.includes(searchKeyword.value) ||
        app.appCode.includes(searchKeyword.value);
  });
});

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.clientId) {
          loadData();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

/**
 * 加载数据
 */
function loadData(): any {
  loading.value = true;
  // 并行加载应用列表和已授权的应用
  Promise.all([
    getAppList({pageNumber: 1, pageSize: 1000}),
    getClientApps(props.clientId)
  ]).then((results: any) => {
    const [appsRes, authRes] = results;

    if (appsRes.code === 0) {
      const apps = appsRes.data.rows || [];
      // 获取已授权的应用ID列表
      const authorizedIds = authRes.code === 0 ? (authRes.data || []).map((item: any) => item.appId) : [];

      // 为每个应用添加授权状态
      appList.value = apps.map((app: any) => ({
        ...app,
        authorized: authorizedIds.includes(app.id)
      }));
    }
    loading.value = false;
  }).catch(() => {
    loading.value = false;
    proxy?.$modal.msgError("加载数据失败");
  });
}

/**
 * 授权状态变化
 */
function handleAuthChange(row: any): any {
  // 可以在这里添加即时保存逻辑，或者等待最后统一保存
}

/**
 * 检查是否可选
 */
function checkSelectable(row: any): any {
  return true;
}

/**
 * 多选变化
 */
function handleSelectionChange(selection: any): any {
  // 处理多选逻辑
}

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('dialogOfClosedMethods', val);
}

/** 重置 */
function reset(): any {
  searchKeyword.value = "";
  appList.value = [];
  authorizedAppIds.value = [];
}

/** 提交授权 */
function submitAuth(): any {
  loading.value = true;
  // 获取所有已授权的应用ID
  const authorizedIds = appList.value
      .filter((app: any) => app.authorized)
      .map((app: any) => app.id);

  saveClientApps({
    clientId: props.clientId,
    appIds: authorizedIds
  }).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      proxy?.$modal.msgSuccess("授权保存成功");
      dialogOfClosedMethods(true);
    } else {
      proxy?.$modal.msgError(res.message || "授权保存失败");
    }
  }).catch(() => {
    loading.value = false;
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

        &.ml-20 {
          margin-left: 20px;
        }
      }
    }
  }
}
</style>
