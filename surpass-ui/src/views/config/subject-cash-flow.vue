<template>
  <div class="app-container">
    <el-row :gutter="20" class="main-container">
      <el-col :span="8" class="scrollable-content">
        <el-card class="common-card scroll-inner">
<!--          <el-row>
            <el-form :model="queryParams" ref="queryRef" :inline="true"
                     @submit.native.prevent>
              <el-form-item label="会计准则">
                <el-select v-model="queryParams.standardId" @change="handleQuery" style="width: 200px">
                  <el-option
                      v-for="item in standardList"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id">
                  </el-option>
                </el-select>
              </el-form-item>
            </el-form>
          </el-row>-->
          <el-row>
            <span style="font-size: 14px; font-weight: bold;margin-bottom: 10px">会计科目</span>
          </el-row>
          <el-row class="scroll-tree">
            <template v-if="deptOptions && deptOptions.length > 0">
              <el-tree
                  highlight-current
                  ref="resTreeRef"
                  :data="deptOptions"
                  :props="defaultProps"
                  check-strictly
                  @node-click="handleNodeClick"
                  value-key="id"
                  v-slot="{ node, data }"
              >
                <span>
                  <span v-if="node.label.length <= 20">{{ node.label }}</span>
                  <span v-else>
                    <el-tooltip effect="dark" :content="node.label" placement="right">
                    <span>{{ node.label.slice(0, 20) + '...' }}</span>
                  </el-tooltip>
                  </span>
                </span>
              </el-tree>
            </template>
            <template v-else>
              <div class="no-data-tip">暂无数据</div>
            </template>
          </el-row>
        </el-card>
      </el-col>
      <el-col :span="16" class="fixed-sidebar">
        <el-card class="common-card sidebar-inner">
          <el-row>
            <div v-if="currentSubjectName" style="font-size: 18px">
              {{ currentSubjectName ? `【${currentSubjectName}】` : '' }}
              <hr style="border: none; border-top: 1px solid #ccc; margin-top: 8px;"/>
            </div>
          </el-row>
          <el-row>
            <div style="background-color: #f0f8ff; width: 100%;height: 30px;">
              <span style="font-weight: bold;line-height: 30px">现金流量项目配置</span>
            </div>
          </el-row>
          <el-row class="mt10">
            <el-radio-group v-model="direction" @change="changeDirection">
              <el-radio label="借方" :value="1" border></el-radio>
              <el-radio label="贷方" :value="2" border></el-radio>
            </el-radio-group>
          </el-row>
          <el-row class="main-row">
            <span class="common-font">现金流量主表项目</span>
            <el-table :data="mainTableList" class="main-table"
                      @select="handleSelect"
                      ref="singleTable">
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column prop="itemName" align="left">
              </el-table-column>
            </el-table>
          </el-row>
          <el-row class="main-row">
            <span class="common-font">现金流量补充资料项目</span>
            <el-table :data="supplementaryList" class="main-table"
                      @select="handleSelectSupple"
                      ref="singleTableSupple">
              <el-table-column type="selection" width="55"></el-table-column>
              <el-table-column prop="itemName" align="left">
              </el-table-column>
            </el-table>
          </el-row>
          <el-row class="mt10" justify="end">
            <el-button type="primary" @click="submit" :disabled="currentSubjectName === ''">保存</el-button>
          </el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import {reactive, toRefs, ref, nextTick} from "vue";
import {listStandardsAll} from "@/api/system/standard/standard";
import {getTree} from "@/api/system/standard/standard-subject";
import {fetchSelectItem, getSelectItem, saveSelectItem} from "@/api/config/cash-flow-balance";
import bookStore from "@/store/modules/bookStore";
import modal from "@/plugins/modal";
import {checkCashFlowSettingPermission} from "@/utils/CashFlowRule"

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    category: 1,
    pageSizeOptions: [10, 20, 50]
  }
});
const currBookStore = bookStore()
const deptOptions: any = ref<any[]>([]);
const resTreeRef: any = ref<any>({});
const currentSubjectName = ref("")
const currentSubject = ref({})
const mainTableList = ref([])
const supplementaryList = ref([])
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name'
})
const {queryParams} = toRefs(data);
//会计准则
const standardList: any = ref<any>([]);
const direction = ref(1)
const singleTable: any = ref<any>({});
const singleTableSupple: any = ref<any>({});
//所选的主表项目
const selectedRow = ref(null)
//所选的补充资料项目
const selectedRowSupple = ref(null)

/*获取准则列表*/
function getStandardList() {
  listStandardsAll({}).then((res: any) => {
    if (res.code === 0) {
      if (Array.isArray(res.data) && res.data.length > 0) {
        standardList.value = res.data;
        queryParams.value.standardId = standardList.value[0].id;
      } else {
        // 如果数据为空数组时，确保有默认处理逻辑
        standardList.value = [];
        queryParams.value.standardId = null; // 或设置为适当的默认值
      }
      getSubjectTree();
    }
  });
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  children?: TreeNode[]; // 子节点可能不存在
}

function getSubjectTree(): any {
 /* getTree({standardId: queryParams.value.standardId}).then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
  });*/
  getTree({bookId: currBookStore.bookId}).then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
  });
}

function handleQuery() {
  queryParams.value.pageNumber = 1;
  getSubjectTree();
}

/** 节点单击事件*/
function handleNodeClick(data: any) {
  currentSubjectName.value = data.name;
  currentSubject.value = data;
  // 使用异步操作确保 this.currentId 被赋值后再调用 getList
  nextTick(() => {
    clearSelected();
    selectedItem();
  });
}

function getCashFlowItem() {
  getSelectItem({cashFlowItemType: 0}).then((response: any) => {
    if (response.code === 0) {
      mainTableList.value = response.data.map((item: any) => {
            return item.itemCode === "35-hl-djje" && item.itemName.startsWith("四、")
                ? {...item, itemName: item.itemName.replace(/^四、/, "")}
                : item;
          }
      );
    }
  })
  getSelectItem({cashFlowItemType: 1}).then((response: any) => {
    if (response.code === 0) {
      supplementaryList.value = response.data.map((item: any) => {
            return item;
          }
      );
    }
  })
}

function handleSelect(selection: any, row: any) {
  // 如果点击的是当前已选中的那一行，表示用户要取消
  if (selectedRow.value === row) {
    singleTable.value.clearSelection()
    selectedRow.value = null
  } else {
    singleTable.value.clearSelection()
    singleTable.value.toggleRowSelection(row, true)
    selectedRow.value = row
  }
}

function handleSelectSupple(selection: any, row: any) {
  // 如果点击的是当前已选中的那一行，表示用户要取消
  if (selectedRowSupple.value === row) {
    singleTableSupple.value.clearSelection()
    selectedRowSupple.value = null
  } else {
    singleTableSupple.value.clearSelection()
    singleTableSupple.value.toggleRowSelection(row, true)
    selectedRowSupple.value = row
  }
}

function changeDirection() {
  clearSelected();
  selectedItem();
}

function selectedItem() {
  //查询所选
  const params = {
    subjectCode: currentSubject.value.code,
    direction: direction.value,
  }
  fetchSelectItem(params).then((response: any) => {
    const data = response.data;
    toggleRowByCode(data?.itemCodeMain, data?.itemCodeSupple);
  })
}

function toggleRowByCode(mainCode?: string, suppleCode?: string) {
  const selectRow = (list: any[], tableRef: any, code?: string, row: any) => {
    if (!code) return
    const target = list.find(item => item.itemCode === code)
    if (target) {
      tableRef?.toggleRowSelection?.(target, true)
      row.value = target;
    }
  }

  selectRow(mainTableList.value, singleTable.value, mainCode, selectedRow)
  selectRow(supplementaryList.value, singleTableSupple.value, suppleCode, selectedRowSupple)
}

function submit() {

  const result = checkCashFlowSettingPermission(currentSubject.value);

  if (!result.canSetMainTable && selectedRow.value) {
    modal.msgWarning(result.reason);
    return;
  }

  if (!result.canSetSupplementary && selectedRowSupple.value) {
    modal.msgWarning(result.reason);
    return;
  }

  if (result.canSetSupplementary && !result.canSetMainTable && selectedRow.value) {
    modal.msgWarning(result.reason);
    return;
  }

  if (result.canSetMainTable && !result.canSetSupplementary && selectedRowSupple.value) {
    modal.msgWarning(result.reason);
    return;
  }
  const dataDto = {
    subjectCode: currentSubject.value.code,
    itemCodeMain: selectedRow.value?.itemCode,
    itemCodeSupple: selectedRowSupple.value?.itemCode,
    direction: direction.value
  }


  saveSelectItem(dataDto).then((response: any) => {
    if (response.code === 0) {
      modal.msgSuccess("新增成功");
    } else {
      modal.msgError(response.message);
    }
  })
}

function clearSelected() {
  //清除所选
  singleTable.value.clearSelection()
  singleTableSupple.value.clearSelection()
  selectedRow.value = null;
  selectedRowSupple.value = null;
}

// getStandardList();
getSubjectTree();
getCashFlowItem();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.common-card {
  margin-bottom: 15px;
}

.main-container {
  height: 82vh;
  display: flex;
  overflow: hidden;
}

.main-row {
  border: 1px solid lightgray;
  margin-top: 10px;
  border-radius: 8px;
  padding: 10px;
}

.scrollable-content {
  height: 82vh;
}

.scroll-tree {
  height: 71vh;
  overflow-y: auto;
}

.scroll-inner {
  padding: 16px;
}

.fixed-sidebar {
  height: 82vh;
  overflow: hidden;
  background-color: #f5f5f5;
  border-left: 1px solid #ddd;
}

.sidebar-inner {
  position: sticky;
  top: 0;
  height: 82vh;
  padding: 16px;
}

:deep(.el-table) {
  /* 减小表格行高 */
  --el-table-row-height: 32px; /* 默认可能是 50px 左右，可以根据需要调整 */

  /* 减小单元格内边距 */
  td.el-table__cell {
    padding: 6px 0; /* 上下内边距减小 */
  }

  /* 完全隐藏表头 */
  .el-table__header-wrapper {
    display: none !important;
  }

  /*  !* 调整表头行高 *!
    th.el-table__cell {
      padding: 0; !* 表头可以稍微高一点 *!
      height: 0 !important;
    }*/

  /*  !* 隐藏表头复选框 *!
    .el-table__header-wrapper .el-checkbox {
      display: none;
    }*/
}

.common-font {
  font-size: 14px;
  font-weight: bold;
  margin-bottom: 10px;
}

.main-table {
  border: 1px solid lightgray;
  height: 23.5vh;
  overflow-y: auto;
}

.no-data-tip {
  font-size: 14px;
  margin-top: 50%;
  margin-left: 40%;
  color: #999;
}
</style>
