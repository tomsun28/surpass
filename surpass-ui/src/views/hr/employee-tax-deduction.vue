<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
        <el-form-item :label="$t('jbx.employeetaxdeduction.idCardNo') + '：'" prop="idCardNo">
          <el-input
              v-model="queryParams.idCardNo"
              placeholder=""
              clearable
              style="width: 240px"
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
        <div>
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
        <div class="btn-form-right">
          <el-button @click="importVisible = true">{{ $t('jbx.text.import') }}</el-button>
        </div>
      </div>

      <el-table v-loading="loading" :data="list" border @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.employeeNo')" align="center" prop="employeeNo"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.employeeName')" align="center" prop="employeeName"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.idCardType')" align="center" prop="idCardType"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.idCardNo')" align="center" prop="idCardNo"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.education')" align="center" prop="education"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.continuingEducation')" align="center"
                         prop="continuingEducation"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.medical')" align="center" prop="medical"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.housingLoan')" align="center" prop="housingLoan"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.rent')" align="center" prop="rent"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.elderlyCare')" align="center" prop="elderlyCare"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.infantsCare')" align="center" prop="infantsCare"
                         width="140"/>
        <!--
        <el-table-column :label="$t('jbx.employeetaxdeduction.individualPension')" align="center" prop="individualPension"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.enterprisePension')" align="center" prop="enterprisePension"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.commercialHealth')" align="center" prop="commercialHealth"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.deferredPension')" align="center" prop="deferredPension"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.donationAllowed')" align="center" prop="donationAllowed"/>
        <el-table-column :label="$t('jbx.employeetaxdeduction.others')" align="center" prop="others"/>
        </el-table-column>
        -->
        <el-table-column :label="$t('jbx.text.action')" align="center"
                         class-name="small-padding fixed-width" width="80" fixed="right">
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
          v-show="total > 0"
          :total="total"
          v-model:page="queryParams.pageNumber"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-card>
    <!-- 添加或修改参数配置对话框 -->
    <el-drawer v-model="open" append-to-body
               :close-on-click-modal="false" size="600px">
      <template #header>
        <h4>{{ title }}</h4>
      </template>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="150px">
        <el-tabs model-value="first" class="demo-tabs">
          <el-tab-pane label="基本信息" name="first">
            <el-form-item :label="$t('jbx.employeetaxdeduction.employeeNo')" prop="employeeNo" :required="true">
              <el-input v-model="form.employeeNo" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.employeeName')" prop="employeeName" :required="true">
              <el-input v-model="form.employeeName" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.idCardType')" prop="idCardType" :required="true">
              <el-input v-model="form.idCardType" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.idCardNo')" prop="idCardNo" :required="true">
              <el-input v-model="form.idCardNo" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.education')" prop="education">
              <el-input v-model="form.education" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.continuingEducation')" prop="continuingEducation">
              <el-input v-model="form.continuingEducation" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.medical')" prop="medical">
              <el-input v-model="form.medical" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.housingLoan')" prop="housingLoan">
              <el-input v-model="form.housingLoan" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.rent')" prop="rent">
              <el-input v-model="form.rent" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.elderlyCare')" prop="elderlyCare">
              <el-input v-model="form.elderlyCare" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.infantsCare')" prop="infantsCare">
              <el-input v-model="form.infantsCare" placeholder=""/>
            </el-form-item>
          </el-tab-pane>
          <el-tab-pane label="扩展信息" name="second">
            <el-form-item :label="$t('jbx.employeetaxdeduction.individualPension')" prop="individualPension">
              <el-input v-model="form.individualPension" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.enterprisePension')" prop="enterprisePension">
              <el-input v-model="form.enterprisePension" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.commercialHealth')" prop="commercialHealth">
              <el-input v-model="form.commercialHealth" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.deferredPension')" prop="deferredPension">
              <el-input v-model="form.deferredPension" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.donationAllowed')" prop="donationAllowed">
              <el-input v-model="form.donationAllowed" placeholder=""/>
            </el-form-item>
            <el-form-item :label="$t('jbx.employeetaxdeduction.others')" prop="others">
              <el-input v-model="form.others" placeholder=""/>
            </el-form-item>
          </el-tab-pane>
        </el-tabs>
      </el-form>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
        </div>
      </template>
    </el-drawer>

    <import-upload :import-visible="importVisible" :http-request="importExcel"
                   @cancel="importVisible = false"></import-upload>
  </div>
</template>

<script setup name="EmployeeTaxDeduction" lang="ts">
import ImportUpload from '@/components/ImportUpload/index.vue'
import type {ElForm} from "element-plus";

import {
  ref,
  getCurrentInstance,
  reactive,
  toRefs,
} from "vue";
import modal from "@/plugins/modal";

import * as employeeTaxDeductionService from "@/api/hr/employeetaxdeductionservice";

import {useI18n} from "vue-i18n";

const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const {t} = useI18n()
const list: any = ref<any>([]);
const open: any = ref(false);
const importVisible: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");

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
    idCardNo: undefined
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
  employeeTaxDeductionService.fetch(queryParams.value).then((res: any) => {
    loading.value = false;
    if (res.code === 0) {
      list.value = res.data.records;
      total.value = res.data.total;
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
  const id: any = row?.id != null ? [row.id] : ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return employeeTaxDeductionService.del({ listIds: id });
  }).then(() => {
    getList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {
    // 可以选择在这里处理取消或错误情况
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
  employeeTaxDeductionService.get(id).then((res: any) => {
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
        employeeTaxDeductionService.update(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        employeeTaxDeductionService.add(form.value).then((response: any) => {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      }
    }
  });
}

/** 导入操作*/
function importExcel(item: any): any {
  let formData: any = new FormData();
  formData.append("excelFile", item.file);
  employeeTaxDeductionService.importExcel(formData).then((res: any) => {
    if (res.code === 0) {
      getList()
      modal.msgSuccess(t('jbx.alert.operate.success'));
    } else {
      modal.msgError(res.message);
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

.common-card {
  margin-bottom: 15px;

  .btn-form {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 10px;

    .el-button {
      margin-right: 10px;
    }
  }

  .btn-form-right {
    display: flex;
    justify-content: flex-end;
    align-items: center;

    .el-button {
      margin-right: 0;
      margin-left: 10px;
    }
  }
}

::v-deep(.common-card form .el-form-item--default) {
  margin-bottom: 0;
}
</style>
