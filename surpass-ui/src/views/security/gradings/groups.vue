<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-tabs v-model="activeName" class="demo-tabs"  @tab-change="onGo">
        <el-tab-pane :label="$t('jbx.menu.gradings.organizations')" name="organizations"></el-tab-pane>
        <el-tab-pane :label="$t('jbx.menu.gradings.apps')" name="apps"></el-tab-pane>
        <el-tab-pane :label="$t('jbx.menu.gradings.groups')" name="groups"></el-tab-pane>
      </el-tabs>
      <el-form :model="queryParams" ref="queryRef" :inline="true" style="margin-bottom: 10px">
        <el-form-item :label="$t('jbx.text.groups') + '：'" prop="groupName">
          <el-input
              v-model="queryParams.groupName"
              placeholder=""
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}
          </el-button><!-- type="primary" -->
          <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20" style="min-height: 600px">
        <el-col :span="12">
          <el-table v-loading="loading" :data="list" highlight-current-row @row-click="changeRow">
            <el-table-column :label="$t('jbx.text.groups')"  prop="groupName"/>
            <el-table-column :label="$t('jbx.roles.category.category')" align="center">
              <template #default="scope">
                <el-button  type="text">
                  <dict-tag :options="group_category" :value="scope.row.category"></dict-tag>
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <pagination
              v-show="total > 0"
              :total="total"
              layout="prev, pager, next, total"
              v-model:page="queryParams.pageNumber"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
          />
        </el-col>
        <el-col :span="12" style="margin-top: -40px;">
          <div class="btn-form" style="margin-bottom: 7px">
            <el-button
                type="primary"
                size="small"
                :disabled="leftObj == undefined"
                @click="add"
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

            <span v-if="leftObj!= undefined" style="margin-left: 10px">
            <el-tag type="info">{{ $t('jbx.message.cheack.node') }}：{{leftObj.groupName}}</el-tag>
          </span>
          </div>

          <el-table v-loading="loading" :data="rightList" @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column :label="$t('jbx.text.groups')"  prop="gradingName"/>
            <el-table-column :label="$t('jbx.text.action')" align="center"
                             class-name="small-padding fixed-width" width="120" >
              <template #default="scope">
                <el-button  type="danger" @click="handleDelete(scope.row)" >{{ $t('jbx.text.delete') }}</el-button>
              </template>
            </el-table-column>
          </el-table>

          <pagination
              v-show="rightTotal > 0"
              :total="rightTotal"
              layout="prev, pager, next, total"
              v-model:page="queryAuthParams.pageNumber"
              v-model:limit="queryAuthParams.pageSize"
              @pagination="getRightList"
          />
        </el-col>
      </el-row>

    </el-card>

    <!--  弹出未授权用户组列表 -->
    <not-auth-groups ref="notAuthGroupsRef" @selectGroups="selectGroups"></not-auth-groups>
  </div>
</template>

<script setup name="security-gradings-groups" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import NotAuthGroups from "@/views/security/gradings/not-auth-groups/index"

import {
  listGroup,
} from "@/api/idm/groups";
import {
  apiGroupsGet,
  apiGroupsDeleteByIds,
  apiGroupAuthzGroups
} from "@/api/security/gradings";


import {useI18n} from "vue-i18n";


const {proxy} = getCurrentInstance()!;

const { t } = useI18n()

const router: any = useRouter();

const activeName: any = ref("groups")

const { group_category } = proxy.useDict("group_category");

const notAuthGroupsRef: any = ref(undefined);

const list: any = ref<any>([]);
const rightList: any = ref<any>([]);
const rightTotal: any = ref(0);
const open: any = ref(false);
const loading: any = ref(true);

const leftObj: any = ref(undefined)
const total: any = ref(0);
const title: any = ref("");
const route: any = useRoute();
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    groupName: undefined
  },
  queryAuthParams: {
    pageNumber: 1,
    pageSize: 10,
    groupName: undefined,
    groupId: undefined,
  }
});

const { queryParams,queryAuthParams } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  listGroup(queryParams.value).then((res: any) =>  {
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

/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) =>  item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 重置按钮操作 */
function resetQuery(): any {
  queryRef?.value?.resetFields();;
  handleQuery();
}

function add(): any {
  notAuthGroupsRef.value.openApps(leftObj.value.id);
}

function onGo(): any {
  router.push({path: "/security/gradings/"+activeName.value});
};

function changeRow(row: any): any {
  leftObj.value = row;
  queryAuthParams.value.pageNumber = 1;
  getRightList();
}


function getRightList(): any {
  queryAuthParams.value.groupId = leftObj.value.id;
  queryAuthParams.value.groupName =  leftObj.value.groupName;
  loading.value = true;
  apiGroupsGet(queryAuthParams.value).then((res: any) =>  {
    loading.value = false;
    rightList.value = res.data.records;
    rightTotal.value = res.data.total;
  });
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return apiGroupsDeleteByIds(id);
  }).then(() => {
    getRightList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {});
}

/** 授权应用 */
function selectGroups(ids: any): any {
  let data: any = {
    groupId: leftObj.value.id,
    gradings: ids
  }
  apiGroupAuthzGroups(data).then((res: any) =>  {
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
      getRightList();
    }
  });
}

//加载左侧分页
getList();



</script>
<style scoped>
::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}
.common-card{
  margin-bottom: 15px;
}
::v-deep(.common-card form .el-form-item--default){
  margin-bottom: 0px;
}
.auths{
  display: flex;
}
.auths .cleft{
  width: 50%;
}
.md{
  color:#ccc;font-size:12px;margin-left: 20px
}


</style>
