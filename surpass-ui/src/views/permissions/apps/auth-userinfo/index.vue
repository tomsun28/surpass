<template>
  <el-drawer
      v-model="drawer"
      :title="t('jbx.text.authUser')"
      size="1000px"
  >
    <div class="app-container1" style="margin-top: -10px">
      <el-form :model="queryParams" ref="queryRef"  :inline="true">
        <el-form-item :label="t('jbx.roles.member')" prop="memberName">
          <el-input
              v-model="queryParams.memberName"
              @keyup.enter="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{t('jbx.text.query')}}</el-button>
          <el-button @click="resetQuery">{{t('jbx.text.reset')}}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="10" class="mb8">
        <el-col :span="1.5">
          <el-button
              type="primary"
              @click="handleUser"
          >{{t('jbx.roles.addUser')}}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="primary"
              @click="handlePost"
          >{{t('jbx.roles.addPost')}}</el-button>
        </el-col>
        <el-col :span="1.5">
          <el-button
              type="danger"
              :disabled="multiple"
              @click="batchHandleDelete"
          >{{t('jbx.text.delete')}}</el-button>
        </el-col>
      </el-row>
      <!-- 表格数据 -->
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column :label="t('jbx.roles.member')" prop="memberName" :show-overflow-tooltip="true"  />
        <el-table-column :label="t('jbx.users.department')" prop="department" :show-overflow-tooltip="true" />
        <el-table-column :label="t('jbx.roles.type.type')"  width="150" >
          <template #default="scope">
            <span v-if="scope.row.type === 'USER'">{{t('jbx.roles.type.user')}}</span>
            <span v-if="scope.row.type === 'POST'">{{t('jbx.roles.type.post')}}</span>
          </template>
        </el-table-column>
        <el-table-column  :label="t('jbx.text.action')" align="center" width="100" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip :content="t('jbx.text.delete')" placement="top">
              <el-button link type="primary" icon="Delete"
                         @click="handleDelete(scope.row)" ></el-button>
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
    </div>

    <!--    侧边未授权用户-->
    <not-auth-user-info ref="notAuthUserInfoRef" @selectUser="selectUser"></not-auth-user-info>

    <!--    侧边未授权岗位-->
    <not-auth-post ref="notAuthPostRef" @selectPost="selectPost"></not-auth-post>

  </el-drawer>
</template>

<script setup name="group-auth-userinfo" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import NotAuthUserInfo from "@/views/permissions/apps/auth-userinfo/not-auth-userinfo/index"
import NotAuthPost from "@/views/permissions/apps/auth-userinfo/not-auth-post/index"

import {memberInRole, addMember, removeMember} from "@/api/permissions/rolesmember";

import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const router: any = useRouter();

const {proxy} = getCurrentInstance()!;

const notAuthUserInfoRef: any = ref(undefined);

const notAuthPostRef: any = ref(undefined);


const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);

const drawer: any = ref(false)
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

const data: any = reactive({
  form: {},
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    roleId: undefined,
    appId: undefined,
    memberName: undefined
  }
});
const { queryParams, form, rules } = toRefs(data);

//定义当前角色ID
const currentRoleId: any = ref(undefined)
//定义当前应用ID
const currentAppId: any = ref(undefined)

function authByGroupId(roleId: any, appId: any): any {
  currentAppId.value = appId;
  currentRoleId.value = roleId;
  queryParams.value.roleId = roleId;
  queryParams.value.appId = appId;
  drawer.value = true;
  getList()
}

function handleUser(): any {
  notAuthUserInfoRef.value.openUser(currentRoleId)
}

function handlePost(): any {
  notAuthPostRef.value.openPost(currentRoleId)
}


function selectUser(ids: any, usernames: any): any {
  let data: any = {
    roleId: currentRoleId.value,
    type: 'USER',
    memberIds: ids,
    memberNames: usernames
  }
  addMember(data).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
      handleQuery();
    } else {
      modal.msgError(t('jbx.alert.operate.error'));
    }
  });
}


function selectPost(ids: any, usernames: any): any {
  let data: any = {
    roleId: currentRoleId.value,
    type: 'POST',
    memberIds: ids,
    memberNames: usernames
  }
  addMember(data).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
      handleQuery();
    } else {
      modal.msgError(t('jbx.alert.operate.error'));
    }
  });
}
/** 查询分组列表 */
function getList(): any {
  loading.value = true;
  memberInRole(queryParams.value).then((res: any) =>  {
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
  queryRef?.value?.resetFields();;
  handleQuery();
}


/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) =>  item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

function batchHandleDelete(row: any): any {
  modal.confirm(t('systemNoticeDelete')).then(function () {
    removeMember(ids.value).then((res: any) =>  {
      loading.value = false;
      if (res.code === 0) {
        modal.msgSuccess(t('jbx.alert.operate.success'));
        handleQuery();
      } else {
        modal.msgError(t('jbx.alert.operate.error'));
      }
    });
  }).catch(() => {});
}
/** 删除按钮操作 */
function handleDelete(row: any): any {
  modal.confirm(t('systemNoticeDelete')).then(function () {
    removeMember(row.id).then((res: any) =>  {
      loading.value = false;
      if (res.code === 0) {
        modal.msgSuccess(t('jbx.alert.operate.success'));
        handleQuery();
      } else {
        modal.msgError(t('jbx.alert.operate.error'));
      }
    });
  }).catch(() => {});
}


defineExpose({
  authByGroupId
})

</script>
