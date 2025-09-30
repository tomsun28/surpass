<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="userParams" ref="queryForm" :inline="true" @submit.native.prevent>
          <el-form-item :label="$t('jbx.users.username')">
            <el-input
                v-model="userParams.username"
                clearable
                @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
            <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
    <el-card class="common-card">
      <div class="button-top">
        <div>
          <el-button
              @click="handleAdd"
              type="primary">
            {{ $t('jbx.text.add') }}
          </el-button>
          <el-button
              type="danger"
              :disabled="ids.length === 0"
              @click="onBatchDelete"
          >{{ t('org.button.deleteBatch') }}
          </el-button>
        </div>
        <div style="display: flex">
          <el-button
              type="success"
              plain
              @click="downloadTemplate('template')"
          >{{ $t('jbx.text.template') }}
          </el-button>
          <el-button
              @click="downloadTemplate('user')"
          >{{ $t('jbx.text.export') }}
          </el-button>
          <el-upload
              style="margin-left: 10px"
              action="fake"
              multiple
              accept=".xls,.xlsx,csv"
              :show-file-list="showFileList"
              :file-list="fileList"
              :before-upload="beforeUpload"
              name="excelFile"
              :http-request="importExcel"
          >
            <el-button>{{ $t('jbx.text.import') }}
            </el-button>
          </el-upload>
        </div>
      </div>


      <el-table
          border
          v-loading="loading"
          :data="usersList"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column prop="username" :label="$t('jbx.users.username')" header-align="center" align="left"
                         min-width="100" :show-overflow-tooltip="true"/>
        <el-table-column prop="displayName" :label="$t('jbx.users.displayName')" header-align="center"  align="left"
                         min-width="120" :show-overflow-tooltip="true"/>
        <el-table-column prop="gender" :label="t('jbx.users.gender')" align="center" min-width="60">
          <template #default="scope">
            <span v-if="scope.row.gender === 1">{{ t('jbx.users.genderFemale') }}</span>
            <span v-if="scope.row.gender === 2">{{ t('jbx.users.genderMale') }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="mobile" label="手机号" header-align="center"  align="left"
                         min-width="120" :show-overflow-tooltip="true"/>
        <el-table-column prop="email" label="邮箱" header-align="center"  align="left"
        min-width="120" :show-overflow-tooltip="true"/>
        <el-table-column prop="status" :label="$t('jbx.users.status')" align="center"
                         width="80">
          <template #default="scope">
            <a :title="$t('jbx.users.statusActive')" v-if="scope.row.status === 1">
              <el-icon color="green">
                <SuccessFilled
                    class="success"/>
              </el-icon>
            </a>
            <a :title="$t('jbx.users.statusInactive')" v-if="scope.row.status === 2">
              <el-icon color="grey">
                <WarningFilled/>
              </el-icon>
            </a>
            <a :title="$t('jbx.users.statusForbidden')" v-if="scope.row.status === 4">
              <el-icon color="grey">
                <RemoveFilled/>
              </el-icon>
            </a>
            <a :title="$t('jbx.users.statusLock')" v-if="scope.row.status === 5">
              <el-icon color="orange">
                <Lock/>
              </el-icon>
            </a>
            <a :title="$t('jbx.users.statusDelete')" v-if="scope.row.status === 9">
              <el-icon color="red">
                <CircleCloseFilled/>
              </el-icon>
            </a>
          </template>
        </el-table-column>
        <el-table-column prop="id" label="账套" align="center" width="80">
          <template #default="scope">
              <el-button link type="text" @click="bookDrawerOpen(scope.row)">
                <el-icon><Notebook /></el-icon>
              </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center" width="140">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="角色" v-if="scope.row.status === 1">
              <el-button link icon="Postcard" @click="groupDrawerOpen(scope.row)"></el-button>
            </el-tooltip>
            <el-dropdown v-if="scope.row.status !== 9" style="margin-left: 8px">
              <el-button link icon="MoreFilled"></el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <!--                      <el-dropdown-item v-if="scope.row.status === 1"
                                                          @click.native="onNavToUrl(scope.row.id, scope.row.username, 'accounts')">
                                          <span>{{ $t('jbx.text.accounts') }}</span>
                                        </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.status === 1" @click.native="groupDrawerOpen(scope.row)">
                    <span>角色</span>
                  </el-dropdown-item>                     
                  <el-dropdown-item v-if="scope.row.status === 1" @click.native="postDrawerOpen(scope.row)">
                                          <span>{{ $t('jbx.text.posts') }}</span>
                                        </el-dropdown-item>
                                        <el-dropdown-item v-if="scope.row.status === 1" @click.native="handleHats(scope.row)">
                                          <span>{{ $t('jbx.text.hats') }}</span>
                                        </el-dropdown-item>-->
                  <el-dropdown-item v-if="scope.row.status === 1" @click.native="handlePas(scope.row)">
                    <span>{{ $t('jbx.text.changepassword') }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.status === 1" @click.native="onUpdateStatus(scope.row.id,5)">
                    <span>{{ $t('jbx.text.lock') }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.status === 1" @click.native="onUpdateStatus(scope.row.id,4)">
                    <span>{{ $t('jbx.text.disable') }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.status === 2" @click.native="onUpdateStatus(scope.row.id,1)">
                    <span>{{ $t('jbx.text.enable') }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.status === 4" @click.native="onUpdateStatus(scope.row.id,1)">
                    <span>{{ $t('jbx.text.enable') }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item v-if="scope.row.status === 5" @click.native="onUpdateStatus(scope.row.id,1)">
                    <span>{{ $t('jbx.text.unlock') }}</span>
                  </el-dropdown-item>
                  <el-dropdown-item divided @click.native="onDelete(scope.row)">
                    <span style="color: red">{{ $t('jbx.text.delete') }}</span>
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      <pagination
          v-show="total > 0"
          :total="total"
          v-model:page="userParams.pageNumber"
          v-model:limit="userParams.pageSize"
          :page-sizes="userParams.pageSizeOptions"
          @pagination="getList"
      />
    </el-card>
    <user-edit :title="title" :open="open" :formId="id" @dialogOfClosedMethods="dialogOfClosedMethods"
               :users_state="users_state"
               :users_type="users_type" :status="status" :users_married="users_married"
               :users_id-type="users_idType"></user-edit>

    <!--修改密码对话框-->
    <el-dialog v-model="pasFlag" width="500" :title="title" align-center>
      <change-password ref="changePasRef" :pasFlag="pasFlag" :pasForm="pasForm"
                       @onSubmitSuccess="dialogAndDrawClose"></change-password>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitPass">{{ t('jbx.text.confirm') }}</el-button>
          <el-button @click="dialogAndDrawClose">{{ t('systemCancel') }}</el-button>
        </div>
      </template>
    </el-dialog>
    <!--用户岗位-->
    <postDrawer :title="title" :post-open="postOpen" @postDrawerClose="dialogAndDrawClose"
                :post_options="post_options" :form-id="id"></postDrawer>
    <!--用户组-->
    <user-group :title="title" :username="currentUsername" :form-id="id"
                @groupDrawerClose="dialogAndDrawClose" :group-open="groupOpen"></user-group>
    <!--用户兼职-->
    <user-hats :title="title" :form-id="id" :open="hatsOpen" @hatsDrawerClose="dialogAndDrawClose"></user-hats>

    <!--账套权限-->
    <book :title="title" :username="currentUsername" :form-id="id"
          @bookDrawerClose="dialogAndDrawClose" :book-open="bookOpen"></book>
  </div>
</template>
<script setup lang="ts">
import modal from "@/plugins/modal";
import {ref, getCurrentInstance, reactive, toRefs} from "vue";
import {useI18n} from "vue-i18n";
import {getTree} from "@/api/system/dept";
import {changeStatus, deleteBatch, exportUser, importUsers, listUsers} from "@/api/system/user";
import {RemoveFilled} from "@element-plus/icons-vue";
import UserEdit from "./user/edit.vue"
import changePassword from './user/changePas.vue'
import postDrawer from './user/post.vue'
import userGroup from './user/group.vue'
import book from './user/book.vue'
import userHats from './user/hats.vue'
import {useRouter} from "vue-router";
import SvgIcon from "@/components/SvgIcon/index.vue";

const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {users_type, users_state, status, users_idType, users_married, post_options}
    = proxy?.useDict("users_type", "users_state", "status", "users_idType", "users_married", "post_options");

const data: any = reactive({
  userParams: {
    pageSize: 10,
    pageNumber: 1,
    username: undefined,
    departmentId: undefined,
    pageSizeOptions: [10, 20, 50]
  },
  pasForm: {}
});

const {userParams, pasForm} = toRefs(data);
//组织树数据
const deptOptions: any = ref<any[]>([]);
const defaultProps: any = ref({
  children: "children",
  label: "name"
});
//默认展开节点
const treeData: any = ref<any[]>([]);
const loading: any = ref(true);
const usersList: any = ref<any[]>([]);
const selectionlist: any = ref<any[]>([]);
const ids: any = ref<any[]>([]);
const total: any = ref(0);
const title: any = ref("");
const open: any = ref(false);
const id: any = ref(undefined);
const currentUsername: any = ref(undefined);
//当前选中节点
const currentDepartmentId: any = ref(undefined);
const showFileList: any = ref(false);
const fileList: any = ref<any[]>([]);
//密码对话框设置
const pasFlag: any = ref(false);
//岗位
const postOpen: any = ref(false);
//用户组
const groupOpen: any = ref(false);
const bookOpen: any = ref(false);
//兼职
const hatsOpen: any = ref(false);
const router: any = useRouter(); // 获取路由实例
const changePasRef = ref<InstanceType<typeof changePassword> | null>(null);

function getList(): any {
  listUsers(userParams.value).then((res: any) => {
    if (res.code === 0) {
      usersList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

//新增
function handleAdd(): any {
  id.value = undefined;
  title.value = t('jbx.text.add')
  open.value = true;
}

//查询
function handleQuery(): any {
  userParams.value.pageNumber = 1;
  getList();
}

//重置
function resetQuery(): any {
  userParams.value.username = undefined;
  handleQuery();
}

function handleUpdate(row: any): any {
  id.value = row.id;
  title.value = t('org.titleEdit');
  open.value = true;
}

/** 用户修改或新增框关闭事件*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 通过条件过滤节点  */
const filterNode: any = (value: any, data: any) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 节点单击事件 */
function handleNodeClick(data: any): any {
  currentDepartmentId.value = data.id
  userParams.value.departmentId = data.id;
  handleQuery();
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  children?: TreeNode[]; // 子节点可能不存在
}


/** 获取组织树 */
function getOrgTree(): any {
  getTree().then((response: { data: TreeNode[] }) => {
    // 定义一个递归函数来遍历树形数据并获取所有层级的节点
    const traverse = (node: TreeNode, level: number) => {
      if (level <= 2) { // 只展开到第三级
        treeData.value.push(node.id);
      }
      if (node.children && node.children.length > 0) {
        node.children.forEach((child) => {
          traverse(child, level + 1);
        });
      }
    };
    response.data.forEach((rootNode) => {
      traverse(rootNode, 1);
    });
    deptOptions.value = response.data;
  });
}

/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    let setIds: any = set2String(ids.value);
    return deleteBatch(setIds);
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

function set2String(set: any): any {
  return Array.from(set).join(',');
}

/** 模板下载*/
function downloadTemplate(type: any): any {
  exportUser(type).then((data: any) => {
    const blob: any = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'});
    const url: any = URL.createObjectURL(blob);
    const a: any = document.createElement('a');
    a.href = url;
    a.download = 'user';
    a.click();
    URL.revokeObjectURL(a.href);
    a.remove();
  })
}

function beforeUpload(file: any): any {
  let regExp: any = file.name.replace(/.+\./, '');
  let lower: any = regExp.toLowerCase(); //把大写字符串全部转为小写字符串
  let suffix: any = ['xls', 'xlsx'];
  if (suffix.indexOf(lower) === -1) {
    return modal.msgWarning('请上传后缀名为 xls、xlsx 的附件 !');
  }
  let isLt2M: any = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    return modal.msgError('请上传文件大小不能超过 2MB 的附件 !');
  }
}

/** 导入操作*/
function importExcel(item: any): any {
  let formData: any = new FormData();
  formData.append("excelFile", item.file);
  importUsers(formData).then((res: any) => {
    if (res.code === 0) {
      getList()
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(res.message);
    }
  });
}

//跳转模块
function onNavToUrl(userId: any, username: any, navType: any): any {
  if (navType === 'accounts') {
    // 确保 router 实例存在并且是有效的
    if (router && typeof router.push === 'function') {
      router.push({path: '/accounts/accounts', query: {username, userId}});
    } else {
      console.error('Router instance is not available.');
    }
  }
}


function submitPass() {
  changePasRef.value?.submitForm();
}

/**
 * 密码修改
 * @param row
 */
function handlePas(row: any): any {
  pasForm.value = row;
  title.value = t('jbx.text.changepassword');
  pasFlag.value = true;
}

function postDrawerOpen(row: any): any {
  id.value = row.id;
  title.value = t('org.titleEdit')
  postOpen.value = true;
}

function handleHats(row: any): any {
  id.value = row.id;
  title.value = t('org.titleEdit')
  hatsOpen.value = true;
}

function groupDrawerOpen(row: any): any {
  id.value = row.id;
  currentUsername.value = row.username;
  groupOpen.value = true;
  title.value = t('jbx.roles.member')
}

function bookDrawerOpen(row: any): any {
  id.value = row.id;
  currentUsername.value = row.username;
  bookOpen.value = true;
  title.value = "账套"
  console.log("bookDrawerOpen");
}

function dialogAndDrawClose(): any {
  postOpen.value = false;
  pasFlag.value = false;
  groupOpen.value = false;
  pasFlag.value = false;
  hatsOpen.value = false;
  bookOpen.value = false;
}

function onUpdateStatus(userId: any, status: any): any {
  changeStatus({id: userId, status: status}).then((res: any) => {
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
      getList();
    } else {
      modal.msgError(t('jbx.alert.operate.error'));
    }
  });
}

/**
 * 删除用户
 */
function onDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.username + t('org.deleteTip2')).then(function () {
    return deleteBatch(row.id);
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}

// getOrgTree();
getList();
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
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
</style>
