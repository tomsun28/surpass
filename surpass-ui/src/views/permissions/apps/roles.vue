<template>
  <div class="app-container">
    <el-card class="common-card">
      {{ $t('jbx.message.cheack.node') }}：{{appName}}
    </el-card>
    <el-card class="common-card">
      <el-form :model="queryParams" ref="queryRef" :inline="true">
        <el-form-item :label="$t('jbx.roles.name') + '：'" prop="roleName">
          <el-input
              v-model="queryParams.roleName"
              placeholder=""
              clearable
              style="width: 240px"
              @keyup.enter.native="handleQuery"
          />
        </el-form-item>
        <el-form-item>
          <el-button size="small" @click="handleQuery">{{ $t('jbx.text.query') }}
          </el-button><!-- type="primary" -->
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
            plain
            size="small"
            :disabled="multiple"
            @click="handleDelete"
        >{{ $t('jbx.text.delete') }}
        </el-button>
      </div>

      <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column :label="$t('jbx.text.id')" align="left" prop="roleCode"   show-overflow-tooltip />
        <el-table-column :label="$t('jbx.roles.name')" align="left" prop="roleName"   show-overflow-tooltip />
        <el-table-column :label="$t('jbx.text.description')" align="center" prop="description"  :show-overflow-tooltip="true"/>
        <el-table-column :label="$t('jbx.text.action')" align="center"
                         class-name="small-padding fixed-width" width="200" >
            <template #default="scope">
              <el-button @click="handleUpdate(scope.row)" style ="margin-right: 10px" >{{ $t('jbx.text.edit') }}</el-button>
              <el-dropdown>
                <el-button>
                  {{$t('jbx.text.moreaction')}}<el-icon class="el-icon--right"><arrow-down /></el-icon>
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <div>
                      <el-dropdown-item @click="handleAuthUser(scope.row)">{{t('jbx.text.authUser')}}</el-dropdown-item>
                    </div>
                    <div >
                      <el-dropdown-item @click="handleAuthResource(scope.row)">{{ $t('jbx.text.authResource') }}</el-dropdown-item>
                    </div>
                    <div>
                      <el-dropdown-item @click="handleDelete(scope.row)">{{$t('jbx.text.delete')}}</el-dropdown-item>
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
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('jbx.text.id')" prop="roleCode">
          <el-input v-model="form.roleCode" placeholder="" :disabled="form.id != undefined"/>
        </el-form-item>
        <el-form-item :label="$t('jbx.roles.name')" prop="roleName">
          <el-input v-model="form.roleName" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.text.description')" prop="description">
          <el-input type="textarea"  rows="3"  maxlength="200" show-word-limit v-model="form.description" placeholder="" />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">{{ $t('jbx.text.submit') }}</el-button>
          <el-button @click="cancel">{{ $t('jbx.text.close') }}</el-button>
        </div>
      </template>

    </el-dialog>

    <!--    成员-->
    <auth-user-info ref="authUserInfoRef"></auth-user-info>
    <!--    访问权限-->
    <auth-resource ref="authResourceRef"></auth-resource>

  </div>
</template>

<script setup name="Config-adapters" lang="ts">
import { useRoute, useRouter } from "vue-router";
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";

import AuthResource from "@/views/permissions/apps/auth-resource/index"
import AuthUserInfo from "@/views/permissions/apps/auth-userinfo/index"

import {
  getRoles,
  listRoles,
  create,
  update,
  deleteByIds
} from "@/api/permissions/roles";

import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const route: any = useRoute();
const { t } = useI18n()

const { group_category } = proxy.useDict("group_category");

//获取路由参数
const {appId,appName } = route.query;
const list: any = ref<any>([]);
const open: any = ref(false);
const loading: any = ref(true);
const showSearch: any = ref(true);
const ids: any = ref<any>([]);
const single: any = ref(true);
const multiple: any = ref(true);
const total: any = ref(0);
const title: any = ref("");
const authUserInfoRef: any = ref(undefined);
const authResourceRef: any = ref(undefined);
const data: any = reactive({
  form: {
    id: undefined,
    appId: undefined,
    appName: undefined,
    roleCode: undefined,
    roleName:undefined,
    status: 1,
    category: 'static',
    isdefault:undefined,
    description: undefined
  },
  queryParams: {
    pageNumber: 1,
    pageSize: 10,
    roleName: undefined,
    appId: undefined,
    appName: undefined,
    description: undefined
  },
  rules: {
    roleCode: [{ required: true, message: 'Not empty', trigger: "blur" }],
    roleName: [{ required: true,message: 'Not empty',  trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  queryParams.value.appId = appId;
  queryParams.value.appName = appName;
  listRoles(queryParams.value).then((res: any) =>  {
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


/** 成员 */
function handleAuthUser(row: any): any {
  authUserInfoRef.value.authByGroupId(row.id,appId)
}

/** 访问权限 */
function handleAuthResource(row: any): any {
  authResourceRef.value.authByRole(row.id,row.roleName,appId,appName)
}


/** 多选框选中数据 */
function handleSelectionChange(selection: any): any {
  ids.value = selection.map((item: any) =>  item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}


/** 重置新增的表单以及其他数据  */
function reset(): any {
  form.value ={
    id: undefined,
    appId: undefined,
    appName: undefined,
    roleCode: undefined,
    roleName:undefined,
    status: 1,
    category: 'static',
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
  getRoles(id).then((res: any) =>  {
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
        form.value.appId = appId;
        form.value.appName = appName;
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
