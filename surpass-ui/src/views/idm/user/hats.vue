<template>
  <el-drawer v-model="dialogStatus" :close-on-click-modal="false" size="55%"
             @close="dialogOfClosedMethods(false)">
    <template #header>
      <h4>{{ title }}</h4>
    </template>
    <template #default>
      <div class="button-top">
        <el-button
            @click="handleAdd"
            type="primary">
          {{ $t('jbx.text.add') }}
        </el-button>
      </div>
      <el-table v-loading="loading" :data="hatsList" border>
        <el-table-column prop="jobTitle" :label="$t('jbx.users.jobTitle')" min-width="90" align="center"/>
        <el-table-column prop="departmentId" :label="$t('jbx.users.departmentId')" min-width="90" align="center"/>
        <el-table-column prop="department" :label="$t('jbx.users.department')" min-width="90" align="center"/>
        <el-table-column prop="status" :label="$t('jbx.text.status.status')" min-width="50" align="center">
          <template #default="scope">
            <a :title="$t('jbx.users.statusActive')" v-if="scope.row.status === 1">
              <el-icon color="green">
                <SuccessFilled
                    class="success"/>
              </el-icon>
            </a>
            <a :title="$t('jbx.users.statusInactive')" v-if="scope.row.status === 2">
              <el-icon color="grey">
                <WarningFilled/>
              </el-icon>
            </a>
            <a :title="$t('jbx.users.statusDelete')" v-if="scope.row.status === 9">
              <el-icon color="red">
                <CircleCloseFilled/>
              </el-icon>
            </a>
          </template>
        </el-table-column>
        <el-table-column :label="$t('jbx.text.action')" min-width="90" align="center">
          <template #default="scope">
            <el-button @click.stop="handleUpdate(scope.row)"
            >{{ $t('jbx.text.edit') }}
            </el-button>
            <el-button type="danger" @click="onDelete(scope.row)">
              {{$t("jbx.text.delete")}}
            </el-button>
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

      <el-dialog v-model="editFlag" width="800px" append-to-body :title="editTitle">
        <el-form :model="form" ref="hatRef" :rules="rules" label-width="120px"
                 inline-message v-loading="formLoading">
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.departmentId')">
                <el-input v-model="form.departmentId"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.department')">
                <el-input v-model="form.department"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.jobTitle')">
                <el-input v-model="form.jobTitle"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.jobLevel')">
                <el-input v-model="form.jobLevel"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.organization')">
                <el-input v-model="form.organization"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.division')">
                <el-input v-model="form.division"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.manager')">
                <el-input v-model="form.manager"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.assistant')">
                <el-input v-model="form.assistant"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.costCenter')">
                <el-input v-model="form.costCenter"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workOfficeName')">
                <el-input v-model="form.workOfficeName"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.entryDate')">
                <el-input v-model="form.entryDate"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.quitDate')">
                <el-input v-model="form.quitDate"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workPhoneNumber')">
                <el-input v-model="form.workPhoneNumber"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workEmail')">
                <el-input v-model="form.workEmail"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workCountry')">
                <el-input v-model="form.workCountry"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workRegion')">
                <el-input v-model="form.workRegion"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workLocality')">
                <el-input v-model="form.workLocalityworkLocality"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="t('jbx.users.workStreetAddress')">
                <el-input v-model="form.workStreetAddress"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="gutter">
            <el-col :span="12">
              <el-form-item :label="$t('jbx.users.workPostalCode')">
                <el-input v-model="form.workPostalCode"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label="$t('jbx.users.workFax')">
                <el-input v-model="form.workFax"></el-input>
              </el-form-item>
            </el-col>
          </el-row>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="updateHats" type="primary">{{ $t('jbx.text.confirm') }}</el-button>
          <el-button @click="cancel">{{ $t('systemCancel') }}</el-button>
        </div>
      </el-dialog>
    </template>
  </el-drawer>
</template>
<script setup lang="ts">
import modal from "@/plugins/modal";

import {ref, getCurrentInstance, reactive, toRefs, watch, defineComponent} from "vue";;
import {useI18n} from "vue-i18n";
import {addOneUserHat, deleteUserHat, getHatsDetail, listHats, updateOneUserHat} from "@/api/system/user";

const {t} = useI18n()

const {proxy} = getCurrentInstance()!;
const emit: any = defineEmits(['hatsDrawerClose'])

const props: any = defineProps({
      title: {
        type: String,
        default: ""
      },
      open: {
        type: Boolean,
        default: false
      },
      formId: {
        type: String,
        default: null
      },
    }
)

const data: any = reactive({
  queryParams: {
    assign: 'owns',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  },
  form: {},
  rules: {}
})
const {queryParams, form, rules} = toRefs(data)
const dialogStatus: any = ref(false);
const hatsList: any = ref<any>([]);
const total: any = ref(0);
const loading: any = ref(true);
const editFlag: any = ref(false);
const editTitle: any = ref('');
const gutter: any = ref(20);
const formLoading: any = ref(true)

// 监听 open 变化
watch(
    () => props.open,
    (val: any) => {
      if (val && props.formId) {
        dialogStatus.value = props.open
        queryParams.value.userId = props.formId;
        getList();
      } else {
        reset();
      }
    },
    {immediate: true}
);

function dialogOfClosedMethods(val: any): any {
  dialogStatus.value = false;
  emit('hatsDrawerClose', val);
}

function reset(): any {
  queryParams.value = {
    assign: 'owns',
    pageSize: 5,
    pageNumber: 1,
    pageSizeOptions: [5, 10, 20]
  };
  loading.value = true;
}

function getList(): any {
  listHats(queryParams.value).then((res: any) =>  {
    if (res.code === 0) {
      hatsList.value = res.data.records;
      total.value = res.data.total;
      loading.value = false;
    }
  })
}

function handleUpdate(row: any): any {
  formLoading.value = true;
  let id: any = row.id;
  getHatsDetail(id).then((res: any) =>  {
    if (res.code === 0) {
      form.value = res.data;
      formLoading.value = false;
      if (!formLoading.value) {
        editTitle.value = t('jbx.text.edit')
        editFlag.value = true;
      }
    } else {
      modal.msgError("未获取数据");
    }
  })
}

function handleAdd(): any {
  formLoading.value = false;
  editTitle.value = t('jbx.text.add')
  editFlag.value = true;
  form.value = {}
  hatRef?.value?.resetFields();;
}

function onDelete(row: any): any {
  let id: any = row.id;

  modal.confirm(t('org.deleteTip1') + row.jobTitle + t('org.deleteTip2')).then(function () {
    return deleteUserHat(id);
  }).then((res: any) =>  {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(t('jbx.alert.delete.success'));
    } else {
      modal.msgError(t('jbx.alert.delete.error'));
    }
  }).catch(() => {
  });
}
//新增或修改
function updateHats(): any {
  const isAddOperation: any = !form.value.id;
  if (isAddOperation) {
    form.value.status = 1;
    form.value.userId = props.formId;
  }
  const apiCall: any = isAddOperation ? addOneUserHat(form.value) : updateOneUserHat(form.value);
  const successMessage: any = isAddOperation ? t('jbx.alert.add.success') : t('jbx.alert.update.success');
  const errorMessage: any = isAddOperation ? t('jbx.alert.add.error') : t('jbx.alert.update.error');

  apiCall.then((res: any) =>  {
    if (res.code === 0) {
      getList();
      modal.msgSuccess(successMessage);
    } else {
      modal.msgError(errorMessage);
    }
  });

  editFlag.value = false;
}

function cancel(): any {
  editFlag.value = false;
}
</script>

<style lang="scss" scoped>
.button-top {
  margin-bottom: 10px;
}
</style>
