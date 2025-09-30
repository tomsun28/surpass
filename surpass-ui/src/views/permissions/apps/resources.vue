<template>
  <div class="app-container">
    <!--    <el-card class="common-card">
          {{ $t('jbx.message.cheack.node') }}：{{ appName }}
        </el-card>-->
    <el-card class="common-card">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" @submit.native.prevent>
          <el-form-item :label="$t('jbx.resources.name')">
            <el-input
                v-model="queryParams.resName"
                clearable
                style="width: 240px"
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ $t('jbx.text.query') }}
            </el-button>
            <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-tree
              ref="resTreeRef"
              node-key="key"
              :data="dataOptions"
              :props="defaultProps"
              @node-click="handleNodeClick"
              :expand-on-click-node="false"
              highlight-current
              v-slot="{ node, data }"
          >
            <span>
              <span v-if="node.label?.length<=20">{{ node.label }}</span>
              <span v-else>
                 <el-tooltip class="item" effect="dark" :content="node.label" placement="right">
                   <span>{{ node.label.slice(0, 10) + '...' }}</span>
                </el-tooltip>
              </span>
            </span>
          </el-tree>
        </el-col>
        <el-col :span="18">
          <div class="btn-form" style="margin-bottom: 10px">
            <el-button
                type="primary"
                @click="handleAdd"
            >{{ $t('jbx.text.add') }}
            </el-button>
            <el-button
                type="danger"
                plain
                :disabled="multiple"
                @click="handleDelete"
            >{{ $t('jbx.text.delete') }}
            </el-button>
          </div>

          <el-table v-loading="loading" :data="list" border>
            <el-table-column :label="$t('jbx.resources.name')" align="left" prop="resName"/>
            <el-table-column :label="$t('jbx.resources.resourceType.index')" align="center">
              <template #default="scope">
                <dict-tag :options="resource_type" :value="scope.row.classify"></dict-tag>
              </template>
            </el-table-column>
            <el-table-column :label="$t('jbx.resources.requesturl')" align="left" prop="requestUrl"/>
            <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex" width="120"/>
            <el-table-column :label="$t('jbx.users.status')" align="center" prop="status" width="120">
              <template #default="scope">
                <span v-if="scope.row.status == 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
                <span v-if="scope.row.status == 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
              </template>
            </el-table-column>
            <el-table-column :label="$t('jbx.text.action')" align="center"
                             class-name="small-padding fixed-width" width="80">
              <template #default="scope">
                <el-tooltip content="编辑">
                  <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
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
              @pagination="getList"
          />
        </el-col>
      </el-row>
    </el-card>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="900px" append-to-body destroy-on-close>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('jbx.resources.name')" prop="resName">
              <el-input v-model="form.resName" placeholder=""/>
            </el-form-item>

            <el-form-item :label="$t('jbx.resources.i18n')" prop="i18n">
              <el-input v-model="form.i18n" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.actionTypes')">
              <el-select v-model="form.actionType" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in action_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.resStyle')">
              <el-input readonly v-model="form.resStyle" placeholder=""/>
              <icon-select v-model="form.resStyle" @selected="(name:any) => {form.resStyle = name}"></icon-select>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.icon')">
              <el-input v-model="form.icon" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.iconSelected')">
              <el-input v-model="form.iconSelected" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.isOpen') ">
              <el-switch
                  v-model="form.isOpen"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.isFrame')">
              <el-switch
                  v-model="form.isFrame"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
            <el-form-item :label="$t('jbx.users.status')" prop="status">
              <el-switch
                  v-model="form.status"
                  active-value="1"
                  inactive-value="0"
              ></el-switch>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('jbx.organizations.parentName')" prop="parentId">
              <el-tree-select
                  v-model="form.parentId"
                  :data="dataOptions"
                  :props="defaultProps"
                  check-strictly
                  value-key="key"
              />
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.resourceType.index')">
              <el-select v-model="form.classify" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in resource_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.requesturl')" prop="requestUrl">
              <el-input v-model="form.requestUrl" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.params')">
              <el-input v-model="form.params" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.requestMethod')">
              <el-select v-model="form.requestMethod" placeholder="" clearable style="width: 100%">
                <el-option
                    v-for="dict in method_type"
                    :key="dict.value"
                    :label="dict.label"
                    :value="dict.value"
                />
              </el-select>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.permission')">
              <el-input v-model="form.permission" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
              <el-input v-model="form.sortIndex" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.isCache')">
              <el-switch
                  v-model="form.isCache"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
            <el-form-item :label="$t('jbx.resources.isVisible')">
              <el-switch
                  v-model="form.isVisible"
                  active-value="y"
                  inactive-value="n"
              ></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
</template>

<script setup name="Apps-Resources" lang="ts">
import {useRoute, useRouter} from "vue-router";
import {ElForm} from "element-plus";
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
  watch,
  defineComponent,
  nextTick
} from "vue";
import modal from "@/plugins/modal";
import {
  apiFetch,
  apiTree,
  apiGet,
  apiUpdate,
  apiCreate,
  apiDeleteByIds
} from "@/api/permissions/resources";
import IconSelect from "@/components/IconSelect/index.vue"

import {useI18n} from "vue-i18n";
import SvgIcon from "@/components/SvgIcon/index.vue";


const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);

const {resource_type, action_type, method_type} = proxy.useDict("resource_type", "action_type", "method_type");

const {t} = useI18n()
const router: any = useRouter();
const route: any = useRoute();
//获取路由参数
const {appName} = route.query;

const appId = "1";

const currentResourceId: any = ref(undefined);
const currentResourceName: any = ref(undefined);
const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const total: any = ref(0);
const title: any = ref("");
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const resTreeRef: any = ref(undefined);
const dataOptions: any = ref<any>([]);
const defaultProps: any = ref({
  children: "children",
  label: "title"
});

const data: any = reactive({
  form: {
    id: undefined,
    name: undefined,
    parentName: undefined,
    classify: 'MENU',
    isVisible: 'y',
    isCache: 'n',
    requestMethod: 'GET',
    actionType: 'r',
    parentId: undefined,
    appId: undefined,
    appName: undefined,
    sortIndex: 1,
    status: '1'
  },
  queryParams: {
    appId: '1',
    appName: appName,
    resName: undefined,
    parentId: undefined,
    pageNumber: 1,
    pageSize: 10,
  },
  rules: {
    resName: [{required: true, message: 'Not empty', trigger: "blur"}],
    parentId: [{required: true, message: 'Not empty', trigger: "blur"}],
    i18n: [{required: true, message: 'Not empty', trigger: "blur"}],
    // requestUrl: [{required: true, message: 'Not empty', trigger: "blur"}],
  },
});

const {queryParams, form, rules} = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  apiFetch(queryParams.value).then((res: any) => {
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
  queryParams.value.resName = undefined;
  queryParams.value.parentId = undefined;
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return apiDeleteByIds(id);
  }).then(() => {
    getList();
    loadTree()
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {
  });
}

/** 节点单击事件 */
function handleNodeClick(data: any): any {
  currentResourceId.value = data.key
  currentResourceName.value = data.title;
  queryParams.value.parentId = data.key;
  handleQuery();
}

/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 重置新增的表单以及其他数据  */
function reset(): any {
  form.value = {
    id: undefined,
    name: undefined,
    parentName: undefined,
    classify: 'MENU',
    isVisible: 'y',
    isCache: 'n',
    requestMethod: 'GET',
    actionType: 'r',
    parentId: undefined,
    sortIndex: 1,
    status: '1'
  };
  formRef.value?.clearValidate();
}

/** 添加分组 */
function handleAdd(): any {
  reset();
  form.value.parentName = currentResourceName.value;
  form.value.parentId = currentResourceId.value;
  open.value = true;
  title.value = t('jbx.text.add');
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  reset();
  const id: any = row.id || ids.value;
  apiGet(id).then((res: any) => {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
};


/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) => {
    if (valid) {
      if (form.value.id != undefined) {
        apiUpdate(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
          loadTree()
        });
      } else {
        form.value.appId = appId;
        form.value.appName = appName;
        apiCreate(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
          loadTree()
        });
      }
    }
  });
}

/** 取消按钮 */
function cancel(): any {
  open.value = false;
  reset();
}

function loadTree(): any {
  let params: any = {
    appId: appId,
    appName: appName
  }
  apiTree(params).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      let rootNode: any = {
        title: res.data.rootNode.title,
        key: res.data.rootNode.key
      };
      dataOptions.value = bulidTree(rootNode, res.data.nodes)
    }
    nextTick(() => {
      resTreeRef.value.store.nodesMap[appId].expanded = true;
    });
  });
}

function bulidTree(rootNode: any, nodes: any): any {
  let treeNodes: any = [];
  for (let node of nodes) {
    if (node.key != rootNode.key && node.parentKey == rootNode.key) {
      let nodeType: any = node.attrs && node.attrs.type ? node.attrs.type : '';
      let treeNode: any = {title: node.title, key: node.key, type: nodeType, expanded: false, isLeaf: true};
      bulidTree(treeNode, nodes);
      treeNodes.push(treeNode);
      rootNode.isLeaf = false;
    }
  }
  rootNode.children = treeNodes;
  return [rootNode];
}

loadTree();
getList();
</script>
<style scoped>
::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

.common-card {
  margin-bottom: 15px;
}

::v-deep(.common-card form .el-form-item--default) {
  margin-bottom: 0;
}

:deep(.icon-body){
  padding: 5px 0 !important;
}
</style>
