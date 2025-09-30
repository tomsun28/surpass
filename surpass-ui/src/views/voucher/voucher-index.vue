<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="凭证字" prop="word">
            <el-input
                v-model="queryParams.word"
                placeholder="请输入凭证字"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="年份" prop="voucherYear">
            <el-date-picker
                style="width: 110px"
                v-model="queryParams.voucherYear"
                type="year"
                placeholder="请选择"
                format="YYYY年"
                value-format="YYYY"
                @change="handleQuery"
            />
          </el-form-item>
          <el-form-item label="月份" prop="voucherMonth">
            <el-date-picker
                style="width: 110px"
                v-model="queryParams.voucherMonth"
                type="month"
                placeholder="请选择"
                format="MM月"
                value-format="MM"
                @change="handleQuery"
            />
          </el-form-item>
          <el-form-item label="日期" prop="voucherDate">
            <el-date-picker
                style="width: 140px"
                clearable
                v-model="queryParams.voucherDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择"
                @change="handleQuery">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button @click="handleQuery">搜索</el-button>
            <el-button @click="resetQuery">重置</el-button>
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
            type="success"
            :disabled="ids.length === 0"
            @click="handleSubmit"
        >提交
        </el-button>
        <el-button
            type="success"
            :disabled="ids.length === 0"
            @click="handleAudit"
        >审核
        </el-button>
        <el-button
            type="success"
            :disabled="ids.length === 0"
            @click="handleManager"
        >主管复核
        </el-button>
        <el-button
            type="success"
            :disabled="ids.length === 0"
            @click="handleSender"
        >过账
        </el-button>

        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="handleDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
        <div class="btn-form-right">
          <el-button @click="handleExport">导出</el-button>
          <el-button @click="handleShowVoucherSuccessive">凭证整理</el-button>
        </div>
      </div>

      <el-table max-height="540" v-loading="loading" :data="booksVoucherList"
                border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column label="凭证字" align="left" header-align="center" prop="word" width="150" fixed="left"/>
        <!--        <el-table-column label="公司名称" align="center" prop="companyName" width="150"/>-->
        <el-table-column label="备注" align="left" header-align="center" prop="remark" width="280"
                         :show-overflow-tooltip="true"/>
        <el-table-column label="借方总额" align="right" header-align="center" prop="debitAmount" width="120">
          <template #default="scope">
            {{ formatAmount(scope.row.debitAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="贷方总额" align="right" header-align="center" prop="creditAmount" width="120">
          <template #default="scope">
            {{ formatAmount(scope.row.creditAmount) }}
          </template>
        </el-table-column>
        <el-table-column label="日期" align="center" prop="voucherDate" width="100">
          <template #default="scope">
            <span>{{ parseTime(scope.row.voucherDate, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="附单据" align="center" prop="receiptNum"/>
        <el-table-column label="结转" align="center" prop="carryForward">
          <template #default="scope">
            <span v-if="scope.row.carryForward === 'y'">是</span>
            <span v-if="scope.row.carryForward === 'n'">否</span>
          </template>
        </el-table-column>
        <el-table-column label="制单人" align="center" prop="createdName" :show-overflow-tooltip="true"/>
        <el-table-column label="创建时间" align="center" prop="createdDate" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.createdDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="审核人" align="center" prop="auditMemberName" :show-overflow-tooltip="true"/>
        <el-table-column label="审核时间" align="center" prop="auditDate" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.auditDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="过账人" align="center" prop="senderName" :show-overflow-tooltip="true"/>
        <el-table-column label="过账操作时间" align="center" prop="senderDate" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.senderDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="主管" align="center" prop="managerName" :show-overflow-tooltip="true"/>
        <el-table-column label="主管操作时间" align="center" prop="managerDate" width="160">
          <template #default="scope">
            <span>{{ parseTime(scope.row.managerDate, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status" fixed="right" width="80">
          <template #default="scope">
            <div v-html="getVoucherStatusDesc(scope.row.status)"></div>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="120" fixed="right">
          <template #default="scope">
            <!--      暂存才可以修改      -->
            <el-tooltip
                v-if="'draft' === scope.row.status && isEditable(scope.row.voucherDate)"
                content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip
                v-if="'draft' !== scope.row.status || !isEditable(scope.row.voucherDate)"
                content="查看">
              <el-button link icon="View" @click="handlePreview(scope.row)"></el-button>
            </el-tooltip>
            <!--      审批中才可以取消      -->
            <el-tooltip
                v-if="'reviewing' === scope.row.status"
                content="取消">
              <el-button link icon="RemoveFilled" type="danger"
                         @click="handleCancel(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="currBookStore.termCurrent <= scope.row.voucherDate.substring(0, 7)" content="移除">
              <el-button link icon="Delete" type="danger" @click="handleDelete(scope.row)"></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>

      <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-card>

    <!-- 添加或修改凭证记录对话框 -->
    <el-drawer :title="title" v-model="open" :close-on-click-modal="false" @close="getList" size="1200px">
      <template #header>
        <h4>{{ title }}</h4>
      </template>
      <voucher-edit v-if="open" v-model="form" :edit="!previewMode" :dialog="true" :auto="false"
                    @submit="submitForm"></voucher-edit>
    </el-drawer>

    <!-- 检查结果 -->
    <el-dialog title="检查结果" v-model="dialogVoucherSuccessive.visible" width="1200px"
               style="margin-top: 20vh !important;">
      <el-alert v-if="dialogVoucherSuccessive.isNumber" title="恭喜您,所有凭证已连号，无需整理" type="success"
                :closable="false" show-icon/>

      <!--      <el-form v-model="dialogVoucherSuccessive" inline style="margin-top: 20px">-->
      <!--        <el-form-item label="凭证字">-->
      <!--          <el-select style="width: 60px" v-model="dialogVoucherSuccessive.wordHead" placeholder="凭证字">-->
      <!--            <el-option label="所有" value=""/>-->
      <!--            <el-option label="记" value="记"/>-->
      <!--            <el-option label="收" value="收"/>-->
      <!--            <el-option label="付" value="付"/>-->
      <!--            <el-option label="转" value="转"/>-->
      <!--          </el-select>-->
      <!--        </el-form-item>-->
      <!--        <el-form-item label="起始凭证号">-->
      <!--          <el-input-number v-model="dialogVoucherSuccessive.startWordNumber" :min="1"></el-input-number>-->
      <!--        </el-form-item>-->
      <!--        <el-form-item>-->
      <!--          <el-radio-group v-model="dialogVoucherSuccessive.successiveMethod">-->
      <!--            <el-radio-button label="按凭证号顺次前移补齐断号" value="sequential"/>-->
      <!--            <el-radio-button label="按凭证日期重新顺次编号" value="date"/>-->
      <!--          </el-radio-group>-->
      <!--        </el-form-item>-->
      <!--        <el-form-item label="作废凭证参与凭证整理">-->
      <!--          <el-switch v-model="dialogVoucherSuccessive.nullify"/>-->
      <!--        </el-form-item>-->
      <!--        <el-form-item>-->
      <!--          <el-button @click="handleVoucherSuccessiveQuery">搜索</el-button>-->
      <!--        </el-form-item>-->
      <!--      </el-form>-->

      <el-table v-loading="dialogVoucherSuccessive.loading" border :data="voucherSuccessiveList">
        <el-table-column label="原始凭证号" prop="sourceWord" align="center"></el-table-column>
        <el-table-column label="新凭证号" prop="targetWord" align="center"></el-table-column>
        <template #empty>
          <div style="text-align: center">暂无记录</div>
        </template>
      </el-table>

      <template #footer>
        <el-button @click="dialogVoucherSuccessive.visible = false">取消</el-button>
        <el-button v-if="voucherSuccessiveList.length > 0" v-loading="dialogVoucherSuccessive.btnLoading" type="primary"
                   @click="handleVoucherSuccessiveUpdate">
          确认整理
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="BooksVoucher">
import * as voucherApis from "@/api/system/voucher/voucher";
import {useI18n} from "vue-i18n";
import voucherEdit from "./voucher-edit.vue";
import {useRouter} from "vue-router";
import {getVoucherStatusDesc} from "@/utils/enums/VoucherStatusEnum"
import {parseTime} from "@/utils/Jinbooks";
import {formatAmount} from "@/utils";
import {reactive} from "vue";
import bookStore from "@/store/modules/bookStore";
import {downloadData} from "@/utils/index"

const currBookStore = bookStore()
const router = useRouter();
const {proxy} = getCurrentInstance();
const {t} = useI18n()
const booksVoucherList = ref([]);
const voucherSuccessiveList = ref([]);
const open = ref(false);
const previewMode = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const title = ref("");
const dialogVoucherSuccessive = reactive({
  visible: false,
  loading: false,
  btnLoading: false,
  title: '',
  isNumber: false,
  successiveMethod: "sequential", // 按凭证号顺次前移补齐断号（sequential），按凭证日期重新顺次编号（date）
  nullify: true,
  wordHead: '记',
  startWordNumber: 1
});
const data = reactive({
  form: {},
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    orderByColumn: "voucherDate,carryForward,wordHead,wordNum",
    isAsc: "desc,asc,desc,desc",
    word: null,
    bookId: null,
    companyName: null,
    voucherYear: currBookStore.termCurrent ? currBookStore.termCurrent.substring(0, 4) : parseTime(new Date(), "{y}"),
    voucherMonth: currBookStore.termCurrent ? currBookStore.termCurrent.substring(5, 7) : parseTime(new Date(), "{m}"),
    voucherDate: null,
  },
});

const {queryParams, form} = toRefs(data);

/** 查询凭证记录列表 */
function getList() {
  loading.value = true;
  voucherApis.listVouchers(queryParams.value).then(response => {
    booksVoucherList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

// 取消按钮
function cancel() {
  open.value = false;
  reset();
}

// 表单重置
function reset() {
  form.value = {
    id: null,
    word: null,
    bookId: null,
    wordHead: '收',
    wordNum: null,
    companyId: null,
    companyName: null,
    remark: null,
    receiptNum: 0,
    debitAmount: null,
    creditAmount: null,
    voucherYear: null,
    voucherMonth: null,
    voucherDate: parseTime(new Date(), "{y}-{m}-{d}"),
    carryForward: null,
    auditMemberId: null,
    auditMemberName: null,
    auditDate: null,
    status: null,
    items: []
  };
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef");
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id);
  single.value = selection.length !== 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
function handleAdd() {
  previewMode.value = false;
  router.push({
    path: "/voucher/voucher-edit"
  })
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset();
  const _id = row.id || ids.value
  voucherApis.getOneVoucher(_id).then(response => {
    response.data.items.forEach(t => {
      t.subjectCode = t.subjectName.split("-")[0]
      t.detailedSubjectCode = t.detailedAccounts?.split("-")[0]
    })
    form.value = response.data
    open.value = true;
    previewMode.value = false;
    title.value = "修改凭证记录";
  });
}

/** 查看操作 */
function handlePreview(row) {
  reset();
  const _id = row.id || ids.value
  voucherApis.getOneVoucher(_id).then(response => {
    response.data.items.forEach(t => {
      t.subjectCode = t.subjectName.split("-")[0]
      t.detailedSubjectCode = t.detailedAccounts?.split("-")[0]
    })
    form.value = response.data
    open.value = true;
    previewMode.value = true;
    title.value = "查看凭证记录";
  });
}

/** 修改按钮操作 */
function handleCancel(row) {
  const _id = [row.id] || ids.value.join(",");
  voucherApis.cancelVoucherByIds(_id).then(res => {
    proxy.$modal.msgSuccess("已取消");
    getList()
  });
}

/** 提交按钮 */
function submitForm(res) {
  if (res.code === 0) {
    open.value = false
    getList();
  }
}

function handleSubmit(row) {
  const _ids = row.id || ids.value.join(",");
  const idList = _ids.split(",")
  const submitIds = booksVoucherList.value.filter(item => {
    return idList.indexOf(item.id) > -1 && 'draft' === item.status
  }).map(item => item.id)
  if (!submitIds.length) {
    proxy.$modal.msgError("没有可以提交的凭证项。");
    return
  }
  voucherApis.submitBatch(submitIds).then(res => {
    getList();
    proxy.$modal.msgSuccess("操作成功")
  })
}

function handleAudit(row) {
  const _ids = row.id || ids.value.join(",");
  proxy.$modal.confirm('确认审核"' + _ids + '"等凭证？').then(function () {
    return voucherApis.auditBatch(_ids);
  }).then((res) => {
    getList();
    proxy.$modal.msgSuccess(res.msg);
  }).catch(() => {
  });
}

function handleSender(row) {
  const _ids = row.id || ids.value.join(",");
  voucherApis.senderBatch(_ids).then(res => {
    getList();
    proxy.$modal.msgSuccess("操作成功")
  }).catch((err) => {
    proxy.$modal.msgError(err.msg || "操作失败");
  });
}

function handleManager(row) {
  const _ids = row.id || ids.value.join(",");
  voucherApis.manageBatch(_ids).then(res => {
    proxy.$modal.msgSuccess("操作成功")
    getList();
  }).catch((err) => {
    proxy.$modal.msgError(err.msg || "操作失败");
  });
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value.join(",");
  proxy.$modal.confirm('删除凭证可能为导致不连号，确认删除凭证记录编号为"' + _ids + '"的数据项？').then(function () {
    return voucherApis.deleteBatch(_ids);
  }).then(() => {
    getList();
    proxy.$modal.msgSuccess("删除成功");
  }).catch(() => {
  });
}

/**
 * 凭证连号检查
 */
function handleShowVoucherSuccessive() {
  dialogVoucherSuccessive.visible = true
  dialogVoucherSuccessive.isNumber = false
  voucherSuccessiveList.value = []
  handleVoucherSuccessiveQuery()
}

/**
 * 更新凭证为连号
 */
function handleVoucherSuccessiveUpdate() {
  dialogVoucherSuccessive.btnLoading = true
  voucherApis.updateVoucherSuccessive(voucherSuccessiveList.value).then(res => {
    proxy.$modal.msgSuccess("凭证更新成功")
    dialogVoucherSuccessive.visible = false
    getList()
  }).finally(() => {
    dialogVoucherSuccessive.btnLoading = false
  })

  dialogVoucherSuccessive.visible = false
}

function handleVoucherSuccessiveQuery() {
  dialogVoucherSuccessive.loading = true
  voucherApis.getVoucherSuccessiveList(dialogVoucherSuccessive).then(res => {
    voucherSuccessiveList.value = res.data
    if (res.data.length === 0) {
      dialogVoucherSuccessive.isNumber = true
    }
  }).finally(() => {
    dialogVoucherSuccessive.loading = false
  })
}

/**
 * 导出
 */
function handleExport() {
  voucherApis.exportVouchers(queryParams.value).then(data => {
    downloadData(data, "凭证 " + parseTime(new Date()) + ".xlsx")
  })
}

function isEditable(date) {
  const now = new Date(currBookStore.termCurrent + "-01")
  return parseTime(new Date(date), "{y}-{m}-{d}") >= parseTime(now, "{y}-{m}-{d}")
}

getList();
</script>

<style lang="scss" scoped>
.app-container {
  padding: 0;
  background-color: #f5f7fa;
}

.btn-form {
  margin-bottom: 10px;

  .btn-form-right {
    float: right;
  }
}

.common-card {
  margin-bottom: 15px;
}
</style>
