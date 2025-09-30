<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <el-form :model="queryParams" ref="queryRef" inline v-show="showSearch" label-width="68px">
        <el-form-item :label="$t('jbx.journalentry.remark')" prop="providerName">
          <el-input
              v-model="queryParams.providerName"
              placeholder=""
              clearable
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
          <el-button @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('jbx.text.delete') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange"
                border max-height="550">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.journalentry.tradeDate')" align="left" prop="tradeDate" width="160"/>
        <el-table-column :label="$t('jbx.journalentry.remark')" align="left" prop="remark"/>
        <el-table-column :label="$t('jbx.journalentry.subjectId')" align="left" prop="subjectId">
          <template #default="scope">
            {{ getSubjectName(scope.row.subjectId) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalaccout.category.category')" align="center" prop="category">
          <template #default="scope">
            <span v-if="scope.row.category == 'deposit'">{{ $t('jbx.journalaccout.category.deposit') }}</span>
            <span v-if="scope.row.category == 'cash'">{{ $t('jbx.journalaccout.category.cash') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalentry.accName')" align="left" prop="accName"/>
        <el-table-column :label="$t('jbx.journalentry.income')" align="right">
          <template #default="scope">
            {{ formatAmount(scope.row.income) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalentry.expenditure')" align="right">
          <template #default="scope">
            {{ formatAmount(scope.row.expenditure) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalentry.balance')" align="right">
          <template #default="scope">
            {{ formatAmount(scope.row.balance) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.voucher.voucher')" align="center" prop="voucherId" width="80">
          <template #default="scope">
            <el-button v-if="scope.row.voucherId === null ||scope.row.voucherId ===''" type="text"
                       @click="newVoucher(scope.row)">
              生成
            </el-button>
            <el-button v-if="scope.row.voucherId !== null &&scope.row.voucherId !==''" type="text"
                       @click="viewVoucher(scope.row.voucherId)">
              查看
            </el-button>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" align="center"
                         class-name="small-padding fixed-width" width="80" fixed="right">
          <template #default="scope">
            <el-tooltip v-if="scope.row.voucherId === null ||scope.row.voucherId ===''" content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip v-if="scope.row.voucherId === null ||scope.row.voucherId ===''" content="删除">
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
          @pagination="getList"
      />
    </el-card>
    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('jbx.journalentry.remark')" prop="remark" required>
          <el-input v-model="form.remark" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalentry.accId')" prop="accId" required>
          <el-select
              @change="handleAccIdChange"
              v-model="form.accId"
              placeholder="请选择"
          >
            <el-option
                v-for="item in accOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalentry.tradeDate')" prop="tradeDate" required>
          <el-date-picker v-model="form.tradeDate" placeholder="" type="datetime" value-format="YYYY-MM-DD HH:mm:ss"/>
        </el-form-item>

        <el-form-item :label="$t('jbx.journalentry.accId')" prop="accId" v-show="false">
          <el-input v-model="form.accId" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalentry.accCode')" prop="accCode" v-show="false">
          <el-input v-model="form.accCode" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalentry.accName')" prop="accName" v-show="false">
          <el-input v-model="form.accName" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalentry.category.category')" prop="category" v-show="false">
          <el-input v-model="form.category" placeholder=""/>
        </el-form-item>

        <el-form-item :label="$t('jbx.journalentry.subjectId')" prop="subjectId">
          <el-tree-select
              ref="resTreeRef"
              v-model="form.subjectId"
              :data="subjectOptions"
              :props="defaultProps"
              check-strictly
              value-key="id"
              show-checkbox
              clearable
          />
        </el-form-item>
        <!--
        <el-form-item :label="$t('jbx.journalentry.voucherId')" prop="voucherId">
          <el-input v-model="form.voucherId" placeholder=""/>
        </el-form-item>
        -->
        <el-form-item prop="direction" :label="$t('jbx.journalentry.direction')" required>
          <el-radio-group v-model="form.direction">
            <el-radio-button value="i">{{ t('jbx.journalentry.income') }}</el-radio-button>
            <el-radio-button value="e">{{ t('jbx.journalentry.expenditure') }}</el-radio-button>
            <el-radio-button value="o" v-if="form.initBalance">初始化</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="form.direction == 'i'" :label="$t('jbx.journalentry.income')" prop="income" required>
          <el-input v-model="form.income"
                    :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                    :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
        </el-form-item>
        <el-form-item v-if="form.direction == 'o'" label="金额" prop="income" required>
          <el-input v-model="form.income"
                    :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                    :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
        </el-form-item>
        <el-form-item v-if="form.direction == 'e'" :label="$t('jbx.journalentry.expenditure')" prop="expenditure"
                      required>
          <el-input v-model="form.expenditure"
                    :formatter="(value:any) => `￥ ${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
                    :parser="(value:any) => value.replace(/￥\s?|(,*)/g, '')"></el-input>
        </el-form-item>
        <el-form-item :label="$t('jbx.text.description')" prop="description">
          <el-input v-model="form.description" placeholder="" type="textarea"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        </div>
      </template>
    </el-dialog>

    <!-- 添加或修改凭证记录对话框 -->
    <el-drawer :title="voucherTitle" v-model="voucherOpen" :close-on-click-modal="false" @close="getList" size="1200px">
      <template #header>
        <h4>{{ voucherTitle }}</h4>
      </template>
      <voucher-edit v-if="voucherOpen" v-model="voucherForm" :edit="!voucherPreviewMode" :dialog="true" :auto="false"
                    @submit="submitForm"></voucher-edit>
    </el-drawer>
  </div>
</template>

<script setup name="SecuritySocialsprovider" lang="ts">
import {ElForm} from "element-plus";
import dayjs from 'dayjs'
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import * as journalEntryService from "@/api/journal/journalentryservice";
import * as journalAccountservice from "@/api/journal/journalaccountservice";
import * as voucherApis from "@/api/system/voucher/voucher";
import voucherEdit from "@/views/voucher/voucher-edit.vue";
import booksSetStore from "@/store/modules/bookStore";
import {formatAmount} from "@/utils"
import Decimal from 'decimal.js'
import {
  getTree,
} from "@/api/system/standard/standard-subject";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const currBookStore = booksSetStore()
const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");
let accOptions: any = ref([]);
const voucherOpen: any = ref(false);
const voucherTitle: any = ref("");
const voucherPreviewMode = ref(false);
const subjectOptions: any = ref<any[]>([]);
let subjectList: any = ref([]);
const defaultProps: any = ref({
  value: 'id',
  children: 'children',
  label: 'name',
  disabled: disabledFilter
})

const data: any = reactive({
  form: {
    id: undefined,
    scanCode: 'false',
    display: 'false',
    initBalance: false,
    income: 0.0,
    expenditure: 0.0,
    sortIndex: 1,
    status: 1
  },
  voucherForm: {},
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    providerName: undefined
  },
  rules: {
    icon: [{required: true, message: 'Not empty', trigger: "blur"}],
    provider: [{required: true, message: 'Not empty', trigger: "blur"}],
    providerName: [{required: true, message: 'Not empty', trigger: "blur"}],
    clientId: [{required: true, message: 'Not empty', trigger: "blur"}],
    income: [{required: true, message: '请输入收入金额', trigger: "blur"}],
    expenditure: [{required: true, message: '请输入支出金额', trigger: "blur"}],
    clientSecret: [{required: true, message: 'Not empty', trigger: "blur"}]
  },
});

const {queryParams, form, rules, voucherForm} = toRefs(data);

function getSubjectName(subjectId: string): string {
  if (subjectId == null || subjectId === "") {
    return "";
  }
  for (let i = 0; i < subjectList.value.length; i++) {
    if (subjectList.value[i].id == subjectId) {
      return subjectList.value[i].name;
    }

  }
  return "";
}

function disabledFilter(data: any, node: any): any {
  if (!currBookStore.bookId) {
    return false;
  }
  return new RegExp(`/${currBookStore.bookId}(/|$)`).test(data.idPath)
}

interface TreeNode {
  id: string | number; // 根据你的数据实际情况调整类型
  name: string;
  children?: TreeNode[]; // 子节点可能不存在
}

function buildSubject(nodes: TreeNode[]) {
  for (let i = 0; i < nodes.length; i++) {
    let item = nodes[i];
    subjectList.value.push(item);
    if (item.children) {
      buildSubject(item.children);
    }
  }
}

function getSubjectTree() {
  getTree({bookId: currBookStore.bookId}).then((response: { data: TreeNode[] }) => {
    subjectOptions.value = response.data;
    buildSubject(response.data);
    form.value.parentId = undefined;
  });
}

getSubjectTree();

/** 分页列表 */
function getList(): any {
  loading.value = true;
  journalEntryService.fetch(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.records;
      total.value = res.data.total;
    }
  });
}

function getAccList(): any {
  loading.value = true;
  journalAccountservice.findAll(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      for (let i = 0; i < res.data.length; i++) {
        //console.log(res.data[i]);
        accOptions.value.push({
          value: res.data[i].id,
          label: res.data[i].accCode + "_" + res.data[i].accName + "(余额：" + res.data[i].balance + ")",
          data: res.data[i]
        })
      }
      console.log(accOptions);
      //formRef.forceUpdate();
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
  queryRef?.value?.resetFields();
  ;
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return journalEntryService.del(id);
  }).then(() => {
    getList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {
  });
}


/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 重置新增的表单以及其他数据  */
function reset(): any {

  accOptions.value = [];
  getAccList();

  form.value = {
    id: undefined,
    tradeDate: dayjs(new Date()).format("YYYY-MM-DD HH:mm:ss"),
    direction: "i",
    income: 0.0,
    expenditure: 0.0,
    scanCode: 'false',
    display: 'false',
    sortIndex: 1,
    status: 1
  };

  console.log(form.value);
  formRef.value?.clearValidate();
}

/** 添加分组 */
function handleAdd(): any {
  reset();

  open.value = true;
  title.value = t('jbx.text.add');
}

/** 修改按钮操作 */
function handleUpdate(row: any): any {
  reset();
  const id: any = row.id || ids.value;
  journalEntryService.get(id).then((res: any) => {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
};


/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) => {
    if (valid) {
      if (form.value.id != undefined) {
        journalEntryService.update(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        journalEntryService.add(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

function handleAccIdChange(val: any) {
  for (let i = 0; i < accOptions.value.length; i++) {
    if (val === accOptions.value[i].value) {
      console.log(accOptions.value[i].data);
      form.value.accCode = accOptions.value[i].data.accCode;
      form.value.accName = accOptions.value[i].data.accName;
      form.value.category = accOptions.value[i].data.category;
      console.log("balance " + accOptions.value[i].data.balance);
      form.value.initBalance = new Decimal(accOptions.value[i].data.balance).eq(new Decimal(0));
      console.log("initBalance " + form.value.initBalance);
    }
  }
}

/** 查看操作 */
function viewVoucher(voucherId: any) {
  voucherApis.getOneVoucher(voucherId).then((response: any) => {
    response.data.items.forEach((t: any) => {
      //t.subjectCode = t.subjectName.split("-")[0]
      //t.detailedSubjectCode = t.detailedAccounts.split("-")[0]
    })
    voucherForm.value = response.data
    voucherOpen.value = true;
    voucherPreviewMode.value = false;
    voucherTitle.value = "凭证记录";
  });
}

/** 新增按钮操作 */
function newVoucher(rowData: any) {
  voucherPreviewMode.value = false;
  journalEntryService.generateVoucherSubmit({id: rowData.id, voucherType: 1}).then((res: any) => {
    if (res.code === 0) {
      viewVoucher(res.data)
      getList();
    }
  })
}

/** 取消按钮 */
function cancel(): any {
  open.value = false;
  reset();
}

getList();


</script>
<style scoped>
::v-deep(.el-checkbox__label) {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap
}

</style>
