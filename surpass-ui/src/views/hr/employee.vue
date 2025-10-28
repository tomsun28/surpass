<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="姓名" prop="displayName">
            <el-input
                v-model="queryParams.displayName"
                placeholder="请输入姓名"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="工号" prop="employeeNumber">
            <el-input
                v-model="queryParams.employeeNumber"
                placeholder="请输入工号"
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
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ t('org.button.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="handleDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-row :gutter="20">
        <el-col :xs="8" :sm="6" :md="6" :lg="4" :xl="4">
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
          <el-table v-loading="loading" :data="employeeList" border @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55" align="center"/>
            <el-table-column prop="employeeNumber" label="工号" align="center" min-width="100"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="displayName" label="姓名" align="center" min-width="60"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="departmentName" label="所属部门" align="center" min-width="80"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column prop="jobTitle" label="职务" align="center" min-width="80"
                             :show-overflow-tooltip="true">
            </el-table-column>
            <el-table-column label="性别" align="center" prop="gender" min-width="45">
              <template #default="scope">
                <span>{{ GendersEnum.getGenderName(scope.row.gender) }}</span>
              </template>
            </el-table-column>
            <el-table-column label="员工类型" align="center" prop="employeeType" min-width="80"
                             :show-overflow-tooltip="true">
              <template #default="scope">
                <dict-tag-number :options="employee_types" :value="scope.row.employeeType"/>
              </template>
            </el-table-column>
            <el-table-column label="员工状态" align="center" prop="employeeStatus" min-width="80"
                             :show-overflow-tooltip="true">
              <template #default="scope">
                <dict-tag-number :options="users_state" :value="scope.row.employeeStatus"/>
              </template>
            </el-table-column>
            <el-table-column prop="status" :label="t('org.status')" align="center" min-width="40">
              <template #default="scope">
                <span v-if="scope.row.status === 1"><el-icon color="green"><SuccessFilled
                    class="success"/></el-icon></span>
                <span v-if="scope.row.status === 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
              </template>
            </el-table-column>
            <el-table-column label="操作" align="center" fixed="right" width="80">
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
              v-show="total>0"
              :total="total"
              v-model:page="queryParams.pageNum"
              v-model:limit="queryParams.pageSize"
              @pagination="getList"
          />
        </el-col>
      </el-row>
    </el-card>
    <edit-form :title="title" :open="open"
               :form-id="id"
               :org-options="deptOptions"
               :users-id-type="users_idType"
               :defaultStandardId="queryParams.standardId"
               :employee_types="employee_types"
               :employee_statuses="users_state"
               @dialogOfClosedMethods="dialogOfClosedMethods"></edit-form>
  </div>
</template>

<script setup lang="ts">
import {listEmployee, delEmployee} from "@/api/system/hr/employee";
import {useI18n} from "vue-i18n";
import booksSetStore from "@/store/modules/bookStore";
import * as GendersEnum from "@/utils/enums/GendersEnum";
import * as EducationsEnum from "@/utils/enums/EducationsEnum";
import * as SysStatusEnum from "@/utils/enums/SysStatusEnum";
import * as IdTypeEnum from "@/utils/enums/IdTypeEnum";
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {parseTime} from "@/utils/Surpass";
import {getTree} from "@/api/system/dept";
import modal from "@/plugins/modal";
import editForm from "./employee/edit.vue"
import {getTypeName} from "@/utils/enums/IdTypeEnum";

const {proxy} = getCurrentInstance()!;
const {users_idType, users_state, employee_types}
    = proxy?.useDict("users_idType", "users_state", "employee_types");
const {t} = useI18n()
const booksSet = booksSetStore()
const employeeList = ref([]);
const open = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const id: any = ref(undefined);
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const total = ref(0);
const title = ref("");
const treeData: any = ref<any[]>([]);
const deptOptions: any = ref<any[]>([]);
const defaultProps = ref({
  children: "children",
  label: "name"
});

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
});

const {queryParams} = toRefs(data);
const currentDepartmentId = ref(null);

/** 查询员工信息列表 */
function getList() {
  loading.value = true;
  listEmployee(queryParams.value).then((response: any) => {
    employeeList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.displayName = undefined;
  queryParams.value.employeeNumber = undefined;
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

/** 新增按钮操作 */
function handleAdd() {
  title.value = "添加员工信息";
  id.value = undefined;
  open.value = true;
}

/** 修改按钮操作 */
function handleUpdate(row: any) {
  id.value = row.id;
  title.value = "修改员工信息";
  open.value = true;
}

/** 删除按钮操作 */
function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除员工信息编号为"' + _ids + '"的数据项？').then(function () {
    return delEmployee({listIds: [_ids]});
  }).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
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

/** 通过条件过滤节点  */
const filterNode: any = (value: any, data: any) => {
  if (!value) return true;
  return data.label.indexOf(value) !== -1;
};

/** 节点单击事件 */
function handleNodeClick(data: any): any {
  currentDepartmentId.value = data.id
  queryParams.value.departmentId = data.id;
  handleQuery();
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

getOrgTree();
getList();
</script>

<style lang="scss" scoped>

</style>
