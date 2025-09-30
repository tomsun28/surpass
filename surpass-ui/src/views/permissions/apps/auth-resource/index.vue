<template>
  <el-drawer
      v-model="drawer"
      :title="t('jbx.text.authResource')"
      size="600px"
  >
    <div class="app-container1" style="margin-top: -10px">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item :label="t('jbx.roles.name')">
          <el-input v-model="currentRoleName" disabled  />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="menuExpand" @change="handleCheckedTreeExpand($event, 'menu')">{{t('jbx.text.expandOrFold')}}</el-checkbox>
          <el-checkbox v-model="menuNodeAll" @change="handleCheckedTreeNodeAll($event, 'menu')">{{t('jbx.text.selectAllOrNot')}}</el-checkbox>
          <el-checkbox v-model="menuCheckStrictly" @change="handleCheckedTreeConnect($event, 'menu')">{{t('jbx.text.connectChild')}}</el-checkbox>
        </el-form-item>
        <el-form-item>
          <el-tree
              ref="resTreeRef"
              node-key="key"
              :data="dataOptions"
              :props="defaultProps"
              :check-strictly="!menuCheckStrictly"
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
      </el-form>
    </div>
    <template #footer>
      <div style="flex: auto">
        <el-button type="primary" @click="submitForm">{{t('jbx.text.confirm')}}</el-button>
        <el-button @click="cancel">{{t('jbx.text.close')}}</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<script setup name="roles-auth-resource" lang="ts">
import { useRoute, useRouter } from "vue-router";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import { getResourceByRole,tree,saveRoleAuthz } from "@/api/permissions/permission";

import { useI18n } from 'vue-i18n'


const { t } = useI18n()

const router: any = useRouter();

const loading: any = ref(true);

const {proxy} = getCurrentInstance()!;
const drawer: any = ref(false)

const menuExpand: any = ref(true);
const menuNodeAll: any = ref(false);

const menuCheckStrictly: any = ref(false);

const resTreeRef: any = ref(undefined);

const dataOptions: any = ref<any>([]);
const defaultProps: any = ref({
  children: "children",
  label: "title"
});

const data: any = reactive({
  form: {},
  queryParams: {
    appId: undefined,
    roleId: undefined,
    username: undefined,
    displayName: undefined,
    mobile: undefined
  }
});

const { queryParams, form, rules } = toRefs(data);

//定义当前角色ID
const currentRoleId: any = ref(undefined)

//定义当前角色名称
const currentRoleName: any = ref(undefined)

//定义当前应用ID
const currentAppId: any = ref(undefined)

//定义当前应用名称
const currentAppName: any = ref(undefined)

function authByRole(roleId: any, RoleName: any, appId: any, appName: any): any {
  currentRoleId.value = roleId;
  currentRoleName.value = RoleName;
  currentAppId.value = appId;
  currentAppName.value = appName;
  queryParams.value.appId = appId;
  queryParams.value.roleId = roleId;
  drawer.value = true;
  loadTree()
}





/** 树权限（展开/折叠）*/
function handleCheckedTreeExpand(value: any, type: any): any {
  if (type == "menu") {
    let treeList: any = dataOptions.value;
    for (let i: any = 0; i < treeList.length; i++) {
      resTreeRef.value.store.nodesMap[treeList[i].key].expanded = value;
    }
  }
}

/** 树权限（全选/全不选） */
function handleCheckedTreeNodeAll(value: any, type: any): any {
  if (type == "menu") {
    resTreeRef.value.setCheckedNodes(value ? dataOptions.value : []);
  }
}

/** 树权限（父子联动） */
function handleCheckedTreeConnect(value: any, type: any): any {
  if (type == "menu") {
    menuCheckStrictly.value = value ? true : false;
  }
}

function loadTree(): any {
  let params: any = {
    appId: currentAppId.value,
    appName: currentAppName.value
  }
  tree(params).then((res: any) =>  {
    loading.value = false;
    if (res.code === 0) {
      let rootNode: any = {
        title: res.data.rootNode.title,
        key: res.data.rootNode.key
      };
      dataOptions.value = bulidTree(rootNode,res.data.nodes)
    }
    let radate: any = {
      appId: currentAppId.value,
      roleId: currentRoleId.value
    }
    nextTick(() => {
      resTreeRef.value.store.nodesMap[currentAppId.value].expanded = true;
    });
    //加载已授权的资源勾选状态
    getResourceByRole(radate).then((res2: any) =>  {
      if (res2.code === 0) {
        nextTick(() => {
          let checkedKeys: any = res2.data;
          checkedKeys.forEach((v: any) => {
            nextTick(() => {
              resTreeRef.value.setChecked(v.resourceId, true, false);
            });
          });
        });
      }
    });
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

/** 提交按钮 */
function submitForm(): any {
  form.value.appId = currentAppId.value;
  form.value.roleId = currentRoleId.value;
  form.value.resourceIds = getMenuAllCheckedKeys()
  saveRoleAuthz(form.value).then((res: any) =>  {
    if (res.code === 0) {
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(t('jbx.alert.operate.error'));
    }
  });
}

/** 所有菜单节点数据 */
function getMenuAllCheckedKeys(): any {
  // 目前被选中的菜单节点
  let checkedKeys: any = resTreeRef.value.getCheckedKeys();
  // 半选中的菜单节点
  let halfCheckedKeys: any = resTreeRef.value.getHalfCheckedKeys();
  checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
  return checkedKeys;
}


/** 重置新增的表单以及其他数据  */
function reset(): any {
  if (resTreeRef.value != undefined) {
    resTreeRef.value.setCheckedKeys([]);
  }
  menuExpand.value = false;
  menuNodeAll.value = false;
  form.value = {};
  formRef?.value?.resetFields();;
}
/** 取消按钮 */
function cancel(): any {
  drawer.value = false;
  reset();
}

defineExpose({
  authByRole
})


</script>
