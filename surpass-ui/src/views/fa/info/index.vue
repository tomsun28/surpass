<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item label="资产编码">
            <el-input v-model="queryParams.code"/>
          </el-form-item>
          <el-form-item label="资产名称">
            <el-input v-model="queryParams.assetName"/>
          </el-form-item>
          <el-form-item label="资产分类">
            <el-select
                v-model="queryParams.categoryId"
                filterable
                remote
                reserve-keyword
                :remote-method="remoteMethod"
                :loading="loadingSelect"
                @visible-change="handleVisibleChange"
                @scroll="handleScroll"
                popper-class="custom-select-dropdown"
                placeholder="请选择">
              <template #default>
                <div class="custom-dropdown-wrap" @scroll="handleScroll">
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                  />
                </div>
              </template>
            </el-select>
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
        <el-button type="primary" @click="handleAdd">新增</el-button>
        <el-button @click="onBatchDelete" :disabled="ids.length === 0" type="danger">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="dataList"
                border
                @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"/>
        <el-table-column fixed prop="code" label="资产编码" align="center" width="170"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="assetName" label="资产名称" align="center" width="170"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column fixed prop="categoryName" label="资产分类" align="center" width="170"
                         :show-overflow-tooltip="true">
        </el-table-column>
        <el-table-column prop="depreciationSubjectName" label="折旧方法" align="center" width="140"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag-number :options="depreciation_method" :value="scope.row.depreciationMethod"/>
          </template>
        </el-table-column>
        <el-table-column prop="assetStatus" label="资产状态" align="center" width="120"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag-number :options="assets_status" :value="scope.row.assetStatus"/>
          </template>
        </el-table-column>
        <el-table-column prop="custodianName" label="保管人" align="center" width="200"
                         :show-overflow-tooltip="true">
        </el-table-column>
  <!--        <el-table-column prop="departmentName" label="使用部门" align="center" width="200"
                           :show-overflow-tooltip="true">
          </el-table-column>-->
        <el-table-column label="凭证科目" align="center">
          <el-table-column prop="assetSubjectName" label="固定资产科目" align="center" width="200"
                           :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="depreciationSubjectName" label="累计折旧科目" align="center" width="200"
                           :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="expenseSubjectName" label="折旧费用科目" align="center" width="200"
                           :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="purchaserSubjectName" label="资产购入对方科目" align="center" width="200"
                           :show-overflow-tooltip="true">
          </el-table-column>
          <el-table-column prop="liquidationSubjectName" label="资产清理科目" align="center" width="200"
                           :show-overflow-tooltip="true">
          </el-table-column>
        </el-table-column>
        <el-table-column label="新增资产凭证" align="center" width="110"
                         :show-overflow-tooltip="true" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.assetVoucherId === null ||scope.row.assetVoucherId ===''"
                       type="text" @click="generateCurrentVoucher(scope.row,1)">
              生成
            </el-button>
            <el-button v-if="scope.row.assetVoucherId !== null && scope.row.assetVoucherId !==''"
                       type="text" @click="viewVoucher(scope.row.assetVoucherId)">
              查看
            </el-button>
            <el-button v-if="scope.row.assetVoucherId !== null &&scope.row.assetVoucherId !==''"
                       type="text" @click="deleteVoucher(scope.row,1)">
              删除
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="清理凭证" align="center" width="100"
                         :show-overflow-tooltip="true" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.cleanUpVoucherId === null ||scope.row.cleanUpVoucherId ===''"
                       type="text" @click="generateCurrentVoucher(scope.row,2)">
              生成
            </el-button>
            <el-button v-if="scope.row.cleanUpVoucherId !== null && scope.row.cleanUpVoucherId !==''"
                       type="text" @click="viewVoucher(scope.row.cleanUpVoucherId)">
              查看
            </el-button>
            <el-button v-if="scope.row.cleanUpVoucherId !== null &&scope.row.cleanUpVoucherId !==''"
                       type="text" @click="deleteVoucher(scope.row,2)">
              删除
            </el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="110" fixed="right">
          <template #default="scope">
            <el-button icon="Edit" link @click="handleUpdate(scope.row)"></el-button>
            <el-button icon="Delete" type="danger" link @click="handleDelete(scope.row)"></el-button>
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
               :methods="depreciation_method"
               :dept-options="deptOptions"
               :subject-options="subjectOptions"
               :assets-status="assets_status"
               @dialogOfClosedMethods="dialogOfClosedMethods"
    ></edit-form>
    <!-- 添加或修改凭证记录对话框 -->
    <el-drawer :title="voucherTitle" v-model="voucherOpen" :close-on-click-modal="false" @close="getList" size="1200px">
      <template #header>
        <h4>{{ voucherTitle }}</h4>
      </template>
      <voucher-edit v-if="voucherOpen" v-model="voucherForm" :edit="!voucherPreviewMode" :dialog="true" :auto="false"
      ></voucher-edit>
    </el-drawer>
  </div>
</template>
<script setup lang="ts">
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import {useI18n} from "vue-i18n";
import {delLead, fetchPage, generateVoucher, deleteVoucherSubmit} from "@/api/fa/info";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import {useRouter} from "vue-router";
import EditForm from "./edit.vue"
import {getTree} from "@/api/system/standard/standard-subject";
import bookStore from "@/store/modules/bookStore";
import {TreeNode} from "@/types/commnon";
import * as deptApi from "@/api/system/dept.js";
import * as categoryApi from "@/api/fa/category"
import * as voucherApis from "@/api/system/voucher/voucher";
import voucherEdit from "@/views/voucher/voucher-edit.vue";

const currBookStore = bookStore()
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {depreciation_method, assets_status}
    = proxy?.useDict("depreciation_method", "assets_status");
const router = useRouter()

const data: any = reactive({
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  form: {},
  rules: {},
  voucherForm: {},
});
const dataList = ref([]);
const open = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const {queryParams, form, rules, voucherForm} = toRefs(data);
const deptOptions: any = ref<any[]>([]);
const subjectOptions: any = ref<any[]>([]);
const voucherOpen = ref(false);
const voucherPreviewMode = ref(false);

// 选项列表
const options = ref<any>([])
// 是否正在加载
const loadingSelect = ref(false)
const totalSelect = ref(0);
const voucherTitle = ref("");

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.code = undefined;
  queryParams.value.assetName = undefined;
  queryParams.value.categoryId = undefined;
  handleQuery();
}

function getList() {
  loading.value = true;
  fetchPage(queryParams.value).then((response: any) => {
    dataList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

/*关闭抽屉*/
function dialogOfClosedMethods(val: any): any {
  open.value = false;
  id.value = undefined;
  if (val) {
    getList();
  }
}

function handleUpdate(row: any) {
  id.value = row.id;
  title.value = "修改资产信息";
  open.value = true;
}

function handleShow(row: any) {
  router.push({path: '/lead/detail', query: {id: row.id}});
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除资产ID为"' + _ids + '"的数据项？').then(function () {
    return delLead({listIds: [_ids]});
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


/** 多选删除操作*/
function onBatchDelete(): any {
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return delLead({listIds: ids.value});
  }).then((res: any) => {
    if (res.code === 0) {
      handleQuery();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(res.message);
    }
  }).catch(() => {
  });
}

function handleAdd() {
  id.value = undefined;
  title.value = "新增资产信息";
  open.value = true;
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}


function getSetTree() {
  getTree({bookId: currBookStore.bookId}).then((response: { data: TreeNode[] }) => {
    subjectOptions.value = response.data;
  });
}

/** 获取组织树 */
function getOrgTree(): any {
  deptApi.getTree().then((response: { data: TreeNode[] }) => {
    deptOptions.value = response.data;
  });
}


let pageNumber = 1;
let pageSize = 10;
let currentKeyword = '';
// 模拟接口：获取数据
const fetchOptions = async (keyword = '', append = false) => {
  loadingSelect.value = true

  try {
    // 你需要替换为你实际的接口请求
    const response = await categoryApi.fetchPage({ keyword, pageNumber, pageSize })

    totalSelect.value = response.data.total
    const data = response.data.records.map((item: any) => ({
      label: item.categoryName,
      value: item.id
    }))
    options.value = append ? [...options.value, ...data] : data
  } finally {
    loadingSelect.value = false
  }
}

// 打开下拉框时加载第一页数据
const handleVisibleChange = (visible: any) => {
  if (visible && options.value.length === 0) {
    pageNumber = 1
    fetchOptions('', false)
  }
}
// 搜索触发
const remoteMethod = (query: any) => {
  currentKeyword = query
  pageNumber = 1
  fetchOptions(query, false)
}
// 下拉滚动加载更多
const handleScroll = (e: any) => {
  const scrollBottom = e.target.scrollHeight - e.target.scrollTop - e.target.clientHeight
  if (scrollBottom <= 5 && options.value.length < totalSelect.value && !loadingSelect.value) {
    pageNumber++
    fetchOptions(currentKeyword, true)
  }
}

function generateCurrentVoucher(row: any, voucherType: number) {
  generateVoucher({id: row.id, voucherType: voucherType}).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess("操作成功");
      viewVoucher(res.data)
    }
  });
}

/** 查看操作 */
function viewVoucher(voucherId: any) {
  voucherApis.getOneVoucher(voucherId).then((response: any) => {
    response.data.items.forEach((t: any) => {
      t.subjectCode = t.subjectName.split("-")[0]
      // t.detailedSubjectCode = t.detailedAccounts.split("-")[0]
    })
    voucherForm.value = response.data
    voucherOpen.value = true;
    voucherPreviewMode.value = false;
    voucherTitle.value = "凭证记录";
  });
}

/*删除凭证*/
function deleteVoucher(row: any, voucherType: number) {
  deleteVoucherSubmit({id: row.id, voucherType: voucherType}).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess("操作成功");
    }
  });
}

getList();
getSetTree();
getOrgTree();
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 10px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.custom-dropdown-wrap {
  max-height: 200px;
  overflow-y: auto;
}
</style>
