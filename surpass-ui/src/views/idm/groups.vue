<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true"
                 @submit.native.prevent>
          <el-form-item :label="t('jbx.roles.name')">
            <el-input
                v-model="queryParams.roleName"
                clearable
                style="width: 200px"
                @keyup.enter="handleQuery"
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
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ t('org.button.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="onBatchDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-table
          border
          v-loading="loading"
          :data="groupList"
          @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="roleCode" :label="t('jbx.text.id')" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="roleName" :label="t('jbx.roles.name')" align="center" min-width="100"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column prop="category" :label="t('jbx.organizations.type')" align="center" min-width="80">
          <template #default="scope">
            <dict-tag :options="group_category_options" :value="scope.row.category"/>
          </template>
        </el-table-column>
        <el-table-column prop="description" :label="t('jbx.text.description')" align="center" min-width="250"
                         :show-overflow-tooltip="true"></el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center" width="80">
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="成员管理">
              <el-button link icon="User" @click="handleMember(scope.row)"></el-button>
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
          @pagination="getList"
      />
    </el-card>
    <edit-form :title="title" :open="open"
               :form-id="id"
               :deptOptions="deptOptions"
               :group_category_options="group_category_options"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
    <member-component :open="memberFlag" :title="title" :form-id="id"
                      :form-name="currentName"
                      :role_members_type="role_members_type"
                      :use_gender="user_gender_type"
                      @memberDrawerClose="memberDrawerClose"></member-component>
  </div>
</template>

<script setup lang="ts">
import {ref, getCurrentInstance, reactive, toRefs} from "vue";
import modal from "@/plugins/modal";
import {useI18n} from "vue-i18n";
import {deleteBatch, listGroup} from "@/api/system/group.js";
import editForm from "./group/edit.vue";
import {getTree} from "@/api/system/dept";
import {handleTree} from "@/utils/Jinbooks";
import {set2String} from "@/utils"
import memberComponent from './group/members.vue'
import {useRouter} from "vue-router";
import SvgIcon from "@/components/SvgIcon/index.vue";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;

const {role_members_type, user_gender_type, group_category_options, group_type}
    = proxy?.useDict("role_members_type", "user_gender_type", "group_category_options", "group_type");

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);
const groupList: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const title: any = ref("");
const id: any = ref(undefined);
const total: any = ref(0);
const ids: any = ref<any>([]);
const selectionlist: any = ref<any>([]);
const deptOptions: any = ref<any>([]);
//成员开关
const memberFlag: any = ref(false);
const currentName: any = ref('');
const router: any = useRouter(); // 获取路由实例

/**
 * 获取列表
 */
function getList(): any {
  listGroup(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      groupList.value = res.data.records;
      total.value = res.data.total;
    }
  })
}

/**
 * 查询
 */
function handleQuery(): any {
  queryParams.value.pageNumber = 1;
  getList();
}

/**
 * 重置
 */
function resetQuery(): any {
  queryParams.value.roleName = undefined;
  handleQuery();
}

function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function handleAdd(): any {
  id.value = undefined;
  title.value = t('jbx.text.add')
  open.value = true;
}

function handleUpdate(row: any): any {
  id.value = row.id;
  title.value = t('jbx.text.edit')
  open.value = true;
}

/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 获取组织树 */
function getOrgTree(): any {
  getTree().then((response: any) => {
    response.data.nodes.forEach((node: any) => {
      if (node.key === response.data.rootNode.key) {
        node.parentKey = null;
      }
    });
    deptOptions.value = handleTree(response.data.nodes, 'key', 'parentKey', 'children')
    deptOptions.value = deptOptions.value.filter((t: any) => {
      return response.data.rootNode.key == t.key
    })
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

/** 删除按钮操作 */
function handleDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.roleName + t('org.deleteTip2')).then(function () {
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

function memberDrawerClose(): any {
  memberFlag.value = false;
}

function handleMember(row: any): any {
  id.value = row.id;
  title.value = t('jbx.roles.member');
  memberFlag.value = true;
  currentName.value = row.roleName;
}

//跳转模块
function onNavToUrl(groupId: any, roleName: any): any {
  // 确保 router 实例存在并且是有效的
  if (router && typeof router.push === 'function') {
    router.push({path: '/access/access', query: {groupId, roleName}});
  } else {
    console.error('Router instance is not available.');
  }
}

getList();
getOrgTree();
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

</style>
