<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch"
                 @submit.native.prevent>
          <el-form-item :label="t('org.name')">
            <el-input
                v-model="queryParams.orgName"
                :placeholder="t('org.placeholder.name')"
                clearable
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
      <div class="button-top">
        <div>
          <el-button
              type="primary"
              @click="handleAdd"
          >{{ t('org.button.add') }}
          </el-button>
          <el-button
              type="danger"
              @click="onBatchDelete"
              :disabled="ids.length === 0"
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
              @click="downloadTemplate('org')"
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

      <el-row :gutter="20">
        <el-col :xs="8" :sm="6" :md="6" :lg="4" :xl="4">
          <!--          <el-row style="display: flex;justify-content: center">
                      <span style="line-height: 30px;" @click="handleClick" class="clickable">{{ t('org.organization') }}</span>
                    </el-row>-->
          <el-tree
              style="width: 100%;margin-top: 10px"
              node-key="id"
              :data="deptOptions"
              :props="defaultProps"
              :expand-on-click-node="false"
              :filter-node-method="filterNode"
              ref="tree"
              :default-expanded-keys="treeData"
              @node-click="handleNodeClick"
              highlight-current
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
        <el-col :xs="16" :sm="18" :md="18" :lg="20" :xl="20">
          <!--          <div v-if="currentDepartmentName"
                         style="height: 10px;font-size: 18px;">
                      {{ `【${currentDepartmentName}】` }}
                    </div>-->
          <el-table
              v-loading="loading"
              :data="deptList"
              border
              @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column prop="orgCode" :label="t('jbx.organizations.code')" align="left" min-width="100"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="orgName" :label="t('org.name')" align="left" min-width="100"
                             :show-overflow-tooltip="true"></el-table-column>
            <el-table-column prop="type" :label="t('jbx.organizations.type')" align="center" min-width="60">
              <template #default="scope">
                <dict-tag :options="org_type" :value="scope.row.type"/>
              </template>
            </el-table-column>
            <el-table-column prop="sortIndex" :label="t('org.sort')" align="center"
                             min-width="40">
            </el-table-column>
            <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
              <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
                <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
              </template>
            </el-table-column>
            <el-table-column prop="actions" :label="t('org.operate')" align="center" width="80">
              <template #default="scope">
                <el-tooltip content="编辑">
                  <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
                </el-tooltip>
                <el-tooltip v-if="scope.row.parentId != null
              && scope.row.parentId != '-1'
              && scope.row.parentId != '0'
              && scope.row.id != scope.row.instId" content="移除">
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
              :page-sizes="queryParams.pageSizeOptions"
              @pagination="getList"
          />
        </el-col>
      </el-row>
    </el-card>
    <!--新增或修改对话框-->
    <OrgEdit :title="title" :open="open" :formId="id" @dialogOfClosedMethods="dialogOfClosedMethods"
             :org-type="org_type" :deptOptions="deptOptions" :currentTreeId="currentDepartmentId"
             :currentTreeParentId="idParent" :current-tree-inst-id="idInst"></OrgEdit>
  </div>
</template>

<script setup name="Dept" lang="ts">
import {
  listDept,
  getTree,
  deleteBatch, exportOrgs, importOrgs
} from "@/api/system/dept.js";
import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
} from "vue";
import modal from "@/plugins/modal"
import OrgEdit from "./organizations/edit.vue";
import {useI18n} from "vue-i18n";
const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const {org_type} = proxy?.useDict("org_type");

const deptList: any = ref<any[]>([]);
const open: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const title: any = ref("");
const id: any = ref(undefined);
const idParent: any = ref(undefined);
const idInst: any = ref(undefined);
const total: any = ref(0);
const deptOptions: any = ref<any[]>([]);
const defaultProps: any = ref({
  children: "children",
  label: "name"
});
//默认展开节点
const treeData: any = ref<any[]>([]);//当前选中节点
const currentDepartmentId: any = ref(undefined);
const currentDepartmentName: any = ref('');
const selectionlist: any = ref<any[]>([]);
const ids: any = ref<any[]>([]);
const showFileList: any = ref(false);
const fileList: any = ref<any[]>([]);

/** 通过条件过滤节点  */
const filterNode: any = (value: any, data: any) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

const data: any = reactive({
  queryParams: {
    orgName: undefined,
    parentId: undefined,
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  }
});

const {queryParams} = toRefs(data);

/** 查询部门列表 */
function getList(): any {
  loading.value = true;
  listDept(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      loading.value = false;
      deptList.value = res.data.records;
      total.value = res.data.total
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
  queryParams.value.orgName = undefined;
  handleQuery();
}

/** 新增按钮操作 */
function handleAdd(): any {
  id.value = undefined;
  idParent.value = undefined;
  idInst.value = undefined;
  title.value = t('org.titleAdd')
  open.value = true;
}

/** 节点单击事件 */
function handleNodeClick(data: any): any {
  currentDepartmentId.value = data.id
  currentDepartmentName.value = data.name
  queryParams.value.parentId = data.id;
  handleQuery();
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  id.value = row.id;
  idParent.value = row.parentId;
  idInst.value = row.instId;
  title.value = t('org.titleEdit');
  open.value = true;
}

/** 组织修改或新增框关闭事件*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  idParent.value = undefined;
  idInst.value = undefined;
  if (val) {
    getList();
    getOrgTree();
  }
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  modal.confirm(t('org.deleteTip1') + row.orgName + t('org.deleteTip2'))
      .then(function () {
        return deleteBatch(row.id);
      }).then((res: any) => {
    if (res.code === 0) {
      getList();
      getOrgTree()
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  })
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


/** 多选操作*/
function handleSelectionChange(selection: any): any {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
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
      getOrgTree()
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
  exportOrgs(type).then((data: any) => {
    const blob: any = new Blob([data], {type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8'});
    const url: any = URL.createObjectURL(blob);
    const a: any = document.createElement('a');
    a.href = url;
    a.download = 'org';
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
  importOrgs(formData).then((res: any) => {
    if (res.code === 0) {
      getList()
      modal.msgSuccess(t('jbx.alert.operate.success'));
      getOrgTree()
    } else {
      modal.msgError(res.message);
    }
  });
}

getList();
getOrgTree();
</script>

<style lang="scss" scoped>
.clickable {
  line-height: 30px;
  cursor: pointer; /* 设置鼠标悬停时为指针样式 */
}

::v-deep .el-dialog {
  min-height: 100px;
}

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

.el-form-item--small.el-form-item {
  margin-bottom: 10px;
}

.button-top {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
</style>
