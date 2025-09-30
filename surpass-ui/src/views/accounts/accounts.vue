<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-row>
        <el-col :span="9">
          <el-form :model="appParams" ref="appRef" :inline="true"
                   @submit.native.prevent>
            <el-form-item>
              <el-select v-model="selectType" @change="handleChangeQuery" style="width: 100px">
                <el-option
                    v-for="item in app_select_type"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item :label="t('jbx.apps.id')" v-if="selectType === 'APPID'">
              <el-input
                  v-model="appParams.id"
                  clearable
                  style="width: 100px"
              />
            </el-form-item>
            <el-form-item :label="t('jbx.apps.name')" v-if="selectType === 'APPNAME'">
              <el-input
                  v-model="appParams.appName"
                  clearable
                  style="width: 100px"
              />
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
              <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-col>
          <el-col :span="15">
          <el-form :model="accountsParams" ref="appRef" :inline="true"
                   @submit.native.prevent>
            <el-form-item :label="t('jbx.apps.name')">
              <el-input
                  disabled
                  v-model="selectAppName"
                  clearable
              />
            </el-form-item>
            <el-form-item :label="t('jbx.users.username')">
              <el-input
                  v-model="accountsParams.username"
                  clearable
              />
            </el-form-item>
            <el-form-item>
              <el-button @click="handleAccountQuery">{{ t('org.button.query') }}</el-button>
              <el-button @click="resetQueryAccount">{{ t('org.button.reset') }}</el-button>
            </el-form-item>
          </el-form>
        </el-col>
      </el-row>
    </el-card>
    <el-card>
      <el-row :gutter="20">
        <el-col :span="9">
          <el-table
              class="appTable"
              ref="appTable"
              v-loading="appLoading"
              :data="appList"
              border
              @select="handleSelectionChangeApp"
          >
            <el-table-column type="selection" width="50" align="center"/>
            <el-table-column prop="id" :label="t('jbx.apps.id')" align="center"
                             min-width="100" :show-overflow-tooltip="true"/>
            <el-table-column prop="appName" :label="t('jbx.apps.name')" align="center"
                             min-width="60" :show-overflow-tooltip="true"/>
          </el-table>
          <pagination
              :pager-count="5"
              layout="total, sizes, prev, pager, next"
              v-show="appTotal > 0"
              :total="appTotal"
              v-model:page="appParams.pageNumber"
              v-model:limit="appParams.pageSize"
              :page-sizes="appParams.pageSizeOptions"
              @pagination="getAppList"
          />
        </el-col>
          <el-col :span="15">
          <div class="button-top">
            <el-button
                :disabled="!selectProtocolId"
                @click="handleAdd"
                type="primary">
              {{ $t('jbx.text.add') }}
            </el-button>
            <el-button
                :disabled="!selectProtocolId"
                @click="handleUnit"
            >
              {{ $t('accountJoint') }}
            </el-button>
            <el-button
                type="danger"
                :disabled="idsAccount.length === 0"
                @click="onDeleteBatch"
            >{{ t('org.button.deleteBatch') }}
            </el-button>
          </div>
          <el-table
              border
              v-loading="accountLoading"
              :data="accountList"
              @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column :label="t('jbx.accounts.username')" align="center" prop="username"
                             min-width="80"
                             :show-overflow-tooltip="true"/>
            <el-table-column :label="t('jbx.accounts.displayName')" align="center" prop="displayName"
                             :show-overflow-tooltip="true" min-width="80"></el-table-column>
            <el-table-column :label="t('jbx.accounts.appName')" align="center" prop="appName"
                             :show-overflow-tooltip="true" min-width="100"/>
            <el-table-column :label="t('jbx.accounts.relatedUsername')" align="center"
                             prop="relatedUsername" :show-overflow-tooltip="true" min-width="100"></el-table-column>
            <el-table-column :label="t('org.type')" align="center" prop="category" min-width="60"/>
            <el-table-column :label="t('jbx.accountsstrategy.createType')" align="center"
                             prop="createType" min-width="60"></el-table-column>
            <el-table-column :label="t('org.status')" align="center" prop="status" min-width="40">
              <template #default="scope">
                <a :title="$t('jbx.users.statusActive')" v-if="scope.row.status === 1">
                  <el-icon color="green"><SuccessFilled
                      class="success"/></el-icon>
                </a>
                <a :title="$t('jbx.users.statusInactive')" v-if="scope.row.status === 2">
                  <el-icon color="grey"><WarningFilled /></el-icon>
                </a>
                <a :title="$t('jbx.users.statusForbidden')" v-if="scope.row.status === 4">
                  <el-icon color="grey"><RemoveFilled /></el-icon>
                </a>
              </template>
            </el-table-column>
            <el-table-column :label="$t('jbx.text.action')" align="center" width="180">
              <template #default="scope">
                <el-button
                    v-if="scope.row.category === 'app'"
                    style ="margin-right: 10px"
                    @click="handleUpdate(scope.row)"
                >{{ $t('jbx.text.edit') }}
                </el-button>
                <el-dropdown>
                  <el-button>
                    {{ $t('jbx.text.moreaction') }}<el-icon class="el-icon--right"><arrow-down /></el-icon>
                  </el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item v-if="scope.row.status === 2" @click.native="onUpdateStatus(scope.row.id,1)">
                        <span>{{ $t('jbx.text.enable') }}</span>
                      </el-dropdown-item>
                      <el-dropdown-item v-if="scope.row.status === 4" @click.native="onUpdateStatus(scope.row.id,1)">
                        <span>{{ $t('jbx.text.enable') }}</span>
                      </el-dropdown-item>
                      <el-dropdown-item v-if="scope.row.status === 1" @click.native="onUpdateStatus(scope.row.id,4)">
                        <span>{{ $t('jbx.text.disable') }}</span>
                      </el-dropdown-item>
                      <el-dropdown-item divided @click.native="handleDelete(scope.row)">
                        <span style="color: red">{{$t('jbx.text.delete')}}</span>
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </template>
            </el-table-column>
          </el-table>
          <pagination
              v-show="accountTotal > 0"
              :total="accountTotal"
              v-model:page="accountsParams.pageNumber"
              v-model:limit="accountsParams.pageSize"
              :page-sizes="accountsParams.pageSizeOptions"
              @pagination="getAccountList"
          />
        </el-col>
      </el-row>
      <account-edit :open="open" :title="title"
                    :app-id="selectProtocolId"
                    :app-name="selectAppName"
                    :formId="id"
                    :dept-options="deptOptions"
                    :tree-data="treeData"
                 @dialogOfClosedMethods="dialogOfClosedMethods"></account-edit>
      <unitEditForm :open="openUnit" :title="title"
                    :app-id="selectProtocolId"
                    :app-name="selectAppName"
                    :dept-options="deptOptions"
                    :tree-data="treeData"
                    @dialogOfClosedMethods="dialogOfClosedMethods"></unitEditForm>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {listApps} from "@/api/system/apps";
import {deleteAccounts, listAccounts, updateStatus} from "@/api/system/account.js";
import accountEdit from "./accountManagement/edit.vue"
import unitEditForm from "./accountManagement/unit-edit.vue"
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {getTree} from "@/api/system/dept";
import {handleTree} from "@/utils/Jinbooks";
import {set2String} from "@/utils";
import {useRoute} from "vue-router";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {app_select_type} = proxy!.useDict("app_select_type")
// 获取路由参数
const route: any = useRoute();

const data: any = reactive({
  appParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  accountsParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});
const {appParams, accountsParams} = toRefs(data);
const open: any = ref(false);
const openUnit: any = ref(false);
const id: any = ref(undefined);
const appList: any = ref<any>([]);
const accountList: any = ref<any>([]);
const appTotal: any = ref(0);
const accountTotal: any = ref(0);
const title: any = ref("");
const appLoading: any = ref(true);
const accountLoading: any = ref(true);
//默认展开节点
const treeData: any = ref<any>([]);
//账号所选id集合
const idsAccount: any = ref<any>([]);
const selectionlist: any = ref<any>([]);
const selectType: any = ref('APPID')
const selectedRows: any = ref<any>([])
const selectProtocolId: any = ref(undefined)
const selectAppName: any = ref(undefined)
const deptOptions: any = ref<any>([])

onMounted(() => {
  // 获取查询参数
  let userId: any = route.query.userId;
  let username: any = route.query.username;

  if (userId && username) {
    accountsParams.value.username = username
    // 这里执行获取参数后的查询逻辑
    getAccountList();
  }
});

function getAppList(): any {
  listApps(appParams.value).then((res: any) =>  {
    appList.value = res.data.records;
    appTotal.value = res.data.total;
    appLoading.value = false;
  })
}

function getAccountList(): any {
  listAccounts(accountsParams.value).then((res: any) =>  {
    accountList.value = res.data.records;
    accountTotal.value = res.data.total;
    accountLoading.value = false;
  })
}

function handleQuery(): any {
  appParams.value.pageNumber = 1;
  getAppList();
}

function handleAccountQuery(): any {
  accountLoading.value = true;
  accountsParams.value.pageNumber = 1;
  getAccountList();
}

function resetQueryAccount(): any {
  accountsParams.value.username = null;
  handleAccountQuery();
}

function handleChangeQuery(): any {
  appLoading.value = true;
  if (selectType.value === 'APPID') {
    appParams.value.appName = null;
  } else {
    appParams.value.id = null;
  }
  handleQuery();
}

function resetQuery(): any {
  appLoading.value = true;
  appParams.value.id = null;
  appParams.value.appName = null;
  selectType.value = 'APPID';
  selectedRows.value = [];
  selectProtocolId.value = null;
  selectAppName.value = null;
  //查询应用
  handleQuery();
  //查询账号
  handleAccountQuery();
}

/**
 * 应用单选
 */
function handleSelectionChangeApp(selection: any, row: any): any {
  proxy.$refs.appTable.clearSelection()
  if (selection.length === 0) {
    // 判断selection是否有值存在
    selectProtocolId.value = null;
    selectAppName.value = null;
    selectAppToQuery();
    return
  }

  if (row) {
    selectProtocolId.value = row.id
    selectAppName.value = row.appName
    proxy.$refs.appTable.toggleRowSelection(row, true)
    selectAppToQuery();
  }
}
function selectAppToQuery(): any {
  accountsParams.value.appId = selectProtocolId.value;
  accountsParams.value.selectAppName = selectAppName.value;
  handleAccountQuery()
}

function selectRow(row: any): any {
  // 如果已经有选中的行，则清除选中状态
  if (selectedRows.value.length > 0) {
    proxy.$refs.appTable.clearSelection();
  }

  // 选中当前点击的行
  proxy.$refs.appTable.toggleRowSelection(row);

  // 更新 selectedRows 列表，仅保留当前选中的行
  selectedRows.value = [row];

  // 更新选中的协议 ID
  selectProtocolId.value = row.id;
  selectAppName.value = row.appName;

  //查询账号
  //查询账号
  accountsParams.value.appId = selectProtocolId.value;
  accountsParams.value.selectAppName = selectAppName.value;
  handleAccountQuery();
}

/**
 * 修改状态
 * @param userId
 * @param status
 */
function onUpdateStatus(userId: any, status: any): any {
  updateStatus({ id: userId, status: status }).then((res: any) =>  {
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
      getAccountList();
    } else {
      modal.msgError(t('jbx.alert.operate.error'));
    }
  });
}

function dialogOfClosedMethods(val: any): any {
  open.value = false;
  openUnit.value = false;
  id.value = undefined;
  if (val) {
    getAccountList();
  }
}

function handleAdd(): any {
  open.value = true;
  id.value = undefined;
  title.value = t('jbx.text.add')
}

function handleUnit(): any {
  openUnit.value = true;
  id.value = undefined;
  title.value = t('accountJoint')
}

/** 获取组织树 */
function getOrgTree(): any {
  getTree().then((response: any) =>  {
    response.data.nodes.forEach((node: any) =>  {
      if (node.key === response.data.rootNode.key) {
        node.parentKey = null;
      }
    });
    deptOptions.value = handleTree(response.data.nodes, 'key', 'parentKey', 'children')
    deptOptions.value = deptOptions.value.filter((t: any) =>  {
      return response.data.rootNode.key == t.key
    })
    treeData.value.push(response.data.rootNode.key);
  });
}

function handleDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.username + t('org.deleteTip2')).then(function () {
    return deleteAccounts(row.id);
  }).then((res: any) =>  {
    if (res.code === 0) {
      getAccountList();
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

function onDeleteBatch(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds: any = set2String(idsAccount.value);
    return deleteAccounts(setIds);
  }).then((res: any) =>  {
    if (res.code === 0) {
      getAccountList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  idsAccount.value = selectionlist.value.map((item: any) =>  item.id);
}

function handleUpdate(row: any): any {
  open.value = true;
  id.value = row.id;
  title.value = t('jbx.text.update')
}


getAppList();
getAccountList();
getOrgTree();
</script>

<style lang="scss" scoped>
.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.button-top {
  margin-bottom: 10px;
}

.appTable :deep(.el-table__header-wrapper  .el-checkbox) {
  display:none
}

</style>
