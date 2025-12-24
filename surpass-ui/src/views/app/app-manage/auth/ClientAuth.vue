<template>
  <el-card class="common-card query-box" style="height: 100%">
    <el-row :gutter="20">
      <el-col :span="10">
        <div class="page-header">
          <h3>客户端</h3>
<!--          <p>管理数据库连接配置，支持多种数据库类型</p>-->
        </div>
        <el-table
            border
            v-loading="loading"
            :data="clientList"
            highlight-current-row
            row-key="clientId"
            @row-click="handleRowClick"
        >
          <el-table-column
              width="50"
              class-name="radio-column"
          >
            <template #default="{ row }">
              <el-radio
                  v-model="selectedClientId"
                  :value="row.clientId"
              />
            </template>
          </el-table-column>

          <el-table-column
              prop="clientName"
              label="客户端"
              align="center"
              min-width="120"
              :show-overflow-tooltip="true"
          />
          <el-table-column
              prop="clientId"
              label="客户端ID"
              align="center"
              min-width="140"
              :show-overflow-tooltip="true"
          />
        </el-table>
        <pagination
            v-show="total > 0"
            :total="total"
            v-model:page="queryParams.pageNumber"
            v-model:limit="queryParams.pageSize"
            :page-sizes="queryParams.pageSizeOptions"
            @pagination="getList"
        />
      </el-col>
      <el-col :span="14">
        <el-button
            type="primary"
            @click="submitForm"
            :disabled="currentRow == undefined"
        >{{ $t('jbx.text.save') }}
        </el-button>
        <div class="ch">
           <span>
            <el-radio-group v-model="checkStrictly">
              <el-radio :value="false" size="large">{{ $t('jbx.message.tree.strictly.yes') }}</el-radio>
              <el-radio :value="true" size="large">{{ $t('jbx.message.tree.strictly.no') }}</el-radio>
            </el-radio-group>
             <span class="md">{{ $t('jbx.message.tree.strictly.md') }}</span>
           </span>
        </div>
        <el-tree
            ref="resTreeRef"
            node-key="id"
            :check-strictly="checkStrictly"
            :data="dataOptions"
            :props="defaultProps"
            :default-expanded-keys="treeData"
            :default-checked-keys="cheackdData"
            :expand-on-click-node="false"
            highlight-current
            show-checkbox
            v-slot="{ node, data }"
        >
            <span class="tree-node">
    <!-- 左侧名称 -->
    <span class="tree-label">
      <span v-if="node.label.length <= 10">{{ node.label }}</span>
      <el-tooltip
          v-else
          effect="dark"
          :content="node.label"
          placement="right"
      >
        <span>{{ node.label.slice(0, 10) + '...' }}</span>
      </el-tooltip>
    </span>

              <!-- 右侧资源类型 -->
    <el-tag
        size="small"
        :type="resourceTagType(data.classify)"
        class="tree-tag"
    >
      {{ resourceLabel(data.classify) }}
    </el-tag>
  </span>
        </el-tree>
      </el-col>
    </el-row>
  </el-card>
</template>
<script setup lang="ts">
import {list} from "@/api/api-service/client";
import {useI18n} from "vue-i18n";
import {getCurrentInstance, reactive, ref, toRefs, watch} from "vue";
import DictTagNumber from "@/components/DIctTagNumber/index.vue";
import * as appResourcesApi from "@/api/app/resources";
import modal from "@/plugins/modal";
import {getResourceByClient} from "@/api/app/resources";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const {client_type}
    = proxy?.useDict("client_type");

const props = defineProps({
  appId: {
    type: String,
    default: undefined
  }
});


const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const resourceLabel = (classify: any) => {
  const map = {
    menu: '菜单',
    button: '按钮',
    api: 'API',
    openApi: 'OpenAPI'
  }
  return map[classify] || '未知'
}

const resourceTagType = (classify: any) => {
  const map = {
    menu: 'success',
    button: 'info',
    api: 'warning',
    openApi: 'danger'
  }
  return map[classify] || ''
}

const {queryParams} = toRefs(data);
const clientList: any = ref<any>([]);
const loading: any = ref(true);
const total: any = ref(0);
const currentRow = ref<any>(undefined);
const selectedClientId = ref<string | null>(null);
//树型规则，false=遵循父子规则，true=不遵循
const checkStrictly = ref(true);
const dataOptions = ref([]);
const treeData = ref([]);//当前选中节点
const defaultProps = ref({
  children: "children",
  label: "name"
});
//右侧树型勾选集合
const cheackdData: any = ref<any>([]);
const resTreeRef: any = ref<any>({});


/**
 * 点击行时同步 radio
 */
function handleRowClick(row: any) {
  if (currentRow.value?.id === row.id) {
    return;
  }
  selectedClientId.value = row.clientId;
  currentRow.value = row;
  checkStrictly.value = true;
  cheackdData.value = [];
  let param: any = {
    appId: props.appId,
    clientId: row.id
  }
  appResourcesApi.getResourceByClient(param).then((res: any) => {
    if (res.code === 0) {
      for (let i in res.data) {
        cheackdData.value.push(res.data[i].resourceId)
      }
      resTreeRef.value.setCheckedKeys(cheackdData.value)
    }
  });
}


/**
 * 获取列表
 */
function getList(): any {
  list(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      clientList.value = res.data.rows;
      total.value = res.data.records;

      // 默认选中第一行
      if (clientList.value.length > 0) {
        const first = clientList.value[0];

        // 触发一次完整的选中逻辑（和点击行完全一致）
        handleRowClick(first);
      }
    }
  })
}

function loadTree() {
  let params = {
    appId: props.appId,
  }

  appResourcesApi.getTree(params).then((res: any) => {
    // 清空，避免多次调用重复 push
    treeData.value = []

    collectExpandIds(res.data.resources || [], treeData, 1)

    dataOptions.value = res.data.resources;
  })
}

const collectExpandIds = (nodes: any, targetRef: any, maxLevel = 1) => {
  const traverse = (node: any, level: any) => {
    if (level <= maxLevel) {
      targetRef.value.push(node.id)
    }
    if (Array.isArray(node.children)) {
      node.children.forEach((child: any) => traverse(child, level + 1))
    }
  }

  nodes.forEach((root: any) => traverse(root, 1))
}

function submitForm() {
//声明选择权限的ID集合
  let resourceIds: any = [];
  //获取树选中节点集合
  let treeData: any = resTreeRef.value.getCheckedNodes();
  for (let i in treeData) {
    //添加到选择集合中
    resourceIds.push(treeData[i].id)
  }
  let data: any = {
    clientId: currentRow.value.id,
    appId: props.appId,
    resourceIds: resourceIds
  }
  appResourcesApi.saveClientAuthz(data).then((res: any) => {
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(res.message);
    }
  });
}

watch(
    () => props.appId,
    (val) => {
      if (val != null) {
        // queryParams.value.appId = val
        getList();
        loadTree();
      }
    },
    {immediate: true}
)

</script>

<style scoped lang="scss">
:deep(.radio-column .cell) {
  display: flex;
  justify-content: right;
  align-items: center;
}

.page-header {
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e6e6e6;
}

.page-header h3 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.md {
  color: #ccc;
  font-size: 12px;
  margin-left: 20px
}

.tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.tree-label {
  display: flex;
  align-items: center;
  gap: 6px;
}

.tree-tag {
  margin-left: 8px;
}

</style>
