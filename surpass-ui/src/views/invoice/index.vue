<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" label-width="68px">
          <el-form-item label="开票日期">
            <el-date-picker
                v-model="queryParams.invoiceDateRange"
                type="daterange"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期"
                value-format="YYYY-MM-DD"
            />
          </el-form-item>
          <el-form-item label="发票号码">
            <el-input
                v-model="queryParams.invoiceNo"
                placeholder="请输入发票号码"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="发票类型">
            <el-select v-model="queryParams.invoiceType" clearable style="width: 240px">
              <el-option
                  v-for="dict in invoice_types"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="发票代码">
            <el-input
                v-model="queryParams.invoiceCode"
                placeholder="请输入发票代码"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-row>
            <el-form-item>
              <el-radio-group v-model="queryParams.direction" @change="getList">
                <el-radio-button value="INPUT">进项发票</el-radio-button>
                <el-radio-button value="OUTPUT">销项发票</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item>
              <el-button @click="handleQuery">{{ t('org.button.query') }}</el-button>
              <el-button @click="resetQuery">{{ t('org.button.reset') }}</el-button>
            </el-form-item>
          </el-row>
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
        <el-table-column type="selection" width="50" align="center" fixed/>
        <el-table-column prop="invoiceDate" width="200" align="center" label="开票日期" fixed/>
        <el-table-column prop="invoiceNo" width="200" align="center" label="发票号码"
                         :show-overflow-tooltip="true"/>
        <el-table-column prop="invoiceCode" width="200" align="center" label="发票代码"
                         :show-overflow-tooltip="true"/>
        <el-table-column prop="invoiceType" label="发票类型" align="center" width="120"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag :options="invoice_types" :value="scope.row.invoiceType"/>
          </template>
        </el-table-column>
        <el-table-column
            width="200"
            align="center"
            :label="queryParams.direction === 'OUTPUT' ? '销方名称' : '购方名称'"
        >
          <template #default="scope">
            {{ queryParams.direction === 'OUTPUT' ? scope.row.sellerName : scope.row.buyerName }}
          </template>
        </el-table-column>
        <el-table-column
            width="200"
            align="center"
            :label="queryParams.direction === 'OUTPUT' ? '销方税号' : '购方税号'"
        >
          <template #default="scope">
            {{ queryParams.direction === 'OUTPUT' ? scope.row.sellerTaxNo : scope.row.buyerTaxNo }}
          </template>
        </el-table-column>
        <el-table-column prop="totalAmount" label="金额合计（不含税）" align="center" width="200"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.totalAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="taxAmount" label="税额" align="center" width="200"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.taxAmount) }}
          </template>
        </el-table-column>
        <el-table-column prop="totalWithTax" label="价税合计" align="center" width="200"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            {{ formatAmount(scope.row.totalWithTax) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="发票状态" align="center" width="120"
                         :show-overflow-tooltip="true">
          <template #default="scope">
            <dict-tag :options="invoice_status" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="发票凭证" align="center" width="110"
                         :show-overflow-tooltip="true" fixed="right">
          <template #default="scope">
            <el-button v-if="scope.row.invoiceVoucherId === null ||scope.row.invoiceVoucherId ===''"
                       type="text" @click="generateCurrentVoucher(scope.row)">
              生成
            </el-button>
            <el-button v-if="scope.row.invoiceVoucherId !== null && scope.row.invoiceVoucherId !==''"
                       type="text" @click="viewVoucher(scope.row.invoiceVoucherId)">
              查看
            </el-button>
            <el-button v-if="scope.row.invoiceVoucherId !== null &&scope.row.invoiceVoucherId !==''"
                       type="text" @click="deleteVoucher(scope.row)">
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
               :invoice_types="invoice_types"
               :invoiceDirection="queryParams.direction"
               :invoice_status="invoice_status"
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
import {delInvoice, fetchPage, generateVoucher, deleteVoucherSubmit} from "@/api/invoice/invoice";
import {ElForm} from "element-plus";
import modal from "@/plugins/modal";
import {useRouter} from "vue-router";
import EditForm from "./edit.vue"
import bookStore from "@/store/modules/bookStore";
import {formatAmount} from "@/utils";
import * as voucherApis from "@/api/system/voucher/voucher";
import voucherEdit from "@/views/voucher/voucher-edit.vue";

const currBookStore = bookStore()
const {t} = useI18n()
const {proxy} = getCurrentInstance()!;
const {invoice_types, invoice_status}
    = proxy?.useDict("invoice_types", "invoice_status");
const router = useRouter()

const data: any = reactive({
  queryParams: {
    direction: 'INPUT',
    invoiceDateRange: [], // 前端绑定 daterange
    startDate: '',        // 拆分后的开始日期
    endDate: '',         // 拆分后的结束日期
    pageNumber: 1,
    pageSize: 10,
    pageSizeOptions: [10, 20, 50]
  },
  voucherForm: {},
});
const {queryParams, voucherForm} = toRefs(data);
const dataList = ref([]);
const open = ref(false);
const loading = ref(true);
const id: any = ref(undefined);
const total = ref(0);
const title = ref("");
const ids = ref([]);
const selectionlist: any = ref<any>([]);
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const voucherOpen = ref(false);
const voucherPreviewMode = ref(false);
const voucherTitle = ref("");

/** 搜索按钮操作 */
function handleQuery() {
  if (queryParams.value.invoiceDateRange.length === 2) {
    queryParams.value.startDate = queryParams.value.invoiceDateRange[0];
    queryParams.value.endDate   = queryParams.value.invoiceDateRange[1];
  } else {
    queryParams.value.startDate = '';
    queryParams.value.endDate   = '';
  }

  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryParams.value.invoiceDateRange = [];
  queryParams.value.startDate = '';
  queryParams.value.endDate = '';
  queryParams.value.invoiceNo = undefined;
  queryParams.value.invoiceCode = undefined;
  queryParams.value.invoiceType = undefined;
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
  let subtitle = "进项发票";
  switch (queryParams.value.direction) {
    case "INPUT":
      // 不改，保持默认“销项发票”
      break;
    case "OUTPUT":
      subtitle = "销项发票";
      break;
    default:
      // 当以上所有情况都不匹配时执行
      break;
  }
  id.value = row.id;
  title.value = "修改发票信息 - " + subtitle;
  open.value = true;
}

function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除发票ID为"' + _ids + '"的数据项？').then(function () {
    return delInvoice({listIds: [_ids]});
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
    return delInvoice({listIds: ids.value});
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
  let subtitle = "进项发票";
  switch (queryParams.value.direction) {
    case "INPUT":
      // 不改，保持默认“销项发票”
      break;
    case "OUTPUT":
      subtitle = "销项发票";
      break;
    default:
      // 当以上所有情况都不匹配时执行
      break;
  }
  id.value = undefined;
  title.value = "新增发票信息 - " + subtitle;
  open.value = true;
}

function handleSelectionChange(selection: any) {
  selectionlist.value = selection;
  ids.value = selectionlist.value.map((item: any) => item.id);
}

function generateCurrentVoucher(row: any) {
  generateVoucher({id: row.id, direction: queryParams.value.direction}).then((res: any) => {
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
function deleteVoucher(row: any) {
  deleteVoucherSubmit({id: row.id}).then((res: any) => {
    if (res.code === 0) {
      getList();
      modal.msgSuccess("操作成功");
    }
  });
}

getList();
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
