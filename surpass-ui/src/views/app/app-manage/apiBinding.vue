<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="70%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>API绑定 - {{ appName }}</h4>
    </template>
    <template #default>
      <div class="api-bind-container">
        <el-card class="search-card">
          <el-form :inline="true" :model="queryParams">
            <el-form-item>
              <el-input
                  v-model="queryParams.apiName"
                  placeholder="API名称"
                  clearable
                  style="width: 200px"
                  @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-input
                  v-model="queryParams.apiPath"
                  placeholder="API路径"
                  clearable
                  style="width: 200px"
                  @keyup.enter="handleQuery"
              />
            </el-form-item>
            <el-form-item>
              <el-select
                  v-model="queryParams.method"
                  placeholder="请求方法"
                  clearable
                  style="width: 150px"
              >
                <el-option label="GET" value="GET"/>
                <el-option label="POST" value="POST"/>
                <el-option label="PUT" value="PUT"/>
                <el-option label="DELETE" value="DELETE"/>
                <el-option label="PATCH" value="PATCH"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select
                  v-model="queryParams.bound"
                  placeholder="绑定状态"
                  clearable
                  style="width: 150px"
              >
                <el-option label="已绑定" :value="1"/>
                <el-option label="未绑定" :value="0"/>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
              <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="api-list-card">
          <div class="stats-info">
            <span>已绑定API数: <strong style="color: #409eff">{{ boundCount }}</strong></span>
          </div>

          <el-table
              v-loading="loading"
              :data="apiList"
              border
              max-height="500"
          >
            <el-table-column prop="name" label="API名称" align="center" min-width="150"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="path" label="API路径" align="center" min-width="200"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="method" label="请求方法" align="center" width="100">
              <template #default="scope">
                <el-tag :type="getMethodTagType(scope.row.method)">
                  {{ scope.row.method }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="description" label="描述" align="center" min-width="200"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column label="绑定状态" align="center" width="100">
              <template #default="scope">
                <el-tag v-if="scope.row.bound" type="success">已绑定</el-tag>
                <el-tag v-else type="info">未绑定</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" width="120">
              <template #default="scope">
                <el-switch
                    v-model="scope.row.bound"
                    @change="handleBindChange(scope.row)"
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
        <el-button type="primary" @click="submitBind" :loading="saveLoading">保存</el-button>
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
import {getAppApis, saveAppApis} from "@/api/api-service/apps";
import {list as getApiList} from "@/api/api-service/apiDefinitionApi";
import {useI18n} from "vue-i18n";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;

const props: any = defineProps({
  open: Boolean,
  appId: {
    default: undefined
  },
  appName: {
    type: String,
    default: ""
  }
});

const emit: any = defineEmits(['dialogOfClosedMethods']);

const dialogStatus: any = ref(false);
const loading: any = ref(false);
const saveLoading: any = ref(false);
const apiList: any = ref<any>([]);
const total: any = ref(0);
const boundCount: any = ref(0);

// 存储所有已绑定的API ID（包括当前页和其他页的）
const boundApiIds: any = ref<Set<any>>(new Set());

// 存储本次操作中修改的绑定状态
const changedBindings: any = ref<Map<any, boolean>>(new Map());

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50],
    apiName: undefined,
    apiPath: undefined,
    method: undefined,
    bound: undefined
  }
});

const {queryParams} = toRefs(data);

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val) {
        dialogStatus.value = props.open;
        if (props.appId) {
          loadBoundApis();
        }
      } else {
        reset();
      }
    },
    {immediate: true}
);

/**
 * 加载已绑定的API ID列表
 */
function loadBoundApis(): any {
  loading.value = true;
  getAppApis(props.appId).then((res: any) => {
    if (res.code === 0) {
      // 将已绑定的API ID存入Set
      boundApiIds.value = new Set((res.data || []).map((item: any) => item.apiId));
      boundCount.value = boundApiIds.value.size;
      // 加载API列表
      getList();
    } else {
      loading.value = false;
      proxy?.$modal.msgError("加载绑定信息失败");
    }
  }).catch(() => {
    loading.value = false;
    proxy?.$modal.msgError("加载绑定信息失败");
  });
}

/**
 * 获取API列表（分页）
 */
function getList(): any {
  loading.value = true;
  getApiList(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      const apis = res.data || [];
      total.value = apis.length;

      // 为每个API添加绑定状态
      apiList.value = apis.map((api: any) => {
        const apiId = api.id;
        // 如果有修改记录，使用修改后的状态，否则使用原始绑定状态
        const bound = changedBindings.value.has(apiId)
            ? changedBindings.value.get(apiId)
            : boundApiIds.value.has(apiId);

        return {
          ...api,
          bound: bound
        };
      });

      // 根据绑定状态筛选
      if (queryParams.value.bound !== undefined) {
        apiList.value = apiList.value.filter((api: any) => {
          return queryParams.value.bound === 1 ? api.bound : !api.bound;
        });
      }

      loading.value = false;
    } else {
      loading.value = false;
      proxy?.$modal.msgError("加载API列表失败");
    }
  }).catch(() => {
    loading.value = false;
    proxy?.$modal.msgError("加载API列表失败");
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
  queryParams.value.apiName = undefined;
  queryParams.value.apiPath = undefined;
  queryParams.value.method = undefined;
  queryParams.value.bound = undefined;
  handleQuery();
}

/**
 * 绑定状态变化
 */
function handleBindChange(row: any): any {
  const apiId = row.id;
  const newStatus = row.bound;

  // 记录修改
  changedBindings.value.set(apiId, newStatus);

  // 更新已绑定数量统计
  if (newStatus) {
    boundApiIds.value.add(apiId);
  } else {
    boundApiIds.value.delete(apiId);
  }
  boundCount.value = boundApiIds.value.size;
}

/**
 * 获取方法标签类型
 */
function getMethodTagType(method: string): string {
  const types: any = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  };
  return types[method] || 'info';
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
    apiName: undefined,
    apiPath: undefined,
    method: undefined,
    bound: undefined
  };
  apiList.value = [];
  boundApiIds.value = new Set();
  changedBindings.value = new Map();
  boundCount.value = 0;
  total.value = 0;
}

/** 提交绑定 */
function submitBind(): any {
  saveLoading.value = true;

  // 获取最终的绑定API ID列表
  const finalBoundIds = Array.from(boundApiIds.value);

  saveAppApis({
    appId: props.appId,
    apiIds: finalBoundIds
  }).then((res: any) => {
    saveLoading.value = false;
    if (res.code === 0) {
      proxy?.$modal.msgSuccess("绑定保存成功");
      // 清空修改记录
      changedBindings.value.clear();
      dialogOfClosedMethods(true);
    } else {
      proxy?.$modal.msgError(res.message || "绑定保存失败");
    }
  }).catch(() => {
    saveLoading.value = false;
    proxy?.$modal.msgError("绑定保存失败");
  });
}
</script>

<style lang="scss" scoped>
.api-bind-container {
  .search-card {
    margin-bottom: 15px;
  }

  .api-list-card {
    .stats-info {
      margin-bottom: 15px;
      padding: 10px;
      background-color: #f5f7fa;
      border-radius: 4px;

      span {
        font-size: 14px;
        color: #606266;
      }
    }
  }
}
</style>
