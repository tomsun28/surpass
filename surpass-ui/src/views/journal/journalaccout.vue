<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <el-form :model="queryParams" ref="queryRef" inline v-show="showSearch">
        <el-form-item :label="$t('jbx.journalaccout.accName')" prop="accName">
          <el-input
              v-model="queryParams.accName"
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
      <el-table v-loading="loading" :data="list" show-summary :summary-method="getSummaries"
                border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.journalaccout.accName')" align="left" prop="accName"/>
        <el-table-column :label="$t('jbx.journalaccout.category.category')" align="center" prop="category">
          <template #default="scope">
            <span v-if="scope.row.category == 'deposit'">{{ $t('jbx.journalaccout.category.deposit') }}</span>
            <span v-if="scope.row.category == 'cash'">{{ $t('jbx.journalaccout.category.cash') }}</span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalaccout.accCode')" align="left" prop="accCode"/>
        <el-table-column :label="$t('jbx.journalaccout.balance')" align="right" prop="balance">
          <template #default="scope">
            {{ formatAmount(scope.row.balance) }}
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.journalaccout.currency')" align="center" prop="currency"/>
        <el-table-column :label="$t('jbx.journalaccout.bank')" align="left" prop="bank"/>
        <el-table-column :label="$t('jbx.journalaccout.bankNo')" width="180" align="left" prop="bankNo"/>
        <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex"/>
        <el-table-column :label="$t('jbx.text.status.status')" align="center" prop="status">
          <template #default="scope">
            <span v-if="scope.row.status == 1"><el-icon color="green"><SuccessFilled class="success"/></el-icon></span>
            <span v-if="scope.row.status == 0"><el-icon color="#808080"><CircleCloseFilled/></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" width="80" align="center"
                         class-name="small-padding fixed-width" fixed="right" >
          <template #default="scope">
            <el-tooltip content="编辑">
              <el-button link icon="Edit" @click="handleUpdate(scope.row)"></el-button>
            </el-tooltip>
            <el-tooltip content="删除">
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
        <el-form-item prop="category" :label="$t('jbx.journalaccout.category.category')" :required="true">
          <el-select v-model="form.category" clearable>
            <el-option
                v-for="dict in journalAccout_category_types"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalaccout.accCode')" prop="accCode">
          <el-input v-model="form.accCode" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalaccout.accName')" prop="accName">
          <el-input v-model="form.accName" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalaccout.subjectId')" prop="subjectId">
          <el-input v-model="form.subjectId" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalaccout.currency')" prop="currency">
          <el-input v-model="form.currency" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalaccout.bankNo')" prop="bankNo">
          <el-input v-model="form.bankNo" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.journalaccout.bank')" prop="bank">
          <el-input v-model="form.bank" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
          <el-input-number v-model="form.sortIndex" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.text.description')" prop="description">
          <el-input v-model="form.description" placeholder=""/>
        </el-form-item>
        <el-form-item prop="status" :label="$t('jbx.users.status')">
          <el-switch
              v-model="form.status"
              :active-value="1"
              :inactive-value="0"
          ></el-switch>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="SecuritySocialsprovider" lang="ts">
import {ElForm} from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, PropType, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  fetch,
  get,
  update,
  del,
  add,
  allBalance
} from "@/api/journal/journalaccountservice";
import {h} from 'vue'
import type {VNode} from 'vue'
import {useI18n} from "vue-i18n";
import {formatAmount} from "@/utils"

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()

const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");
const accountAllBalance: any = ref(0);

const {journalAccout_category_types}
    = proxy?.useDict("journalAccout_category_types");

const data: any = reactive({
  form: {
    id: undefined,
    scanCode: 'false',
    display: 'false',
    sortIndex: 1,
    status: 1
  },
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
    clientSecret: [{required: true, message: 'Not empty', trigger: "blur"}]
  },
});

const {queryParams, form, rules} = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  fetch(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.records;
      total.value = res.data.total;
    }
  });

  allBalance(queryParams.value).then((res: any) => {
    if (res.code === 0) {
      accountAllBalance.value = res.data;
    }
  });
}

const getSummaries = () => {
  const sums: (string | VNode)[] = []
  sums[0] = ''
  sums[1] = h('div', {style: {textAlign: 'center'}}, ['合计',])
  sums[2] = ''
  sums[3] = ''
  sums[4] = '' + formatAmount(accountAllBalance.value);
  return sums
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
    return del(id);
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
  form.value = {
    id: undefined,
    scanCode: 'false',
    display: 'false',
    sortIndex: 1,
    status: 1
  };
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
  get(id).then((res: any) => {
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
        update(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        add(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      }
    }
  });
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
