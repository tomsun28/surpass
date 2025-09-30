<template>
  <div class="app-container">
    <el-card class="common-card query-box">
      <div class="queryForm">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true"
                 v-show="showSearch" label-width="68px">
          <el-form-item label="" prop="assistType">
            <el-radio-group v-model="queryParams.assistType" @change="handleQuery">
              <el-radio-button v-for="(aux, index) in subjects_auxiliary"
                               :key="index"
                               :label="aux.label" :value="aux.value"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="编码" prop="assistCode">
            <el-input
                v-model="queryParams.assistCode"
                placeholder="请输入编码"
                clearable
                @keyup.enter="handleQuery"
            />
          </el-form-item>
          <el-form-item label="名称" prop="assistName">
            <el-input
                v-model="queryParams.assistName"
                placeholder="请输入名称"
                clearable
                @keyup.enter="handleQuery"
            />
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
        <el-button
            type="primary"
            @click="handleAdd"
        >{{ t('org.button.add') }}
        </el-button>
        <el-button
            type="danger"
            :disabled="ids.length === 0"
            @click="handleDelete"
        >{{ t('org.button.deleteBatch') }}
        </el-button>
      </div>
      <el-table v-loading="loading" border :data="assistList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"/>
        <el-table-column prop="assistCode" label="编码" align="center" width="110"></el-table-column>
        <el-table-column prop="assistName" label="名称" align="center"></el-table-column>
        <el-table-column v-if="queryParams.assistType === '5'"
                         prop="dept" label="部门" align="center">
          <template #default="scope">
            <span>{{ assistDeptMap[scope.row.dept] }}</span>
          </template>
        </el-table-column>
        <el-table-column v-if="queryParams.assistType === '6'"
                         prop="spec" label="规格" align="center"></el-table-column>
        <el-table-column v-if="queryParams.assistType === '6'"
                         prop="unit" label="单位" align="center"></el-table-column>
        <el-table-column prop="remark" label="备注" align="center"></el-table-column>
        <el-table-column prop="status" label="是否禁用" align="center">
          <template #default="scope">
            <dict-tag :options="sys_disable" :value="scope.row.status"/>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" fixed="right" width="80">
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
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
      />
    </el-card>

    <el-drawer v-model="dialog.visible" :close-on-click-modal="false" size="40%">
      <template #header>
        <h4>{{ dialog.title }}</h4>
      </template>
      <template #default>
        <el-form :model="form" :rules="rules" ref="assistAccRef" label-width="120px"
                 inline-message>
          <el-form-item label="编码" prop="assistCode">
            <el-input v-model="form.assistCode" :placeholder="'请输入编码,建议以'+form.assistCodePlaceholder+'开头'"/>
          </el-form-item>

          <el-form-item label="名称" prop="assistName">
            <el-input v-model="form.assistName" placeholder="请输入名称"></el-input>
          </el-form-item>

          <el-form-item v-if="form.assistType === '5'" label="部门" prop="dept">
            <el-select v-model="form.dept">
              <el-option v-for="item in assistDeptList" :label="item.assistName" :value="item.assistCode"/>
            </el-select>
          </el-form-item>

          <el-form-item v-if="form.assistType === '6'" label="规格" prop="spec">
            <el-input v-model="form.spec" placeholder="请输入规格"></el-input>
          </el-form-item>

          <el-form-item v-if="form.assistType === '6'" label="单位" prop="unit">
            <el-input v-model="form.unit" placeholder="请输入单位"></el-input>
          </el-form-item>

          <el-form-item label="备注" prop="remark">
            <el-input type="textarea" v-model="form.remark" placeholder="请输入备注"></el-input>
          </el-form-item>

          <el-form-item label="是否禁用" prop="status">
            <el-select v-model="form.status" placeholder="请选择是否禁用">
              <el-option v-for="item in sys_disable" :label="item.label" :value="item.value"/>
            </el-select>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <div style="flex: auto">
          <el-button @click="dialog.visible = false">{{ t('org.cancel') }}</el-button>
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">{{ t('org.confirm') }}</el-button>
        </div>
      </template>
    </el-drawer>
  </div>
</template>

<script setup lang="ts" name="AssistAcc">
import {getAssistAcc, listAssistAcc, addAssistAcc, delAssistAcc, updateAssistAcc} from "@/api/config/assistAcc";
import {useI18n} from "vue-i18n";
import bookStore from "@/store/modules/bookStore";
import {reactive, ref, toRefs, getCurrentInstance} from "vue";
import modal from "@/plugins/modal";
import {ElForm, FormInstance} from "element-plus";
import SvgIcon from "@/components/SvgIcon/index.vue";

const {proxy} = getCurrentInstance()!;
const {sys_disable, subjects_auxiliary} = toRefs<any>(proxy?.useDict('sys_disable', "subjects_auxiliary"));
const {t} = useI18n()
const currBookStore = bookStore()
const assistList = ref<any>([]);
const assistDeptList = ref<any>([]);
const assistDeptMap = ref<any>({});
const loading = ref(true);
const buttonLoading = ref(false);
const showSearch = ref(true);
const ids = ref([]);
const selectionList: any = ref<any>([]);
const total = ref(0);

const assistAccRef = ref<FormInstance>();
const queryFormRef = ref<FormInstance>();

const dialog = reactive<any>({
  visible: false,
  title: ''
});
const initFormData = {
  bookId: currBookStore.bookId,
  assistType: undefined, // 辅助核算类型
  assistCode: undefined, // 辅助核算编码
  assistCodePlaceholder: undefined, // 辅助核算编码
  assistName: undefined, // 辅助核算名称
  spec: undefined, // 规格
  unit: undefined, // 单位
  remark: undefined, // 备注
  status: "n" // 是否禁用：y/n
}
const data: any = reactive({
  form: {...initFormData},
  queryParams: {
    bookId: currBookStore.bookId,
    pageNumber: 1,
    pageSize: 10,
    orderByColumn: "status",
    isAsc: "asc",
    assistType: "1", // 类型
    assistCode: "", // 编码
    assistName: "", // 名称
  },
  rules: {
    assistType: [
      {required: true, message: '辅助核算类型不能为空', trigger: 'blur'}
    ],
    assistCode: [
      {required: true, message: '辅助核算编码不能为空', trigger: 'blur'}
    ],
    assistName: [
      {required: true, message: '辅助核算名称不能为空', trigger: 'blur'}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询辅助核算列表 */
function getList() {
  loading.value = true;
  listAssistAcc(queryParams.value).then((response: any) => {
    assistList.value = response.data.records;
    total.value = response.data.total;
    loading.value = false;
  });
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  assistAccRef.value?.resetFields();
  form.value.assistType = queryParams.value.assistType;
};

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
function resetQuery() {
  queryFormRef.value?.resetFields();
  handleQuery();
}

// 多选框选中数据
function handleSelectionChange(selection: any) {
  selectionList.value = selection;
  ids.value = selectionList.value.map((item: any) => item.id);
}

const getAssistDeptList = () => {
  assistDeptList.value.length = 0
  listAssistAcc({
    bookId: currBookStore.bookId,
    pageNumber: 1,
    pageSize: 1000,
    orderByColumn: "status",
    isAsc: "asc",
    assistType: "4",
  }).then((response: any) => {
    assistDeptList.value = response.data.records;
    assistDeptList.value.forEach((item: any) => {
      assistDeptMap.value[item.assistCode] = item.assistName
    })
  });
};

/** 新增按钮操作 */
function handleAdd() {
  reset();
  console.log("assistType "+form.value.assistType);
  form.value.assistCodePlaceholder = form.value.assistType;
  dialog.visible = true;
  dialog.title = "添加辅助核算";
  getAssistDeptList()
}

/** 修改按钮操作 */
const handleUpdate = async (row?: any) => {
  reset();
  const id = row?.id
  const res = await getAssistAcc(id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = '修改设备信息';
  getAssistDeptList()
};

/** 提交按钮 */
const submitForm = () => {
  assistAccRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateAssistAcc(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addAssistAcc(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess('操作成功');
      dialog.visible = false;
      await getList();
    }
  });
};

/** 删除按钮操作 */
function handleDelete(row: any) {
  const _ids = row.id || ids.value;
  modal.confirm('是否确认删除辅助核算编号为"' + _ids + '"的数据项？').then(function () {
    return delAssistAcc({listIds: [_ids]});
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

getList();
getAssistDeptList()
</script>

<style lang="scss" scoped>
.btn-form {
  margin-bottom: 10px;
}

.common-card {
  margin-bottom: 15px;
}

.app-container {
  padding: 0;
  background-color: #f5f7fa;
}
</style>
