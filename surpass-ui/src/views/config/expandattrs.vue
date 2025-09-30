<template>
  <div class="app-container">
    <el-card class="common-card">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch">
      <el-form-item :label="$t('jbx.expandattrs.attr') + '：'" prop="attr">
        <el-input
          v-model="queryParams.attr"
          placeholder=""
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item><!--  style="width: 240px" -->
      <el-form-item :label="$t('jbx.apps.adapterName') + '：'" prop="attrName">
        <el-input
          v-model="queryParams.attrName"
          placeholder=""
          clearable
          style="width: 240px"
          @keyup.enter.native="handleQuery"
        /><!-- style="width: 240px" -->
      </el-form-item>
      <el-form-item :label="$t('jbx.expandattrs.resources') + '：'" prop="resources">
        <el-select v-model="queryParams.resources" placeholder="" clearable   style="width: 240px">
          <el-option
            v-for="dict in extend_obj"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button size="small" @click="handleQuery">{{$t('jbx.text.query')}}</el-button><!-- type="primary" -->
        <el-button size="small" @click="resetQuery">{{$t('jbx.text.reset')}}</el-button>
      </el-form-item>
    </el-form>
  </el-card>

   <el-card class="common-card">
   <div class="btn-form">
    <!-- <el-row :gutter="10" class="mb8">
      <el-col :span="1.5"> -->
        <el-button
          type="primary"
          size="small"
          @click="handleAdd"
        >{{$t('jbx.text.add')}}
        </el-button><!--  plain
          icon="el-icon-plus" -->
     <!--  </el-col>
      <el-col :span="1.5"> -->
        <el-button
          type="danger"
          plain
          size="small"
          :disabled="multiple"
          @click="handleDelete"
        >{{$t('jbx.text.delete')}}
        </el-button><!--  icon="el-icon-delete" -->
     <!--  </el-col> -->

    <!--   <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row> -->
    </div>
    <el-table v-loading="loading" :data="list" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column :label="$t('jbx.expandattrs.attr')" align="center" prop="attr"/>
      <el-table-column :label="$t('jbx.apps.adapterName')" align="center" prop="attrName"/>
      <el-table-column :label="$t('jbx.expandattrs.type')" align="center" prop="type">
        <template #default="scope">
          <dict-tag :options="data_type" :value="scope.row.type"></dict-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.expandattrs.default')" align="center" prop="defaultValue"/>
      <el-table-column :label="$t('jbx.text.sortIndex')" align="center" prop="sortIndex"/>
      <el-table-column :label="$t('jbx.expandattrs.resources')" align="center" prop="resources">
        <template #default="scope">
          <dict-tag :options="extend_obj" :value="scope.row.resources"></dict-tag>
        </template>
      </el-table-column>
      <el-table-column :label="$t('jbx.history.systemlogsMessageaction')" align="center"
                       class-name="small-padding fixed-width" width="200" >
        <template #default="scope">
          <el-button

            @click="handleUpdate(scope.row)"
          >{{$t('jbx.text.edit')}}
          </el-button><!--  plain
            type="primary" -->
          <el-button
            type="danger"
            @click="handleDelete(scope.row)"
          >{{$t('jbx.text.delete')}}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNumber"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </el-card>

    <!-- 添加或修改参数配置对话框 -->
    <el-dialog :title="title" v-model="open" width="600px" append-to-body>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item :label="$t('jbx.expandattrs.attr')" prop="attr">
          <el-input v-model="form.attr" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.apps.adapterName')" prop="attrName">
          <el-input v-model="form.attrName" placeholder=""/>
        </el-form-item>
        <el-form-item :label="$t('jbx.expandattrs.type')" prop="type">
          <el-select v-model="form.type" placeholder="" clearable style="width: 100%">
            <el-option
              v-for="dict in data_type"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('jbx.expandattrs.default')" prop="defaultValue">
          <el-input v-model="form.defaultValue" placeholder=""/>
        </el-form-item>

        <el-form-item :label="$t('jbx.expandattrs.resources')" prop="resources">
          <el-select v-model="form.resources" placeholder="" clearable style="width: 100%">
            <el-option
              v-for="dict in extend_obj"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item :label="$t('jbx.text.sortIndex')" prop="sortIndex">
          <el-input-number v-model="form.sortIndex" placeholder=""/>
        </el-form-item>

        <el-form-item :label="$t('jbx.text.description')" prop="description">
          <el-input type="textarea" rows="3"  maxlength="200" show-word-limit v-model="form.description" placeholder=""/>
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

<script setup name="Config-expandattrs" lang="ts">
import { ElForm } from "element-plus";
import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";
import modal from "@/plugins/modal";
import {
  getExpandattrs,
  listExpandattrs,
  addExpandattrs,
  updateExpandattrs,
  delExpandattrs
} from "@/api/config/expandattrs";

import {useI18n} from "vue-i18n";
const {proxy} = getCurrentInstance()!;
const formRef = ref<InstanceType<typeof ElForm> | null>(null);
const queryRef = ref<InstanceType<typeof ElForm> | null>(null);
const { t } = useI18n()
const { data_type,extend_obj } = proxy.useDict("data_type","extend_obj");

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
    attr: [{ required: true, message: 'Not empty', trigger: "blur" }],
    attrName: [{ required: true,message: 'Not empty',  trigger: "blur" }],
    type: [{ required: true,message: 'Not empty',  trigger: "blur" }],
    resources: [{ required: true,message: 'Not empty',  trigger: "blur" }],
  },
});

const { queryParams, form, rules } = toRefs(data);

/** 分页列表 */
function getList(): any {
  loading.value = true;
  listExpandattrs(queryParams.value).then((res: any) =>  {
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
    return delExpandattrs(id);
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
  getExpandattrs(id).then((res: any) =>  {
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
        updateExpandattrs(form.value).then((response: any) =>  {
          modal.msgSuccess(t('jbx.alert.operate.success'));
          open.value = false;
          getList();
        });
      } else {
        addExpandattrs(form.value).then((response: any) =>  {
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
