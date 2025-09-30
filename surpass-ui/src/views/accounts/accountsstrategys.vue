<template>
  <div class="app-container">
    <el-card class="common-card">
      <div class="queryForm">
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
            <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
            <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            size="small"
            @click="handleAdd"
        >{{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            plain
            size="small"
            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('jbx.text.delete') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.accountsstrategy.name')" align="left" prop="name"
                         :show-overflow-tooltip="true"/>
        <el-table-column :label="$t('jbx.apps.name')" align="left" prop="appName" :show-overflow-tooltip="true"/>
        <el-table-column :label="$t('jbx.accountsstrategy.createType')" align="left">
          <template #default="scope">
            <dict-tag :options="create_type" :value="scope.row.createType"></dict-tag>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.users.status')" align="center" prop="status">
          <template #default="scope">
            <span v-if="scope.row.status == 1"><el-icon color="green"><SuccessFilled class="success"/></el-icon></span>
            <span v-if="scope.row.status == 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>

        <el-table-column :label="$t('jbx.text.action')" align="center" class-name="small-padding fixed-width"
                         width="200">
          <template #default="scope">
            <el-button
                @click="handleUpdate(scope.row)"
            >{{ $t('jbx.text.edit') }}
            </el-button>
            <el-button
                type="danger"
                @click="handleDelete(scope.row)">
              {{ $t('jbx.text.delete') }}
            </el-button>
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
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="$t('jbx.accountsstrategy.name')" prop="name">
          <el-input v-model="form.name" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.apps.name')" prop="appName">
          <el-input v-model="form.appName" placeholder="" :disabled="true">
            <template #append>
              <el-button @click="openApps">{{ t('jbx.text.select') }}</el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item :label="$t('jbx.accountsstrategy.mapping')" prop="mapping">
          <el-select v-model="form.mapping" placeholder="" clearable style="width: 100%">
            <el-option
                v-for="dict in account_mapping"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('jbx.accountsstrategy.suffixes')" prop="suffixes">
          <el-input v-model="form.suffixes" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.accountsstrategy.orgIdsList')" prop="orgIdsList">
          <el-tree
              ref="resTreeRef"
              node-key="key"
              :data="deptOptions"
              :props="defaultProps"
              :default-checked-keys="cheackdData"
              :expand-on-click-node="false"
              highlight-current
              show-checkbox
              v-slot="{ node, data }"
          >
              <span>
                <span v-if="node.label.length<=10">{{ node.label }}</span>
                <span v-else>
                   <el-tooltip class="item" effect="dark" :content="node.label" placement="right">
                     <span>{{ node.label.slice(0, 10) + '...' }}</span>
                  </el-tooltip>
                </span>
              </span>
          </el-tree>
        </el-form-item>
        <el-form-item :label="$t('jbx.accountsstrategy.filters')" prop="description">
          <el-input type="textarea" rows="3" maxlength="200" show-word-limit v-model="form.filters" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.accountsstrategy.createType')" prop="createType">
          <el-select v-model="form.createType" placeholder="" clearable style="width: 100%">
            <el-option
                v-for="dict in create_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('jbx.users.status')" prop="status">
          <el-switch
              v-model="form.status"
              active-value="1"
              inactive-value="0"
          ></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!--  弹出应用列表 -->
    <select-apps ref="selectAppsRef" @changeApps="changeApps"></select-apps>

  </div>
</template>


<script setup name="Accounts-strategys" lang="ts">
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
  apiGet,
  apiCreate,
  apiUpdate,
  apiOrgTree,
  apiDeleteById
} from "@/api/accounts/accountsstrategys";

import selectApps from "@/components/SelectApps/index"

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()
const {protocol_type, create_type, account_mapping} = proxy.useDict("protocol_type", "create_type", "account_mapping");
const selectAppsRef: any = ref(undefined);
const deptOptions: any = ref<any>([]);
const defaultProps: any = ref({
  children: "children",
  label: "title"
});
//右侧树型勾选集合
const cheackdData: any = ref<any>([]);
const resTreeRef: any = ref(undefined);
const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

const data: any = reactive({
  form: {
    id: undefined,
    name: undefined,
    mapping: 'username',
    status: '1',
    orgIdsList: undefined,
    createType: 'automatic'
  },
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    appName: undefined,
  },
  rules: {
    name: [{required: true, message: 'Not empty', trigger: "blur"}],
    protocol: [{required: true, message: 'Not empty', trigger: "blur"}],
    adapter: [{required: true, message: 'Not empty', trigger: "blur"}],
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
  queryRef?.value?.resetFields();
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return apiDeleteById(id);
  }).then(() => {
    getList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {
  });
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
    mapping: 'username',
    orgIdsList: undefined,
    status: '1',
    createType: 'automatic'
  };
  cheackdData.value = [];
  nextTick(() => {
    if (resTreeRef != undefined && resTreeRef.value != undefined) {
      resTreeRef.value.setCheckedKeys([])
    }
  });
  formRef.value?.clearValidate();
}

/** 添加分组 */
function handleAdd(): any {
  reset();
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
    cheackdData.value = [];
    if (form.value.orgIdsList !== undefined) {
      let orgs: any = form.value.orgIdsList.split(",");
      for (let i in orgs) {
        if (orgs[i] !== '') {
          cheackdData.value.push(orgs[i])
        }
      }
      if (resTreeRef.value != undefined) {
        nextTick(() => {
          resTreeRef.value.setCheckedKeys(cheackdData.value)
        });
      }
    }
    title.value = t('jbx.text.edit');
  });
};


function loadTree(): any {
  apiOrgTree().then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      let rootNode: any = {
        title: res.data.rootNode.title,
        key: res.data.rootNode.key,
      };
      deptOptions.value = bulidTree(rootNode, res.data.nodes)
    }
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

/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) => {
    if (valid) {
      //声明选择权限的ID集合
      let resourceIds: any = [];
      //获取树选中节点集合
      let treeData: any = resTreeRef.value.getCheckedNodes();
      let orgIdsList: any = "";
      for (let i: any in treeData) {
        console.log(i * 1 + 1, treeData.length)
        if (i * 1 + 1 === treeData.length) {
          orgIdsList += treeData[i].key
        } else {
          orgIdsList += treeData[i].key + ","
        }
      }
      form.value.orgIdsList = orgIdsList;
      if (form.value.id != undefined) {
        apiUpdate(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        apiCreate(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
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

/** 授权应用 */
function changeApps(id: any, appName: any): any {
  form.value.appId = id;
  form.value.appName = appName;
}

function openApps(): any {
  selectAppsRef.value.openApps();
}

getList();

loadTree();

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
  margin-bottom: 0px;
}
</style>
