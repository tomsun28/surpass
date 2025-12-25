<template>
  <el-card class="common-card query-box">
    <div class="queryForm">
      <el-form :model="queryParams" ref="queryRef" :inline="true"
               @submit.native.prevent>
        <el-form-item label="资源名称">
          <el-input
              v-model="queryParams.name"
              clearable
              style="width: 200px"
              @keyup.enter="loadApis"
          />
        </el-form-item>
        <el-form-item label="资源类型">
          <el-select v-model="queryParams.classify" clearable style="width: 200px" @change="loadApis">
            <el-option
                v-for="dict in resources_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button @click="loadApis">{{ t('org.button.query') }}</el-button>
          <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </div>
  </el-card>
  <div class="api-page">
    <el-row :gutter="20">
      <el-col :span="6">
        <el-tree
            style="width: 100%; "
            node-key="id"
            :data="dataOptions"
            :props="defaultProps"
            :expand-on-click-node="false"
            :filter-node-method="filterNode"
            ref="tree"
            :default-expanded-keys="treeData"
            @node-click="handleNodeClick"
            highlight-current
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
      <el-col :span="18">
        <div class="page-content">
          <!-- 操作栏 -->
          <div class="action-bar">
            <el-button type="primary" @click="showCreateDialog">
              新增资源
            </el-button>
            <el-button
                type="danger"
                :disabled="ids.length === 0"
                @click="onBatchDelete"
            >{{ t('org.button.deleteBatch') }}
            </el-button>
            <el-button @click="refreshList">
              刷新
            </el-button>
          </div>

          <!-- API列表 -->
          <el-table :data="apiList" border v-loading="loading" style="width: 100%"
                    @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column prop="name" label="资源名称"/>
            <el-table-column prop="path" label="请求地址"/>
            <el-table-column prop="path" label="资源类型">
              <template #default="scope">
                <dict-tag :options="resources_type" :value="scope.row.classify"/>
              </template>
            </el-table-column>
            <el-table-column prop="method" label="方法"
                             v-if="queryParams.classify === 'openApi' || queryParams.classify === 'api'">
              <template #default="{ row }">
                <el-tag :type="getMethodTagType(row.method)">
                  {{ row.method }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="datasourceId" label="数据源"
                             v-if="queryParams.classify === 'openApi'">
              <template #default="{ row }">
                <el-tag v-if="row.datasourceId">
                  {{ dataSourceList.find(ds => ds.id === row.datasourceId)?.name }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="120">
              <template #default="{ row }">
                <el-tooltip content="版本管理" placement="top" v-if="row.classify === 'openApi'">
                  <el-button link icon="Document" @click="viewVersions(row)"></el-button>
                </el-tooltip>
                <el-tooltip content="编辑" placement="top">
                  <el-button link icon="Edit" type="primary" @click="editApi(row)"></el-button>
                </el-tooltip>
                <el-tooltip content="删除" placement="top">
                  <el-button link icon="Delete" type="danger" @click="deleteApi(row)"></el-button>
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
              @pagination="loadApis"
          />
          <!-- 空状态 -->
          <el-empty v-if="!loading && apiList.length === 0" description="暂无API定义"/>
        </div>
      </el-col>
    </el-row>
    <!-- 新增/编辑对话框 -->
    <el-dialog
        :title="dialogTitle"
        v-model="dialogVisible"
        width="1000px"
        :before-close="handleDialogClose"
        append-to-body
        destroy-on-close
    >
      <el-form
          ref="formRef"
          :model="formData"
          :rules="formRules"
          label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资源名称" prop="name">
              <el-input v-model="formData.name" placeholder="请输入资源名称"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源类型">
              <el-radio-group v-model="formData.classify">
                <el-radio
                    v-for="dict in resources_type"
                    :key="dict.value"
                    :label="dict.value"
                >
                  {{ dict.label }}
                </el-radio>
              </el-radio-group>
            </el-form-item>

          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" v-if="formData.classify !== 'button'">
            <el-form-item label="请求地址" prop="path">
              <el-input v-model="formData.path" placeholder="请输入资源路径，如：/users"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源" prop="datasourceId" v-if="formData.classify === 'openApi'">
              <el-select
                  v-model="formData.datasourceId"
                  placeholder="请选择数据源"
                  style="width: 100%"
                  :loading="dataSourceLoading"
              >
                <el-option
                    v-for="ds in dataSourceList"
                    :key="ds.id"
                    :label="ds.name"
                    :value="ds.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" v-if="formData.classify === 'api' || formData.classify === 'openApi'">
            <el-form-item label="请求方式" prop="method">
              <el-select v-model="formData.method" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in method_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.classify === 'api' || formData.classify === 'openApi'">
            <el-form-item label="请求参数">
              <el-input v-model="formData.params" placeholder=""/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限标识">
              <el-input v-model="formData.permission" placeholder=""/>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.classify === 'button'">
            <el-form-item label="操作类型">
              <el-select v-model="formData.actionType" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in action_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20" v-if="formData.classify === 'menu'">
          <el-col :span="12">
            <el-form-item label="资源样式">
              <el-input readonly v-model="formData.resStyle" placeholder=""/>
              <icon-select style="padding-left: 0;padding-right: 0" v-model="formData.resStyle"
                           @selected="(name) => {formData.resStyle = name}"></icon-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="父级菜单" prop="parentId">
              <el-tree-select
                  clearable
                  v-model="formData.parentId"
                  :data="dataOptionsMenu"
                  :props="defaultProps"
                  check-strictly
                  value-key="id"
                  :placeholder="t('org.placeholder.parent')"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" v-if="formData.classify === 'openApi'">
            <el-form-item label="是否开放">
              <el-switch
                  v-model="formData.isOpen"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.classify === 'menu'">
            <el-form-item label="外部链接">
              <el-switch
                  v-model="formData.isFrame"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12" v-if="formData.classify === 'menu'">
            <el-form-item label="是否缓存">
              <el-switch
                  v-model="formData.isCache"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="formData.classify === 'menu'">
            <el-form-item label="是否可见">
              <el-switch
                  v-model="formData.isVisible"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-switch
                  v-model="formData.status"
                  active-value="1"
                  inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
              <el-input v-model="formData.sortIndex" placeholder=""/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="描述" prop="description">
              <el-input
                  v-model="formData.description"
                  type="textarea"
                  :rows="3"
                  placeholder="请输入API描述"
              />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="handleSubmit">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, reactive, computed, toRefs, nextTick} from 'vue'
import {useRouter} from 'vue-router'
import {ElMessage, ElMessageBox} from 'element-plus'
import * as dataSourceApi from '@/api/api-service/dataSource.ts'
import * as apiDefinitionApi from '@/api/api-service/apiDefinitionApi.ts'
import * as appResourcesApi from '@/api/app/resources.js'
import modal from "@/plugins/modal.js";
import {set2String} from "@/utils/index.js";
import {useI18n} from "vue-i18n";
import * as proxy from "@/utils/Dict.js";
import IconSelect from "@/components/IconSelect/index.vue";
import DictTag from "@/components/DictTag/index.vue";

const {resources_type, action_type, method_type} = proxy.useDict("resources_type", "action_type", "method_type");
const router = useRouter()

const props = defineProps({
  appId: {
    type: String,
    default: undefined
  }
});

// 响应式数据
const loading = ref(false)
const dataSourceLoading = ref(false)
const dialogVisible = ref(false)
const submitting = ref(false)
const isEdit = ref(false)
const formRef = ref()
const resTreeRef = ref(undefined);
const ids = ref([]);
const selectionlist = ref([]);
const apiList = ref([])
const total = ref(0);
const dataSourceList = ref([])
const dataOptions = ref([]);
const dataOptionsMenu = ref([]);
const treeData = ref([]);//当前选中节点
const treeDataMenu = ref([]);//当前选中节点菜单
const defaultProps = ref({
  children: "children",
  label: "name"
});

const data = reactive({
  queryParams: {
    appId: null,
    name: '',
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {t} = useI18n()

const {queryParams} = toRefs(data);

const formData = reactive({
  id: null,
  name: '',
  classify: 'menu',
  path: '',
  method: 'GET',
  params: '',
  datasourceId: null,
  description: '',
  actionType: 'r',
  permission: '',
  resStyle: '',
  parentId: '',
  isVisible: 'y',
  isCache: 'n',
  isFrame: 'n',
  isOpen: 'y',
  status: '1',
  sortIndex: '1'
})

// 表单验证规则
const formRules = {
  name: [
    {required: true, message: '请输入API名称', trigger: 'blur'}
  ],
  path: [
    {required: true, message: '请输入API路径', trigger: 'blur'}
  ],
  method: [
    {required: true, message: '请选择HTTP方法', trigger: 'change'}
  ],
  datasourceId: [
    {required: true, message: '请选择数据源', trigger: 'change'}
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑资源' : '新增资源')

// 方法
const loadApis = async () => {
  appResourcesApi.pageResources(queryParams.value).then((res) => {
    if (res.code === 0) {
      loading.value = false;
      apiList.value = res.data.rows;
      total.value = res.data.records;
    }
  })
}

const loadDataSources = async () => {
  try {
    dataSourceLoading.value = true
    const response = await dataSourceApi.list()
    dataSourceList.value = response.data || []
  } catch (error) {
    ElMessage.error('加载数据源列表失败')
    console.error('加载数据源列表失败:', error)
  } finally {
    dataSourceLoading.value = false
  }
}

const showCreateDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

/** 通过条件过滤节点  */
const filterNode = (value, data) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

const editApi = (row) => {
  isEdit.value = true
  Object.assign(formData, {...row})
  dialogVisible.value = true
}

const resetForm = () => {
  Object.assign(formData, {
    id: null,
    name: '',
    classify: 'menu',
    path: '',
    method: 'GET',
    params: '',
    datasourceId: null,
    description: '',
    actionType: 'r',
    permission: '',
    resStyle: '',
    parentId: '',
    isVisible: 'y',
    isCache: 'n',
    isFrame: 'n',
    isOpen: 'y',
    status: '1',
    sortIndex: '1'
  })
  formRef.value?.clearValidate()
}

const handleDialogClose = () => {
  dialogVisible.value = false
  resetForm()
}

const handleSubmit = async () => {
  const handleResponse = (res, successMessage) => {
    if (res.code === 0) {
      dialogVisible.value = false
      loadApis()
      loadTree()
    } else {
      modal.msgError(res.message);
    }
    submitting.value = false
  };

  formRef?.value?.validate((valid) => {
    if (valid) {
      submitting.value = true
      formData.appId = props.appId;

      const operation = isEdit.value ? appResourcesApi.update : appResourcesApi.create;
      const successMessage = isEdit.value
          ? t('org.success.update')
          : t('org.success.add');
      operation(formData).then((res) => handleResponse(res, successMessage));
    }
  });
}

const viewVersions = (row) => {
  router.push(`/api/Version?apiId=${row.id}`)
}

const deleteApi = async (row) => {
  try {
    await ElMessageBox.confirm(
        `确定要删除资源 "${row.name}" 吗？此操作不可恢复。`,
        '确认删除',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }
    )

    await appResourcesApi.deleteData(row.id)
    ElMessage.success('删除成功')
    loadApis()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
      console.error('删除失败:', error)
    }
  }
}

/** 多选删除操作*/
function onBatchDelete() {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds = set2String(ids.value);
    return appResourcesApi.deleteData(setIds);
  }).then((res) => {
    if (res.code === 0) {
      loadApis();
      loadTree();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

/** 多选操作*/
function handleSelectionChange(selection) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item) => item.id);
}

const getMethodTagType = (method) => {
  const types = {
    'GET': 'success',
    'POST': 'primary',
    'PUT': 'warning',
    'DELETE': 'danger',
    'PATCH': 'info'
  }
  return types[method] || 'info'
}

const refreshList = () => {
  loadApis()
}

/**
 * 重置
 */
function resetQuery() {
  queryParams.value.name = undefined;
  queryParams.value.classify = undefined;
  queryParams.value.parentId = undefined;
  loadApis();
}

function loadTree() {
  let params = {
    appId: props.appId,
  }

  appResourcesApi.getTree(params).then(res => {
    // 清空，避免多次调用重复 push
    treeData.value = []
    treeDataMenu.value = []

    collectExpandIds(res.data.resources || [], treeData, 1)
    collectExpandIds(res.data.resourcesMenu || [], treeDataMenu, 1)

    dataOptions.value = res.data.resources;
    dataOptionsMenu.value = res.data.resourcesMenu;
  })
}

const collectExpandIds = (nodes, targetRef, maxLevel = 1) => {
  const traverse = (node, level) => {
    if (level <= maxLevel) {
      targetRef.value.push(node.id)
    }
    if (Array.isArray(node.children)) {
      node.children.forEach(child => traverse(child, level + 1))
    }
  }

  nodes.forEach(root => traverse(root, 1))
}

/** 节点单击事件 */
function handleNodeClick(data) {
  queryParams.value.parentId = data.key;
  loadApis();
}

const resourceLabel = (classify) => {
  const map = {
    menu: '菜单',
    button: '按钮',
    api: 'API',
    openApi: 'OpenAPI'
  }
  return map[classify] || '未知'
}

const resourceTagType = (classify) => {
  const map = {
    menu: 'success',
    button: 'info',
    api: 'warning',
    openApi: 'danger'
  }
  return map[classify] || ''
}


watch(
    () => props.appId,
    (val) => {
      if (val != null) {
        queryParams.value.appId = val
        loadApis()
        loadTree();
        loadDataSources()
      }
    },
    {immediate: true}
)
</script>

<style scoped>
.api-page {
  background: #fff;
  border-radius: 4px;
  padding: 20px;
  min-height: calc(100vh - 140px);
}

.page-header h2 {
  margin: 0 0 8px 0;
  color: #303133;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.action-bar {
  margin-bottom: 20px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
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
