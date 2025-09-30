<template>
  <div class="app-container">
    <el-card class="common-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
        <el-form-item :label="$t('jbx.apps.adapterName') + '：'" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder=""
            clearable
            @keyup.enter.native="handleQuery"
          />
        </el-form-item><!-- style="width: 240px" -->
        <el-form-item>
          <el-button  size="small" @click="handleQuery">{{ $t('jbx.text.query') }}
          </el-button><!-- type="primary" -->
          <el-button size="small" @click="resetQuery">{{ $t('jbx.text.reset') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card class="common-card">
        <div class="btn-form">
<!--          <el-button
            type="primary"
            size="small"
            @click="handleAdd"
          >{{ $t('jbx.text.add') }}
          </el-button>-->
          <el-button
            type="danger"
            plain
            size="small"
            :disabled="multiple"
            @click="handleDelete"
          >{{ $t('jbx.text.delete') }}
          </el-button>
         </div>
      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.apps.adapterName')" align="center" prop="name"/>
        <el-table-column :label="$t('jbx.synchronizers.scheduler')" align="center" prop="scheduler"/>
        <el-table-column :label="$t('jbx.users.status')" align="center" prop="status">
          <template #default="scope">
            <span v-if="scope.row.status == 1"><el-icon color="green"><SuccessFilled class="success" /></el-icon></span>
            <span v-if="scope.row.status == 0"><el-icon color="#808080"><CircleCloseFilled /></el-icon></span>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.history.systemlogsMessageaction')" align="center"
                         class-name="small-padding fixed-width" width="200" >
          <template #default="scope">
            <el-button @click="handleUpdate(scope.row)" style ="margin-right: 10px">{{t('jbx.text.edit')}}</el-button>

            <el-dropdown>
              <el-button>
                {{t('jbx.text.moreaction')}}<el-icon class="el-icon--right"><arrow-down /></el-icon>
              </el-button>
              <template #dropdown>
                <el-dropdown-menu>
                  <div>
                    <el-dropdown-item  @click="handleSync(scope.row)">{{t('jbx.text.synchr')}}</el-dropdown-item>
                  </div>
                  <div>
                    <el-dropdown-item  @click="handleDelete(scope.row)">{{ $t('jbx.text.delete') }}</el-dropdown-item>
                  </div>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="140px">
<!--        <el-form-item :label="$t('jbx.organizations.id')" prop="id">
          <el-input :disabled="form.d_id != undefined" v-model="form.id" placeholder=""/>
        </el-form-item>-->
        <el-form-item :label="$t('jbx.synchronizers.sourceType')" prop="sourceType">
          <el-select :disabled="form.d_id != undefined" v-model="form.sourceType" placeholder="" clearable style="width: 100%">
            <el-option
              v-for="dict in sync_source_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('jbx.synchronizers.service')" prop="service">
          <el-input v-model="form.service" placeholder=""/>
        </el-form-item>

        <el-form-item :label="$t('jbx.synchronizers.scheduler')" prop="scheduler">
          <el-input v-model="form.scheduler" placeholder="0 0 12 * * ?"/>
        </el-form-item>

        <el-form-item :label="$t('jbx.users.workStreetAddress')" prop="providerUrl">
          <el-input v-model="form.providerUrl" placeholder=""/>
        </el-form-item>

        <el-form-item :label="$t('jbx.emailsenders.account')" prop="principal">
          <el-input v-model="form.principal" placeholder=""/>
        </el-form-item>

        <el-form-item :label="$t('jbx.emailsenders.credentials')" prop="credentials">
          <el-input type="password" v-model="form.credentials" placeholder=""/>
        </el-form-item>

        <div v-if="form.d_id">
          <el-form-item :label="$t('jbx.synchronizers.userBasedn')" prop="userBasedn">
            <el-input v-model="form.userBasedn" placeholder=""/>
          </el-form-item>
          <el-form-item :label="$t('jbx.synchronizers.userFilters')" prop="userFilters">
            <el-input v-model="form.userFilters" placeholder=""/>
          </el-form-item>
          <el-form-item :label="$t('jbx.synchronizers.orgBasedn')" prop="orgBasedn">
            <el-input v-model="form.orgBasedn" placeholder=""/>
          </el-form-item>
          <el-form-item :label="$t('jbx.synchronizers.orgFilters')" prop="orgFilters">
            <el-input v-model="form.orgFilters" placeholder=""/>
          </el-form-item>
          <el-form-item :label="$t('jbx.synchronizers.msadDomain')" prop="msadDomain">
            <el-input v-model="form.msadDomain" placeholder=""/>
          </el-form-item>
          <el-form-item label="SSL" prop="sslSwitch">
            <el-switch
              v-model="form.sslSwitch"
              active-value="1"
              inactive-value="0"
            ></el-switch>
          </el-form-item>
        </div>

        <el-form-item :label="$t('jbx.synchronizers.resumeTime')" prop="resumeTime">
          <el-input v-model="form.resumeTime" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.roles.suspendTime')" prop="suspendTime">
          <el-input v-model="form.suspendTime" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.users.status')" prop="status">
          <el-switch
            v-model="form.status"
            active-value="1"
            inactive-value="0"
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

<script setup name="Config-synchronizers" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  getSynchronizers,
  listSynchronizers,
  addSynchronizers,
  updateSynchronizers,
  delSynchronizers,
  setSynchronizers
} from "@/api/config/synchronizers";

import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()
const { sync_source_type } = proxy.useDict("sync_source_type");

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
    name: undefined,
    protocol: undefined,
    adapter: undefined,
    sortIndex: 1,
    description: undefined
  },
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    name: undefined
  },
  rules: {
    name: [{ required: true, message: 'Not empty', trigger: "blur" }],
    protocol: [{ required: true,message: 'Not empty',  trigger: "blur" }],
    adapter: [{ required: true,message: 'Not empty',  trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  listSynchronizers(queryParams.value).then((res: any) =>  {
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
    return delSynchronizers(id);
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
    name: undefined,
    protocol: undefined,
    adapter: undefined,
    sortIndex: 1,
    description: undefined
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
  getSynchronizers(id).then((res: any) =>  {
    form.value = res.data;
    open.value = true;
    title.value = t('jbx.text.edit');
  });
};

function handleSync(row: any): any {
  setSynchronizers(row.id).then((res: any) =>  {
    modal.msgSuccess("同步成功")
  }).catch((err: any) =>  {
    modal.msgError('同步失败')
  })
}

/** 提交按钮 */
function submitForm(): any {
  formRef?.value?.validate((valid: any) =>  {
    if (valid) {
      if (form.value.id != undefined) {
        updateSynchronizers(form.value).then((response: any) =>  {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        addSynchronizers(form.value).then((response: any) =>  {
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
