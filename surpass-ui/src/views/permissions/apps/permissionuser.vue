<template>
  <div class="app-container">
    <el-card class="common-card">
      {{ $t('jbx.message.cheack.node') }}：{{appName}}
    </el-card>
    <el-card class="common-card">
      <el-tabs v-model="activeName" class="demo-tabs"  @tab-change="onGo">
        <el-tab-pane :label="$t('jbx.menu.permissions.groups')" name="permission"></el-tab-pane>
        <el-tab-pane :label="$t('jbx.menu.permissions.users')" name="permissionuser"></el-tab-pane>
      </el-tabs>
      <el-form :model="queryParams" ref="queryRef" :inline="true" style="margin-bottom: 10px">
        <el-form-item :label="$t('jbx.users.username') + '：'" prop="username">
          <el-input
              v-model="queryParams.username"
              placeholder=""
              clearable
              style="width: 140px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item :label="$t('jbx.users.displayName') + '：'" prop="displayName">
          <el-input
              v-model="queryParams.displayName"
              placeholder=""
              clearable
              style="width: 140px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}
          </el-button><!-- type="primary" -->
          <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-table v-loading="loading" :data="list" highlight-current-row @row-click="changeRow">
            <el-table-column :label="$t('jbx.users.username')"  prop="username" show-overflow-tooltip />
            <el-table-column :label="$t('jbx.users.displayName')"  prop="displayName" show-overflow-tooltip />
            <el-table-column :label="$t('jbx.users.mobile')"  prop="mobile"  show-overflow-tooltip />
            <el-table-column :label="$t('jbx.users.email')"  prop="email"  show-overflow-tooltip />
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
          <el-button
              type="primary"
              size="small"
              @click="submitForm"
              :disabled="leftObj == undefined"
          >{{ $t('jbx.text.save') }}
          </el-button>
          <span v-if="leftObj!= undefined" style="margin-left: 10px">
            <el-tag type="info">已选择：{{leftObj.username +"("+leftObj.displayName+")"}}</el-tag>
          </span>
          <div class="ch">

           <span>
            <el-radio-group v-model="checkStrictly">
              <el-radio :value="false" size="large">遵循父子规则</el-radio>
              <el-radio :value="true" size="large">不遵循父子规则</el-radio>
            </el-radio-group>
             <span class="md">遵循父子规则，勾选父节点，子节点自动勾选，不遵循父子规则，勾选父节点，子节点不会勾选</span>
           </span>
          </div>

          <el-tree
              ref="resTreeRef"
              node-key="key"
              :check-strictly="checkStrictly"
              :data="deptOptions"
              :props="defaultProps"
              :default-checked-keys="cheackdData"
              :default-expanded-keys="[appId]"
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
        </el-col>
      </el-row>

    </el-card>
  </div>
</template>

<script setup name="Permissions-Apps" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  userList,
} from "@/api/idm/users";
import {
  getResourceByUser,
  saveUsersAuthz,
  tree
} from "@/api/permissions/permission";


import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const { t } = useI18n()
const router: any = useRouter();
const activeName: any = ref("permissionuser")
const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
//树型规则，false=遵循父子规则，true=不遵循
const checkStrictly= ref(true);
const leftObj: any = ref(undefined)
const total: any = ref(0);
const title: any = ref("");

//右侧树型勾选集合
const cheackdData: any = ref<any>([]);
const route: any = useRoute();
//获取路由参数
const {appId,appName } = route.query;

const resTreeRef: any = ref<any>({});

const { group_category } = proxy.useDict("group_category");

const deptOptions: any = ref<any>([]);
const defaultProps: any = ref({
  children: "children",
  label: "title"
});
//默认展开节点
const treeData: any = ref<any>([]);

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    displayName:undefined,
    username: undefined
  }
});

const { queryParams } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  userList(queryParams.value).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.records;
      total.value = res.data.total;
    }
  });
}

function bulidTree(rootNode: any, nodes: any): any {
  let treeNodes: any = [];
  for (let node of nodes) {
    if (node.key != rootNode.key && node.parentKey == rootNode.key) {
      let nodeType: any = node.attrs && node.attrs.type ? node.attrs.type : '';
      let treeNode: any = { title: node.title, key: node.key, type: nodeType, expanded: false, isLeaf: true };
      bulidTree(treeNode,nodes);
      treeNodes.push(treeNode);
      rootNode.isLeaf = false;
    }
  }
  rootNode.children = treeNodes;
  return [rootNode];
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


function onGo(): any {
  let otherQueryParams: any = {
    appId: appId,
    appName: appName
  }
  router.push({path: "/permissions/apps/"+activeName.value, query: otherQueryParams});
};

function changeRow(row: any): any {
  leftObj.value = row;
  checkStrictly.value = true;
  cheackdData.value = [];
  let param: any = {
    appId: appId,
    userId: row.id
  }
  loading.value = true;
  getResourceByUser(param).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      for (let i in res.data) {
        cheackdData.value.push(res.data[i].resourceId)
      }
      resTreeRef.value.setCheckedKeys(cheackdData.value)
    }
  });
}

function loadTree(): any {
  let params: any = {
    appId:appId,
    appName:appName
  }
  tree(params).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      let rootNode: any = {
        title: res.data.rootNode.title,
        key: res.data.rootNode.key,
      };
      deptOptions.value = bulidTree(rootNode,res.data.nodes)
    }
  });
}

function submitForm(): any {
  //声明选择权限的ID集合
  let resourceIds: any = [];
  //获取树选中节点集合
  let treeData: any = resTreeRef.value.getCheckedNodes();
  for (let i in treeData) {
    //添加到选择集合中
    resourceIds.push(treeData[i].key)
  }
  let data: any = {
    appId: appId,
    userId: leftObj.value.id,
    resourceIds: resourceIds
  }
  loading.value = true;
  saveUsersAuthz(data).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(t('jbx.alert.operate.error'));
    }
  });
}


//加载左侧分页
getList();

//加载右侧树
loadTree();

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
