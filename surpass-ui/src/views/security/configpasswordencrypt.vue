<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
        <el-form-item :label="$t('jbx.passwordEncrypt.encodeId') + '：'" prop="encodeId">
          <el-input
              v-model="queryParams.encodeId"
              placeholder=""
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}</el-button>
          <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="common-card">
      <div class="btn-form">
        <el-button
            type="primary"
            size="small"
            @click="handleAdd"
        >{{ $t('jbx.text.add') }}
        </el-button>
        <el-button
            type="danger"
            size="small"
            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('jbx.text.delete') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.passwordEncrypt.encodeId')" align="left" prop="encodeId" width="200" :show-overflow-tooltip="true"/>
        <el-table-column :label="$t('jbx.passwordEncrypt.passwordEncoder')" align="left" prop="passwordEncoder" :show-overflow-tooltip="true"/>

        <el-table-column :label="$t('jbx.passwordEncrypt.isDefault')" align="center" width="100">
          <template #default="scope">
            <span v-if="scope.row.isDefault == 'N'">
              {{$t('jbx.text.no')}}
            </span>
            <span v-if="scope.row.isDefault == 'Y'">
              {{$t('jbx.text.yes')}}
            </span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.passwordEncrypt.status')" align="center" prop="status" width="100">
          <template #default="scope">
            <span v-if="scope.row.status == 1"><el-icon color="green"><SuccessFilled class="success" /></el-icon></span>
            <span v-if="scope.row.status == 0"><el-icon color="#808080"><CircleCloseFilled /></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex"  width="100"/>
        <el-table-column :label="$t('jbx.text.createdDate')" align="center" prop="createdDate"  width="180"/>

        <el-table-column :label="$t('jbx.text.action')" align="center" width="200">
          <template #default="scope">
            <el-button
                size="small"
                @click="handleUpdate(scope.row)"
            >{{ $t('jbx.text.edit') }}
            </el-button>
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.row)"
            >
              {{ $t('jbx.text.delete') }}
            </el-button>
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
        <el-form-item :label="$t('jbx.passwordEncrypt.encodeId')" prop="encodeId">
          <el-input v-model="form.encodeId" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.passwordEncrypt.passwordEncoder')" prop="passwordEncoder">
          <el-input v-model="form.passwordEncoder" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
          <el-input-number v-model="form.sortIndex" placeholder=""/>
        </el-form-item>
        <el-form-item  :label="$t('jbx.passwordEncrypt.isDefault')">
          <el-radio-group v-model="form.isDefault" size="medium">
            <el-radio-button label="N">{{$t('jbx.text.no')}}</el-radio-button>
            <el-radio-button label="Y">{{$t('jbx.text.yes')}}</el-radio-button>
          </el-radio-group>
        </el-form-item>
        <el-form-item prop="status" :label="$t('jbx.users.status')">
          <el-switch
              v-model="form.status"
              :active-value="1"
              :inactive-value="0"
          ></el-switch>
        </el-form-item>
        <el-form-item :label="$t('jbx.text.description')">
          <el-input type="textarea" autosize v-model="form.description" placeholder="" :autosize="{ minRows: 2, maxRows: 4}" maxlength="200"/>
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

<script setup name="ConfigPassworDencrypt" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  fetch,
  get,
  create,
  update,
  deleteByIds
} from "@/api/security/passwordencrypt";


import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()

const list: any = ref<any>([]);
const open: any = ref(false);
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
    isDefault: 'N',
    sortIndex:1,
    status: 1
  },
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    encodeId: undefined
  },
  rules: {
    passwordEncoder: [{ required: true,message: 'Not empty',  trigger: "blur" }],
    encodeId: [{ required: true,message: 'Not empty',  trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  fetch(queryParams.value).then((res: any) =>  {
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
  queryRef?.value?.resetFields();;
  handleQuery();
}

/** 删除按钮操作 */
function handleDelete(row: any): any {
  const id: any = row.id || ids.value;
  modal.confirm(t('jbx.confirm.text.delete')).then(function () {
    return deleteByIds(id);
  }).then(() => {
    getList();
    modal.msgSuccess(t('jbx.alert.operate.success'));
  }).catch(() => {});
}


/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) =>  item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 重置新增的表单以及其他数据  */
function reset(): any {
  form.value = {
    id: undefined,
    isDefault: 'N',
    sortIndex:1,
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
  get(id).then((res: any) =>  {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
};


/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      if (form.value.id != undefined) {
        update(form.value).then((response: any) =>  {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        create(form.value).then((response: any) =>  {
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
.common-card{
  margin-bottom: 15px;
}

::v-deep(.common-card form .el-form-item--default){
  margin-bottom: 0px;
}
</style>
